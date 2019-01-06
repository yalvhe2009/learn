package club.dmyang.OvserverPattern;
public class Main {
    public static void main(String[] args) {
        // 背景：X公司的employee1和employee2在上班时做其他事总被老板抓；
        // 特意叮嘱前台小妹老板来了和走了时要通知一声他两
        Boss boss = new Boss();
        Observer employee1 = new Employee1();
        Observer employee2 = new Employee2();
        //添加订阅者
        boss.attach(employee1);
        boss.attach(employee2);
        //老板来公司了，前台小妹发布一个老板状态变化的消息
        boss.setBossState("老板来了，请做好准备！");
        //老板离开公司时，前台小妹发布一个老板状态变化的消息
        boss.setBossState("老板走了，可以放松一下喽");
    }
}
