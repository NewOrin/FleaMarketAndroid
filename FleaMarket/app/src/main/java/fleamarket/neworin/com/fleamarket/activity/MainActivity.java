package fleamarket.neworin.com.fleamarket.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.bmob.btp.callback.UploadListener;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.fragment.FocusFragment;
import fleamarket.neworin.com.fleamarket.fragment.HomeFragment;
import fleamarket.neworin.com.fleamarket.fragment.MeFragment;
import fleamarket.neworin.com.fleamarket.fragment.MessageFragment;
import fleamarket.neworin.com.fleamarket.util.AppUtil;
import fleamarket.neworin.com.fleamarket.util.Constant;
import fleamarket.neworin.com.fleamarket.util.DataBaseHelper;
import fleamarket.neworin.com.fleamarket.util.SharedPreferencesHelper;
import fleamarket.neworin.com.fleamarket.view.TopBar;

public class MainActivity extends AppCompatActivity {

    private ImageView image;
    private final int RESULT = 1;
    Bitmap bitmap;
    private String photoPath;

    private FragmentPagerAdapter mAdapter;
    private List<android.support.v4.app.Fragment> mFragments;
    private ViewPager fragment_viewpager;
    private TopBar myTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        initView();
        intEvent();
        setSelect(0);
    }

    private void intEvent() {
        myTopBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
//                AppUtil.showToast(MainActivity.this, "搜索");
            }

            @Override
            public void rightClick() {
                BmobUser user = BmobUser.getCurrentUser(MainActivity.this);
                if (user != null)
                    startActivity(new Intent(MainActivity.this, PublishActivity.class));
                else {
                    AppUtil.showToast(MainActivity.this, "请先登录");
                    startActivity(new Intent(MainActivity.this, FirstActivity.class));
                }
            }
        });
    }

    private void initView() {
        myTopBar = (TopBar) findViewById(R.id.main_topbar);
        fragment_viewpager = (ViewPager) findViewById(R.id.fragment_viewpager);
        fragment_viewpager.setOffscreenPageLimit(3);//设置缓存页数
        mFragments = new ArrayList<>();

        mFragments.add(new HomeFragment());
        mFragments.add(new FocusFragment());
        mFragments.add(new MessageFragment());
        mFragments.add(new MeFragment());
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        fragment_viewpager.setAdapter(mAdapter);
        fragment_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int currentItem = fragment_viewpager.getCurrentItem();
                switch (currentItem) {
                    case 0:
                        myTopBar.setTitleText("主页");
                        break;
                    case 1:
                        myTopBar.setTitleText("关注");
                        break;
                    case 2:
                        myTopBar.setTitleText("消息");
                        break;
                    case 3:
                        myTopBar.setTitleText("我的");
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void doTabClick(View v) {
        switch (v.getId()) {
            case R.id.tab_main:
                setSelect(0);
                myTopBar.setTitleText("主页");

                break;
            case R.id.tab_focus:
                setSelect(1);
                myTopBar.setTitleText("关注");

                break;
            case R.id.tab_message:
                setSelect(2);
                myTopBar.setTitleText("消息");

                break;
            case R.id.tab_me:
                setSelect(3);
                myTopBar.setTitleText("我的");
                break;
        }
    }

    private void setSelect(int i) {
        fragment_viewpager.setCurrentItem(i);
    }

    /**
     * 从图库中获取图片
     */
    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT);
    }

    /**
     * 重写该方法用于获得重要信息
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Log.d("NewOrin", "Error");
            return;
        }
        if (resultCode == RESULT_OK) {
//            showPhoto1(data);
            showPhoto2(data);
        }
    }

    /**
     * 处理图片方法1，
     * 直接处理返回的图片是被系统压缩过的，不过自己在测试的过程并没有区别；
     * 如果用户不断的重新获取图片的话，必须把现在的Bmp内存释放，否则会报错！ bmp.recycle()。
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void showPhoto1(Intent data) {
        //选择图片
        Uri uri = data.getData();
        ContentResolver cr = this.getContentResolver();
        if (bitmap != null) {
            bitmap.recycle();
        }
        try {
            bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        image.setImageBitmap(bitmap);
    }

    /**
     * 处理图片，方法二，获得图片的地址再处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void showPhoto2(Intent data) {
        Uri uri = data.getData();
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        photoPath = cursor.getString(column_index);
        bitmap = BitmapFactory.decodeFile(photoPath);
        Log.d("NewOrin", "图片路径是:" + photoPath);
        image.setImageBitmap(bitmap);
    }

    /**
     * 上传图片
     */
    private void uploadPhoto() {
        if (photoPath.equals("")) {
            Log.d("NewOrin", "photoPath为空");
            return;
        }

        BTPFileResponse response = BmobProFile.getInstance(this).upload(photoPath, new UploadListener() {
            @Override
            public void onSuccess(String fileName, String url, BmobFile bmobFile) {
                Log.d("NewOrin", "文件上传成功：" + fileName + ",可访问的文件地址：" + bmobFile.getUrl());
                Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(int progress) {
                Log.d("NewOrin", "onProgress :" + progress);
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                Log.d("NewOrin", "文件上传失败：" + errormsg);
                Toast.makeText(MainActivity.this, "文件上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean isAutoLogin() {
        SharedPreferencesHelper sph = new SharedPreferencesHelper(this, "userinfo");
        if (sph.getBooleanValue(Constant.IS_AUTO_LOGIN))
            return true;
        else
            return false;
    }

}
