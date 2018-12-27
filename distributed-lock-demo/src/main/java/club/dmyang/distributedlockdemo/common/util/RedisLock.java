package club.dmyang.distributedlockdemo.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class RedisLock {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 加锁
     * @param key  productId
     * @param value  当前的时间 + 超时的时间
     * @return
     */
    public boolean lock(String key, String value){
        if ( stringRedisTemplate.opsForValue().setIfAbsent(key, value) ){
            // 设置成功，强占资源成功。
            return true;
        }
        //这里没有在Redis级别上设置超时机制，设置进入后就要手动删除，否则永久存在。
        //注意，这里特别要注意死锁问题
        //如果在service加锁成功后的某个操作抛出异常，则解锁的代码也就没能执行，导致死锁

        String currentValue = stringRedisTemplate.opsForValue().get(key);
        //如果锁过期
        if (!StringUtils.isEmpty(currentValue)  //这句只是防止空指针异常
                && Long.parseLong(currentValue) < System.currentTimeMillis()  //判断是否过期
        ){
            //获取上一个锁的时间
            String oldValue = stringRedisTemplate.opsForValue().getAndSet(key, value);
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
                return true;
            }

        }
        return false;
    }
    /**
     * 解锁
     * @param key
     * @param value
     */
    public void unlock(String key, String value){
        try {
            String currentVaule = stringRedisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentVaule) && currentVaule.equals(value)) {
                stringRedisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("【redis分布式锁】解锁异常,{}", e);
        }
    }
}
