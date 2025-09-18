package com.wise.app.controller;

import com.wise.app.dto.AnalyzeResponse;
import com.wise.app.service.AnalyzeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnalyzeController {

    private final AnalyzeService analyzeService;

    @PostMapping("/analyze")
    public AnalyzeResponse analyze(@RequestParam("file") MultipartFile file) throws Exception {
        // 업로드된 파일 임시 저장
        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFile);

        // Python 서버 호출
        AnalyzeResponse response = analyzeService.analyzeImage(tempFile);

        // 임시 파일 삭제
        tempFile.delete();

        return response;
    }
}
