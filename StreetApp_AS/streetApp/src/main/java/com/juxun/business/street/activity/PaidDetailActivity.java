package com.juxun.business.street.activity;

/**
 * 14、结算详情
 */

import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.bean.OrderModel;
import com.juxun.business.street.bean.SettleListBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.PartnerPosOrderBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaidDetailActivity extends BaseActivity {
    /**
     * ----------------------- 商品清单--------------------------------
     **/
    @ViewInject(R.id.tv_order_id)
    private TextView tv_order_id;

    @ViewInject(R.id.tv_order_creat_date)
    private TextView tv_order_creat_date;

    @ViewInject(R.id.tv_order_end_date)
    private TextView tv_order_end_date;

    @ViewInject(R.id.tv_order_pay_type)
    private TextView tv_order_pay_type;

    @ViewInject(R.id.tv_order_state)
    private TextView tv_order_state;

    @ViewInject(R.id.tv_order_price)
    private TextView tv_order_price;
    SettleListBean partnerPosOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_detail);
        ViewUtils.inject(this);
        initTitle();
        title.setText("订单详情");
        initView();
    }

    public void initView() {
        partnerPosOrder = (SettleListBean) getIntent().getSerializableExtra("financeSettle");
        initDate();
    }

    private void initDate() {
        if (partnerPosOrder.getOrder_id() != null) {
            tv_order_id.setText(partnerPosOrder.getOrder_id());
        }


        tv_order_end_date.setText(Tools.getDateformat2(partnerPosOrder.getCreate_date()));
        tv_order_creat_date.setText(Tools.getDateformat2(partnerPosOrder.getOrder_date()));

        tv_order_price.setText("¥" + Tools.getFenYuan(partnerPosOrder.getSettle_pay_price()));
        // 订单状态【1.待付款,2.待发货,3.已发货,4.交易完成,5.已取消,6.已退货】
        switch (partnerPosOrder.getSettle_stauts()) {
            case -1:
                tv_order_pay_type.setText("异常状态");
                break;
            case 0:
                tv_order_pay_type.setText("冻结中");
                break;
            case 1:
                tv_order_pay_type.setText("已解冻");
                break;
            default:
                break;
        }
        // 支付方式 //支付类型 1.微信支付.2.支付宝支付 3、盒子支付
        switch (partnerPosOrder.getPay_type()) {
            case 1:
                tv_order_state.setText("微信支付");
                break;
            case 2:
                tv_order_state.setText("支付宝支付");
                break;
            case 3:
                tv_order_state.setText("刷卡支付");
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            String msg = json.optString("msg");
            if (stauts == 0) {

            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

}