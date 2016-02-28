package fleamarket.neworin.com.fleamarket.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jorge.circlelibrary.ImageCycleView;

import java.util.ArrayList;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.bean.ImageCycle;

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
        ArrayList<ImageCycle> imageList = new ArrayList<>();
        imageList.add(new ImageCycle("http://attach.bbs.miui.com/forum/month_1012/101203122706c89249c8f58fcc.jpg", "小仓柚子"));
        imageList.add(new ImageCycle("http://bbsdown10.cnmo.com/attachments/201308/06/091441rn5ww131m0gj55r0.jpg", "抚媚妖娆性感美女"));
        imageList.add(new ImageCycle("http://kuoo8.com/wall_up/hsf2288/200801/2008012919460743597.jpg", "热血沸腾 比基尼"));
        imageList.add(new ImageCycle("http://d.3987.com/taiqiumein_141001/007.jpg", "台球美女"));
    }

    private void initCarsueView(ArrayList<ImageCycle> imageList) {
        LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getScreenHeight(getActivity()) * 3 / 10);
        image_cycle.setLayoutParams(cParams);
     }


    int getScreenHeight(Context context) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
