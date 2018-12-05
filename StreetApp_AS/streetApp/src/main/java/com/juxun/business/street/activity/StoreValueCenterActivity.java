package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.StoreCenAndHisAdapter;
import com.juxun.business.street.bean.StoreCenterBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *储值中心页面 
 */
public class StoreValueCenterActivity extends BaseActivity implements
        OnClickListener {
    // 顶部标题栏目
    @ViewInject(R.id.button_back)
    private ImageView button_back; // 退回按钮
    @ViewInject(R.id.button_add)
    private ImageButton button_add; // 退回按钮

    // 头部数值显示
    @ViewInject(R.id.tv_total_num)
    private TextView tv_total_num; // 充值数值
    @ViewInject(R.id.tv_total_num_after)
    private TextView tv_total_num_after;// 累计充值说明
    @ViewInject(R.id.tv_canuse_num)
    private TextView tv_canuse_num;// 可用余额
    @ViewInject(R.id.tv_totaluse_num)
    private TextView tv_totaluse_num;// 累计消费

    // 用户统计
    @ViewInject(R.id.tv_userstatistic)
    private TextView tv_userstatistic;

    // 活动列表
    @ViewInject(R.id.lv_acts)
    private ListView lv_acts;// 展示列表
    @ViewInject(R.id.ll_none)
    private LinearLayout ll_none;// 没有活动的列表展示内容
    @ViewInject(R.id.tv_his_acts)
    private TextView tv_his_acts;// 历史活动记录

    private StoreCenAndHisAdapter mStoreCenAndHisAdapter;
    private List<StoreCenterBean> mStoreCardsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storecenter); // 与【采购订单】取消订单布局一致
        ViewUtils.inject(this);
        initViewEvents();
    }

    private void initViewEvents() {
        button_back.setOnClickListener(this);
        button_add.setOnClickListener(this);
        tv_userstatistic.setOnClickListener(this);
        tv_his_acts.setOnClickListener(this);

        lv_acts.setSelector(R.color.transparent);
        lv_acts.setDividerHeight(0);
        lv_acts.setDivider(null);
        mStoreCenAndHisAdapter = new StoreCenAndHisAdapter(this,
                mStoreCardsList);
        lv_acts.setAdapter(mStoreCenAndHisAdapter);
        lv_acts.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(StoreValueCenterActivity.this,
                        StoreActsActivity.class);
                StoreCenterBean storeCenterBean = mStoreCardsList.get(arg2);
                intent.putExtra("stores", storeCenterBean);
                intent.putExtra("name", storeCenterBean.getName());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 数据请求
        requestIndexDatas();
    }

    /**
     * 请求储值中心的首页数据
     */
    private void requestIndexDatas() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        mQueue.add(ParamTools.packParam(Constants.storeValueCenterIndex, this,
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
            JSONObject list = json.optJSONObject("result");

            if (status == 0) {
                bindUpperViews(list);
                mStoreCardsList = JSON.parseArray(
                        list.optString("activity_list"),
                        StoreCenterBean.class);
                if (mStoreCardsList != null && mStoreCardsList.size() != 0) {
                    lv_acts.setVisibility(View.VISIBLE);
                    tv_his_acts.setVisibility(View.VISIBLE);
                    mStoreCenAndHisAdapter
                            .updateHistoryList(mStoreCardsList, 0);
                } else {
                    ll_none.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(StoreValueCenterActivity.this, "解析错误");
        }
    }

    /*
     * 头部展示的数据
     */
    private void bindUpperViews(JSONObject json) {
        tv_total_num.setText(Tools.getFenYuan(json.optInt("total_recharge_price")) + "");
        tv_canuse_num.setText(Tools.getFenYuan(json.optInt("balance")) + "");
        tv_totaluse_num.setText(Tools.getFenYuan(json.optInt("useMoney")) + "");

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.button_add:
                Tools.jump(StoreValueCenterActivity.this, AddStoreActivity.class,
                        false);
                break;
            case R.id.tv_userstatistic:
                Tools.jump(StoreValueCenterActivity.this,
                        UserStatisticsctivity.class, false);
                break;
            case R.id.tv_his_acts:
                intent.setClass(this, StoreHistoryActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
