package fleamarket.neworin.com.fleamarket.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;
import com.jorge.circlelibrary.ImageCycleView;

import org.json.JSONArray;

import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.bean.ImageCycle;
import fleamarket.neworin.com.fleamarket.util.AnalyseData;
import fleamarket.neworin.com.fleamarket.util.BitmapCache;
import fleamarket.neworin.com.fleamarket.net.GetNetDataUtil;
import fleamarket.neworin.com.fleamarket.util.MyApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ImageCycleView image_cycle;
    private View view;
    private ArrayList<ImageCycle> imageList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        queryData(getActivity(), "ImageCycle");
    }

    /**
     * 初始化控件
     */
    private void initView() {
        image_cycle = (ImageCycleView) view.findViewById(R.id.image_cycle);
    }

    /**
     * 给图片设置信息
     */
    private void setImageData() {
        ArrayList<String> imageDescList = new ArrayList<>();
        ArrayList<String> urlList = new ArrayList<>();
        for (int i = 0; i < imageList.size(); i++) {
            imageDescList.add(imageList.get(i).getImage_desc());
            urlList.add(imageList.get(i).getImage_url());
        }
        Log.d("NewOrin", "imageDescList = " + imageDescList.toString());
        Log.d("NewOrin", "urlList = " + urlList.toString());
        initCarsueView(imageDescList, urlList);
    }

    /**
     * 初始化轮播图
     *
     * @param imageDescList
     * @param urlList
     */
    private void initCarsueView(ArrayList<String> imageDescList, ArrayList<String> urlList) {
        LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getScreenHeight(getActivity()) * 3 / 10);
        image_cycle.setLayoutParams(cParams);
        ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                /**显示图片**/
                loadImageWithCache(imageURL, imageView);
            }

            @Override
            public void onImageClick(int position, View imageView) {
                /**实现点击事件**/
            }
        };
        image_cycle.setImageResources(imageDescList, urlList, mAdCycleViewListener);
        image_cycle.startImageCycle();
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    private int getScreenHeight(Context context) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 加载图片
     *
     * @param imageUrl
     * @param imageView
     */
    private void loadImageWithCache(String imageUrl, ImageView imageView) {

        ImageLoader loader = new ImageLoader(MyApplication.getHttpQueues(), new BitmapCache());
        ImageLoader.ImageListener listener = loader.getImageListener(imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        //加载及缓存网络图片
        loader.get(imageUrl, listener);
    }

    /**
     * 查询表的数据
     *
     * @param table
     */
    public void queryData(Context context, String table) {
        BmobQuery query = new BmobQuery(table);
        query.findObjects(context, new FindCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                Log.d("NewOrin", "获取成功" + jsonArray.toString());
                imageList = AnalyseData.imageList(jsonArray);
                setImageData();
            }

            @Override
            public void onFailure(int i, String s) {
                Log.d("NewOrin", "获取失败" + s);
            }
        });
    }
}
