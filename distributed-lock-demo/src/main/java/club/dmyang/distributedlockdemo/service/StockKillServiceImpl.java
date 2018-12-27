package club.dmyang.distributedlockdemo.service;

import club.dmyang.distributedlockdemo.common.util.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class StockKillServiceImpl implements StockKillService {

    public static final int TIMEOUT_LOCK = 10 * 1000;//分布式锁超时时间10s


    @Autowired
    private RedisLock redisLock;

    /**
     * 用map模拟数据库
     */
    static Map<String, Integer> products;//id -> 总份数
    static Map<String, Integer> stock;//id -> 剩余份数
    static Map<String, String> orders;//用户id -> 商品id

    static {
        /**
         * 模拟多个表，商品信息表，库存表，秒杀成功的订单表
         */
        products = new HashMap<>();
        stock = new HashMap<>();
        orders = new HashMap<>();
        products.put("123456", 10000);
        stock.put("123456",10000);
    }

    private String queryMap(String productId) {
        StringBuilder builder = new StringBuilder();
        builder.append("大促销了，皮皮虾特价了，限购：" + products.get(productId));
        builder.append(" 还剩：" + stock.get(productId) + "份");
        builder.append(" 成功下单人数：" + orders.size());
        return builder.toString();
    }

    @Override
    public String queryStockKillProductInfo(String productId) {
        return queryMap(productId);
    }

    @Override
    public void orderProductMockDiffUser(String productId) {

        //加锁
        long time = System.currentTimeMillis() + TIMEOUT_LOCK;
        if ( !redisLock.lock(productId, String.valueOf(time))) {
            throw new RuntimeException("抢购的人太多啦，换个姿势在试试");
        }

        // 1. 查询该商品，为0则活动结束
        Integer stockNum = stock.get(productId);
        if (stockNum == 0) {
            throw new RuntimeException("活动已经结束");
        }else {
            //下单
            orders.put(UUID.randomUUID().toString(),productId);//主键方面偷个懒,uuid了
            //扣库存,(模拟耗时的数据库操作)
            try {
                stockNum -= 1;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stock.put(productId, stockNum);
        }

        //解锁
        redisLock.unlock(productId, String.valueOf(time));
    }
}
