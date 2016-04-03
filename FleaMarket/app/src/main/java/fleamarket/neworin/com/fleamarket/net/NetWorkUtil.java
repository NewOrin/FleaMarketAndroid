package fleamarket.neworin.com.fleamarket.net;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fleamarket.neworin.com.fleamarket.bean.Area;
import fleamarket.neworin.com.fleamarket.util.Constant;

/**
 * 访问网络类
 * Created by NewOr on 2016/4/1.
 */
public class NetWorkUtil {

    RequestQueue queue;

    /**
     * 构造方法
     *
     * @param context
     */
    public NetWorkUtil(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    /**
     * 从网络获取行政区数据
     *
     * @param params
     * @return
     */
    public void getAreaInfo(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, final String params) {
        JsonObjectRequest request = new JsonObjectRequest(Constant.GET_AREA_INFO_URL + "&parentId=" + URLEncoder.encode(params), null, listener, errorListener) {
        };
        queue.add(request);
    }
}
