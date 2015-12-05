package themerom.bonus.com.themerom.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by bonus on 12/4/15.
 * Class name ${type_name}
 */
public class DownloadDbHelperDao {

    private static final String TAG = DownloadDbHelperDao.class.getSimpleName();
    private DownloadDbHelper mHelper = null;
    private SQLiteDatabase db ;

    public DownloadDbHelperDao(Context context){
        mHelper = new DownloadDbHelper(context);
        db = mHelper.getReadableDatabase();
    }

    /**
     * 插入数据
     * @param table_name
     * @param values
     * @return
     */
    public long insertData(String table_name, ContentValues values){
        if(db == null){
            db = mHelper.getReadableDatabase();
        }
        long index = db.insert(table_name,"_id",values);
        return index;//if -1,insert failed
    }

    /**
     * 查询数据
     * @param table_name
     * @param name
     * @return
     */
    public Cursor queryData(String table_name,String name){
        if(db == null){
            db = mHelper.getReadableDatabase();
        }
        Cursor cursor = null;
        try{
            if(name != null){
                cursor = db.query(table_name, null, "name = ?", new String[]{name}, null, null, null);
            }else{
                cursor = db.query(table_name,null,null,null,null,null,null);
            }
        }catch(Exception e){
            throw new RuntimeException(table_name + " db query error");
        }finally {
            if(cursor != null){
                cursor.close();
            }
            if(db != null){
                db.close();
            }
        }
        return cursor;
    }

    /**
     * 更新数据
     * @param table_name
     * @param name
     * @param values
     * @return
     */
    public boolean updateData(String table_name, String name, ContentValues values){
        if(db == null){
            db = mHelper.getReadableDatabase();
        }
        try{
            db.update(table_name,values,"name = ?",new String[]{name});
            return true;
        }catch(Exception e){
            new RuntimeException(table_name + " update failed");
        }finally {
            if(db != null){
                db.close();
            }
        }
        return false;
    }

    public boolean deleteData(String table_name, String name){
        if(db == null){
            db = mHelper.getReadableDatabase();
        }
        try{
            db.delete(table_name,"name = ?",new String[]{name});
            return true;
        }catch(Exception e){
            new RuntimeException(table_name + " db delete failed");
        }finally{
            if(db != null){
                db.close();
            }
        }
        return false;
    }
}
