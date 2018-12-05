package com.juxun.business.street.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.StoreCenAndHisAdapter;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.StoreCenterBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshBase.OnRefreshListener;
import com.ming.ui.PullToRefreshListView;
import com.yl.ming.efengshe.R;

/**
 * 历史活动页面
 *
 * @author wood121
 */
public class StoreHistoryActivity extends BaseActivity implements
        OnClickListener {

    @ViewInject(R.id.button_back)
    private ImageView button_back;
    @ViewInject(R.id.ll_wu)
    private LinearLayout ll_wu;// 没有数据时显示内容
    @ViewInject(R.id.eleListView)
    private PullToRefreshListView mPullToRefreshListView; // 展示的列表

    private ListView mListView; //
    private List<StoreCenterBean> mStoreCardsList;
    private StoreCenAndHisAdapter mStoreCenAndHisAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_history); // 与【采购订单】取消订单布局一致
        ViewUtils.inject(this);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestHistoryList();
    }

    private void initViews() {
        initPull();
        button_back.setOnClickListener(this);
        mStoreCenAndHisAdapter = new StoreCenAndHisAdapter(this,
                mStoreCardsList);
        mListView.setAdapter(mStoreCenAndHisAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(StoreHistoryActivity.this,
                        StoreActsActivity.class);
                StoreCenterBean storeCenterBean = mStoreCardsList.get(arg2);
                intent.putExtra("stores", storeCenterBean);
                intent.putExtra("name", storeCenterBean.getName());
                startActivity(intent);
            }
        });
    }

    private void initPull() {
        mPullToRefreshListView.setPullLoadEnabled(false);
        mPullToRefreshListView.setScrollLoadEnabled(true);
        mListView = mPullToRefreshListView.getRefreshableView();
        mListView.setDividerHeight(0);
        mPullToRefreshListView
                .setOnRefreshListener(new OnRefreshListener<ListView>() {
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {

                        requestHistoryList();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                    }
                });
    }

    private void requestHistoryList() {
        Map<String, String> map = new HashMap<String, String>();
        //map.put("agency_id", partnerBean.getAdmin_agency() + "");
        map.put("auth_token", partnerBean.getAuth_token() + "");
        mQueue.add(ParamTools.packParam(Constants.getMemberRechargeActivityHistoryList, this,
                this, map));
        loading();
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.optInt("status");
            String msg = json.optString("msg");
            if (status == 0) {
                mStoreCardsList = JSON.parseArray(
                        json.optString("result"),
                        StoreCenterBean.class);
                if (mStoreCardsList != null) {
                    if (mStoreCardsList.size() != 0) {
                        mPullToRefreshListView.setVisibility(View.VISIBLE);
                        ll_wu.setVisibility(View.GONE);
                        mStoreCenAndHisAdapter
                                .updateHistoryList(mStoreCardsList, 1);
                        if (mStoreCardsList.size() < 10) {
                            mPullToRefreshListView.setHasMoreData(false);
                        }
                    } else {
                        mPullToRefreshListView.setVisibility(View.GONE);
                        ll_wu.setVisibility(View.VISIBLE);
                    }
                } else {
                    mPullToRefreshListView.setVisibility(View.GONE);
                    ll_wu.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(StoreHistoryActivity.this, "解析错误");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                finish();
                break;

            default:
                break;
        }
    }

}
