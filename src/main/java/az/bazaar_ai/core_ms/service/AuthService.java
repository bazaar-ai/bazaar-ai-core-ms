package az.bazaar_ai.core_ms.service;

import static az.bazaar_ai.core_ms.util.constants.ErrorConstants.EMAIL_ALREADY_EXISTS;
import static az.bazaar_ai.core_ms.util.constants.ErrorConstants.INVALID_VERIFICATION_TOKEN;
import static az.bazaar_ai.core_ms.util.constants.ErrorConstants.PENDING_VERIFICATION_NOT_FOUND;
import static az.bazaar_ai.core_ms.util.constants.ErrorConstants.VERIFICATION_TOKEN_EXPIRED;
import static az.bazaar_ai.core_ms.util.constants.EventConstants.USERNAME;
import static az.bazaar_ai.core_ms.util.constants.EventConstants.VERIFICATION_CODE;
import static az.bazaar_ai.core_ms.util.generators.OTPGenerator.generateSixDigitOTP;
import static az.bazaar_ai.core_ms.util.redis.RedisUtils.registerKey;
import static org.springframework.security.core.userdetails.User.withUsername;

import az.bazaar_ai.core_ms.handler.exception.ApplicationException;
import az.bazaar_ai.core_ms.handler.exception.InvalidCredentialsException;
import az.bazaar_ai.core_ms.handler.exception.ResourceNotFoundException;
import az.bazaar_ai.core_ms.model.dto.auth.AuthResponse;
import az.bazaar_ai.core_ms.model.dto.auth.RegisterRequest;
import az.bazaar_ai.core_ms.model.dto.auth.VerificationRequest;
import az.bazaar_ai.core_ms.model.dto.shared.SuccessResponse;
import az.bazaar_ai.core_ms.model.entity.User;
import az.bazaar_ai.core_ms.model.event.NotificationEvent;
import az.bazaar_ai.core_ms.model.redis.CachedVerificationData;
import az.bazaar_ai.core_ms.repository.UserRepository;
import az.bazaar_ai.core_ms.security.JwtService;
import az.bazaar_ai.core_ms.util.enums.NotificationType;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    public static final int MAX_FAILED_ATTEMPTS = 3;
    public static final int VERIFICATION_CODE_EXPIRY_MINUTES = 5;

    private final JwtService jwtService;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    public SuccessResponse<Void> register(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        log.info("register started for user: {}", email);

        if (userRepository.findByEmail(email).isPresent()) {
            throw new ApplicationException(EMAIL_ALREADY_EXISTS);
        }

        String password = passwordEncoder.encode(registerRequest.getPassword());
        String code = generateSixDigitOTP();

        CachedVerificationData cachedVerificationData = CachedVerificationData.builder()
                .hashedToken(passwordEncoder.encode(code))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .phone(registerRequest.getPhone())
                .userRole(registerRequest.getUserRole())
                .userEmail(email)
                .hashedPassword(password)
                .attemptCount(0)
                .expiryDate(LocalDateTime.now().plusMinutes(VERIFICATION_CODE_EXPIRY_MINUTES))
                .build();

        redisService.set(
                registerKey(email),
                cachedVerificationData,
                Duration.ofMinutes(VERIFICATION_CODE_EXPIRY_MINUTES)
        );

        applicationEventPublisher.publishEvent(new NotificationEvent(
                email,
                NotificationType.VERIFICATION_CODE,
                Map.of(
                        USERNAME, registerRequest.getFirstName(),
                        VERIFICATION_CODE, code
                )
        ));

        return SuccessResponse.of("verification code sent to your email successfully!");
    }

    public SuccessResponse<AuthResponse> verify(VerificationRequest verificationRequest) {
        String email = verificationRequest.getEmail();
        CachedVerificationData cachedVerificationData = redisService
                .get(registerKey(email), CachedVerificationData.class)
                .orElseThrow(() -> new ResourceNotFoundException(PENDING_VERIFICATION_NOT_FOUND));

        if (cachedVerificationData.getExpiryDate().isBefore(LocalDateTime.now())) {
            redisService.delete(registerKey(email));
            throw new InvalidCredentialsException(VERIFICATION_TOKEN_EXPIRED);
        }

        if (!passwordEncoder.matches(verificationRequest.getCode(), cachedVerificationData.getHashedToken())) {
            cachedVerificationData.setAttemptCount(cachedVerificationData.getAttemptCount() + 1);
            if (cachedVerificationData.getAttemptCount() >= MAX_FAILED_ATTEMPTS) {
                redisService.delete(registerKey(email));
                throw new InvalidCredentialsException(INVALID_VERIFICATION_TOKEN);
            }

            long remaining = Duration.between(LocalDateTime.now(), cachedVerificationData.getExpiryDate()).toMinutes();
            redisService.set(
                    registerKey(email),
                    cachedVerificationData,
                    Duration.ofMinutes(Math.max(remaining, 1))
            );

            throw new InvalidCredentialsException(INVALID_VERIFICATION_TOKEN);
        }

        var user = User.builder()
                .firstName(cachedVerificationData.getFirstName())
                .lastName(cachedVerificationData.getLastName())
                .userRole(cachedVerificationData.getUserRole())
                .phone(cachedVerificationData.getPhone())
                .email(cachedVerificationData.getUserEmail())
                .password(cachedVerificationData.getHashedPassword())
                .build();
        userRepository.save(user);

        redisService.delete(registerKey(email));

        return SuccessResponse.of(generateAuthResponse(user), "account verified successfully");
    }

    private AuthResponse generateAuthResponse(User user) {
        var UserDetails = withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getUserRole().name())
                .build();

        var accessToken = jwtService.generateToken(UserDetails);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
