package club.dmyang.OvserverPattern;
public class Employee2 implements Observer {
    @Override
    public void update(String msg) {
        System.out.println("Employee2收到：" + msg);
    }
}
