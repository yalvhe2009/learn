import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 1.补全类 ShiyanlouImpl，实现 toUp(String name) 方法, toUp 方法将传入的字符串转为大写并返回
 * (异常情况返回 null)，例如传入的为 shiyanlou，返回的为 SHIYANLOU。
 * 2.补全 Main 类的 ioc(String className, Shiyanlou s, String methodName, String name) 方法，
 * 实现功能：反射生成 className 类实例;找到 className 实例中类型为 Shiyanlou 的变量并调用
 * set 方法注入参数 s;然后调用 className 的 methodName 方法传入 name 参数。
 *
 * 目标：
 * 1.保证 ShiyanlouImpl 类的源文件存放路径为 /home/shiyanlou/ShiyanlouImpl.java；
 * 2.保证 Main 类的源文件存放路径为 /home/shiyanlou/Main.java；
 * 3.最终实现的 IOC 满足介绍部分的所有需求。
 */


public class Main {

    public static void main(String[] args) {

        Shiyanlou s = new ShiyanlouImpl();
        ioc("MyClass",s,"test","aldjafslkjfl");
    }

    // TODO
    public static void  ioc(String className,Shiyanlou s,String methodName, String name){
        try {
            Class clazz = Class.forName(className);
            Object instance = clazz.newInstance();//反射生成实例
            //找到 className 实例中类型为 Shiyanlou 的变量
            Field[] fields = clazz.getDeclaredFields();
            Field f  = null;
            for (int i = 0 ; i < fields.length ; i++){
                String fieldName = fields[i].getType().getName();
                if (fieldName.equals("Shiyanlou")){
                    f = fields[i];
                    break;
                }
            }
            String fieldName = f.getName();//找出类型是Shiyanlou的成员的名字
            Method[] methods = clazz.getMethods();
            //找到该成员对应的set方法
            Method m = null;
            for (int i = 0 ; i < methods.length ; i++){
                String mName = methods[i].getName().toLowerCase();
                if (mName.equals("set"+fieldName.toLowerCase())){
                    m = methods[i];
                    break;
                }
            }
            //执行该方法，传入s
            m.invoke(instance,s);
            //调用methodName方法，传入name
            Method method = clazz.getMethod(methodName, String.class);
            method.invoke(instance,name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
