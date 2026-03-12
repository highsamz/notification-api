package br.com.avaliacao_globo.infrastructure.messaging.publisher;

import br.com.avaliacao_globo.application.dto.message.NotificationEventMessage;
import br.com.avaliacao_globo.application.dto.request.NotificationRequest;
import br.com.avaliacao_globo.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(NotificationRequest request) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.SUBSCRIPTION_QUEUE,
                toMessage(request)
        );
    }

    private NotificationEventMessage toMessage(NotificationRequest request) {
        return new NotificationEventMessage(
                request.subscriptionId(),
                request.eventType().name()
        );
    }
}
