package fleamarket.neworin.com.fleamarket.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 创建数据库类
 * Created by NewOr on 2016/3/1.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context) {
        super(context, Constant.DB_NAME, null, Constant.VERSION);
    }

    // 首次创建数据库的时候调用， 一般可以把建库建表的操作在里边执行
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + Constant.TABLE_USER + "(" + Constant.USER_NAME + " text primary key,"
                + Constant.PASSWORD + " text not null," + Constant.PHONE_NUMBER + " text not null," + Constant.GENDER + " text,"
                + Constant.ADDRESS + " text," + Constant.AVATAR_URL + " text)");
        Log.d("NewOrin","数据库创建成功！");
    }

    // 这个方法只有在当数据库版本发生变化时自动执行
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
