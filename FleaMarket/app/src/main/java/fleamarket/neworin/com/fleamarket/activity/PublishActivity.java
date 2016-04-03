package fleamarket.neworin.com.fleamarket.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.adapter.MyGridViewAdapter;
import fleamarket.neworin.com.fleamarket.bean.Post;
import fleamarket.neworin.com.fleamarket.bean.User;
import fleamarket.neworin.com.fleamarket.util.AppUtil;
import fleamarket.neworin.com.fleamarket.view.TopBar;

public class PublishActivity extends Activity {

    private TopBar publish_topbar;
    private GridView publish_gridview;
    private MyGridViewAdapter adapter;
    private TextView tv_show_location, tv_show_price, tv_show_orig_price;
    private LinearLayout layout_input_price;
    private EditText et_sell_price, et_orig_price, et_publish_title, et_publish_detail;
    private RadioButton rb_mail, rb_no_mail;
    private RadioGroup rg_if_mail;
    private Button btn_publish;

    private final int IMAGE_OPEN = 1;//打开图片标记
    private String pathImage;//选择图片路径
    private String ifMail = "不包邮";//是否包邮,默认不包邮
    private String sell_price, title, desc;
    private String orig_price = "";
    private ArrayList<Bitmap> bmp_list;
    private List<BmobFile> image_list = new ArrayList<>();//帖子图片集合
    private List<String> path_list = new ArrayList<>();//存放图片地址
    private List<String> image_urls;//存放上传图片的url

    /**
     * 高德地图
     */
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

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

