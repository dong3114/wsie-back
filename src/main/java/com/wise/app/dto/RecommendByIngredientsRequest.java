package com.wise.app.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RecommendByIngredientsRequest {
    @NotNull @Size(min = 1)
    private List<@NotBlank String> ingredients;

    @Min(1) @Max(20)
    private Integer top_k = 3; // Python 키에 맞춤 (snake_case)
}
