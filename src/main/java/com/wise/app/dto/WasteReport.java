package com.wise.app.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WasteReport {
    private String id;              // Firestore 문서 ID
    private String restaurantName;  // 가게명 (선택)
    private String date;            // 분석 날짜 (YYYY-MM-DD)
    private String analyzedBy;      // 분석자 or 시스템
    private String resultSummary;   // 분석 요약 (예: "잔반량 2.3kg, 원가손실 18,000원")
    private Map<String, Object> details; // 세부 결과 (재료별 폐기량, 비용)
}
