package themerom.bonus.com.themerom.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by helios on 11/10/15.
 */
public class ThemeEntity implements Serializable {

    private String id;
    private String name;
    private String description;
    private String time;
    private String width;
    private String height;
    private String maintype;
    private String sontype;
    private String issmall;
    private String themename;
    private String designerName;
    private String specialName;
    private String packageName;
    private List<Preview>  PreviewList;
    private List<Preview> ImageList;
    private int downloadnum;
    private int goodsnum;

    public ThemeEntity(String id, String name, String themename, List<Preview> previewList) {
        this.id = id;
        this.name = name;
        this.themename = themename;
        PreviewList = previewList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getMaintype() {
        return maintype;
    }

    public void setMaintype(String maintype) {
        this.maintype = maintype;
    }

    public String getSontype() {
        return sontype;
    }

    public void setSontype(String sontype) {
        this.sontype = sontype;
    }

    public String getIssmall() {
        return issmall;
    }

    public void setIssmall(String issmall) {
        this.issmall = issmall;
    }

    public String getThemename() {
        return themename;
    }

    public void setThemename(String themename) {
        this.themename = themename;
    }

    public String getDesignerName() {
        return designerName;
    }

    public void setDesignerName(String designerName) {
        this.designerName = designerName;
    }

    public String getSpecialName() {
        return specialName;
    }

    public void setSpecialName(String specialName) {
        this.specialName = specialName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<Preview> getPreviewList() {
        return PreviewList;
    }

    public void setPreviewList(List<Preview> previewList) {
        PreviewList = previewList;
    }

    public List<Preview> getImageList() {
        return ImageList;
    }

    public void setImageList(List<Preview> imageList) {
        ImageList = imageList;
    }

    public int getDownloadnum() {
        return downloadnum;
    }

    public void setDownloadnum(int downloadnum) {
        this.downloadnum = downloadnum;
    }

    public int getGoodsnum() {
        return goodsnum;
    }

    public void setGoodsnum(int goodsnum) {
        this.goodsnum = goodsnum;
    }
}
