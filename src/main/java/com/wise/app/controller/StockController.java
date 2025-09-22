package com.wise.app.controller;

import com.wise.app.dto.Stock;
import com.wise.app.service.FirestoreService;
import com.google.cloud.firestore.*; // ⬅ Firestore import
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final FirestoreService firestoreService;
    private final Firestore firestore; // ⬅ Firestore Bean 주입

    @PostMapping
    public String addStock(@RequestBody Stock stock) throws Exception {
        return firestoreService.saveStock(stock);
    }

    @GetMapping("/{name}")
    public Stock getStock(@PathVariable String name) throws Exception {
        return firestoreService.getStock(name);
    }

    @DeleteMapping("/{name}")
    public String deleteStock(@PathVariable String name) throws Exception {
        return firestoreService.deleteStock(name);
    }

    @Operation(summary="임박 재고 조회", description="days 이내 소비기한 정렬 + 페이지네이션")
    @GetMapping("/expiring")
    public ResponseEntity<?> expiring(@RequestParam(defaultValue="3") int days,
                                      @RequestParam(defaultValue="20") int limit,
                                      @RequestParam(required=false) String cursor) throws Exception {
        String until = java.time.LocalDate.now().plusDays(days).toString();
        CollectionReference col = firestore.collection("stocks"); // ⬅ 여기서 바로 firestore 사용

        Query q = col.whereLessThanOrEqualTo("stock_expiration_date", until)
                     .orderBy("stock_expiration_date")
                     .limit(limit);

        if (cursor != null) {
            DocumentSnapshot snap = col.document(cursor).get().get();
            if (snap.exists()) q = q.startAfter(snap);
        }

        var docs = q.get().get().getDocuments();
        var list = docs.stream().map(d -> d.getData()).toList();
        String next = docs.isEmpty() ? null : docs.get(docs.size() - 1).getId();
        return ResponseEntity.ok(Map.of("items", list, "nextCursor", next));
    }
}
