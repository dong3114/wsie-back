// com/wise/app/dto/AnalyzeResponse.java
package com.wise.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class AnalyzeResponse {
    @JsonProperty("menu")
    private String menu;

    @JsonProperty("waste_ratio")
    private Double wasteRatio;

    @JsonProperty("suggestion")
    private String suggestion;
}
