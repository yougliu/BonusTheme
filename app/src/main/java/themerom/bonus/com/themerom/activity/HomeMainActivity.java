package themerom.bonus.com.themerom.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.squareup.okhttp.Request;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import themerom.bonus.com.themerom.R;
import themerom.bonus.com.themerom.adapter.ThemeAdapter;
import themerom.bonus.com.themerom.adapter.WallpaperAdapter;
import themerom.bonus.com.themerom.callback.OkHttpCallback;
import themerom.bonus.com.themerom.contants.Contacts;
import themerom.bonus.com.themerom.entity.MsgEvent;
import themerom.bonus.com.themerom.entity.Preview;
import themerom.bonus.com.themerom.entity.ThemeEntity;
import themerom.bonus.com.themerom.entity.WallpaperEntity;
import themerom.bonus.com.themerom.utils.OkHttpClientManager;
import themerom.bonus.com.themerom.utils.ThemeUtil;
import themerom.bonus.com.themerom.view.GalleryViewPager;

public class HomeMainActivity extends AppCompatActivity {

    private static final String TAG = HomeMainActivity.class.getSimpleName();
    private final String mPageName = "HomeMainActivity";
    private GalleryViewPager mGalleryViewPager;
    private LinearLayout mOvalLayout;
    DisplayImageOptions options;
    private GridView mWallpaperGrid, mThemeGrid;
    private int[] imageArray = {R.drawable.roll_1, R.drawable.roll_2, R.drawable.roll_3};
    private static final int SWITCH_TIME = 5000;

    private List<ThemeEntity> mThemeEntitys = new ArrayList<>();
    private List<WallpaperEntity> mWallpaperEntitys = new ArrayList<>();
    private ThemeAdapter mThemeAdapter;
    private WallpaperAdapter mWallpapaerAdapter;
    private BroadcastReceiver mReceiver;
    private static final String ACTION_NETWORK_REQUEST = "request.network.action";
    private OkHttpClientManager mClientManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);
        //for umeng
        MobclickAgent.setSessionContinueMillis(3000);
        //for eventBus
        EventBus.getDefault().register(this);
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
        mClientManager = OkHttpClientManager.getInstance();
        setNoNetWork();

        mThemeAdapter = new ThemeAdapter(HomeMainActivity.this,mThemeEntitys,options);
        mThemeGrid.setAdapter(mThemeAdapter);

        mWallpapaerAdapter = new WallpaperAdapter(this,mWallpaperEntitys,options);
        mWallpaperGrid.setAdapter(mWallpapaerAdapter);

    }

    //set no net work environment
    private void setNoNetWork() {
        if(ThemeUtil.isNetWorkAvailable(HomeMainActivity.this)){
            Contacts.getPixel(HomeMainActivity.this);
            Contacts.getUri();
            initRecomTheme();
            initRecomWallpaper();
        }
        List<Preview> previews = new ArrayList<>();
        previews.add(new Preview("238*423", "drawable://"+R.drawable.ic_stub));
        mThemeEntitys.add(new ThemeEntity("1","","",previews));

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
        if(mClientManager == null){
            mClientManager = OkHttpClientManager.getInstance();
        }
        mClientManager.excute(new Request.Builder().url(Contacts.HomeWallPath).build(), new OkHttpCallback() {
            @Override
            public void onError(Request request, Exception e) {
                // TODO: 11/14/15
            }

            @Override
            public void onSuccess(Object response) {
                String str = response.toString().substring(0,1);
                if(!str.equals("[")){
                    return;
                }
                Gson gson = new Gson();
                List<WallpaperEntity> wallpaperEntities = new ArrayList<WallpaperEntity>();
                wallpaperEntities = gson.fromJson(response.toString(),new TypeToken<List<WallpaperEntity>>(){}.getType());
                if(wallpaperEntities.size() > 0){
                    mWallpaperEntitys.clear();
                    for (int i = 0;i<wallpaperEntities.size();i++){
                        mWallpaperEntitys.add(wallpaperEntities.get(i));
                    }
                }
                mWallpaperGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ThemeUtil.toast(HomeMainActivity.this,"positon = "+position,Contacts.TOAST_SHORT_DURATION);
                    }
                });
                mWallpapaerAdapter.notifyDataSetChanged();
            }
        });
    }

    //pull recom theme from net work
    private void initRecomTheme() {
        // TODO: 11/12/15  android 6.0后，不能使用httpclient，所以不能使用xutils。
        //此处使用okhttp
        //for test to get
//        OkHttpClient client = new OkHttpClient();
//        final Request request = new Request.Builder().url(Contacts.MPATH).build();
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                // TODO: 11/12/15
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                String result = response.body().string();
//
//            }
//        });

        //for test
        if(mClientManager == null){
            mClientManager = OkHttpClientManager.getInstance();
        }
        mClientManager.excute(new Request.Builder().url(Contacts.MPATH).build(), new OkHttpCallback() {
            @Override
            public void onError(Request request, Exception e) {
                // TODO: 11/14/15 load location resource
            }

            @Override
            public void onSuccess(Object response) {
                Log.d("bonus", "response = " + response.toString());
                String str = response.toString().substring(0,1);
                if(!str.equals("[")){
                    return;
                }
                Gson gson = new Gson();
                List<ThemeEntity>  themeEntities = new ArrayList<ThemeEntity>();
                themeEntities = gson.fromJson(response.toString(),new TypeToken<List<ThemeEntity>>(){}.getType());
                Log.d("bonus","size = "+themeEntities.size());
                if(themeEntities.size() > 0){
                    mThemeEntitys.clear();
                    for (int i = 0;i<themeEntities.size();i++){
                        mThemeEntitys.add(themeEntities.get(i));
                    }
                }
                mThemeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // start load theme
                        StartLoadTheme(position);
                    }
                });
                mThemeAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * @param position
     */
    private void StartLoadTheme(int position) {
        Intent intent = new Intent(this,DownloadTheme.class);
        intent.putExtra(Contacts.DOWNLOAD_THEME_ENTITY,mThemeEntitys.get(position));
        startActivity(intent);
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

    public void onEventMainThread(MsgEvent msg){
        Log.d("bonus","onEventMainThread");
        ThemeUtil.toast(this,"onEventMainThread", Contacts.TOAST_SHORT_DURATION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //for eventbus
        EventBus.getDefault().unregister(this);
        if(mReceiver != null){
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //for umeng
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //for umeng
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
