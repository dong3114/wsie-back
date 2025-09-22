package com.wise.app.controller;

import com.wise.app.dto.WasteReport;
import com.wise.app.service.WasteReportService;
import com.google.cloud.firestore.*; // Firestore import
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/waste-reports")
@RequiredArgsConstructor
public class WasteReportController {

    private final WasteReportService service;
    private final Firestore firestore; // ⬅ Firestore Bean 주입

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

    @Operation(summary="잔반 TOP N", description="wasteRatio(desc) 상위 N")
    @GetMapping("/top")
    public ResponseEntity<?> top(@RequestParam(defaultValue="5") int limit) throws Exception {
        var docs = firestore.collection("waste_reports")
                .orderBy("wasteRatio", Query.Direction.DESCENDING)
                .limit(limit).get().get().getDocuments();
        var list = docs.stream().map(DocumentSnapshot::getData).toList();
        return ResponseEntity.ok(list);
    }
}
