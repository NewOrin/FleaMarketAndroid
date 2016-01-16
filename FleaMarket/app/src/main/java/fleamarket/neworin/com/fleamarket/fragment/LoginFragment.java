package fleamarket.neworin.com.fleamarket.fragment;


import android.app.Fragment;
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
import fleamarket.neworin.com.fleamarket.util.AppUtil;
import fleamarket.neworin.com.fleamarket.util.ClearEditText;
import fleamarket.neworin.com.fleamarket.util.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ClearEditText et_login_account, et_login_password;
    private Button btn_login;
    private String username, password;
    private TextView tv_icon_account, tv_icon_password;

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
                AppUtil.showToast(getActivity(), "登录成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                AppUtil.showToast(getActivity(), "登录失败");
                Log.d("NewOrin", "登录失败" + msg);
            }
        });
    }
}
