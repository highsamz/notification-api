package br.com.avaliacao_globo.application.dto.request;


import br.com.avaliacao_globo.domain.enums.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationRequest(

        @NotBlank
        String subscriptionId,

        @NotNull
        EventType eventType

) {}
