package themerom.bonus.com.themerom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import themerom.bonus.com.themerom.R;

/**
 * Created by bonus on 11/28/15.
 * Class name ${type_name}
 */
public class StaggeredAdapter extends RecyclerView.Adapter<StaggeredAdapter.StaggeredViewHolder>{

    private LayoutInflater mInflater;
    private String[] mDates;
    private List<Integer> mHeights;
    private onStaggeredItemClickListener mListener;

    public interface onStaggeredItemClickListener{
        void onStaggeredItemClick(View view,int position);
        void onStaggeredItemLongClick(View view,int position);
    }

    public void setOnStaggeredItemClickListener(onStaggeredItemClickListener listener){
        this.mListener = listener;
    }

    public StaggeredAdapter(Context context,String[] mDates) {
        this.mDates = mDates;
        mInflater = LayoutInflater.from(context);
        mHeights = new ArrayList<>();
        for(int i = 0;i<mDates.length;i++){
            mHeights.add((int) (100+Math.random()*200));
        }
    }

    @Override
    public StaggeredViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        StaggeredViewHolder viewHolder = new StaggeredViewHolder(mInflater.inflate(R.layout.gridview_image_item_layout,null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StaggeredViewHolder staggeredViewHolder, int i) {
        ViewGroup.LayoutParams params = staggeredViewHolder.imageView.getLayoutParams();
        params.height = mHeights.get(i);
        staggeredViewHolder.imageView.setLayoutParams(params);
        //设置图片

    }

    @Override
    public int getItemCount() {
        return mDates.length;
    }

    class StaggeredViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public StaggeredViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.id_gridview_image);
        }
    }
}
