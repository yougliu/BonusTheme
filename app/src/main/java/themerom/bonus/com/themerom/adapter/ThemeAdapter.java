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
import themerom.bonus.com.themerom.entity.ThemeEntity;

/**
 * Created by helios on 11/10/15.
 */
public class ThemeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<ThemeEntity> mThemeEntitys;
    private DisplayImageOptions mOptions;

    public ThemeAdapter(Context context,List<ThemeEntity> themeEntities, DisplayImageOptions options) {
        mInflater = LayoutInflater.from(context);
        this.mThemeEntitys = themeEntities;
        this.mContext = context;
        this.mOptions = options;
    }

    @Override
    public int getCount() {
        return mThemeEntitys.size();
    }

    @Override
    public Object getItem(int position) {
        return mThemeEntitys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, android.view.View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.theme_item_layout,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.iconImageView = (ImageView) convertView.findViewById(R.id.id_theme_icon);
            viewHolder.selectImageView = (ImageView) convertView.findViewById(R.id.id_theme_select);
            viewHolder.themeTitle = (TextView) convertView.findViewById(R.id.id_theme_title);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.id_theme_progressbar);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(mThemeEntitys.get(position).getPreviewList().get(0).getPath()
                , viewHolder.iconImageView, mOptions, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                viewHolder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                viewHolder.progressBar.setVisibility(View.GONE);
                viewHolder.themeTitle.setText(mThemeEntitys.get(position).getThemename());
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                viewHolder.progressBar.setVisibility(View.GONE);
            }
        });
        return convertView;
    }


    private static class ViewHolder{
        ImageView iconImageView;
        ImageView selectImageView;
        TextView themeTitle;
        ProgressBar progressBar;
    }
}
