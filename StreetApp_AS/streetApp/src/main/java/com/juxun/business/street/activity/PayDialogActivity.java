/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.iboxpay.cashbox.minisdk.CashboxProxy;
import com.iboxpay.cashbox.minisdk.PayType;
import com.iboxpay.cashbox.minisdk.SignType;
import com.iboxpay.cashbox.minisdk.callback.ITradeCallback;
import com.iboxpay.cashbox.minisdk.exception.ConfigErrorException;
import com.iboxpay.cashbox.minisdk.model.ErrorMsg;
import com.iboxpay.cashbox.minisdk.model.ParcelableBitmap;
import com.iboxpay.cashbox.minisdk.model.ParcelableMap;
import com.juxun.business.street.bean.GiveOrder;
import com.juxun.business.street.bean.Order;
import com.juxun.business.street.bean.PayBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.StringUtil;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WuJianHua 选择支付方式弹框
 */
public class PayDialogActivity extends BaseActivity {
    @ViewInject(R.id.aliPaycard)
    private LinearLayout aliPaycard; // 支付宝刷卡
    @ViewInject(R.id.aliPay)
    private LinearLayout aliPay; // 支付宝支付

    @ViewInject(R.id.weixinPaycard)
    private LinearLayout weixinPaycard; // 微信刷卡
    @ViewInject(R.id.weixinPay)
    private LinearLayout weixinPay; // 微信支付

    @ViewInject(R.id.casbox)
    private LinearLayout casbox; // 银联刷卡
    @ViewInject(R.id.dismissView)
    private TextView cancel; // 取消

    private String count;
    private double Amount;
    private int type; // 支付方式 1.支付宝 2.微信 4.盒子

