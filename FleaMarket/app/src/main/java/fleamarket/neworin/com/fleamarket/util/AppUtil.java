package fleamarket.neworin.com.fleamarket.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by NewOr on 2016/1/14.
 */
public class AppUtil {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
