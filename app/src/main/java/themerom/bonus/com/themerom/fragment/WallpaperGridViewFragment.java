package themerom.bonus.com.themerom.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;

import themerom.bonus.com.themerom.R;
import themerom.bonus.com.themerom.adapter.WallpaperGridAdapter;
import themerom.bonus.com.themerom.contants.Contacts;

/**
 * Created by bonus on 11/26/15.
 * Class name ${type_name}
 */
public class WallpaperGridViewFragment extends Fragment{

    private static final String TAG = WallpaperGridViewFragment.class.getSimpleName();
    private WallpaperGridAdapter mAdapter;
    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private GridView mGridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallpaper_gridview_layout,null,false);
        mGridView = (GridView) view.findViewById(R.id.id_wallpaper_gridview);
        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.gridview_column_width);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.gridview_spacing);
        mAdapter = new WallpaperGridAdapter(mGridView,getContext(), Contacts.imageThumbUrls);
        mGridView.setAdapter(mAdapter);
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int numColumns = (int) Math.floor(mGridView.getWidth()/(mImageThumbSpacing+mImageThumbSize));
                if(numColumns > 0){
                    int columnWidth = mGridView.getWidth()/numColumns-mImageThumbSpacing;
                    mAdapter.setItemHeight(columnWidth);
                    mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.flushCache();
    }
}
