package com.juxun.business.street.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.StoreCardsAdapter;
import com.juxun.business.street.bean.StoreStaBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.TimerDateUtil;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.CustomDatePicker;
import com.juxun.business.street.widget.wheel.CustomDatePicker2;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 储值统计页面
 *
 * @author wood121
 */
public class StoreValueStaActivity extends BaseActivity implements
        OnClickListener {

    @ViewInject(R.id.tv_date_left)
    private TextView tv_date_left; // 开始时间
    @ViewInject(R.id.tv_date_right)
    private TextView tv_date_right; // 结束时间
    @ViewInject(R.id.tv_date_confirm)
    private TextView tv_date_confirm; // 确定按钮
    @ViewInject(R.id.button_back)
    private ImageView button_back; // 确定按钮

    @ViewInject(R.id.tv_three)
    private TextView tv_three; // 近三天
    @ViewInject(R.id.tv_week)
    private TextView tv_week; // 近一周
    @ViewInject(R.id.tv_month)
    private TextView tv_month; // 近一个月

    @ViewInject(R.id.lv_cards)
    private ListView lv_cards; // 储值卡列表
    @ViewInject(R.id.ll_none)
    private LinearLayout ll_none; // 没有数据的内容显示
    @ViewInject(R.id.view_upper_divider)
    private View view_upper_divider; // 没有数据的时候分割线的处理
    @ViewInject(R.id.view_lowwer_divider)
    private View view_lowwer_divider; // 没有数据的时候分割线的处理

    private static final int TEXT_LEFT = 0;
    private static final int TEXT_RIGHT = 1;
    private StoreCardsAdapter mStoreCardsAdapter;
    private CustomDatePicker2 customDatePicker1, customDatePicker2;
    private Calendar mCalendar;
    private String mActivity_id;
    private String startimer, endtimer;
    private TimerDateUtil timerDateUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_statistics); // 与【采购订单】取消订单布局一致
        ViewUtils.inject(this);
        initView();
        timerDateUtil = new TimerDateUtil();
        startimer = timerDateUtil.getDate(3);
        endtimer = timerDateUtil.getNowDate();
        tv_date_left.setText(startimer);
        tv_date_right.setText(endtimer);
        requestRecords(); // 一开始查询近三天的数据
    }

    private void initView() {
        mActivity_id = getIntent().getStringExtra("activity_id");

        button_back.setOnClickListener(this);
        tv_date_left.setOnClickListener(this);
        tv_date_right.setOnClickListener(this);
        tv_date_confirm.setOnClickListener(this);
        tv_three.setOnClickListener(this);
        tv_week.setOnClickListener(this);
        tv_month.setOnClickListener(this);


        // 展示列表处始化
        mStoreCardsAdapter = new StoreCardsAdapter(this);
        lv_cards.setAdapter(mStoreCardsAdapter);
        initDatePicker();
    }


    private void initDatePicker() {
        final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        customDatePicker1 = new CustomDatePicker2(StoreValueStaActivity.this, new CustomDatePicker2.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                if (!endtimer.equals("")) {
                    try {
                        if (sdf2.parse(endtimer).getTime() < sdf2.parse(time).getTime()) {
                            Tools.showToast(getApplicationContext(), "开始时间不能大于等于结束时间");
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                startimer = time;
                tv_date_left.setText(time);
                if (!tv_date_right.getText().toString().equals("")) {
                    requestRecords();
                }
            }
        }, "2017-01-01 00:00:00", "2025-12-28 00:00:00"); // 初始化日期格式请用：yyyy-MM-dd，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 不显示时和分
        customDatePicker1.setIsLoop(false); // 不允许循环滚动

        customDatePicker2 = new CustomDatePicker2(StoreValueStaActivity.this, new CustomDatePicker2.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                if (!startimer.equals("")) {
                    try {
                        if (sdf2.parse(startimer).getTime() > sdf2.parse(time).getTime()) {
                            Tools.showToast(getApplicationContext(), "结束时间不能小于等于开始时间");
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                endtimer = time;
                tv_date_right.setText(time);
                if (!tv_date_left.getText().toString().equals("")) {
                    requestRecords();
                }
            }
        }, "2017-01-01 00:00:00", "2025-12-28 00:00:00"); // 初始化日期格式请用：yyyy-MM-dd，否则不能正常运行
        customDatePicker2.showSpecificTime(false); // 显示时和分
        customDatePicker2.setIsLoop(false); // 允许循环滚动
    }

    private void requestRecords() {

        // 不填就不按该条件查询 s
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token() + ""); // token传递
        map.put("activity_id", mActivity_id); // 储值活动id,s
        map.put("member_phone", ""); // 用户电话查询
        map.put("type", "-1"); // -1代表查所有0充值1消费
        // yyyy-MM-dd HH:mm 开始时间,s mYearSint, mMonthSint,mDaySint
        map.put("start_time", startimer);
        // yyyy-MM-dd HH:mm 结束时间,s mYearEint, mMonthEint,mDayEint
        map.put("end_time", endtimer);
        map.put("pageNumber", "1");
        map.put("pageSize", "10000");
        mQueue.add(ParamTools.packParam(Constants.getMemberRechargeRecordList,
                this, this, map));
        loading();
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.optInt("status");
            String msg = json.optString("msg");
            if (url.contains(Constants.getMemberRechargeRecordList)) {
                if (status == 0) {
                    List<StoreStaBean> storeStasList = JSON.parseArray(
                            json.optString("result"), StoreStaBean.class);
                    if (storeStasList == null || storeStasList.size() == 0) {
                        ll_none.setVisibility(View.VISIBLE);
                        lv_cards.setVisibility(View.GONE);
                        view_lowwer_divider.setVisibility(View.GONE);
                        view_upper_divider.setVisibility(View.GONE);
                    } else {
                        lv_cards.setVisibility(View.VISIBLE);
                        ll_none.setVisibility(View.GONE);
                        mStoreCardsAdapter.updateCardsAdapter(storeStasList);
                    }
                } else {
                    Tools.showToast(this, msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(StoreValueStaActivity.this, "解析错误");
        }
    }

    private void selected(int index) {
        // 字体的改变
        tv_three.setTextColor(index == 101 ? getResources().getColor(
                R.color.blue) : getResources().getColor(R.color.jiujiujiu));
        tv_week.setTextColor(index == 102 ? getResources().getColor(
                R.color.blue) : getResources().getColor(R.color.jiujiujiu));
        tv_month.setTextColor(index == 103 ? getResources().getColor(
                R.color.blue) : getResources().getColor(R.color.jiujiujiu));
        // 背景颜色的切换
        tv_three.setBackgroundResource(index == 101 ? R.drawable.storedvalue_btn_time_s
                : R.drawable.storedvalue_btn_time_n);
        tv_week.setBackgroundResource(index == 102 ? R.drawable.storedvalue_btn_time_s
                : R.drawable.storedvalue_btn_time_n);
        tv_month.setBackgroundResource(index == 103 ? R.drawable.storedvalue_btn_time_s
                : R.drawable.storedvalue_btn_time_n);
        tv_date_left.setText(startimer);
        tv_date_right.setText(endtimer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.tv_date_left:
                if (tv_date_left.getText().toString().equals("")) {
                    customDatePicker1.show(timerDateUtil.getNowDate2());
                } else {
                    customDatePicker1.show(tv_date_left.getText().toString());
                }
                break;
            case R.id.tv_date_right:
                if (tv_date_right.getText().toString().equals("")) {
                    customDatePicker2.show(timerDateUtil.getNowDate2());
                } else {
                    customDatePicker2.show(tv_date_right.getText().toString());
                }
                break;
            //近三天
            case R.id.tv_three:
                startimer = timerDateUtil.getDate(3);
                endtimer = timerDateUtil.getNowDate();
                selected(101);
                requestRecords();
                break;
            //近一周
            case R.id.tv_week:
                startimer = timerDateUtil.getDate(7);
                endtimer = timerDateUtil.getNowDate();

                selected(102);
                requestRecords();
                break;
            //近一月
            case R.id.tv_month:
                startimer = timerDateUtil.getDate(30);
                endtimer = timerDateUtil.getNowDate();
                selected(103);
                requestRecords();
                break;
            case R.id.tv_date_confirm:
                startimer = tv_date_left.getText().toString();
                endtimer = tv_date_right.getText().toString();
                endtimer = timerDateUtil.getNowDate();
                requestRecords();
                break;

            default:
                break;
        }
    }


}
