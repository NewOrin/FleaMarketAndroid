package fleamarket.neworin.com.fleamarket.util;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * 缓存图片
 * Created by NewOr on 2016/2/28.
 */
public class BitmapCache implements ImageLoader.ImageCache {
    //LruCache是基于内存的缓存类
    private LruCache<String, Bitmap> lruCache;
    //LruCache的最大缓存大小
    private int max = 10 * 1024 * 1024;

    public BitmapCache() {
        lruCache = new LruCache<String, Bitmap>(max) {
            //缓存图片大小
            public int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String s) {
        return lruCache.get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        lruCache.put(s, bitmap);
    }
}
