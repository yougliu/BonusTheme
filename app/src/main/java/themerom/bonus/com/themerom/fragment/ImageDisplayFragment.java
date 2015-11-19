package themerom.bonus.com.themerom.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import themerom.bonus.com.themerom.R;

/**
 * Created by bonus on 11/18/15.
 * Class name ${type_name}
 */
public class ImageDisplayFragment extends BaseFragment {

    private static final String TAG = ImageDisplayFragment.class.getSimpleName();
    private DisplayImageOptions mOptions;
    private LayoutInflater mInflater;
    private LinearLayout mLayoutLeft;
    private LinearLayout mLayoutRight;
    private LinearLayout mLayoutDrag;
    private Button mSave,mSetWallpaper,mSetLockpaper,mZambia,mClose;
    private ViewPager mViewPager;
    private ImageView mDrag;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mInflater = inflater;
        View view = mInflater.inflate(R.layout.image_display_layout, null);
        mViewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
        mLayoutLeft = (LinearLayout) view.findViewById(R.id.id_choice_left);
        mLayoutRight = (LinearLayout) view.findViewById(R.id.id_choice_right);
        mLayoutDrag = (LinearLayout) view.findViewById(R.id.id_drag);

        return view;
    }
}
