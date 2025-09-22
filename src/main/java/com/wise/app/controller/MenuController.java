package com.wise.app.controller;

import com.wise.app.dto.MenuItem;
import com.wise.app.dto.RecommendRequest;
import com.wise.app.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "Menus")
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService service;

    @Operation(summary="추천 3개 생성(룰/AI)", description="policy: zero-waste|chef|mixed")
    @PostMapping("/recommend")
    public ResponseEntity<?> recommend(@RequestBody RecommendRequest req) throws Exception {
        var items = service.recommend(req);
        return ResponseEntity.ok(Map.of("menus", items, "generated_at", new Date()));
    }

    @Operation(summary="메뉴 목록", description="type 필터 및 페이지네이션 지원")
    @GetMapping
    public ResponseEntity<?> list(@RequestParam(required=false) String type,
                                @RequestParam(defaultValue="20") int limit,
                                @RequestParam(required=false) String cursor) {
        try {
            var list = service.list(type, limit, cursor);
            if (list == null) list = java.util.List.of(); // null 방어

            String next = (list.isEmpty() ? null : list.get(list.size()-1).getId());

            // null 허용을 위해 HashMap 사용
            java.util.Map<String,Object> body = new java.util.HashMap<>();
            body.put("items", list);
            body.put("nextCursor", next);

            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(java.util.Map.of("error", e.getMessage()));
        }
    }



    @Operation(summary="메뉴 상세")
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) throws Exception {
        MenuItem m = service.get(id);
        return (m == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(m);
    }

    @Operation(summary="메뉴 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(Map.of("updatedAt", service.delete(id)));
    }
}
