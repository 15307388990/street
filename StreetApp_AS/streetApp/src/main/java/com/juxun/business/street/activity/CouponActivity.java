package com.juxun.business.street.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.CouponListAdapter;
import com.juxun.business.street.bean.CouponListBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshListView;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 代金券列表
 */
public class CouponActivity extends BaseActivity {

    @Bind(R.id.button_back)
    ImageView buttonBack;
    @Bind(R.id.textview_title)
    TextView textviewTitle;
    @Bind(R.id.button_function)
    TextView buttonFunction;
    @Bind(R.id.lv_list)
    PullToRefreshListView lv_list;
    @Bind(R.id.ll_wu)
    LinearLayout llWu;

    private ListView mListView;
    private CouponListAdapter couponListAdapter;
    private int pageNumber = 1; //列表请求第几页
    private List<CouponListBean> mStoreCardsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        initPull();
        couponListAdapter = new CouponListAdapter(this);
        mListView.setAdapter(couponListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CouponActivity.this, CouponAtcsActivity.class);
                intent.putExtra("mStoreCardsListBean", mStoreCardsList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCouponList();
    }

    /**
     * 请求代金券列表数据
     */
    private void getCouponList() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("pageNumber", pageNumber + "");
        map.put("pageSize", 100 + "");
        mQueue.add(ParamTools.packParam(Constants.getCouponList, this,
                this, map));
        loading();
    }


    private void initPull() {
        lv_list.setPullLoadEnabled(false);
        lv_list.setScrollLoadEnabled(false);
        mListView = lv_list.getRefreshableView();
        mListView.setDivider(new ColorDrawable(getResources().getColor(R.color.gray)));
        mListView.setDividerHeight(0);
        lv_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });

    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.optInt("status");
            String msg = json.optString("msg");
            if (status == 0) {
                mStoreCardsList = JSON.parseArray(json.optString("result"), CouponListBean.class);

                if (mStoreCardsList != null && mStoreCardsList.size() != 0) {
                    lv_list.setVisibility(View.VISIBLE);
                    llWu.setVisibility(View.GONE);
                    couponListAdapter.updateAdapter(mStoreCardsList);
                } else {
                    llWu.setVisibility(View.VISIBLE);
                    lv_list.setVisibility(View.GONE);
                }
            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, "解析错误");
        }
    }

    @OnClick({R.id.button_back, R.id.button_function})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.button_function:
                //新增代金券卡片
                Tools.jump(this, AddCouponActivity.class, false);
                break;
        }
    }
}
