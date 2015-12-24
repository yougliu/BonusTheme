package themerom.bonus.com.themerom.callback;

/**
 * Created by helios on 12/24/15.
 */
public interface ProgressListener {

    void setMax(long max);
    void setProgress(long progress);
    void onSuccess(String result);
    void onFailed(String result,Exception e);
}
