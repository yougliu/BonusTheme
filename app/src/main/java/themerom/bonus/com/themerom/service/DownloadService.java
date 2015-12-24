package themerom.bonus.com.themerom.service;

import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import themerom.bonus.com.themerom.callback.DownloadListener;
import themerom.bonus.com.themerom.callback.ProgressListener;
import themerom.bonus.com.themerom.db.DownloadDao;
import themerom.bonus.com.themerom.entity.TDownload;
import themerom.bonus.com.themerom.utils.CloseUtils;

/**
 * Created by bonus on 12/4/15.
 * Class name ${type_name}
 */
public class DownloadService extends Service {

    private static final String TAG = DownloadService.class.getSimpleName();
    private String downloadPath, savePath;
    private DownloadDao mDownloadDao;
    //多线程管理
    private ExecutorService executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDownloadDao = new DownloadDao(this);
    }

    public class MyBinder extends Binder implements DownloadListener{

        private long startPoint;
        private long endPoint;
        private long currentProgress;

        @Override
        public void download(final String url, final String savePath, final ProgressListener progressListener,final int threadSize
        ,final String label,final String fileName) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL uri = new URL(url);
                        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
                        connection.setConnectTimeout(5000);
                        connection.setRequestMethod("GET");
                        int responseCode = connection.getResponseCode();
                        if(responseCode != 200){
                            throw new NetworkErrorException("http connection response code not 200");
                        }
                        long max = connection.getContentLength();
                        progressListener.setMax(max);
                        //判断是否第一次下载
                        boolean isExist = mDownloadDao.queryExist(url);
                        if(isExist){
                            for (int i = 0; i < threadSize ; i++){
                                TDownload tDownload = new TDownload();
                                tDownload.setMax(max);
                                tDownload.setProgress(0);
                                tDownload.setSavePath(savePath);
                                tDownload.setThreadId(i);
                                tDownload.setUrl(url);
                                tDownload.setLabel(label);
                                mDownloadDao.insertDownload(tDownload);
                            }
                        }else{
                            //已经下载过
                            int len = 0;
                            for(int i = 0; i < threadSize ; i++){
                                long progress = mDownloadDao.queryDownload(url, i);
                                len += progress;
                            }
                            progressListener.setProgress(len);
                        }
                        File file = new File(savePath, fileName);
                        RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rwd");
                        randomAccessFile.setLength(max);
                        randomAccessFile.close();
                        //获取每个线程对应的下载量
                        long block = max%threadSize == 0 ? max/threadSize : max/threadSize+1;
                        for(int k = 0; k < threadSize ; k++){
                            //开启子线程进行下载
                            threadDownload(url,savePath,max,block,k,progressListener);
                        }
                    } catch (MalformedURLException e) {
                        progressListener.onFailed("failed",e);
                        e.printStackTrace();
                    } catch (IOException e) {
                        progressListener.onFailed("failed",e);
                        e.printStackTrace();
                    } catch (NetworkErrorException e) {
                        progressListener.onFailed("failed",e);
                        e.printStackTrace();
                    }
                }
            });
        }

        private void threadDownload(final String urlString, final String savePath, final long max, final long block, final int threadId, final ProgressListener progressListener) {
            currentProgress = mDownloadDao.queryDownload(urlString,threadId);
            startPoint = threadId * block + currentProgress;
            endPoint = (threadId+1)*block - 1;
            //开启子线程进行下载
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(urlString);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(5000);
                        connection.setRequestProperty("Range","bytes = "+startPoint + "-"+endPoint);
                        InputStream is = connection.getInputStream();
                        RandomAccessFile accessFile = new RandomAccessFile(savePath,"rwd");
                        accessFile.seek(startPoint);
                        //进行文件保存
                        int len = 0;
                        byte[] buffer = new byte[1024*8];
                        while((len = is.read(buffer)) != -1){
                            if(!DownloadDao.isDownLoad){
                                mDownloadDao.saveProgress(urlString,threadId,currentProgress);
                                break;
                            }
                            accessFile.write(buffer,0,len);
                            currentProgress += len;
                            progressListener.setProgress(currentProgress);
                        }
                        CloseUtils.close(is);
                        CloseUtils.close(accessFile);
                        progressListener.onSuccess("success");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        progressListener.onFailed("failed",e);
                    } catch (IOException e) {
                        progressListener.onFailed("failed",e);
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void pause() {

        }
    }

}
