package themerom.bonus.com.themerom.entity;

import java.io.Serializable;

/**
 * Created by helios on 11/10/15.
 */
public class Preview implements Serializable{

    private String  Pixel;
    private String Path;

    public Preview(String pixel, String path) {
        Pixel = pixel;
        Path = path;
    }

    public Preview() {
    }

    public String getPixel() {
        return Pixel;
    }

    public String getPath() {
        return Path;
    }

    public void setPixel(String pixel) {
        Pixel = pixel;
    }

    public void setPath(String path) {
        Path = path;
    }
}
