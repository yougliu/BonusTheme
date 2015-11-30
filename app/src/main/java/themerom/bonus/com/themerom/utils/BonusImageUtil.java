package themerom.bonus.com.themerom.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by helios on 11/9/15.
 */
public class BonusImageUtil {
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

    //get cache dir
    public static File getDiskCacheDir(Context context,String fileName){
        String cachePath;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)&&!Environment.isExternalStorageRemovable()){
            cachePath = context.getExternalCacheDir().getPath();
        }else{
            cachePath = context.getCacheDir().getPath();
        }
        Log.d("bonus","cachePath = "+cachePath);
        return new File(cachePath+File.separator+fileName);
    }

    //get app version
    public static int getApplicationVersion(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    //md5加密
    public static String hashkeyForDisk(String url){
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            return bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return String.valueOf(url.hashCode());
        }
    }

    private static String bytesToHexString(byte[] bytes){
        StringBuilder builder = new StringBuilder();
        for (int i =0;i<bytes.length;i++){
            String hex = Integer.toHexString(0xFF&bytes[i]);
            if(hex.length() == 1){
                builder.append("0");
            }
            builder.append(hex);
        }
        return builder.toString();
    }

    //px
    public static boolean isFHD(Context context){
        return context.getResources().getDisplayMetrics().densityDpi == 480;
    }

}
