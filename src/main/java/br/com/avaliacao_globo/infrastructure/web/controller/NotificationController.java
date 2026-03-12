package br.com.avaliacao_globo.infrastructure.web.controller;

import br.com.avaliacao_globo.application.dto.request.NotificationRequest;
import br.com.avaliacao_globo.infrastructure.messaging.publisher.NotificationPublisher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationPublisher notificationPublisher;

    @PostMapping
    public ResponseEntity<Void> receiveNotification(@Valid @RequestBody NotificationRequest request) {
        notificationPublisher.publish(request);
        return ResponseEntity.accepted().build();
    }
}
