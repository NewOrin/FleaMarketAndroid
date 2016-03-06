package fleamarket.neworin.com.fleamarket.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import fleamarket.neworin.com.fleamarket.R;

/**
 * GridView适配器
 * Created by NewOr on 2016/3/6.
 */
public class MyGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<Bitmap> image_list;
    private LayoutInflater mInflater;

    public MyGridViewAdapter(Context context, List<Bitmap> image_list) {
        this.mContext = context;
        this.image_list = image_list;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return image_list.size();
    }

    @Override
    public Object getItem(int position) {
        return image_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.gridview_item, null);
            holder.image_show = (ImageView) convertView.findViewById(R.id.publish_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.image_show.setImageBitmap(image_list.get(position));
        return convertView;
    }

    private class ViewHolder {
        ImageView image_show;
    }
}
