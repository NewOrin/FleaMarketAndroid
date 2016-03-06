package fleamarket.neworin.com.fleamarket.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.view.TopBar;

public class PublishActivity extends AppCompatActivity {

    private TopBar publish_topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initView();
        initEvent();
    }

    private void initEvent() {
        publish_topbar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
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
        publish_topbar = (TopBar) findViewById(R.id.publish_topbar);
        publish_topbar.setrightButtonVisible(false);
    }
}
