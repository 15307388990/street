/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：Street 类名称：PayActivity 类描述： 支付页面
 */
public class PayActivity extends BaseActivity {

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

    private int pay;
    private String count;
    private long startTime;
    private String orderIdStr;
    private MediaPlayer mediaPlayer;
    private static final float BEEP_VOLUME = 0.10f;
    private Boolean isClose = true;
    //从哪个页面跳转的
    private int tab;
    private Integer countInt;
    private String mAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ViewUtils.inject(this);
        initTitle();

        title.setText(R.string.receive_money);

        Intent intent = getIntent();
        pay = intent.getIntExtra("pay", 0);    //1支付宝、2微信支付
        tab = intent.getIntExtra("tab", 0);
        mAmount = intent.getStringExtra("mAmount");
        count = intent.getStringExtra("count");

        setData();
        codePay();
    }

    /**
     * 设置数据
     */
    public void setData() {
        //付款金额
        payMoney.setText("¥" + count);
        //支付方式
        if (pay == 2) {
            payMethodTips.setText(getString(R.string.weixinpay_tips));
            payMethod.setText(getString(R.string.weixin_pay));
        } else {
            payMethodTips.setText(getString(R.string.alipay_tips));
            payMethod.setText(getString(R.string.ali_pay));
        }
        //交易时间
        orderTime.setText(Tools.obtainDateNow());
    }

    // 获取数据：生成二维码的接口？
    public void codePay() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("order_pay_type", pay + "");
        map.put("pay_price", mAmount);
        if (mSavePreferencesData.getBooleanData("isSn")) {
            map.put("order_source", "4");
        } else {
            map.put("order_source", "1");
        }
        mQueue.add(ParamTools.packParam(Constants.codePay, this, this, map));
        loading();
    }

    /**
     * 检测状态
     */
    public void orderQuery() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("order_num", orderIdStr);
        mQueue.add(ParamTools.packParam(Constants.orderQuery, this, this, map));
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            String msg = json.optString("msg");
            if (stauts == 0) {
                if (url.contains(Constants.codePay)) {
                    orderIdStr = json.optString("order_id");
                    orderId.setText(orderIdStr);
                    qrcodeView.setImageBitmap(BitmapUtil.createQRImage(json.optString("qr_code"), 400, 400));
                    startTime = System.currentTimeMillis();
                    orderQuery();
                } else if (url.contains(Constants.orderQuery)) {
                    //支付成功的数据
                    GiveOrder giveOrder = JSON.parseObject(json.optString("result"), GiveOrder.class);
                    if (giveOrder.getOrder_state() == 1) {  //付款成功
                        Intent intent = new Intent(PayActivity.this, PaySuccessActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("give", giveOrder);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        this.finish();
                        isClose = false;
                    } else {
                        if (System.currentTimeMillis() - startTime < 5 * 60 * 1000) {
                            orderQuery();
                        }
                        return;
                    }
                }
            } else if (stauts == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(this, LoginActivity.class, true);
                Tools.showToast(this, "登录过期请重新登录");
                Tools.exit();
            } else {
                if (url.contains(Constants.orderPay)) {
                    Tools.showToast(this, "订单生成失败");
                }
                if (url.contains(Constants.lineOrderState)) {
                    if (System.currentTimeMillis() - startTime < 5 * 60 * 1000) {
                        orderQuery();
                    }
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tab != 666) {
            if (isClose) {
                Tools.jump(PayActivity.this, OrderActivity.class, true);
                finish();
            }
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

    private void initBeepSound() {
        if (mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    protected void onStop() {
        mQueue.stop();
        super.onStop();
    }
}
