package fleamarket.neworin.com.fleamarket.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fleamarket.neworin.com.fleamarket.bean.Area;

/**
 * 解析Json字符串
 * Created by NewOr on 2016/4/3.
 */
public class AnalyticJson {
    public static List<Area> analyticAreaJson(JSONObject jsonObject) {
        List<Area> list = new ArrayList<>();
        try {
            String reason = jsonObject.getString("reason");
            if (!reason.equals("Succes")) return null;
            Area area;
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                area = new Area();
                area.setParent_id(object.getString("parent_id"));
                area.setArea_id(object.getString("area_id"));
                area.setLevel(object.getString("level"));
                area.setName(object.getString("name"));
                list.add(area);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("NewOrin", "解析完毕:" + list.toString());
        return list;
    }
}
