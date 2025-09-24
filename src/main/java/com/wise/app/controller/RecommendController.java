// src/main/java/com/wise/app/controller/RecommendController.java
package com.wise.app.controller;

import com.wise.app.dto.RecommendByIngredientsRequest;
import com.wise.app.dto.RecipeItem;
import com.wise.app.service.RecommendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
public class RecommendController {

    private final RecommendService service;

    @PostMapping("/by-ingredients")
    public ResponseEntity<List<RecipeItem>> byIngredients(@Valid @RequestBody RecommendByIngredientsRequest req) {
        return ResponseEntity.ok(service.recommendByIngredients(req));
    }
}
