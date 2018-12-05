package com.juxun.business.street.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.MessageAdapter;
import com.juxun.business.street.adapter.MessageAdapter.onClickBack;
import com.juxun.business.street.bean.Agreement7;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.MessageBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.SwipeBackController;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshBase.OnRefreshListener;
import com.ming.ui.PullToRefreshListView;
import com.yl.ming.efengshe.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * @author MessageListActivity 消息中心
 */

public class MessageListActivity extends BaseActivity implements onClickBack {
    @ViewInject(R.id.list)
    private PullToRefreshListView mPullList;
    @ViewInject(R.id.ll_wu)
    private LinearLayout ll_wu;

    private int pagenumber = 1;
    private List<MessageBean> messageBeans;
    private ListView mlistview;
    private MessageAdapter mAdapter;
    public SwipeBackController swipeBackController;// 右滑关闭

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message);
        ViewUtils.inject(this);
        swipeBackController = new SwipeBackController(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("消息中心");
        initView();
    }

    private void initView() {
        initPull();
        messageBeans = new ArrayList<MessageBean>();
        mAdapter = new MessageAdapter(this, messageBeans, this);
        mlistview.setAdapter(mAdapter);
        // mlistview.setOnItemClickListener(new OnItemClickListener() {
        //
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view, int
        // position, long id) {
        // String urlString = Constants.mainUrl + Constants.messageInfo +
        // "agency_id="
        // + storeBean.getAdmin_agency() + "&auth_token=" +
        // storeBean.getAuth_token() + "&message_id="
        // + messageBeans.get(position).getId();
        // Intent intent = new Intent(MessageListActivity.this,
        // WebviewActivity.class);
        // intent.putExtra("name", "消息详情");
        // intent.putExtra("url", urlString);
        // startActivity(intent);
        //
        // }
        // });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        messageList();
    }

    /**
     * 消息列表
     */
    private void messageList() {
        // auth_token
        // agency_id
        // page_number 页码
        // page_size 页数
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("pageNumber", pagenumber + "");
        map.put("pageSize", 10 + "");
        mQueue.add(ParamTools.packParam(Constants.messageList, this, this, map));
    }

    private void initPull() {
        mPullList.setPullLoadEnabled(false);
        mPullList.setScrollLoadEnabled(true);
        mlistview = mPullList.getRefreshableView();
        // mlistview.setSelector(R.color.register_bg_color);
        mlistview.setDividerHeight(0);
        mPullList.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pagenumber = 1;
                messageList();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pagenumber++;
                messageList();

            }
        });

    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            String msg = json.optString("msg");
            if (stauts == 0) {
                if (url.contains(Constants.messageList)) {
                    String liString = json.getString("result");
                    List<MessageBean> list = JSON.parseArray(liString, MessageBean.class);
                    if (pagenumber > 1) {
                        messageBeans.addAll(list);
                    } else {
                        messageBeans = list;
                    }
                    if (messageBeans.size() > 0) {
                        mPullList.setVisibility(View.VISIBLE);
                        ll_wu.setVisibility(View.GONE);
                    } else {
                        mPullList.setVisibility(View.GONE);
                        ll_wu.setVisibility(View.VISIBLE);

                    }
                    if (list.size() < 10) {
                        mPullList.setHasMoreData(false);
                    }
                    mAdapter.updateListView(messageBeans);
                    mPullList.onPullDownRefreshComplete();
                    mPullList.onPullUpRefreshComplete();
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

    @Override
    public void onClick(int id) {
        String urlString = Constants.mainUrl + Constants.messageInfo
                + "&auth_token=" + partnerBean.getAuth_token() + "&message_id=" + id;
        Intent intent = new Intent(MessageListActivity.this, WebviewActivity.class);
        Agreement7 agreement7 = new Agreement7();
        agreement7.setLink_url(urlString);
        agreement7.setTitle("消息详情");
        intent.putExtra("agreement7", agreement7);
        startActivity(intent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (swipeBackController.processEvent(event)) {
            return true;
        } else {
            return onTouchEvent(event);
        }

    }
}
