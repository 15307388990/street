package com.juxun.business.street.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.RedPacketAdapter;
import com.juxun.business.street.bean.RedPacketBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
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

/**
 * @author RedPacketActivity 红包列表
 */
public class RedPacketActivity extends BaseActivity {

    @ViewInject(R.id.button_back)
    private ImageView button_back;// 返回
    @ViewInject(R.id.button_function)
    private TextView button_function;// 兑换
    @ViewInject(R.id.lv_list)
    private PullToRefreshListView mPulllistview;// 列表
    @ViewInject(R.id.ll_wu)
    private LinearLayout ll_wu;// 无数据
    private ListView mListview;
    @ViewInject(R.id.tv_red_record)
    private TextView tv_red_record;// 查看红包历史记录

    private int pageNumber = 1;
    private List<RedPacketBean> redPacketBeans;
    private RedPacketAdapter redPacketAdapter;
    private int redState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_red_packet);
        ViewUtils.inject(this);
        initView();
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.button_back, R.id.tv_red_record, R.id.button_function})
    public void clickMethod(View v) {
        if (v.getId() == R.id.button_back) {
            finish();
        } else if (v.getId() == R.id.tv_red_record) {
            // 跳转历史红包记录
            Tools.jump(RedPacketActivity.this, RedHistoryActivity.class, true);
        } else if (v.getId() == R.id.button_function) {
            Intent intent = new Intent(RedPacketActivity.this, RedExchangeActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    private void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.red_packet_img_itm, null);
        initPull();
        mListview.addFooterView(view);
        redPacketBeans = new ArrayList<>();
        redPacketAdapter = new RedPacketAdapter(this, redPacketBeans);
        mListview.setAdapter(redPacketAdapter);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Tools.jump(RedPacketActivity.this, RedHistoryActivity.class, true);
            }
        });
    }

    private void initPull() {
        mPulllistview.setPullLoadEnabled(false);
        mPulllistview.setScrollLoadEnabled(true);
        mListview = mPulllistview.getRefreshableView();
        mListview.setDivider(getResources().getDrawable(R.color.register_bg_color));
        mListview.setDividerHeight(20);
        mPulllistview.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber = 1;
                drawRedPacketList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber++;
                drawRedPacketList();
            }
        });
    }

    // 红包列表
    private void drawRedPacketList() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("pageNumber", pageNumber + "");
        map.put("pageSize", 10 + "");
        map.put("state", redState + "");  //0全部、1可用红包、2不可用红包
        mQueue.add(ParamTools.packParam(Constants.drawRedPacketList, this, this, map));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                drawRedPacketList();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawRedPacketList();
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("stauts");
            String msg = json.optString("msg");
            if (stauts == 0) {
                if (url.contains(Constants.drawRedPacketList)) {
                    String liString = json.getString("result");
                    List<RedPacketBean> list = JSON.parseArray(liString, RedPacketBean.class);
                    if (list.size() < 10) {
                        mPulllistview.setHasMoreData(false);
                    }
                    if (pageNumber > 1) {
                        redPacketBeans.addAll(list);
                    } else {
                        redPacketBeans = list;
                    }
                    if (redPacketBeans.size() > 0) {
                        mPulllistview.setVisibility(View.VISIBLE);
                        ll_wu.setVisibility(View.GONE);
                    } else {
                        mPulllistview.setVisibility(View.GONE);
                        ll_wu.setVisibility(View.VISIBLE);
                    }
                    redPacketAdapter.updateListView(redPacketBeans);
                    mPulllistview.onPullDownRefreshComplete();
                    mPulllistview.onPullUpRefreshComplete();
                }
            } else if (stauts == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(this, LoginActivity.class, true);
                Tools.showToast(this, "登录过期请重新登录");
                Tools.exit();
            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }
}
