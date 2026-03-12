package br.com.avaliacao_globo.application.dto.message;

import jakarta.validation.constraints.NotBlank;

public record NotificationEventMessage(
        @NotBlank
        String subscriptionId,
        @NotBlank
        String eventType
) {
}