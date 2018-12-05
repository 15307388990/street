package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.bean.SettleListBean;
import com.juxun.business.street.bean.Msgmodel;
import com.juxun.business.street.bean.ParseModel;
import com.juxun.business.street.bean.SettleListBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 页面：可提现金额 CanCashActivity
 *
 * @author wood121
 */
public class CanCashActivity extends BaseActivity {

    @ViewInject(R.id.tv_cash_record)
    private TextView tv_cash_record; // 提现记录
    @ViewInject(R.id.tv_can_cash)
    private TextView tv_can_cash; // 可提现金额

    @ViewInject(R.id.tv_freeze_money)
    private TextView tv_freeze_money; // 冻结金额
    // 没有记录显示
    @ViewInject(R.id.ll_nolist)
    private RelativeLayout ll_nolist; // 冻结金额
    // 有记录可以查询
    @ViewInject(R.id.ll_haslist)
    private RelativeLayout ll_haslist; //
    @ViewInject(R.id.ll_goods_container)
    private LinearLayout ll_goods_container; // 冻结记录
    @ViewInject(R.id.tv_all_records)
    private TextView tv_all_records; // 查看全部冻结记录

    @ViewInject(R.id.btn_request_cash)
    private Button btn_request_cash; // 申请提现

    private DecimalFormat mDf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancash);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("可提现金额");
        mDf = new DecimalFormat("0.00");
    }

    @Override
    protected void onResume() {
        super.onResume();
        canCashDatas();
    }

    /* 获取数据 */
    public void canCashDatas() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("pageNumber", 1 + "");
        map.put("pageSize", 10 + "");
        mQueue.add(ParamTools.packParam(Constants.freezeList, this, this, map));
        loading();
    }

    private void initViewDatas(JSONObject json) {
        // 两个需要展示的数据
        tv_can_cash.setText(mDf.format(Tools.getFenYuan(json
                .optInt("can_withdraw_price"))));
        tv_freeze_money.setText(mDf.format(Tools.getFenYuan(json
                .optInt("freeze_price"))));
        // 按钮的处理
        if (json.optInt("can_withdraw_price") > 0) {
            btn_request_cash.setBackgroundResource(R.drawable.button_bg1);
            btn_request_cash.setTextColor(getResources()
                    .getColor(R.color.white));
            btn_request_cash.setEnabled(true);
        } else {
            btn_request_cash.setBackgroundResource(R.drawable.button_bg);
            btn_request_cash.setTextColor(getResources().getColor(
                    R.color.jiujiujiu));
            btn_request_cash.setEnabled(false);
        }
    }

    private void updateListview(List<SettleListBean> financeList) {
        if (financeList == null || financeList.size() == 0) {
            ll_nolist.setVisibility(View.VISIBLE);
            ll_haslist.setVisibility(View.GONE);

            // // 没有查看全部按钮
            // tv_all_records.setVisibility(View.GONE);
            // // 中间展示文字
            // TextView textView = new TextView(this);
            // textView.setLayoutParams(new LayoutParams(
            // LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            // textView.setTextSize(17);
            // textView.setText("暂无待入账记录");
            // textView.setTextColor(getResources().getColor(R.color.jiujiujiu));
            // // 字体居中
            // ll_goods_container.setGravity(Gravity.CENTER);
            // LayoutParams layoutParams = new LayoutParams(
            // LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            // layoutParams.setMargins(0, 0, 0, Tools.dip2px(this, 89));
            // ll_goods_container.setLayoutParams(layoutParams);
            //
            // ll_goods_container.addView(textView);
        } else {
            // 最多展示三组
            ll_nolist.setVisibility(View.GONE);
            ll_haslist.setVisibility(View.VISIBLE);
            ll_goods_container.removeAllViews(); // 清空我们的

            for (int i = 0; i < financeList.size(); i++) {
                SettleListBean financeSettle = financeList.get(i);
                View itemView = LayoutInflater.from(this).inflate(
                        R.layout.item_cancash_activity, null);
                TextView tvName = (TextView) itemView
                        .findViewById(R.id.tv_name);
                TextView tvDate = (TextView) itemView
                        .findViewById(R.id.tv_date);
                TextView tvPrice = (TextView) itemView
                        .findViewById(R.id.tv_price);

                tvDate.setText(Tools.getDateformat(financeSettle
                        .getOrder_date()));
                tvPrice.setText("+"
                        + Tools.getFenYuan(financeSettle.getSettle_price()));

                // 4 此商品为POS订单
                if (financeSettle.getSettle_type() == 4) {
                    // 支付方式 //支付类型 1.微信支付.2.支付宝支付 3、盒子支付
                    switch (financeSettle.getPay_type()) {
                        case 1:
                            tvName.setText("微信支付");
                            break;
                        case 2:
                            tvName.setText("支付宝支付");
                            break;
                        case 3:
                            tvName.setText("盒子支付");
                            break;
                        default:
                            break;
                    }
                } else if (financeSettle.getSettle_type() == 2) {// 2:特卖分佣订单
                    tvName.setText("特卖分佣订单所得");
                } else if (financeSettle.getSettle_type() == 3) {// 3:海淘分佣订单
                    tvName.setText("海淘分佣订单所得");
                } else {
                    String spec = financeSettle.getSpec();
                    List<Msgmodel> msgmodels = new ArrayList<Msgmodel>();
                    try {
                        msgmodels = new ParseModel().getMsgmodel(spec);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (msgmodels != null) {
                        if (msgmodels.size() > 1) {
                            String goodsName = "";
                            for (int j = 0; j < msgmodels.size(); j++) {
                                // 第一个名字前面不添加、
                                if (j == 0) {
                                    goodsName = msgmodels.get(0)
                                            .getCommodityName();
                                    continue;
                                }
                                goodsName = goodsName + ", "
                                        + msgmodels.get(j).getCommodityName();
                            }
                            tvName.setText(goodsName);
                        } else if (msgmodels.size() == 1) { // 一个的情况直接取名字
                            tvName.setText(msgmodels.get(0).getCommodityName());
                        }
                    }
                }
                ll_goods_container.addView(itemView);
            }
        }
    }

    @OnClick({R.id.tv_cash_record, R.id.tv_all_records, R.id.btn_request_cash})
    public void clickMethod(View v) {

        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_cash_record: // 提现记录
                intent.setClass(CanCashActivity.this, CashRecordActivity.class);
                // intent.setClass(CanCashActivity.this, CashFreezeActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_all_records: // 查看全部记录
                intent.setClass(CanCashActivity.this, CashFreezeActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_request_cash: // 申请提现
                Tools.jump(this, ApplyWithdrawActivity.class, false);
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
                JSONObject jsonObject=json.optJSONObject("result");
                initViewDatas(jsonObject);
                String settle_list = jsonObject.optString("settle_list");
                List<SettleListBean> financeList = JSON.parseArray(settle_list, SettleListBean.class);
                if (financeList != null) {
                    updateListview(financeList);
                }

            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, "发生错误请重试" + url);
        }
    }

}
