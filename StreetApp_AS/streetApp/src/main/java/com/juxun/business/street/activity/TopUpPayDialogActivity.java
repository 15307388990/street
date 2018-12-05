/**
 *
 */
package com.juxun.business.street.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.AuthTask;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.iboxpay.cashbox.minisdk.CashboxProxy;
import com.iboxpay.cashbox.minisdk.PayType;
import com.iboxpay.cashbox.minisdk.SignType;
import com.iboxpay.cashbox.minisdk.callback.ITradeCallback;
import com.iboxpay.cashbox.minisdk.exception.ConfigErrorException;
import com.iboxpay.cashbox.minisdk.model.ErrorMsg;
import com.iboxpay.cashbox.minisdk.model.ParcelableBitmap;
import com.iboxpay.cashbox.minisdk.model.ParcelableMap;
import com.juxun.business.street.bean.ConfirmBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.GiveOrder;
import com.juxun.business.street.bean.Order;
import com.juxun.business.street.bean.PayBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.StringUtil;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.util.alipay.PayResult;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author 充值弹框
 */
public class TopUpPayDialogActivity extends BaseActivity {
    @ViewInject(R.id.ll_one)
    private LinearLayout ll_one;
    @ViewInject(R.id.aliPaycard)
    private LinearLayout aliPaycard; // 支付宝刷卡

    @ViewInject(R.id.weixinPaycard)
    private LinearLayout weixinPaycard; // 微信刷卡

    @ViewInject(R.id.ll_casbox)
    private LinearLayout ll_casbox; // 银联支付
    @ViewInject(R.id.ll_two)
    private LinearLayout ll_two;
    @ViewInject(R.id.ll_ali_pay)
    private LinearLayout ll_ali_pay;
    @ViewInject(R.id.ll_weixin_pay)
    private LinearLayout ll_weixin_pay;

    @ViewInject(R.id.dismissView)
    private TextView cancel; // 取消

