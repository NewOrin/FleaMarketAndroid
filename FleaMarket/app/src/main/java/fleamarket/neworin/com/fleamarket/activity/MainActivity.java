package fleamarket.neworin.com.fleamarket.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;

import java.io.File;
import java.io.FileNotFoundException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;
import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.fragment.HomeFragment;
import fleamarket.neworin.com.fleamarket.fragment.MeFragment;
import fleamarket.neworin.com.fleamarket.fragment.PublishFragment;
import fleamarket.neworin.com.fleamarket.util.AppUtil;
import fleamarket.neworin.com.fleamarket.util.Constant;

public class MainActivity extends AppCompatActivity {

    private ImageView image;
    private final int RESULT = 1;
    Bitmap bitmap;
    private String photoPath;

    private Fragment currentFragment;//用于存放当前Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
        initView();
    }

    private void initFragment() {
        currentFragment = new HomeFragment();
        getFragmentManager().beginTransaction().add(R.id.main_fragment_container, currentFragment).commit();
    }

    private void initView() {

    }

    public void doTabClick(View v) {
        switch (v.getId()) {
            case R.id.tab_main:
                switchFragment(new HomeFragment());
                break;
            case R.id.tab_publish:
                switchFragment(new PublishFragment());
                break;
            case R.id.tab_me:
                switchFragment(new MeFragment());
                break;
        }
    }

    /**
     * 切换Fragment
     *
     * @param fragment
     */
    private void switchFragment(Fragment fragment) {
        if (currentFragment != fragment) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!fragment.isAdded()) {
                transaction.hide(currentFragment).replace(R.id.main_fragment_container, fragment).commit();
            } else {
                transaction.hide(currentFragment).show(fragment).commit();
            }
            currentFragment = fragment;
        }
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
}
