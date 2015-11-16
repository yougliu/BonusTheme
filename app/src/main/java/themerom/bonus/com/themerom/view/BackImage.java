package themerom.bonus.com.themerom.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import themerom.bonus.com.themerom.R;

/**
 * Created by Bonus on 2015/11/15 0015.umeng key 5649bd67e0f55a2e1400099d
 */
public class BackImage extends View implements View.OnClickListener{
    private Paint mPaint;
    private int strokeWidth = 8;
    private int mWidth;
    private int mHeight;
    private double widthMargin;
    private double heightMargin;
    private OnBackClickListener mListener;

    public BackImage(Context context) {
        this(context, null);
    }

    public BackImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    public BackImage(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthModel = MeasureSpec.getMode(widthMeasureSpec);
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);
        Log.d("bonus","model = "+widthModel+", "+heightModel+", "+MeasureSpec.AT_MOST+", "+MeasureSpec.EXACTLY);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        widthMargin = mWidth*0.3;
        heightMargin = mHeight*0.3;
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.title_text_color));
        mPaint.setStrokeWidth(strokeWidth);
        canvas.drawLine((float)(mWidth-widthMargin),(float)heightMargin,(float)widthMargin,mHeight/2,mPaint);
        canvas.drawLine((float)widthMargin,mHeight/2,(float)(mWidth-widthMargin),(float)(mHeight-heightMargin),mPaint);
        canvas.save();
        canvas.rotate(180,mWidth/2,mHeight/2);
    }

    public void setOnBackClickListener(OnBackClickListener  listener){
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if(v != null){
            mListener.onBackClick();
        }else{
            new RuntimeException("back click view is null");
        }
    }

    public interface OnBackClickListener{
        void onBackClick();
    }
}
