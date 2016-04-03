package fleamarket.neworin.com.fleamarket.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.util.AppUtil;
import fleamarket.neworin.com.fleamarket.view.TopBar;

public class AlterPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private TopBar alter_password_topbar;
    private Button btn_alter_pwd;
    private EditText et_input_pwd1, et_input_pwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_alter_password);
        initView();
        initEvent();
    }

    private void initEvent() {
        alter_password_topbar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        btn_alter_pwd.setOnClickListener(this);
    }

    private void initView() {
        alter_password_topbar = (TopBar) findViewById(R.id.alter_password_topbar);
        btn_alter_pwd = (Button) findViewById(R.id.btn_alter_pwd);
        et_input_pwd1 = (EditText) findViewById(R.id.et_input_pwd1);
        et_input_pwd2 = (EditText) findViewById(R.id.et_input_pwd2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_alter_pwd:
                String password1 = et_input_pwd1.getText().toString();
                String password2 = et_input_pwd2.getText().toString();
                if (TextUtils.isEmpty(password1) && TextUtils.isEmpty(password2)) {
                    AppUtil.showToast(AlterPasswordActivity.this, "请将密码输入完整!");
                } else if (!password1.equals(password2)) {
                    AppUtil.showToast(AlterPasswordActivity.this, "两次输入密码不一致!");
                } else {
                    AppUtil.showToast(AlterPasswordActivity.this, "修改成功!!");
                }
                break;
        }
    }
}
