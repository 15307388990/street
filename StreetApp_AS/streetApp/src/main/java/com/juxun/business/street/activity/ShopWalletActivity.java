package com.juxun.business.street.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.ChartPie;
import com.juxun.business.street.widget.ChartPie.Element;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShopWalletActivity extends BaseActivity {

    @ViewInject(R.id.circle_view)
    private ChartPie circle_view;// 钱包资产饼状图

    @ViewInject(R.id.tv_total_money)
    private TextView tv_total_money;// 总资产
    @ViewInject(R.id.tv_can_cash)
    private TextView tv_can_cash;// 可提现金额
    @ViewInject(R.id.tv_balance)
    private TextView tv_balance;// 采购金额

    @ViewInject(R.id.tv_cash_money)
    private TextView tv_cash_money;// 可提现金额
    @ViewInject(R.id.rl_cancash)
    private RelativeLayout rl_cancash;// 可提现金额跳转
    @ViewInject(R.id.tv_total_income)
    private TextView tv_total_income;// 实收总金额
    @ViewInject(R.id.tv_has_cashed)
    private TextView tv_has_cashed;// 已提现金额
    @ViewInject(R.id.tv_freeze)
    private TextView tv_freeze;// 冻结金额

    @ViewInject(R.id.tv_purchase_balance)
    private TextView tv_purchase_balance;// 采购金额
    @ViewInject(R.id.rl_purchase_balance)
    private RelativeLayout rl_purchase_balance;// 采购金额跳转

    // 有点击事件的两个
    @ViewInject(R.id.tv_tocash)
    private TextView tv_tocash;// 提现
    @ViewInject(R.id.tv_tocharge)
    private TextView tv_tocharge;// 充值

    private ArrayList<Element> datasList = new ArrayList<Element>(); // 饼图颜色及比例列表
    private DecimalFormat mDf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_wallet);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("钱包总资产");
    }

    @Override
    protected void onResume() {
        getWalletDatas();
        super.onResume();
    }

    private void initViewDatas(JSONObject json) {
        mDf = new DecimalFormat("0.00");

        double can_withdraw_price = Tools.getFenYuan(json
                .optInt("can_withdraw_price"));// 可提现金额
        double remainingBalance = Tools.getFenYuan(json
                .optInt("remainingBalance"));// 采购余额
        double total_price = Tools.getFenYuan(json.optInt("total_price"));// 实收总金额
        double withdraw_price = Tools.getFenYuan(json.optInt("withdraw_price"));// 已提现金额
        double freeze_price = Tools.getFenYuan(json.optInt("freeze_price"));// 冻结金额
        double totalPrice = can_withdraw_price + remainingBalance;

        // 饼状图部分
        tv_total_money.setText(mDf.format(totalPrice));
        tv_can_cash.setText(mDf.format(can_withdraw_price) + " 元");
        tv_balance.setText(mDf.format(remainingBalance) + " 元");

        if (totalPrice == 0) {
            circle_view.setData(datasList, "", "");
        } else {
            datasList.add(new Element(getResources().getColor(
                    R.color.point_cash_yellow),
                    (float) (can_withdraw_price * 360 / totalPrice)));
            datasList.add(new Element(getResources().getColor(
                    R.color.point_cash_green),
                    (float) (remainingBalance * 360 / totalPrice)));
            circle_view.setData(datasList, "", "");
        }

        // 提现部分
        tv_cash_money.setText(mDf.format(can_withdraw_price));
        tv_total_income.setText(mDf.format(total_price));
        tv_has_cashed.setText(mDf.format(withdraw_price));
        tv_freeze.setText(mDf.format(freeze_price));

        // 充值部分
        tv_purchase_balance.setText(mDf.format(remainingBalance));
    }

    /* 获取店铺详情 */
    private void getWalletDatas() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.shopWallet, this, this, map));
        loading();
    }

    @OnClick({R.id.tv_tocash, R.id.tv_tocharge, R.id.rl_purchase_balance,
            R.id.rl_cancash})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.tv_tocash: // 提现页面
                Tools.jump(this, ApplyWithdrawActivity.class, false);
                break;
            case R.id.tv_tocharge: // 充值页面
                Tools.jump(this, TopUpActivity.class, false);
                break;
            case R.id.rl_purchase_balance: // 采购余额
                Tools.jump(this, PurchasingBalanceActivity.class, false);
                break;
            case R.id.rl_cancash: // 可提现余额
                Tools.jump(this, CanCashActivity.class, false);
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
                JSONObject jsonObject = json.optJSONObject("result");
                initViewDatas(jsonObject);
            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, "解析错误");
        }
    }

}
