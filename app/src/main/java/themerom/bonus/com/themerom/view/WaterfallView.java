package themerom.bonus.com.themerom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import themerom.bonus.com.themerom.R;
import themerom.bonus.com.themerom.contants.Contacts;
import themerom.bonus.com.themerom.utils.BonusImageUtil;
import themerom.bonus.com.themerom.utils.FallImageLoader;

/**
 * Created by bonus on 12/3/15.
 * Class name ${type_name}
 */
public class WaterfallView extends ScrollView implements View.OnTouchListener{
    private static final String TAG = WaterfallView.class.getSimpleName();
    private Context mContext;
    private int background;
    private int columnNum;
    private LinearLayout firstLayout;
    private LinearLayout secondLayout;
    private int firstLayoutHeight;
    private int secondLayoutHeight;
    private static final int PAGE_SIZE = 15;
    private int mLayoutWidth;
    private int page;
    private boolean loadOnce;
    private FallImageLoader imageLoader;
    /**
     * MyScrollView下的直接子布局。
     */
    private static View scrollLayout;

    /**
     * 布局的高度。
     */
    private static int scrollViewHeight;

    /**
     * 布局的宽度
     */
    private static int scrollViewWidth;

    private int columnWidth;

    /**
     * 记录上垂直方向的滚动距离。
     */
    private static int lastScrollY = -1;

    /**
     * 记录所有界面上的图片，用以可以随时控制对图片的释放。
     */
    private List<ImageView> imageViewList = new ArrayList<ImageView>();

    private ImageView imageView;
    /**
     * 在Handler中进行图片可见性检查的判断，以及加载更多图片的操作。
     */
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            WaterfallView message = (WaterfallView) msg.obj;
            int scrollY = message.getScrollY();
            if(scrollY == lastScrollY){
                if(scrollViewHeight + scrollY >= scrollLayout.getHeight()){
                    message.loadMoreImage();
                }
                message.checkVisibility();
            }else{
                lastScrollY = scrollY;
                Message msg2 = new Message();
                msg2.obj = message;
                sendMessageDelayed(msg2,5);
            }
        }
    };

    public WaterfallView(Context context) {
        this(context, null);
    }

    public WaterfallView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterfallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.waterfallView);
        background = array.getColor(R.styleable.waterfallView_waterfallBackground,0xE0EEEE);
        columnNum = array.getInteger(R.styleable.waterfallView_columNum,3);
        imageLoader = FallImageLoader.getInstance(context);
        setOnTouchListener(this);
        scrollViewWidth = getWidth();
        Log.d(TAG,"scrollViewWidth = "+scrollViewWidth);
        array.recycle();
    }

    public void checkVisibility() {
        for (int i = 0; i < imageViewList.size(); i++) {
            ImageView imageView = imageViewList.get(i);
            int borderTop = (Integer) imageView.getTag(R.string.bolder_top);
            int borderBottom = (Integer) imageView
                    .getTag(R.string.bolder_bottom);
            if (borderBottom > getScrollY()
                    && borderTop < getScrollY() + scrollViewHeight) {
                String imageUrl = (String) imageView.getTag(R.string.image_url);
                Bitmap bitmap = imageLoader.getImageBitmap(imageUrl,columnWidth);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(R.drawable.empty_photo);
                }
            } else {
                imageView.setImageResource(R.drawable.empty_photo);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed&&!loadOnce){
            scrollViewHeight = getHeight();
            scrollLayout = getChildAt(0);
            firstLayout = (LinearLayout) findViewById(R.id.first_column);
            secondLayout = (LinearLayout) findViewById(R.id.second_column);
            columnWidth = firstLayout.getWidth();
            loadOnce = true;
            //load image
            loadMoreImage();
        }
    }

    private void loadMoreImage() {
        int startIndex = page*PAGE_SIZE;
        int endIndex = startIndex + PAGE_SIZE;
        if(startIndex < Contacts.imageThumbUrls.length){
            BonusImageUtil.toast(getContext(),"正在加载中……",Contacts.TOAST_SHORT_DURATION);
            if(endIndex > Contacts.imageThumbUrls.length){
                endIndex = Contacts.imageThumbUrls.length;
            }
            for (int i = startIndex ;i < endIndex ;i++){
                Bitmap  bitmap = FallImageLoader.getInstance(mContext).getImageBitmap(Contacts.imageThumbUrls[i],columnWidth);
                Log.d(TAG,"bitmap = "+(bitmap == null));
                if(bitmap != null){
                    addImageView(bitmap,columnWidth,bitmap.getHeight(),Contacts.imageThumbUrls[i]);
                }
            }
            page++;
        }else{
            BonusImageUtil.toast(getContext(),"没有更多图片",Contacts.TOAST_SHORT_DURATION);
        }
    }

    private void addImageView(Bitmap bitmap , int width, int height,String url){
        Log.d(TAG,"addView = ");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
        if(imageView != null){
            imageView.setImageBitmap(bitmap);
        }else{
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(bitmap);
            imageView.setLayoutParams(params);
            imageView.setPadding(5,5,5,5);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setTag(R.string.image_url,url);
            imageViewList.add(imageView);
            Log.d(TAG,"addView = ");
            findColumnToAdd(imageView,height).addView(imageView);
        }
    }

    /**
     * 添加到对应的linearlayout
     * @param imageView
     * @param height
     * @return
     */
    private LinearLayout findColumnToAdd(ImageView imageView, int height) {
        if(firstLayoutHeight <= secondLayoutHeight){
            imageView.setTag(R.string.bolder_top,firstLayoutHeight);
            firstLayoutHeight += height;
            imageView.setTag(R.string.bolder_bottom,firstLayoutHeight);
            return firstLayout;
        }else{
            imageView.setTag(R.string.bolder_top,secondLayoutHeight);
            secondLayoutHeight += height;
            imageView.setTag(R.string.bolder_bottom,secondLayoutHeight);
            return secondLayout;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            Message msg = new Message();
            msg.obj = this;
            handler.sendMessageDelayed(msg,5);
        }
        return false;
    }
}
