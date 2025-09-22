package com.wise.app.dto;

import lombok.Data;

@Data
public class RecommendRequest {
    private String date;              // YYYY-MM-DD
    private String policy;            // "zero-waste" | "chef" | "mixed"
}
