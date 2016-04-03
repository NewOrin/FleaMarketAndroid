package fleamarket.neworin.com.fleamarket.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fleamarket.neworin.com.fleamarket.R;

public class DeliverAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_address);
    }

    public void addressDoClick(View v) {
        startActivity(new Intent(this, AddAddressActivity.class));
    }
}
