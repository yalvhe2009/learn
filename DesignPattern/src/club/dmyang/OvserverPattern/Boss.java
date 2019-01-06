package club.dmyang.OvserverPattern;
public class Boss extends Subject {
    // 老板的状态
    private String bossState;
    public String getBossState() {
        return bossState;
    }
    public void setBossState(String bossState) {
        this.bossState = bossState;
        //老板状态变化，通知所有订阅者（Observer）
        this.notifyAllSubscribes(bossState);
    }
}
