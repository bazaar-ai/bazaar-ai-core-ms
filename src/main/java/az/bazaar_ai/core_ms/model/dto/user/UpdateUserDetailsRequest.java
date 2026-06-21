package az.bazaar_ai.core_ms.model.dto.user;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
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
public class UpdateUserDetailsRequest {

    @NotBlank(message = "name cannot be empty!")
    String name;

    String city;

    @NotBlank(message = "phone cannot be empty!")
    String phone;

    String country;
    String address;

    @Past(message = "date of birth must be a past date!")
    LocalDate dateOfBirth;
}
