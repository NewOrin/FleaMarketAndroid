package fleamarket.neworin.com.fleamarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import fleamarket.neworin.com.fleamarket.util.ViewHolder;

/**
 * 万能适配器
 * Created by NewOr on 2016/3/5.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    public Context mContext;
    public List<T> mDatas;
    public int mLayout;
    public LayoutInflater mInflater;

    public CommonAdapter(Context context, List<T> datas,int layout) {
        this.mContext = context;
        this.mDatas = datas;
        this.mLayout = layout;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext,convertView,parent,mLayout,position);
        convert(holder, getItem(position));
        return holder.getConvertView();
    }
    public abstract void convert(ViewHolder holder, T t);
}
