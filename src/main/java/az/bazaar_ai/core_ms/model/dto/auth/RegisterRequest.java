package az.bazaar_ai.core_ms.model.dto.auth;

import az.bazaar_ai.core_ms.util.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class RegisterRequest {

    @NotBlank(message = "firstName cannot be empty!")
    String firstName;

    @NotBlank(message = "lastName cannot be empty!")
    String lastName;

    @NotBlank(message = "phone cannot be empty!")
    String phone;

    @Email(message = "email is not valid!")
    @NotBlank(message = "email cannot be empty!")
    String email;

    @NotBlank(message = "password cannot be empty!")
    String password;

    @NotNull(message = "userRole cannot be empty!")
    UserRole userRole;
}