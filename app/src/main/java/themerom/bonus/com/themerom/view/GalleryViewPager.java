package themerom.bonus.com.themerom.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by helios on 11/7/15.
 */
public class GalleryViewPager extends Gallery implements AdapterView.OnItemClickListener, View.OnTouchListener,
        AdapterView.OnItemSelectedListener{

    private Context mContext;
    private GalleryOnItemClickListener mListener;
    private int mSwitchTime;
    private Timer mTimer;
    private LinearLayout mOvalLayout;
    private int curIndex = 0;
    private int oldIndex = 0;
    private int mFocusId ;
    private int mNormalId ;
    private int[] mAdId;
    private String[] mUris;
    private List<ImageView> listImgs;
    private TextView mAdTextView;
    private String[] mAdText;
    private static final int MSG_SWITCH_IMG = 0x01;

    //启动方法--资源入口
    public void start(Context context, int switchTime, LinearLayout ovalLayout,int focusId,
                      int normalId, int[] adId, String[] uris, TextView adTextView, String[] adText){

        this.mContext = context;
        this.mSwitchTime = switchTime;
        this.mOvalLayout = ovalLayout;
        this.mFocusId = focusId;
        this.mNormalId = normalId;
        this.mAdId = adId;
        this.mUris = uris;
        this.mAdTextView = adTextView;
        this.mAdText = adText;

        //初始化图片
        initImage();
        setAdapter(new AdAdapter());
        setOnItemClickListener(this);
        setOnItemSelectedListener(this);
        setOnTouchListener(this);
        setSoundEffectsEnabled(false);
        setAnimationDuration(800);
        setUnselectedAlpha(1f);
        setSpacing(0);
        setSelection(getCount()/2);//center
        setFocusableInTouchMode(true);
        initOvalLayout();

        startTimer();
    }

    private void startTimer() {
        if(mTimer == null && listImgs.size() >1 && mSwitchTime >0){
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(MSG_SWITCH_IMG);
                }
            },mSwitchTime,mSwitchTime);
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_SWITCH_IMG:
                    onScroll(null,null,1,0);
                    onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
                    break;
            }
        }
    };

    private void initOvalLayout() {
        if(mOvalLayout != null && listImgs.size() <2){
            mOvalLayout.removeAllViews();
            mOvalLayout.getLayoutParams().height = 0;
        }else if(mOvalLayout != null){
            mOvalLayout.removeAllViews();
            int ovalHeight = (int) (mOvalLayout.getLayoutParams().height*0.7);
            int ovalMargin = (int)(mOvalLayout.getLayoutParams().height*0.2);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ovalHeight,ovalHeight);
            params.setMargins(ovalMargin,0,ovalMargin,0);
            for (int i = 0; i<listImgs.size();i++){
                View view = new View(mContext);
                view.setLayoutParams(params);
                view.setBackgroundResource(mNormalId);
                mOvalLayout.addView(view);
            }
            mOvalLayout.getChildAt(0).setBackgroundResource(mFocusId);
        }
    }

    private void initImage() {
        listImgs = new ArrayList<>();
        int len = mUris != null?mUris.length:mAdId.length;
        for (int i = 0;i<len;i++){
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            if(mUris == null){
                imageView.setImageResource(mAdId[i]);
            }else{
                //todo net work

            }
            listImgs.add(imageView);
        }
    }

    class AdAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(listImgs.size() < 2)
                return listImgs.size();
            return Integer.MAX_VALUE;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return listImgs.get(position%listImgs.size());
        }
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public GalleryViewPager(Context context) {
        super(context);
    }

    public GalleryViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GalleryViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2,int velocityX, int velocityY){
        int event;
        if(isScroolLeft(e1, e2)){
            event = KeyEvent.KEYCODE_DPAD_LEFT;
        }else{
            event = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        onKeyDown(event, null);
        return true;
    }

    private boolean isScroolLeft(MotionEvent e1,MotionEvent e2){
        return e2.getX() >(e1.getX() + 50);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mListener != null){
            mListener.onItemClick(position);
        }
    }

    public void setGalleryOnItemClickListener(GalleryOnItemClickListener listener){
        this.mListener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int key = event.getAction();
        if(key == MotionEvent.ACTION_UP || key == MotionEvent.ACTION_CANCEL){
            Log.d("bonus","startTimer");
            startTimer();
        }else{
            stopTimer();
        }
        return false;
    }

    private void stopTimer() {
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        curIndex = position%listImgs.size();
        if(mOvalLayout != null && listImgs.size() >1){
            mOvalLayout.getChildAt(curIndex).setBackgroundResource(mFocusId);
            mOvalLayout.getChildAt(oldIndex).setBackgroundResource(mNormalId);
            oldIndex = curIndex;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface GalleryOnItemClickListener{
        void onItemClick(int position);
    }
}
