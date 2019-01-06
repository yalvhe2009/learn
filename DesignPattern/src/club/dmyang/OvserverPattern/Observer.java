package club.dmyang.OvserverPattern;
/**
 * 观察者；订阅者
 */
public interface Observer {
    /**
     * 接受发布者的消息
     * @param msg
     */
    void update(String msg);
}
