// API Key 검증 + 수신
package com.wise.app.controller;

import com.wise.app.config.CallbackProps;
import com.wise.app.dto.AnalyzeCallbackRequest;
import com.wise.app.dto.AnalyzeCallbackResponse;
import com.wise.app.service.PythonCallbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/callback")
@RequiredArgsConstructor
public class PythonCallbackController {

    private final PythonCallbackService callbackService;
    private final CallbackProps props;

    @PostMapping("/analyze")
    public ResponseEntity<AnalyzeCallbackResponse> receiveAnalyzeResult(
            @RequestHeader(name = "X-API-KEY", required = false) String key,
            @Valid @RequestBody AnalyzeCallbackRequest request
    ) {
        if (!Objects.equals(props.getApiKey(), key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            String id = callbackService.saveAnalyzeResult(request);
            return ResponseEntity.ok(new AnalyzeCallbackResponse(id, "OK"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AnalyzeCallbackResponse(null, "ERROR: " + e.getMessage()));
        }
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("READY");
    }
}
