package com.juxun.business.street.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.juxun.business.street.util.Tools;
import com.yl.ming.efengshe.R;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity {
    private ViewPager viewPager; // android-support-v4中的滑动组件
    private MyPageChangeListener mPageChangeListener;
    private MyPagerAdapter mAdapter;
    private List<View> imageViews; // 滑动的图片集合
    private int[] imageResources = {R.drawable.guidepage1,
            R.drawable.guidepage2, R.drawable.guidepage3};

    private List<View> dots; // 图片标题正文的那些点
    private View v_dot0, v_dot1, v_dot2;

    private Button btn_skip, btn_go;// 跳过
    private TextView mTv_upper1;
    private TextView mTv_upper2;
    private TextView mTv_upper3;
    private TextView mTv_lower1;
    private TextView mTv_lower2;
    private TextView mTv_lower3;
    private String[] upperStrs = {"商品管理改版", "更改提现入口", "新增营业分析"};
    private String[] lowerStrs = {"管理商品更方便快捷", "以后想提现更方便", "随时查看营业动态"};

    private ArrayList<TextView> mTvUppers;
    private ArrayList<TextView> mTvLowers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 初始化视图
        setContentView(R.layout.activity_guide);
        mSavePreferencesData.putBooleanData("isFirstEnter", true);
        initViewsAndEvents();
    }

    private void initViewsAndEvents() {
        // 找到控件
        btn_skip = (Button) findViewById(R.id.btn_skip); // 跳过按钮
        mTv_upper1 = (TextView) findViewById(R.id.tv_upper1);// 文案
        mTv_upper2 = (TextView) findViewById(R.id.tv_upper2);// 文案
        mTv_upper3 = (TextView) findViewById(R.id.tv_upper3);// 文案
        mTvUppers = new ArrayList<TextView>();
        mTvUppers.add(mTv_upper1);
        mTvUppers.add(mTv_upper2);
        mTvUppers.add(mTv_upper3);

        mTv_lower1 = (TextView) findViewById(R.id.tv_lower1);
        mTv_lower2 = (TextView) findViewById(R.id.tv_lower2);
        mTv_lower3 = (TextView) findViewById(R.id.tv_lower3);
        mTvLowers = new ArrayList<TextView>();
        mTvLowers.add(mTv_lower1);
        mTvLowers.add(mTv_lower2);
        mTvLowers.add(mTv_lower3);

        btn_go = (Button) findViewById(R.id.btn_go);// 立即体验
        btn_skip.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Tools.jump(GuideActivity.this, LoginActivity.class, true);
            }
        });
        btn_go.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Tools.jump(GuideActivity.this, LoginActivity.class, true);
            }
        });

        // 循环点
        v_dot0 = findViewById(R.id.v_dot0);
        v_dot1 = findViewById(R.id.v_dot1);
        v_dot2 = findViewById(R.id.v_dot2);
        dots = new ArrayList<View>();
        dots.add(v_dot0);
        dots.add(v_dot1);
        dots.add(v_dot2);

        // viewPager
        mAdapter = new MyPagerAdapter();
        viewPager = (ViewPager) findViewById(R.id.vp);
        viewPager.setAdapter(mAdapter);// 设置填充ViewPager页面的适配器
        viewPager.setOnPageChangeListener(mPageChangeListener);
        mPageChangeListener = new MyPageChangeListener();
        viewPager.setOnPageChangeListener(mPageChangeListener);
        imageViews = new ArrayList<View>();

        for (int i = 0; i < 3; i++) { // 只有三张图

            View guideView = LayoutInflater.from(this).inflate(
                    R.layout.guide_img, null);
            // 改变的图片
            ImageView iv_guide = (ImageView) guideView
                    .findViewById(R.id.iv_guide);
            iv_guide.setImageResource(imageResources[i]);
            iv_guide.setScaleType(ScaleType.CENTER_CROP);
            imageViews.add(guideView);
        }
    }

    /**
     * 当ViewPager中页面的状态发生改变时调用
     *
     * @author Administrator
     */
    private class MyPageChangeListener implements OnPageChangeListener,
            OnClickListener {
        private int oldPosition = 0; // 记录的
        private int currentPosition = 0;

        /**
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
            // 按钮的变化
            if (position == 2) { // 只有三张
                btn_skip.setVisibility(View.GONE);
            } else {
                btn_skip.setVisibility(View.VISIBLE);
            }

            // 点的变化
            dots.get(oldPosition).setBackgroundResource(
                    R.drawable.guidepage_point_n);
            dots.get(position).setBackgroundResource(
                    R.drawable.guidepage_point_s);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // 滑动的情况，当前页面淡出
            mTvUppers.get(arg0).setAlpha(1 - arg1);
            mTvLowers.get(arg0).setAlpha(1 - arg1);

            // 即将到来的页面淡入
            if (arg2 > 0) {
                if (arg0 > currentPosition) { // 右滑
                    currentPosition = arg0;

                    // 文案的切换
                    if (arg1 >= 0.5) {
                        mTvUppers.get(arg0 - 1).setAlpha((float) (arg1 * 0.9));
                        mTvLowers.get(arg0 - 1).setAlpha((float) (arg1 * 0.9));
                    }

                    // btn的切换
                    if (arg0 == 2) {
                        btn_go.setAlpha(1 - arg1);
                    }
                } else {
                    currentPosition = arg0; // 左滑

                    // 文案的切换
                    mTvUppers.get(arg0 + 1).setAlpha((float) (arg1 * 0.9));
                    mTvLowers.get(arg0 + 1).setAlpha((float) (arg1 * 0.9));

                    // btn的切换
                    if (arg0 == 1) {
                        btn_go.setAlpha(arg1);
                    }
                }
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.v_dot0:
                    onPageSelected(0);
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.v_dot1:
                    onPageSelected(1);
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.v_dot2:
                    onPageSelected(2);
                    viewPager.setCurrentItem(2);
                    break;
            }
        }
    }

    /**
     * 填充ViewPager页面的适配器
     *
     * @author Administrator
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(imageViews.get(arg1));

            View view = imageViews.get(arg1);
            return view;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            Log.e("yj", "destroyItem" + imageViews.get(arg1));
            View view = (View) arg2;
            ((ViewPager) arg0).removeView(view);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }
    }

    @Override
    public void onResponse(String response, String url) {

    }

}
