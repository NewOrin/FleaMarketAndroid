package fleamarket.neworin.com.fleamarket.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.adapter.CommonAdapter;
import fleamarket.neworin.com.fleamarket.bean.Address;
import fleamarket.neworin.com.fleamarket.net.BmobNetWork;
import fleamarket.neworin.com.fleamarket.util.AppUtil;
import fleamarket.neworin.com.fleamarket.util.ViewHolder;
import fleamarket.neworin.com.fleamarket.view.TopBar;

public class DeliverAddressActivity extends AppCompatActivity {

    private TopBar deliver_topbar;
    private ListView deliver_listview;
    private List<Address> data_list;
    public static final int ADD_ADDRESS_CODE = 1;
    public static final int EDIT_ADDRESS_CODE = 2;
    public static final int DELETE_ADDRESS_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_deliver_address);
        AppUtil.showProgressDialog(this, "请稍后...");
        initView();
        initEvent();
        getInternetData();
    }

    /**
     * 从网络获取数据
     */
    private void getInternetData() {
        BmobQuery<Address> query = new BmobQuery<>();
        query.findObjects(this, new FindListener<Address>() {
            @Override
            public void onSuccess(List<Address> list) {
                data_list = list;
                setListViewData();
            }

            @Override
            public void onError(int i, String s) {
                AppUtil.showToast(DeliverAddressActivity.this, "获取数据失败");
            }
        });
    }

    /**
     * 设置并显示ListView
     */
    private void setListViewData() {
        if (data_list != null && data_list.size() != 0) {
            deliver_listview.setAdapter(new CommonAdapter<Address>(this, data_list, R.layout.item_deliver_listview) {
                @Override
                public void convert(ViewHolder holder, Address address) {
                    holder.setText(R.id.tv_deliver_name, address.getReciever_name());
                    holder.setText(R.id.tv_deliver_phone, address.getPhone());
                    holder.setText(R.id.tv_deliver_address, (address.getArea_address() + address.getDetail_address()));
                }

                @Override
                public void itemClick(final ViewHolder holder, final Address address, final int item_position) {
                    holder.setTextClickListener(R.id.tv_delete_address, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppUtil.showAlertDialog(DeliverAddressActivity.this, "确认删除该地址?" + item_position, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    doDeleteAddress(address);
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                        }
                    });

                    holder.setTextClickListener(R.id.tv_edit_address, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DeliverAddressActivity.this, AddEditAddressActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("position", item_position);
                            bundle.putSerializable("address", address);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, EDIT_ADDRESS_CODE);
                        }
                    });
                }
            });
        }
        AppUtil.closeProgressDialog();
    }

    private void doDeleteAddress(final Address address) {
        AppUtil.showProgressDialog(DeliverAddressActivity.this, "请稍后");
        BmobNetWork.doDeleteAddress(this, address, new DeleteListener() {
            @Override
            public void onSuccess() {
                AppUtil.showToast(DeliverAddressActivity.this, "删除成功!");
                data_list.remove(address);
                setListViewData();
                AppUtil.closeProgressDialog();
            }

            @Override
            public void onFailure(int i, String s) {
                AppUtil.closeProgressDialog();
                AppUtil.showToast(DeliverAddressActivity.this, "删除失败!" + s);
            }
        });
    }

    private void initEvent() {
        deliver_topbar.setOnTopBarClickListener(new TopBar.topbarClickListener() {
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
        deliver_topbar = (TopBar) findViewById(R.id.deliver_topbar);
        deliver_listview = (ListView) findViewById(R.id.deliver_listview);
        data_list = new ArrayList<>();
    }

    public void addressDoClick(View v) {
        startActivityForResult(new Intent(this, AddEditAddressActivity.class), ADD_ADDRESS_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == ADD_ADDRESS_CODE) {
                Bundle bundle = data.getExtras();
                data_list.add((Address) bundle.getSerializable("address"));
                setListViewData();
            } else if (requestCode == EDIT_ADDRESS_CODE) {
                Bundle bundle = data.getExtras();
                if (resultCode == EDIT_ADDRESS_CODE) {
                    data_list.set(bundle.getInt("position"), (Address) bundle.getSerializable("address"));
                } else if (resultCode == DELETE_ADDRESS_CODE) {
                    data_list.remove(bundle.getInt("position"));
                }
                setListViewData();
            }
        }
    }
}
