package com.wise.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WasteDetail {
    private String itemName;   // 재료명 (예: 쌀, 김치, 고기)
    private double amount;     // 폐기량 (단위 kg, 개수 등)
    private String unit;       // 단위 (kg, g, 개)
    private int cost;          // 폐기 비용 (원)
}
