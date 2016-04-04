package fleamarket.neworin.com.fleamarket.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.view.TopBar;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private TopBar add_address_topbar;
    private RelativeLayout layout_select_address;
    public static final int SELECTAREA_CODE = 1;
    private TextView tv_show_select_area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_address);
        initView();
        initEvent();
    }

    private void initEvent() {
        layout_select_address.setOnClickListener(this);
        add_address_topbar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
    }

    private void initView() {
        layout_select_address = (RelativeLayout) findViewById(R.id.layout_select_address);
        add_address_topbar = (TopBar) findViewById(R.id.add_address_topbar);
        tv_show_select_area = (TextView) findViewById(R.id.tv_show_select_area);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_select_address:
                startActivityForResult(new Intent(AddAddressActivity.this, SelectAreaActivity.class), SELECTAREA_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case SELECTAREA_CODE:
                    Bundle bundle = data.getExtras();
                    tv_show_select_area.setText(bundle.getString("address"));
                    tv_show_select_area.setTextColor(getResources().getColor(R.color.black));
                    break;
            }
        }
    }
}
