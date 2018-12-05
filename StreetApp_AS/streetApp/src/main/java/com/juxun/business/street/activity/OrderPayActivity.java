/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.juxun.business.street.bean.GiveOrder;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.BitmapUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 生成二维码支付
 */
public class OrderPayActivity extends BaseActivity {

    @ViewInject(R.id.payMethodTips)
    private TextView payMethodTips;// 支付方式提示
    @ViewInject(R.id.payMoney)
    private TextView payMoney;// 支付金额
    @ViewInject(R.id.payMethod)
    private TextView payMethod;// 支付方式
    @ViewInject(R.id.orderId)
    private TextView orderId;// 交易单号
    @ViewInject(R.id.orderTime)
    private TextView orderTime;// 交易时间
    @ViewInject(R.id.qrcodeView)
    private ImageView qrcodeView;// 二维码图片

    private int payType;// 0为微信支付 1为支付宝支付
    private String qr_code;// 二维码地址
    private String payOrderId;// 订单号
    private int payPrice;// 金额
    private long startTime;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ViewUtils.inject(this);
        initTitle();
        title.setText(R.string.receive_money);
        payType = getIntent().getIntExtra("payType", 0);
        payPrice = getIntent().getIntExtra("payPrice", 0);
        qr_code = getIntent().getStringExtra("qr_code");
        payOrderId = getIntent().getStringExtra("payOrderId");
        type = getIntent().getIntExtra("type", 0);

        setData();
    }

    /**
     * 检测状态
     */
    public void checkState() {
        Map<String, String> map = new HashMap<String, String>();
        // trade_no String 管理员机构id
        // admin_id int 管理员id
        // auth_token 登陆令牌
        map.put("trade_no", payOrderId);
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.orderQuery, this, this, map));
    }

    /**
     * 检测状态
     */
    public void orderQuery2() {
        Map<String, String> map = new HashMap<String, String>();
        // trade_no String 管理员机构id
        // admin_id int 管理员id
        // auth_token 登陆令牌
        map.put("trade_no", payOrderId);
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.orderQuery2, this, this, map));
    }

    /**
     * 设置数据
     */
    public void setData() {
        if (payType == 0) {
            payMethodTips.setText(getString(R.string.weixinpay_tips));
            payMethod.setText(getString(R.string.weixin_pay));
        } else {
            payMethodTips.setText(getString(R.string.alipay_tips));
            payMethod.setText(getString(R.string.ali_pay));
        }
        payMoney.setText("¥" + Tools.getFenYuan(payPrice));
        orderTime.setText(Tools.obtainDateNow());
        qrcodeView.setImageBitmap(BitmapUtil.createQRImage(qr_code, 400, 400));
        startTime = System.currentTimeMillis();
        if (type == 0) {
            checkState();
        } else {
            orderQuery2();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (type == 0) {
            Tools.jump(OrderPayActivity.this, OrderActivity.class, true);
        }
        finish();
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("stauts");
            if (stauts == 0) {
                if (url.contains(Constants.orderQuery)) {
                    Gson gson = new Gson();
                    GiveOrder giveOrder = gson.fromJson(response, GiveOrder.class);
                    if (giveOrder.getOrder_state() == 1) {
                        Intent intent = new Intent(OrderPayActivity.this, PaySuccessActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("give", giveOrder);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        this.finish();
                    } else {
                        if (System.currentTimeMillis() - startTime < 5 * 60 * 1000) {
                            checkState();
                        }
                        return;
                    }
                } else if (url.contains(Constants.orderQuery2)) {
                    Gson gson = new Gson();
                    GiveOrder giveOrder = gson.fromJson(response, GiveOrder.class);
                    if (giveOrder.getOrder_state() == 1) {
                        Tools.showToast(OrderPayActivity.this, "充值成功");
                        this.finish();
                    } else {
                        if (System.currentTimeMillis() - startTime < 5 * 60 * 1000) {
                            orderQuery2();
                        }
                        return;
                    }
                }

            } else if (stauts == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(this, LoginActivity.class, false);
                Tools.showToast(this, "登录过期请重新登录");
                Tools.acts.clear();
            } else {
                if (url.contains(Constants.lineOrderState)) {
                    if (System.currentTimeMillis() - startTime < 5 * 60 * 1000) {
                        checkState();
                    }
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    public void paySuccess() {
        qrcodeView.setBackgroundResource(android.R.color.transparent);
        qrcodeView.setImageResource(R.drawable.success_icon);
        payMethodTips.setTextColor(getResources().getColor(R.color.blue));
        payMethodTips.setTextSize(30);
        payMethodTips.setPadding(0, 15, 0, 25);
        payMethodTips.setText(R.string.pay_success);
    }

    protected void onStop() {
        mQueue.stop();
        super.onStop();

    }

    ;
}
