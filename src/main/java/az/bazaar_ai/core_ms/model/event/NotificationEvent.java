package az.bazaar_ai.core_ms.model.event;

import az.bazaar_ai.core_ms.util.enums.NotificationType;
import java.util.Map;

public record NotificationEvent(
        String to,
        NotificationType type,
        Map<String, Object> params) {
}
