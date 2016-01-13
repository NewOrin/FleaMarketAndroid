package fleamarket.neworin.com.fleamarket.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fleamarket.neworin.com.fleamarket.R;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button btn_register;

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
        btn_register.setOnClickListener(this);
    }

    private void initView() {
        btn_register = (Button) view.findViewById(R.id.btn_register);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                break;
        }
    }
}
