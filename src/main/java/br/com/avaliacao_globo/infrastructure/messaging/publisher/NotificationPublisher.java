package br.com.avaliacao_globo.infrastructure.messaging.publisher;

import br.com.avaliacao_globo.application.dto.request.NotificationRequest;
import br.com.avaliacao_globo.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(NotificationRequest notificationRequest) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.SUBSCRIPTION_QUEUE, notificationRequest);
    }
}
