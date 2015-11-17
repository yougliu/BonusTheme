package themerom.bonus.com.themerom.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import themerom.bonus.com.themerom.R;

/**
 * Created by bonus on 11/17/15.
 * Class name ${type_name}
 */
public class GalleryImageView extends ImageView{

    private static final String TAG = GalleryImageView.class.getSimpleName();
    private Paint mPaint;
    private static final int STROKE_WIDTH = 2;
    private int mWidth;
    private int mHeight;

    public GalleryImageView(Context context) {
        this(context, null);
    }

    public GalleryImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GalleryImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mPaint = new Paint();
        mPaint.setStrokeWidth(STROKE_WIDTH);
        mPaint.setColor(getResources().getColor(R.color.title_bg_color));
        mPaint.setAntiAlias(true);
        canvas.drawLine(0,0,mWidth,0,mPaint);
        canvas.drawLine(mWidth,0,mHeight,mWidth,mPaint);
        canvas.drawLine(mHeight,mWidth,0,mHeight,mPaint);
        canvas.drawLine(0,mHeight,0,0,mPaint);
    }
}
