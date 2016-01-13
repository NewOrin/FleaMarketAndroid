package fleamarket.neworin.com.fleamarket.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.fragment.LoginFragment;
import fleamarket.neworin.com.fleamarket.fragment.RegisterFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, new RegisterFragment());
        transaction.commit();
        initView();
        initEvent();

    }

    private void initEvent() {
    }

    private void initView() {
    }


    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                switchFragment(R.id.layout_login, new RegisterFragment());
                break;
            case R.id.btn_login:
                Toast.makeText(MainActivity.this, "Work", Toast.LENGTH_SHORT).show();
                Log.d("NewOrin", "Work");
                break;
        }
    }

    private void switchFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewId, fragment).commit();
    }
}
