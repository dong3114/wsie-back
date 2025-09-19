package com.wise.app.controller;

import com.wise.app.dto.Stock;
import com.wise.app.service.FirestoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final FirestoreService firestoreService;

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
}
