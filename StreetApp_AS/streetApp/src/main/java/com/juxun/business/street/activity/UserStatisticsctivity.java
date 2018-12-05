package com.juxun.business.street.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.UserStatisticsAdapter;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.UserStatisticsBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshListView;
import com.ming.ui.PullToRefreshBase.OnRefreshListener;
import com.yl.ming.efengshe.R;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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

/**
 * @author luoming 用户统计
 */
public class UserStatisticsctivity extends BaseActivity implements OnClickListener {
    @ViewInject(R.id.ll_chongzhi)
    private LinearLayout ll_chongzhi;
    @ViewInject(R.id.tv_chongzhi)
    private TextView tv_chongzhi;
    @ViewInject(R.id.iv_chongzhi)
    private ImageView iv_chongzhi;

    @ViewInject(R.id.ll_xiaofei)
    private LinearLayout ll_xiaofei;
    @ViewInject(R.id.tv_xiaofei)
    private TextView tv_xiaofei;
    @ViewInject(R.id.iv_xiaofei)
    private ImageView iv_xiaofei;

    @ViewInject(R.id.ll_jine)
    private LinearLayout ll_jine;
    @ViewInject(R.id.tv_jine)
    private TextView tv_jine;
    @ViewInject(R.id.iv_jine)
    private ImageView iv_jine;

    @ViewInject(R.id.ll_wu)
    private LinearLayout ll_wu;
    @ViewInject(R.id.lv_list)
    private PullToRefreshListView lv_list;
    private ListView mListView;
    private int filterSort;// 排序， 1 充值金额 倒序，2 充值金额 顺序，3 消费金额倒序 4消费金额顺序 5 余额倒序
    // 6余额顺序
    private List<UserStatisticsBean> lists;
    private UserStatisticsAdapter userStatisticsAdapter;

    @ViewInject(R.id.button_back)
    private ImageView button_back;
    @ViewInject(R.id.textview_title)
    private TextView textview_title;
    @ViewInject(R.id.button_search)
    private ImageButton button_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userstatistics);
        ViewUtils.inject(this);
        textview_title.setText("用户统计");
        button_search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Tools.jump(UserStatisticsctivity.this, UserStatisticsSearchActivity.class, false);
            }
        });
        button_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initPull();
        initOclik();
        getMemberRecordStatices();

    }

    private void initOclik() {
        lists = new ArrayList<UserStatisticsBean>();
        userStatisticsAdapter = new UserStatisticsAdapter(this, lists);
        mListView.setAdapter(userStatisticsAdapter);
        ll_chongzhi.setOnClickListener(this);
        ll_xiaofei.setOnClickListener(this);
        ll_jine.setOnClickListener(this);
//        mListView.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(UserStatisticsctivity.this, UserDetailsActivity.class);
//                intent.putExtra("nameString", lists.get(position).getMember_phone());
//                startActivity(intent);
//            }
//        });
    }

    private void initPull() {
        lv_list.setPullLoadEnabled(false);
        lv_list.setScrollLoadEnabled(false);
        mListView = lv_list.getRefreshableView();
        //mListView.setDivider(new ColorDrawable(R.color.gray));
        mListView.setDividerHeight(0);
        lv_list.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getMemberRecordStatices();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });
    }

    private void initBtn() {
        initState();
        switch (filterSort) {
            case 1:
                tv_chongzhi.setTextColor(getResources().getColor(R.color.blue));
                iv_chongzhi.setImageResource(R.drawable.sort_sign_down_s);
                break;
            case 2:
                tv_chongzhi.setTextColor(getResources().getColor(R.color.blue));
                iv_chongzhi.setImageResource(R.drawable.sort_sign_up_s);
                break;
            case 3:
                tv_xiaofei.setTextColor(getResources().getColor(R.color.blue));
                iv_xiaofei.setImageResource(R.drawable.sort_sign_down_s);
                break;
            case 4:
                tv_xiaofei.setTextColor(getResources().getColor(R.color.blue));
                iv_xiaofei.setImageResource(R.drawable.sort_sign_up_s);
                break;
            case 5:
                tv_jine.setTextColor(getResources().getColor(R.color.blue));
                iv_jine.setImageResource(R.drawable.sort_sign_down_s);
                break;
            case 6:
                tv_jine.setTextColor(getResources().getColor(R.color.blue));
                iv_jine.setImageResource(R.drawable.sort_sign_up_s);
                break;
            default:
                break;
        }
        getMemberRecordStatices();
    }

    // 用户统计
    private void getMemberRecordStatices() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        mQueue.add(ParamTools.packParam(Constants.getMemberRecordStatices, this, this, map));
    }

    /**
     * 初始化 头部状态栏
     */
    private void initState() {
        tv_chongzhi.setTextColor(getResources().getColor(R.color.actionbar_title_color));
        tv_xiaofei.setTextColor(getResources().getColor(R.color.actionbar_title_color));
        tv_jine.setTextColor(getResources().getColor(R.color.actionbar_title_color));
        iv_chongzhi.setImageResource(R.drawable.sort_sign_none);
        iv_xiaofei.setImageResource(R.drawable.sort_sign_none);
        iv_jine.setImageResource(R.drawable.sort_sign_none);

    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.getInt("status");
            String msg = json.getString("msg");
            if (stauts == 0) {
                if (url.contains(Constants.getMemberRecordStatices)) {
                    String memberRechargeRecordList = json.getString("result");
                    lists = JSON.parseArray(memberRechargeRecordList, UserStatisticsBean.class);
                    if (lists.size() > 0) {
                        ll_wu.setVisibility(View.GONE);
                        lv_list.setVisibility(View.VISIBLE);
                        userStatisticsAdapter.Update(lists);
                    } else {
                        ll_wu.setVisibility(View.VISIBLE);
                        lv_list.setVisibility(View.GONE);
                    }

                }
            } else if (stauts == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(UserStatisticsctivity.this, LoginActivity.class, false);
                Tools.showToast(UserStatisticsctivity.this, "登录过期请重新登录");
                Tools.acts.clear();
            } else {
                Tools.showToast(UserStatisticsctivity.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.ll_chongzhi:
                if (filterSort == 1) {
                    filterSort = 2;
                } else {
                    filterSort = 1;
                }
                break;
            case R.id.ll_xiaofei:
                if (filterSort == 3) {
                    filterSort = 4;
                } else {
                    filterSort = 3;
                }
                break;
            case R.id.ll_jine:
                if (filterSort == 5) {
                    filterSort = 6;
                } else {
                    filterSort = 5;
                }
                break;
            default:
                break;
        }
        initBtn();

    }

}
