package fleamarket.neworin.com.fleamarket.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.activity.SelectAreaActivity;
import fleamarket.neworin.com.fleamarket.adapter.CommonAdapter;
import fleamarket.neworin.com.fleamarket.bean.Area;
import fleamarket.neworin.com.fleamarket.net.NetWorkUtil;
import fleamarket.neworin.com.fleamarket.util.AnalyticJson;
import fleamarket.neworin.com.fleamarket.util.AppUtil;
import fleamarket.neworin.com.fleamarket.util.FragmentInteraction;
import fleamarket.neworin.com.fleamarket.util.ViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProvinceFragment extends Fragment {

    private View view;
    private static List<Area> data_list = new ArrayList<>();
    private ListView province_listview;
    private FragmentInteraction interactionListener;

    public ProvinceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_province, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppUtil.showProgressDialog(getActivity(), "请稍后...");
        if (data_list.size() == 0) {
            getAreaData();
        } else initView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FragmentInteraction) {
            interactionListener = (FragmentInteraction) activity;
        } else new IllegalArgumentException("activity must implements FragmentInteraction");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("NewOrin", "ProvinceFragment---onDetach方法执行...");
        interactionListener = null;
    }

    private void showListView() {
        province_listview.setAdapter(new CommonAdapter<Area>(getActivity(), data_list, R.layout.item_listview_area) {
            @Override
            public void convert(ViewHolder holder, Area area) {
                holder.setText(R.id.tv_show_area, area.getName());
            }

            @Override
            public void itemClick(ViewHolder holder, Area area,int item_position) {

            }
        });
        AppUtil.closeProgressDialog();
        iniEvent();
    }

    private void iniEvent() {
        province_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                interactionListener.sendArgs(data_list.get(position).getLevel(), data_list.get(position).getName(), data_list.get(position).getArea_id());
            }
        });
    }


    private void initView() {
        if (data_list != null) {
            province_listview = (ListView) view.findViewById(R.id.province_listview);
            showListView();
        } else AppUtil.showToast(getActivity(), "加载数据失败!");
    }

    /**
     * 访问网络获取数据
     */
    private void getAreaData() {
        NetWorkUtil netWorkUtil = new NetWorkUtil(getActivity());
        netWorkUtil.getAreaInfo(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("NewOrin", "查询行政返回数据:" + jsonObject.toString());
                data_list = AnalyticJson.analyticAreaJson(jsonObject);
                initView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }, "");
    }

    @Override
    public void onDestroy() {
        Log.d("NewOrin", "ProvinceFragment---onDestroy方法执行...");
        super.onDestroy();
    }

}
