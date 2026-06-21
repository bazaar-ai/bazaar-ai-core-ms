package az.bazaar_ai.core_ms.controller;

import az.bazaar_ai.core_ms.model.dto.shared.SuccessResponse;
import az.bazaar_ai.core_ms.model.dto.user.UpdateUserDetailsRequest;
import az.bazaar_ai.core_ms.model.dto.user.UserDetailsResponse;
import az.bazaar_ai.core_ms.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/me")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<SuccessResponse<UserDetailsResponse>> getUserDetails(Principal principal) {
        return ResponseEntity.ok(userService.getUserDetails(principal.getName()));
    }

    @PutMapping
    public ResponseEntity<SuccessResponse<Void>> updateUserDetails(Principal principal, @Valid @RequestBody UpdateUserDetailsRequest updateUserDetailsRequest) {
        return ResponseEntity.ok(userService.updateUserDetails(principal.getName(), updateUserDetailsRequest));
    }

    @PostMapping(value = "/upload-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<Void>> uploadPhotoProfile(Principal principal, @RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        return ResponseEntity.ok(userService.uploadProfilePhoto(principal.getName(), multipartFile));
    }
}
