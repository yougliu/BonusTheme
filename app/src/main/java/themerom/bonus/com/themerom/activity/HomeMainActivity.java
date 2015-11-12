package themerom.bonus.com.themerom.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.List;

import themerom.bonus.com.themerom.R;
import themerom.bonus.com.themerom.adapter.ThemeAdapter;
import themerom.bonus.com.themerom.entity.Preview;
import themerom.bonus.com.themerom.entity.ThemeEntity;
import themerom.bonus.com.themerom.entity.WallpaperEntity;
import themerom.bonus.com.themerom.utils.ThemeUtil;
import themerom.bonus.com.themerom.view.GalleryViewPager;

public class HomeMainActivity extends AppCompatActivity {

    private static final String TAG = HomeMainActivity.class.getSimpleName();
    private GalleryViewPager mGalleryViewPager;
    private LinearLayout mOvalLayout;
    DisplayImageOptions options;
    private GridView mWallpaperGrid, mThemeGrid;
    private int[] imageArray = {R.drawable.roll_1, R.drawable.roll_2, R.drawable.roll_3};
    private static final int SWITCH_TIME = 3000;

    private List<ThemeEntity> mThemeEntitys = new ArrayList<>();
    private List<WallpaperEntity> mWallpaperEntitys = new ArrayList<>();
    private ThemeAdapter mThemeAdapter;
    private WallpaperAdapter mWallpapaerAdapter;
    private BroadcastReceiver mReceiver;
    private static final String ACTION_NETWORK_REQUEST = "request.network.action";


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
                ThemeUtil.toast(HomeMainActivity.this, "viewpager ...", 0);
            }
        });

        setNoNetWork();

        mThemeAdapter = new ThemeAdapter(HomeMainActivity.this,mThemeEntitys,options,true);
        mThemeGrid.setAdapter(mThemeAdapter);

    }

    //set no net work environment
    private void setNoNetWork() {
        if(ThemeUtil.isNetWorkAvailable(HomeMainActivity.this)){
            initRecomTheme();
            initRecomWallpaper();
        }
        List<Preview> previews = new ArrayList<>();
        previews.add(new Preview("238*423", "drawable://"+R.drawable.ic_stub));
        mThemeEntitys.add(new ThemeEntity("1","","",previews));
        mThemeEntitys.add(new ThemeEntity("2","","",previews));
        mThemeEntitys.add(new ThemeEntity("3","","",previews));

        mWallpaperEntitys.add(new WallpaperEntity(previews));
        mWallpaperEntitys.add(new WallpaperEntity(previews));

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO: 11/11/15 initReComTheme and initRecomWallpapers 
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NETWORK_REQUEST);
        registerReceiver(mReceiver,filter);
    }

    //pull recom wallpaper from net work
    private void initRecomWallpaper() {
        // TODO: 11/11/15
    }

    //pull recom theme from net work
    private void initRecomTheme() {
        // TODO: 11/12/15  android 6.0后，不能使用httpclient，所以不能使用xutils。
        //此处使用okhttp

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

    //jump
    public void jumpToActivity(View view){
        int id = view.getId();
        switch (id){
            case R.id.id_home_theme:
                // TODO: 11/10/15

                break;

            case R.id.id_home_wallpaper:
                break;

            case R.id.id_home_liveWallPaper:
                break;

        }
    }


    private static class ViewHolder{
        ImageView imageView;
        TextView textView;
    }


    class WallpaperAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mReceiver != null){
            unregisterReceiver(mReceiver);
        }
    }
}
