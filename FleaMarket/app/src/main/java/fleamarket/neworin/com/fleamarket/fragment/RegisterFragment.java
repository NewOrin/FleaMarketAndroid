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

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
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
        SMSSDK.initSDK(getActivity(), Constant.APP_KEY, Constant.APP_SECRET);//初始化SMSSDK

        //事件调用监听类
        EventHandler eventHandler = new EventHandler() {
            //事件执行后调用
            @Override
            public void afterEvent(int event, int result, Object data) {
                super.afterEvent(event, result, data);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    Log.d("NewOrin", result + "验证成功!");
                }
            }
        };
        //注册回调接口
        SMSSDK.registerEventHandler(eventHandler);
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
                getStrings();
                validateCode(et_register_phone.getText().toString(), et_validate.getText().toString());
                break;
        }
    }

    /**
     * 发送验证码
     *
     * @param phone
     */
    private void sendVerificaCode(String phone) {
        SMSSDK.getVerificationCode("86", phone);
    }

    /**
     * 校验验证码
     */
    private void validateCode(String phone, String code) {
        if (!TextUtils.isEmpty(code)) {
            Log.d("NewOrin", phone + "," + code);
            SMSSDK.submitVerificationCode("86", phone, code);
        } else {
            AppUtil.showToast(getActivity(), "验证码不能为空");
        }
    }

    /**
     * 获取文本框内字符串
     */
    private void getStrings() {
        password = et_validate.getText().toString();
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
