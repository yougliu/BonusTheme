package themerom.bonus.com.themerom.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by helios on 11/9/15.
 */
public class ThemeUitl {
    //Toast
    public static void toast(Context context, String content, int duration){
        Toast.makeText(context, content, duration).show();
    }
}
