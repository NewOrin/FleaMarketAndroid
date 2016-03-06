package fleamarket.neworin.com.fleamarket.fragment;


import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.activity.MainActivity;
import fleamarket.neworin.com.fleamarket.util.AppUtil;
import fleamarket.neworin.com.fleamarket.util.ClearEditText;
import fleamarket.neworin.com.fleamarket.util.Constant;
import fleamarket.neworin.com.fleamarket.util.DataBaseHelper;
import fleamarket.neworin.com.fleamarket.util.SharedPreferencesHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ClearEditText et_login_account, et_login_password;
    private Button btn_login;
    private String username, password;
    private TextView tv_icon_account, tv_icon_password;
    private SQLiteDatabase db;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initEvent();
        Bmob.initialize(getActivity(), Constant.APPLICATION_ID);
        DataBaseHelper helper = new DataBaseHelper(getActivity());
        db = helper.getWritableDatabase();
    }

    private void initEvent() {
        btn_login.setOnClickListener(this);
    }


    private void initView() {
        et_login_account = (ClearEditText) view.findViewById(R.id.et_login_account);
        et_login_password = (ClearEditText) view.findViewById(R.id.et_login_password);
        btn_login = (Button) view.findViewById(R.id.btn_login);
        tv_icon_account = (TextView) view.findViewById(R.id.tv_icon_account);
        tv_icon_password = (TextView) view.findViewById(R.id.tv_icon_password);
        setIcon();
    }

    /**
     * 给TextView指定文字
     */
    private void setIcon() {
        Typeface iconfont = Typeface.createFromAsset(getActivity().getAssets(), "font/iconfont.ttf");
        tv_icon_account.setTypeface(iconfont);
        tv_icon_password.setTypeface(iconfont);
    }

    @Override
    public void onClick(View view) {
        username = et_login_account.getText().toString();
        password = et_login_password.getText().toString();
        switch (view.getId()) {
            case R.id.btn_login:
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    AppUtil.showToast(getActivity(), "用户名或密码不能为空");
                } else {
                    doLogin();
                }
                break;
        }
    }

    private void doLogin() {
        BmobUser user = new BmobUser();
        user.setUsername(username);
        user.setPassword(password);
        user.login(getActivity(), new SaveListener() {
            @Override
            public void onSuccess() {
                if(!ifUsernameExist(username)){
                insertDBData(username, password);}
                insertSPData(username, password);
                startActivity(new Intent(getActivity(), MainActivity.class));
                AppUtil.showToast(getActivity(), "登录成功");
                getActivity().finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                AppUtil.showToast(getActivity(), "登录失败");
                Log.d("NewOrin", "登录失败" + msg);
            }
        });
    }

    /**
     * 给数据库插入数据
     * @param username
     * @param password
     */
    private void insertDBData(String username, String password) {
        ContentValues cv = new ContentValues();
        cv.put(Constant.USER_NAME, username);
        cv.put(Constant.PASSWORD, password);
        cv.put(Constant.PHONE_NUMBER, username);
        db.insert(Constant.TABLE_USER, null, cv);
        cv.clear();
        Log.d("NewOrin", "用户添加数据库成功！");
    }

    /**
     * SharedPreferences插入数据
     * @param username
     * @param password
     */
    private void insertSPData(String username, String password) {
        SharedPreferencesHelper sph = new SharedPreferencesHelper(getActivity(), "userinfo");
        sph.putStringValue(Constant.USER_NAME, username);
        sph.putStringValue(Constant.PASSWORD, password);
        sph.putBooleanValue(Constant.IS_AUTO_LOGIN, true);
    }
    private Boolean ifUsernameExist(String username){
        String sql = "select * from "+Constant.TABLE_USER + " where "+Constant.USER_NAME + " = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{username});
        if(cursor.moveToFirst()){
            Log.d("NewOrin","用户存在，无需插入数据到数据库");
            return true;
        }else {
            Log.d("NewOrin","不存在用户,需要插入");
            return false;
        }
    }
}
