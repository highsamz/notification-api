package br.com.avaliacao_globo.infrastructure.messaging.publisher;

import br.com.avaliacao_globo.application.dto.message.NotificationEventMessage;
import br.com.avaliacao_globo.application.dto.request.NotificationRequest;
import br.com.avaliacao_globo.config.RabbitMQConfig;
import br.com.avaliacao_globo.exception.MessagePublishException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(NotificationRequest request) {
        NotificationEventMessage message = new NotificationEventMessage(
                request.subscriptionId(),
                request.eventType().name()
        );

        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.SUBSCRIPTION_QUEUE, message);
        } catch (AmqpException ex) {
            throw new MessagePublishException("Failed to publish message to RabbitMQ", ex);
        }
    }
}