    private int Amount;// 需要充值都金额
    private int recharge_activity_id;// 充值活动id
    public RequestQueue mQueue; // 请求列队
    private int paytype; // 支付方式 1.微信 2.支付宝
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // Intent intent = new Intent(
                        // OrderConfirmationActivity.this, MyOrder.class);
                        // startActivity(intent);
                        Tools.showToast(getApplicationContext(), "充值成功");
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getApplicationContext(), "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Tools.showToast(getApplicationContext(), "充值取消");
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(TopUpPayDialogActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
    private Handler mHandler2 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    // 刷卡支付完成
                    // Tools.jump(ConfirmTheOrderActivity.this, OrderActivity.class,
                    // true);
                    // Tools.exit();
                    finish();
                    break;
                }
                case 2: {
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_up_dialog_pay);
        ViewUtils.inject(this);
        mQueue = Volley.newRequestQueue(this);
        overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);

        Amount = getIntent().getIntExtra("Amount", 0);
        recharge_activity_id = getIntent().getIntExtra("recharge_activity_id", 0);
        if (mSavePreferencesData.getBooleanData("isSn")) {
            ll_one.setVisibility(View.VISIBLE);
            ll_two.setVisibility(View.GONE);
        } else {
            ll_one.setVisibility(View.GONE);
            ll_two.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.ll_weixin_pay, R.id.ll_ali_pay, R.id.cancel, R.id.dismissView, R.id.aliPaycard, R.id.weixinPaycard,
            R.id.ll_casbox})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.aliPaycard:
                // Intent intent1 = new Intent(this, PayActivity.class);
                // intent1.putExtra("pay", 1);
                // intent1.putExtra("tab", 666);
                // intent1.putExtra("count", count);
                // startActivity(intent1);
                paytype = 2;
                rechargeOrder();
                break;
            case R.id.weixinPaycard:
                paytype = 1;
                rechargeOrder();
                break;
            case R.id.ll_ali_pay:
                paytype = 2;
                rechargeOrder();
                break;
            case R.id.ll_weixin_pay:
                paytype = 1;
                rechargeOrder();
                break;
            case R.id.ll_casbox:
                paytype = 3;
                rechargeOrder();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            switch (resultCode) {
                case RESULT_OK:
                    String qCode = data.getStringExtra("result");
                    // PayMoney(qCode);
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

    // /* 执行扫描结果上传操作 */
    // public void PayMoney(String qCode) {
    // // admin_agency String 管理员机构id
    // // pay_price String 价格
    // // admin_id int 管理员id
    // // auth_token String 登陆令牌
    // // auth_code String 支付码中包含的内容
    // Map<String, String> map = new HashMap<String, String>();
    // map.put("pay_price", getIntent().getStringExtra("count"));// 价格
    // map.put("auth_code", qCode);// 授权码
    // map.put("agency_id", storeBean.getAdmin_agency() + "");// 管理员机构id
    // map.put("auth_token", storeBean.getAuth_token() + "");// 验签参数
    // map.put("admin_id", storeBean.getAdmin_id() + "");// 管理员id
    // map.put("order_source", 1 + "");
    // // 支付宝
    // if (type == 1) {
    // mQueue.add(ParamTools.packParam(Constants.AlipayMoneyAction, this, this,
    // map));
    //
    // }
    // // 微信
    // else if (type == 2) {
    // mQueue.add(ParamTools.packParam(Constants.WeixinMoneyAction, this, this,
    // map));
    //
    // }
    //
    // }

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
     * 充值下单
     */
    private void rechargeOrder() {
        if (Amount == 0) {
            Tools.showToast(TopUpPayDialogActivity.this, "金额必须大于0");
            return;
        }
        // agency_id 机构id
        // admin_id 用户id
        // auth_token
        // recharge_activity_id 充值活动id
        // recharge_price 充值金额
        // pay_type 支付类型 1微信支付2支付宝3box
        // recharge_souce 等于2为pos版本
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token() + "");// 验签参数
        map.put("price", Amount + "");
        map.put("pay_type", paytype + "");

//
//		map.put("admin_id", storeBean.getAdmin_id() + "");// 操作员id（操作员登录必填）
//		map.put("agency_id", storeBean.getAdmin_agency() + "");
//		map.put("recharge_activity_id", recharge_activity_id + "");
//		map.put("recharge_price", Amount + "");
//		map.put("pay_type", paytype + "");
//		if (mSavePreferencesData.getBooleanData("isSn")) {
//			map.put("recharge_souce", "2");
//		} else {
//			map.put("recharge_souce", "1");
//		}

        mQueue.add(ParamTools.packParam(Constants.rechargeOrder, this, this, map));
    }

    ;

    /**
     * 订单状态查询接口
     */
    public void giveOrder(String out_trade_no) {
        Map<String, String> map = new HashMap<String, String>();
        // trade_no String 管理员机构id
        // admin_id int 管理员id
        // auth_token 登陆令牌
        map.put("trade_no", out_trade_no);
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.lineOrderState, this, this, map));
    }

    ;

    @Override
    public void onErrorResponse(VolleyError error) {
        Tools.showToast(getApplicationContext(), "网络异常 重新连接");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        mQueue.stop();

    }

    @Override
    public void onResponse(String response, String url) {
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            if (stauts == 0) {
                if (url.contains(Constants.AlipayMoneyAction) || url.contains(Constants.WeixinMoneyAction)) {
                    // out_trade_no = json.getString("order_id");
                    // startTime = System.currentTimeMillis();
                    // giveOrder(out_trade_no);
                    Toast.makeText(getApplicationContext(), json.optString("msg"), 1).show();

                } else if (url.contains(Constants.lineOrderState)) {
                    int result = json.optInt("s_result");
                    if (result == 3) {
                        // if (System.currentTimeMillis() - startTime < 5 * 60 *
                        // 1000) {
                        // giveOrder(out_trade_no);
                        // }
                    } else if (result == 2) {
                        Tools.showToast(getApplicationContext(), "用户取消支付");
                        this.finish();
                    } else if (result == 1) {
                        Gson gson = new Gson();
                        GiveOrder giveOrder = gson.fromJson(response, GiveOrder.class);
                        Intent intent = new Intent(TopUpPayDialogActivity.this, PaySuccessActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("give", giveOrder);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } else if (url.contains(Constants.rechargeOrder)) {


                    if (paytype == 1) {
                        // 微信支付
                        if (mSavePreferencesData.getBooleanData("isSn")) {
                            try {
                                String qr_code = json.getString("qr_code");
                                String payOrderId = json.getString("payOrderId");
                                int payPrice = json.getInt("payPrice");
                                Intent intent1 = new Intent(TopUpPayDialogActivity.this, OrderPayActivity.class);
                                intent1.putExtra("qr_code", qr_code);
                                intent1.putExtra("payOrderId", payOrderId);
                                intent1.putExtra("payPrice", payPrice);
                                intent1.putExtra("payType", 0);
                                intent1.putExtra("type", 1);
                                startActivity(intent1);
                                finish();
                                Tools.webexit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // initRay(confirmBean);
                        }
                    } else if (paytype == 2) {
                        // 支付宝支付
                        if (mSavePreferencesData.getBooleanData("isSn")) {
                            try {
                                String qr_code = json.getString("qr_code");
                                String payOrderId = json.getString("payOrderId");
                                int payPrice = json.getInt("payPrice");
                                Intent intent1 = new Intent(TopUpPayDialogActivity.this, OrderPayActivity.class);
                                intent1.putExtra("qr_code", qr_code);
                                intent1.putExtra("payOrderId", payOrderId);
                                intent1.putExtra("payPrice", payPrice);
                                intent1.putExtra("payType", 1);
                                intent1.putExtra("type", 1);
                                startActivity(intent1);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            String result = json.optString("result");
                            initRay(result);
                        }

                    } else if (paytype == 3) {
                        String payString = json.getString("boxPayResBean");
                        PayBean payBean = JSON.parseObject(payString, PayBean.class);
                        paySwipCard(payBean);
                    }

                }
            } else {
                Toast.makeText(getApplicationContext(), "未知错误" + json.optString("msg"), 1).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
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
        // outTradeNo = "out_" + System.currentTimeMillis();

        order.setmPayType(StringUtil.PAY_TYPE_SWIP_CARD);
        try {
            ParcelableMap additionalMap = new ParcelableMap();
            additionalMap.put(ParcelableMap.TRANSACTION_ID, payBean.getTransactionId());
            additionalMap.put(ParcelableMap.ORDER_TIME, payBean.getOrderTime());
            additionalMap.put(ParcelableMap.CALL_BACK_URL, payBean.getNotify_url());
            // 需要参与签名的字段：appCode,partnerId,iboxMchtNo,partnerUserId,transactionId,orderNo(可不传),outTradeNo,tradeNo(没有时传null),transAmount,orderTime,callbackUrl;

            // ****设置打印出来的订单号*****
            additionalMap.put(ParcelableMap.PRINT_ORDER_NO, "交易自定义第三方流水号");
            // 发起交易
            CashboxProxy.getInstance(TopUpPayDialogActivity.this).startTrading(PayType.TYPE_CARD,
                    payBean.getTotal_fee(), payBean.getTradeNo(), payBean.getTransactionId(), SignType.TYPE_MD5,
                    payBean.getSign(), additionalMap, new ITradeCallback() {
                        @Override
                        public void onTradeSuccess(ParcelableMap map) {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            mHandler2.sendEmptyMessage(1);
                            // for (String key : map.getMap().keySet()) {
                            // }
                            // mCbTradeNo = map.get(ParcelableMap.CB_TRADE_NO);
                            // outTradeNo =
                            // map.get(ParcelableMap.PARTNER_TRADE_NO);
                            // mAmount = map.get(ParcelableMap.TRANS_AMOUNT);
                            // mTradeTime = map.get("transaction_time");

                            // *******写入文件******
                            // order.setmCbTradeNo(mCbTradeNo);
                            // order.setmOutTradeNo(outTradeNo);
                            // order.setmAmount(toYuanAmount(mAmount));
                            // order.setmTradeStatus("交易成功");
                            // objToFile.writeObject(order);

                            // ********写入文件结束************
                        }

                        // 设置了不显示盒子签购单时回调此方法。
                        @Override
                        public void onTradeSuccessWithSign(ParcelableMap map, ParcelableBitmap signBitmap) {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            mHandler2.sendEmptyMessage(1);
                            Log.e("&&&&&", "onTradeSuccessWithSign");
                            for (String key : map.getMap().keySet()) {
                            }
                            // mCbTradeNo = map.get(ParcelableMap.CB_TRADE_NO);
                            // outTradeNo =
                            // map.get(ParcelableMap.PARTNER_TRADE_NO);
                            // mAmount = map.get(ParcelableMap.TRANS_AMOUNT);
                            // mTradeTime = map.get("transaction_time");

                            // // *******写入文件******
                            // order.setmCbTradeNo(mCbTradeNo);
                            // order.setmOutTradeNo(outTradeNo);
                            // order.setmAmount(toYuanAmount(mAmount));
                            // order.setmTradeStatus("交易成功");
                            // objToFile.writeObject(order);
                        }

                        @Override
                        public void onTradeFail(ErrorMsg msg) {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            mHandler2.sendEmptyMessage(1);
                            // *******写入文件******
                            // order.setmTradeStatus(msg.getErrorMsg());
                            // order.setmAmount(toYuanAmount(mAmount));
                            // order.setmTradeStatus("交易失败");
                            // order.setmCbTradeNo(mCbTradeNo);
                            // order.setmOutTradeNo(outTradeNo);
                            // objToFile.writeObject(order);
                            // ********写入文件结束************

                        }

                    });
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    private void initRay(final String resultJson) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(TopUpPayDialogActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(resultJson, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void initRay(final ConfirmBean confirmBean) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(TopUpPayDialogActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(confirmBean.getSign(), true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
