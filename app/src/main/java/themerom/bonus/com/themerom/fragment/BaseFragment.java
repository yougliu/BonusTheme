package themerom.bonus.com.themerom.fragment;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by bonus on 11/18/15.
 * base fragment for all fragment
 */
public class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
}
