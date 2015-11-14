package themerom.bonus.com.themerom.contants;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import themerom.bonus.com.themerom.entity.Preview;
import themerom.bonus.com.themerom.entity.WallpaperEntity;
import themerom.bonus.com.themerom.utils.ThemeUtil;

/**
 * Created by helios on 11/11/15.
 */
public class Contacts {

    public static final int TOAST_LONG_DURATION = 1;
    public static final int TOAST_SHORT_DURATION = 0;

    private List<Preview> thumbPath = new ArrayList<>();
    private List<Preview> originPath = new ArrayList<>();
    private List<WallpaperEntity> wallpaperPath = new ArrayList<>();

    public static String DownloadNumber="/tools/picture_good_data.php?pic_type=topicpaper&isdownload=1";
    public static String WallpaperDownloadNumber="/online_wallpaper/tools/picture_good_data.php?pic_type=wallpaper&isdownload=1";
    public static String Zambia ="/online_wallpaper/tools/picture_good_data.php?pic_type=topicpaper";
    public static String WallpaperZambia ="/online_wallpaper/tools/picture_good_data.php?pic_type=wallpaper";
    public static String HomeWallPath = "/picture_json.php?type=wallpaper&page=0";
    public static String MPATH = "/picture_json.php?&type=topicpaper";
    public  static String Wallpath = "/picture_json.php?&type=wallpaper";
    public  static String ChoicePath ="/picture_json.php?type=wallpaper&desc=desc";

    public static final String DOWNLOAD_THEME_ENTITY = "download_theme_entity";



    public List<WallpaperEntity> getWallpaperPath() {
        return wallpaperPath;
    }

    public void setWallpaperPath(List<WallpaperEntity> wallpaperPath) {
        this.wallpaperPath = wallpaperPath;
    }

    public List<Preview> getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(List<Preview> thumbPath) {
        this.thumbPath = thumbPath;
    }

    public List<Preview> getOriginPath() {
        return originPath;
    }

    public void setOriginPath(List<Preview> originPath) {
        this.originPath = originPath;
    }

    public static void getPixel(Context context){
        int p = 0;
        if(ThemeUtil.isFHD(context)){
            p = 1;
        }
        String ptype="&ptype="+p;
        HomeWallPath=HomeWallPath+ptype;
        MPATH=MPATH+ptype;
        Wallpath=Wallpath+ptype;
        ChoicePath=ChoicePath+ptype;
    }

    public static void getUri(){
        String str="http://muldown.fuli365.net/online_wallpaper";
        HomeWallPath=str+HomeWallPath;
        MPATH=str+MPATH;
        Wallpath=str+Wallpath;
        ChoicePath=str+ChoicePath;
        DownloadNumber=str+DownloadNumber;
        WallpaperDownloadNumber=str+WallpaperDownloadNumber;
        Zambia=str+Zambia;
        WallpaperZambia=str+WallpaperZambia;
    }



}
