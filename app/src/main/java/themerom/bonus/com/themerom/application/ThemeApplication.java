package themerom.bonus.com.themerom.application;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.xutils.x;

/**
 * Created by helios on 11/7/15.
 */
public class ThemeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        
        initImageLoader(getApplicationContext());
    }

    private void initImageLoader(Context context) {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.memoryCache(new LruMemoryCache(maxMemory/8))
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheSize(50*1024*1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(5)
                .tasksProcessingOrder(QueueProcessingType.FIFO);
        ImageLoader.getInstance().init(config.build());
        x.Ext.init(this);
    }
}
