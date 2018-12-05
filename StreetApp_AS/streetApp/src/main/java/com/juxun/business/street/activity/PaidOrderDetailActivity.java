package com.juxun.business.street.activity;

/**
 * 我的订单详情
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.bean.OrderModel;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.MallOrderBean;
import com.juxun.business.street.bean.Msgmodel;
import com.juxun.business.street.bean.ParseModel;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaidOrderDetailActivity extends BaseActivity {
    /**
     * ----------------------- 商品清单--------------------------------
     **/
    @ViewInject(R.id.ll_list)
    private LinearLayout ll_list;// 商品列表
    @ViewInject(R.id.tv_describe)
    private TextView tv_describe;// 共1件商品 运费10元 优惠5元 合计
    @ViewInject(R.id.tv_total_price)
    private TextView tv_total_price;// 总价 红色的
    /**
     * ----------------------- 订单信息--------------------------------
     **/
    @ViewInject(R.id.tv_jiaoyi_number)
    private TextView tv_jiaoyi_number;// 订单编号
    @ViewInject(R.id.tv_state)
    private TextView tv_state;// 资金状态
    @ViewInject(R.id.tv_paytype)
    private TextView tv_paytype;// 支付方式
    @ViewInject(R.id.tv_ordercreateTime)
    private TextView tv_ordercreateTime;// 下单时间
    @ViewInject(R.id.tv_order_end_date)
    private TextView tv_order_end_date;// 付款时间
    @ViewInject(R.id.tv_delivery_date)
    private TextView tv_delivery_date;// 发货时间
    @ViewInject(R.id.tv_deal_date)
    private TextView tv_deal_date;// 成交时间
    /**
     * -----------------------收货信息--------------------------------
     **/
    @ViewInject(R.id.tv_name)
    private TextView tv_name;// 收货人
    @ViewInject(R.id.tv_ipone)
    private TextView tv_ipone;// 联系电话
    @ViewInject(R.id.tv_adds)
    private TextView tv_adds;// 收货地址
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();
    private String settle_id;
    OrderModel mallOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_order_detail);
        ViewUtils.inject(this);
        initTitle();
        title.setText("订单详情");
        initView();
    }

    public void initView() {
        settle_id = getIntent().getStringExtra("settle_id");
        Map<String, String> map = new HashMap<String, String>();
        map.put("order_num", settle_id + "");
        //  map.put("order_num", orderModel.getMember_id() + "");
        map.put("auth_token", partnerBean.getAuth_token());// 管理员设备id
        mQueue.add(ParamTools
                .packParam(Constants.getOrderInfo, this, this, map));

    }

    public int totalPrice;
    DecimalFormat df = new java.text.DecimalFormat("0.00");

    private void initDate() {
        String spec = mallOrder.getSpec();
        List<Msgmodel> msgmodels = new ArrayList<Msgmodel>();
        try {
            msgmodels = new ParseModel().getMsgmodel(spec);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (msgmodels.size() > 0) {
            int number = 0;
            for (int i = 0; i < msgmodels.size(); i++) {
                number = msgmodels.get(i).getGoodsCount() + number;
                View lview = LayoutInflater.from(PaidOrderDetailActivity.this).inflate(R.layout.buy_item, null);
                TextView tv_name = (TextView) lview.findViewById(R.id.tv_name);
                TextView tv_price = (TextView) lview.findViewById(R.id.tv_pirce);
                TextView tv_number = (TextView) lview.findViewById(R.id.tv_number);
                ImageView imageView = (ImageView) lview.findViewById(R.id.iv_img);
                Msgmodel msgmodel = msgmodels.get(i);
                imageLoader.displayImage(Constants.imageUrl + msgmodel.getCommodityICon(), imageView, options);
                tv_name.setText(msgmodel.getCommodityName());
                if (msgmodel.getSpecNames() != null) {
                    //tv_specNames.setText(msgmodel.getSpecNames());
                }
                tv_price.setText(Tools.getFenYuan(msgmodel.getPrice()) + "元");
                tv_number.setText("x" + msgmodel.getGoodsCount());
                ll_list.addView(lview);
            }

            totalPrice = mallOrder.getTotal_price() + mallOrder.getDelivery_price() - mallOrder.getRedpacket_price() - mallOrder.getGold_price();
            tv_describe.setText(
                    "共" + number + "件商品,运费" + Tools.getFenYuan(mallOrder.getDelivery_price()) + "元 ,红包优惠" + Tools.getFenYuan(mallOrder.getRedpacket_price())
                            + "元,e蜂币抵扣" + Tools.getFenYuan(mallOrder.getGold_price()) + "元,合计： " + Tools.getFenYuan(totalPrice) + "元");
        }
        //  tv_total_price.setText("¥" + mallOrder.getTotal_price());
        tv_jiaoyi_number.setText("订单编号：" + mallOrder.getOrder_num());

        // 订单状态【1.待付款,2.待发货,3.已发货,4.交易完成,5.已取消,6.已退货】
        switch (mallOrder.getOrder_state()) {
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
        // 支付方式 //支付类型 1.微信支付.2.支付宝支付 3、盒子支付
        switch (mallOrder.getPay_type()) {
            case 1:
                tv_paytype.setText("支付方式：微信支付");
                break;
            case 2:
                tv_paytype.setText("支付方式：支付宝支付");
                break;
            case 4:
                tv_paytype.setText("支付方式：刷卡支付");
                break;
            default:
                break;
        }
        tv_ordercreateTime.setText("下单时间：" + Tools.getDateformat2(mallOrder.getCreate_date()));
        if (mallOrder.getFinish_time() != null) {
            tv_order_end_date.setText("付款时间：" + Tools.getDateformat2(Long.valueOf(mallOrder.getFinish_time().toString())));
        } else {
            tv_order_end_date.setText("付款时间：");
        }
        if (mallOrder.getReal_delivery_time() != null) {
            tv_delivery_date.setText("发货时间：" + Tools.getDateformat2(Long.valueOf(mallOrder.getReal_delivery_time().toString())));
        } else {
            tv_delivery_date.setText("发货时间：");
        }
        if (mallOrder.getConfirm_date() != null) {
            tv_deal_date.setText("成交时间：" + Tools.getDateformat2(Long.valueOf(mallOrder.getConfirm_date().toString())));
        } else {
            tv_deal_date.setText("成交时间：" );
        }

            tv_name.setText(mallOrder.getConsignee_name());
            tv_ipone.setText(mallOrder.getConsignee_phone());
            tv_adds.setText(mallOrder.getConsignee_address());
        }

        @Override
        public void onResponse (String response, String url){
            dismissLoading();
            try {
                JSONObject json = new JSONObject(response);
                int stauts = json.optInt("status");
                String msg = json.optString("msg");
                if (stauts == 0) {
                    mallOrder = JSON.parseObject(json.getString("result"),
                            OrderModel.class);
                    if (mallOrder != null) {
                        initDate();
                    }
                } else {
                    Tools.showToast(this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Tools.showToast(this, R.string.tips_unkown_error);
            }
        }

    }