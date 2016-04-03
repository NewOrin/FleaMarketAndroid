package fleamarket.neworin.com.fleamarket.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 发布帖子实体类
 * Created by NewOr on 2016/3/10.
 */
public class Post extends BmobObject {

    private User author;//发布人,这里体现的是一对一的关系，该帖子属于某个用户
    private String title;//帖子题目
    private String desc;//帖子描述
    private List<String> image_urls;//帖子图片
    private String ifMail;//是否包邮
    private String sell_price;//售价
    private String orig_price;//原价
    private String address;//地址
    private String favour_count;//赞的人数
    private String comment_count;//评论条数
    private BmobRelation post_like_user;//多对多关系：用于存储喜欢该帖子的所有用户

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getImage_urls() {
        return image_urls;
    }

    public void setImage_urls(List<String> image_urls) {
        this.image_urls = image_urls;
    }

    public String getIfMail() {
        return ifMail;
    }

    public void setIfMail(String ifMail) {
        this.ifMail = ifMail;
    }

    public String getSell_price() {
        return sell_price;
    }

    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }

    public String getOrig_price() {
        return orig_price;
    }

    public void setOrig_price(String orig_price) {
        this.orig_price = orig_price;
    }

    public BmobRelation getPost_like_user() {
        return post_like_user;
    }

    public void setPost_like_user(BmobRelation post_like_user) {
        this.post_like_user = post_like_user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFavour_count() {
        return favour_count;
    }

    public void setFavour_count(String favour_count) {
        this.favour_count = favour_count;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    @Override
    public String toString() {
        return "Post{" +
                "author=" + author +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", image_urls=" + image_urls +
                ", ifMail='" + ifMail + '\'' +
                ", sell_price='" + sell_price + '\'' +
                ", orig_price='" + orig_price + '\'' +
                ", address='" + address + '\'' +
                ", favour_count='" + favour_count + '\'' +
                ", comment_count='" + comment_count + '\'' +
                ", post_like_user=" + post_like_user +
                '}';
    }
}
