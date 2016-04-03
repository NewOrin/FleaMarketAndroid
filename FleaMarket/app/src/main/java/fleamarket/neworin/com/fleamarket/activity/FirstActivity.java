package fleamarket.neworin.com.fleamarket.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.fragment.LoginFragment;
import fleamarket.neworin.com.fleamarket.fragment.RegisterFragment;

public class FirstActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private Fragment currentFragment;//存放当前的Fragment
    private Fragment getCurrentFragment;//用于比较当前Fragment
    private TextView tv_left, tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_first);
        initFragment();
        initView();
        initEvent();
    }

    private void initEvent() {
    }

    /**
     * 显示第一个Fragment
     */
    private void initFragment() {
        currentFragment = new LoginFragment();
        getFragmentManager().beginTransaction().add(R.id.fragment_container, currentFragment).commit();
    }

    /**
     * 初始化View
     */
    private void initView() {
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    /**
     * 各个控件事件的监听
     *
     * @param v
     */
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                nextFragment(true, new RegisterFragment());
//                switchFragment(new RegisterFragment());
                break;
            case R.id.btn_login:

                 break;
            case R.id.tv_left:
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
        }
    }

    /**
     * 切换Fragment
     *
     * @param fragment
     */
    private void switchFragment(Fragment fragment) {
        if (currentFragment != fragment) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!fragment.isAdded()) {
                transaction.hide(currentFragment).replace(R.id.fragment_container, fragment).commit();
            } else {
                transaction.hide(currentFragment).show(fragment).commit();
            }
            currentFragment = fragment;
        }
    }


    /**
     * 退出确认对话框
     */
    private void confirmExitDialog() {
        new AlertDialog.Builder(this).setTitle("确认").setMessage("确认退出?").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();//退出程序
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public void onBackStackChanged() {
        getCurrentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        if (getCurrentFragment instanceof LoginFragment) {
            tv_title.setText("登录");
            tv_left.setVisibility(View.GONE);
        } else if (getCurrentFragment instanceof RegisterFragment) {
            tv_left.setVisibility(View.VISIBLE);
            tv_title.setText("注册");
        }
    }

    //创建新的Fragment对象，如果backStackFlag属性为true，会将该对象添加到回退栈当中
    private void nextFragment(boolean backStackFlag, Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (!fragment.isAdded()) {
            transaction.hide(currentFragment).replace(R.id.fragment_container, fragment);
        } else {
            transaction.hide(currentFragment).show(fragment);
        }
        currentFragment = fragment;
        if (backStackFlag) {
            //将当前Fragment的状态添加到回退栈中
            transaction.addToBackStack(null);
            transaction.commit();
            //指定回退栈监听器
            manager.addOnBackStackChangedListener(this);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            confirmExitDialog();
        }
        return super.onKeyDown(keyCode, event);
    }
}
