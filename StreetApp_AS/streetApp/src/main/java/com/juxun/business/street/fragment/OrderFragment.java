package com.juxun.business.street.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxun.business.street.activity.EleOrderDetailActivity;
import com.juxun.business.street.activity.LoginActivity;
import com.juxun.business.street.activity.MainActivity;
import com.juxun.business.street.adapter.MyOrderdapter;
import com.juxun.business.street.adapter.MyOrderdapter.onOperateOrder;
import com.juxun.business.street.bean.OrderModel;
import com.juxun.business.street.bean.ParseModel;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshBase.OnRefreshListener;
import com.ming.ui.PullToRefreshListView;
import com.yl.ming.efengshe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luoming 订单
 */
@SuppressLint("ValidFragment")
public class OrderFragment extends BaseFragment implements onOperateOrder {
    /*
     * 控件
     */
    private RadioGroup cagetoryGroup;
    private RadioButton whole;// 全部
    private RadioButton to_be_shipped;// 待发货
    private RadioButton shipped;// 已发货
    private RadioButton completed;// 已完成
    private RadioButton canceled;// 已取消

    private PullToRefreshListView mPullToRefreshListView;
    private LinearLayout ll_wu;// 没有订单
    private ListView mListView;
    private MyOrderdapter adapter;
    /*
     * 数据集合
     */
    private List<OrderModel> wholeList = new ArrayList<OrderModel>();
    private List<OrderModel> toBeshippedList = new ArrayList<OrderModel>();
    private List<OrderModel> shippedList = new ArrayList<OrderModel>();
    private List<OrderModel> completedList = new ArrayList<OrderModel>();
    private List<OrderModel> canceledList = new ArrayList<OrderModel>();

 //   private List<OrderModel> returnSingleList = new ArrayList<OrderModel>();

    // 订单状态//0 全部 1待发货 2已完成 3（退货单）
    private int pageNumber = 1;// 当前页码
    private int pageSize = 10;// 每页个数
    private int tag = 2;
    private MainActivity activity;
    private int mPosition; // 删除的订单对应位置
    private int mOrderTypeFrom; // 删除订单对应的列表:已完成，已取消，全部列表

    public OrderFragment() {
    }

    public OrderFragment(Activity context) {
        super(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_electricity_supplier,
                container, false);
        // initTitle();
        // title.setText(getString(R.string.electricity_supplier_order));
        initView(view);
        initValues();
        bindEvents();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 订单状态0全部 2.待发货,3.已发货,4.交易完成,5.已取消
        pageNumber = 1;
        if (tag == 0) {
            wholeList.clear();
            requestHttp(0);
        } else if (tag == 2) {
            toBeshippedList.clear();
            requestHttp(2);
        } else if (tag == 3) {
            shippedList.clear();
            requestHttp(3);
        } else if (tag == 4) {
            completedList.clear();
            requestHttp(4);
        } else if (tag == 5) {
            canceledList.clear();
            requestHttp(5);
        }
        adapter.notifyDataSetChanged();
    }

    private void initView(View view) {
        // 头部选择
        cagetoryGroup = (RadioGroup) view.findViewById(R.id.cagetoryGroup);
        whole = (RadioButton) view.findViewById(R.id.wholeItem);
        to_be_shipped = (RadioButton) view.findViewById(R.id.to_be_shipped);
        shipped = (RadioButton) view.findViewById(R.id.shipped);
        completed = (RadioButton) view.findViewById(R.id.completed);
        canceled = (RadioButton) view.findViewById(R.id.canceled);
        // 展示列表
        mPullToRefreshListView = (PullToRefreshListView) view
                .findViewById(R.id.eleListView);
        ll_wu = (LinearLayout) view.findViewById(R.id.ll_wu);
    }

