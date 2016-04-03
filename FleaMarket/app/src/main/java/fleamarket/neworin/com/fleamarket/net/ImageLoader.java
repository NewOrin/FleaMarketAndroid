package fleamarket.neworin.com.fleamarket.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import fleamarket.neworin.com.fleamarket.R;
import fleamarket.neworin.com.fleamarket.adapter.HomeListViewAdapter;

public class ImageLoader {

    private ImageView mImageView;
    private String mUrl;

    // 使用LruCache缓存，创建cache
    private LruCache<String, Bitmap> mCaches;
    private ListView mListView;
    private Set<NewsAsyncTask> mTasks;

    public ImageLoader(ListView listview) {
        mListView = listview;
        mTasks = new HashSet<ImageLoader.NewsAsyncTask>();
        // 获取最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        mCaches = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 在每次存入缓存的时候调用，告诉系统当前存入的缓存有多大
                return value.getByteCount();// 将bitmap的实际大小保存进去
            }
        };
    }

    /*
     * 将bitmap存入缓存，<Key,Value>形式，类似Map
     */
    public void addBitmapToCache(String url, Bitmap bitmap) {
        // 判断缓存是否有当前url的图片
        if (getBitmapFromCache(url) == null) {
            mCaches.put(url, bitmap);
        }
    }

    /**
     * 得到缓存数据
     *
     * @param url
     * @return
     */
    public Bitmap getBitmapFromCache(String url) {
        return mCaches.get(url);
    }

    /**
     * 安卓中不能通过子线程更新UI，所以通过Handler来更新
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mImageView.getTag().equals(mUrl)) {
                mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }

    };

    /**
     * 实现异步加载有2中方式 1，多线程； 2，通过异步加载
     */

    /**
     * 1,多线程方式
     */
    public void showImageByThread(ImageView imageView, final String url) {
        mImageView = imageView;
        this.mUrl = url;
        new Thread() {
            @Override
            public void run() {
                super.run();
                Bitmap bitmap = getBitmapFromURL(url);
                // 将bitmap以message的形式发送出去以更新UI
                Message message = Message.obtain();
                message.obj = bitmap;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    /**
     * 2,异步加载方式
     *
     * @param imageView
     * @param url
     */
    public void showImageByAsyncTask(ImageView imageView, String url) {
        // 从缓存中取出对应的图片
        Bitmap bitmap = getBitmapFromCache(url);
        // 如果缓存中没有则从网络下载
        if (bitmap == null) {
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 加载从start到end的所有图片
     *
     * @param start
     * @param end
     */
    public void loadImages(int start, int end) {
        for (int i = start; i < end; i++) {
            String url = HomeListViewAdapter.URLS[i];
            // 从缓存中取出对应的图片
            Bitmap bitmap = getBitmapFromCache(url);
            // 如果缓存中没有则从网络下载
            if (bitmap == null) {
                NewsAsyncTask task = new NewsAsyncTask(url);
                task.execute(url);
                mTasks.add(task);
            } else {
                ImageView imageView = (ImageView) mListView
                        .findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private class NewsAsyncTask extends AsyncTask<String, Void, Bitmap> {

        // private ImageView imageView;
        private String url;

        // public NewsAsyncTask(ImageView imageView, String url) {
        // this.imageView = imageView;
        // this.url = url;
        // }

        public NewsAsyncTask(String url) {
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... arg0) {
            String url = arg0[0];
            // 从网络获取图片
            Bitmap bitmap = getBitmapFromURL(url);
            if (bitmap != null) {
                // 将不在缓存中的图片加入缓存
                addBitmapToCache(url, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            // if (imageView.getTag().equals(url)) {
            // imageView.setImageBitmap(bitmap);
            // }
            ImageView imageView = (ImageView) mListView.findViewWithTag(url);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            mTasks.remove(this);
        }
    }

    /**
     * 从网络获取图片
     *
     * @param urlString
     * @return
     */
    public Bitmap getBitmapFromURL(String urlString) {
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            // 通过安卓中的decodeStream API将inputstream转化为bitmap
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    public void cancelAllTasks() {
        if (mTasks != null) {
            for (NewsAsyncTask task : mTasks) {
                task.cancel(false);
            }
        }
    }
}
