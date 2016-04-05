package fleamarket.neworin.com.fleamarket.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * 项目工具类
 * Created by NewOr on 2016/1/14.
 */
public class AppUtil {

    public static ProgressDialog progressDialog;//创建ProgressDialog对象

    /**
     * 显示Toast
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示ProgressDialog
     *
     * @param context
     * @param msg
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context, String msg) {

        return progressDialog = ProgressDialog.show(context, null, msg);
    }

    /**
     * 关闭ProgressDialog
     */
    public static void closeProgressDialog() {
        progressDialog.dismiss();
    }

    public static void showAlertDialog(Context context, String message, DialogInterface.OnClickListener onClickListener,DialogInterface.OnClickListener onCancleClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("确认");
        builder.setMessage(message);
        builder.setPositiveButton("确认", onClickListener);
        builder.setNegativeButton("取消", onCancleClickListener);
        builder.create().show();
    }
}
