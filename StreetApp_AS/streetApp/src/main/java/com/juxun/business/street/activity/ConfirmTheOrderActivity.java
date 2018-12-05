package com.juxun.business.street.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.AuthTask;
import com.juxun.business.street.bean.AddressInfoModel;
import com.juxun.business.street.bean.ConfirmOrderBean;
import com.juxun.business.street.bean.PartnerBean;
import com.juxun.business.street.bean.RedPacketBean;
import com.juxun.business.street.bean.ShopingCartBean;
import com.juxun.business.street.bean.ShopingCartBean2;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.util.alipay.PayResult;
import com.juxun.business.street.widget.RedPackageDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.juxun.business.street.config.Constants.mallSetInfo;

/**
 * @author ConfirmTheOrderActivity 确认下单
 */
public class ConfirmTheOrderActivity extends BaseActivity implements RedPackageDialog.onConfirmListener, OnClickListener {
    private static final int GET_PASS = 101;
    @ViewInject(R.id.button_back)
    private ImageView button_back;// 返回

    @ViewInject(R.id.ll_new)
    private LinearLayout ll_new;// 新增地址
    @ViewInject(R.id.rl_adds)
    private RelativeLayout rl_adds;// 地址详情
    @ViewInject(R.id.tv_username)
    private TextView tv_username;// 收货人
    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;// 电话号码
    @ViewInject(R.id.tv_adds)
    private TextView tv_adds;// 地址

    @ViewInject(R.id.ll_list)
    private LinearLayout ll_list;// 商品列表
    @ViewInject(R.id.ll_red)
    private LinearLayout ll_red;// 红包弹框
    @ViewInject(R.id.tv_heji)
    private TextView tv_heji;// 合计金额
    @ViewInject(R.id.tv_preferential)
    private TextView tv_preferential;// 优惠金额
    @ViewInject(R.id.tv_red)
    private TextView tv_red;//

    @ViewInject(R.id.ll_yue)
    private LinearLayout ll_yue;    //设置条目的背景颜色
    @ViewInject(R.id.tv_can_use)
    private TextView tv_can_use;    //余额
    @ViewInject(R.id.tv_tocharge)
    private TextView tv_tocharge;   //立即充值
    @ViewInject(R.id.cb_yue)
    private CheckBox cb_yue;    //采购余额支付的选中
    @ViewInject(R.id.cb_alipay)
    private CheckBox cb_alipay;// 支付宝支付
    @ViewInject(R.id.cb_cod)
    private CheckBox cb_cod;// 货到付款

    @ViewInject(R.id.btn_place)
    private Button btn_place;// 确认下单

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();
    DecimalFormat df = new java.text.DecimalFormat("0.00");

    List<ShopingCartBean2> suplierList;// 商品列表
    List<RedPacketBean> redPacketBeans;// 红包列表
    private int totalPrice = 0;// 订单总金额
    private int redpacketPrice = 0;// 使用红包金额
    private int redID = -1;// 红包ID
    private int payType = 6;//  1.微信支付.2.支付宝支付 3.pos 4货到付款 5白条支付 6余额支付
    private AddressInfoModel addressInfoModel;// 地址对象  //
    private String commodityJson;// 商品对象
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

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
     * 返回的交易时间
     */
    private String mTradeTime;
    private String out_trade_no;
    private long startTime;

