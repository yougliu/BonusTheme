package themerom.bonus.com.themerom.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.Toast;

/**
 * Created by helios on 11/9/15.
 */
public class ThemeUtil {
    //Toast
    public static void toast(Context context, String content, int duration){
        Toast.makeText(context, content, duration).show();
    }

    //network
    public static boolean isNetWorkAvailable(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager == null){
            return false;
        }
        NetworkInfo[] networkInfo = manager.getAllNetworkInfo();
        if(networkInfo != null && networkInfo.length >0){
            for (int i = 0; i < networkInfo.length ; i++){
                if(networkInfo[i].getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }

    //sdcard
    public static boolean hasSDCard(Context context){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }

    public static String getStoragePath(Context context){
        String path = null;
        if(hasSDCard(context)){
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        }else{
            path = context.getFilesDir().getAbsolutePath();
        }
        return path;
    }

    //px
    public static boolean isFHD(Context context){
        return context.getResources().getDisplayMetrics().densityDpi == 480;
    }

}
