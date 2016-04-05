package fleamarket.neworin.com.fleamarket.net;

import android.content.Context;

import cn.bmob.v3.listener.DeleteListener;
import fleamarket.neworin.com.fleamarket.bean.Address;

/**
 * 处理Bmob后端云中的数据
 * Created by NewOr on 2016/4/5.
 */
public class BmobNetWork {

    /**
     * 从服务器删除Address对象
     * 注意：删除数据只能通过objectId来删除，目前不提供查询条件方式的删除方法。
     *
     * @param context
     * @param address
     * @param deleteListener
     */
    public static void doDeleteAddress(Context context, Address address, DeleteListener deleteListener) {
        address.delete(context, deleteListener);
    }
}
