package com.wise.app.service;

import com.wise.app.dto.AnalyzeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import org.springframework.core.io.FileSystemResource;

import java.io.File;

@Service
@RequiredArgsConstructor
public class AnalyzeService {

    private final WebClient webClient;

    public AnalyzeResponse analyzeImage(File imageFile) {
        return webClient.post()
                .uri("/analyze")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(new org.springframework.util.LinkedMultiValueMap<String, Object>() {{
                    add("file", new FileSystemResource(imageFile));
                }})
                .retrieve()
                .bodyToMono(AnalyzeResponse.class)
                .block();
    }
}
