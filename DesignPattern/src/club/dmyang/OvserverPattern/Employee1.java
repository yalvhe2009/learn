package club.dmyang.OvserverPattern;
public class Employee1 implements Observer {
    @Override
    public void update(String msg) {
        System.out.println("Employee1收到："+msg);
    }
}
