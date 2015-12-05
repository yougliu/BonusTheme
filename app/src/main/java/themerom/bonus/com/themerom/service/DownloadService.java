package themerom.bonus.com.themerom.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by bonus on 12/4/15.
 * Class name ${type_name}
 */
public class DownloadService extends Service {

    private static final String TAG = DownloadService.class.getSimpleName();
    private String downloadPath, savePath;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public void download(){
        
    }
}
