package themerom.bonus.com.themerom.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import libcore.io.DiskLruCache;
import themerom.bonus.com.themerom.callback.BonusDisplayImageListener;
import themerom.bonus.com.themerom.callback.BonusDownloadImageProgressListener;

/**
 * Created by bonus on 11/28/15.
 * Class name ${type_name}
 */
public class BonusImageLoader {

    /**
     * for LruCache and DiskLruCache
     */

    private Set<BitmapWorkTask> mWorkTasks;
    private static BonusImageLoader mInstance;
    private Context mContext;
    private String mImageUrl;
    private LruCache<String,Bitmap> mCache;
    private DiskLruCache mDiskLruCache;
    private int mPathScheme = UNKOWN_SCHEME_PATH;
    private static final int HTTP_SCHEME_PATH = 1;
    private static final int FILE_SCHEME_PATH = 2;
    private static final int CONTENT_SCHEME_PATH = 3;
    private static final int ASSETS_SCHEME_PATH = 4;
    private static final int DRAWABLE_SCHEME_PATH = 5;
    private static final int HTTPS_SCHEME_PATH = 6;
    private static final int UNKOWN_SCHEME_PATH = 0;


    private BonusImageLoader(Context context) {
        this.mContext = context;
    }

    /**
     * 单例模式
     * @return
     */
    public static BonusImageLoader getInstance(Context context){
        if(mInstance == null){
            synchronized (BonusImageLoader.class){
                if(mInstance == null){
                    mInstance = new BonusImageLoader(context);
                }
            }
        }
        return mInstance;
    }


    private int getPathStyle(Context context,String path){
        String[] splitString = path.split(":");
        String scheme = splitString[0];
        if(scheme.equals("http")){
            mPathScheme = HTTP_SCHEME_PATH;
        }else if(scheme.equals("file")){
            mPathScheme = FILE_SCHEME_PATH;
        }else if(scheme.equals("content")){
            mPathScheme = CONTENT_SCHEME_PATH;
        }else if(scheme.equals("assets")){
            mPathScheme = ASSETS_SCHEME_PATH;
        }else if(scheme.equals("drawable")){
            mPathScheme = DRAWABLE_SCHEME_PATH;
        }else if(scheme.equals("https")){
            mPathScheme = HTTPS_SCHEME_PATH;
        }
        return mPathScheme;
    }

    private InputStream getStream(ImageView imageView, String urlPath, Context context){
        switch (getPathStyle(context,urlPath)){
            case HTTP_SCHEME_PATH:
                break;
            case FILE_SCHEME_PATH:
                break;
            case CONTENT_SCHEME_PATH:
                break;
            case ASSETS_SCHEME_PATH:
                break;
            case DRAWABLE_SCHEME_PATH:
                break;
            case UNKOWN_SCHEME_PATH:
                break;
        }
    }

    private InputStream getStreamFromFile(String urlPath){
        if(urlPath != null){
            
        }
    }

    /**
     * for cache set
     */
    private void initLruCache(Context context,BonusDisplayImageOptions options){
        if(options.isCacheInMemory()){
            int memoryCacheSize = (int) options.getMemoryCacheSize();
            mCache = new LruCache<String,Bitmap>(memoryCacheSize){
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };
        }
        if(options.isCacheInDisk()){
            String diskCacheFileName = options.getDiskCacheFileName();
            long diskCacheSize = options.getDiskCacheSize();
            File cacheFile = BonusImageUtil.getDiskCacheDir(context, diskCacheFileName);
            if(!cacheFile.exists()){
                cacheFile.mkdirs();
            }
            try {
                mDiskLruCache = DiskLruCache.open(cacheFile, BonusImageUtil.getApplicationVersion(context),1,diskCacheSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param imageView
     * @param imagePath
     * @param options
     * @param listener
     * @param progressListener
     */
    public void displayImage(ImageView imageView, String imagePath, BonusDisplayImageOptions options, BonusDisplayImageListener listener,BonusDownloadImageProgressListener progressListener){
        if(imageView == null || imagePath == null){
            return ;
        }


    }

    /**
     * for net bitmap image display
     * 1:check memory
     * 2:check disk memory
     * 3:net work
     * 4:add lrumemory and disklrumemory
     * @param imageView
     * @param imagePath
     * @param options
     * @param listener
     */
    public void displayImage(ImageView imageView, String imagePath, BonusDisplayImageOptions options, BonusDisplayImageListener listener){
        if(imageView == null || imagePath == null){
            return ;
        }


    }

    public void displayImage(ImageView imageView, String imagePath, BonusDisplayImageOptions options, BonusDownloadImageProgressListener listener){
        if(imageView == null || imagePath == null){
            return ;
        }
    }

    public void displayImage(ImageView imageView, String imagePath, BonusDownloadImageProgressListener listener){
        if(imageView == null || imagePath == null){
            return ;
        }
    }

    public void displayImage(ImageView imageView, String imagePath, BonusDisplayImageOptions options){
        if(imageView == null || imagePath == null){
            return ;
        }
    }

    public void displayImage(ImageView imageView, String imagePath){
        if(imageView == null || imagePath == null){
            return ;
        }
    }

    /**
     * add cache
     * @param key
     * @param bitmap
     */
    public void addBitmapToCache(String key,Bitmap bitmap, BonusDisplayImageOptions options){
        if(options.isCacheInMemory()){
            if(getBitmapFromCache(key) == null){
                mCache.put(key,bitmap);
            }
        }
    }

    /**
     * get cache
     * @param key
     * @return
     */
    public Bitmap getBitmapFromCache(String key){
        if(key == null){
            throw new NullPointerException("key == null");
        }
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
