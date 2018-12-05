package com.juxun.business.street.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.CouponHisAdapter;
import com.juxun.business.street.bean.CouponHisBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.ClearEditText;
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
import static com.yl.ming.efengshe.R.id.ed_clear;

public class CouponHisSearchActivity extends BaseActivity {

    @Bind(R.id.button_back)
    ImageView buttonBack;
    @Bind(ed_clear)
    ClearEditText edClear;
    @Bind(R.id.tv_search)
    TextView tvSearch;
    @Bind(R.id.rl_top)
    LinearLayout rlTop;
    @Bind(R.id.lv_list)
    ListView lvList;
    @Bind(R.id.ll_wu)
    LinearLayout llWu;
    private CouponHisAdapter couponHisAdapter;
    private String activity_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_his_search);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        //列表中的数据
        couponHisAdapter = new CouponHisAdapter(this);
        activity_id = getIntent().getStringExtra("activity_id");
        lvList.setAdapter(couponHisAdapter);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    @OnClick({R.id.button_back, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.tv_search:
                if (!TextUtils.isEmpty(edClear.getText())) {
                    getCouponList(edClear.getText().toString());
                }
                break;
        }
    }

    private void getCouponList(String s) {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("activity_id", activity_id);
        map.put("draw_state", "0");
        map.put("pageNumber", "1");
        map.put("pageSize", 10 + "");
        map.put("search_key", s);
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
}
