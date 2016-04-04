package fleamarket.neworin.com.fleamarket.util;

/**
 * Fragment与Activity通信接口
 * Created by NewOr on 2016/4/3.
 */
public interface FragmentInteraction {
    void sendArgs(String level, String name, String parentId);
}