    private void initValues() {
        initPull();
        mListView = mPullToRefreshListView.getRefreshableView();
        adapter = new MyOrderdapter(activity, toBeshippedList, this);
        mListView.setAdapter(adapter);
        // 默认选中待发货页面
        to_be_shipped.setChecked(true);
        toBeshippedList.clear();
        requestHttp(2);
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
                        pageNumber = 1;
                        if (tag == 0) {
                            wholeList.clear();
                            requestHttp(0);
                        } else if (tag == 2) {
                            toBeshippedList.clear();
                            requestHttp(2);
                        } else if (tag == 3) {
                            shippedList.clear();
                            requestHttp(3);
                        } else if (tag == 4) {
                            completedList.clear();
                            requestHttp(4);
                        } else if (tag == 5) {
                            canceledList.clear();
                            requestHttp(5);
                        }
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        pageNumber++;
                        if (tag == 0) {
                            pageNumber++;
                            requestHttp(0);
                        } else if (tag == 2) {
                            pageNumber++;
                            requestHttp(2);
                        } else if (tag == 3) {
                            pageNumber++;
                            requestHttp(3);
                        } else if (tag == 4) {
                            pageNumber++;
                            requestHttp(4);
                        } else if (tag == 5) {
                            pageNumber++;
                            requestHttp(5);
                        }
                    }
                });
    }

    private void bindEvents() {
        cagetoryGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            // 订单状态 已发货 3，已完成 4 待发货 2，退货单 6 全部 0
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.wholeItem) {
                    tag = 0;
                    pageNumber = 1;
                    wholeList.clear();
                    adapter.updateListView(wholeList, 0);
                    requestHttp(0);
                    loading();
                } else if (checkedId == R.id.to_be_shipped) {
                    tag = 2;
                    pageNumber = 1;
                    toBeshippedList.clear();
                    adapter.updateListView(toBeshippedList, 2);
                    requestHttp(2);
                    loading();
                } else if (checkedId == R.id.shipped) {
                    tag = 3;
                    pageNumber = 1;
                    shippedList.clear();
                    adapter.updateListView(shippedList, 3);
                    requestHttp(3);
                    loading();
                } else if (checkedId == R.id.completed) {
                    tag = 4;
                    pageNumber = 1;
                    completedList.clear();
                    adapter.updateListView(completedList, 4);
                    requestHttp(4);
                    loading();
                } else if (checkedId == R.id.canceled) {
                    tag = 5;
                    pageNumber = 1;
                    canceledList.clear();
                    adapter.updateListView(canceledList, 5);
                    requestHttp(5);
                    loading();
                }
            }
        });
    }

    // 订单状态0全部 2.待发货,3.已发货,4.交易完成,5.已取消
    private void requestHttp(int tag) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("order_state", tag + "");
        map.put("pageNumber", pageNumber + "");
        map.put("pageSize", pageSize + "");
        mQueue.add(ParamTools.packParam(Constants.shopOrderList, this, this,
                map));
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.getInt("status");
            String msg = json.getString("msg");
            if (stauts == 0) {
                if (url.contains(Constants.shopOrderList)) {
                    JSONArray jsonArray = json.getJSONArray("result");
                    mPullToRefreshListView.onPullDownRefreshComplete();
                    mPullToRefreshListView.onPullUpRefreshComplete();
                    if (jsonArray.length() > 0) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<OrderModel>>() {
                        }.getType();
                        List<OrderModel> list = gson.fromJson(
                                jsonArray.toString(), type);
                        // 订单状态 已发货 3，已完成 4 待发货 2，退货单 6 全部 0
                        if (list.size() < 10) {
                            mPullToRefreshListView.setHasMoreData(false);
                        }

                        if (list.size() > 0) { // 是否有数据的显示情况
                            ll_wu.setVisibility(View.GONE);
                            mPullToRefreshListView.setVisibility(View.VISIBLE);
                        } else {
                            ll_wu.setVisibility(View.VISIBLE);
                            mPullToRefreshListView.setVisibility(View.GONE);
                        }

                        if (tag == 0) {
                            wholeList.addAll(list);
                            adapter.updateListView(wholeList, 0);
                        } else if (tag == 2) {
                            toBeshippedList.addAll(list);
                            adapter.updateListView(toBeshippedList, 2);
                        } else if (tag == 3) {
                            shippedList.addAll(list);
                            adapter.updateListView(shippedList, 3);
                        } else if (tag == 4) {
                            completedList.addAll(list);
                            adapter.updateListView(completedList, 4);
                        } else if (tag == 5) {
                            canceledList.addAll(list);
                            adapter.updateListView(canceledList, 5);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else if (url.contains(Constants.orDeleteOrder)) {
                    Tools.showToast(mcontext, "删除订单成功");
                    switch (mOrderTypeFrom) {
                        case 0: // 全部
                            wholeList.remove(mPosition);
                            adapter.updateListView(wholeList, 0);
                            break;
                        case 4: // 已完成
                            completedList.remove(mPosition);
                            adapter.updateListView(completedList, 4);
                            break;
                        case 5: // 已取消
                            canceledList.remove(mPosition);
                            adapter.updateListView(canceledList, 5);
                            break;
                        default:
                            break;
                    }
                } else if (url.contains(Constants.sendOut)) {
                    Tools.showToast(mcontext, "发货成功");
                    switch (mOrderTypeFrom) {
                        case 0:
                            wholeList.clear();
                            requestHttp(0);
                            break;
                        case 2:
                            toBeshippedList.remove(mPosition);
                            adapter.updateListView(toBeshippedList, 2);
                            break;
                        default:
                            break;
                    }
                }
            } else if (stauts == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(mcontext, LoginActivity.class, false);
                Tools.showToast(mcontext, "登录过期请重新登录");
                Tools.acts.clear();
            } else {
                Tools.showToast(mcontext, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteOrder(String order_id, int position, int orderTypeFrom) {
        this.mPosition = position;
        this.mOrderTypeFrom = orderTypeFrom; // 在哪个列表进行的操作
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("order_id", order_id);
        mQueue.add(ParamTools.packParam(Constants.orDeleteOrder, this, this,
                map));
    }

    @Override
    public void sendOrder(String order_id, int position, int orderTypeFrom) {
        this.mPosition = position;
        this.mOrderTypeFrom = orderTypeFrom;

        Map<String, String> map = new HashMap<>();
        map.put("order_id", order_id);
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.sendOut, this, this, map));
    }
}
