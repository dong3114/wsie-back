package com.wise.app.dto;

import lombok.Data;

@Data
public class AnalyzeResponse {
    private String menu;
    private Double waste_ratio;
    private String suggestion;
}