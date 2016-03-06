package fleamarket.neworin.com.fleamarket.util;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cn.bmob.v3.Bmob;
import fleamarket.neworin.com.fleamarket.activity.FirstActivity;
import fleamarket.neworin.com.fleamarket.activity.MainActivity;

/**
 * Created by NewOr on 2016/2/27.
 */
public class MyApplication extends Application {
    private static RequestQueue queues;

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, Constant.APPLICATION_ID);
        queues = Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getHttpQueues() {
        return queues;
    }
}
