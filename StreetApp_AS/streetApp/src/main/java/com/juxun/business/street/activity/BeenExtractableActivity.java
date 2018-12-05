package com.juxun.business.street.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.ExtractableAdapter;
import com.juxun.business.street.bean.FinanceWithdrawBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类名称：ExtractableActivity 类描述： 可提现金额 首页 创建人：罗富贵 创建时间：2016年5月11日
 * 旧的，
 */
public class BeenExtractableActivity extends BaseActivity {

    @ViewInject(R.id.lv_list)
    private ListView lv_list;
    @ViewInject(R.id.tv_total_price)
    private TextView tv_total_price;// 合计
    @ViewInject(R.id.ll_wu)
    private LinearLayout ll_wu;
    @ViewInject(R.id.btn_withdrawal)
    private Button btn_withdrawal;// 我要提现
    private List<FinanceWithdrawBean> financeWithdraws;
    private ExtractableAdapter extractableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_been_extractable_list);
        ViewUtils.inject(this);
        initTitle();
        initView();
        title.setText("可提现金额");
    }

    @Override
    protected void onResume() {
        initDate();
        super.onResume();
    }

    private void initView() {
        financeWithdraws = new ArrayList<FinanceWithdrawBean>();
        extractableAdapter = new ExtractableAdapter(BeenExtractableActivity.this, financeWithdraws);
        View view = LayoutInflater.from(BeenExtractableActivity.this).inflate(R.layout.btn_withdrawal, null);
        Button btn_withdrawal = (Button) view.findViewById(R.id.btn_withdrawal);
        lv_list.addFooterView(view);
        lv_list.setAdapter(extractableAdapter);
        btn_withdrawal.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Tools.jump(BeenExtractableActivity.this, ApplyWithdrawActivity.class, false);
            }
        });
        this.btn_withdrawal.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Tools.jump(BeenExtractableActivity.this, ApplyWithdrawActivity.class, false);
            }
        });

    }

    private void initDate() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("pageNum", 1 + "");
        map.put("pageSize", 1000 + "");
        mQueue.add(ParamTools.packParam(Constants.withdrawList, this, this, map));
        loading();
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("stauts");
            String msg = json.optString("msg");
            if (stauts == 0) {
//                tv_total_price.setText(json.optDouble("sum") / 100 + "");
                financeWithdraws = JSON.parseArray(json.getString("result"), FinanceWithdrawBean.class);
                if (financeWithdraws.size() > 0) {
                    lv_list.setVisibility(View.VISIBLE);
                    ll_wu.setVisibility(View.GONE);
                } else {
                    lv_list.setVisibility(View.GONE);
                    ll_wu.setVisibility(View.VISIBLE);
                }
                extractableAdapter.updateListView(financeWithdraws);
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
