package fleamarket.neworin.com.fleamarket.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * 项目工具类
 * Created by NewOr on 2016/1/14.
 */
public class AppUtil {
    public static ProgressDialog progressDialog;

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static ProgressDialog showProgressDialog(Context context, String msg) {

        return progressDialog = ProgressDialog.show(context, null, msg);
    }

    public static void closeProgressDialog() {
        progressDialog.dismiss();
    }
}
