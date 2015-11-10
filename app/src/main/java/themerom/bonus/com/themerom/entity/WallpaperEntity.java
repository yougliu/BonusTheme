package themerom.bonus.com.themerom.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by helios on 11/10/15.
 */
public class WallpaperEntity implements Serializable {

    private String id;
    private String name;
    private String description;
    private String time;
    private String width;
    private String height;
    private String maintype;
    private String sontype;
    private String issmall;
    private List<Preview> PreviewList;
    private List<Preview>  ImageList;
    private int downloadnum;
    private int goodsnum;

    public WallpaperEntity(List<Preview> previewList, List<Preview> imageList) {
        PreviewList = previewList;
        ImageList = imageList;
    }

    public WallpaperEntity(List<Preview> imageList) {
        ImageList = imageList;
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
