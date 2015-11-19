package themerom.bonus.com.themerom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import themerom.bonus.com.themerom.R;

/**
 * Created by bonus on 11/19/15.
 * Class name ${type_name}
 */
public class BonusPopupWindowView extends View {
    private static final String TAG = BonusPopupWindowView.class.getSimpleName();
    private List<Map<String,Integer>> mIResource = new ArrayList<>();
    private List<Map<String,String>> mSResource = new ArrayList<>();//for network
    private int mBackground;
    private float mTextSize;
    private int mTextGravity;
    private int mImageGravity;
    private static final int CENTER = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;

    public BonusPopupWindowView(Context context) {
        this(context, null);
    }

    public BonusPopupWindowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BonusPopupWindowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getResources().obtainAttributes(attrs, R.styleable.bonusPopupWindowView);
        mBackground = array.getColor(R.styleable.bonusPopupWindowView_background, Color.TRANSPARENT);
        mTextSize = array.getDimension(R.styleable.bonusPopupWindowView_textSize,18);
        mTextGravity = array.getInteger(R.styleable.bonusPopupWindowView_textGravity,LEFT);
        mImageGravity = array.getInteger(R.styleable.bonusPopupWindowView_imageGravity,LEFT);
        array.recycle();
    }

    public List<Map<String, Integer>> getmIResource() {
        return mIResource;
    }

    public void setmIResource(List<Map<String, Integer>> mIResource) {
        this.mIResource = mIResource;
    }

    public List<Map<String, String>> getmSResource() {
        return mSResource;
    }

    public void setmSResource(List<Map<String, String>> mSResource) {
        this.mSResource = mSResource;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mIResource != null&&mIResource.size() > 0){

        }
    }
}
