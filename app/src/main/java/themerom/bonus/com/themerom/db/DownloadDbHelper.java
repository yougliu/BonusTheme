package themerom.bonus.com.themerom.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import themerom.bonus.com.themerom.entity.TDownload;
import themerom.bonus.com.themerom.entity.UserInfo;

/**
 * Created by bonus on 12/4/15.
 * Class name ${type_name}
 */
public class DownloadDbHelper extends OrmLiteSqliteOpenHelper{

    private static final String DB_NAME = "t_download.db";
    private static final int DB_VERSION = 1;
    private Map<String , Dao> mDaos = new HashMap<>();
    private static DownloadDbHelper mInstance;

    public DownloadDbHelper(Context context) {
        super(context, DB_NAME , null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, TDownload.class);
            TableUtils.createTable(connectionSource, UserInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource,TDownload.class,true);
            TableUtils.dropTable(connectionSource,UserInfo.class,true);
            this.onCreate(database,connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DownloadDbHelper getInstance(Context context){
        context = context.getApplicationContext();
        if(mInstance == null){
            synchronized (DownloadDbHelper.class){
                if(mInstance == null){
                    mInstance = new DownloadDbHelper(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * get dao
     * @param clazz
     * @return
     */
    public synchronized Dao getDao(Class clazz){
        Dao dao = null;
        String className = clazz.getSimpleName();
        if(mDaos.containsKey(className)){
            dao = mDaos.get(className);
        }else{
            try {
                dao = super.getDao(clazz);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();
        for (String className : mDaos.keySet()){
            Dao dao = mDaos.get(className);
            dao = null;
        }
    }
}
