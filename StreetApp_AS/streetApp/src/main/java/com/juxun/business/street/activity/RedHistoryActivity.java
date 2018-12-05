package com.juxun.business.street.activity;


import android.os.Bundle;
import android.view.View;
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
 * @author RedPacketActivity 历史红包列表
 */
public class RedHistoryActivity extends BaseActivity {

    @ViewInject(R.id.button_back)
    private ImageView button_back;// 返回
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_red_history);
        ViewUtils.inject(this);
        initView();
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.button_back})
    public void clickMethod(View v) {
        if (v.getId() == R.id.button_back) {
            finish();
        }
    }

    private void initView() {
        // View view =
        // LayoutInflater.from(this).inflate(R.layout.red_packet_img_itm, null);
        initPull();
        redPacketBeans = new ArrayList<RedPacketBean>();
        redPacketAdapter = new RedPacketAdapter(this, redPacketBeans);
        mListview.setAdapter(redPacketAdapter);
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
        map.put("state", "0");
        map.put("pageNumber", pageNumber + "");
        map.put("pageSize", 10 + "");
        mQueue.add(ParamTools.packParam(Constants.drawRedPacketList, this, this, map));
        loading();
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
            int stauts = json.optInt("status");
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

            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, "数据解析错误");
        }
    }

}
