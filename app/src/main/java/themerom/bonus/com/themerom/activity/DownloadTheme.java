package themerom.bonus.com.themerom.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
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
import themerom.bonus.com.themerom.callback.ProgressListener;
import themerom.bonus.com.themerom.contants.Contacts;
import themerom.bonus.com.themerom.db.DownloadDao;
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
public class DownloadTheme extends Activity implements BackImage.OnBackClickListener,View.OnClickListener{
    private static final String TAG = "BTAG";
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
    private SharedPreferences mPreferences;
    //for xutils3
    private DownloadManager manager;
    private DownloadItemViewHolder holder;
    //for custom break point download
    private DownloadService mService;
    private themerom.bonus.com.themerom.service.DownloadService.MyBinder myBinder;
    private String url = "http://muldown.fuli365.net/online_wallpaper/topicapk/com.shly.theme.doraemon.apk";
    private String mLabel;
    private String mSavePath;
    private String mFileName;
    private int mThreadSize;
    private static final int SEND_MAX_CODE = 0x01;
    private static final int SEND_PROGRESS_CODE = 0x02;
    private static final int SEND_SUCCESS_CODE = 0x03;
    private static final int SEND_FAILED_CODE = 0x04;
    private long maxLenght;
    private String convertMax;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (themerom.bonus.com.themerom.service.DownloadService.MyBinder) service;
            Log.d(TAG,"onServiceConnected myBinder = "+(myBinder == null));
            myBinder.download(url, mSavePath, new ProgressListener() {
                @Override
                public void setMax(long max) {
                    maxLenght = max;
                    String maxString = BonusImageUtil.convertStorage(max);
                    Message msg = mHandler.obtainMessage();
                    msg.what = SEND_MAX_CODE;
                    msg.obj = maxString;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void setProgress(long progress) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = SEND_PROGRESS_CODE;
                    msg.obj = progress;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onSuccess(String result) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = SEND_SUCCESS_CODE;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onFailed(String result, Exception e) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = SEND_FAILED_CODE;
                    msg.obj = e;
                    mHandler.sendMessage(msg);
                }
            },mThreadSize,mLabel,mFileName);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"onServiceDisconnected name = "+name);
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SEND_MAX_CODE:
                    String str = (String) msg.obj;
                    convertMax = str;
                    download.setText("大小："+str);
                    break;
                case SEND_PROGRESS_CODE:
                    long progress = (long) msg.obj;
                    int percent = (int) (progress*100/maxLenght);
                    download.setText("下载："+percent+" / "+convertMax);
                    break;
                case SEND_SUCCESS_CODE:
                    download.setText("应用");
                    break;
                case SEND_FAILED_CODE:
                    Exception e = (Exception) msg.obj;
                    Log.d(TAG,"download fail "+e.toString());
                    download.setText("下载");
                    Toast.makeText(DownloadTheme.this,"请检查网络链接，重新下载！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_theme_layout);
        mThemeEntity = (ThemeEntity) getIntent().getExtras().getSerializable(Contacts.DOWNLOAD_THEME_ENTITY);
        mPreferences = getSharedPreferences(Contacts.SHARE_PREFERENCE, Context.MODE_PRIVATE);
        initOptions();
        initView();
        mTitle.setText(mThemeEntity.getThemename());
        manager = DownloadService.getDownloadManager();
        apkName = mThemeEntity.getPackageName().substring(
                mThemeEntity.getPackageName().lastIndexOf("/") + 1,
                mThemeEntity.getPackageName().lastIndexOf("."));
        String label = apkName;
        mLabel = label;
        mSavePath = BonusImageUtil.getStoragePath(this)+"/sdcard/breakPoint/";
        mFileName = mThemeEntity.getThemename()+".apk";
        mThreadSize = Runtime.getRuntime().availableProcessors();
        //note for close xutils3 start
//        try {
//            if (!mPreferences.getBoolean(apkName, false)) {
//                manager.startDownload(mThemeEntity.getPackageName(), label, "/sdcard/xUtils/" + mThemeEntity.getThemename() + ".apk", false, false, null);
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//
//        DownloadInfo downloadInfo = manager.getDownloadInfo(0);
//        Log.d(TAG, "downloadInfo = " + (downloadInfo == null) + ", " + apkName);
//        if (downloadInfo != null) {
//            holder = new DownloadItemViewHolder(download, downloadInfo);
//            holder.refresh();
//            Log.d(TAG, "downloadInfo state= " + downloadInfo.getState().value());
//            if (downloadInfo.getState().value() < DownloadState.FINISHED.value()) {
//                try {
//                    manager.startDownload(
//                            downloadInfo.getUrl(),
//                            downloadInfo.getLabel(),
//                            downloadInfo.getFileSavePath(),
//                            downloadInfo.isAutoResume(),
//                            downloadInfo.isAutoRename(),
//                            holder);
//                } catch (DbException ex) {
//                    Toast.makeText(x.app(), "添加下载失败", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
        //note for close xutils3 end
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
        download.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.id_button_download:
                if(zambia.getVisibility() == View.VISIBLE){
                    zambia.setVisibility(View.GONE);
                }
                //start service
                if(!BonusImageUtil.isNetWorkAvailable(this)){
                    return;
                }
                Intent downloadService = new Intent(DownloadTheme.this,DownloadService.class);
                boolean b = bindService(downloadService, conn, Context.BIND_AUTO_CREATE);
                Log.d(TAG,"b = "+b);
                if(DownloadDao.isDownLoad){
                    DownloadDao.isDownLoad = false;
                    download.setText("暂停");
                }else{
                    DownloadDao.isDownLoad = true;
                }
//                myBinder.download(url, mSavePath, new ProgressListener() {
//                    @Override
//                    public void setMax(long max) {
//                        maxLenght = max;
//                        String maxString = BonusImageUtil.convertStorage(max);
//                        Message msg = mHandler.obtainMessage();
//                        msg.what = SEND_MAX_CODE;
//                        msg.obj = maxString;
//                        mHandler.sendMessage(msg);
//                    }
//
//                    @Override
//                    public void setProgress(long progress) {
//                        Message msg = mHandler.obtainMessage();
//                        msg.what = SEND_PROGRESS_CODE;
//                        msg.obj = progress;
//                        mHandler.sendMessage(msg);
//                    }
//
//                    @Override
//                    public void onSuccess(String result) {
//                        Message msg = mHandler.obtainMessage();
//                        msg.what = SEND_SUCCESS_CODE;
//                        msg.obj = result;
//                        mHandler.sendMessage(msg);
//                    }
//
//                    @Override
//                    public void onFailed(String result, Exception e) {
//                        Message msg = mHandler.obtainMessage();
//                        msg.what = SEND_FAILED_CODE;
//                        msg.obj = e;
//                        mHandler.sendMessage(msg);
//                    }
//                },mThreadSize,mLabel,mFileName);
                break;
            case R.id.id_button_zambia:
                break;
            case R.id.id_button_delete:
                break;
            default:
                break;
        }
    }

    class DownloadItemViewHolder extends DownloadViewHolder {

        public DownloadItemViewHolder(View view, DownloadInfo downloadInfo) {
            super(view, downloadInfo);
            refresh();
        }

        @Event(R.id.id_button_download)//for click download
        private void toggleEvent(View view) {
            DownloadState state = downloadInfo.getState();
            switch (state) {
                case WAITING:
                    Log.d(TAG, "waiting");
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
                    Log.d(TAG, "started");
                    manager.stopDownload(downloadInfo);
                    break;
                case ERROR:
                    Log.d(TAG, "error");
                case STOPPED:
                    Log.d(TAG, "stopped");
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
                    Log.d(TAG, "finished");
                    Toast.makeText(x.app(), "已经下载完成", Toast.LENGTH_LONG).show();
                    // TODO: 12/7/15 install apk
                    PackageManager packageManager = getPackageManager();
                    try {
                        PackageInfo info = packageManager.getPackageInfo(apkName, 0);

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

        @Event(R.id.id_button_delete)
        private void toggleDelete(View view) {
            BonusImageUtil.toast(DownloadTheme.this, "delete", Contacts.TOAST_SHORT_DURATION);
            mPreferences.edit().remove(apkName).commit();
            // TODO: 12/7/15 delete apk         
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
                    Log.d(TAG, "refresh waiting");
                case STARTED:
                    Log.d(TAG, "refresh started ");
                    download.setText(downloadInfo.getProgress() + "%");
                    zambia.setVisibility(View.GONE);
                    break;
                case ERROR:
                    Log.d(TAG, "refresh error");
                    BonusImageUtil.toast(DownloadTheme.this, "网络链接错误或网速较低，以断开链接！", Contacts.TOAST_SHORT_DURATION);
                case STOPPED:
                    Log.d(TAG, "refresh stopped");
                    download.setText(x.app().getString(R.string.download_continue));
                    break;
                case FINISHED:
                    Log.d(TAG, "refresh finished");
                    download.setText(x.app().getString(R.string.application));
                    zambia.setVisibility(View.VISIBLE);
                    mPreferences.edit().putBoolean(apkName, true).commit();
                    delete.setVisibility(View.VISIBLE);
                    break;
                default:
                    download.setText(x.app().getString(R.string.download));
                    break;
            }
        }
    }

    class DownloadThemeAdapter extends PagerAdapter {

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
            View view = LayoutInflater.from(DownloadTheme.this).inflate(R.layout.image_pager_item_layout, container, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.id_pager_image);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoader.getInstance().displayImage(mThemeEntity.getImageList().get(position).getPath(), imageView, options);
            container.addView(view);
            return view;
        }
    }

    class SetThemeTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
                Object am = activityManagerNative.getMethod("getDefault",activityManagerNative);
                Object config = am.getClass().getMethod("getConfiguration",am.getClass());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            BonusImageUtil.toast(DownloadTheme.this, "set theme", Contacts.TOAST_SHORT_DURATION);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        unbindService(conn);
    }
}
