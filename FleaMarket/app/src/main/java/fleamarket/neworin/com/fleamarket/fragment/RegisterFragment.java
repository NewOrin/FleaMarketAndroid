package fleamarket.neworin.com.fleamarket.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.util.AppUtil;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button btn_register, btn_validate;
    private Handler handler = new Handler();
    private int seconds = 5;

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

    }

    private void initEvent() {
        btn_validate.setOnClickListener(this);
    }

    private void initView() {
        btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_validate = (Button) view.findViewById(R.id.btn_validate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_validate:
                AppUtil.showToast(getActivity(), "验证码已发送到手机");
                handler.postAtTime(thread, 1000);
                break;
        }
    }

    Thread thread = new Thread() {
        @Override
        public void run() {
            while (!thread.isInterrupted()) {
                showCountSeconds();
                handler.postDelayed(this, 1000);
            }
        }
    };

    /**
     * 显示获取验证码计数器
     */
    private void showCountSeconds() {
        if (seconds >= 1) {
            btn_validate.setClickable(false);
            btn_validate.setText(seconds + "秒后再次获取");
            seconds--;
        } else {
            thread.interrupt();
            seconds = 5;
            btn_validate.setText("获取验证码");
            btn_validate.setClickable(true);
        }
    }
}
