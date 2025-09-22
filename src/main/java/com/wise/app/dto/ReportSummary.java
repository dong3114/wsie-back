package com.wise.app.dto;

import lombok.*;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportSummary {
    private String start;             // YYYY-MM-DD
    private String end;               // YYYY-MM-DD
    private int wasteCost;            // 폐기비용 합계(원)
    private double carbonReductionKg; // 추정 탄소 절감량(kg)
    private double marginImprovement; // 마진 개선율(0~1)
    private Map<String, Object> extras; // 선택: 기타 지표
}
