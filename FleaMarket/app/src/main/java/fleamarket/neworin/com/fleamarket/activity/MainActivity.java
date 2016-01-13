package fleamarket.neworin.com.fleamarket.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.fragment.LoginFragment;
import fleamarket.neworin.com.fleamarket.fragment.RegisterFragment;

public class MainActivity extends AppCompatActivity {

    private String app_key = "ea28ef597102";
    private String app_secret = "d08b7d02a565ace684d4357be1884f43";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化SMSSDK
        SMSSDK.initSDK(this, app_key, app_secret);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, new LoginFragment());
        transaction.commit();
        initView();
        initEvent();

    }

    private void initEvent() {
    }

    private void initView() {
    }


    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                switchFragment(R.id.layout_login, new RegisterFragment());
                break;
            case R.id.btn_login:
                Log.d("NewOrin", "Work");
                //显示手机号
                RegisterPage registerPage = new RegisterPage();
                //注册回调事件
                registerPage.setRegisterCallback(new EventHandler() {
                    //事件完成后调用
                    @Override
                    public void afterEvent(int event, int result, Object data) {
                        //判断结果是否已经完成
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            //获取数据data
                            HashMap<String, Object> maps = (HashMap<String, Object>) data;
                            //国家，手机号(信息)
                            String country = (String) maps.get("country");
                            String phone = (String) maps.get("phone");
                            submitUserInfo(country, phone);
                        }
                        //获取数据
                    }
                });
                //显示注册界面
                registerPage.show(this);
                break;
        }
    }

    /**
     * 提交用户信息
     *
     * @param country
     * @param phone
     */
    public void submitUserInfo(String country, String phone) {
        SMSSDK.submitUserInfo("023", "NewOrin", null, country, phone);
    }

    private void switchFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewId, fragment).commit();
    }
}
