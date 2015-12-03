package themerom.bonus.com.themerom.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import libcore.io.DiskLruCache;

/**
 * Created by bonus on 12/3/15.
 * Class name ${type_name}
 */
public class FallImageLoader {

    /**
     * just for water fall layout
     */
    private static FallImageLoader mInstance;
    private LruCache<String, Bitmap> mLruCache;
    private DiskLruCache mDiskLruCache;
    private Context mContext;

    private FallImageLoader(Context context){
        this.mContext = context;
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        mLruCache = new LruCache<String, Bitmap>(maxMemory/8){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };

        try {
            mDiskLruCache = DiskLruCache.open(BonusImageUtil.getDiskCacheDir(context,"waterfall"),
                    BonusImageUtil.getApplicationVersion(context),1,10*1024*1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FallImageLoader getInstance(Context context){
        if(mInstance == null){
            synchronized (FallImageLoader.class){
                if(mInstance == null){
                    mInstance = new FallImageLoader(context);
                }
            }
        }
        return mInstance;
    }

    public void addBitmapToLruCache(String key, Bitmap bitmap){
        if(getBitmpaFromLruCache(key) == null){
            mLruCache.put(key,bitmap);
        }
    }

    public Bitmap getBitmpaFromLruCache(String key){
        if(key == null){
            return null;
        }
        return mLruCache.get(key);
    }

    private int calculateSampleSize(BitmapFactory.Options options,int reqWidth){
        int outWidth = options.outWidth;
        int sampleSize = 1;
        if(reqWidth < outWidth){
            sampleSize = Math.round(outWidth/reqWidth);
        }
        return sampleSize;
    }

    private InputStream decodeInputStreamFromInputStream(InputStream inputStream, int reqWidth){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream,null,options);
        options.inSampleSize = calculateSampleSize(options,reqWidth);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream,null,options);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bout);
        ByteArrayInputStream decodeStream = new ByteArrayInputStream(bout.toByteArray());
        return decodeStream;
    }

    public Bitmap getImageBitmap(String url,final int reqWidth){
        if(url == null ){
            return null;
        }

        Bitmap bitmap = getBitmpaFromLruCache(url);
        if(bitmap != null){
            return bitmap;
        }
        FileInputStream fInputStream = null;
        FileDescriptor fileDescriptor = null;
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskLruCache.get(BonusImageUtil.hashkeyForDisk(url));
            if(snapshot != null){
                //have disk cache
                fInputStream = (FileInputStream) snapshot.getInputStream(0);
                fileDescriptor = fInputStream.getFD();
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                if(url != null && bitmap != null){
                    addBitmapToLruCache(url,bitmap);
                }
                return bitmap;
            }else{
                //must load from net work,add lrucache and disk cache
                BitmapWorkTask task = new BitmapWorkTask();
                task.execute(url);
                try{
                    snapshot = mDiskLruCache.get(BonusImageUtil.hashkeyForDisk(url));
                    if(snapshot != null) {
                        //have disk cache
                        fInputStream = (FileInputStream) snapshot.getInputStream(0);
                        fInputStream = (FileInputStream) decodeInputStreamFromInputStream(fInputStream,reqWidth);
                        fileDescriptor = fInputStream.getFD();
                        bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        return bitmap;
                    }else{
                        return null;
                    }
                }catch(Exception e){
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    class BitmapWorkTask extends AsyncTask<String,Void,Bitmap>{

        String url = null;

        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            FileInputStream fInputStream = null;
            FileDescriptor fileDescriptor = null;
            DiskLruCache.Snapshot snapshot = null;
            String key = BonusImageUtil.hashkeyForDisk(url);
            try {
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                OutputStream outputStream = editor.newOutputStream(0);
                //net work download
                if(loadResourceFromNetwork(url,outputStream)){
                    Log.d("TAG","commit = "+url);
                    editor.commit();
                }else{
                    Log.d("TAG","abort = "+url);
                    editor.abort();
                }
                snapshot = mDiskLruCache.get(key);
                if(snapshot == null){
                    return null;
                }else{
                    Log.d("TAG","snapshot = "+(snapshot == null));
                    fInputStream = (FileInputStream) snapshot.getInputStream(0);
                    fileDescriptor = fInputStream.getFD();
                    Log.d("TAG","fileDescriptor = "+(fileDescriptor == null));
                    Bitmap b = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    Log.d("TAG","b = "+(b == null));
                    return b;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d("TAG","bitmap = "+(bitmap == null));
            if(bitmap != null){
                addBitmapToLruCache(url,bitmap);
            }
        }

        private boolean loadResourceFromNetwork(String urlString,OutputStream outputStream){
            HttpURLConnection connection = null;
            BufferedInputStream bInputStream = null;
            BufferedOutputStream bOutputStream = null;
            try {
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(15000);
                bInputStream = new BufferedInputStream(connection.getInputStream(),10*1024);
                bOutputStream = new BufferedOutputStream(outputStream,10*1024);
                int b = 0;
                while((b = bInputStream.read()) != -1){
                    bOutputStream.write(b);
                }
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
