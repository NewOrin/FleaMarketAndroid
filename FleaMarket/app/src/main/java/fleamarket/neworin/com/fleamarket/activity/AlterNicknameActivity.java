package fleamarket.neworin.com.fleamarket.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.util.AppUtil;
import fleamarket.neworin.com.fleamarket.view.TopBar;

public class AlterNicknameActivity extends AppCompatActivity implements View.OnClickListener {

    private TopBar alterinfo_topbar;
    private EditText et_set_nickname;
    private Button btn_set_nickname;
    private TextView tv_show_strnumber;
    private int charMaxNum = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_alter_nickname);
        initView();
        initEvent();
    }

    private void initEvent() {
        alterinfo_topbar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        et_set_nickname.addTextChangedListener(textWatcher);
        btn_set_nickname.setOnClickListener(this);
    }

    private void initView() {
        et_set_nickname = (EditText) findViewById(R.id.et_set_nickname);
        tv_show_strnumber = (TextView) findViewById(R.id.tv_show_strnumber);
        btn_set_nickname = (Button) findViewById(R.id.btn_set_nickname);
        alterinfo_topbar = (TopBar) findViewById(R.id.alter_nickname_topbar);
        //接收传送的值
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            alterinfo_topbar.setTitleText(extras.getString("title"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set_nickname:
                if (!et_set_nickname.getText().toString().equals("")) {
                    Intent intent = new Intent();
                    intent.putExtra("nickname", et_set_nickname.getText().toString());
                    setResult(AccountActivity.SET_NICKNAME_ACTIVITY_BACK, intent);
                    finish();
                } else {
                    AppUtil.showToast(AlterNicknameActivity.this, "昵称不能为空!");
                }
                break;
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s != null) {
                tv_show_strnumber.setText("还能输入" + (charMaxNum - s.length()) + "个字");
                if (!et_set_nickname.getText().toString().equals("")) {
                    btn_set_nickname.setClickable(true);
                } else {
                    btn_set_nickname.setClickable(false);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
