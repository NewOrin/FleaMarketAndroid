package com.example.slideview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 详情参照
 * http://blog.csdn.net/itachi85/article/details/50724558
 * Created by NewOr on 2016/2/29.
 */
public class MyView extends View {
    private int lastX;
    private int lastY;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 方法二
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算移动的距离
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                //对left和right进行偏移
                offsetLeftAndRight(offsetX);
                //对top和bottom进行偏移
                offsetTopAndBottom(offsetY);
        }
        return true;
    }
/**
 * 方法一
 @Override public boolean onTouchEvent(MotionEvent event) {
 //获取手指处的横坐标和纵坐标
 int x = (int) event.getX();
 int y = (int) event.getY();
 switch (event.getAction()) {
 case MotionEvent.ACTION_DOWN:
 lastX = x;
 lastY = y;
 break;
 case MotionEvent.ACTION_MOVE:
 //计算移动的距离
 int offsetX = x - lastX;
 int offsetY = y - lastY;
 //调用layout方法重新放置它的位置
 layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
 break;
 }
 return true;
 }**/
}
