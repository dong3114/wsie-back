package com.wise.app.controller;

import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SystemController {
    private final Firestore firestore;
    private final WebClient webClient; // 이미 Bean 구성되어 있다고 가정

    @GetMapping("/health")
    public Map<String,Object> health() {
        return Map.of("ok", true, "ts", Instant.now().toString());
    }

    @GetMapping("/ready")
    public ResponseEntity<?> ready() {
        try {
            // Firestore ping
            firestore.listCollections(); // 예외 안나면 OK

            // Python ping
            String py = webClient.get().uri("http://localhost:8000/health")
                    .retrieve().bodyToMono(String.class).blockOptional().orElse("OK");
            return ResponseEntity.ok(Map.of("firestore","OK","python",py));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
