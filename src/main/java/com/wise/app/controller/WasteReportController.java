package com.wise.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wise.app.dto.WasteReport;
import com.wise.app.service.WasteReportService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/waste-reports")
public class WasteReportController {

    private final WasteReportService service;

    @Autowired
    public WasteReportController(WasteReportService service) {
        this.service = service;
    }

    // 저장
    @PostMapping
    public ResponseEntity<?> addReport(@RequestBody WasteReport report) {
        try {
            String time = service.saveReport(report);
            return ResponseEntity.ok(Map.of("message", "Report saved", "updatedAt", time));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<?> getAllReports() {
        try {
            return ResponseEntity.ok(service.getAllReports());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getReportById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(service.getReportById(id));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}
