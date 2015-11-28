package themerom.bonus.com.themerom.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import libcore.io.DiskLruCache;

/**
 * Created by bonus on 11/28/15.
 * Class name ${type_name}
 */
public class BonusImageLoader {

    /**
     * for LruCache and DiskLruCache
     */

    private Set<BitmapWorkTask> mWorkTasks;
    private Context mContext;
    private String mImageUrl;
    private LruCache<String,Bitmap> mCache;
    private DiskLruCache mDiskLruCache;
    private int mPathStyle = 0;
    private static final int HTTP_TYPE_PATH = 1;
    private static final int FILE_TYPE_PATH = 2;
    private static final int CONTENT_TYPE_PATH = 3;
    private static final int ASSETS_TYPE_PATH = 4;
    private static final int DRAWABLE_TYPE_PATH = 5;
    private View targetView;

    public BonusImageLoader(View view, String url) {
        this.targetView = view;
        this.mContext = targetView.getContext();
        this.mImageUrl = url;
        getPathStyle(mContext,url);
        initLruCache(mContext);
    }

    private int getPathStyle(Context context,String path){
        String[] splitString = path.split(":");
        String head = splitString[0];
        if(head.equals("http")){
            mPathStyle = HTTP_TYPE_PATH;
        }else if(head.equals("file")){
            mPathStyle = FILE_TYPE_PATH;
        }else if(head.equals("content")){
            mPathStyle = CONTENT_TYPE_PATH;
        }else if(head.equals("assets")){
            mPathStyle = ASSETS_TYPE_PATH;
        }else if(head.equals("drawable")){
            mPathStyle = DRAWABLE_TYPE_PATH;
        }
        return mPathStyle;
    }

    /**
     * for cache set
     */
    private void initLruCache(Context context){
        int memoryMax = (int) Runtime.getRuntime().maxMemory();
        int cache = memoryMax/8;
        mCache = new LruCache<String,Bitmap>(cache){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        File cacheFile = ThemeUtil.getDiskCacheDir(context,"cache");
        if(!cacheFile.exists()){
            cacheFile.mkdirs();
        }
        try {
            mDiskLruCache = DiskLruCache.open(cacheFile,ThemeUtil.getApplicationVersion(context),1,10*1024*1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * add cache
     * @param key
     * @param bitmap
     */
    public void addBitmapToCache(String key,Bitmap bitmap){
        if(getBitmapFromCache(key) == null){
            mCache.put(key,bitmap);
        }
    }

    /**
     * get cache
     * @param key
     * @return
     */
    public Bitmap getBitmapFromCache(String key){
        return mCache.get(key);
    }

    /**
     * flush disk cache
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

    public void cancelAllTask(){
        if(mWorkTasks != null){
            for(BitmapWorkTask task:mWorkTasks){
                task.cancel(false);
            }
        }
    }

    private

    class BitmapWorkTask extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

        }
    }
}
