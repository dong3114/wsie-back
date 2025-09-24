// 이미지 멀티파트 → Python 프록시
package com.wise.app.service;

import com.wise.app.dto.AnalyzeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AnalyzeService {

    private final WebClient wsiePyWebClient;

    public AnalyzeResponse analyzeImage(MultipartFile file) {
        try {
            MultipartBodyBuilder mb = new MultipartBodyBuilder();
            mb.part("file", new ByteArrayResource(file.getBytes()){
                @Override public String getFilename() { return file.getOriginalFilename(); }
            }).filename(file.getOriginalFilename())
             .contentType(MediaType.parseMediaType(file.getContentType() == null ? "application/octet-stream" : file.getContentType()));

            return wsiePyWebClient.post()
                    .uri("/analyze")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(mb.build()))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, resp ->
                            resp.bodyToMono(String.class).flatMap(body -> Mono.error(new RuntimeException("PY error: " + body))))
                    .bodyToMono(AnalyzeResponse.class)
                    .block(); // 단순 동기화 (필요 시 비동기로 전환)
        } catch (Exception e) {
            throw new RuntimeException("Analyze proxy failed: " + e.getMessage(), e);
        }
    }
}
