package themerom.bonus.com.themerom.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;


/**
 * Created by bonus on 11/30/15.
 * Class name ${type_name}
 */
public final class BonusDisplayImageOptions {

    private final int threadPoolSize;
    private final int imageResOnLoading;
    private final int imageResOnFail ;
    private final int imageResForEmptyUrl ;
    private final Drawable imageOnLoading ;
    private final Drawable imageOnFail ;
    private final Drawable imageForEmptyUrl ;
    private final boolean cacheInMemory;
    private final boolean cacheInDisk;
    private final ImageView.ScaleType scaleType;
    private final boolean considerExifParams;
    private final long diskCacheSize;
    private final int diskCacheCount;
    private final String diskCacheNameGenerator;
    private final long imageSize;//for B
    private final String diskCacheFileName;//default for application
    private final long memoryCacheSize;

    private BonusDisplayImageOptions(BonusDisplayImageOptions.Builder builder){
        this.threadPoolSize = builder.threadPoolSize;
        this.imageResOnLoading = builder.imageResOnLoading;
        this.imageResOnFail = builder.imageResOnFail;
        this.imageResForEmptyUrl = builder.imageResForEmptyUrl;
        this.imageOnLoading = builder.imageOnLoading;
        this.imageOnFail = builder.imageOnFail;
        this.imageForEmptyUrl = builder.imageForEmptyUrl;
        this.cacheInMemory = builder.cacheInMemory;
        this.cacheInDisk = builder.cacheInDisk;
        this.scaleType = builder.scaleType;
        this.considerExifParams = builder.considerExifParams;
        this.diskCacheSize = builder.diskCacheSize;
        this.diskCacheCount = builder.diskCacheCount;
        this.diskCacheNameGenerator = builder.diskCacheNameGenerator;
        this.imageSize = builder.imageSize;
        this.diskCacheFileName = builder.diskCacheFileName;
        this.memoryCacheSize = builder.memoryCacheSize;
    }

    public boolean shouldShowImageOnLoading(){
        return this.imageResOnLoading != 0 || this.imageOnLoading != null;
    }

    public boolean shouldShowImageOnFail(){
        return this.imageResOnFail != 0 || this.imageOnFail != null;
    }

    public boolean shouldShowImageForEmptyUrl(){
        return this.imageResForEmptyUrl != 0 || this.imageForEmptyUrl != null;
    }

    public Drawable getImageOnLoading(Resources res){
        return this.imageResOnLoading != 0 ? res.getDrawable(this.imageResOnLoading) : this.imageOnLoading;
    }

    public Drawable getImageOnFail(Resources res){
        return this.imageResOnFail != 0 ? res.getDrawable(imageResOnFail) : this.imageOnFail;
    }

    public Drawable getImageForEmptyUrl(Resources res){
        return this.imageResForEmptyUrl != 0 ? res.getDrawable(imageResForEmptyUrl) : this.imageForEmptyUrl;
    }

    public boolean isCacheInMemory(){
        return this.cacheInMemory;
    }

    public boolean isCacheInDisk(){
        return this.cacheInDisk;
    }

    public boolean isConsiderExifParams(){
        return this.considerExifParams;
    }

    public ImageView.ScaleType getScaleType(){
        return this.scaleType;
    }


    public long getDiskCacheSize() {
        return this.diskCacheSize;
    }

    public int getDiskCacheCount() {
        return this.diskCacheCount;
    }

    public String getDiskCacheNameGenerator() {
        return this.diskCacheNameGenerator;
    }

    public long getImageSize() {
        return this.imageSize;
    }

    public String getDiskCacheFileName(){
        return this.diskCacheFileName;
    }

    public long getMemoryCacheSize(){
        return this.memoryCacheSize;
    }

    public static BonusDisplayImageOptions newDefaultImageOptions(){
        return new BonusDisplayImageOptions.Builder().build();
    }



    public static class Builder{
        private int threadPoolSize = 0;
        private int imageResOnLoading = 0;
        private int imageResOnFail = 0;
        private int imageResForEmptyUrl = 0;
        private Drawable imageOnLoading = null;
        private Drawable imageOnFail = null;
        private Drawable imageForEmptyUrl = null;
        private boolean cacheInMemory;
        private boolean cacheInDisk;
        private ImageView.ScaleType scaleType;
        private boolean considerExifParams;
        private long memoryCacheSize;
        private long diskCacheSize;
        private int diskCacheCount;
        private String diskCacheNameGenerator;
        private long imageSize;//for B
        private String diskCacheFileName;

        public Builder(){
            this.scaleType = ImageView.ScaleType.FIT_XY;
            this.considerExifParams = false;
            this.diskCacheCount = 100;
            this.diskCacheSize = 10*1024*1024;
            this.diskCacheNameGenerator = "MD5";
            this.memoryCacheSize = Runtime.getRuntime().maxMemory()/8;
            this.diskCacheFileName = "cache";
        }

        public BonusDisplayImageOptions.Builder threadPoolSize(int poolSize){
            this.threadPoolSize = poolSize;
            return this;
        }

        public BonusDisplayImageOptions.Builder showImageResOnLoading(int imageRes){
            this.imageResOnLoading = imageRes;
            return this;
        }

        public BonusDisplayImageOptions.Builder showImageResOnFail(int imageRes){
            this.imageResOnFail = imageRes;
            return this;
        }

        public BonusDisplayImageOptions.Builder showImageResForEmptyUrl(int imageRes){
            this.imageResForEmptyUrl = imageRes;
            return this;
        }

        public BonusDisplayImageOptions.Builder showImageOnLoading(Drawable imageDrawable){
            this.imageOnLoading = imageDrawable;
            return this;
        }

        public BonusDisplayImageOptions.Builder showImageOnFail(Drawable imageDrawable){
            this.imageOnFail = imageDrawable;
            return this;
        }

        public BonusDisplayImageOptions.Builder showImageForEmptyUrl(Drawable imageDrawable){
            this.imageForEmptyUrl = imageDrawable;
            return this;
        }

        public BonusDisplayImageOptions.Builder cacheInMemory(boolean flag){
            this.cacheInMemory = flag;
            return this;
        }

        public BonusDisplayImageOptions.Builder cacheInDisk(boolean flag){
            this.cacheInDisk = flag;
            return this;
        }

        public BonusDisplayImageOptions.Builder setScaleType(ImageView.ScaleType scaleType){
            this.scaleType = scaleType;
            return this;
        }

        public BonusDisplayImageOptions.Builder considerExifParams(boolean flag){
            this.considerExifParams = flag;
            return this;
        }

        public BonusDisplayImageOptions.Builder diskCacheSize(long size){
            this.diskCacheSize = size;
            return this;
        }

        public BonusDisplayImageOptions.Builder diskCacheCount(int count){
            this.diskCacheCount = count;
            return this;
        }

        public BonusDisplayImageOptions.Builder diskCacheNameGenerator(String format){
            this.diskCacheNameGenerator = format;
            return this;
        }

        public BonusDisplayImageOptions.Builder imageSize(long imageSize){
            this.imageSize = imageSize;
            return this;
        }

        public BonusDisplayImageOptions.Builder diskCacheFile(String diskCacheFileName){
            this.diskCacheFileName = diskCacheFileName;
            return this;
        }

        public BonusDisplayImageOptions.Builder memoryCacheSize(long memoryCacheSize){
            if(memoryCacheSize > Runtime.getRuntime().maxMemory()){
                return this;
            }
            this.memoryCacheSize = memoryCacheSize;
            return this;
        }

        public BonusDisplayImageOptions build(){
            return new BonusDisplayImageOptions(this);
        }
    }
}
