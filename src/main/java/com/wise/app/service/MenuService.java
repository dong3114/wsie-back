package com.wise.app.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.wise.app.dto.MenuItem;
import com.wise.app.dto.RecommendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final Firestore firestore;
    private static final String COL = "menus";

    public List<MenuItem> list(String type, int limit, String cursor) throws Exception {
        CollectionReference col = firestore.collection(COL);
        Query q = (type != null && !type.isBlank())
                ? col.whereEqualTo("menuType", type)
                : col;
        q = q.orderBy("createdAt", Query.Direction.DESCENDING).limit(limit);

        if (cursor != null) {
            // cursor = 이전 페이지 마지막 문서 ID
            DocumentSnapshot snap = col.document(cursor).get().get();
            if (snap.exists()) q = q.startAfter(snap);
        }
        return q.get().get().getDocuments().stream()
                .map(d -> {
                    MenuItem m = d.toObject(MenuItem.class);
                    if (m.getId() == null) m.setId(d.getId());
                    return m;
                }).collect(Collectors.toList());
    }

    public MenuItem get(String id) throws Exception {
        DocumentSnapshot d = firestore.collection(COL).document(id).get().get();
        if (!d.exists()) return null;
        MenuItem m = d.toObject(MenuItem.class);
        if (m.getId() == null) m.setId(d.getId());
        return m;
    }

    public String delete(String id) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> w = firestore.collection(COL).document(id).delete();
        return w.get().getUpdateTime().toString();
    }

    /** 추천 3개 생성 (초기: 더미/룰베이스 → 나중에 Python 호출로 대체) */
    public List<MenuItem> recommend(RecommendRequest req) throws Exception {
        List<MenuItem> items = List.of(
            MenuItem.builder().menuName("토마토 계란볶음")
                .ingredients(List.of("토마토","계란","양파"))
                .estimatedCost(3000).price(7000).margin(4000)
                .menuType("zero-waste").tips("남은 토마토 소진 / 20분 조리")
                .createdAt(Timestamp.now()).build(),
            MenuItem.builder().menuName("김치전")
                .ingredients(List.of("김치","부침가루"))
                .estimatedCost(2500).price(6500).margin(4000)
                .menuType("pick").tips("저녁해피아워 특가")
                .createdAt(Timestamp.now()).build(),
            MenuItem.builder().menuName("채소볶음밥")
                .ingredients(List.of("밥","채소믹스","간장"))
                .estimatedCost(2000).price(6000).margin(4000)
                .menuType("chef").tips("잔반 밥 활용 / 불맛 강조")
                .createdAt(Timestamp.now()).build()
        );

        CollectionReference col = firestore.collection(COL);
        WriteBatch batch = firestore.batch();
        int mutations = 0; // 몇 개 추가했는지 카운트
        List<MenuItem> withIds = new ArrayList<>();

        for (MenuItem m : items) {
            // 중복 검사
            var existing = col.whereEqualTo("menuName", m.getMenuName())
                            .whereEqualTo("menuType", m.getMenuType())
                            .limit(1)
                            .get().get();
            if (existing.isEmpty()) {
                // 새 문서로 추가
                DocumentReference ref = col.document();
                m.setId(ref.getId());
                batch.set(ref, m);
                mutations++;
                withIds.add(m);
            } else {
                // 이미 있는 경우 기존 ID만 세팅
                var doc = existing.getDocuments().get(0);
                m.setId(doc.getId());
                withIds.add(m);
            }
        }

        if (mutations > 0) { // 추가한 게 있을 때만 commit
            batch.commit().get();
        }

        return withIds;
    }


}
