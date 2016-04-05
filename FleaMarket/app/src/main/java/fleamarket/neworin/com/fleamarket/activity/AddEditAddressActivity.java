package fleamarket.neworin.com.fleamarket.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.bean.Address;
import fleamarket.neworin.com.fleamarket.bean.User;
import fleamarket.neworin.com.fleamarket.net.BmobNetWork;
import fleamarket.neworin.com.fleamarket.util.AppUtil;
import fleamarket.neworin.com.fleamarket.view.TopBar;

public class AddEditAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private TopBar add_address_topbar;
    private RelativeLayout layout_select_address, layout_delete_address;
    public static final int SELECTAREA_CODE = 1;
    private TextView tv_show_select_area;
    private EditText et_add_address_name, et_add_address_phone, et_add_address_detail;
    private String name = "";
    private String phone = "";
    private String detail = "";
    private String area = "";
    private int FLAG = 0;
    private Address mAddress;
    private int item_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_address);
        mAddress = (Address) getIntent().getSerializableExtra("address");
        if (mAddress != null) {
            item_position = getIntent().getExtras().getInt("position");
            FLAG = 1;
        } else mAddress = new Address();
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
                setData();
            }
        });
        layout_delete_address.setOnClickListener(this);
    }

    /**
     * 获取EditText中的信息
     */
    private void setData() {
        name = et_add_address_name.getText().toString();
        phone = et_add_address_phone.getText().toString();
        detail = et_add_address_detail.getText().toString();
        if (name.isEmpty()) AppUtil.showToast(this, "收货人不能为空!");
        else if (phone.isEmpty()) AppUtil.showToast(this, "联系电话不能为空!");
        else if (area.isEmpty()) AppUtil.showToast(this, "请选择所在地区!");
        else {
            mAddress.setReciever_name(name);
            mAddress.setPhone(phone);
            mAddress.setDetail_address(detail);
            mAddress.setArea_address(area);
            User u = BmobUser.getCurrentUser(this, User.class);
            mAddress.setUser(u);
            AppUtil.showProgressDialog(AddEditAddressActivity.this, "请稍后");
            if (FLAG == 0) saveAddress();
            else if (FLAG == 1) updateAddress();
        }
    }

    /**
     * 保存地址信息
     */
    private void saveAddress() {
        mAddress.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                AppUtil.showToast(AddEditAddressActivity.this, "添加成功!");
                AppUtil.closeProgressDialog();
                backData(DeliverAddressActivity.ADD_ADDRESS_CODE);
            }

            @Override
            public void onFailure(int i, String s) {
                AppUtil.showToast(AddEditAddressActivity.this, "添加失败!" + s);
                AppUtil.closeProgressDialog();
            }
        });

    }

    /**
     * 返回数据给DeliverAddressActivity
     */
    private void backData(int requestCode) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("address", mAddress);
        bundle.putInt("position", item_position);
        intent.putExtras(bundle);
        setResult(requestCode, intent);
        AddEditAddressActivity.this.finish();
    }

    /**
     * 更新地址
     */
    private void updateAddress() {
        mAddress.update(this, mAddress.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                AppUtil.showToast(AddEditAddressActivity.this, "更新地址成功!");
                AppUtil.closeProgressDialog();
                backData(DeliverAddressActivity.EDIT_ADDRESS_CODE);
            }

            @Override
            public void onFailure(int i, String s) {
                AppUtil.showToast(AddEditAddressActivity.this, "更新地址失败!" + s);
                AppUtil.closeProgressDialog();
            }
        });
    }

    private void initView() {
        layout_select_address = (RelativeLayout) findViewById(R.id.layout_select_address);
        add_address_topbar = (TopBar) findViewById(R.id.add_address_topbar);
        tv_show_select_area = (TextView) findViewById(R.id.tv_show_select_area);
        et_add_address_name = (EditText) findViewById(R.id.et_add_address_name);
        et_add_address_phone = (EditText) findViewById(R.id.et_add_address_phone);
        et_add_address_detail = (EditText) findViewById(R.id.et_add_address_detail);
        layout_delete_address = (RelativeLayout) findViewById(R.id.layout_delete_address);
        if (FLAG == 0) layout_delete_address.setVisibility(View.GONE);//若是添加地址，则隐藏删除
        /**
         * 若是编辑地址，则在EditText中显示相应内容
         */
        if (FLAG == 1) {
            add_address_topbar.setTitleText("编辑地址");
            layout_delete_address.setVisibility(View.VISIBLE);
            et_add_address_name.setText(mAddress.getReciever_name());
            et_add_address_phone.setText(mAddress.getPhone());
            et_add_address_detail.setText(mAddress.getDetail_address());
            area = mAddress.getArea_address();
            tv_show_select_area.setText(area);
            tv_show_select_area.setTextColor(getResources().getColor(R.color.black));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_select_address:
                startActivityForResult(new Intent(AddEditAddressActivity.this, SelectAreaActivity.class), SELECTAREA_CODE);
                break;
            case R.id.layout_delete_address:
                doDeleteAddress();
                break;
        }
    }

    /**
     * 删除地址操作
     */
    private void doDeleteAddress() {
        AppUtil.showAlertDialog(AddEditAddressActivity.this, "确认删除该地址?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BmobNetWork.doDeleteAddress(AddEditAddressActivity.this, mAddress, new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        AppUtil.showToast(AddEditAddressActivity.this, "地址删除成功!");
                        backData(DeliverAddressActivity.DELETE_ADDRESS_CODE);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        AppUtil.showToast(AddEditAddressActivity.this, "地址删除失败!" + s);
                    }
                });
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case SELECTAREA_CODE:
                    Bundle bundle = data.getExtras();
                    area = bundle.getString("address");
                    tv_show_select_area.setText(area);
                    tv_show_select_area.setTextColor(getResources().getColor(R.color.black));
                    break;
            }
        }
    }
}
