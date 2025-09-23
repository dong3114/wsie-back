// com/wise/app/controller/AnalyzeController.java
package com.wise.app.controller;

import com.wise.app.dto.AnalyzeResponse;
import com.wise.app.service.AnalyzeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AnalyzeController {

    private final AnalyzeService analyzeService;

    @PostMapping("/analyze")
    public ResponseEntity<AnalyzeResponse> analyze(@RequestParam("file") MultipartFile file) {
        var result = analyzeService.analyzeImage(file);
        return ResponseEntity.ok(result);
    }
}
