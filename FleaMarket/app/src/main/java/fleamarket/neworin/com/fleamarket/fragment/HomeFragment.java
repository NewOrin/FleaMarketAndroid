package fleamarket.neworin.com.fleamarket.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.adapter.HomeListViewAdapter;
import fleamarket.neworin.com.fleamarket.bean.Post;
import fleamarket.neworin.com.fleamarket.util.AppUtil;
import fleamarket.neworin.com.fleamarket.util.Constant;
import fleamarket.neworin.com.fleamarket.util.DataBaseHelper;
import fleamarket.neworin.com.fleamarket.util.MyDataBase;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private View view;
    private ListView listview_home;
    private MyDataBase myDataBase;

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
        AppUtil.showProgressDialog(getActivity(), "请稍后");
        initView();
        DataBaseHelper helper = new DataBaseHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        myDataBase = new MyDataBase(helper, db, getActivity());
    }

    /**
     * 初始化控件
     */

    private void initView() {
        listview_home = (ListView) view.findViewById(R.id.listview_home);
        BmobQuery<Post> query = new BmobQuery<>();
        query.include("author");
        query.findObjects(getActivity(), new FindListener<Post>() {
            @Override
            public void onSuccess(List<Post> list) {
                Log.d("NewOrin", list.toString());
                if (list.size() != 0) {
                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(list);
                    //若表中post字段没有数据，则插入数据，有则更新
                    if (myDataBase.doQueryDB(Constant.TABLE_POST, new String[]{Constant.POST}, null, null, null, null, null, null) != null) {
                        Log.d("NewOrin", "需要更新post");
                        myDataBase.doUpdateDB(Constant.TABLE_POST, Constant.POST, jsonStr, null, null);
                    } else {
                        Log.d("NewOrin", "需要插入post");
                        myDataBase.doInsertDB(Constant.TABLE_POST, Constant.POST, jsonStr);
                    }
                    showListView(list);
                } else {
                    AppUtil.showToast(getActivity(), "没有数据");
                }
            }

            @Override
            public void onError(int i, String s) {
                AppUtil.showToast(getActivity(), "获取数据失败");
            }
        });
    }

    /**
     * 显示ListView
     *
     * @param list 获得帖子的数据
     */
    private void showListView(List<Post> list) {
        listview_home.setAdapter(new HomeListViewAdapter(list, getActivity(), listview_home));
        if (AppUtil.progressDialog != null) {
            AppUtil.closeProgressDialog();
        }
    }

}
