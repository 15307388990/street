package com.juxun.business.street.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.SuperChannelAdapter;
import com.juxun.business.street.adapter.SupplierGoodsAdapter;
import com.juxun.business.street.bean.ChannelBean;
import com.juxun.business.street.bean.ShopingCartBean;
import com.juxun.business.street.bean.ShopingCartBean2;
import com.juxun.business.street.bean.SupplierGoodsBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshGridView;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.juxun.business.street.config.Constants.shoppingCart;
import static com.yl.ming.efengshe.R.id.ll_wu;
import static com.yl.ming.efengshe.R.id.tv_class;

/**
 * 采购列表
 */

public class SupplierShopListActivity extends BaseActivity {

    @Bind(R.id.button_back)
    ImageView buttonBack;

    @Bind(R.id.tv_class)
    TextView tvClass;
    @Bind(R.id.ll_class)
    LinearLayout ll_class;
    @Bind(R.id.iv_class)
    ImageView ivClass;

    @Bind(R.id.ll_sale)
    LinearLayout ll_sale;
    @Bind(R.id.tv_sale)
    TextView tvSale;
    @Bind(R.id.iv_sale)
    ImageView ivSale;

    @Bind(R.id.ll_price)
    LinearLayout ll_price;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.iv_price)
    ImageView ivPrice;

    @Bind(R.id.refreshGridView)
    PullToRefreshGridView refreshGridView;
    @Bind(ll_wu)
    LinearLayout llWu;

    @Bind(R.id.tv_number)
    TextView tvNumber;  //购物车的商品数量
    @Bind(R.id.rl_shopcart)
    RelativeLayout rlShopcart;

    private int pageNumber = 1;
    private String commodity_name = "";
    private int service_agency_id = 0;
    private int order_by = 1;   //1默认，2销量排序，3价格排序
    private String order_by_type = "desc";  // * desc asc 默认desc降序
    private PopupWindow popupWindow;
    private GridView gridView;
    private SupplierGoodsAdapter supplierGoodsAdapter;
    private List<SupplierGoodsBean> supplierGoodsBeenList;
    private int colnum = 3; //列表需要显示的列数
    private List<ChannelBean> groupList;
    private List<ChannelBean> chidList;
    private SuperChannelAdapter groupAdapter;
    private SuperChannelAdapter chidAdapter;
    private int groupint, chidint;  //父频道、子频道显示的位置
    private ListView lv_group;
    private ListView lv_chid;
    private int SuperpageNumber;
    private int firstChannelId = -1;
    private int secondChannelId = -1;
    private int goodsOrigin = 0;
    String json = "";
    private String channel = "全部";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_shop_list);
        ButterKnife.bind(this);
        Tools.webacts.add(this);
        json = getIntent().getStringExtra("json");
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                firstChannelId = jsonObject.getInt("first_channel_id");
                secondChannelId = jsonObject.getInt("second_channel_id");
                channel = jsonObject.optString("channel_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getSupplierCommodityList();
        initView();
    }

    @Override
    protected void onResume() {
        shoppingCart();
        super.onResume();
    }

    @Override
    protected void onPause() {
        goodsOrigin = 0;
        super.onPause();
    }

    private void shoppingCart() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        mQueue.add(ParamTools.packParam(shoppingCart, this, this, map));
    }

    private void initView() {
        tvClass.setText(channel);
        refreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                pageNumber = 1;
                getSupplierCommodityList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                pageNumber++;
                getSupplierCommodityList();
            }
        });
        refreshGridView.setPullLoadEnabled(true);
        refreshGridView.setScrollLoadEnabled(false);
        gridView = refreshGridView.getRefreshableView();
        gridView.setNumColumns(colnum);
        supplierGoodsAdapter = new SupplierGoodsAdapter(this);
        gridView.setAdapter(supplierGoodsAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SupplierShopListActivity.this, SupplierGoodsInfoActivity.class);
                SupplierGoodsBean supplierGoodsBean = supplierGoodsBeenList.get(position);
                intent.putExtra("commodity_id", supplierGoodsBean.getId() + "");
                startActivity(intent);
            }
        });
        popupWindow = new PopupWindow(this);
    }

    private void getSupplierChannelList() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        mQueue.add(ParamTools.packParam(Constants.getSupplierChannelList, this, this, map));
    }

    //获取商品列表
    private void getSupplierCommodityList() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("pageNumber", pageNumber + "");
        map.put("pageSize", 12 + "");
        map.put("commodity_name", commodity_name);
        if (firstChannelId != -1) {
            map.put("first_level_channel_id", firstChannelId + "");
        }
        if (secondChannelId != -1) {
            map.put("second_level_channel_id", secondChannelId + "");
        }
