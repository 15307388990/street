/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

import com.juxun.business.street.bean.PartnerBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.WheelTimeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 设置营业时间
 */
public class SetBussinessTimeActivity extends BaseActivity {
    @ViewInject(R.id.wv_star_hh)
    private WheelTimeView wv_star_hh;
    @ViewInject(R.id.wv_star_mm)
    private WheelTimeView wv_star_mm;
    @ViewInject(R.id.wv_end_hh)
    private WheelTimeView wv_end_hh;
    @ViewInject(R.id.wv_end_mm)
    private WheelTimeView wv_end_mm;
    @ViewInject(R.id.ll_timer)
    private LinearLayout ll_timer;
    @ViewInject(R.id.cb_swith)
    private CheckBox cb_swith;
    private String[] HH = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13",
            "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "00",};
    private String[] MM = new String[]{"00", "15", "30", "45"};

    private int business_type = 1;
    private String start_time, end_time;
    private PartnerBean mPartnerBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_timer);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("营业时间");
        more.setText("保存");
        more.setVisibility(View.VISIBLE);
        more.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!cb_swith.isChecked()) {
                    if (Integer.valueOf(wv_end_hh.getSeletedItem()) < Integer.valueOf(wv_star_hh.getSeletedItem())) {
                        Tools.showToast(getApplicationContext(), "结束时间必须大于开始时间");
                        return;
                    }
                    if (Integer.valueOf(wv_end_hh.getSeletedItem()) == Integer.valueOf(wv_star_hh.getSeletedItem())
                            && Integer.valueOf(wv_end_mm.getSeletedItem()) <= Integer
                            .valueOf(wv_star_mm.getSeletedItem())) {
                        Tools.showToast(getApplicationContext(), "结束时间必须大于开始时间");
                        return;
                    }
                }
                start_time = wv_star_hh.getSeletedItem() + ":" + wv_star_mm.getSeletedItem();
                end_time = wv_end_hh.getSeletedItem() + ":" + wv_end_mm.getSeletedItem();
                updateBusinessTime();

            }
        });
        initView();
    }

    private void initView() {
        wv_star_hh.setOffset(1);
        wv_star_hh.setItems(Arrays.asList(HH));
        // wv_star_hh.setOnWheelViewListener(new WheelView.OnWheelViewListener()
        // {
        // @Override
        // public void onSelected(int selectedIndex, String item) {
        // }
        // });
        wv_star_mm.setOffset(1);
        wv_star_mm.setItems(Arrays.asList(MM));

        wv_end_hh.setOffset(1);
        wv_end_hh.setItems(Arrays.asList(HH));

        wv_end_mm.setOffset(1);
        wv_end_mm.setItems(Arrays.asList(MM));
        cb_swith.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    business_type = 1;
                    ll_timer.setVisibility(View.GONE);
                } else {
                    ll_timer.setVisibility(View.VISIBLE);
                    business_type = 0;

                }

            }
        });

        mPartnerBean = (PartnerBean) getIntent().getSerializableExtra("mPartnerBean");
        Log.e("url", "修改营业时间之后" + mPartnerBean);
        if (mPartnerBean.getIs_24hour_business() == 1) {
            cb_swith.setChecked(true);
        } else {
            cb_swith.setChecked(false);
            String[] starStrings = mPartnerBean.getBusiness_start_date().split(":");
            wv_star_hh.setSeletion(Arrays.asList(HH).indexOf(starStrings[0]));
            wv_star_mm.setSeletion(Arrays.asList(MM).indexOf(starStrings[1]));
            String[] endStrings = mPartnerBean.getBusiness_end_date().split(":");
            wv_end_hh.setSeletion(Arrays.asList(HH).indexOf(endStrings[0]));
            wv_end_mm.setSeletion(Arrays.asList(MM).indexOf(endStrings[1]));
        }
    }

    /* 修改店铺营业时间 */
    private void updateBusinessTime() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("business_type", business_type + "");
        if (business_type == 0) {
            map.put("start_time", start_time);
            map.put("end_time", end_time);
        }
        mQueue.add(ParamTools.packParam(Constants.updateBusinessTime, this, this, map));
        loading();
    }

    /**
     * 单击事件
     */
    @OnClick({})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.exit:
                Intent intent1 = new Intent(this, LoginOutDialogActivity.class);
                startActivityForResult(intent1, 100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            if (stauts == 0) {
                if (url.contains(Constants.updateBusinessTime)) {
                    mPartnerBean.setBusiness_start_date(start_time);
                    mPartnerBean.setBusiness_end_date(end_time);
                    mPartnerBean.setIs_24hour_business(business_type);
                    Intent intent = new Intent();
                    intent.putExtra("mPartnerBean", mPartnerBean);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } else if (stauts < 0) {
                Tools.dealErrorMsg(this, url, stauts, json.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }
}
