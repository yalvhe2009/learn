
## 概述

使用SpringBoot，搭建分布式锁测试代码环境。需要配置一个外置的Redis，请在application.yml中配置。



## 前言

我们知道，在秒杀等高并发场景下，如果不能对并发访问的资源进行可靠的保护，将会造成商品超卖等问题。通常最简单的办法就是在被并发访问的资源上添加synchronize关键字对资源进行保护。但使用synchronize关键字有如下缺点：

- 仅仅支持单点，无法支持分布式。
- 效率低
- 控制粒度低（如同一时刻有两个商品A和B进行秒杀，但都是使用商品id调用buy()函数，假设所有人都在抢A，只有一个人在抢B，如果使用synchronize关键字在buy方法上，则会使得仅有一个抢B的人也特别的卡。）

针对上述三个问题，提出基于Redis的分布式锁。

## 前置知识

1. Redis的相关命令：
    - setnx mykey "hello"   //如果mykey存在则返回0不做操作，如果mykey不存在，则设置并返回1.【jedis中的方法是：setIfAbsent】
    - getset myage "0"     //假如myage本来是18，则执行这条命令后，返回18，但此时redis中myage的值被设置为0.（这个二语义操作实际上是个原子操作）
2. 实现分步实施锁的基本思想：当有两台tomcat应用同时要操作一个资源,应用1和应用2都指定同一个key，两个应用都使用setnx来建立key，谁建立成功了，谁就能操作资源。操作完成后，需要把key删除掉，以便下次继续抢占资源。

## 实现代码

```java

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
```


## 总结

使用redis实现分布式锁有如下优势：

- 支持分布式
- 可以更细粒度的控制
- 效率较高

其实实现分布式锁的基本原理就是：多台机器上多个进程对一个数据进行操作的互斥。

另外，Redis作为单线程的服务，也是它可以用做实现分布式锁的重要原因之一。另一原因或许是因为它可以轻松应对十万级别的并发。