package themerom.bonus.com.themerom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import themerom.bonus.com.themerom.R;

/**
 * Created by bonus on 11/18/15.
 * Class name ${type_name}
 */
public class BonusProgressBar extends View{
    private static final String TAG = BonusProgressBar.class.getSimpleName();
    private float mpWidth;
    private int mColor;
    private int mpColor;
    private boolean isTextDisplay;
    ;
    private float mTextSize;
    private int mtColor;
    private Paint mPaint;
    private int max;
    private int progress;
    private int roundStyle;
    private static final int STROKE = 0;
    private static final int FILL = 1;
    private int style;
    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;
    private static final int ROUND = 2;

    //for gradient
    private boolean isRotation;
    private boolean isGradient;
    private int startColor;
    private int middleColor;
    private int endColor;
    private int degree;
    private LinearInterpolator mLinearInterpolator;
    private AlphaAnimation mAlphaAnimation;
    private RotateAnimation mRoatateAnimation;

    public BonusProgressBar(Context context) {
        this(context, null);
    }

    public BonusProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BonusProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.bonusProgressBar);

        mpWidth = array.getDimension(R.styleable.bonusProgressBar_progressBarWidth,5);
        mColor = array.getColor(R.styleable.bonusProgressBar_Color,getResources().getColor(android.R.color.darker_gray));
        mpColor = array.getColor(R.styleable.bonusProgressBar_progressColor,getResources().getColor(android.R.color.holo_green_light));
        isTextDisplay = array.getBoolean(R.styleable.bonusProgressBar_textIsDisplayable,false);
        mTextSize = array.getDimension(R.styleable.bonusProgressBar_textSize,20);
        mtColor = array.getColor(R.styleable.bonusProgressBar_textColor,getResources().getColor(android.R.color.black));
        max = array.getInt(R.styleable.bonusProgressBar_max, 100);
        roundStyle = array.getInt(R.styleable.bonusProgressBar_roundStyle,STROKE);
        style = array.getInt(R.styleable.bonusProgressBar_style,ROUND);
        isRotation = array.getBoolean(R.styleable.bonusProgressBar_isRotation,false);

        isGradient = array.getBoolean(R.styleable.bonusProgressBar_isGradient,false);
        startColor = array.getColor(R.styleable.bonusProgressBar_startColor,getResources().getColor(android.R.color.holo_green_light));
        middleColor = array.getColor(R.styleable.bonusProgressBar_middleColor, Color.YELLOW);
        endColor = array.getColor(R.styleable.bonusProgressBar_endColor,Color.RED);
        degree = array.getInteger(R.styleable.bonusProgressBar_degree,40);
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth()/2;
        int radius = (int) (center - mpWidth/2);
        RectF rectF = new RectF(center-radius,center-radius,center+radius,center+radius);
        if(style == HORIZONTAL || style == VERTICAL){
            // TODO: 11/18/15
            return ;
        }

        if(isRotation){
            //like normal circle progressbar

            if (true){
                int startAngle = 0,endAngle = 0;
                endAngle = startAngle+degree;
                if(startColor >= 360){
                    startAngle -= 360;
                }else if(endAngle >= 360){
                    endAngle -= 360;
                }
                if(isGradient){
                    int[] colors = new int[]{startColor,middleColor,endColor};
                    LinearGradient gradient = new LinearGradient(0,0,degree,degree,colors,new float[]{0.0f,0.5f,0.8f}, Shader.TileMode.MIRROR);
                    mPaint.setShader(gradient);
                    mPaint.setStyle(Paint.Style.STROKE);
                    mPaint.setAntiAlias(true);
                    mPaint.setStrokeWidth(mpWidth);
                    canvas.drawArc(rectF,startAngle,endAngle,false,mPaint);
                }else{
                    mPaint.setColor(mpColor);
                    mPaint.setStyle(Paint.Style.STROKE);
                    mPaint.setAntiAlias(true);
                    mPaint.setStrokeWidth(mpWidth);
                    canvas.drawArc(rectF,startAngle,endAngle,false,mPaint);
                }
            }
        }else{
            //画最外层的大圆环
            mPaint.setColor(mColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(mpWidth);
            canvas.drawCircle(center,center,radius,mPaint);

            //画百分比
            mPaint.setStrokeWidth(0);
            mPaint.setColor(mtColor);
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mPaint.setTextSize(mTextSize);
            int percent = (int)(((float)progress / (float)max) * 100); ;
            float textWidth = mPaint.measureText(percent+"%");
            if(isTextDisplay && roundStyle == STROKE){
                canvas.drawText(percent+"%",center-textWidth/2,center-mTextSize/2,mPaint);
            }

            //画进度条
            mPaint.setStrokeWidth(mpWidth);
            mPaint.setColor(mpColor);

            if(roundStyle == STROKE){
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(rectF,270,progress*360/max,false,mPaint);
            }else if(roundStyle == FILL){
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                if(progress != 0){
                    canvas.drawArc(rectF,270,progress*360/max,true,mPaint);
                }
            }
        }
    }

    @Override
    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);
            // let's be nice with the UI thread
            if (v == GONE || v == INVISIBLE) {
                stopAnimation();
            } else {
                startAnimation();
            }
        }
    }

    void startAnimation() {
        if(getVisibility() != VISIBLE){
            return;
        }
        if(mLinearInterpolator == null){
            mLinearInterpolator = new LinearInterpolator();
        }

        if(mRoatateAnimation == null){
            mRoatateAnimation = new RotateAnimation(0,360,getWidth()/2,getWidth()/2);
        }else{
            mRoatateAnimation.reset();
        }
        mRoatateAnimation.setDuration(3000);
        mRoatateAnimation.setRepeatMode(RotateAnimation.INFINITE);
        mRoatateAnimation.setRepeatCount(Animation.INFINITE);
        mRoatateAnimation.setInterpolator(mLinearInterpolator);
        mRoatateAnimation.setStartTime(Animation.START_ON_FIRST_FRAME);
        postInvalidate();
    }

    void stopAnimation() {
        if(mAlphaAnimation != null){
            mAlphaAnimation.cancel();
        }
    }

    public synchronized int getMax(){
        return max;
    }

    public synchronized void setMax(int max){
        if(max < 0){
            throw new IllegalArgumentException("max can not less than 0");
        }
        this.max = max;
    }

    public synchronized  int getProgress(){
        return progress;
    }

    public synchronized void setProgress(int progress){
        if(progress < 0){
            throw new IllegalArgumentException("progress can not less than 0");
        }else if(progress > max){
            this.progress = max;
        }else if(progress <= max){
            this.progress = progress;
            postInvalidate();
        }
    }

    public float getMpWidth() {
        return mpWidth;
    }

    public void setMpWidth(float mpWidth) {
        this.mpWidth = mpWidth;
    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    public int getMpColor() {
        return mpColor;
    }

    public void setMpColor(int mpColor) {
        this.mpColor = mpColor;
    }

    public boolean isTextDisplay() {
        return isTextDisplay;
    }

    public void setIsTextDisplay(boolean isTextDisplay) {
        this.isTextDisplay = isTextDisplay;
    }

    public float getmTextSize() {
        return mTextSize;
    }

    public void setmTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public int getMtColor() {
        return mtColor;
    }

    public void setMtColor(int mtColor) {
        this.mtColor = mtColor;
    }

    public int getRoundStyle() {
        return roundStyle;
    }

    public void setRoundStyle(int roundStyle) {
        this.roundStyle = roundStyle;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }
}