    /**
     * 您的订单号
     */
    private String outTradeNo;
    /**
     * 钱盒交易订单号
     */
    private String mCbTradeNo;
    /**
     * 返回的交易金额
     */
    private String mAmount;
    /**
     * 返回的交易时间s
     */
    private String mTradeTime;
    private String out_trade_no;
    private long startTime;
    private Intent intentScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay);
        ViewUtils.inject(this);
        count = getIntent().getStringExtra("count");
        Amount = Double.valueOf(count);
        mAmount = StringUtil.toFenByYuan(count);
        overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.cancel, R.id.aliPay, R.id.weixinPay, R.id.dismissView, R.id.aliPaycard, R.id.weixinPaycard,
            R.id.casbox})
    public void clickMethod(View v) {
        intentScan = new Intent(this, MipcaActivityCapture.class);
        switch (v.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.weixinPay:
                type = 2;
                intentScan.putExtra("type", 5);
                checkoutCameraPermissions();  //是否有权限
                break;
            case R.id.aliPay:
                type = 1;
                intentScan.putExtra("type", 5);
                checkoutCameraPermissions();
                break;
            case R.id.aliPaycard:
                Intent intentPayCard = new Intent(this, PayActivity.class);
                intentPayCard.putExtra("pay", 1);
                intentPayCard.putExtra("tab", 666);
                intentPayCard.putExtra("mAmount", mAmount);
                intentPayCard.putExtra("count", count);
                startActivity(intentPayCard);
                break;
            case R.id.weixinPaycard:
                Intent intentPay = new Intent(this, PayActivity.class);
                intentPay.putExtra("pay", 2);
                intentPay.putExtra("tab", 666);
                intentPay.putExtra("mAmount", mAmount);
                intentPay.putExtra("count", count);
                startActivity(intentPay);
                break;
            case R.id.casbox:
                if (mSavePreferencesData.getBooleanData("isSn")) {  //首选判断设备是否为pos机
                    if (!ParamTools.isFastDoubleClick()) {
                        // 如果订单金额 小于2块 则不能支付
                        if (Amount < 2) {
                            Tools.showToast(this, "金额不超过2元不能使用银联支付");
                        } else {
                            toPayByBoxAction();
                        }
                    } else {
                        Tools.showToast(this, "请不要重复点击");
                    }
                } else {
                    Tools.showToast(this, "该设备不支持银联支付");
                }
                break;
        }
    }

    @Override
    protected void gotThePermission() {
        startActivityForResult(intentScan, 1);
    }

    @PermissionGrant(REQUEST_CODE_TAKE_PHOTE)
    public void requestCameraSuccess() {
        //扫描只有在被同意的情况下才能跳转
        startActivityForResult(intentScan, 1);
    }

    @PermissionDenied(REQUEST_CODE_TAKE_PHOTE)
    public void requestCameraFailed() {
        Toast.makeText(this, "请打开拍照权限以扫码支付", Toast.LENGTH_SHORT).show();
    }

    /**
     * 刷卡支付响应事件
     */
    private void paySwipCard(PayBean payBean) {
        order = new Order();
        // 获取当前交易时间
        order.setmDateTime(payBean.getOrderTime());
        order.setmGoodsName("");
        // ***********订单存储**************
        order.setmPayType(StringUtil.PAY_TYPE_SWIP_CARD);
        // *************订单存储结束**********************

        // 模拟生产一个第三方流水号
        outTradeNo = "out_" + System.currentTimeMillis();

        order.setmPayType(StringUtil.PAY_TYPE_SWIP_CARD);
        try {
            String transactionId = System.currentTimeMillis() + "";
            ParcelableMap additionalMap = new ParcelableMap();
            additionalMap.put(ParcelableMap.TRANSACTION_ID, payBean.getTransactionId());
            additionalMap.put(ParcelableMap.ORDER_TIME, payBean.getOrderTime());
            additionalMap.put(ParcelableMap.CALL_BACK_URL, payBean.getNotify_url());
            // 需要参与签名的字段：appCode,partnerId,iboxMchtNo,partnerUserId,transactionId,orderNo(可不传),outTradeNo,tradeNo(没有时传null),transAmount,orderTime,callbackUrl;

            // ****设置打印出来的订单号*****
            additionalMap.put(ParcelableMap.PRINT_ORDER_NO, "交易自定义第三方流水号");
            // 发起交易
            CashboxProxy.getInstance(PayDialogActivity.this).startTrading(PayType.TYPE_CARD, payBean.getTotal_fee(),
                    payBean.getTradeNo(), payBean.getTransactionId(), SignType.TYPE_MD5, payBean.getSign(),
                    additionalMap, new ITradeCallback() {
                        @Override
                        public void onTradeSuccess(ParcelableMap map) {
                            finish();
                            for (String key : map.getMap().keySet()) {

                            }
                            mCbTradeNo = map.get(ParcelableMap.CB_TRADE_NO);
                            outTradeNo = map.get(ParcelableMap.PARTNER_TRADE_NO);
                            mAmount = map.get(ParcelableMap.TRANS_AMOUNT);
                            mTradeTime = map.get("transaction_time");

                            // *******写入文件******
                            order.setmCbTradeNo(mCbTradeNo);
                            order.setmOutTradeNo(outTradeNo);
                            order.setmAmount(toYuanAmount(mAmount));
                            order.setmTradeStatus("交易成功");
                            objToFile.writeObject(order);


                            // ********写入文件结束************
                        }

                        // 设置了不显示盒子签购单时回调此方法。
                        @Override
                        public void onTradeSuccessWithSign(ParcelableMap map, ParcelableBitmap signBitmap) {
                            finish();
                            Log.e("&&&&&", "onTradeSuccessWithSign");
                            for (String key : map.getMap().keySet()) {
                            }
                            mCbTradeNo = map.get(ParcelableMap.CB_TRADE_NO);
                            outTradeNo = map.get(ParcelableMap.PARTNER_TRADE_NO);
                            mAmount = map.get(ParcelableMap.TRANS_AMOUNT);
                            mTradeTime = map.get("transaction_time");

                            // *******写入文件******
                            order.setmCbTradeNo(mCbTradeNo);
                            order.setmOutTradeNo(outTradeNo);
                            order.setmAmount(toYuanAmount(mAmount));
                            order.setmTradeStatus("交易成功");
                            objToFile.writeObject(order);
                        }

                        @Override
                        public void onTradeFail(ErrorMsg msg) {
                            finish();

                            // *******写入文件******
                            order.setmTradeStatus(msg.getErrorMsg());
                            order.setmAmount(toYuanAmount(mAmount));
                            order.setmTradeStatus("交易失败");
                            order.setmCbTradeNo(mCbTradeNo);
                            order.setmOutTradeNo(outTradeNo);
                            objToFile.writeObject(order);
                            // ********写入文件结束************
                        }
                    });
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            switch (resultCode) {
                case RESULT_OK:
                    String qCode = data.getStringExtra("result");
                    PayMoney(qCode);
                    break;
                case RESULT_FIRST_USER:
                    break;
                case RESULT_CANCELED:
                    break;
                default:
                    break;
            }
        }
    }

    /* 执行扫描结果上传操作 */
    public void PayMoney(String qCode) {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("order_pay_type", type + "");   //type 1支付宝、2微信
        map.put("pay_price", mAmount);
        map.put("order_source", 1 + "");
        map.put("auth_code", qCode);// 授权码
        mQueue.add(ParamTools.packParam(Constants.scanPay, this, this, map));
    }

    /* 获取支付标志 */
    public void toPayByBoxAction() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pay_price", Amount + "");// 金额
        map.put("auth_token", partnerBean.getAuth_token() + "");// 验签参数
        mQueue.add(ParamTools.packParam(Constants.toPayByBoxAction, this, this, map));
    }

    /**
     * 通过上传 userID获取 apikey
     */
    public void getApiKey() {
        Map<String, String> map = new HashMap<String, String>();
        // map.put("userid", userID);
        map.put("auth_token", partnerBean.getAuth_token() + "");// 验签参数
        mQueue.add(ParamTools.packParam(Constants.giveMyKeyAction, this, this, map));
    }

    /**
     * 订单状态查询接口
     */
    public void orderQuery(String out_trade_no) {
        Map<String, String> map = new HashMap<>();
        map.put("order_num", out_trade_no);
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.orderQuery, this, this, map));
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Tools.showToast(getApplicationContext(), "网络异常 重新连接");
    }

    private String getIp() {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);
    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        mQueue.stop();
    }

    private String toYuanAmount(String transAmount) {
        double amountDouble = Double.parseDouble(transAmount) / 100;
        return String.valueOf(amountDouble);
    }

    @Override
    public void onResponse(String response, String url) {
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("stauts");
            String msg = json.optString("msg");
            if (stauts == 0) {
                if (url.contains(Constants.toPayByBoxAction)) {
                    String payString = json.getString("pay_sign");
                    PayBean payBean = JSON.parseObject(payString, PayBean.class);
                    paySwipCard(payBean);
                } else if (url.contains(Constants.scanPay)) {
                    out_trade_no = json.getString("order_id");
                    startTime = System.currentTimeMillis();
                    orderQuery(out_trade_no);
                } else if (url.contains(Constants.orderQuery)) {
                    //支付成功的数据
                    GiveOrder giveOrder = JSON.parseObject(json.optString("result"), GiveOrder.class);
                    if (giveOrder.getOrder_state() == 3) {
                        Tools.showToast(getApplicationContext(), "用户取消支付");
                        this.finish();
                    } else if (giveOrder.getOrder_state() == 1) {
                        Intent intent = new Intent(PayDialogActivity.this, PaySuccessActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("give", giveOrder);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        if (System.currentTimeMillis() - startTime < 5 * 60 * 1000) {
                            orderQuery(out_trade_no);
                        }
                    }
                }
            } else {
                Tools.showToast(this, "位置错误");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }
}
