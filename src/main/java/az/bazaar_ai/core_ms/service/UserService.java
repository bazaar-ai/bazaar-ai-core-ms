package az.bazaar_ai.core_ms.service;

import static az.bazaar_ai.core_ms.util.constants.ErrorConstants.FAILED_TO_UPLOAD_PROFILE_PHOTO;
import static az.bazaar_ai.core_ms.util.constants.ErrorConstants.PHONE_ALREADY_EXISTS;
import static az.bazaar_ai.core_ms.util.constants.ErrorConstants.USER_NOT_FOUND;

import az.bazaar_ai.core_ms.handler.exception.ApplicationException;
import az.bazaar_ai.core_ms.handler.exception.ResourceNotFoundException;
import az.bazaar_ai.core_ms.model.dto.shared.SuccessResponse;
import az.bazaar_ai.core_ms.model.dto.user.UpdateUserDetailsRequest;
import az.bazaar_ai.core_ms.model.dto.user.UserDetailsResponse;
import az.bazaar_ai.core_ms.model.entity.User;
import az.bazaar_ai.core_ms.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public SuccessResponse<UserDetailsResponse> getUserDetails(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));

        return SuccessResponse.of(toUserDetailsResponse(user), "user details retrieved successfully!");
    }

    public SuccessResponse<Void> updateUserDetails(String email, UpdateUserDetailsRequest updateUserDetailsRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));

        if (userRepository.existsByPhone(updateUserDetailsRequest.getPhone())) {
            throw new ApplicationException(PHONE_ALREADY_EXISTS);
        }

        user.setName(updateUserDetailsRequest.getName());
        user.setPhone(updateUserDetailsRequest.getPhone());
        user.setCountry(updateUserDetailsRequest.getCountry());
        user.setCity(updateUserDetailsRequest.getCity());
        user.setAddress(updateUserDetailsRequest.getAddress());
        user.setDateOfBirth(updateUserDetailsRequest.getDateOfBirth());

        userRepository.save(user);

        return SuccessResponse.of("user details updated successfully!");
    }

    public SuccessResponse<Void> uploadProfilePhoto(String email, MultipartFile multipartFile) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));

        if (Objects.isNull(multipartFile) || multipartFile.isEmpty()) {
            user.setProfilePhoto(null);
            userRepository.save(user);
            return SuccessResponse.of("profile photo removed successfully!");
        }

        try {
            user.setProfilePhoto(multipartFile.getBytes());
            userRepository.save(user);
        } catch (Exception exception) {
            throw new ApplicationException(FAILED_TO_UPLOAD_PROFILE_PHOTO);
        }
        return SuccessResponse.of("profile photo uploaded successfully!");
    }

    private UserDetailsResponse toUserDetailsResponse(User user) {
        return UserDetailsResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userStatus(user.getUserStatus())
                .userRole(user.getUserRole())
                .phone(user.getPhone())
                .name(user.getName())
                .profilePhoto(user.getProfilePhoto())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
