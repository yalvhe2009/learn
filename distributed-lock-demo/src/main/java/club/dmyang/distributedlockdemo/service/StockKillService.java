package club.dmyang.distributedlockdemo.service;

public interface StockKillService {

    //根据商品id查询库存
    public String queryStockKillProductInfo(String productId);

    //模拟不同用户购买商品
    public void orderProductMockDiffUser(String productId);
}
