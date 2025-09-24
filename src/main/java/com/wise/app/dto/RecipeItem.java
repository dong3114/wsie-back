// src/main/java/com/wise/app/dto/RecipeItem.java
package com.wise.app.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RecipeItem {
    private String title;
    private String summary;
    private Integer recipe_id;     // Python 키에 맞춤 (snake_case)
    private List<String> ingredients;
    private String full_recipe;    // Python 키에 맞춤 (snake_case)
}
