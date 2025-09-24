// src/main/java/com/wise/app/service/RecommendService.java
package com.wise.app.service;

import com.wise.app.dto.RecommendByIngredientsRequest;
import com.wise.app.dto.RecipeItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendService {
    private final WebClient wsiePyWebClient;

    public List<RecipeItem> recommendByIngredients(RecommendByIngredientsRequest req) {
        try {
            return wsiePyWebClient.post()
                    .uri("/api/v1/recommend/by-ingredients")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(req) // snake_case 필드 그대로 전달
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, resp ->
                        resp.bodyToMono(String.class).flatMap(b -> Mono.error(new RuntimeException("PY error: " + b))))
                    .bodyToFlux(RecipeItem.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Recommend proxy failed: " + e.getMessage(), e);
        }
    }
}
