package fleamarket.neworin.com.fleamarket.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences帮助类
 * Created by NewOr on 2016/3/2.
 */
public class SharedPreferencesHelper {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    /**
     * 构造方法
     *
     * @param context
     * @param name
     */
    public SharedPreferencesHelper(Context context, String name) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * 存放字符串类数据
     *
     * @param key
     * @param value
     */
    public void putStringValue(String key, String value) {
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 存放Boolean类型数据
     *
     * @param key
     * @param value
     */
    public void putBooleanValue(String key, Boolean value) {
        editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获取字符串类型数据
     *
     * @param key
     * @return
     */
    public String getStringValue(String key) {
        return sharedPreferences.getString(key, null);
    }

    public Boolean getBooleanValue(String key) {
        return sharedPreferences.getBoolean(key, false);
    }
}