//        map.put("service_agency_id", service_agency_id + "");
        map.put("order_by", order_by + "");   //排序方式 1、默认排序 时间排序 2、销量排序 * 3、价格排序
        map.put("order_by_type", order_by_type);
        mQueue.add(ParamTools.packParam(Constants.getSupplierCommodityList, this, this, map));
        loading();
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            String msg = json.optString("msg");
            if (url.contains(Constants.getSupplierChannelList)) {
                if (stauts == 0) {
                    groupList = new ArrayList<ChannelBean>();
                    ChannelBean channelBean = new ChannelBean();
                    channelBean.setChannel_name("全部");
                    groupList.add(channelBean);
                    groupList.addAll(JSON.parseArray(json.getString("result"), ChannelBean.class));
                    if (groupList.size() > 0) {
                        chidList = groupList.get(groupint).getChildChannelList();
                        groupAdapter.updateListView(groupList, 0);
                        chidAdapter.updateListView(chidList, 1);
                        groupAdapter.setSelectItem(groupint);
                        chidAdapter.setSelectItem(chidint);
                    } else {
                        Tools.showToast(this, "未查询到频道数据");
                    }
                } else {
                    Tools.showToast(this, msg);
                }
            } else if (url.contains(Constants.getSupplierCommodityList)) {
                if (stauts == 0) {
                    String liString = json.getString("result");
                    List<SupplierGoodsBean> list = JSON.parseArray(liString, SupplierGoodsBean.class);
                    if (pageNumber > 1) {
                        supplierGoodsBeenList.addAll(list);
                    } else {
                        supplierGoodsBeenList = list;
                    }
                    if (list.size() == 12) {
                        refreshGridView.setHasMoreData(true);
                    } else {
                        refreshGridView.setHasMoreData(false);
                    }
                    if (supplierGoodsBeenList != null && supplierGoodsBeenList.size() > 0) {
                        llWu.setVisibility(View.GONE);
                        refreshGridView.setVisibility(View.VISIBLE);
                        supplierGoodsAdapter.updateAdapter(supplierGoodsBeenList);
                    } else {
                        llWu.setVisibility(View.VISIBLE);
                        refreshGridView.setVisibility(View.GONE);
                    }
                    refreshGridView.onPullDownRefreshComplete();
                    refreshGridView.onPullUpRefreshComplete();
                } else {
                    Tools.showToast(this, msg);
                }
            } else if (url.contains(shoppingCart)) {
                if (stauts == 0) {
                    List<ShopingCartBean2> mShopingCartBeans = JSON.parseArray(json.getString("result"),
                            ShopingCartBean2.class);
                    if (mShopingCartBeans != null && mShopingCartBeans.size() > 0) {
                        for (int i = 0; i < mShopingCartBeans.size(); i++) {
                            goodsOrigin = goodsOrigin + mShopingCartBeans.get(i).getMsg_count();
                        }
                        tvNumber.setVisibility(View.VISIBLE);
                        tvNumber.setText(goodsOrigin + "");
                    } else {
                        tvNumber.setVisibility(View.GONE);
                    }
                } else {
                    Tools.showToast(this, msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    @OnClick({R.id.button_back, tv_class, R.id.tv_sale, R.id.tv_price, R.id.rl_shopcart})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.button_back:
                finish();
                break;
            case tv_class:
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    initPupopWindow();
                }
                break;
            case R.id.tv_sale:
                order_by = 2;
                ivPrice.setImageResource(R.drawable.sort_sign_none);
                if (order_by_type == "desc") {
                    order_by_type = "asc";
                    ivSale.setImageResource(R.drawable.sort_sign_down_s);
                } else {
                    order_by_type = "desc";
                    ivSale.setImageResource(R.drawable.sort_sign_up_s);
                }
                getSupplierCommodityList();
                break;
            case R.id.tv_price:
                order_by = 3;
                ivSale.setImageResource(R.drawable.sort_sign_none);
                if (order_by_type == "desc") {
                    order_by_type = "asc";
                    ivPrice.setImageResource(R.drawable.sort_sign_down_s);
                } else {
                    order_by_type = "desc";
                    ivPrice.setImageResource(R.drawable.sort_sign_up_s);
                }
                getSupplierCommodityList();
                break;
            case R.id.rl_shopcart:
                intent.setClass(SupplierShopListActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
                break;
        }
    }

    // 分类弹框
    private void initPupopWindow() {
        ivClass.setImageResource(R.drawable.icon_arrow_up);
        // 初始化popupWindow
        View leftPopView = LayoutInflater.from(this).inflate(R.layout.super_pop_class, null);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setContentView(leftPopView);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        // 数据逻辑处理
        groupList = new ArrayList<>();
        chidList = new ArrayList<>();
        groupAdapter = new SuperChannelAdapter(this, groupList, 0);
        chidAdapter = new SuperChannelAdapter(this, chidList, 1);
        groupAdapter.setSelectItem(groupint); // 根据id查找此时选中的是什么？

        // UI操作
        lv_group = (ListView) leftPopView.findViewById(R.id.lv_group);
        lv_chid = (ListView) leftPopView.findViewById(R.id.lv_chid);

        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                groupint = arg2;
                groupAdapter.setSelectItem(groupint);
                if (arg2 == 0) {
                    tvClass.setText(groupList.get(groupint).getChannel_name());
                    firstChannelId = -1;
                    secondChannelId = -1;
                    getSupplierCommodityList();
                    popupWindow.dismiss();
                    ivClass.setImageResource(R.drawable.icon_arrow_down);
                    return;
                }
                chidList = groupList.get(arg2).getChildChannelList();
                chidAdapter.updateListView(chidList, 1);
                SuperpageNumber = 1;
                firstChannelId = groupList.get(groupint).getId();
                tvClass.setText(groupList.get(groupint).getChannel_name());
                int size = chidList.size();
                // 如果该列表下只有一个全部
                if (size == 0) {
                    tvClass.setText(groupList.get(groupint).getChannel_name());
                    secondChannelId = -1;
                    chidint = 0;
                    getSupplierCommodityList();
                    popupWindow.dismiss();
                    ivClass.setImageResource(R.drawable.icon_arrow_down);
                    chidAdapter.setSelectItem(0);
                }

            }
        });
        lv_chid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                SuperpageNumber = 1;
                chidint = arg2;
                lv_chid.setSelection(0);
                chidAdapter.setSelectItem(chidint);
                if (arg2 == 0) {
                    tvClass.setText(groupList.get(groupint).getChannel_name());
                    secondChannelId = 0;
                } else {
                    tvClass.setText(chidList.get(arg2).getChannel_name());
                    secondChannelId = chidList.get(arg2).getId();
                }
                popupWindow.dismiss();
                ivClass.setImageResource(R.drawable.icon_arrow_down);
                getSupplierCommodityList();
            }
        });
        lv_group.setAdapter(groupAdapter);
        lv_chid.setAdapter(chidAdapter);
        getSupplierChannelList();
        TextView tv_class_close = (TextView) leftPopView.findViewById(R.id.tv_colse);
//        tv_class_close.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//                ivClass.setImageResource(R.drawable.icon_arrow_down);
//            }
//        });
        leftPopupShow();
    }

    private void leftPopupShow() {
        // popupWindow展示位置设置，7.0系统有区别的
        if (Build.VERSION.SDK_INT >= 24) {
//            int[] location = new int[2];
//            // 记录parent在屏幕中的位置
//            ll_class.getLocationOnScreen(location);
//            int offsetY = location[1];
//            if (Build.VERSION.SDK_INT == 25) {
//                // 重新设置 PopupWindow 的高度
//                popupWindow.setHeight(mDisplayheight - offsetY - ll_class.getHeight());
//            }
//            popupWindow.showAtLocation(ll_class, Gravity.NO_GRAVITY, 0, offsetY + ll_class.getHeight());

            Rect visibleFrame = new Rect();
            ll_class.getGlobalVisibleRect(visibleFrame);
            int height = ll_class.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            popupWindow.setHeight(height);
            popupWindow.showAsDropDown(ll_class, 0, 0);
        } else {
            popupWindow.showAsDropDown(ll_class, 0, 0);
        }
    }
}
