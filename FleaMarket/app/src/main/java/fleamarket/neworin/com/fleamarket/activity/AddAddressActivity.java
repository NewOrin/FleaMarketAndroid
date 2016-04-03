package fleamarket.neworin.com.fleamarket.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.net.NetWorkUtil;
import fleamarket.neworin.com.fleamarket.util.AnalyticJson;
import fleamarket.neworin.com.fleamarket.view.TopBar;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private TopBar add_address_topbar;
    private RelativeLayout layout_select_address;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_select_address:
                NetWorkUtil netWorkUtil = new NetWorkUtil(AddAddressActivity.this);
                netWorkUtil.getAreaInfo(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("NewOrin", "查询行政返回数据:" + jsonObject.toString());
                        AnalyticJson.analyticAreaJson(jsonObject);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }, "e484841f-27e9-4ec9-96d6-d8f8855caec1");

                break;
        }
    }
}
