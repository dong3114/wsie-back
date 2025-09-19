package com.wise.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.wise.app.dto.WasteReport;

@Service
public class WasteReportService {

    private final Firestore firestore;

    @Autowired
    public WasteReportService(Firestore firestore) {
        this.firestore = firestore;
    }

    private static final String COLLECTION_NAME = "waste_reports";

    // 저장
    public String saveReport(WasteReport report) throws Exception {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        report.setId(docRef.getId());
        ApiFuture<WriteResult> future = docRef.set(report);
        return future.get().getUpdateTime().toString();
    }

    // 전체 조회
    public List<WasteReport> getAllReports() throws Exception {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        return future.get().getDocuments().stream()
                .map(doc -> doc.toObject(WasteReport.class))
                .collect(Collectors.toList());
    }

    // 상세 조회
    public WasteReport getReportById(String id) throws Exception {
        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (doc.exists()) {
            return doc.toObject(WasteReport.class);
        } else {
            throw new RuntimeException("Report not found: " + id);
        }
    }
}
