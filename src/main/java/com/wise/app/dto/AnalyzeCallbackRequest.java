// src/main/java/com/wise/app/dto/AnalyzeCallbackRequest.java
package com.wise.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class AnalyzeCallbackRequest {

    @NotBlank
    private String menu;

    @JsonProperty("waste_ratio")
    @NotNull
    private Double wasteRatio;

    @NotBlank
    private String suggestion;

    // 중복 방지/추적용 (Python에서 uuid 생성)
    @JsonProperty("trace_id")
    private String traceId;

    // 선택(있으면 저장)
    @JsonProperty("image_url")
    private String imageUrl;
}
