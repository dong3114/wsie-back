package com.wise.app.controller;

import com.wise.app.dto.ReportSummary;
import com.wise.app.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Reports")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService service;

    @Operation(summary="기간 요약 리포트", description="폐기비용/탄소절감/마진개선(시연용 가정식)")
    @GetMapping("/summary")
    public ResponseEntity<ReportSummary> summary(@RequestParam String start,
                                                 @RequestParam String end) throws Exception {
        return ResponseEntity.ok(service.summary(start, end));
    }
}
