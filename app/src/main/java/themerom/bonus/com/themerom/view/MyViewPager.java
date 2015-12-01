package themerom.bonus.com.themerom.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *
 * for drag
 * Created by bonus on 12/1/15.
 * Class name ${type_name}
 */
public class MyViewPager extends ViewPager{

    private boolean noScrool;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public void setNoScrool(boolean noScrool){
        this.noScrool = noScrool;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(noScrool){
            return false;
        }else{
            return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(noScrool){
            return false;
        }else{
            return super.onInterceptTouchEvent(ev);
        }
    }
}
