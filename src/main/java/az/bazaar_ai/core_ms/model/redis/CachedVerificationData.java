package az.bazaar_ai.core_ms.model.redis;

import java.time.LocalDateTime;

import az.bazaar_ai.core_ms.util.enums.UserRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CachedVerificationData {

    @ToString.Exclude
    String hashedToken;

    @ToString.Exclude
    String userEmail;

    @ToString.Exclude
    String hashedPassword;

    String firstName;
    String lastName;

    String phone;

    UserRole userRole;

    int attemptCount;

    LocalDateTime expiryDate;
}