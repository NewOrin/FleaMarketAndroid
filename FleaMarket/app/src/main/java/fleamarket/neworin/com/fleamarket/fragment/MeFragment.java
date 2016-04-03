package fleamarket.neworin.com.fleamarket.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.activity.AccountActivity;
import fleamarket.neworin.com.fleamarket.adapter.CommonAdapter;
import fleamarket.neworin.com.fleamarket.util.ViewHolder;

public class MeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private RelativeLayout layout_avatar;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initEvent();
    }

    private void initEvent() {
        layout_avatar.setOnClickListener(this);
    }

    private void initView() {
        layout_avatar = (RelativeLayout) view.findViewById(R.id.layout_avatar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_avatar:
                startActivity(new Intent(getActivity(), AccountActivity.class));
                break;
        }
    }
}
