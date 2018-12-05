/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.juxun.business.street.bean.TopUpBean;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @version 充值记录详情
 */
public class PurchasingDetailsActivity extends BaseActivity {
    @ViewInject(R.id.tv_remark)
    private TextView tv_remark;// 备注
    @ViewInject(R.id.tv_number)
    private TextView tv_number;// 订单号
    @ViewInject(R.id.tv_timer)
    private TextView tv_timer;// 时间
    @ViewInject(R.id.tv_price)
    private TextView tv_price;// 余额
    @ViewInject(R.id.tv_type_title)
    private TextView tv_type_title;//交易类型标题
    @ViewInject(R.id.tv_type)
    private TextView tv_type;//类型
    private TopUpBean mTopUpBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchasing_details);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("交易详情");
        initView();
    }

    private void initView() {
        mTopUpBean = (TopUpBean) getIntent().getSerializableExtra("topup");
        if (mTopUpBean.getRecharge_remark() != null) {
            tv_remark.setText(mTopUpBean.getRecharge_remark());
        }
        tv_number.setText(mTopUpBean.getOut_order_num());
        tv_timer.setText(Tools.getDateformat2(mTopUpBean.getRecharge_time()));
        switch (mTopUpBean.getRecharge_type()) {
            case 0:
            case 1:
            case 2:
            case 3:
                tv_type.setText("余额充值");
                tv_type_title.setText("余额充值");
                tv_price.setText("+" + Tools.getFenYuan(mTopUpBean.getRecharge_price()));
                break;
            case 5:
                tv_type.setText("采购订单退款");
                tv_type_title.setText("采购订单退款");
                tv_price.setText("+" + Tools.getFenYuan(mTopUpBean.getRecharge_price()));
                break;
            case 4:
                tv_type.setText("采购订单支付");
                tv_type_title.setText("采购订单支付");
                tv_price.setText("- " + Tools.getFenYuan(mTopUpBean.getPay_price()));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            Log.i("test", response);
            int status = json.getInt("status");
            String msg = json.getString("msg");
            if (status == 0) {
            } else if (status == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(PurchasingDetailsActivity.this, LoginActivity.class, false);
                Tools.showToast(PurchasingDetailsActivity.this, "登录过期请重新登录");
                Tools.acts.clear();
            } else {
                Tools.showToast(getApplicationContext(), msg);
            }
        } catch (JSONException e) {
            Tools.showToast(getApplicationContext(), "解析数据错误");
        }
    }
}
