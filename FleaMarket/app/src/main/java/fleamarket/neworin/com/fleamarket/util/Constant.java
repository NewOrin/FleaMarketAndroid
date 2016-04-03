package fleamarket.neworin.com.fleamarket.util;

/**
 * Created by NewOr on 2016/1/14.
 */
public class Constant {
    public static final String APPLICATION_ID = "917763ebdf7e440bed6485af5adf7bb6";

    public static final String DB_NAME = "account.db";//建立数据库名称
    public static final int VERSION = 1;//数据库的版本

    /**
     * 用户表
     */
    public static final String TABLE_USER = "_user";//用户表
    public static final String USER_NAME = "username";//用户表中的用户姓名
    public static final String PASSWORD = "password";//密码
    public static final String PHONE_NUMBER = "phonenumber";//手机号
    public static final String GENDER = "gender";//性别
    public static final String ADDRESS = "address";
    public static final String AVATAR_URL = "avatarurl";//头像图片url
    public static final String IS_AUTO_LOGIN = "isAutoLogin";
    public static final String POST_STR = "post";//用户帖子

    public static final String GET_AREA_KEY = "36fbf23ba27f410685544933c78eb33c";
    public static final String GET_AREA_INFO_URL = "http://api.avatardata.cn/SimpleArea/LookUp?key="+GET_AREA_KEY;
}
