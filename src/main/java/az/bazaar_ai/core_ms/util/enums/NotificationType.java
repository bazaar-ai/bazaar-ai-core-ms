package az.bazaar_ai.core_ms.util.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    VERIFICATION_CODE("verification-email", "Your Shopery Verification Code");

    private final String templateName;
    private final String subject;
}
