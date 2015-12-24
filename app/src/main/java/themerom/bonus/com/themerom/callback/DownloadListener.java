package themerom.bonus.com.themerom.callback;

/**
 * Created by helios on 12/24/15.
 */
public interface DownloadListener {

    void download(String url,String savePath,ProgressListener progressListener,int threadSize,String label,String fileName);
    void pause();
}
