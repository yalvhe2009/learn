/**
 * Created by brss on 2018/2/25.
 */
public class ShiyanlouImpl implements Shiyanlou {
    @Override
    public String toUp(String name) {
        if (name==null){
            return null;
        }else {
            return name.toUpperCase();
        }
    }
}
