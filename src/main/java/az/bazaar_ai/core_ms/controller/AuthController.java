package az.bazaar_ai.core_ms.controller;

import az.bazaar_ai.core_ms.model.dto.auth.AuthResponse;
import az.bazaar_ai.core_ms.model.dto.auth.LoginRequest;
import az.bazaar_ai.core_ms.model.dto.auth.RegisterRequest;
import az.bazaar_ai.core_ms.model.dto.auth.VerificationRequest;
import az.bazaar_ai.core_ms.model.dto.shared.SuccessResponse;
import az.bazaar_ai.core_ms.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<Void>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/verify")
    public ResponseEntity<SuccessResponse<AuthResponse>> verify(@Valid @RequestBody VerificationRequest verificationRequest) {
        return ResponseEntity.ok(authService.verify(verificationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}
