package themerom.bonus.com.themerom.callback;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by bonus on 11/30/15.
 * Class name ${type_name}
 */
public interface BonusDisplayImageListener {

    void onLoadingStarted(String var1, View var2);
    void onLoadingCompleted(String var1, View var2, Bitmap var3);
    void onLoadingFailed(String var1, View var2);
    void onLoadingCanceled(String var1, View var2);

}
