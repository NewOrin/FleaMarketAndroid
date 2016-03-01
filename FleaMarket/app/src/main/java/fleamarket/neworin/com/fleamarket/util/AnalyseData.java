package fleamarket.neworin.com.fleamarket.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fleamarket.neworin.com.fleamarket.bean.ImageCycle;

/**
 * 解析数据工具类
 * Created by NewOr on 2016/2/29.
 */
public class AnalyseData {

    public static ArrayList<ImageCycle> imageList(JSONArray jsonArray) {
        ArrayList<ImageCycle> imageList = new ArrayList<>();
        ImageCycle imageCycle;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                imageCycle = new ImageCycle(obj.getString("image_url"), obj.getString("image_desc"));
                imageList.add(imageCycle);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Log.d("NewOrin", "imageList = " + imageList.toString());
        return imageList;
    }
}
