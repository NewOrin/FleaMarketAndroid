package fleamarket.neworin.com.fleamarket.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.adapter.MyGridViewAdapter;
import fleamarket.neworin.com.fleamarket.util.AppUtil;
import fleamarket.neworin.com.fleamarket.view.TopBar;

public class PublishActivity extends Activity {

    private TopBar publish_topbar;
    private GridView publish_gridview;
    private final int IMAGE_OPEN = 1;//打开图片标记
    private String pathImage;//选择图片路径
    private ArrayList<Bitmap> bmp_list;
    private MyGridViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /**
         * 防止键盘挡住输入框
         * 不希望遮挡设置activity属性 android:windowSoftInputMode = "adjustPan"
         * 希望动态调整高度 android:windowSoftInputMode = "adjustResize"
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        publish_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == bmp_list.size() - 1) {
                    AppUtil.showToast(PublishActivity.this, "添加图片");
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGE_OPEN);
                }
            }
        });

        publish_gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != bmp_list.size() - 1) {
                    bmp_list.remove(position);
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
        });
    }

    /**
     * 选择图片
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Log.d("NewOrin", "取消了");
            return;
        } else if (requestCode == IMAGE_OPEN) {
            getPhotoUrl(data);
        }
    }

    /**
     * 获取图片路径
     *
     * @param data
     */
    private void getPhotoUrl(Intent data) {
        Uri uri = data.getData();
        if (!TextUtils.isEmpty(uri.getAuthority())) {
            //查询选择图片
            Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            //返回没有找到图片
            if (null == cursor) {
                Log.d("NewOrin", "没有找到图片");
                return;
            }
            //光标移动至开头，获取图片路径
            cursor.moveToFirst();
            pathImage = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            Log.d("NewOrin", "图片路径为:" + pathImage);
        }
    }

    /**
     * 刷新并显示图片
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(pathImage)) {
            Bitmap addbmp = BitmapFactory.decodeFile(pathImage);
            bmp_list.add(0, addbmp);
            adapter = new MyGridViewAdapter(this, bmp_list);
            publish_gridview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            pathImage = null;
        } else {
            Log.d("NewOrin", "pathImage为空");
        }
    }

    private void initView() {
        bmp_list = new ArrayList<>();
        bmp_list.add(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_add));
        publish_topbar = (TopBar) findViewById(R.id.publish_topbar);
        publish_topbar.setrightButtonVisible(false);
        publish_gridview = (GridView) findViewById(R.id.publish_gridview);
        adapter = new MyGridViewAdapter(this, bmp_list);
        publish_gridview.setAdapter(adapter);
    }
}
