package fleamarket.neworin.com.fleamarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.bean.Post;
import fleamarket.neworin.com.fleamarket.net.ImageLoader;
import fleamarket.neworin.com.fleamarket.util.AppUtil;

/**
 * 主页Fragment中ListView的Adapter
 * Created by NewOr on 2016/3/22.
 */
public class HomeListViewAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    private List<Post> postBeanList;
    private Context context;
    private int mStart, mEnd;
    private ImageLoader mImageLoader;
    public static String[] URLS;// 用来保存当前获得的所有图片的URL地址
    private boolean mFirstIn;
    private ViewHolder holder = null;

    public HomeListViewAdapter(List<Post> postBeanList, Context context, ListView listView) {
        mFirstIn = true;
        this.postBeanList = postBeanList;
        this.context = context;
        mImageLoader = new ImageLoader(listView);
        URLS = new String[postBeanList.size()];
        for (int i = 0; i < postBeanList.size(); i++) {
            URLS[i] = postBeanList.get(i).getImage_urls().get(0);
        }
        listView.setOnScrollListener(this);
    }

    public void notifyData() {
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return postBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return postBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_listview, null);
            holder.item_tv_userame = (TextView) convertView.findViewById(R.id.item_tv_userame);
            holder.item_tv_desc = (TextView) convertView.findViewById(R.id.item_tv_desc);
            holder.item_tv_publish_time = (TextView) convertView.findViewById(R.id.item_tv_publish_time);
            holder.item_tv_price = (TextView) convertView.findViewById(R.id.item_tv_price);
            holder.item_img = (ImageView) convertView.findViewById(R.id.item_img);
            holder.layout_favour = (LinearLayout) convertView.findViewById(R.id.layout_favour);
            holder.tv_show_favour_count = (TextView) convertView.findViewById(R.id.tv_show_favour_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_img.setImageResource(R.mipmap.ic_launcher);
        String url = postBeanList.get(position).getImage_urls().get(0);
        //给图片设置Tag
        holder.item_img.setTag(url);
        //使用AsyncTask方式加载图片
        mImageLoader.showImageByAsyncTask(holder.item_img, url);
        holder.item_tv_userame.setText(postBeanList.get(position).getAuthor().getUsername());
        holder.item_tv_publish_time.setText(postBeanList.get(position).getCreatedAt());
        holder.item_tv_price.setText(" ￥ " + postBeanList.get(position).getSell_price());
        holder.item_tv_desc.setText("    " + postBeanList.get(position).getDesc().trim());
        if (postBeanList.get(position).getFavour_count().equals("")) {
            holder.tv_show_favour_count.setText("0");
        } else {
            holder.tv_show_favour_count.setText(postBeanList.get(position).getFavour_count());
        }
        holder.layout_favour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(postBeanList.get(position).getFavour_count());
                count++;
                postBeanList.get(position).setFavour_count(count + "");
                notifyData();
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView item_tv_userame, item_tv_publish_time, item_tv_price, item_tv_desc, tv_show_favour_count;
        ImageView item_img;
        LinearLayout layout_favour;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            //加载图片可见项
            mImageLoader.loadImages(mStart, mEnd);
        } else {
            //停止加载图片
            mImageLoader.cancelAllTasks();
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;// 第一个可见项
        mEnd = firstVisibleItem + visibleItemCount;// 最后一个可见项
        //第一次显示的时候调用
        if (mFirstIn && visibleItemCount > 0) {
            mImageLoader.loadImages(mStart, mEnd);
            mFirstIn = false;
        }
    }
}
