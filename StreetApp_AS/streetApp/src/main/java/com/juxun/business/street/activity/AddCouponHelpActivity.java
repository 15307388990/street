package com.juxun.business.street.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yl.ming.efengshe.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新增代金券页面说明
 */
public class AddCouponHelpActivity extends BaseActivity {

    @Bind(R.id.btn_back)
    ImageView btnBack;
    @Bind(R.id.top_view_back)
    LinearLayout topViewBack;
    @Bind(R.id.right_view_text)
    TextView rightViewText;
    @Bind(R.id.top_view_text)
    TextView topViewText;
    @Bind(R.id.right_img)
    ImageView rightImg;
    @Bind(R.id.titleBar)
    RelativeLayout titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon_help);
        ButterKnife.bind(this);

        initTitle();
        topViewText.setText("新增代金券帮助");
    }

    @Override
    public void onResponse(String response, String url) {

    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }
}
