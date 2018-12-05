package com.juxun.business.street.activity;

/**
 * @author ExtractableDetailActivity 提现详情
 */

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.FinanceWithdrawBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

public class ExtractableDetailActivity extends BaseActivity {

    @ViewInject(R.id.tv_account)
    private TextView mAccount;// 提现账户
    @ViewInject(R.id.tv_way)
    private TextView mWay;// 提现方式
    @ViewInject(R.id.tv_time)
    private TextView mTime;// 提现时间
    @ViewInject(R.id.tv_tips)
    private TextView mTips;// 打款时间
    @ViewInject(R.id.tv_card)
    private TextView tv_card;// 交易号
    @ViewInject(R.id.tv_tips)
    private TextView tv_tips;// 留言
    @ViewInject(R.id.tv_state)
    private TextView tv_state;// 提现状态
    @ViewInject(R.id.tv_price)
    private TextView tv_price;// 提现金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_extractable_detail);
        ViewUtils.inject(this);
        initTitle();
        title.setText("提现详情");
        initView();
    }

    private void initView() {
        FinanceWithdrawBean finance = (FinanceWithdrawBean) getIntent().getSerializableExtra("finance");
        initDate(finance);
    }

    private void initDate(FinanceWithdrawBean finance) {
        tv_price.setText("¥" + finance.getWithdraw_price() / 100);
        switch (finance.getWithdraw_stauts()) {
            case 0:
                tv_state.setText("(提现中)");
                break;
            case 1:
                tv_state.setText("(提现成功)");
                break;
            case -1:
                tv_state.setText("(提现失败)");
                break;
            default:
                break;
        }
        mAccount.setText(finance.getWithdraw_access());
        switch (finance.getWithdraw_type()) {
            case 1:
                mWay.setText("支付宝");
                break;
            case 2:
                mWay.setText("银行卡");
                break;
            default:
                break;
        }
        mTime.setText(Tools.getDateformat2(Long.parseLong(finance.getWithdraw_date())));
        tv_card.setText(finance.getWithdraw_number());
        tv_tips.setText(finance.getWithdraw_leave_message());

    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("stauts");
            String msg = json.optString("msg");
            if (stauts == 0) {


            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    @OnClick({R.id.tv_commit})
    void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.tv_commit:
                break;
        }

    }

}
