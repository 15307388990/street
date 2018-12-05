/**
 *
 */
package com.juxun.business.street.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.PurchasingBaianceAdapter;
import com.juxun.business.street.bean.Agreement7;
import com.juxun.business.street.bean.TopUpBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshBase.OnRefreshListener;
import com.ming.ui.PullToRefreshListView;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.juxun.business.street.config.Constants.mallSetInfo;

/**
 * @version 采购余额
 */
public class PurchasingBalanceActivity extends BaseActivity {
    @ViewInject(R.id.lv_list)
    private PullToRefreshListView mpuListView;
    @ViewInject(R.id.ll_top_up)
    private LinearLayout ll_top_up;// 充值
    @ViewInject(R.id.tv_amountof)
    private TextView tv_amountof; // 金额
    @ViewInject(R.id.iv_help)
    private ImageView iv_help;// 帮助
    @ViewInject(R.id.tv_text)
    private TextView tv_text;

    private ListView mlistview;
    private int pageNumber = 1;
    private List<TopUpBean> mTopUpBeans;
    private PurchasingBaianceAdapter mPurchasingBaianceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchasing_balance);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("采购余额");
        initView();
    }

    private void initView() {
        initPull();
        tv_amountof.setText(mDf.format(Tools.getFenYuan(partnerBean.getRemaining_balance())));
        mTopUpBeans = new ArrayList<>();
        mPurchasingBaianceAdapter = new PurchasingBaianceAdapter(this, mTopUpBeans);
        mlistview.setAdapter(mPurchasingBaianceAdapter);
        ll_top_up.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Tools.jump(PurchasingBalanceActivity.this, TopUpActivity.class, false);
            }
        });
        iv_help.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchasingBalanceActivity.this, WebviewActivity.class);
                Agreement7 agreement7 = new Agreement7();
                agreement7.setLink_url(Constants.mainUrl + Constants.supplierRules);
                agreement7.setTitle("规则说明");
                intent.putExtra("agreement7", agreement7);
                startActivity(intent);
            }
        });
    }

    private void initPull() {
        mpuListView.setPullLoadEnabled(false);
        mpuListView.setScrollLoadEnabled(true);
        mpuListView.setPullRefreshEnabled(false);
        mlistview = mpuListView.getRefreshableView();
        mlistview.setSelector(R.color.transparent);
        mlistview.setDividerHeight(0);
        mpuListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber = 1;
                findRechargeRecordList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber++;
                findRechargeRecordList();
            }
        });
    }

    // 获取充值记录
    private void findRechargeRecordList() {
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", "10");
        map.put("pageNumber", pageNumber + "");
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.findRechargeRecordList, this, this, map));
    }

    /* 获取店铺详情 */
    private void mallSetInfo(String auth_token) {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", auth_token);
        mQueue.add(ParamTools.packParam(Constants.mallSetInfo, this, this, map));
    }

    @Override
    protected void onResume() {
        super.onResume();
        findRechargeRecordList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            Log.i("test", response);
            int status = json.getInt("status");
            String msg = json.getString("msg");
            if (status == 0) {
                if (url.contains(Constants.findRechargeRecordList)) {

                    List<TopUpBean> list = JSON.parseArray(json.optString("result"), TopUpBean.class);
                    if (pageNumber == 1) {
                        mTopUpBeans = list;
                    } else {
                        mTopUpBeans.addAll(list);
                    }
                    if (list.size() < 10) {
                        mpuListView.setHasMoreData(false);
                    }
                    if (mTopUpBeans.size() < 1) {
                        mpuListView.setVisibility(View.GONE);
                        tv_text.setVisibility(View.GONE);
                    } else {
                        mpuListView.setVisibility(View.VISIBLE);
                        tv_text.setVisibility(View.VISIBLE);
                    }
                    mpuListView.onPullDownRefreshComplete();
                    mpuListView.onPullUpRefreshComplete();
                    mPurchasingBaianceAdapter.updateListView(mTopUpBeans);
                    mallSetInfo(partnerBean.getAuth_token());
                } else if (url.contains(mallSetInfo)) {
                    mSavePreferencesData.putStringData("json", json.optString("result"));
                    initAdmin();
                    tv_amountof.setText(mDf.format(Tools.getFenYuan(partnerBean.getRemaining_balance())));
                }
            } else if (status == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(PurchasingBalanceActivity.this, LoginActivity.class, true);
                Tools.showToast(PurchasingBalanceActivity.this, "登录过期请重新登录");
                Tools.exit();
            } else {
                Tools.showToast(getApplicationContext(), msg);
            }
        } catch (JSONException e) {
            Tools.showToast(getApplicationContext(), "解析数据错误");
        }
    }
}
