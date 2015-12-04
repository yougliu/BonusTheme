package themerom.bonus.com.themerom.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import themerom.bonus.com.themerom.R;
import themerom.bonus.com.themerom.activity.WallPaperImageActivity;
import themerom.bonus.com.themerom.contants.Contacts;

/**
 * Created by bonus on 11/26/15.
 * Class name ${type_name}
 */
public class WallpaperGalleryFragment extends Fragment {

    private static final String TAG = WallpaperGalleryFragment.class.getSimpleName();
    private ImageSwitcher switcher;
    private Gallery mGallery;
    private Context mContext;
    private int[] thumbnailIds;
    private int[] originalIds;
    private int mPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallpaper_gallery_switch_layout,null);
        mContext = getActivity();
        switcher = (ImageSwitcher)view.findViewById(R.id.id_image_switcher);
        mGallery = (Gallery) view.findViewById(R.id.id_wallpaper_gallery);
        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(mContext);
                imageView.setBackgroundColor(0xffffff);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return imageView;
            }
        });
        switcher.setInAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
        switcher.setOutAnimation(AnimationUtils.loadAnimation(mContext,android.R.anim.fade_out));
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),WallPaperImageActivity.class);
                intent.putExtra("position",mPosition);
                intent.putExtra(Contacts.RESOURCE_TYPE,Contacts.TYPE_LOCAL);
                intent.putExtra("originalIds",originalIds);
                startActivity(intent);
            }
        });

        //load gallery resource
        loadWallpaperResource(mContext);
        mGallery.setAdapter(new GalleryAdapter());
        mGallery.setSelection(4);
        mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switcher.setImageResource(thumbnailIds[position]);
                mPosition = position;
            }
        });

        mGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switcher.setImageResource(thumbnailIds[position]);
                mPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
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


    class GalleryAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.wallpaper_gallery_item_layout,parent,false);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.id_gallery_image);
            imageView.setImageResource(thumbnailIds[position]);
            return convertView;
        }
    }
}
