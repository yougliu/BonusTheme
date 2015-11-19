package themerom.bonus.com.themerom.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import themerom.bonus.com.themerom.R;
import themerom.bonus.com.themerom.contants.Contacts;
import themerom.bonus.com.themerom.utils.ThemeUtil;
import themerom.bonus.com.themerom.view.BackImage;
import themerom.bonus.com.themerom.view.GalleryImageView;

/**
 * Created by bonus on 11/17/15.
 * Class name ${type_name}
 */
public class WallpaperActivity extends Activity implements BackImage.OnBackClickListener{
    private static final String TAG = WallpaperActivity.class.getSimpleName();
    private BackImage mBack;
    private TextView mTitle;
    private ImageView mSet;
    private ImageSwitcher switcher;
    private Gallery mGallery;
    private int[] thumbnailIds;
    private int[] originalIds;
    public static final String WALLPAPER_ORIGINAL_PATH = "/media/wallpapers/original";
    public static final String WALLPAPER_THUMBNAIL_PATH = "/media/wallpapers/thumbnail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpaper_detail_layout);
        initActionBar();
        switcher = (ImageSwitcher) findViewById(R.id.id_image_switcher);
        mGallery = (Gallery) findViewById(R.id.id_wallpaper_gallery);
        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(WallpaperActivity.this);
                imageView.setBackgroundColor(0xffffff);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return imageView;
            }
        });
        switcher.setInAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_in));
        switcher.setOutAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_out));
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 11/17/15
            }
        });

        //load gallery resource
        loadWallpaperResource(this);
        mGallery.setAdapter(new GalleryAdapter());
        mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switcher.setImageResource(thumbnailIds[position]);
                ThemeUtil.toast(WallpaperActivity.this,"switch onclick "+ position, Contacts.TOAST_SHORT_DURATION);
            }
        });

        mGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switcher.setImageResource(thumbnailIds[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initActionBar() {
        mBack = (BackImage) findViewById(R.id.id_back);
        mTitle = (TextView) findViewById(R.id.id_actionbar_title);
        mSet = (ImageView) findViewById(R.id.id_set);
        mBack.setOnBackClickListener(this);
    }

    private void loadWallpaperResource(Context context) {
        TypedArray thumbArray = context.getResources().obtainTypedArray(R.array.thumbnail_wallpaper);
        TypedArray originArray = context.getResources().obtainTypedArray(R.array.original_wallpaper);
        thumbnailIds = new int[thumbArray.length()];
        originalIds = new int[originArray.length()];
        for (int i = 0;i<originArray.length();i++){
            thumbnailIds[i] = thumbArray.getResourceId(i,0);
            originalIds[i] = originArray.getResourceId(i,0);
        }
        thumbArray.recycle();
        originArray.recycle();
    }

    @Override
    public void onBackClick() {
        finish();
    }

    class GalleryAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return thumbnailIds.length;
        }

        @Override
        public Object getItem(int position) {
            return thumbnailIds[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(WallpaperActivity.this).inflate(R.layout.wallpaper_gallery_item_layout,parent,false);
            }
            GalleryImageView imageView = (GalleryImageView) convertView.findViewById(R.id.id_gallery_image);
            imageView.setImageResource(thumbnailIds[position]);
            return convertView;
        }
    }


    class myas extends AsyncTask<Integer,String,Boolean>{

        @Override
        protected Boolean doInBackground(Integer... params) {
            return null;
        }
    }
}
