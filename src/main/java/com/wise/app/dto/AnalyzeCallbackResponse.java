// src/main/java/com/wise/app/dto/AnalyzeCallbackResponse.java
package com.wise.app.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class AnalyzeCallbackResponse {
    private String id;      // 저장된 문서 ID (= traceId)
    private String status;  // "OK"
}
