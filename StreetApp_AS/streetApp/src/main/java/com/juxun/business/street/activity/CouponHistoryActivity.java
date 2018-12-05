package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.CouponHisAdapter;
import com.juxun.business.street.bean.CouponHisBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshListView;
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

import static com.juxun.business.street.config.Constants.getCouponHistory;

/**
 * 代金券  领取记录
 */
public class CouponHistoryActivity extends BaseActivity {

    @Bind(R.id.button_back)
    ImageView buttonBack;
    @Bind(R.id.right_img)
    ImageView rightImg;

    @Bind(R.id.rl_top)
    RelativeLayout rlTop;
    @Bind(R.id.rbt_has_used)
    RadioButton rbtHasUsed;
    @Bind(R.id.rbt_not_use)
    RadioButton rbtNotUse;

    @Bind(R.id.lv_list)
    PullToRefreshListView lvList;

    @Bind(R.id.tv_wu)
    TextView tvWu;
    @Bind(R.id.ll_wu)
    LinearLayout llWu;

    private int pageNumber;
    private ListView listView;
    private CouponHisAdapter couponHisAdapter;
    private ArrayList<CouponHisBean> couponHisBeanList;
    private String activity_id;
    private int draw_state =2;//draw_state2使用 1未使用 0全部

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_history);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //获取活动的id
        activity_id = getIntent().getStringExtra("activity_id");

        initPull();

        couponHisBeanList = new ArrayList<>();
        couponHisAdapter = new CouponHisAdapter(this);
        listView.setAdapter(couponHisAdapter);
        listView.setDivider(null);
        getCouponHistory();
    }

    private void initPull() {
        rbtHasUsed.setChecked(true);
        lvList.setPullLoadEnabled(false);
        lvList.setScrollLoadEnabled(true);
        listView = lvList.getRefreshableView();
        lvList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber = 1;
                getCouponHistory();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber++;
                getCouponHistory();
            }
        });
    }

    // 领取记录
    private void getCouponHistory() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("activity_id", activity_id);
        map.put("draw_state", draw_state + "");
        map.put("pageNumber", pageNumber + "");
        map.put("pageSize", 10 + "");
        mQueue.add(ParamTools.packParam(getCouponHistory, this, this, map));
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.optInt("status");
            String msg = json.optString("msg");
            if (url.contains(getCouponHistory)) {
                if (status == 0) {
                    List<CouponHisBean> hisBeanList = JSON.parseArray(json.optString("result"), CouponHisBean.class);
                    if (hisBeanList != null && hisBeanList.size() > 0) {
                        couponHisAdapter.updateAdapter(hisBeanList);

                        llWu.setVisibility(View.GONE);
                        lvList.setVisibility(View.VISIBLE);
                    } else {
                        llWu.setVisibility(View.VISIBLE);
                        lvList.setVisibility(View.GONE);
                    }
                } else {
                    Tools.showToast(this, msg);
                }
            } else if (true) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, "数据解析异常");
        }
    }

    @OnClick({R.id.button_back, R.id.right_img, R.id.rbt_has_used, R.id.rbt_not_use})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.right_img:
                //跳转搜索页面
                Intent intent=new Intent(this,CouponHisSearchActivity.class);
                intent.putExtra("activity_id",activity_id);
                this.startActivity(intent);
                break;
            case R.id.rbt_has_used:
                draw_state = 2;
                getCouponHistory();
                break;
            case R.id.rbt_not_use:
                draw_state = 1;
                getCouponHistory();
                break;
        }
    }
}
