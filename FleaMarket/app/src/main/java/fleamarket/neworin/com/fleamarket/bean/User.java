package fleamarket.neworin.com.fleamarket.bean;

import cn.bmob.v3.BmobUser;

/**
 * 用户信息实体类
 * Created by Administrator on 2016/1/15.
 */
public class User extends BmobUser {

    private String gender;
    private String address;
    private String avatar_url;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