    //支付宝支付授权
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
                        Tools.showToast(ConfirmTheOrderActivity.this, "下单成功");
                        Tools.jump(ConfirmTheOrderActivity.this, OrderActivity.class, true);
                        Tools.webexit();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getApplicationContext(), "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Tools.jump(ConfirmTheOrderActivity.this, OrderActivity.class, true);
                            Tools.webexit();
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(ConfirmTheOrderActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }
    };

    private Handler mHandler2 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    Tools.jump(ConfirmTheOrderActivity.this, OrderActivity.class, true);
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
    };

    private AlertDialog mAlertDialog;
    private int create_type = 0; // 0购物车，1直接下单
    private String commodity_ids;
    private int remaining_balance;
    private String pay_pass;
    private Object resultJson;
    private ConfirmOrderBean confirmOrderBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_confirm_order);
        ViewUtils.inject(this);
        Tools.webacts.add(this);

        drawRedPacketList();    //获取红包列表
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        findAddress();
        mallSetInfo();
    }

    /* 获取店铺详情 */
    private void mallSetInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", mSavePreferencesData.getStringData("auth_token"));
        mQueue.add(ParamTools.packParam(mallSetInfo, this, this, map));
    }

    private void initView() {
        payType = 6;
        cb_yue.setChecked(true);    //默认的余额支付
        tv_tocharge.setOnClickListener(this);
        //供应链商品
        Intent intent = getIntent();
        suplierList = (List<ShopingCartBean2>) intent.getSerializableExtra("suplierList");
        create_type = intent.getIntExtra("create_type", 0);

        redPacketBeans = new ArrayList<>();
        ll_red.setOnClickListener(this);
        ll_new.setOnClickListener(this);
        rl_adds.setOnClickListener(this);
        cb_yue.setOnClickListener(this);
        cb_alipay.setOnClickListener(this);
        cb_cod.setOnClickListener(this);
        btn_place.setOnClickListener(this);
        button_back.setOnClickListener(this);

        commodity_ids = ""; //需要上传的对象

        if (suplierList != null && suplierList.size() > 0) {
            for (int i = 0; i < suplierList.size(); i++) {
                View lview = LayoutInflater.from(this).inflate(R.layout.oder_item3, null);
                TextView tv_name = (TextView) lview.findViewById(R.id.tv_name);
                TextView tv_purchase_quantity = (TextView) lview.findViewById(R.id.tv_purchase_quantity);
                TextView tv_total_price = (TextView) lview.findViewById(R.id.tv_total_price);
                TextView tv_price = (TextView) lview.findViewById(R.id.tv_price);
                ImageView imageView = (ImageView) lview.findViewById(R.id.iv_img);

                ShopingCartBean2 msgmodel = suplierList.get(i);
                String[] cover = msgmodel.getCommodity_icon().split(",");
                imageLoader.displayImage(Constants.imageUrl + cover[0], imageView, options);
                tv_name.setText(msgmodel.getCommodity_name());
                tv_purchase_quantity.setText("x" + msgmodel.getMsg_count());
                tv_price.setText("¥" + Tools.getFenYuan(msgmodel.getPrice_high()));
                tv_total_price.setText("小计：¥" + Tools.getFenYuan(msgmodel.getMsg_count() * msgmodel.getPrice_high()));
                ll_list.addView(lview);
                totalPrice = (totalPrice + msgmodel.getMsg_count() * msgmodel.getPrice_high());
                if (i == 0) {
                    commodity_ids = msgmodel.getId() + "";
                } else {
                    commodity_ids = commodity_ids + "," + msgmodel.getId();
                }
            }
        }
        commodityJson = JSON.toJSONString(suplierList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tocharge:  //立即充值
                Tools.jump(this, TopUpActivity.class, false);
                break;
            case R.id.ll_red:
                RedPackageDialog redPackageDialog = new RedPackageDialog(ConfirmTheOrderActivity.this,
                        ConfirmTheOrderActivity.this, redPacketBeans, totalPrice);
                redPackageDialog.showAtLocation(tv_phone, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.ll_new:
                Tools.jump(ConfirmTheOrderActivity.this, AddAddressActivity.class, false);
                break;
            case R.id.ll_adds:
                Intent intent = new Intent(ConfirmTheOrderActivity.this, AddressListActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.cb_yue:   //余额支付
                cb_yue.setChecked(true);
                cb_alipay.setChecked(false);
                cb_cod.setChecked(false);
                payType = 6;
                break;
            case R.id.cb_alipay:    //支付宝支付
                cb_yue.setChecked(false);
                cb_alipay.setChecked(true);
                cb_cod.setChecked(false);
                payType = 2;
                break;
            case R.id.cb_cod:
                cb_yue.setChecked(false);
                cb_alipay.setChecked(false);
                cb_cod.setChecked(true);
                payType = 4;
                break;
            case R.id.rl_adds:
                Intent intent2 = new Intent(ConfirmTheOrderActivity.this, AddressListActivity.class);
                intent2.putExtra("type", 1);
                startActivity(intent2);
                break;
            case R.id.btn_place:    //确认下单操作
                confirmPlaceAnOrder();
                break;
            case R.id.button_back:
                finish();
                break;
            default:
                break;
        }
    }

    // 确认下单：核对地址、核对支付方式
    private void confirmPlaceAnOrder() {
        if (addressInfoModel == null) {
            Tools.showToast(getApplicationContext(), "地址未填写");
            return;
        }
        if (cb_yue.isChecked()) {   //余额支付的情况
            if (remaining_balance < totalPrice) { //余额不足的情况
                //余额不足，进行提示
                showDialog();
                return;
            } else {    //有钱的情况、去获取密码
                Intent intent = new Intent(this, VerifyPaymentPasswordActivity.class);
                startActivityForResult(intent, GET_PASS);
            }
        } else {    //支付宝支付、货到付款的情况
            checkfirmPlaceAnOrderHttp();
        }
    }

    private void checkfirmPlaceAnOrderHttp() {
        Map<String, String> map = new HashMap<>();
        map.put("create_type", create_type + "");
        map.put("commodity_ids", commodity_ids); //使用","进行隔断，上传的商品id们
        if (create_type == 1) { //1是直接下单的操作
            map.put("msg_count", suplierList.get(0).getMsg_count() + "");   //msg_count
        }
        map.put("address_id", addressInfoModel.getId() + "");   //地址的id
        map.put("auth_token", mSavePreferencesData.getStringData("auth_token"));
        if (redID == -1) {  //获取到的红包id
            map.put("red_id", "0");
        } else {
            map.put("red_id", redID + "");
        }
        mQueue.add(ParamTools.packParam(Constants.confirmPlaceAnOrder, this, this, map));
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

    // 获取默认地址
    private void findAddress() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", mSavePreferencesData.getStringData("auth_token"));
        mQueue.add(ParamTools.packParam(Constants.findAddress, this, this, map));
        loading();
    }

    // 获取红包列表
    private void drawRedPacketList() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", mSavePreferencesData.getStringData("auth_token"));
        map.put("state", "1");  //都是查询可用的红包
        map.put("pageNumber", 1 + "");
        map.put("pageSize", 100000 + "");
        mQueue.add(ParamTools.packParam(Constants.drawRedPacketList, this, this, map));
    }

    // 初始化匹配红包
    private void initRed() {
        Collections.sort(redPacketBeans, new Comparator<RedPacketBean>() {
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
        });
        Boolean is = true;
        for (int i = 0; i < redPacketBeans.size(); i++) {
            // 如果订单金额 大于等于 满减金额
            if (totalPrice >= redPacketBeans.get(i).getFull_price()) {
                // 选取金额最大的那个
                if (is) {
                    redpacketPrice = redPacketBeans.get(i).getRedpacket_price();
                    redID = redPacketBeans.get(i).getActivity_id();
                    redPacketBeans.get(i).setState(2);
                    is = false;
                }
                redPacketBeans.get(i).setState(1);
            }
        }
        initPrice(is);
    }

    private void initPrice(Boolean is) {
        int thisTotalPrice = 0;
        for (int i = 0; i < suplierList.size(); i++) {
            thisTotalPrice = thisTotalPrice + suplierList.get(i).getMsg_count() * suplierList.get(i).getPrice_high();
        }
        tv_heji.setText("¥" + Tools.getFenYuan(thisTotalPrice - redpacketPrice));

        if (redpacketPrice == 0) {
            tv_preferential.setVisibility(View.GONE);
        } else {
            tv_preferential.setVisibility(View.VISIBLE);
            tv_preferential.setText("(已抵扣" + redpacketPrice + "元)");
        }
        if (is) {
            ll_red.setClickable(false);
            tv_red.setText("没有可用红包");
        } else {
            if (redID == 0) {
                tv_red.setText("不使用红包");
                tv_red.setTextColor(getResources().getColor(R.color.black));
            } else {
                tv_red.setText(redpacketPrice + "元");
                tv_red.setTextColor(getResources().getColor(R.color.red));
            }
        }
    }

    //发起支付
    private void orderPay() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", mSavePreferencesData.getStringData("auth_token"));
        map.put("order_id", confirmOrderBean.getId() + "");
        if (payType == 6) { //余额支付、需要输入支付密码
            map.put("pay_pass", pay_pass);
        } else {
            map.put("pay_pass", "");
        }
        map.put("pay_type", payType + "");

        mQueue.add(ParamTools.packParam(Constants.orderPay, this, this, map));
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.optInt("status");
            String msg = json.optString("msg");
            if (status == 0) {
                resultJson = json.optJSONObject("result");
                if (url.contains(Constants.findAddress)) {
                    if (resultJson == null) {
                        ll_new.setVisibility(View.VISIBLE);
                        rl_adds.setVisibility(View.GONE);
                    } else {
                        ll_new.setVisibility(View.GONE);
                        rl_adds.setVisibility(View.VISIBLE);
                        addressInfoModel = JSON.parseObject(String.valueOf(resultJson), AddressInfoModel.class);
                        tv_username.setText(addressInfoModel.getUser_name());
                        tv_phone.setText(addressInfoModel.getTel());
                        tv_adds.setText(addressInfoModel.getArea_name() + addressInfoModel.getAddress());
                    }
                } else if (url.contains(Constants.drawRedPacketList)) {
                    if (resultJson != null) {
                        redPacketBeans = JSON.parseArray(String.valueOf(resultJson), RedPacketBean.class);
                    } else {
                        redPacketBeans = new ArrayList<>();
                    }
                    initRed();  //获取红包列表，匹配是否可以使用
                } else if (url.contains(Constants.mallSetInfo)) {
                    partnerBean = JSON.parseObject(json.optString("result"), PartnerBean.class);
                    mSavePreferencesData.putStringData("mallSet", json.optString("result"));

                    remaining_balance = partnerBean.getRemaining_balance();
                    tv_can_use.setText("可用余额¥" + mDf.format(Tools.getFenYuan(remaining_balance)));
                } else if (url.contains(Constants.confirmPlaceAnOrder)) {   //生成订单
                    confirmOrderBean = JSON.parseObject(String.valueOf(resultJson), ConfirmOrderBean.class);
//                    if (payType == 4) { //货到付款的直接属于支付成功,不需要下一步发起支付
//                        Tools.showToast(ConfirmTheOrderActivity.this, "下单成功");
//                        Tools.jump(ConfirmTheOrderActivity.this, OrderActivity.class, true);
//                        Tools.webexit();
//                    } else {    //余额支付的
                    orderPay();
                    //  }
                } else if (url.contains(Constants.orderPay)) {
                    if (payType == 6 || payType == 4) {  //余额支付
                        //余额支付:订单创建成功了，
                        Tools.showToast(ConfirmTheOrderActivity.this, "下单成功");
                        Tools.jump(ConfirmTheOrderActivity.this, OrderActivity.class, true);
                        Tools.webexit();
                    } else {  //支付宝付款
                        initRay(json.optString("result"));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == GET_PASS) {
                pay_pass = data.getStringExtra("pay_pass");
                checkfirmPlaceAnOrderHttp();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfirm(int id) {
        for (int i = 0; i < redPacketBeans.size(); i++) {
            redPacketBeans.get(i).setState(2);
        }
        if (id == -1) {
            // 不使用红包
            redpacketPrice = 0;
            redID = 0;
        } else {
            // 使用红包
            redpacketPrice = redPacketBeans.get(id).getRedpacket_price();
            redPacketBeans.get(id).setState(1);
            redID = redPacketBeans.get(id).getAgency_id();
        }
        initPrice(false);
    }

    private void initRay(final String sign) {
//        String[] split = resultJson.split("&");
//        String splitStr = split[split.length - 2];
//        final String substring = splitStr.substring(6, splitStr.length() - 1);

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(ConfirmTheOrderActivity.this);
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
