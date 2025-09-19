package com.wise.app.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.wise.app.dto.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class FirestoreService {

    private final Firestore firestore;

    // Create / Update
    public String saveStock(Stock stock) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> result = firestore.collection("stocks")
                .document(stock.getStock_name())
                .set(stock);
        return result.get().getUpdateTime().toString();
    }

    // Read
    public Stock getStock(String stockName) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("stocks").document(stockName);
        DocumentSnapshot snapshot = docRef.get().get();
        return snapshot.exists() ? snapshot.toObject(Stock.class) : null;
    }

    // Delete
    public String deleteStock(String stockName) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> result = firestore.collection("stocks").document(stockName).delete();
        return result.get().getUpdateTime().toString();
    }
}
