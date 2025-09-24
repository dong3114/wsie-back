// 그래프 분석 프록시
package com.wise.app.service;

import com.wise.app.dto.GraphAnalyzeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AnalyzeGraphService {
    private final WebClient wsiePyWebClient;

    public GraphAnalyzeResponse analyzeGraph(MultipartFile file) {
        try {
            MultipartBodyBuilder mb = new MultipartBodyBuilder();
            mb.part("file", new ByteArrayResource(file.getBytes()){
                @Override public String getFilename() { return file.getOriginalFilename(); }
            }).filename(file.getOriginalFilename())
             .contentType(MediaType.parseMediaType(
                file.getContentType() == null ? "application/octet-stream" : file.getContentType()
             ));

            return wsiePyWebClient.post()
                    .uri("/api/v1/analyze/graph")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromMultipartData(mb.build()))
                    .exchangeToMono(resp -> {
                        if (resp.statusCode().is2xxSuccessful()) {
                            return resp.bodyToMono(GraphAnalyzeResponse.class);
                        }
                        // 에러 바디를 그대로 끌어와 전파
                        return resp.bodyToMono(String.class).defaultIfEmpty("")
                            .flatMap(body -> {
                                HttpStatus status = resp.statusCode().is4xxClientError()
                                        ? HttpStatus.BAD_REQUEST : HttpStatus.BAD_GATEWAY; // PY 문제는 502로 맵핑
                                return Mono.error(new ResponseStatusException(status, "PY error: " + body));
                            });
                    })
                    .block();
        } catch (ResponseStatusException e) {
            throw e; // 컨트롤러로 전달
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Analyze-Graph proxy failed: " + e.getMessage(), e);
        }
    }
}
