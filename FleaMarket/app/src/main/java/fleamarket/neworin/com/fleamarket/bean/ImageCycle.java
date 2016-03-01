package fleamarket.neworin.com.fleamarket.bean;

import cn.bmob.v3.BmobObject;

/**
 * 轮播图片信息
 * Created by NewOr on 2016/2/28.
 */
public class ImageCycle extends BmobObject {
    private String image_url;
    private String image_desc;

    public ImageCycle(String image_url, String image_desc) {
        this.image_url = image_url;
        this.image_desc = image_desc;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_desc() {
        return image_desc;
    }

    public void setImage_desc(String image_desc) {
        this.image_desc = image_desc;
    }

    @Override
    public String toString() {
        return "ImageCycle{" +
                "image_url='" + image_url + '\'' +
                ", image_desc='" + image_desc + '\'' +
                '}';
    }
}
