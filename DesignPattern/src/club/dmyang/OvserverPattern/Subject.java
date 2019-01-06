package club.dmyang.OvserverPattern;
import java.util.ArrayList;
import java.util.List;
/**
 * 被观察者；发布者
 */
public class Subject {
    private List<Observer> observerList = new ArrayList<>();
    /**
     * 添加订阅者
     * @param observer
     */
    public void attach(Observer observer){
        observerList.add(observer);
    }
    /**
     * 删除订阅者
     * @param observer
     */
    public void detach(Observer observer) {
        observerList.remove(observer);
    }
    /**
     * 通知所有订阅者
     */
    public void notifyAllSubscribes(String msg){
        observerList.forEach(e -> e.update(msg));
    }
}
