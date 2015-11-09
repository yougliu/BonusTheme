package themerom.bonus.com.themerom.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import themerom.bonus.com.themerom.R;
import themerom.bonus.com.themerom.utils.ThemeUitl;
import themerom.bonus.com.themerom.view.GalleryViewPager;

public class HomeMainActivity extends AppCompatActivity {

    private static final String TAG = HomeMainActivity.class.getSimpleName();
    private GalleryViewPager mGalleryViewPager;
    private LinearLayout mOvalLayout;
    DisplayImageOptions options;
    private GridView mWallpaperGrid, mThemeGrid;
    private int[] imageArray = {R.drawable.roll_1, R.drawable.roll_2, R.drawable.roll_3};
    private static final int SWITCH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);
        initImageOptions();
        mGalleryViewPager = (GalleryViewPager) findViewById(R.id.id_galleryViewPager);
        mOvalLayout = (LinearLayout) findViewById(R.id.id_oval);
        mWallpaperGrid = (GridView) findViewById(R.id.id_grid_wallpaper);
        mThemeGrid = (GridView) findViewById(R.id.id_grid_theme);

        mGalleryViewPager.start(this,SWITCH_TIME,mOvalLayout,R.drawable.ic_focus_select,R.drawable.ic_focus,
                imageArray,null,null,null);
        mGalleryViewPager.setGalleryOnItemClickListener(new GalleryViewPager.GalleryOnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //// TODO: 11/9/15
                ThemeUitl.toast(HomeMainActivity.this, "viewpager ...", 0);
            }
        });

    }

    private void initImageOptions() {
        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .showImageOnLoading(R.drawable.ic_stub)
                .build();
    }

}
