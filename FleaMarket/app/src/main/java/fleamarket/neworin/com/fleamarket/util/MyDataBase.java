package fleamarket.neworin.com.fleamarket.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * 操纵数据库类
 * Created by NewOr on 2016/2/29.
 */
public class MyDataBase {

    private DataBaseHelper mDataBaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    private Context mContext;

    /**
     * 构造方法
     *
     * @param helper
     * @param db
     * @param context
     */
    public MyDataBase(DataBaseHelper helper, SQLiteDatabase db, Context context) {
        this.mDataBaseHelper = helper;
        this.mSqLiteDatabase = db;
        this.mContext = context;
    }

    /**
     * 数据库表查询
     *
     * @param table         表名称
     * @param columns       列名称数组
     * @param selection     条件子句，相当于where
     * @param selectionArgs 条件语句的参数数组
     * @param groupBy       分组
     * @param having        分组条件
     * @param orderBy       排序类
     * @param limit         分页查询的限制
     * @return
     */
    public Cursor doQueryDB(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor cursor = mSqLiteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        if (cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }

    /**
     * 插入数据操作
     *
     * @param table  表名
     * @param values values[0]是key,values[1]是value，以此类推
     */
    public void doInsertDB(String table, String... values) {
        ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据cv.put("password","123456");//添加用户名//实例化一个ContentValues用来装载待插入的数据cv.put("password","123456");//添加用户名
        cv.put(values[0], values[1]);
        mSqLiteDatabase.insert(table, null, cv);
    }

    /**
     * 更新表操作
     *
     * @param table       表名
     * @param key         表字段
     * @param value       内容
     * @param whereClause 条件
     * @param whereArgs   条件参数
     */
    public void doUpdateDB(String table, String key, String value, String whereClause, String[] whereArgs) {
        ContentValues cv = new ContentValues();
        cv.put(key, value);
        mSqLiteDatabase.update(table, cv, whereClause, whereArgs);
    }
}
