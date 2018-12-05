/**
 *
 */
package com.juxun.business.street.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

/**
 * @author 红包弹框
 */
public class AredenvelopeDialogActivity extends BaseActivity {

    @ViewInject(R.id.iv_colse)
    private ImageView iv_colse;// 关闭

    @ViewInject(R.id.tv_price)
    private TextView tv_price;// 金额

    @ViewInject(R.id.tv_to_view)
    private TextView tv_to_view;// 去查看

    private double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_a_red);
        ViewUtils.inject(this);
        overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
        price = getIntent().getDoubleExtra("price", 0);
        tv_price.setText("¥" + price);
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.iv_colse, R.id.tv_to_view})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.iv_colse:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.tv_to_view:
                Tools.jump(AredenvelopeDialogActivity.this, RedPacketActivity.class, true);
                break;
        }
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    @Override
    public void onResponse(String response, String url) {

    }
}
