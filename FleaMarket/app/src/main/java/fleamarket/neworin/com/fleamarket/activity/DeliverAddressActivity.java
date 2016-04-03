package fleamarket.neworin.com.fleamarket.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import fleamarket.neworin.com.fleamarket.R;

public class DeliverAddressActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_deliver_address);
        initView();
        initEvent();
    }

    private void initEvent() {
    }

    private void initView() {

    }

    public void addressDoClick(View v) {
        startActivity(new Intent(this, AddAddressActivity.class));
    }

}
