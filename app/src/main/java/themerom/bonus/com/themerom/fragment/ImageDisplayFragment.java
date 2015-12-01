package themerom.bonus.com.themerom.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.HashMap;
import java.util.Map;

import themerom.bonus.com.themerom.R;
import themerom.bonus.com.themerom.view.MyViewPager;

/**
 * Created by bonus on 11/18/15.
 * Class name ${type_name}
 */
public class ImageDisplayFragment extends Fragment {

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
    private boolean popStatus;
    private int mWidth;
    private int mHeight;
    private float curX;
    private float downX;
    private float offSetX;


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
                .imageScaleType(ImageScaleType.NONE)
                        // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .build();
        mWidth = getResources().getDisplayMetrics().widthPixels;
        mHeight = getResources().getDisplayMetrics().heightPixels;
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

        return view;
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
            Drawable drawable = getResources().getDrawable(originalIds[position]);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            Log.d(TAG,"width = "+bitmap.getWidth()+", "+mWidth);
            if(bitmap.getWidth() > mWidth){
                dragStatus = true;
            }else{
                dragStatus = false;
            }
            mLayoutDrag.setVisibility(dragStatus?View.VISIBLE:View.GONE);
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
