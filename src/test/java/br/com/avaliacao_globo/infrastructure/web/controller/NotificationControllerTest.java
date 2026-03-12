package br.com.avaliacao_globo.infrastructure.web.controller;

import br.com.avaliacao_globo.exception.MessagePublishException;
import br.com.avaliacao_globo.infrastructure.messaging.publisher.NotificationPublisher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationPublisher notificationPublisher;

    @Test
    @DisplayName("Should return 202 and publish message when request is valid")
    void shouldReturnAcceptedWhenRequestIsValid() throws Exception {
        String requestBody = """
                {
                  "subscriptionId": "sub_123",
                  "eventType": "SUBSCRIPTION_PURCHASED"
                }
                """;

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isAccepted());

        verify(notificationPublisher, times(1)).publish(any());
    }

    @Test
    @DisplayName("Should return 400 when subscriptionId is blank")
    void shouldReturnBadRequestWhenSubscriptionIdIsBlank() throws Exception {
        String requestBody = """
                {
                  "subscriptionId": "",
                  "eventType": "SUBSCRIPTION_PURCHASED"
                }
                """;

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when eventType is invalid")
    void shouldReturnBadRequestWhenEventTypeIsInvalid() throws Exception {
        String requestBody = """
                {
                  "subscriptionId": "sub_123",
                  "eventType": "INVALID_EVENT"
                }
                """;

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when eventType is null")
    void shouldReturnBadRequestWhenEventTypeIsNull() throws Exception {
        String requestBody = """
                {
                  "subscriptionId": "sub_123",
                  "eventType": null
                }
                """;

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when request body is missing required fields")
    void shouldReturnBadRequestWhenRequestBodyIsInvalid() throws Exception {
        String requestBody = """
                {
                }
                """;

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnServiceUnavailableWhenPublishingFails() throws Exception {
        String requestBody = """
            {
              "subscriptionId": "sub_123",
              "eventType": "SUBSCRIPTION_PURCHASED"
            }
            """;

        doThrow(new MessagePublishException("Failed to publish message to RabbitMQ", new RuntimeException()))
                .when(notificationPublisher)
                .publish(any());

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void shouldReturnBadRequestWhenJsonIsMalformed() throws Exception {
        String requestBody = """
            {
              "subscriptionId": "sub_123",
              "eventType": "SUBSCRIPTION_PURCHASED"
            """;

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenSubscriptionIdHasInvalidType() throws Exception {
        String requestBody = """
            {
              "subscriptionId": 123,
              "eventType": "SUBSCRIPTION_PURCHASED"
            }
            """;

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}