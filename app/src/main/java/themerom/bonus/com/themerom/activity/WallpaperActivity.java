package themerom.bonus.com.themerom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import themerom.bonus.com.themerom.R;
import themerom.bonus.com.themerom.view.BackImage;

/**
 * Created by bonus on 11/17/15.
 * Class name ${type_name}
 */
public class WallpaperActivity extends Activity implements BackImage.OnBackClickListener{
    private static final String TAG = WallpaperActivity.class.getSimpleName();
    private BackImage mBack;
    private TextView mTitle;
    private ImageView mSet;
    public static final String WALLPAPER_ORIGINAL_PATH = "/media/wallpapers/original";
    public static final String WALLPAPER_THUMBNAIL_PATH = "/media/wallpapers/thumbnail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpaper_detail_layout);
        initActionBar();
    }

    private void initActionBar() {
        mBack = (BackImage) findViewById(R.id.id_back);
        mTitle = (TextView) findViewById(R.id.id_actionbar_title);
        mSet = (ImageView) findViewById(R.id.id_set);
        mBack.setOnBackClickListener(this);
    }


    @Override
    public void onBackClick() {
        finish();
    }


}
