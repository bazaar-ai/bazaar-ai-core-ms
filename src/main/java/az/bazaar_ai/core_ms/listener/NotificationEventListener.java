package az.bazaar_ai.core_ms.listener;

import az.bazaar_ai.core_ms.model.event.NotificationEvent;
import az.bazaar_ai.core_ms.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final EmailService emailService;

    @Async
    @EventListener
    public void onNotification(NotificationEvent notificationEvent) {
        emailService.sendNotification(notificationEvent);
    }
}
