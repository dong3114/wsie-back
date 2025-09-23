// Service: Firestore 저장
package com.wise.app.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.wise.app.dto.AnalyzeCallbackRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PythonCallbackService {

    public String saveAnalyzeResult(AnalyzeCallbackRequest req) throws Exception {
        Firestore db = FirestoreClient.getFirestore();

        String id = Optional.ofNullable(req.getTraceId())
                .filter(s -> !s.isBlank())
                .orElse(UUID.randomUUID().toString());

        Map<String, Object> data = new HashMap<>();
        data.put("menu", req.getMenu());
        data.put("wasteRatio", req.getWasteRatio());  // 카멜케이스로 저장
        data.put("suggestion", req.getSuggestion());
        data.put("imageUrl", req.getImageUrl());
        data.put("createdAt", Timestamp.now());

        ApiFuture<?> future = db.collection("analyze_results")
                .document(id)
                .set(data, SetOptions.merge());

        future.get(); // 동기 저장 보장(필요 시 비동기로 전환 가능)
        return id;
    }
}
