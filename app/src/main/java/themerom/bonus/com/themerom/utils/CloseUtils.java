package themerom.bonus.com.themerom.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by helios on 12/24/15.
 */
public class CloseUtils {

    /**
     * 所有实现Closeable的类都可以用这个方法进行关闭，如流等
     * @param closeable
     */
    public static void close(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
