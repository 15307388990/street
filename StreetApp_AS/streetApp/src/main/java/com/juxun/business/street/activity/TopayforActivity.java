package com.juxun.business.street.activity;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.AuthTask;
import com.iboxpay.cashbox.minisdk.CashboxProxy;
import com.iboxpay.cashbox.minisdk.PayType;
import com.iboxpay.cashbox.minisdk.SignType;
import com.iboxpay.cashbox.minisdk.callback.IFetchCardInfoCallback;
import com.iboxpay.cashbox.minisdk.callback.ITradeCallback;
import com.iboxpay.cashbox.minisdk.exception.ConfigErrorException;
import com.iboxpay.cashbox.minisdk.model.ErrorMsg;
import com.iboxpay.cashbox.minisdk.model.ParcelableBitmap;
import com.iboxpay.cashbox.minisdk.model.ParcelableMap;
import com.iboxpay.cashbox.minisdk.model.PrintPreference;
import com.juxun.business.street.bean.ConfirmBean;
import com.juxun.business.street.bean.Order;
import com.juxun.business.street.bean.PayBean;
import com.juxun.business.street.bean.PurchaseOderBean;
import com.juxun.business.street.bean.RedPacketBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.ObjToFile;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.StringUtil;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.util.alipay.PayResult;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

//import com.alipay.sdk.app.PayTask;

/**
 * @author 重新选择支付方式
 */
public class TopayforActivity extends BaseActivity {
    @ViewInject(R.id.button_back)
    private ImageView button_back;// 返回
    @ViewInject(R.id.tv_heji)
    private TextView tv_heji;// 合计金额
    @ViewInject(R.id.cb_alipay)
    private CheckBox cb_alipay;// 支付宝支付
    @ViewInject(R.id.cb_wechat)
    private CheckBox cb_wechat;// 微信支付
    @ViewInject(R.id.ll_wechat)
    private LinearLayout ll_wechat;// 微信支付
    @ViewInject(R.id.cb_cod)
    private CheckBox cb_cod;// 货到付款
    @ViewInject(R.id.cb_unionpay)
    private CheckBox cb_unionpay;// 银联支付
    @ViewInject(R.id.cb_remaining_balance)
    private CheckBox cb_remaining_balance;

