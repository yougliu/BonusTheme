package themerom.bonus.com.themerom.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import themerom.bonus.com.themerom.R;


/**
 * Created by bonus on 11/26/15.
 * Class name ${type_name}
 */
public class WallpaperWaterfallFragment extends Fragment {
    private static final String TAG = WallpaperWaterfallFragment.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.waterfall_layout,container,false);
        return view;
    }
}
