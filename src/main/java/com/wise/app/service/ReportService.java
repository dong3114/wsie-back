package com.wise.app.service;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.wise.app.dto.ReportSummary;
import com.wise.app.dto.WasteDetail; // 선택적: POJO로 들어온 경우 대비
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final Firestore firestore;

    /**
     * 기간 요약: waste_reports 컬렉션에서 date(start~end) 범위 문서의
     * details 내 cost 값을 합산하여 wasteCost를 계산.
     * details 타입이 Map 또는 List 모두를 안전하게 처리.
     */
    public ReportSummary summary(String start, String end) throws ExecutionException, InterruptedException {
        Query q = firestore.collection("waste_reports")
                .whereGreaterThanOrEqualTo("date", start)
                .whereLessThanOrEqualTo("date", end);

        QuerySnapshot snaps = q.get().get();

        int wasteCostSum = 0;

        for (DocumentSnapshot d : snaps.getDocuments()) {
            Object details = d.get("details");
            if (details == null) continue;

            // details가 배열(List)로 저장된 경우
            if (details instanceof List<?> list) {
                for (Object item : list) {
                    wasteCostSum += extractCost(item);
                }
                continue;
            }

            // details가 객체(Map)로 저장된 경우 (ex: {"rice":{...}, "meat":{...}})
            if (details instanceof Map<?, ?> map) {
                for (Object v : map.values()) {
                    wasteCostSum += extractCost(v);
                }
                continue;
            }

            // 기타 단일 객체 등 예외 케이스
            wasteCostSum += extractCost(details);
        }

        // 탄소 절감량: 데모용 간단 가정식
        double carbon = Math.round((wasteCostSum / 4500.0) * 10.0) / 10.0;

        return ReportSummary.builder()
                .start(start)
                .end(end)
                .wasteCost(wasteCostSum)
                .carbonReductionKg(carbon)
                .marginImprovement(0.05)
                .build();
    }

    /**
     * details 항목에서 cost를 추출 (Map / WasteDetail / 임의 POJO 대비)
     */
    @SuppressWarnings("unchecked")
    private int extractCost(Object obj) {
        if (obj == null) return 0;

        // Map 형태: {"itemName": "...", "amount": ..., "unit": "...", "cost": 7000}
        if (obj instanceof Map<?, ?> m) {
            Object cost = m.get("cost");
            if (cost instanceof Number n) {
                return n.intValue();
            }
            // 혹시 중첩 구조가 있을 때(드물지만 방어)
            Object nested = m.get("data");
            if (nested instanceof Map<?, ?> nm) {
                Object c2 = nm.get("cost");
                if (c2 instanceof Number n2) return n2.intValue();
            }
            return 0;
        }

        // POJO(WasteDetail) 형태
        if (obj instanceof WasteDetail wd) {
            return wd.getCost();
        }

        // 리플렉션으로 필드 접근 시도 (예외 케이스 방어)
        try {
            var f = obj.getClass().getDeclaredField("cost");
            f.setAccessible(true);
            Object v = f.get(obj);
            if (v instanceof Number n) return n.intValue();
        } catch (Exception ignore) {
        }

        return 0;
    }
}
