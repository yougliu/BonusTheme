package themerom.bonus.com.themerom.activity;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import themerom.bonus.com.themerom.fragment.ImageDisplayFragment;

/**
 * Created by bonus on 12/1/15.
 * Class name ${type_name}
 */
public class WallPaperImageActivity extends FragmentActivity{

    private ImageDisplayFragment imageDisplayFragment;
    private String tag = ImageDisplayFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int position = getIntent().getIntExtra("position",1);
        int[] originalIds = getIntent().getIntArrayExtra("originalIds");
        if(savedInstanceState == null){
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            imageDisplayFragment = (ImageDisplayFragment) manager.findFragmentByTag(tag);
            if(imageDisplayFragment == null){
                imageDisplayFragment = new ImageDisplayFragment();
                imageDisplayFragment.setArguments(getIntent().getExtras());
            }
            transaction.add(android.R.id.content,imageDisplayFragment,tag).commit();
            findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
    }
}
