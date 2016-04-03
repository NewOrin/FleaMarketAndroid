package fleamarket.neworin.com.fleamarket.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fleamarket.neworin.com.fleamarket.R;

/**
 * 自定义UI--Topbar
 * Created by NewOr on 2016/3/4.
 */
public class TopBar extends RelativeLayout {

    private Button leftButton, rightButton;//左右边Butotn
    private TextView tvTitle;//中间的标题TextView

    //左边Button的属性
    private int leftTextColor;
    private Drawable leftBackground;
    private String leftText;

    //右边Button的属性
    private int rightTextColor;
    private Drawable rightBackground;
    private String rightText;

    //Title属性
    private float titleTextSize;//文字大小
    private int titleTextColor;//文字颜色
    private String title;//显示文字内容

    //将控件放到ViewGroup中
    private LayoutParams leftParams, rightParams, titleParams;


    //需要一个变量来映射调用者所传递过来的接口
    private topbarClickListener listener;

    //接口回调机制，首先定义一个接口
    public interface topbarClickListener {
        public void leftClick();

        public void rightClick();
    }

    public void setOnTopBarClickListener(topbarClickListener listener) {
        this.listener = listener;
    }

    public TopBar(Context context) {
        super(context);
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);//TypedArray用来获取我们自定义的属性

        //左边Button
        leftTextColor = ta.getColor(R.styleable.TopBar_leftTextColor, 0);//自定义View的名字+属性名
        leftBackground = ta.getDrawable(R.styleable.TopBar_leftBackground);
        leftText = ta.getString(R.styleable.TopBar_leftText);

        //右边Button
        rightTextColor = ta.getColor(R.styleable.TopBar_rightTextColor, 0);//自定义View的名字+属性名
        rightBackground = ta.getDrawable(R.styleable.TopBar_rightBackground);
        rightText = ta.getString(R.styleable.TopBar_rightText);

        //中间Title
        titleTextSize = ta.getDimension(R.styleable.TopBar_myTitleTextSize, 0);
        titleTextColor = ta.getColor(R.styleable.TopBar_myTitleTextColor, 0);
        title = ta.getString(R.styleable.TopBar_myTitle);

        ta.recycle();//避免浪费资源，避免由于缓存造成的错误

        //实例化控件
        leftButton = new Button(context);
        rightButton = new Button(context);
        tvTitle = new TextView(context);

        //将我们自定义属性赋给控件
        leftButton.setTextColor(leftTextColor);
        leftButton.setBackground(leftBackground);
        leftButton.setText(leftText);

        rightButton.setTextColor(rightTextColor);
        rightButton.setBackground(rightBackground);
        rightButton.setText(rightText);

        tvTitle.setText(title);
        tvTitle.setTextColor(titleTextColor);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setGravity(Gravity.CENTER);

        //给TopBar设置背景颜色
        setBackgroundColor(Color.RED);

        //定义控件的布局属性
        leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//定义宽，高属性
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);//设置左对齐

        addView(leftButton, leftParams);//将leftButton以leftParam的形式加入到ViewGroup中

        //定义控件的布局属性
        rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//定义宽，高属性
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);//设置左对齐

        addView(rightButton, rightParams);//将rightButton以rightParams的形式加入到ViewGroup中

        //定义控件的布局属性
        titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);//定义宽，高属性
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);//设置左对齐

        addView(tvTitle, titleParams);//将tvTitle以titleParams的形式加入到ViewGroup中

        /**
         * 左右标题点击事件
         */
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftClick();
            }
        });
        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rightClick();
            }
        });


    }

    /**
     * 动态设置标题栏文字
     *
     * @param titleText
     */
    public void setTitleText(String titleText) {
        tvTitle.setText(titleText);
    }

    public void setLeftButtonVisible(boolean flag) {
        if (flag)
            leftButton.setVisibility(VISIBLE);
        else
            leftButton.setVisibility(GONE);
    }

    public void setrightButtonVisible(boolean flag) {
        if (flag)
            rightButton.setVisibility(VISIBLE);
        else
            rightButton.setVisibility(GONE);
    }
}
