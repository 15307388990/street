package com.juxun.business.street.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.PurchaseOrderdapter;
import com.juxun.business.street.bean.Agreement7;
import com.juxun.business.street.bean.PurchaseOderBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.SwipeBackController;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
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

import static com.juxun.business.street.config.Constants.supplierOrderComplete;

/**
 * @author OrderActivity 采购订单（供应链订单体系）
 */
public class OrderActivity extends BaseActivity
        implements PurchaseOrderdapter.OneCallBack, PurchaseOrderdapter.TwoCallBack {

    @ViewInject(R.id.button_back)
    private ImageView button_back;// 返回
    @ViewInject(R.id.rbt_all)
    private RadioButton rbt_all;// 全部
    @ViewInject(R.id.rbt_obligation)
    private RadioButton rbt_obligation;// 待付款
    @ViewInject(R.id.rbt_forthegoods)
    private RadioButton rbt_forthegoods;// 待收货
    @ViewInject(R.id.rbt_have_the_goods)
    private RadioButton rbt_have_the_goods;// 已收货
    @ViewInject(R.id.lv_list)
    private PullToRefreshListView mPulllistview;// 列表
    @ViewInject(R.id.ll_wu)
    private LinearLayout ll_wu;// 无数据
    private ListView mListview;

    private int orderState = 0;
    private int pageNumber = 1;
    private List<PurchaseOderBean> oderlist;
    private PurchaseOrderdapter orderAapter;
    public SwipeBackController swipeBackController;// 右滑关闭
    private int position;// 被操作的项；

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_purchase_oder);
        ViewUtils.inject(this);
        swipeBackController = new SwipeBackController(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageNumber = 1;
        findOrderList();
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.button_back, R.id.rbt_all, R.id.rbt_obligation, R.id.rbt_forthegoods, R.id.rbt_have_the_goods})
    public void clickMethod(View v) {
        if (v.getId() == R.id.button_back) {
            finish();
        } else if (v.getId() == R.id.rbt_all) {
            orderState = 0;
            pageNumber = 1;
            mListview.setSelection(0);
            findOrderList();
        } else if (v.getId() == R.id.rbt_obligation) {
            orderState = 1;
            pageNumber = 1;
            mListview.setSelection(0);
            findOrderList();
        } else if (v.getId() == R.id.rbt_forthegoods) {
            orderState = 3;
            pageNumber = 1;
            mListview.setSelection(0);
            findOrderList();
        } else if (v.getId() == R.id.rbt_have_the_goods) {
            orderState = 4;
            mListview.setSelection(0);
            pageNumber = 1;
            findOrderList();
        }
    }

    private void initView() {
        //【1.待付款,2.待发货,3.已发货,4.交易完成,5.已取消,6.已退货】0全部
        orderState = getIntent().getIntExtra("orderState", 0);  //支付界面的跳转、StoreFragment的跳转
        switch (orderState) {
            case 0:
                rbt_all.setChecked(true);
                break;
            case 1:
                rbt_obligation.setChecked(true);
                break;
            case 3:
                rbt_forthegoods.setChecked(true);
                break;
            case 4:
                rbt_have_the_goods.setChecked(true);
                break;
            default:
                break;
        }
        //页面初始化控件
        initPull();
        oderlist = new ArrayList<>();
        orderAapter = new PurchaseOrderdapter(this, oderlist, this, this);
        mListview.setAdapter(orderAapter);
        mListview.setDivider(null);
        mListview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String url = Constants.mainUrl + "/supplier/purchaseOrderInfo.html?orderId="
//                        + oderlist.get(position).getId()  + "&auth_token="
//                        + partnerBean.getAuth_token();
//                Intent intent = new Intent(OrderActivity.this, WebviewActivity.class);
//                Agreement7 agreement7 = new Agreement7();
//                agreement7.setLink_url(url);
//                agreement7.setTitle("订单详情");
//                intent.putExtra("agreement7", agreement7);
//                startActivityForResult(intent, 2);
                Intent intent = new Intent(OrderActivity.this, PurchaseOrderDetailsActivity.class);
                intent.putExtra("mPurchaseOderBean", oderlist.get(position));
                startActivity(intent);
            }
        });
    }

    private void initPull() {
        mPulllistview.setPullLoadEnabled(false);
        mPulllistview.setScrollLoadEnabled(true);
        mListview = mPulllistview.getRefreshableView();
        mPulllistview.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber = 1;
                findOrderList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber++;
                findOrderList();
            }
        });

    }

    // 确认订单 modifyOrderState
    private void supplierOrderComplete(int id) {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("order_id", id + "");
        mQueue.add(ParamTools.packParam(supplierOrderComplete, this, this, map));
    }

    // 删除订单
    private void deleteOrder(int id, int state) {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("order_id", id + "");
        mQueue.add(ParamTools.packParam(Constants.deleteOrder, this, this, map));
    }

    // 获取合伙人采购订单
    private void findOrderList() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("order_state", orderState + "");    //【1.待付款,2.待发货,3.已发货,4.交易完成,5.已取消,6.已退货,7退款中8退款失败】
        map.put("pageNumber", pageNumber + "");
        map.put("pageSize", 10 + "");
        mQueue.add(ParamTools.packParam(Constants.findOrderList, this, this, map));
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.optInt("status");
            String msg = json.optString("msg");
            if (status == 0) {
                if (url.contains(Constants.findOrderList)) {
                    List<PurchaseOderBean> list = JSON.parseArray(json.getString("result"), PurchaseOderBean.class);
                    if (list.size() < 10) {
                        mPulllistview.setHasMoreData(false);
                    }

                    if (pageNumber > 1) {
                        oderlist.addAll(list);
                    } else {
                        oderlist = list;
                    }

                    if (oderlist.size() > 0) {
                        mPulllistview.setVisibility(View.VISIBLE);
                        ll_wu.setVisibility(View.GONE);
                    } else {
                        mPulllistview.setVisibility(View.GONE);
                        ll_wu.setVisibility(View.VISIBLE);
                    }

                    mPulllistview.onPullDownRefreshComplete();
                    mPulllistview.onPullUpRefreshComplete();
                    orderAapter.updateListView(oderlist);
                } else if (url.contains(Constants.deleteOrder)) {
                    // 删除订单 本地操作
                    if (oderlist != null) {
                        oderlist.remove(position);
                        orderAapter.updateListView(oderlist);
                    }
                } else if (url.contains(supplierOrderComplete)) {
                    // 确认收货
                    if (oderlist != null) {
                        oderlist.get(position).setOrder_state(4);
                        orderAapter.updateListView(oderlist);
                    }
                }
            } else if (status == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(this, LoginActivity.class, true);
                Tools.showToast(this, "登录过期请重新登录");
                Tools.exit();
            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, getResources().getString(R.string.tips_unkown_error));
        }
    }

    @Override
    public void onTwo(int type, int position) {
        if (type == 1) {
//            modifyOrderState(oderlist.get(position).getId(), 4);
            supplierOrderComplete(oderlist.get(position).getId());
            this.position = position;
        }
    }

    @Override
    public void onOne(int type, int position) {
        this.position = position;
        if (type == 1) {
            Intent intent = new Intent(OrderActivity.this, CancelReasonActivity.class);
            intent.putExtra("order_id", oderlist.get(position).getId() + "");
            startActivityForResult(intent, 1);
        } else if (type == 2) {
            deleteOrder(oderlist.get(position).getId(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                int state = data.getIntExtra("state", 0);
                // 订单状态1.待付款,2.待发货,3.已发货,4.交易完成,5.已取消,6.已退货",
                if (orderState == 0) {
                    oderlist.get(position).setOrder_state(state);
                } else {
                    oderlist.remove(position);
                }
                orderAapter.updateListView(oderlist);
            } else if (requestCode == 2) {
                // 从订单详情回来
                int state = data.getIntExtra("state", 0);
                if (state == 4) {
                    // 确认收货
                    if (orderState == 0) {
                        oderlist.get(position).setOrder_state(4);
                    } else {
                        oderlist.remove(position);
                    }
                } else if (state == 7) {
                    // 删除订单
                    oderlist.remove(position);
                } else if (state == 8) {
                    // 申请售后 成功 TODO 售后申请的字段
//                    oderlist.get(position).setCustomer_service_status(1);
                } else {
                    // 取消订单
                    if (orderState == 0) {
                        oderlist.get(position).setOrder_state(state);
                    } else {
                        oderlist.remove(position);
                    }
                }
                orderAapter.updateListView(oderlist);
            }
        }
    }
}
