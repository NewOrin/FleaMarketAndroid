package fleamarket.neworin.com.fleamarket.activity;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.fragment.FragmentProvince;
import fleamarket.neworin.com.fleamarket.view.TopBar;

public class SelectAreaActivity extends AppCompatActivity {

    private TopBar select_area_topbar;
    private Fragment currentFragment;//存放当前的Fragment
    private Fragment getCurrentFragment;//用于比较当前Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_area);
        initFragment();
        initView();
        initEvent();
    }

    private void initFragment() {
        currentFragment = new FragmentProvince();
        getFragmentManager().beginTransaction().add(R.id.fragment_area_container, currentFragment).commit();
    }

    private void initEvent() {
        select_area_topbar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {

            }

            @Override
            public void rightClick() {

            }
        });
    }

    private void initView() {
        select_area_topbar = (TopBar) findViewById(R.id.select_area_topbar);
    }
}