    /**
     * 初始化监听
     */
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
                    showDeleteImageDialog(position);
                }
                return true;
            }
        });
        layout_input_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputPriceDialog();
            }
        });
        rg_if_mail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rb_mail.getId()) {
                    ifMail = "包邮";
                } else {
                    ifMail = "不包邮";
                }
            }
        });
        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title = et_publish_title.getText().toString();
                desc = et_publish_detail.getText().toString();
                if (title.isEmpty()) {
                    AppUtil.showToast(PublishActivity.this, "标题不能为空");
                } else if (desc.isEmpty()) {
                    AppUtil.showToast(PublishActivity.this, "闲置描述不能为空");
                } else if (sell_price == null) {
                    AppUtil.showToast(PublishActivity.this, "请填写售价");
                } else if (path_list.size() != 0) {
                    uploadImages();
                } else {
                    uploadPost();
                }
            }
        });
        tv_show_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputLocationDialog();
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
//            Log.d("NewOrin", "取消了");
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
//                Log.d("NewOrin", "没有找到图片");
                return;
            }
            //光标移动至开头，获取图片路径
            cursor.moveToFirst();
            pathImage = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            Log.d("NewOrin", "图片路径为:" + pathImage);
            path_list.add(pathImage);
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
//            Log.d("NewOrin", "pathImage为空");
        }
    }

    /**
     * 初始化
     */
    private void initView() {
        bmp_list = new ArrayList<>();
        bmp_list.add(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_add));

        tv_show_location = (TextView) findViewById(R.id.tv_show_location);
        tv_show_price = (TextView) findViewById(R.id.tv_show_price);
        tv_show_orig_price = (TextView) findViewById(R.id.tv_show_orig_price);
        layout_input_price = (LinearLayout) findViewById(R.id.layout_input_price);
        publish_topbar = (TopBar) findViewById(R.id.publish_topbar);
        rb_mail = (RadioButton) findViewById(R.id.rb_mail);
        rb_no_mail = (RadioButton) findViewById(R.id.rb_no_mail);
        rg_if_mail = (RadioGroup) findViewById(R.id.rg_if_mail);
        btn_publish = (Button) findViewById(R.id.btn_publish);
        et_publish_title = (EditText) findViewById(R.id.et_publish_title);
        et_publish_detail = (EditText) findViewById(R.id.et_publish_detail);

        publish_topbar.setrightButtonVisible(false);
        publish_gridview = (GridView) findViewById(R.id.publish_gridview);
        adapter = new MyGridViewAdapter(this, bmp_list);
        publish_gridview.setAdapter(adapter);

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        setAmapParams();
        //设置定位回调监听
        /**
         * 高德地图的定位回调监听实现
         */
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        /**
                         //定位成功回调信息，设置相关消息
                         amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                         amapLocation.getLatitude();//获取纬度
                         amapLocation.getLongitude();//获取经度
                         amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                         amapLocation.getCountry();//国家信息
                         amapLocation.getProvince();//省信息
                         amapLocation.getCity();//城市信息
                         amapLocation.getDistrict();//城区信息
                         amapLocation.getStreet();//街道信息
                         amapLocation.getStreetNum();//街道门牌号信息
                         amapLocation.getCityCode();//城市编码
                         amapLocation.getAdCode();//地区编码
                         */
                        Log.d("NewOrin", "定位结果来源:" + amapLocation.getLocationType() + "\n" + "纬度" + amapLocation.getLatitude() + "\n" + "经度" + amapLocation.getLongitude());
                        tv_show_location.setText(amapLocation.getAddress());
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("NewOrin", "定位 Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                        tv_show_location.setText("未能获取定位信息，请点击手动输入");
                        tv_show_location.setTextColor(getResources().getColor(R.color.red));
                    }
                }
            }
        };
        mLocationClient.setLocationListener(mLocationListener);
    }

    /**
     * 配置高德地图定位参数，启动定位
     */
    private void setAmapParams() {
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(true);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(20000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 确认删除照片对话框
     *
     * @param position
     */
    private void showDeleteImageDialog(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("确认").setIcon(R.mipmap.ic_launcher).setMessage("确认删除该图片？").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bmp_list.remove(position);
                path_list.remove(position);
                adapter.notifyDataSetChanged();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 显示输入价格对话框
     */
    private void showInputPriceDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_input_price, null);
        et_orig_price = (EditText) view.findViewById(R.id.et_orig_price);
        et_sell_price = (EditText) view.findViewById(R.id.et_sell_price);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sell_price = et_sell_price.getText().toString();
                orig_price = et_orig_price.getText().toString();
                tv_show_price.setText("￥" + sell_price);
                tv_show_orig_price.setText("￥" + orig_price);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();//获取dialog
        dialog.show(); //显示dialog
    }

    /**
     * 进行上传操作，先上传图片(若有)，再将帖子信息上传
     */
    private void uploadPost() {
        User u = BmobUser.getCurrentUser(this, User.class);
        Post post = new Post();
        if (image_urls != null) {
            post.setImage_urls(image_urls);
        }
        post.setAuthor(u);
        post.setTitle(title);
        post.setDesc(desc);
        post.setIfMail(ifMail);
        post.setSell_price(sell_price);
        post.setOrig_price(orig_price);
        post.setFavour_count("0");
        post.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.d("NewOrin", "帖子上传成功!");
                AppUtil.showToast(PublishActivity.this, "发布成功!");
                PublishActivity.this.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                AppUtil.showToast(PublishActivity.this, "发布失败:code=" + i + ":" + s);
            }
        });
    }

    /**
     * 上传图片
     */
    private void uploadImages() {
        final String[] imagePaths = (String[]) path_list.toArray(new String[image_list.size()]);
        for (int i = 0; i < imagePaths.length; i++) {
            Log.d("NewOrin", "总共上传的图片:" + imagePaths[i]);
        }
        Bmob.uploadBatch(PublishActivity.this, imagePaths, new cn.bmob.v3.listener.UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的服务器地址
                if (urls.size() == imagePaths.length) {
                    Log.d("NewOrin", "上传成功!");
                    image_urls = urls;
                    uploadPost();
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {
            }

            @Override
            public void onError(int i, String s) {
                Log.d("NewOrin", "图片上传失败!");
                image_urls = null;
            }
        });
    }

    /**
     * 手动设置位置对话框
     */
    private void showInputLocationDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_input_location, null);
        final EditText et_input_location = (EditText) view.findViewById(R.id.et_input_location);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!et_input_location.getText().toString().trim().equals("")) {
                    tv_show_location.setText(et_input_location.getText().toString());
                    tv_show_location.setTextColor(getResources().getColor(R.color.black));
                    dialog.dismiss();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Activity销毁方法
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();//停止定位
        mLocationClient.onDestroy();//销毁定位客户端
    }
}
