package themerom.bonus.com.themerom.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by helios on 12/24/15.
 */
@DatabaseTable(tableName = "user_table")
public class UserInfo implements Parcelable {
    protected UserInfo(Parcel in) {
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
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
    @DatabaseField(columnName = "age")
    private int age;
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(columnName = "schoolName")
    private String schoolName;
    @DatabaseField(columnName = "address")
    private String address;
    @DatabaseField(columnName = "favorite")
    private String favorite;

    public UserInfo() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }
}
