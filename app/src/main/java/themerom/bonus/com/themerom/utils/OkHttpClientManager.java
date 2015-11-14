package themerom.bonus.com.themerom.utils;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
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

    public static OkHttpClientManager getInstance(){
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

    public void excute(final Request request, OkHttpCallback callback){
        if(callback == null){
            callback = OkHttpCallback.DEFAULT_CALLBACK;
        }
        final OkHttpCallback restCallback = callback;
        restCallback.onBefore(request);
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailCallback(request, e, restCallback);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.code() >= 400 && response.code() <= 599){
                    sendFailCallback(request,new IOException(response.body().string()),restCallback);
                }
                final String result = response.body().string();
//                if(restCallback.mType == String.class){
//                    sendSucessCallback(result,restCallback);
//                }else{
//                    Object o = mGson.fromJson(result, restCallback.mType);
//                    sendSucessCallback(o,restCallback);
//                }
                sendSucessCallback(result,restCallback);
            }
        });
    }

    /**
     * @param request
     * @param e
     * @param restCallback
     */
    private void sendFailCallback(final Request request, final IOException e, final OkHttpCallback restCallback) {
        if(restCallback == null){
            return ;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                restCallback.onError(request,e);
                restCallback.onAfter();
            }
        });
    }

    private void sendSucessCallback(final Object object,final OkHttpCallback callback){
        if(callback == null){
            return ;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(object);
                callback.onAfter();
            }
        });
    }


}
