package club.dmyang.distributedlockdemo.controller;

import club.dmyang.distributedlockdemo.service.StockKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockKillController {

    @Autowired
    private StockKillService stockKillService;

    @GetMapping("/buy")
    public String buy(String productId) {
        stockKillService.orderProductMockDiffUser(productId);
        return stockKillService.queryStockKillProductInfo(productId);
    }

    @GetMapping("/query")
    public String query(String productId) {
        return stockKillService.queryStockKillProductInfo(productId);
    }


}
