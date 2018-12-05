package com.juxun.business.street.fragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.activity.AfterSaleListActivity;
import com.juxun.business.street.activity.LoginActivity;
import com.juxun.business.street.adapter.AftersaleHistoryAdapter;
import com.juxun.business.street.adapter.AftersaleHistoryAdapter.onCancelAfter;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.PurchaseOderBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshListView;
import com.ming.ui.PullToRefreshBase.OnRefreshListener;
import com.yl.ming.efengshe.R;

/**
 * 售后记录
 *
 * @author wood121
 */

@SuppressLint("ValidFragment")
public class AfterSaleHistoryFragment extends BaseFragment implements onCancelAfter {

    private Context mContext;
    private LinearLayout ll_wu;
    private PullToRefreshListView refreshListView;
    private ListView listView;
    private int pageNumber = 1;
    private List<PurchaseOderBean> mAfterSaleList;
    private AftersaleHistoryAdapter mRequestAdapter;

    /**
     * @param context
     */
    public AfterSaleHistoryFragment(AfterSaleListActivity context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aftersale_history, container, false);
        initViews(view);
        initValues();
        return view;
    }

    private void initValues() {
        // 初始化下拉刷新listView
        initPull();
        mAfterSaleList = new ArrayList<PurchaseOderBean>();
        mRequestAdapter = new AftersaleHistoryAdapter(mcontext, this);
        listView.setAdapter(mRequestAdapter);
        listView.setDividerHeight(0);
        listView.setEmptyView(ll_wu);

        // 获取具体数据
        getApplyRecordList();
    }

    private void initPull() {
        refreshListView.setPullLoadEnabled(false);
        refreshListView.setScrollLoadEnabled(true);
        listView = refreshListView.getRefreshableView();

        refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber = 1;
                getApplyRecordList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber++;
                getApplyRecordList();
            }
        });
    }

    /**
     * 获取申请记录
     */
    private void getApplyRecordList() {
        Map<String, String> map = new HashMap<String, String>();
        // agency_id
        // auth_token
        // pageNumber
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("pageNumber", pageNumber + "");
        map.put("pageSize", "10");
        mQueue.add(ParamTools.packParam(Constants.getApplyRecordList, this, this, map));
    }

    private void initViews(View view) {
        refreshListView = (PullToRefreshListView) view.findViewById(R.id.lv_fresh);
        ll_wu = (LinearLayout) view.findViewById(R.id.ll_wu);
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            String msg = json.optString("msg");
            if (stauts == 0) {
                if (url.contains(Constants.getApplyRecordList)) {
                    String liString = json.getString("result");
                    List<PurchaseOderBean> list = JSON.parseArray(liString, PurchaseOderBean.class);
                    if (list.size() < 10) {
                        refreshListView.setHasMoreData(false);

                    }
                    if (pageNumber > 1) {
                        mAfterSaleList.addAll(list);
                    } else {
                        mAfterSaleList = list;
                    }
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
                } else if (url.contains(Constants.closeCustomerServiceApply)) {
                    getApplyRecordList();
                    // 通知那边刷新数据
                    mcontext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((AfterSaleListActivity) mcontext).requestSales();
                        }
                    });
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

    @Override
    public void onCancel(final int order_id) {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setMessage("您确定要取消售后申请吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeCustomerServiceApply(order_id);

            }
        });

        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
        builder.create().show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            getApplyRecordList();
        }
    }

    // 取消售后
    private void closeCustomerServiceApply(int order_id) {
        // 取消售后
        Map<String, String> map = new HashMap<String, String>();
        // agency_id
        // auth_token
        // order_id
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("service_order_id", order_id + "");
        mQueue.add(ParamTools.packParam(Constants.closeCustomerServiceApply, this, this, map));
    }
}
