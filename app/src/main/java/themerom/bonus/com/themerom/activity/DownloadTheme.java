package themerom.bonus.com.themerom.activity;

import android.app.Activity;
import android.os.Bundle;

import themerom.bonus.com.themerom.R;

/**
 * Created by bonus on 11/14/15.
 * Class name ${type_name}
 */
public class DownloadTheme extends Activity{
    private static final String TAG = DownloadTheme.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_theme_layout);
    }
}
