package themerom.bonus.com.themerom.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import themerom.bonus.com.themerom.R;
import themerom.bonus.com.themerom.contants.Contacts;
import themerom.bonus.com.themerom.fragment.WallpaperGalleryFragment;
import themerom.bonus.com.themerom.fragment.WallpaperGridViewFragment;
import themerom.bonus.com.themerom.fragment.WallpaperWaterfallFragment;
import themerom.bonus.com.themerom.utils.ThemeUtil;
import themerom.bonus.com.themerom.view.BackImage;

/**
 * Created by bonus on 11/17/15.
 * Class name ${type_name}
 */
public class WallpaperActivity extends Activity implements BackImage.OnBackClickListener,View.OnClickListener{
    private static final String TAG = WallpaperActivity.class.getSimpleName();
    private BackImage mBack;
    private TextView mTitle;
    private ImageView mSet;
    private int mStyle = 0;
    public static final String WALLPAPER_ORIGINAL_PATH = "/media/wallpapers/original";
    public static final String WALLPAPER_THUMBNAIL_PATH = "/media/wallpapers/thumbnail";
    private SharedPreferences mPreferences;
    private WallpaperGalleryFragment mGalleryFragment;
    private WallpaperGridViewFragment mGridViewFragment;
    private WallpaperWaterfallFragment mWaterfallFragment;
    private PopupWindow popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpaper_detail_layout);
        Intent intent = getIntent();
        mStyle = intent.getIntExtra(Contacts.WALLPAPER_STYLE,0);
        initActionBar();
        mPreferences = getSharedPreferences(Contacts.SHARE_PREFERENCE, Context.MODE_PRIVATE);
        mStyle = mPreferences.getInt(Contacts.WALLPAPER_STYLE,Contacts.GALLERY_STYLE);
        Log.d("bonus","mStyle = "+mStyle);
        setStyle(mStyle);
    }

    private void setStyle(int style){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction mTransaction = manager.beginTransaction();
        switch(style){
            case Contacts.GALLERY_STYLE:
                if(mGalleryFragment == null){
                    mGalleryFragment = new WallpaperGalleryFragment();
                }

                mTransaction.add(R.id.id_content,mGalleryFragment,Contacts.GALLERY_TAG);
                mTransaction.commit();
                break;
            case Contacts.GRIDVIEW_STYLE:
                if(mGridViewFragment == null){
                    mGridViewFragment = new WallpaperGridViewFragment();
                }
                mTransaction.add(R.id.id_content,mGridViewFragment,Contacts.GRIDVIEW_TAG);
                mTransaction.commit();
                break;
            case Contacts.WATERFALL_STYLE:
                if(mWaterfallFragment == null){
                    mWaterfallFragment = new WallpaperWaterfallFragment();
                }
                mTransaction.add(R.id.id_content,mWaterfallFragment,Contacts.WATERFALL_TAG);
                mTransaction.commit();
                break;
        }
    }

    private void initActionBar() {
        mBack = (BackImage) findViewById(R.id.id_back);
        mTitle = (TextView) findViewById(R.id.id_actionbar_title);
        mSet = (ImageView) findViewById(R.id.id_set);
        mSet.setOnClickListener(this);
        mBack.setOnBackClickListener(this);
    }


    @Override
    public void onBackClick() {
        finish();
    }


    @Override
    public void onClick(View v) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction mTransaction = manager.beginTransaction();
        int id = v.getId();
        switch (id){
            case R.id.id_set:
                //show popwindow
                View view = LayoutInflater.from(this).inflate(R.layout.wallpaper_style_pop_layout,null);
                TextView id_gallery = (TextView) view.findViewById(R.id.id_gallery);
                TextView id_gridview = (TextView) view.findViewById(R.id.id_gridview);
                TextView id_waterfall = (TextView) view.findViewById(R.id.id_waterfall);
                id_gallery.setOnClickListener(this);
                id_gridview.setOnClickListener(this);
                id_waterfall.setOnClickListener(this);
                popup = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popup.setBackgroundDrawable(new ColorDrawable(0xE0EEEE));
                popup.setFocusable(true);
                popup.setOutsideTouchable(true);
                popup.setAnimationStyle(R.style.popup_anim_style);
                popup.showAsDropDown(findViewById(R.id.id_include));
                break;
            case R.id.id_gallery:
                if(popup.isShowing()){
                    popup.dismiss();
                }
                if(mStyle == Contacts.GALLERY_STYLE){
                    return;
                }
//                mPreferences.edit().putInt(Contacts.WALLPAPER_STYLE,Contacts.GALLERY_STYLE).commit();
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putInt(Contacts.WALLPAPER_STYLE,Contacts.GALLERY_STYLE).commit();
                mStyle = Contacts.GALLERY_STYLE;
                if(mGalleryFragment == null){
                    mGalleryFragment = new WallpaperGalleryFragment();
                }
                mTransaction.replace(R.id.id_content,mGalleryFragment,Contacts.GALLERY_TAG);
                mTransaction.commit();

                break;
            case R.id.id_gridview:
                if(popup.isShowing()){
                    popup.dismiss();
                }
                if(mStyle == Contacts.GRIDVIEW_STYLE){
                    return;
                }
                mStyle = Contacts.GRIDVIEW_STYLE;
                mPreferences.edit().putInt(Contacts.WALLPAPER_STYLE,mStyle).commit();
                int i = mPreferences.getInt(Contacts.WALLPAPER_STYLE,Contacts.GALLERY_STYLE);
                Log.d("bonus","i = "+i);
                if(mGridViewFragment == null){
                    mGridViewFragment = new WallpaperGridViewFragment();
                }
                mTransaction.replace(R.id.id_content,mGridViewFragment,Contacts.GRIDVIEW_TAG);
                mTransaction.commit();
                break;
            case R.id.id_waterfall:
                if(popup.isShowing()){
                    popup.dismiss();
                }
                ThemeUtil.toast(this,"waterfall",Contacts.TOAST_SHORT_DURATION);
                if(mStyle == Contacts.WATERFALL_STYLE){
                    return;
                }
                mStyle = Contacts.WATERFALL_STYLE;
                mPreferences.edit().putInt(Contacts.WALLPAPER_STYLE,mStyle).commit();
                if(mWaterfallFragment == null){
                    mWaterfallFragment = new WallpaperWaterfallFragment();
                }
                mTransaction.replace(R.id.id_content, mWaterfallFragment, Contacts.WATERFALL_TAG);
                mTransaction.commit();
                break;
        }
    }
}
