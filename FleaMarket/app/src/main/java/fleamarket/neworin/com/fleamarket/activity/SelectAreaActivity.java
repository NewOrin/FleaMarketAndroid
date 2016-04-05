package fleamarket.neworin.com.fleamarket.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.fragment.CityFragment;
import fleamarket.neworin.com.fleamarket.fragment.DistrictFragment;
import fleamarket.neworin.com.fleamarket.fragment.ProvinceFragment;
import fleamarket.neworin.com.fleamarket.util.FragmentInteraction;
import fleamarket.neworin.com.fleamarket.view.TopBar;

public class SelectAreaActivity extends AppCompatActivity implements FragmentInteraction {

    private TopBar select_area_topbar;
    private Fragment currentFragment;//存放当前的Fragment
    private FragmentManager manager;
    private FragmentTransaction transaction;
    String[] select_area;

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
        currentFragment = new ProvinceFragment();
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
        select_area = new String[3];
        manager = getFragmentManager();
        select_area_topbar = (TopBar) findViewById(R.id.select_area_topbar);
    }

    private void nextFragment(Fragment fragment) {
        transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_area_container, fragment);
        // 将该Frament添加到回退栈中
        transaction.addToBackStack("Province");
        transaction.commit();
    }

    /**
     * 接口回调，用于处理Fragment返回的数据
     *
     * @param level
     * @param name
     * @param parentId
     */
    @Override
    public void sendArgs(String level, String name, String parentId) {
        Bundle bundle;
        switch (Integer.parseInt(level)) {
            case 1:
                bundle = new Bundle();
                if (name.equals("北京市")) {
                    bundle.putString("parentId", "bf8d9fca-51fd-435d-a131-7fe975795e03");
                } else if (name.equals("上海市")) {
                    bundle.putString("parentId", "e1b307df-53d4-43c8-9798-a4094f253281");
                } else if (name.equals("重庆市")) {
                    bundle.putString("parentId", "5f99ec2c-1170-46f0-b682-c6b81fd6a5fb");
                } else if (name.equals("天津市")) {
                    bundle.putString("parentId", "953cf969-b8d4-4b39-9592-5080a07f6ae8");
                } else {
                    bundle.putString("parentId", parentId);
                }
                Log.d("NewOrin", "province:" + name + ", parentId:" + parentId);
                CityFragment cityFragment = new CityFragment();
                cityFragment.setArguments(bundle);
                nextFragment(cityFragment);
                select_area[0] = name;
                break;
            case 2:
                bundle = new Bundle();
                bundle.putString("parentId", parentId);
                Log.d("NewOrin", "province:" + name + ", parentId:" + parentId);
                DistrictFragment districtFragment = new DistrictFragment();
                districtFragment.setArguments(bundle);
                nextFragment(districtFragment);
                select_area[1] = name;
                break;
            case 3:
                select_area[2] = name;
                StringBuffer sb = new StringBuffer();
                for (String s : select_area) {
                    if (s != null) {
                        sb.append(s+" ");
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("address", sb.toString());
                setResult(AddEditAddressActivity.SELECTAREA_CODE, intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            Log.d("NewOrin", "entrycount===" + manager.getBackStackEntryCount());
            select_area[manager.getBackStackEntryCount()] = "";
            manager.popBackStack();
        }
        return super.onKeyDown(keyCode, event);
    }

}
