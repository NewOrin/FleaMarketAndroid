package fleamarket.neworin.com.fleamarket.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;
import com.jorge.circlelibrary.ImageCycleView;

import java.util.ArrayList;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.bean.ImageCycle;
import fleamarket.neworin.com.fleamarket.util.BitmapCache;
import fleamarket.neworin.com.fleamarket.util.MyApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ImageCycleView image_cycle;
    private View view;

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
        setImageData();
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
        imageDescList.add("小仓柚子");
        imageDescList.add("抚媚妖娆性感美女");
        imageDescList.add("热血沸腾");
        imageDescList.add("台球美女");
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("http://attach.bbs.miui.com/forum/month_1012/101203122706c89249c8f58fcc.jpg");
        urlList.add("http://bbsdown10.cnmo.com/attachments/201308/06/091441rn5ww131m0gj55r0.jpg");
        urlList.add("http://kuoo8.com/wall_up/hsf2288/200801/2008012919460743597.jpg");
        urlList.add("http://d.3987.com/taiqiumein_141001/007.jpg");
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


    private int getScreenHeight(Context context) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    private void loadImageWithCache(String imageUrl, ImageView imageView) {
        ImageLoader loader = new ImageLoader(MyApplication.getHttpQueues(), new BitmapCache());
        ImageLoader.ImageListener listener = loader.getImageListener(imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        //加载及缓存网络图片
        loader.get(imageUrl, listener);
    }
}
