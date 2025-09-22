package com.wise.app.dto;

import lombok.*;
import java.util.List;

import com.google.cloud.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {
    private String id;                // Firestore docId
    private String menuName;
    private List<String> ingredients; // ["토마토","계란",...] 재료 리스트
    private int estimatedCost;
    private int price;
    private int margin;               // price - estimatedCost
    private String menuType;          // "zero-waste" | "chef" | "pick"
    private String tips;              // 레시피/운영 팁
    private Timestamp createdAt;
}
