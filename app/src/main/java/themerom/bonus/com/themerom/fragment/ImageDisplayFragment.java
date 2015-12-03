package themerom.bonus.com.themerom.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import themerom.bonus.com.themerom.R;
import themerom.bonus.com.themerom.view.MyViewPager;

/**
 * Created by bonus on 11/18/15.
 * Class name ${type_name}
 */
public class ImageDisplayFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = ImageDisplayFragment.class.getSimpleName();
    private DisplayImageOptions mOptions;
    private LayoutInflater mInflater;
    private LinearLayout mLayoutLeft;
    private LinearLayout mLayoutRight;
    private LinearLayout mLayoutDrag;
    private Map<Integer,ImageView> imageViewMap = new HashMap<>();
    private Button mSave,mSetWallpaper,mSetLockpaper,mZambia,mClose;
    private MyViewPager mViewPager;
    private ImageView mDrag;
    private int mPosition;
    private int[] originalIds;
    private ViewPagerAdapter mAdapter;
    private boolean dragStatus;
    private int popStatus = 0;
    private int mWidth;
    private int mHeight;
    private float curX;
    private float downX;
    private float offSetX;
    private long downTime = 0;
    private long upTime = 0;
    private float downPosition = 0;
    private float upPosition = 0;
    private static final int POP_MIN_WIDTH = 200;
    private AlertDialog mDialog;
    private WallpaperManager mWallpaperManager;
    private static final int MSG_SET_WALLPAPER = 0x01;
    private static final int MSG_SET_LOCKPAPER = 0x02;
    private TextView dialogTitle;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_SET_WALLPAPER:
                    if(mDialog != null){
                        dialogTitle.setText("Set wallpaper, please wait a mount!");
                        mDialog.show();
                    }
                    break;
                case MSG_SET_LOCKPAPER:
                    if(mDialog != null){
                        dialogTitle.setText("Set lock wallpaper, please wait a mount!");
                        mDialog.show();
                    }
                    break;
            }
            if(this != null){
                this.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mDialog != null && this != null){
                            mDialog.cancel();
                        }
                    }
                },1000);
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                        // drawable
                .showImageForEmptyUri(R.drawable.ic_empty)
                        // drawable
                .showImageOnFail(R.drawable.ic_error)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.NONE)
                        // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .build();
        mWidth = getResources().getDisplayMetrics().widthPixels;
        mHeight = getResources().getDisplayMetrics().heightPixels;
        mWallpaperManager = WallpaperManager.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mInflater = inflater;
        View view = mInflater.inflate(R.layout.image_display_layout, null);
        mViewPager = (MyViewPager) view.findViewById(R.id.id_viewpager);
        mLayoutLeft = (LinearLayout) view.findViewById(R.id.id_choice_left);
        mLayoutRight = (LinearLayout) view.findViewById(R.id.id_choice_right);
        mLayoutDrag = (LinearLayout) view.findViewById(R.id.id_drag);
        mDrag = (ImageView) view.findViewById(R.id.iv_pager_drag);
        mLayoutDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView imageView = imageViewMap.get(originalIds[mPosition]);
                Log.d(TAG,"imageview = "+(imageView == null));
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        mViewPager.setNoScrool(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        curX = event.getX();
                        offSetX += downX - curX;
                        if(imageView != null){
                            if(offSetX > -90 && offSetX < 90 && mDrag.getScrollX() > -87 &&
                                    mDrag.getScrollX() < 87 && imageView.getScrollX() > -360 && imageView.getScrollX() < 360){
                                Log.d(TAG,"SCROLLBY = "+(downX - curX));
                                imageView.scrollBy((int) (downX - curX)*3,0);
                                mDrag.scrollBy((int) (downX - curX),0);
                            }
                        }
                        downX = event.getX();
                        if(mDrag.getScrollX() <= -87 || mDrag.getScrollX() >= 87){
                            int scrollX = mDrag.getScrollX();
                            if(scrollX <= -87){
                                scrollX = -86;
                            }else if(scrollX >= 87){
                                scrollX = 86;
                            }
                            mDrag.setScrollX(scrollX);
                        }
                        if(imageView != null){
                            if(imageView.getScrollX() <= -360 || imageView.getScrollX() >= 360){
                                int iScrollX = imageView.getScrollX();
                                if(imageView.getScrollX() <= -360){
                                    iScrollX = -359;
                                }else if(imageView.getScrollX() >= 360){
                                    iScrollX =359;
                                }
                                imageView.setScrollX(iScrollX);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if(imageView != null){
                            imageView.scrollTo(0,0);
                        }
                        mDrag.scrollTo(0,0);
                        offSetX = 0;
                        mViewPager.setNoScrool(false);
                        break;
                }
                return true;//for drag must return true
            }
        });

        mPosition = getArguments().getInt("position");
        originalIds = getArguments().getIntArray("originalIds");
        mAdapter = new ViewPagerAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setCurrentItem(mPosition);
        Log.d(TAG,"oncreate mposition = "+mPosition);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                long offsetTime = 0;
                float arrange = 0;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    downTime = event.getEventTime();
                    downPosition = event.getX();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    upTime = event.getEventTime();
                    upPosition = event.getX();
                }
                offsetTime = upTime - downTime;
                arrange = upPosition - downPosition;
                if (offsetTime > 0 && offsetTime < 400 && arrange > -40 && arrange < 40) {
                    // click
                    float x = event.getX();
                    float y = event.getY();
                    if (popStatus == 1) {
                        closePopupWindow();
                    } else {
                        openPopupWindow(x, y);
                    }
                }
                return false;
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                Log.d(TAG,"onpagese mposition = "+mPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    /**
     * show popupwindow
     */
    private void openPopupWindow(float curX, float curY) {
        if(curX > mWidth/2){
            //right
            setAnimation(mLayoutRight,R.id.save,getActivity(),R.anim.popup_anim_right_in);
            setAnimation(mLayoutRight,R.id.set_lock,getActivity(),R.anim.popup_anim_right_in);
            setAnimation(mLayoutRight,R.id.set,getActivity(),R.anim.popup_anim_right_in);
            setAnimation(mLayoutRight,R.id.zambia,getActivity(),R.anim.popup_anim_right_in);
            setAnimation(mLayoutRight,R.id.close,getActivity(),R.anim.popup_anim_right_in);
            initPopup(curX, curY, mLayoutRight);
            mLayoutRight.setVisibility(View.VISIBLE);
        }else {
            setAnimation(mLayoutLeft,R.id.save,getActivity(),R.anim.popup_anim_left_in);
            setAnimation(mLayoutLeft,R.id.set_lock,getActivity(),R.anim.popup_anim_left_in);
            setAnimation(mLayoutLeft,R.id.set,getActivity(),R.anim.popup_anim_left_in);
            setAnimation(mLayoutLeft,R.id.zambia,getActivity(),R.anim.popup_anim_left_in);
            setAnimation(mLayoutLeft,R.id.close,getActivity(),R.anim.popup_anim_left_in);
            initPopup(curX, curY, mLayoutLeft);
            mLayoutLeft.setVisibility(View.VISIBLE);
        }
        popStatus++;
    }

    private void initPopup(float curX, float curY, LinearLayout mLayout) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLayout.getLayoutParams();
        int layoutHeight = mLayout.getHeight();
        if (curY < layoutHeight / 4) {
            curY = layoutHeight / 5;
        }
        if (curY > mHeight - 520) {
            curY = mHeight - 520;
        }
        params.topMargin = (int) curY;
        mSave = (Button) mLayout.findViewById(R.id.save);
        mSetLockpaper = (Button) mLayout.findViewById(R.id.set_lock);
        mSetWallpaper = (Button) mLayout.findViewById(R.id.set);
        mZambia = (Button) mLayout.findViewById(R.id.zambia);
        mClose = (Button) mLayout.findViewById(R.id.close);

        mSave.setOnClickListener(this);
        mSetLockpaper.setOnClickListener(this);
        mSetWallpaper.setOnClickListener(this);
        mZambia.setOnClickListener(this);
        mClose.setOnClickListener(this);

        LinearLayout.LayoutParams saveParmas = (LinearLayout.LayoutParams) mSave.getLayoutParams();
        saveParmas.width = (int) (POP_MIN_WIDTH+((Math.random()*mWidth/2)< 100? 250 : (Math.random()*mWidth/2)));

        LinearLayout.LayoutParams setLockParams = (LinearLayout.LayoutParams) mSetLockpaper.getLayoutParams();
        setLockParams.width = (int) (POP_MIN_WIDTH+((Math.random()*mWidth/2)< 20? 300 : (Math.random()*mWidth/2)));

        LinearLayout.LayoutParams setWallParams = (LinearLayout.LayoutParams) mSetWallpaper.getLayoutParams();
        setWallParams.width = (int) (POP_MIN_WIDTH+((Math.random()*mWidth/2)< 250? 250 : (Math.random()*mWidth/2)));

        LinearLayout.LayoutParams setZambia = (LinearLayout.LayoutParams) mZambia.getLayoutParams();
        setZambia.width = (int) (POP_MIN_WIDTH+((Math.random()*mWidth/2)< 100? 100 : (Math.random()*mWidth/2)));

        LinearLayout.LayoutParams closeParams = (LinearLayout.LayoutParams) mClose.getLayoutParams();
        closeParams.width = (int) (POP_MIN_WIDTH+((Math.random()*mWidth/2)< 100? 100 : (Math.random()*mWidth/2)));

        mSave.setLayoutParams(saveParmas);
        mSetLockpaper.setLayoutParams(setLockParams);
        mSetWallpaper.setLayoutParams(setWallParams);
        mZambia.setLayoutParams(setZambia);
        mClose.setLayoutParams(closeParams);

        mLayout.setLayoutParams(params);
    }

    /**
     * hide popupwindow
     */
    private void closePopupWindow() {
        if(mLayoutLeft.getVisibility() == View.VISIBLE){
            mLayoutLeft.setVisibility(View.GONE);
            mLayoutLeft.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.popup_anim_left_out));
        }else if(mLayoutRight.getVisibility() == View.VISIBLE){
            mLayoutRight.setVisibility(View.GONE);
            mLayoutRight.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.popup_anim_right_out));
        }
        popStatus--;
    }

    private void setAnimation(View view, int id, Context context, int anim){
        view.clearAnimation();
        view.findViewById(id).startAnimation(AnimationUtils.loadAnimation(context, anim));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        mDialog = new AlertDialog.Builder(getActivity()).create();
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_view_layout,null);
        final ProgressBar progressBar = (ProgressBar) dialogView.findViewById(R.id.dialog_progressbar);
        dialogTitle = (TextView) dialogView.findViewById(R.id.title_progressbar);
        mDialog.setView(dialogView);
        Message msg = mHandler.obtainMessage();
        switch (id){
            case R.id.save:
                break;
            case R.id.set_lock:
                mHandler.sendEmptyMessage(MSG_SET_LOCKPAPER);
                setLockPaper();
                break;
            case R.id.set:
                setWallpaper();
                mHandler.sendEmptyMessage(MSG_SET_WALLPAPER);
                break;
            case R.id.zambia:
                break;
            case R.id.close:
                getActivity().finish();
                break;
        }
    }

    /**
     * set lock paper
     */
    private void setLockPaper() {
        if(mWallpaperManager == null){
            mWallpaperManager = WallpaperManager.getInstance(getActivity());
        }
        int resourceId = originalIds[mPosition - 1];
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),resourceId);
        Bitmap finalBitmap = decodeBitmap(bitmap);
        Class zClass = mWallpaperManager.getClass();
        try {
            Method lockPaperMethod = zClass.getMethod("setBitmapToLockWallpaper",Bitmap.class);
            lockPaperMethod.invoke(mWallpaperManager,finalBitmap);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }finally{
            if(bitmap != null){
                bitmap.recycle();
                bitmap = null;
            }
            if(finalBitmap != null){
                finalBitmap.recycle();
                finalBitmap = null;
            }
        }
    }

    private Bitmap decodeBitmap(Bitmap bitmap){
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bout);
        while(bout.toByteArray().length/1024 > 1024){
            bout.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,bout);
        }
        ByteArrayInputStream binput = new ByteArrayInputStream(bout.toByteArray());
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        Bitmap decodeBitmap = BitmapFactory.decodeStream(binput,null,options);
//        options.inJustDecodeBounds = false;
//        int decodeWidth = 1;
//        int decodeHeight = 1;
//        if(options.outWidth > mWidth){
//            decodeWidth = options.outWidth/mWidth;
//        }
//        if(options.outHeight > mWidth){
//            decodeHeight = options.outHeight / mHeight;
//        }
//        options.inSampleSize = decodeWidth < decodeHeight ? decodeWidth : decodeHeight;
//        binput = new ByteArrayInputStream(bout.toByteArray());
        return BitmapFactory.decodeStream(binput,null,null);
    }

    /**
     * set wallpaper
     * 1:setBitmap
     * 2:setResource
     * 3:setStream
     */
    private void setWallpaper() {
        Log.d(TAG,"mPosition = "+mPosition);
        if(mWallpaperManager == null){
            mWallpaperManager = WallpaperManager.getInstance(getActivity());
        }
        int resourceId = originalIds[mPosition -1];
        try {
            mWallpaperManager.setResource(resourceId);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Drawable resourceDrawable = getResources().getDrawable(resourceId);
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) resourceDrawable;
//        Bitmap bitmap = bitmapDrawable.getBitmap();
//
//        ByteArrayOutputStream bout = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bout);
//        InputStream is = new ByteArrayInputStream(bout.toByteArray());
//
//        try {
//            mWallpaperManager.setStream(is);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    class ViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return originalIds != null ? originalIds.length: 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.image_pager_item_layout,container,false);
            ImageView imageView = (ImageView) view.findViewById(R.id.id_pager_image);
            ImageLoader.getInstance().displayImage("drawable://"+originalIds[position],imageView,mOptions);
            mPosition = position;
//            Drawable drawable = getResources().getDrawable(originalIds[position]);
//            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//            Bitmap bitmap = bitmapDrawable.getBitmap();
//            Log.d(TAG,"width = "+bitmap.getWidth()+", "+mWidth);
//            if(bitmap.getWidth() > mWidth){
//                dragStatus = true;
//            }else{
//                dragStatus = false;
//            }
//            mLayoutDrag.setVisibility(dragStatus?View.VISIBLE:View.GONE);
            container.addView(view,0);
            imageViewMap.put(originalIds[position], imageView);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            imageViewMap.remove(originalIds[position]);
        }
    }
}
