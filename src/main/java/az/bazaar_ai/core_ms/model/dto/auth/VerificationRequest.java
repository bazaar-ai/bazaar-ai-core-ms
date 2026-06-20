package az.bazaar_ai.core_ms.model.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationRequest {

    @Email(message = "Email is not valid!")
    @NotBlank(message = "Email cannot be empty!")
    String email;

    @Pattern(regexp = "^[0-9]{6}$", message = "Code must be exactly 6 digits!")
    String code;
}
