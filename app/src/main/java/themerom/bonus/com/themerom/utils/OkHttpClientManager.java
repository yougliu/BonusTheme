package themerom.bonus.com.themerom.utils;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.CookieManager;
import java.net.CookiePolicy;

import themerom.bonus.com.themerom.callback.OkHttpCallback;

/**
 * Created by helios on 11/13/15.
 */
public class OkHttpClientManager {

    private static final String TAG = OkHttpClientManager.class.getSimpleName();
    private static OkHttpClientManager mInstance;
    private Handler mHandler;
    private OkHttpClient mClient;
    private Gson mGson;

    public OkHttpClientManager() {
        mClient = new OkHttpClient();
        mClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mHandler = new Handler(Looper.getMainLooper());
        final int sdk = Build.VERSION.SDK_INT;
        if(sdk >= 23){
            GsonBuilder builder = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.FINAL,Modifier.TRANSIENT,Modifier.STATIC);
            mGson = builder.create();
        }else{
            mGson = new Gson();
        }
    }

    private static OkHttpClientManager getInstance(){
        if(mInstance == null){
            synchronized (OkHttpClientManager.class){
                if(mInstance == null){
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * @param url
     * @return
     */
    private Response getAsyn(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Call call = mClient.newCall(request);
        return call.execute();
    }

    /**
     * shortcut alt+shift+j
     * @param url
     * @return
     * @throws IOException
     */
    private String getAsString(String url) throws IOException {
        Response response = getAsyn(url);
        return response.body().string();
    }

    /**
     * @param url
     * @param callback
     * @return
     */
    private void getAsyn(String url,final OkHttpCallback callback){
        final Request request = new Request.Builder().url(url).build();

    }

    public Handler getmHandler(){
        return mHandler;
    }

    public OkHttpClient getmClient(){
        return mClient;
    }




}
