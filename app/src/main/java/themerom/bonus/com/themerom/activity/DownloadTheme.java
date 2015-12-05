package themerom.bonus.com.themerom.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.io.File;

import de.greenrobot.event.EventBus;
import themerom.bonus.com.themerom.R;
import themerom.bonus.com.themerom.contants.Contacts;
import themerom.bonus.com.themerom.entity.MsgEvent;
import themerom.bonus.com.themerom.entity.ThemeEntity;
import themerom.bonus.com.themerom.utils.BonusImageUtil;
import themerom.bonus.com.themerom.view.BackImage;
import themerom.bonus.com.themerom.xutils3.DownloadInfo;
import themerom.bonus.com.themerom.xutils3.DownloadManager;
import themerom.bonus.com.themerom.xutils3.DownloadService;
import themerom.bonus.com.themerom.xutils3.DownloadState;
import themerom.bonus.com.themerom.xutils3.DownloadViewHolder;

/**
 * Created by bonus on 11/14/15.
 * Class name ${type_name}
 * http://muldown.fuli365.net/online_wallpaper/topicapk/com.shly.theme.doraemon.apk
 */
public class DownloadTheme extends Activity implements BackImage.OnBackClickListener{
    private static final String TAG = DownloadTheme.class.getSimpleName();
    private BackImage backView;
    private TextView mThemeTitle;
    private ViewPager mViewPager;
    private Button zambia, download, delete;
    private DisplayImageOptions options;
    private ThemeEntity mThemeEntity;
    private DownloadThemeAdapter mAdapter;
    private TextView mTitle;
    private ImageView actionbarSet;
    private String apkName;
    //for xutils3
    private DownloadManager manager;
    private DownloadItemViewHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_theme_layout);
        mThemeEntity = (ThemeEntity) getIntent().getExtras().getSerializable(Contacts.DOWNLOAD_THEME_ENTITY);
        initOptions();
        initView();
        mTitle.setText(mThemeEntity.getThemename());
        manager = DownloadService.getDownloadManager();
        apkName = mThemeEntity.getPackageName().substring(
                mThemeEntity.getPackageName().lastIndexOf("/") + 1,
                mThemeEntity.getPackageName().lastIndexOf("."));
        String label = apkName;
        try {
            manager.startDownload(mThemeEntity.getPackageName(),label,"/sdcard/xUtils/"+mThemeEntity.getThemename()+".apk",false,false,null);
        } catch (DbException e) {
            e.printStackTrace();
        }

        DownloadInfo downloadInfo = manager.getDownloadInfo(0);
        Log.d(TAG,"downloadInfo = "+(downloadInfo == null)+", "+apkName);
        if(downloadInfo != null){
            holder = new DownloadItemViewHolder(download, downloadInfo);
            holder.refresh();
            Log.d(TAG,"downloadInfo state= "+downloadInfo.getState().value());
            if (downloadInfo.getState().value() < DownloadState.FINISHED.value()) {
                try {
                    manager.startDownload(
                            downloadInfo.getUrl(),
                            downloadInfo.getLabel(),
                            downloadInfo.getFileSavePath(),
                            downloadInfo.isAutoResume(),
                            downloadInfo.isAutoRename(),
                            holder);
                } catch (DbException ex) {
                    Toast.makeText(x.app(), "添加下载失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void initOptions() {
        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.NONE)
                .showImageOnFail(R.drawable.ic_error)
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .build();

    }

    private void initView() {
        backView = (BackImage) findViewById(R.id.id_back);
        backView.setOnBackClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mAdapter = new DownloadThemeAdapter();
        mViewPager.setAdapter(mAdapter);
        zambia = (Button) findViewById(R.id.id_button_zambia);
        download = (Button) findViewById(R.id.id_button_download);
        delete = (Button) findViewById(R.id.id_button_delete);
        mTitle = (TextView) findViewById(R.id.id_actionbar_title);
        actionbarSet = (ImageView) findViewById(R.id.id_set);
        actionbarSet.setVisibility(View.INVISIBLE);
        actionbarSet.setEnabled(false);
    }

    @Override
    public void onBackClick() {
        EventBus.getDefault().post(new MsgEvent<String>("EventBus test"));
        finish();
    }

//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        switch (id) {
//            case R.id.id_button_download:
//                if(zambia.getVisibility() == View.VISIBLE){
//                    zambia.setVisibility(View.GONE);
//                }
//                //start service
//                Log.d(TAG,"URL = "+mThemeEntity.getPackageName());
//
//                DownloadInfo downloadInfo = manager.getDownloadInfo(0);
//                if(downloadInfo != null){
//                    Log.d(TAG,"downloadInfo = "+(downloadInfo == null));
//                    DownloadItemViewHolder holder = new DownloadItemViewHolder(download, downloadInfo);
//                    holder.update(downloadInfo);
//                    if (downloadInfo.getState().value() < DownloadState.FINISHED.value()) {
//                        try {
//                            manager.startDownload(
//                                    downloadInfo.getUrl(),
//                                    downloadInfo.getLabel(),
//                                    downloadInfo.getFileSavePath(),
//                                    downloadInfo.isAutoResume(),
//                                    downloadInfo.isAutoRename(),
//                                    holder);
//                        } catch (DbException ex) {
//                            Toast.makeText(x.app(), "添加下载失败", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }
//                break;
//            case R.id.id_button_zambia:
//                break;
//            case R.id.id_button_delete:
//                break;
//            default:
//                break;
//        }
//    }

    class DownloadItemViewHolder extends DownloadViewHolder {

        public DownloadItemViewHolder(View view, DownloadInfo downloadInfo) {
            super(view, downloadInfo);
            refresh();
        }

        @Event(R.id.id_button_download)//for click
        private void toggleEvent(View view) {
            DownloadState state = downloadInfo.getState();
            switch (state) {
                case WAITING:
                    Log.d(TAG,"waiting");
                    try {
                        manager.startDownload(
                                downloadInfo.getUrl(),
                                downloadInfo.getLabel(),
                                downloadInfo.getFileSavePath(),
                                downloadInfo.isAutoResume(),
                                downloadInfo.isAutoRename(),
                                this);
                    } catch (DbException ex) {
                        Toast.makeText(x.app(), "添加下载失败", Toast.LENGTH_LONG).show();
                    }
                case STARTED:
                    Log.d(TAG,"started");
                    manager.stopDownload(downloadInfo);
                    break;
                case ERROR:
                    Log.d(TAG,"error");
                case STOPPED:
                    Log.d(TAG,"stopped");
                    try {
                        manager.startDownload(
                                downloadInfo.getUrl(),
                                downloadInfo.getLabel(),
                                downloadInfo.getFileSavePath(),
                                downloadInfo.isAutoResume(),
                                downloadInfo.isAutoRename(),
                                this);
                    } catch (DbException ex) {
                        Toast.makeText(x.app(), "添加下载失败", Toast.LENGTH_LONG).show();
                    }
                    break;
                case FINISHED:
                    Log.d(TAG,"finished");
                    Toast.makeText(x.app(), "已经下载完成", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void update(DownloadInfo downloadInfo) {
            super.update(downloadInfo);
            refresh();
        }

        @Override
        public void onWaiting() {
            refresh();
        }

        @Override
        public void onStarted() {
            refresh();
        }

        @Override
        public void onLoading(long total, long current) {
            refresh();
        }

        @Override
        public void onSuccess(File result) {
            refresh();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            refresh();
        }

        @Override
        public void onCancelled(Callback.CancelledException cex) {
            refresh();

        }

        public void refresh() {
            download.setText(R.string.download);
            DownloadState state = downloadInfo.getState();
            switch (state) {
                case WAITING:
                    Log.d(TAG,"refresh waiting");
                case STARTED:
                    Log.d(TAG,"refresh started ");
                    download.setText(downloadInfo.getProgress()+"%");
                    zambia.setVisibility(View.GONE);
                    break;
                case ERROR:
                    Log.d(TAG,"refresh error");
                    BonusImageUtil.toast(DownloadTheme.this,"网络链接错误或网速较低，以断开链接！",Contacts.TOAST_SHORT_DURATION);
                case STOPPED:
                    Log.d(TAG,"refresh stopped");
                    download.setText(x.app().getString(R.string.download_continue));
                    break;
                case FINISHED:
                    Log.d(TAG,"refresh finished");
                    download.setText(x.app().getString(R.string.application));
                    zambia.setVisibility(View.GONE);
                    break;
                default:
                    download.setText(x.app().getString(R.string.download));
                    break;
            }
        }
    }

    class DownloadThemeAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mThemeEntity.getImageList().size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(DownloadTheme.this).inflate(R.layout.image_pager_item_layout,container,false);
            ImageView imageView = (ImageView) view.findViewById(R.id.id_pager_image);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoader.getInstance().displayImage(mThemeEntity.getImageList().get(position).getPath(),imageView,options);
            container.addView(view);
            return view;
        }
    }
}
