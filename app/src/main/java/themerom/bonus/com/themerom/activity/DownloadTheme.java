package themerom.bonus.com.themerom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import themerom.bonus.com.themerom.R;
import themerom.bonus.com.themerom.view.BackImage;

/**
 * Created by bonus on 11/14/15.
 * Class name ${type_name}
 */
public class DownloadTheme extends Activity implements BackImage.OnBackClickListener{
    private static final String TAG = DownloadTheme.class.getSimpleName();
    private BackImage backView;
    private TextView mThemeTitle;
    private ViewPager mViewPager;
    private Button zambia, download, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_theme_layout);
        initView();
    }

    private void initView() {
        backView = (BackImage) findViewById(R.id.id_back);
        backView.setOnBackClickListener(this);
    }

    @Override
    public void onBackClick() {
        Log.d("bonus","------------------ onbackClick");
        finish();
    }
}
