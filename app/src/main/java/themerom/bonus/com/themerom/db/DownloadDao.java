package themerom.bonus.com.themerom.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

import themerom.bonus.com.themerom.entity.TDownload;

/**
 * Created by helios on 12/24/15.
 */
public class DownloadDao {

    private static final String TAG = DownloadDao.class.getSimpleName();
    private Context mContext;
    private Dao<TDownload,Integer> downloadDaoOper;
    private DownloadDbHelper mHelper;
    public static boolean isDownLoad = false;

    public DownloadDao(Context mContext) {
        this.mContext = mContext;
        mHelper = DownloadDbHelper.getInstance(mContext);
        downloadDaoOper = mHelper.getDao(TDownload.class);
    }

    /**
     * 查询是否存在该下载内容
     * @param url
     * @return
     */
    public boolean queryExist(String url){
        if(url == null){
            return false;
        }
        try {
            List<TDownload> tDownloads = downloadDaoOper.queryBuilder().where().eq("url",url).query();
            if(tDownloads != null && tDownloads.size() > 0){
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据url，threadId查询对应threadId的progress
     * @param url
     * @param threadId
     * @return
     */
    public long queryDownload(String url,int threadId){
        try {
            List<TDownload> tDownloads = downloadDaoOper.queryBuilder().where().eq("url",url).and().eq("threadId",threadId).query();
            TDownload download = tDownloads.get(0);
            return download.getProgress();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("query download info failed!");
        }
    }

    /**
     * 添加一条新的记录,
     * success return 1
     * @param download
     */
    public boolean insertDownload(TDownload download){
        try {
            int insert = downloadDaoOper.create(download);
            if(insert == 1){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("insert download failed!");
        }
    }

    public boolean saveProgress(String url,int threadId,long progress){
        try {
            Where<TDownload, Integer> eq = downloadDaoOper.updateBuilder().updateColumnValue("progress", progress).where().eq("url", url).and().eq("threadId", threadId);
            if(eq != null){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new RuntimeException("save progress failed!");
        }
    }

    public void deleteDownload(String url){
        try {
            Where<TDownload, Integer> eq = downloadDaoOper.deleteBuilder().where().eq("url", url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
