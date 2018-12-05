package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.bean.Agreement7;
import com.juxun.business.street.bean.CardInfoBean;
import com.juxun.business.street.bean.StoreCardBean;
import com.juxun.business.street.bean.StoreCenterBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.CustomDatePicker;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author luoming 新增储值或编辑储值活动
 */
public class AddStoreActivity extends BaseActivity {
    @ViewInject(R.id.et_name)
    private EditText et_name;
    @ViewInject(R.id.tv_star_timer)
    private TextView tv_star_timer;// 开始时间
    @ViewInject(R.id.tv_end_timer)
    private TextView tv_end_timer;// 结束时间
    @ViewInject(R.id.tv_add_store)
    private TextView tv_add_store;// 添加储值卡
    @ViewInject(R.id.btn_next)
    private Button btn_next;// 确定
    @ViewInject(R.id.tv_instructions)
    private TextView tv_instructions;

    @ViewInject(R.id.cb_check)
    private CheckBox cb_check;
    private CustomDatePicker customDatePicker1, customDatePicker2;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    String now = sdf.format(new Date());

    private List<StoreCardBean> mCardBeans = new ArrayList<StoreCardBean>();
    private StoreCenterBean mStoreCenterBean = null;
    private CardInfoBean mCardInfoBean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstore);
        ViewUtils.inject(this);
        initTitle();
        mStoreCenterBean = (StoreCenterBean) getIntent().getSerializableExtra("actInfo");
        mCardInfoBean = (CardInfoBean) getIntent().getSerializableExtra("card");
        if (mStoreCenterBean == null) {
            title.setText("新增储值活动");
        } else {
            title.setText("编辑储值活动");
            initDate();
        }

        initDatePicker();
        initOclik();
    }

    private void initDate() {
        et_name.setText(mStoreCenterBean.getName());
        tv_star_timer.setText(Tools.getDateformat2(mStoreCenterBean.getStart_time()));
        tv_end_timer.setText(Tools.getDateformat2(mStoreCenterBean.getEnd_time()));
        tv_add_store.setText(mStoreCenterBean.getRechargeTypeCount() + "张");
        for (CardInfoBean.DenominationGroupDataBean denominationGroupDataBean : mCardInfoBean.getDenominationGroupData()) {
            StoreCardBean storeCardBean = new StoreCardBean();
            storeCardBean.setRecharge_money(denominationGroupDataBean.getRecharge_money());
            storeCardBean.setGive_money(denominationGroupDataBean.getGive_money());
            storeCardBean.setSelledCount(denominationGroupDataBean.getDenoNum());
            mCardBeans.add(storeCardBean);
        }
    }

    private void initOclik() {
        tv_star_timer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tv_star_timer.getText().toString().equals("")) {
                    customDatePicker1.show(now);
                } else {
                    customDatePicker1.show(tv_star_timer.getText().toString());
                }
                btn_next.setEnabled(initBtn());
            }
        });
        tv_end_timer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tv_end_timer.getText().toString().equals("")) {
                    customDatePicker2.show(now);
                } else {
                    customDatePicker2.show(tv_end_timer.getText().toString());
                }
                btn_next.setEnabled(initBtn());

            }
        });
        et_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_next.setEnabled(initBtn());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (et_name.getText().toString().length() > 9) {
                    Tools.showToast(AddStoreActivity.this, "名称请在10字以内");
                    return;
                } else {
                    addOrEditMemberReChargeActivity();
                }
            }
        });
        tv_add_store.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 编辑储值卡
                Intent intent = new Intent(AddStoreActivity.this, AddCardActivity.class);
                intent.putExtra("CardBeans", (Serializable) mCardBeans);
                startActivityForResult(intent, 1);
            }
        });
        cb_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btn_next.setEnabled(initBtn());

            }
        });
        tv_instructions.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStoreActivity.this, WebviewActivity.class);
                Agreement7 agreement7 = new Agreement7();
                agreement7.setLink_url(Constants.mainUrl + Constants.registerRules + "?type = 1");
                agreement7.setTitle("使用说明");
                intent.putExtra("agreement7", agreement7);
                startActivity(intent);
            }
        });
    }

    // 添加储值充值活动
    private void addOrEditMemberReChargeActivity() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date star = null, end = null;
        try {
            star = df.parse(tv_star_timer.getText().toString());
            end = df.parse(tv_end_timer.getText().toString());
            //比较时间的大小
            if (end.getTime() <= star.getTime()) {
                Tools.showToast(this, "结束时间不能小于等于开始时间");
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("name", et_name.getText().toString());// 活动名称
        map.put("start_time", tv_star_timer.getText().toString());// 开始时间
        map.put("end_time", tv_end_timer.getText().toString());// 结束时间
        map.put("auth_token", partnerBean.getAuth_token() + "");
        for (int i = 0; i < mCardBeans.size(); i++) {
            map.put("denomination_list[" + i + "].recharge_money", mCardBeans.get(i).getRecharge_money() + "");// json
            map.put("denomination_list[" + i + "].give_money", mCardBeans.get(i).getGive_money() + "");// json
        }
        if (mStoreCenterBean == null) {
            mQueue.add(ParamTools.packParam(Constants.addOrEditMemberReChargeActivity, this, this, map));
        } else {
            map.put("id", mStoreCenterBean.getId() + "");
            mQueue.add(ParamTools.packParam(Constants.rechargeupdate, this, this, map));
        }


    }

    private boolean initBtn() {
        if (et_name.getText().toString().length() > 0 && tv_star_timer.getText().toString().length() > 0
                && tv_end_timer.getText().toString().length() > 0 && tv_add_store.getText().toString().length() > 0
                && cb_check.isChecked()) {
            btn_next.setBackgroundResource(R.drawable.button_bg1);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            return true;
        } else {
            btn_next.setBackgroundResource(R.drawable.button_bg);
            btn_next.setTextColor(getResources().getColor(R.color.jiujiujiu));
            return false;
        }

    }

    private void initDatePicker() {
        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tv_star_timer.setText(time);
                btn_next.setEnabled(initBtn());
            }
        }, now, "2027-01-01 00:00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm:ss，否则不能正常运行
        customDatePicker1.showSpecificTime(true); // 不显示时和分
        customDatePicker1.setIsLoop(false); // 不允许循环滚动

        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tv_end_timer.setText(time);
                btn_next.setEnabled(initBtn());
            }
        }, now, "2027-01-01 00:00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm:ss，否则不能正常运行
        customDatePicker2.showSpecificTime(true); // 显示时和分
        customDatePicker2.setIsLoop(false); // 允许循环滚动
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    mCardBeans = (List<StoreCardBean>) data.getSerializableExtra("cardbeans");
                    tv_add_store.setText(mCardBeans.size() + "张");
                    btn_next.setEnabled(initBtn());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.getInt("status");
            String msg = json.getString("msg");
            if (stauts == 0) {
                if (url.contains(Constants.addOrEditMemberReChargeActivity) || url.contains(Constants.rechargeupdate)) {
                    if (mStoreCenterBean == null) {
                        Tools.showToast(getApplicationContext(), "添加成功");
                    } else {
                        Tools.showToast(getApplicationContext(), "编辑成功");
                    }

                    finish();
                }
            } else if (stauts == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(AddStoreActivity.this, LoginActivity.class, false);
                Tools.showToast(AddStoreActivity.this, "登录过期请重新登录");
                Tools.acts.clear();
            } else {
                Tools.showToast(AddStoreActivity.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
