package fleamarket.neworin.com.fleamarket.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 用户地址实体类
 * Created by NewOr on 2016/4/4.
 */

public class Address extends BmobObject implements Serializable {

    private User user;
    private String reciever_name;//收货人
    private String phone;//联系电话
    private String area_address;//所在地区
    private String detail_address;//详细街道地址

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReciever_name() {
        return reciever_name;
    }

    public void setReciever_name(String reciever_name) {
        this.reciever_name = reciever_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea_address() {
        return area_address;
    }

    public void setArea_address(String area_address) {
        this.area_address = area_address;
    }

    public String getDetail_address() {
        return detail_address;
    }

    public void setDetail_address(String detail_address) {
        this.detail_address = detail_address;
    }

}
