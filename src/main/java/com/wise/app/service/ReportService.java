package com.wise.app.service;

import com.google.cloud.firestore.*;
import com.wise.app.dto.ReportSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final Firestore firestore;

    /** 폐기비용 합계는 WasteDetail.cost 합산을 가정 */
    public ReportSummary summary(String start, String end) throws ExecutionException, InterruptedException {
        var col = firestore.collection("waste_reports");
        var q = col.whereGreaterThanOrEqualTo("date", start)
                .whereLessThanOrEqualTo("date", end);
        var snaps = q.get().get();

        int wasteCostSum = 0;
        for (DocumentSnapshot d : snaps.getDocuments()) {
            var details = (java.util.List<java.util.Map<String,Object>>) d.get("details");
            if (details != null) {
                for (var it : details) {
                    Object cost = it.get("cost");
                    if (cost instanceof Number n) wasteCostSum += n.intValue();
                }
            }
        }

        double carbon = Math.round((wasteCostSum / 4500.0) * 10) / 10.0;
        return ReportSummary.builder()
                .start(start).end(end)
                .wasteCost(wasteCostSum)
                .carbonReductionKg(carbon)
                .marginImprovement(0.05)
                .build();
    }
}
