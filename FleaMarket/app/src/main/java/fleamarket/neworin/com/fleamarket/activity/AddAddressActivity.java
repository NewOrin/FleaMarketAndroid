package fleamarket.neworin.com.fleamarket.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.view.TopBar;

public class AddAddressActivity extends AppCompatActivity {

    private TopBar add_address_topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        initView();
        initEvent();
    }

    private void initEvent() {
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
        add_address_topbar = (TopBar) findViewById(R.id.add_address_topbar);
    }
}
