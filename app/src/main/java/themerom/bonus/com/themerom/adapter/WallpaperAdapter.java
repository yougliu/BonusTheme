package themerom.bonus.com.themerom.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import themerom.bonus.com.themerom.R;
import themerom.bonus.com.themerom.entity.WallpaperEntity;

/**
 * Created by bonus on 11/14/15.
 * Class name ${type_name}
 */
public class WallpaperAdapter extends BaseAdapter {

    private Context mContext;
    private List<WallpaperEntity> mWallpaperList;
    private LayoutInflater mInflater;
    private DisplayImageOptions mOptions;
    public WallpaperAdapter(Context context,List<WallpaperEntity> wallpaperList,DisplayImageOptions options){
        this.mOptions = options;
        this.mContext = context;
        this.mWallpaperList = wallpaperList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mWallpaperList.size();
    }

    @Override
    public Object getItem(int position) {
        return mWallpaperList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.wallpaper_item_layout,parent,false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.id_wallpaper_icon);
            holder.textView = (TextView) convertView.findViewById(R.id.id_wallpaper_text);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.id_wallpaper_progressbar);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(mWallpaperList.get(position).getImageList() != null){
            String url = mWallpaperList.get(position).getImageList().get(0).getPath();
            ImageLoader.getInstance().displayImage(url, holder.imageView, mOptions, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    holder.progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    holder.progressBar.setVisibility(View.GONE);
                }
            });
        }
        return convertView;
    }

    class ViewHolder{
        ImageView imageView;
        TextView textView;
        ProgressBar progressBar;
    }

}
