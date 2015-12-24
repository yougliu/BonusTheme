package themerom.bonus.com.themerom.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by helios on 12/24/15.
 */
@DatabaseTable(tableName = "tb_download")
public class TDownload implements Parcelable{
    protected TDownload(Parcel in) {
    }

    public static final Creator<TDownload> CREATOR = new Creator<TDownload>() {
        @Override
        public TDownload createFromParcel(Parcel in) {
            return new TDownload(in);
        }

        @Override
        public TDownload[] newArray(int size) {
            return new TDownload[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
    @DatabaseField(generatedId = true)
    private int _id;
    @DatabaseField(columnName = "max")
    private long max;
    @DatabaseField(columnName = "progress")
    private long progress;
    @DatabaseField(columnName = "threadId")
    private int threadId;
    @DatabaseField(columnName = "label")
    private String label;
    @DatabaseField(columnName = "url")
    private String url;
    @DatabaseField(columnName = "savePath")
    private String savePath;
//    @DatabaseField(columnName = "userInfo",canBeNull = true,foreign = true,foreignAutoRefresh = true)
//    private UserInfo userInfo;

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public TDownload() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

//    public UserInfo getUserInfo() {
//        return userInfo;
//    }
//
//    public void setUserInfo(UserInfo userInfo) {
//        this.userInfo = userInfo;
//    }
}
