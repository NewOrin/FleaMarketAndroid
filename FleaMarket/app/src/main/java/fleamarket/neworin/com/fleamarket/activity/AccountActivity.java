package fleamarket.neworin.com.fleamarket.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.bean.User;
import fleamarket.neworin.com.fleamarket.util.AppUtil;
import fleamarket.neworin.com.fleamarket.util.Constant;
import fleamarket.neworin.com.fleamarket.util.DataBaseHelper;
import fleamarket.neworin.com.fleamarket.util.ImageTools;
import fleamarket.neworin.com.fleamarket.util.MyDataBase;
import fleamarket.neworin.com.fleamarket.util.SharedPreferencesHelper;
import fleamarket.neworin.com.fleamarket.view.CircleImageView;
import fleamarket.neworin.com.fleamarket.view.TopBar;

public class AccountActivity extends AppCompatActivity {

    private TopBar topBar;
    private CircleImageView account_avatar;
    private final int ALBUM_IMAGE_OPEN = 0;//从相册打开图片标记
    private final int CAMERA_IMAGE_OPEN = 1;//打开相机标记
    public static final int SET_NICKNAME_ACTIVITY_BACK = 3;
    private String pathImage;
    private static final int SCALE = 5;//照片缩小比例
    private static final int SIZE = 3000000;//图片长宽乘积
    private TextView tv_show_gender, tv_show_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_account);
        initView();
        initEvent();
    }

    private void initEvent() {
        topBar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
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
        topBar = (TopBar) findViewById(R.id.account_topbar);
        account_avatar = (CircleImageView) findViewById(R.id.account_avatar);
        tv_show_gender = (TextView) findViewById(R.id.tv_show_gender);
        tv_show_nickname = (TextView) findViewById(R.id.tv_show_nickname);
    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void accountDoClick(View view) {
        switch (view.getId()) {
            case R.id.layout_set_avatar:
                showChooseAvatarRes();
                break;
            case R.id.layout_set_nickname:
                Intent intent = new Intent(AccountActivity.this, AlterNicknameActivity.class);
                intent.putExtra("title", "修改昵称");
                startActivityForResult(intent, SET_NICKNAME_ACTIVITY_BACK);
                break;
            case R.id.layout_set_sex:
                showChooseGenderDialog();
                break;
            case R.id.layout_set_password:
                showValidatePwdDialog();
                break;
            case R.id.layout_set_address:
                startActivity(new Intent(AccountActivity.this, DeliverAddressActivity.class));
                break;
        }
    }

    /**
     * 显示验证密码对话框
     */
    private void showValidatePwdDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.show_edittext, null);
        final EditText et_password = (EditText) view.findViewById(R.id.et_dialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请验证当前密码");
        builder.setView(view);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferencesHelper helper = new SharedPreferencesHelper(AccountActivity.this, "userinfo");
                if (et_password.getText().toString().equals(helper.getStringValue("password")))
                    startActivity(new Intent(AccountActivity.this, AlterPasswordActivity.class));
                else
                    Toast.makeText(AccountActivity.this, "密码错误!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 显示选择性别对话框
     */
    private void showChooseGenderDialog() {
        String[] choose_list = new String[]{"保密", "男", "女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
        builder.setTitle("选择性别");
        builder.setSingleChoiceItems(choose_list, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        tv_show_gender.setText("保密");
                        dialog.dismiss();
                        break;
                    case 1:
                        tv_show_gender.setText("男");
                        dialog.dismiss();
                        break;
                    case 2:
                        tv_show_gender.setText("女");
                        dialog.dismiss();
                        break;
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 显示选择头像对话框
     */
    private void showChooseAvatarRes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
        builder.setTitle("图片来源");
        builder.setItems(new String[]{"打开相机", "打开相册"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 1) {
                    Intent getFromAlbum = new Intent(Intent.ACTION_PICK);
                    getFromAlbum.setType("image/*");
                    startActivityForResult(getFromAlbum, ALBUM_IMAGE_OPEN);
                } else {
                    Intent getFromCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                    Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
                    getFromCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(getFromCamera, CAMERA_IMAGE_OPEN);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case ALBUM_IMAGE_OPEN:
                    setImage(data, requestCode);
                    break;
                case CAMERA_IMAGE_OPEN:
                    setImage(data, requestCode);
                    break;
                case SET_NICKNAME_ACTIVITY_BACK:
                    Bundle bundle = data.getExtras();
                    Log.d("NewOrin", bundle.getString("nickname"));
                    tv_show_nickname.setText(bundle.getString("nickname"));
                    break;
            }
        }
    }

    /**
     * 处理从相册获取图片并设置ImageView
     *
     * @param data
     */
    private void setImage(Intent data, int requestCode) {
        int width, height;
        //相册获取图片
        if (requestCode == ALBUM_IMAGE_OPEN) {
            ContentResolver resolver = getContentResolver();
            //照片的原始资源地址
            Uri originalUri = data.getData();
            //使用ContentResolver通过uri获取原始图片
            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                if (photo != null) {
                    width = photo.getWidth();
                    height = photo.getHeight();
                    //如果图片超过设定值，则需要压缩
                    if ((width * height) > SIZE) {
                        Log.d("NewOrin", "需要压缩");
                        //为防止原始图片过大，先缩小原图显示，然后再释放原始Bitmap占用的内存
                        Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
                        //释放原始图片占用的内存，防止out of memory
                        account_avatar.setImageBitmap(smallBitmap);
                        photo.recycle();
                    } else {
                        findImageByUri(originalUri);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } //打开相机获取照片
        if (requestCode == CAMERA_IMAGE_OPEN) {
            //将保存在本地的图片取出并缩小后显示在界面上
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpg");
            Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
            //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
            bitmap.recycle();

            //将处理过的图片显示在界面上并保存到本地
            account_avatar.setImageBitmap(newBitmap);
            ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), String.valueOf(System.currentTimeMillis()));
        }
    }

    public void saveImage(Bitmap bitmap, String spath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(spath, false));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 根据uri获取图片
     *
     * @param uri
     */
    private void findImageByUri(Uri uri) {
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
            Log.d("NewOrin", "AccountActivity图片地址" + pathImage);
            uploadImage(pathImage);
            account_avatar.setImageBitmap(BitmapFactory.decodeFile(pathImage));
        }
    }

    /**
     * 上传用户头像
     *
     * @param picPath
     */
    private void uploadImage(String picPath) {
        AppUtil.showProgressDialog(this, "正在上传头像...");
        if (picPath != null) {
            final BmobFile bmobFile = new BmobFile(new File(picPath));
            bmobFile.uploadblock(this, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    AppUtil.showToast(AccountActivity.this, "头像上传成功!");
                    savePicPathToDB(bmobFile.getFileUrl(AccountActivity.this));
                }

                @Override
                public void onFailure(int i, String s) {
                    AppUtil.showToast(AccountActivity.this, "头像上传失败!" + s);
                }
            });

        }
    }

    private void savePicPathToDB(String picPath) {
        DataBaseHelper helper = new DataBaseHelper(this);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        MyDataBase myDataBase = new MyDataBase(helper, sqLiteDatabase, this);
        String currentUser = BmobUser.getCurrentUser(this, User.class).getUsername();
        myDataBase.doUpdateDB(Constant.TABLE_USER, Constant.AVATAR_URL, picPath, Constant.USER_NAME + "=?", new String[]{currentUser});
        User user = new User();
        user.setAvatar_url(picPath);
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this, "userinfo");
        String objectId = sharedPreferencesHelper.getStringValue(Constant.OBJECT_ID);
        user.update(this, objectId, new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.d("NewOrin", "服务器头像地址更新成功!");
            }
             @Override
            public void onFailure(int i, String s) {
                Log.d("NewOrin", "服务器头像地址更新失败!" + s);
                AppUtil.showToast(AccountActivity.this, "服务器头像地址更新失败!" + s);
             }
        });
        AppUtil.closeProgressDialog();
    }
}
