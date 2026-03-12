package br.com.avaliacao_globo.infrastructure.publisher;

import br.com.avaliacao_globo.application.dto.message.NotificationEventMessage;
import br.com.avaliacao_globo.application.dto.request.NotificationRequest;
import br.com.avaliacao_globo.config.RabbitMQConfig;
import br.com.avaliacao_globo.domain.enums.EventType;
import br.com.avaliacao_globo.infrastructure.messaging.publisher.NotificationPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NotificationPublisherTest {

    private RabbitTemplate rabbitTemplate;
    private NotificationPublisher notificationPublisher;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        notificationPublisher = new NotificationPublisher(rabbitTemplate);
    }

    @Test
    @DisplayName("Should publish NotificationEventMessage to RabbitMQ")
    void shouldPublishMessageToRabbitMQ() {
        NotificationRequest request = new NotificationRequest(
                "sub_123",
                EventType.SUBSCRIPTION_PURCHASED
        );

        notificationPublisher.publish(request);

        ArgumentCaptor<NotificationEventMessage> messageCaptor =
                ArgumentCaptor.forClass(NotificationEventMessage.class);

        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.SUBSCRIPTION_QUEUE),
                messageCaptor.capture()
        );

        NotificationEventMessage capturedMessage = messageCaptor.getValue();

        assertEquals("sub_123", capturedMessage.subscriptionId());
        assertEquals("SUBSCRIPTION_PURCHASED", capturedMessage.eventType());
    }
}