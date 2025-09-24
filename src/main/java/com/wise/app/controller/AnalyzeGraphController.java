package com.wise.app.controller;

import com.wise.app.dto.GraphAnalyzeResponse;
import com.wise.app.service.AnalyzeGraphService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

// Swagger/OpenAPI
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AnalyzeGraphController {

    private final AnalyzeGraphService service;

    @Operation(summary = "그래프 기반 심화 분석",
        description = "이미지 업로드 → Python /api/v1/analyze/graph 프록시")
    @PostMapping(value = "/analyze-graph",
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> analyzeGraph(
        @Parameter(description = "분석할 이미지 파일", required = true,
            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                               schema = @Schema(type = "string", format = "binary")))
        @RequestPart("file") MultipartFile file
    ) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("file is empty");
        }
        try {
            GraphAnalyzeResponse res = service.analyzeGraph(file);
            return ResponseEntity.ok(res);
        } catch (ResponseStatusException ex) {
            log.error("Analyze-Graph RSE: {}", ex.getReason());
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        } catch (Exception e) {
            log.error("Analyze-Graph failed", e);
            return ResponseEntity.internalServerError().body("Analyze-Graph failed: " + e.getMessage());
        }
    }
}
