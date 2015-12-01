package themerom.bonus.com.themerom.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    private static final String TAG = BonusImageLoader.class.getSimpleName();
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
        InputStream inputStream = null;
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
        return inputStream;
    }


    private InputStream getStreamFromFile(String urlPath){
        if(urlPath != null){

        }
        return null;
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
     * for net bitmap image display
     * 1:check memory
     * 2:check disk memory
     * 3:net work
     * 4:add lrumemory and disklrumemory
     * @param imageView
     * @param imagePath
     * @param options
     * @param listener
     * @param progressListener
     */
    public void displayImage(ImageView imageView, String imagePath, BonusDisplayImageOptions options, BonusDisplayImageListener listener,BonusDownloadImageProgressListener progressListener){
        Log.d(TAG, "display");
        if(imageView == null || imagePath == null){
            return ;
        }

        if(options == null){
            options = BonusDisplayImageOptions.newDefaultImageOptions();
        }
        initLruCache(mContext,options);
        getPathStyle(mContext,imagePath);

        Log.d(TAG,"---imagePath = "+imagePath);
        if(mPathScheme == HTTP_SCHEME_PATH){
            if(TextUtils.isEmpty(imagePath)){
                if(listener != null){
                    listener.onLoadingStarted(imagePath, imageView);
                }
                if(options.shouldShowImageForEmptyUrl()){
                    imageView.setImageDrawable(options.getImageForEmptyUrl(imageView.getResources()));
                }else{
                    imageView.setImageDrawable(null);
                }
            }else{
                //for url
                if(listener != null){
                    listener.onLoadingStarted(imagePath, imageView);
                }
                Bitmap bitmap = getBitmapFromCache(imagePath);
                if(bitmap != null){
                    imageView.setImageBitmap(bitmap);
                    if(listener != null){
                        listener.onLoadingCompleted(imagePath,imageView,bitmap);
                    }
                }else{
                    //task from net work
                    if(options.shouldShowImageOnLoading()){
                        imageView.setImageDrawable(options.getImageOnLoading(imageView.getResources()));
                    }
                    BitmapWorkTask task = new BitmapWorkTask();
                    task.execute(imagePath);
                    try {
                        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(BonusImageUtil.hashkeyForDisk(imagePath));
                        Log.d(TAG,"----snapshot = "+(snapshot == null));
                        if(snapshot == null){
                            if(listener != null){
                                listener.onLoadingFailed(imagePath,imageView);
                            }
                            if(options.shouldShowImageOnFail()){
                                imageView.setImageDrawable(options.getImageOnFail(imageView.getResources()));
                            }
                        }else{
                            FileInputStream fInputStream = (FileInputStream) snapshot.getInputStream(0);
                            FileDescriptor fileDescriptor = fInputStream.getFD();
                            if(fileDescriptor != null){
                                Bitmap finalBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                                imageView.setImageBitmap(finalBitmap);
                                if(listener != null){
                                    listener.onLoadingCompleted(imagePath,imageView,finalBitmap);
                                }
                            }else{
                                if(listener != null){
                                    listener.onLoadingFailed(imagePath,imageView);
                                }
                                if(options.shouldShowImageOnFail()){
                                    imageView.setImageDrawable(options.getImageOnFail(imageView.getResources()));
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
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

        this.displayImage(imageView,imagePath,options,listener,null);
    }

    public void displayImage(ImageView imageView, String imagePath, BonusDisplayImageOptions options, BonusDownloadImageProgressListener listener){
        if(imageView == null || imagePath == null){
            return ;
        }
        this.displayImage(imageView,imagePath,options,null,listener);
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
        this.displayImage(imageView,imagePath,options,null,null);
    }

    public void displayImage(ImageView imageView, String imagePath){
        if(imageView == null || imagePath == null){
            return ;
        }
        this.displayImage(imageView,imagePath,null,null,null);
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

    /**
     * net work task
     */
    class BitmapWorkTask extends AsyncTask<String,Void,Void>{

        private String imageUrl = null;

        @Override
        protected Void doInBackground(String... params) {
            imageUrl = params[0];
            if(imageUrl == null){
                return null;
            }
            FileInputStream fileInputStream = null;
            FileDescriptor fileDescriptor = null;
            DiskLruCache.Snapshot snapshot = null;
            try {
                final String key = BonusImageUtil.hashkeyForDisk(imageUrl);
                snapshot = mDiskLruCache.get(key);
                Log.d(TAG,"SNAPSHOT = "+(snapshot == null));
                if(snapshot == null){
                    //准备写入缓存
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if(editor != null){
                        OutputStream outputStream = editor.newOutputStream(0);
                        //net work get outputstream
                        if(downloadFromNetwork(imageUrl,outputStream)){
                            Log.d(TAG,"commit = "+imageUrl);
                            editor.commit();
                        }else{
                            Log.d(TAG,"abort = "+imageUrl);
                            editor.abort();
                        }
                    }
                    snapshot = mDiskLruCache.get(key);
                }
                if(snapshot != null){
                    fileInputStream = (FileInputStream) snapshot.getInputStream(0);
                    fileDescriptor = fileInputStream.getFD();
                }
                Bitmap bitmap = null;
                if(fileDescriptor != null){
                    bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    Log.d(TAG,"bitmap = "+(bitmap == null));
                    if(bitmap != null){
                        addBitmapToCache(imageUrl, bitmap);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                if(fileDescriptor == null && fileInputStream != null){
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }


        private boolean downloadFromNetwork(String urlString, OutputStream outputStream){
            HttpURLConnection connection = null;
            BufferedInputStream bInputStream = null;
            BufferedOutputStream bOutputStream = null;
            try {
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10*1000);
                connection.setConnectTimeout(5000);
                bInputStream = new BufferedInputStream(connection.getInputStream(),10*1024);
                bOutputStream = new BufferedOutputStream(outputStream,10*1024);
                int b;
                while((b=bInputStream.read()) != -1){
                    bOutputStream.write(b);
                }
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                if(connection != null){
                    connection.disconnect();
                }
                if(bInputStream != null){
                    try {
                        bInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(bOutputStream != null){
                    try {
                        bOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        }
    }
}
