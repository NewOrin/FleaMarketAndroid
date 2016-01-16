package fleamarket.neworin.com.fleamarket.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;
import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.util.AppUtil;
import fleamarket.neworin.com.fleamarket.util.ClearEditText;
import fleamarket.neworin.com.fleamarket.util.Constant;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button btn_register, btn_validate;
    private int millisInFuture = 3000;//倒计时秒数
    private int countDownInterval = 1000;//倒计时多少
    private TimeCount timeCount;
    private ClearEditText et_register_phone, et_validate, et_register_password1, et_register_password2;

    private String phone, validate, password;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initEvent();
        Bmob.initialize(getActivity(), Constant.APPLICATION_ID);
    }

    /**
     * 初始化监听事件
     */
    private void initEvent() {
        btn_validate.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_validate = (Button) view.findViewById(R.id.btn_validate);
        timeCount = new TimeCount(millisInFuture, countDownInterval);
        et_register_phone = (ClearEditText) view.findViewById(R.id.et_register_phone);
        et_validate = (ClearEditText) view.findViewById(R.id.et_validate);
        et_register_password1 = (ClearEditText) view.findViewById(R.id.et_register_password1);
        et_register_password2 = (ClearEditText) view.findViewById(R.id.et_register_password2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_validate:
                phone = et_register_phone.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    AppUtil.showToast(getActivity(), "验证码已发送到手机");
                    sendVerificaCode(et_register_phone.getText().toString());
                    timeCount.start();
                } else {
                    AppUtil.showToast(getActivity(), "手机号码不能为空!");
                }
                break;
            case R.id.btn_register:
                if (et_register_password1.getText().toString().equals(et_register_password2.getText().toString())) {
                    validateCode(et_register_phone.getText().toString(), et_validate.getText().toString());
                } else {
                    AppUtil.showToast(getActivity(), "两次输入密码不一致");
                }
                break;
        }
    }

    /**
     * 发送验证码
     *
     * @param phone
     */
    private void sendVerificaCode(String phone) {
        BmobSMS.requestSMSCode(getActivity(), phone, "注册模板", new RequestSMSCodeListener() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {//验证码发送成功
                    Log.e("NewOrin", "短信id：" + smsId);//用于查询本次短信发送详情
                }
            }
        });
    }

    /**
     * 校验验证码
     */
    private void validateCode(final String phone, String code) {
        if (!TextUtils.isEmpty(code)) {
            Log.d("NewOrin", phone + "," + code);
            //开始请求后台校验验证码
            BmobSMS.verifySmsCode(getActivity(), phone, code, new VerifySMSCodeListener() {
                @Override
                public void done(BmobException ex) {
                    if (ex == null) {//短信验证码已验证成功
                        Log.d("NewOrin", "验证通过");
                        doRegister(et_register_password1.getText().toString(), phone);
                    } else {
                        AppUtil.showToast(getActivity(), "验证码错误");
                        Log.d("NewOrin", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                    }
                }
            });
        } else {
            AppUtil.showToast(getActivity(), "验证码不能为空");
        }
    }

    private void doRegister(String password, String phone) {
        BmobUser user = new BmobUser();
        user.setUsername(phone);
        user.setPassword(password);
        user.setMobilePhoneNumber(phone);
        user.signUp(getActivity(), new SaveListener() {
            @Override
            public void onSuccess() {
                AppUtil.showToast(getActivity(), "注册成功！");

            }

            @Override
            public void onFailure(int i, String s) {
                AppUtil.showToast(getActivity(), "注册失败" + s);
            }
        });
    }


    /**
     * 通过继承CountDownTimer类实现倒计时
     */
    private class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_validate.setText(millisUntilFinished / 1000 + "秒后重新发送");
            btn_validate.setClickable(false);
            btn_validate.setBackgroundColor(Color.parseColor("#B6B6D8"));
        }

        @Override
        public void onFinish() {
            btn_validate.setText("获取验证码");
            btn_validate.setClickable(true);
            btn_validate.setBackgroundColor(Color.parseColor("#808000"));
        }
    }

}
