package az.bazaar_ai.core_ms.model.dto.user;

import az.bazaar_ai.core_ms.util.enums.UserRole;
import az.bazaar_ai.core_ms.util.enums.UserStatus;
import java.time.Instant;
import java.util.UUID;
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
public class UserDetailsResponse {

    UUID id;

    String name;
    String email;
    String phone;

    byte[] profilePhoto;

    UserStatus userStatus;

    UserRole userRole;

    Instant createdAt;
    Instant updatedAt;
}
