package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

import com.juxun.business.street.util.SystemBarTintManager;
import com.yl.ming.efengshe.R;

public class WelcomeActivity extends BaseActivity {
    private LinearLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        view = (LinearLayout) findViewById(R.id.welcome_layout);
        AlphaAnimation anim = new AlphaAnimation(0.1f, 1.0f);// 动画透明度
        anim.setDuration(4000);
        view.startAnimation(anim);
        anim.setAnimationListener(new WelcomeAnimation());
        setTranslucentStatus();
    }

    private Boolean isFirstEnter = false;

    private class WelcomeAnimation implements AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            isFirstEnter = mSavePreferencesData.getBooleanData("isFirstEnter");

            Intent intent = new Intent();
            if (!isFirstEnter) { // 第一次进来跳转引导页面
                intent.setClass(WelcomeActivity.this, GuideActivity.class);
            } else {
                intent.setClass(WelcomeActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            WelcomeActivity.this.finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

    /**
     * 设置状态栏背景状态
     */
    private void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(android.R.color.transparent);//
        // 状态栏无背景
    }

    @Override
    public void onResponse(String response, String url) {
        // TODO Auto-generated method stub

    }

}
