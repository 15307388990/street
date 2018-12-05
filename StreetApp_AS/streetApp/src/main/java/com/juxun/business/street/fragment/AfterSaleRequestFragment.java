package com.juxun.business.street.fragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshListView;
import com.ming.ui.PullToRefreshBase.OnRefreshListener;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.activity.LoginActivity;
import com.juxun.business.street.adapter.AftersaleRequestAdapter;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.PurchaseOderBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.yl.ming.efengshe.R;

/**
 * 售后申请
 *
 * @author wood121
 */
@SuppressLint("ValidFragment")
public class AfterSaleRequestFragment extends BaseFragment {

    private Context mContext;
    private LinearLayout ll_wu;
    private PullToRefreshListView refreshListView;
    private ListView listView;
    private int pageNumber = 1;
    private List<PurchaseOderBean> mAfterSaleList;
    private AftersaleRequestAdapter mRequestAdapter;

    public AfterSaleRequestFragment(Activity context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aftersale_request, container, false);
        initViews(view);
        initValues();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void initValues() {
        // 初始化下拉刷新listView
        initPull();
        mAfterSaleList = new ArrayList<PurchaseOderBean>();
        mRequestAdapter = new AftersaleRequestAdapter(mcontext, mAfterSaleList);
        listView.setAdapter(mRequestAdapter);
        listView.setDividerHeight(0);

        // 获取具体数据
        requestSales();
    }

    private void initPull() {
        refreshListView.setPullLoadEnabled(false);
        refreshListView.setScrollLoadEnabled(true);
        listView = refreshListView.getRefreshableView();

        refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber = 1;
                requestSales();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber++;
                requestSales();
            }
        });
    }

    public void requestSales() {
        Map<String, String> map = new HashMap<String, String>();
        // agency_id auth_token
        // pager.pagerNumber 请求的页面数量
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("pageNumber", pageNumber + "");
        map.put("pageSize", "10");
        mQueue.add(ParamTools.packParam(Constants.aftersaleRequestList, this, this, map));
    }

    private void initViews(View view) {
        refreshListView = (PullToRefreshListView) view.findViewById(R.id.lv_fresh);
        ll_wu = (LinearLayout) view.findViewById(R.id.ll_wu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            requestSales();
        }
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        // response:resultCode,resultMsg,resultJson
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            String msg = json.optString("msg");

            if (stauts == 0) {
                if (url.contains(Constants.aftersaleRequestList)) {
                    String liString = json.getString("result");
                    List<PurchaseOderBean> list = JSON.parseArray(liString, PurchaseOderBean.class);

                    // 是否可以上拉操作
                    if (list.size() < 10) {
                        refreshListView.setHasMoreData(false);
                    }

                    // 上拉操作加载更多数据
                    if (pageNumber > 1) {
                        mAfterSaleList.addAll(list);
                    } else {
                        mAfterSaleList = list;
                    }

                    // 空白页面的显示
                    if (mAfterSaleList.size() > 0) {
                        refreshListView.setVisibility(View.VISIBLE);
                        ll_wu.setVisibility(View.GONE);
                    } else {
                        refreshListView.setVisibility(View.GONE);
                        ll_wu.setVisibility(View.VISIBLE);
                    }

                    refreshListView.onPullDownRefreshComplete();
                    refreshListView.onPullUpRefreshComplete();
                    mRequestAdapter.updateListView(mAfterSaleList);
                }
            } else if (stauts == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(mcontext, LoginActivity.class, true);
                Toast.makeText(mcontext, "登录过期请重新登录", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mcontext, msg, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mcontext, R.string.tips_unkown_error, Toast.LENGTH_SHORT).show();
        }
    }
}
