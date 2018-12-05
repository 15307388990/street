package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxun.business.street.bean.Agreement7;
import com.juxun.business.street.bean.CouponListBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;

import static com.juxun.business.street.config.Constants.addCouponActs;
import static com.juxun.business.street.config.Constants.redupdate;
import static com.yl.ming.efengshe.R.id.btn_next;
import static com.yl.ming.efengshe.R.id.cb_check;
import static com.yl.ming.efengshe.R.id.et_name;
import static com.yl.ming.efengshe.R.id.tv_end_timer;
import static com.yl.ming.efengshe.R.id.tv_star_timer;

/**
 * 新增代金券页面
 */
public class AddCouponActivity extends BaseActivity implements TextWatcher {

    @Bind(R.id.btn_back)
    ImageView btnBack;
    @Bind(R.id.top_view_back)
    LinearLayout topViewBack;
    @Bind(R.id.right_view_text)
    TextView rightViewText;
    @Bind(R.id.top_view_text)
    TextView topViewText;
    @Bind(R.id.right_img)
    ImageView rightImg;
    @Bind(R.id.titleBar)
    RelativeLayout titleBar;

    @Bind(et_name)
    EditText etName;
    @Bind(tv_star_timer)
    TextView tvStarTimer;
    @Bind(tv_end_timer)
    TextView tvEndTimer;
    @Bind(R.id.et_top_money)
    EditText etTopMoney;
    @Bind(R.id.et_top_money_add)
    EditText etTopMoneyAdd;
    @Bind(R.id.et_count)
    EditText etCount;
    @Bind(R.id.et_explain)
    EditText etExplain;
    @Bind(cb_check)
    CheckBox cbCheck;
    @Bind(R.id.tv_instructions)
    TextView tvInstructions;    //代金券使用说明
    @Bind(btn_next)
    Button btnNext;
    CouponListBean mStoreCardsListBean;
    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        //标题
        initTitle();
        decimalFormat = new DecimalFormat("0.00");
        rightViewText.setText("帮助");
        topViewText.setText("新增代金券活动");
        btnNext.setEnabled(initBtn());
        //活动说明的输入
        etExplain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 140) {
                    Tools.showToast(AddCouponActivity.this, "活动说明不能超过140字");
                }
            }
        });
        etCount.addTextChangedListener(this);
        etExplain.addTextChangedListener(this);
        etName.addTextChangedListener(this);
        etTopMoney.addTextChangedListener(this);
        etTopMoneyAdd.addTextChangedListener(this);

        //代金券使用声明
        cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnNext.setEnabled(initBtn());
            }
        });
        mStoreCardsListBean = (CouponListBean) getIntent().getSerializableExtra("mStoreCardsListBean");
        if (mStoreCardsListBean != null) {
            topViewText.setText("修改代金券活动");
            etName.setText(mStoreCardsListBean.getName());
            tvStarTimer.setText(Tools.getDateformat2(mStoreCardsListBean.getStart_time()));
            tvEndTimer.setText(Tools.getDateformat2(mStoreCardsListBean.getEnd_time()));
            etTopMoney.setText(Tools.getFenYuan(mStoreCardsListBean.getDenomination_list().get(0).getMax_price())+"");
            etTopMoneyAdd.setText(Tools.getFenYuan(mStoreCardsListBean.getDenomination_list().get(0).getUse_price())+"");
            etCount.setText(mStoreCardsListBean.getRed_count() + "");
            etExplain.setText(mStoreCardsListBean.getDescri());
        }
    }

    @OnClick({R.id.btn_back, R.id.right_view_text, tv_star_timer, tv_end_timer, R.id.tv_instructions, btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.right_view_text:
                //右侧的帮助按钮
                Tools.jump(this, AddCouponHelpActivity.class, false);
                break;
            case tv_star_timer:
                onYearMonthDayTimePicker(0);
                break;
            case tv_end_timer:
                onYearMonthDayTimePicker(1);
                break;
            case R.id.tv_instructions:
                //代金券使用说明
//                Tools.jump(this, CouponUseExpalinationActivity.class, false);
                Intent intent = new Intent(this, WebviewActivity.class);
                Agreement7 agreement7 = new Agreement7();
                agreement7.setLink_url(Constants.mainUrl + Constants.registerRules);
                agreement7.setTitle("代金券使用说明");
                intent.putExtra("agreement7", agreement7);
                startActivity(intent);
                break;
            case btn_next:
                addCouponActs();
                break;
        }
    }

    // 添加储值充值活动
    private void addCouponActs() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date star = null, end = null;
        try {
            star = df.parse(tvStarTimer.getText().toString());
            end = df.parse(tvEndTimer.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //比较时间的大小
        if (end.getTime() <= star.getTime()) {
            Tools.showToast(this, "结束时间不能小于等于开始时间");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("name", etName.getText().toString());// 活动名称
        map.put("start_time", tvStarTimer.getText().toString());// 开始时间
        map.put("end_time", tvEndTimer.getText().toString());// 结束时间
        String max_price = etTopMoney.getText().toString();
        map.put("max_price", (int) (Double.valueOf(max_price) * 100) + ""); //满减的数量单位是元，上传的是分操作
        String use_price = etTopMoneyAdd.getText().toString();
        map.put("use_price", (int) (Double.valueOf(use_price) * 100) + "");
        map.put("red_count", etCount.getText().toString()); //红包数量
        map.put("descri", etExplain.getText().toString());  //活动说明
        if (mStoreCardsListBean != null) {
            map.put("activity_id", mStoreCardsListBean.getDenomination_list().get(0).getActivity_id() + "");
            mQueue.add(ParamTools.packParam(redupdate, this, this, map));
        } else {
            mQueue.add(ParamTools.packParam(addCouponActs, this, this, map));
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
                Tools.showToast(this, "添加成功");
                finish();
            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onYearMonthDayTimePicker(final int type) {
        //type:0左边的开始时间、1右边的结束时间
        DateTimePicker picker = new DateTimePicker(this, DateTimePicker.HOUR_24);
        Calendar c = Calendar.getInstance();//
        picker.setDateRangeStart(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
        picker.setDateRangeEnd(2028, 12, 31);
        picker.setTimeRangeStart(0, 0);
        picker.setTimeRangeEnd(23, 30);
        picker.setTopLineColor(0x99FF0000);
        picker.setLabelTextColor(0xFFFF0000);
        picker.setDividerColor(0xFFFF0000);
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                if (type == 0) {
                    tvStarTimer.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":00");
                } else {
                    tvEndTimer.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":00");
                }
            }
        });
        picker.show();
    }

    //活动添加的
    private boolean initBtn() {
        if (etName.getText().toString().length() > 0
                && tvStarTimer.getText().toString().length() > 0
                && tvEndTimer.getText().toString().length() > 0
                && etTopMoney.getText().toString().length() > 0
                && etTopMoneyAdd.getText().toString().length() > 0
                && etCount.getText().toString().length() > 0
                && etExplain.getText().toString().length() > 0 && cbCheck.isChecked()) {
            btnNext.setBackgroundResource(R.drawable.button_bg1);
            btnNext.setTextColor(getResources().getColor(R.color.white));
            return true;
        } else {
            btnNext.setBackgroundResource(R.drawable.button_bg);
            btnNext.setTextColor(getResources().getColor(R.color.jiujiujiu));
            return false;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        btnNext.setEnabled(initBtn());

    }
}