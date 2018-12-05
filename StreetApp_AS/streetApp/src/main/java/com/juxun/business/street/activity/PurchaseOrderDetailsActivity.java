package com.juxun.business.street.activity;


import java.text.DecimalFormat;
/**
 * 供应商订单详情
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.AuthTask;
import com.iboxpay.print.PrintManager;
import com.juxun.business.street.adapter.MsgmodelsAdapter;
import com.juxun.business.street.bean.Agreement7;
import com.juxun.business.street.bean.ConfirmBean;
import com.juxun.business.street.bean.ShopStaBean;
import com.juxun.business.street.bean.ShopingBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.PurchaseOderBean;
import com.juxun.business.street.bean.ShopingCartBean;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.util.alipay.PayResult;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PurchaseOrderDetailsActivity extends BaseActivity implements OnClickListener {
    /**
     * ----------------------- 商品清单--------------------------------
     **/
    @ViewInject(R.id.ll_list)
    private LinearLayout ll_list;// 商品列表
    @ViewInject(R.id.tv_preferential)
    private TextView tv_preferential;// 优惠金额
    @ViewInject(R.id.tv_heji)
    private TextView tv_heji;// 总价 红色的
    /**
     * ----------------------- 订单信息--------------------------------
     **/
    @ViewInject(R.id.tv_jiaoyi_number)
    private TextView tv_jiaoyi_number;// 订单编号
    @ViewInject(R.id.tv_state)
    private TextView tv_state;// 订单状态
    @ViewInject(R.id.tv_ordercreateTime)
    private TextView tv_ordercreateTime;// 下单时间
    @ViewInject(R.id.tv_order_end_date)
    private TextView tv_order_end_date;// 付款时间
    @ViewInject(R.id.tv_delivery_date)
    private TextView tv_delivery_date;// 发货时间
    @ViewInject(R.id.tv_deal_date)
    private TextView tv_deal_date;// 成交时间
    @ViewInject(R.id.tv_orde_type)
    private TextView tv_orde_type;// 付款方式
    /**
     * -----------------------收货信息--------------------------------
     **/
    @ViewInject(R.id.tv_name)
    private TextView tv_name;// 收货人
    @ViewInject(R.id.tv_ipone)
    private TextView tv_ipone;// 联系电话
    @ViewInject(R.id.tv_adds)
    private TextView tv_adds;// 收货地址
    @ViewInject(R.id.tv_order_price)
    private TextView tv_order_price;// 合计
    @ViewInject(R.id.btn_one)
    private Button btn_one;// 按钮
    @ViewInject(R.id.btn_two)
    private Button btn_two;// 按钮2
    @ViewInject(R.id.button_back)
    private ImageView button_back;// 返回
    @ViewInject(R.id.tv_transaction_number)
    private TextView tv_transaction_number;// 交易单号

    @ViewInject(R.id.ll_liyou)
    private LinearLayout ll_liyou;
    @ViewInject(R.id.tv_liyou)
    private TextView tv_liyou;
    @ViewInject(R.id.tv_beizhu)
    private TextView tv_beizhu;
    /**
     * 初始化数据 状态 订单金额 运费 收件人 手机 地址
     */
    MsgmodelsAdapter msgmodelsAdapter;
    private PrintManager mPrintManager;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();
    private PurchaseOderBean mPurchaseOderBean;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    DecimalFormat df = new java.text.DecimalFormat("0.00");

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
                        finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getApplicationContext(), "支付结果确认中", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
                            // 支付失败跳转至我的订单
                            finish();
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(PurchaseOrderDetailsActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.purchase_order_details);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initView();
    }

    public void initView() {
        tv_jiaoyi_number = (TextView) findViewById(R.id.tv_jiaoyi_number);
        tv_ordercreateTime = (TextView) findViewById(R.id.tv_ordercreateTime);
        tv_order_end_date = (TextView) findViewById(R.id.tv_order_end_date);
        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        button_back.setOnClickListener(this);
        mPurchaseOderBean = (PurchaseOderBean) getIntent().getSerializableExtra("mPurchaseOderBean");
        initDate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //findOrder();
    }


    private void initDate() {
        initStata();
        String spec = mPurchaseOderBean.getCommodity_json();
        List<ShopingBean> suplierList = suplierList = JSON.parseArray(spec, ShopingBean.class);
        ll_list.removeAllViews();
        if (suplierList != null && suplierList.size() > 0) {
            for (int i = 0; i < suplierList.size(); i++) {
                View lview = LayoutInflater.from(this).inflate(R.layout.oder_item3, null);
                TextView tv_name = (TextView) lview.findViewById(R.id.tv_name);
                TextView tv_purchase_quantity = (TextView) lview.findViewById(R.id.tv_purchase_quantity);
                TextView tv_total_price = (TextView) lview.findViewById(R.id.tv_total_price);
                TextView tv_price = (TextView) lview.findViewById(R.id.tv_price);
                ImageView imageView = (ImageView) lview.findViewById(R.id.iv_img);
                ShopingBean msgmodel = suplierList.get(i);
                String[] cover = msgmodel.getCommodity_icon().split(",");
                imageLoader.displayImage(Constants.imageUrl + cover[0], imageView, options);
                tv_name.setText(msgmodel.getCommodity_name());
                tv_purchase_quantity.setText("x" + msgmodel.getMsg_count());
                tv_total_price.setText("小计：¥" + Tools.getFenYuan(msgmodel.getPrice_high() * msgmodel.getMsg_count()));
                tv_price.setText("¥" + Tools.getFenYuan(msgmodel.getPrice_high()));
                ll_list.addView(lview);
            }
        }

        if (mPurchaseOderBean.getBuyer_remark() != null) {
            ll_liyou.setVisibility(View.VISIBLE);
            if (mPurchaseOderBean.getAbolish() != null) {
                tv_liyou.setText("取消理由：" + mPurchaseOderBean.getAbolish());
            } else {
                tv_liyou.setText("取消理由：未填");
            }

            tv_beizhu.setText("备注信息：" + mPurchaseOderBean.getBuyer_remark());

        }
        // 订单状态【1.待付款,2.待发货,3.已发货,4.交易完成,5.已取消,6.已退货】
        //1.待付款,2.待发货,3.已发货,4.交易完成,5.已取消,6.已退货,7退款中8退款失败
        switch (mPurchaseOderBean.getOrder_state()) {
            case 1:
                tv_state.setText("待付款");
                break;
            case 2:
                tv_state.setText("待发货");
                break;
            case 3:
                tv_state.setText("已发货");
                break;
            case 4:
                tv_state.setText("交易完成");
                break;
            case 5:
                tv_state.setText("已取消");
                break;
            case 6:
                tv_state.setText("已退货");
                break;
            default:
                break;
        }
        ////1.微信支付.2.支付宝支付 3.pos 4货到付款   5白条支付 6余额支付
        switch (mPurchaseOderBean.getPay_type()) {
            case 1:
                tv_orde_type.setText("支付方式：微信支付");
                break;
            case 2:
                tv_orde_type.setText("支付方式：支付宝支付");
                break;
            case 3:
                tv_orde_type.setText("支付方式：pos支付");
                break;
            case 4:
                tv_orde_type.setText("支付方式：货到付款");
                break;
            case 5:
                tv_orde_type.setText("支付方式：白条支付");
                break;
            case 6:
                tv_orde_type.setText("支付方式：余额支付");
                break;
            default:
                break;
        }

        tv_heji.setText("¥" + Tools.getFenYuan(mPurchaseOderBean.getTotal_price()));
        tv_jiaoyi_number.setText("订单编号：" + mPurchaseOderBean.getOrder_num());
        tv_ordercreateTime.setText("下单时间：" + Tools.getDateformat2(mPurchaseOderBean.getCreate_date()));
        if (mPurchaseOderBean.getPay_end_time() != null) {
            tv_order_end_date.setText("付款时间：" + mPurchaseOderBean.getPay_end_time().toString());
        } else {
            tv_order_end_date.setText("付款时间：");
        }
        if (mPurchaseOderBean.getReal_delivery_date() != null) {
            tv_delivery_date.setText("发货时间：" + Tools.getDateformat2(Long.valueOf(mPurchaseOderBean.getReal_delivery_date().toString())));
        } else {
            tv_delivery_date.setText("发货时间：");
        }
        if (mPurchaseOderBean.getComplete_date() != null) {
            tv_deal_date.setText("完成时间：" + mPurchaseOderBean.getPay_end_time().toString());
        } else {
            tv_deal_date.setText("完成时间：");
        }

        tv_name.setText(mPurchaseOderBean.getConsignee_name());
        tv_ipone.setText(mPurchaseOderBean.getConsignee_phone());
        tv_adds.setText(mPurchaseOderBean.getAddress());
        // 如果有红包抵扣金额 则显示否则不显示
        if (mPurchaseOderBean.getRedpacket_price() > 0) {
            tv_preferential.setVisibility(View.VISIBLE);
            tv_preferential.setText("(已抵扣：" + mPurchaseOderBean.getRedpacket_price() / 100 + "元)");
        } else {
            tv_preferential.setVisibility(View.GONE);
        }
        tv_transaction_number.setText("交易单号：" + mPurchaseOderBean.getOrder_num());
    }

    // 取消订单
    private void modifyOrderState(int id, int state) {
        Map<String, String> map = new HashMap<String, String>();
        // agencyId
        // orderState 0全部1待付款2待收货3已收货
        // pager.pagerNumber
        // pager.pagerCount
        map.put("order_id", id + "");
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.supplierOrderComplete, this, this, map));
    }

    // 删除订单
    private void deleteOrder() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("order_id", mPurchaseOderBean.getId() + "");
        mQueue.add(ParamTools.packParam(Constants.deleteOrder, this, this, map));
    }

    private void initStata() {
        switch (mPurchaseOderBean.getOrder_state()) {
            case 1:
                btn_one.setText("取消订单");
                btn_two.setText("立即支付");
                break;
            case 2:
                btn_one.setText("取消订单");
                btn_two.setVisibility(View.GONE);
                break;
            case 3:
                btn_one.setVisibility(View.GONE);
                btn_two.setText("确认收货");
                break;
            case 4:
                btn_one.setText("删除订单");
                if (mPurchaseOderBean.getCustomer_status() == 1) {
                    if (mPurchaseOderBean.getCan_refund_num() > 0) {
                        btn_two.setText("申请售后");
                    } else {
                        btn_two.setVisibility(View.GONE);
                    }
                } else if (mPurchaseOderBean.getCustomer_status() == 2) {
                    btn_two.setVisibility(View.GONE);
                }
                break;
            case 5:
                btn_two.setVisibility(View.GONE);
                btn_one.setText("删除订单");
                break;
            case 6:
                btn_two.setVisibility(View.GONE);
                btn_one.setText("删除订单");
                break;
            default:
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                PurchaseOrderDetailsActivity.this.finish();
                break;
            case R.id.btn_one:
                switch (mPurchaseOderBean.getOrder_state()) {
                    // 取消订单
                    case 1:
                    case 2:
                        AlertDialog.Builder builder = new Builder(PurchaseOrderDetailsActivity.this);
                        builder.setMessage("您确定要取消订单吗？");
                        builder.setTitle("提示");
                        builder.setPositiveButton("取消订单", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(PurchaseOrderDetailsActivity.this, CancelReasonActivity.class);
                                intent.putExtra("order_id", mPurchaseOderBean.getId() + "");
                                startActivityForResult(intent, 2);

                            }
                        });

                        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
                        builder.create().show();
                        break;
                    case 3:
                        break;
                    case 4:
                    case 5:
                    case 6:
                        // 删除订单操作
                        AlertDialog.Builder builder2 = new Builder(PurchaseOrderDetailsActivity.this);
                        builder2.setMessage("您确定要删除该订单?");
                        builder2.setTitle("提示");
                        builder2.setPositiveButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder2.setNegativeButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteOrder();
                            }
                        });
                        builder2.create().show();
                        break;
                    default:

                        break;
                }
                break;
            case R.id.btn_two:
                switch (mPurchaseOderBean.getOrder_state()) {
                    // 立即支付
                    case 1:
                        againPay();
                        break;
                    case 4:
                        Intent intent = new Intent();
                        intent.setClass(PurchaseOrderDetailsActivity.this, RequestAfterSaleActivity.class);
                        intent.putExtra("order_num", mPurchaseOderBean.getOrder_num()
                                + "");
                        intent.putExtra("order_id", mPurchaseOderBean.getId()
                                + "");
                        intent.putExtra("pay_type", mPurchaseOderBean.getPay_type());

                        startActivityForResult(intent, 1);
                        break;
                    case 3:
                        AlertDialog.Builder builder = new Builder(PurchaseOrderDetailsActivity.this);
                        builder.setMessage("您确定已经收到货物吗?");
                        builder.setTitle("提示");
                        builder.setPositiveButton("收货", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                modifyOrderState(mPurchaseOderBean.getId(), 4);

                            }
                        });

                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
                        builder.create().show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    // 重新支付
    private void againPay() {
        // Map<String, String> map = new HashMap<String, String>();
        // map.put("orderId", mPurchaseOderBean.getId() + "");
        // mQueue.add(ParamTools.packParam(Constants.againPay, this, this,
        // map));
        String url;
        Intent intent = new Intent(this, TopayforActivity.class);
//        int idString = mPurchaseOderBean.getId();
//        if (mSavePreferencesData.getBooleanData("isSn")) {
//            url = Constants.mainUrl + Constants.continuePay + "order_id=" + idString + "&auth_token=" + partnerBean.getAuth_token() + "&os_type=3";
//        } else {
//            url = Constants.mainUrl + Constants.continuePay + "order_id=" + idString + "&auth_token=" + partnerBean.getAuth_token() + "&os_type=0";
//        }
//        Agreement7 agreement7 = new Agreement7();
//        agreement7.setLink_url(url);
//        agreement7.setTitle("重新支付");
        intent.putExtra("id", mPurchaseOderBean.getId());
        intent.putExtra("totalPrice", mPurchaseOderBean.getTotal_price());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
//                int state = data.getIntExtra("state", 0);
//                mPurchaseOderBean.setOrder_state(state);
//                initDate();
                finish();
            } else if (requestCode == 2) {
                this.finish();
            }

        }
    }

    @Override
    public void onResponse(String response, String url) {
        // TODO Auto-generated method stub
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("resultCode");
            if (stauts == 0) {
                if (url.contains(Constants.findOrder)) {
                    String resultJson = json.optString("resultJson");
                    mPurchaseOderBean = JSON.parseObject(resultJson, PurchaseOderBean.class);
                    initDate();

                } else if (url.contains(Constants.supplierOrderComplete)) {
                    Tools.showToast(PurchaseOrderDetailsActivity.this, "操作成功");
                    finish();
                } else if (url.contains(Constants.againPay)) {
                    String resultJson = json.optString("resultJson");
                    ConfirmBean confirmBean = JSON.parseObject(resultJson, ConfirmBean.class);
                    if (confirmBean.getPayType() == 4) {
                        // 货到付款
                        Tools.showToast(getApplicationContext(), "下单成功");
                        Tools.jump(PurchaseOrderDetailsActivity.this, OrderActivity.class, true);
                    } else {
                        initRay(confirmBean);
                    }

                } else if (url.contains(Constants.deleteOrder)) {
                    Tools.showToast(PurchaseOrderDetailsActivity.this, "操作成功");
                    finish();
                }

            } else {
                Tools.showToast(this, json.optString("resultMsg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    private void initRay(final ConfirmBean confirmBean) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // 构造AuthTask 对象
                        AuthTask authTask = new AuthTask(PurchaseOrderDetailsActivity.this);
                        // 调用授权接口，获取授权结果
                        Map<String, String> result = authTask.authV2(confirmBean.getSign(), true);

                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }
}