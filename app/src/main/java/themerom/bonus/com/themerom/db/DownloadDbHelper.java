package themerom.bonus.com.themerom.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bonus on 12/4/15.
 * Class name ${type_name}
 */
public class DownloadDbHelper extends SQLiteOpenHelper{

    private static final String TAG = DownloadDbHelper.class.getSimpleName();
    private static final String DB_NAME = "bonustheme.db";
    private static final int DB_VERSION = 1;

    public DownloadDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DownloadDbHelper(Context context){
        this(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String dTable = "create table download ("+
                "_id integer primary key autoincrement,"+
                "name text,"+
                "number int,"+
                "progress int)";
        String zTable = "create table zambia ("+
                "_id integer primary key autoincrement,"+
                "name text,"+
                "number int)";
        db.execSQL(dTable);
        db.execSQL(zTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
