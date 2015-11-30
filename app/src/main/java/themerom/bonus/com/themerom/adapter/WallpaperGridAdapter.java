package themerom.bonus.com.themerom.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import libcore.io.DiskLruCache;
import themerom.bonus.com.themerom.R;
import themerom.bonus.com.themerom.utils.BonusImageUtil;

/**
 * Created by bonus on 11/26/15.
 * Class name ${type_name}
 */
public class WallpaperGridAdapter extends BaseAdapter{
    private Set<BitmapWorkTask> taskCollection;
    private LruCache<String,Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCache;
    private GridView mGridView;
    private int mItemHeight = 0;
    private String[] mImageThumbs;
    private Context mContext;

    public WallpaperGridAdapter(GridView mGridView,Context context,String[] imageThumbs) {
        this.mGridView = mGridView;
        this.mImageThumbs = imageThumbs;
        this.mContext = context;
        taskCollection = new HashSet<>();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cache = maxMemory/8;
        mMemoryCache = new LruCache<String, Bitmap>(cache){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };

        try {
            File cacheFile = BonusImageUtil.getDiskCacheDir(context, "cache");
            if(!cacheFile.exists()){
                cacheFile.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheFile, BonusImageUtil.getApplicationVersion(context),1,10*1024*1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return mImageThumbs.length;
    }

    @Override
    public Object getItem(int position) {
        return mImageThumbs[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_image_item_layout,null,false);
        }
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.id_gridview_image);
        if(imageView.getLayoutParams().height != mItemHeight){
            imageView.getLayoutParams().height = mItemHeight;
        }
        // 给ImageView设置一个Tag，保证异步加载图片时不会乱序
        imageView.setTag(mImageThumbs[position]);
        imageView.setImageResource(R.drawable.empty_photo);
        //加载网络图片图片
        loadBitmapImage(imageView,mImageThumbs[position]);
        return convertView;
    }

    /**
     * 将bitmap加入到lrucache中,lrucache中不采用加密key
     * @param key
     * @param bitmap
     */
    public void addBitmapToMemoryCache(String key,Bitmap bitmap){
        if(getBitmapFromMemoryCache(key) == null){
            mMemoryCache.put(key,bitmap);
        }
    }

    /**
     * 从lrucache中获取想对应的bitmap
     * @param key
     */
    public Bitmap getBitmapFromMemoryCache(String key){
        return mMemoryCache.get(key);
    }

    /**
     * 先从lrucache中获取bitmap，如果没有，在通过task获取
     * @param imageView
     * @param mImageThumb
     */
    public void loadBitmapImage(ImageView imageView, String mImageThumb) {
        Bitmap bitmap = getBitmapFromMemoryCache(mImageThumb);
        if(bitmap == null){
            //load from network
            BitmapWorkTask task = new BitmapWorkTask();
            taskCollection.add(task);
            task.execute(mImageThumb);
        }else{
            if(imageView != null&&bitmap != null){
                imageView.setImageBitmap(bitmap);
            }
        }
    }


    /**
     * 先检查disk cache，在进入网络请求，在写入lrucache，diskLrucache
     */
    class BitmapWorkTask extends AsyncTask<String ,Void ,Bitmap>{

        private String imageUrl;
        @Override
        protected Bitmap doInBackground(String... params) {
            imageUrl = params[0];
            FileDescriptor fileDescriptor = null;
            FileInputStream inputStream = null;
            DiskLruCache.Snapshot snapshot = null;
            try {
                final String key = BonusImageUtil.hashkeyForDisk(imageUrl);
                snapshot = mDiskLruCache.get(key);
                if(snapshot == null){
                    //没有对应的缓存，load from network ，准备写入缓存
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if(editor != null){
                        OutputStream outputStream = editor.newOutputStream(0);
                        if(downloadFromNetwork(imageUrl,outputStream)){
                            editor.commit();
                        }else{
                            editor.abort();
                        }
                        // 缓存被写入后，再次查找key对应的缓存
                    }
                    snapshot = mDiskLruCache.get(key);
                }
                if(snapshot != null){
                    inputStream = (FileInputStream) snapshot.getInputStream(0);
                    fileDescriptor = inputStream.getFD();
                }
                // 将缓存数据解析成Bitmap对象,并添加到lrucache
                Bitmap bitmap = null;
                if(fileDescriptor != null){
                    bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                }
                if(bitmap != null){
                    addBitmapToMemoryCache(imageUrl,bitmap);
                }
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                if(fileDescriptor == null && inputStream != null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            // 根据Tag找到相应的ImageView控件，将下载好的图片显示出来。
            ImageView imageView = (ImageView) mGridView.findViewWithTag(imageUrl);
            if(imageView != null&&bitmap != null){
                imageView.setImageBitmap(bitmap);
            }
            taskCollection.remove(this);
        }

        private boolean downloadFromNetwork(String urlString,OutputStream out){
            HttpURLConnection conn = null;
            BufferedInputStream inputStream = null;
            BufferedOutputStream outputStream = null;
            try {
                URL url = new URL(urlString);
                try {
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(15000);
                    inputStream = new BufferedInputStream(conn.getInputStream(),8*1024);
                    outputStream = new BufferedOutputStream(out,8*1024);
                    int b;
                    while((b=inputStream.read()) != -1){
                        outputStream.write(b);
                    }
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }finally{
                if(conn != null){
                    conn.disconnect();
                }
                if(inputStream != null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(outputStream != null){
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        }

    }

    public void setItemHeight(int height){
        if(height == mItemHeight){
            return ;
        }
        mItemHeight = height;
        notifyDataSetChanged();
    }

    /**
     * 将缓存记录同步到journal文件中
     */
    public void flushCache(){
        if(mDiskLruCache != null){
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取消所有正在下载或等待下载的任务
     */
    public void cancleAllTask(){
        if(taskCollection != null){
            for (BitmapWorkTask task:taskCollection){
                task.cancel(false);
            }
        }
    }
}
