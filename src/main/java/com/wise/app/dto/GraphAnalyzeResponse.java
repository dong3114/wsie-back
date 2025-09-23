package com.wise.app.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GraphAnalyzeResponse {
    private String report;
    private List<Improvement> improvements;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Improvement {
        private String suggestion; // Python improvements[i].suggestion
    }
}