    @ViewInject(R.id.ll_unionpay)
    private LinearLayout ll_unionpay;// 银联支付
    @ViewInject(R.id.btn_zhifu)
    private Button btn_zhifu;// 确认下单
    @ViewInject(R.id.tv_v1)
    private TextView tv_v1;
    @ViewInject(R.id.tv_v2)
    private TextView tv_v2;
    @ViewInject(R.id.tv_totalPrice)
    private TextView tv_totalPrice;
    @ViewInject(R.id.tv_remaining_balance)
    private TextView tv_remaining_balance;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();
    DecimalFormat df = new java.text.DecimalFormat("0.00");
    private int payType = 4;// 支付类型.1微信支付。2支付宝支付,3pos,4货到付款",
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private Order order;
    /**
     * 您的订单号
     */
    private String outTradeNo;
    // 订单存储
    private ObjToFile objToFile;
    /**
     * 钱盒交易订单号
     */
    private String mCbTradeNo;
    /**
     * 返回的交易金额
     */
    private String mAmount;
    /**
     * 返回的交易时间
     */
    private String mTradeTime;
    private String out_trade_no;
    private long startTime;
    private int totalPrice;
    private AlertDialog mAlertDialog;

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
                        Tools.webexit();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getApplicationContext(), "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Tools.webexit();
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(TopayforActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
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
                    Tools.webexit();
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
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_to_payfor);
        ViewUtils.inject(this);
        Tools.webacts.add(this);
        id = getIntent().getIntExtra("id", 0);
        totalPrice = getIntent().getIntExtra("totalPrice", 0);
        initView();
        if (!partnerBean.isSn()) {
            ll_wechat.setVisibility(View.GONE);
            ll_unionpay.setVisibility(View.GONE);
            tv_v1.setVisibility(View.GONE);
            tv_v2.setVisibility(View.GONE);
        }

    }

    private void initView() {
        tv_totalPrice.setText("¥" + Tools.getFenYuan(totalPrice));
        tv_remaining_balance.setText("可以用余额" + Tools.getFenYuan(partnerBean.getRemaining_balance()));
        cb_alipay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cb_alipay.setChecked(true);
                cb_wechat.setChecked(false);
                cb_cod.setChecked(false);
                cb_unionpay.setChecked(false);
                cb_remaining_balance.setChecked(false);
                payType = 2;
            }
        });
        cb_wechat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cb_alipay.setChecked(false);
                cb_wechat.setChecked(true);
                cb_cod.setChecked(false);
                cb_unionpay.setChecked(false);
                cb_remaining_balance.setChecked(false);
                payType = 1;
            }
        });
        cb_cod.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cb_alipay.setChecked(false);
                cb_wechat.setChecked(false);
                cb_cod.setChecked(true);
                cb_unionpay.setChecked(false);
                cb_remaining_balance.setChecked(false);
                payType = 4;
            }
        });
        cb_unionpay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cb_alipay.setChecked(false);
                cb_wechat.setChecked(false);
                cb_cod.setChecked(false);
                cb_unionpay.setChecked(true);
                cb_remaining_balance.setChecked(false);
                payType = 3;
            }
        });
        cb_remaining_balance.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_alipay.setChecked(false);
                cb_wechat.setChecked(false);
                cb_cod.setChecked(false);
                cb_unionpay.setChecked(false);
                cb_remaining_balance.setChecked(true);
                payType = 6;
            }
        });
        button_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_zhifu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (payType == 6) {
                    if (partnerBean.getRemaining_balance() < totalPrice) { //余额不足的情况
                        //余额不足，进行提示
                        showDialog();
                        return;
                    } else {    //有钱的情况、去获取密码
                        Intent intent = new Intent(TopayforActivity.this, VerifyPaymentPasswordActivity.class);
                        startActivityForResult(intent, 1);
                    }
                } else {
                    againPay("");
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                String pay_pass = data.getStringExtra("pay_pass");
                againPay(pay_pass);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //提示没钱
    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 控件初始化
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_store_acts, null);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_content.setText("您的余额不足、\n请立即充值或选择其它支付方式");

        LinearLayout ll_two = (LinearLayout) view.findViewById(R.id.ll_two);
        ll_two.setVisibility(View.GONE);
        TextView tv_iknow = (TextView) view.findViewById(R.id.tv_iknow);
        tv_iknow.setVisibility(View.VISIBLE);
        builder.setView(view);

        mAlertDialog = builder.create();
        mAlertDialog.show();

        tv_iknow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    // 获取默认地址
    private void againPay(String pay_pass) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("order_id", id + "");
        map.put("pay_type", payType + "");//支付类型 支付方式 1.微信支付.2.支付宝支付 3.pos 4货到付款 5白条支付 6余额支付
        map.put("pay_pass", pay_pass);
        map.put("auth_token", partnerBean.getAuth_token());
        if (partnerBean.isSn()) {
            map.put("version", "pos");
        }
        mQueue.add(ParamTools.packParam(Constants.againPay, this, this, map));
        loading();
    }

    class RedCom implements Comparator<RedPacketBean> {

        @Override
        public int compare(RedPacketBean lhs, RedPacketBean rhs) {
            double sdto1 = lhs.getRedpacket_price();

            double sdto2 = rhs.getRedpacket_price();
            if (sdto1 < sdto2) {
                return 1;
            } else if (sdto1 == sdto2) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    @Override
    public void onResponse(String response, String url) {
        // TODO Auto-generated method stub
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            String msg = json.optString("msg");
            if (stauts == 0) {
                if (url.contains(Constants.againPay)) {
                    if (payType == 1) {
                        JSONObject resultJson = json.optJSONObject("result");
                        String codeUrl = resultJson.getString("qr_code");
                        String payOrderId = resultJson.getString("payOrderId");
                        Intent intent = new Intent(this, PayActivity.class);
                        intent.putExtra("pay", 0);
                        intent.putExtra("codeUrl", codeUrl);
                        intent.putExtra("payOrderId", payOrderId);
                        intent.putExtra("count", totalPrice + "");
                        startActivity(intent);

                    } else if (payType == 2) {
                        if (partnerBean.isSn()) {
                            JSONObject resultJson = json.optJSONObject("result");
                            String codeUrl = resultJson.getString("qr_code");
                            String payOrderId = resultJson.getString("payOrderId");
                            Intent intent1 = new Intent(this, PayActivity.class);
                            intent1.putExtra("codeUrl", codeUrl);
                            intent1.putExtra("payOrderId", payOrderId);
                            intent1.putExtra("count", totalPrice + "");
                            intent1.putExtra("pay", 1);
                            startActivity(intent1);
                        } else {
                            String resultJson = json.optString("result");
                            // ConfirmBean confirmBean = JSON.parseObject(resultJson, ConfirmBean.class);
                            initRay(resultJson);
                        }
                    } else if (payType == 3) {
                        JSONObject resultJson = json.optJSONObject("result");
                        String payString = resultJson.getString("boxPayResBean");
                        PayBean payBean = JSON.parseObject(payString, PayBean.class);
                        paySwipCard(payBean);
                    } else if (payType == 4) {
                        Tools.showToast(TopayforActivity.this, "下单成功");
                        Tools.jump(TopayforActivity.this, OrderActivity.class, true);
                        Tools.webexit();
                    } else if (payType == 6) {
                        Tools.showToast(TopayforActivity.this, "下单成功");
                        Tools.jump(TopayforActivity.this, OrderActivity.class, true);
                        Tools.webexit();
                    }

                }

            } else {
                Tools.showToast(this, msg);
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
            CashboxProxy.getInstance(TopayforActivity.this).startTrading(PayType.TYPE_CARD, payBean.getTotal_fee(),
                    payBean.getTradeNo(), payBean.getTransactionId(), SignType.TYPE_MD5, payBean.getSign(),
                    additionalMap, new ITradeCallback() {
                        @Override
                        public void onTradeSuccess(ParcelableMap map) {
                            mHandler2.sendEmptyMessage(1);
                            for (String key : map.getMap().keySet()) {
                            }
                            mCbTradeNo = map.get(ParcelableMap.CB_TRADE_NO);
                            outTradeNo = map.get(ParcelableMap.PARTNER_TRADE_NO);
                            mAmount = map.get(ParcelableMap.TRANS_AMOUNT);
                            mTradeTime = map.get("transaction_time");

                            // *******写入文件******
//							order.setmCbTradeNo(mCbTradeNo);
//							order.setmOutTradeNo(outTradeNo);
//							order.setmAmount(toYuanAmount(mAmount));
//							order.setmTradeStatus("交易成功");
//							objToFile.writeObject(order);
                            // ********写入文件结束************
                        }

                        // 设置了不显示盒子签购单时回调此方法。
                        @Override
                        public void onTradeSuccessWithSign(ParcelableMap map, ParcelableBitmap signBitmap) {
                            Log.e("&&&&&", "onTradeSuccessWithSign");
                            mHandler2.sendEmptyMessage(1);
                            for (String key : map.getMap().keySet()) {
                            }
                            mCbTradeNo = map.get(ParcelableMap.CB_TRADE_NO);
                            outTradeNo = map.get(ParcelableMap.PARTNER_TRADE_NO);
                            mAmount = map.get(ParcelableMap.TRANS_AMOUNT);
                            mTradeTime = map.get("transaction_time");

                            // *******写入文件******
//							order.setmCbTradeNo(mCbTradeNo);
//							order.setmOutTradeNo(outTradeNo);
//							order.setmAmount(toYuanAmount(mAmount));
//							order.setmTradeStatus("交易成功");
//							objToFile.writeObject(order);

                        }

                        @Override
                        public void onTradeFail(ErrorMsg msg) {
                            mHandler2.sendEmptyMessage(1);

                            // *******写入文件******
//							order.setmTradeStatus(msg.getErrorMsg());
//							order.setmAmount(toYuanAmount(mAmount));
//							order.setmTradeStatus("交易失败");
//							order.setmCbTradeNo(mCbTradeNo);
//							order.setmOutTradeNo(outTradeNo);
//							objToFile.writeObject(order);
                            // ********写入文件结束************
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        }

                    });
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    private String toYuanAmount(String transAmount) {
        if (transAmount != null) {
            double amountDouble = Double.parseDouble(transAmount) / 100;
            return String.valueOf(amountDouble);
        }
        return "";
    }

    private void initRay(final String sign) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(TopayforActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(String.valueOf(sign), true);

                //调用结果传递上去
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
