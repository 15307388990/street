package com.juxun.business.street.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxun.business.street.bean.CouponListBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.juxun.business.street.config.Constants.delCouponActs;

public class CounponActsDetailActivity extends BaseActivity {

    @Bind(R.id.button_back)
    ImageView buttonBack;
    @Bind(R.id.textview_title)
    TextView textviewTitle;
    @Bind(R.id.rl_top)
    RelativeLayout rlTop;

    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.tv_act_name)
    TextView tvActName;
    @Bind(R.id.tv_act_createtime)
    TextView tvActCreatetime;
    @Bind(R.id.tv_act_time)
    TextView tvActTime;
    @Bind(R.id.tv_act_cards)
    TextView tvActCards;
    @Bind(R.id.tv_act_condition)
    TextView tvActCondition;
    @Bind(R.id.tv_act_explaination)
    TextView tvActExplaination;

    @Bind(R.id.tv_left_btn)
    TextView tvLeftBtn;
    @Bind(R.id.tv_right_btn)
    TextView tvRightBtn;

    private CouponListBean mStoreCardsListBean;
    private DecimalFormat decimalFormat;
    private boolean tvLeftBtnCan;
    private boolean tvRightBtnCan;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counpon_acts_detail);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        decimalFormat = new DecimalFormat("0.00");
        mStoreCardsListBean = (CouponListBean) getIntent().getSerializableExtra("mStoreCardsListBean");
        String startTime = Tools.getDateformat2(mStoreCardsListBean.getStart_time());
        String endTime = Tools.getDateformat2(mStoreCardsListBean.getEnd_time());

        tvActTime.setText(startTime + "-" + endTime);
        tvActName.setText(mStoreCardsListBean.getName());
        tvActCreatetime.setText(Tools.getDateformat2(mStoreCardsListBean.getCreate_date()));
        tvActCards.setText(mStoreCardsListBean.getRed_count() + "");
        CouponListBean.DenominationListBean denominationListBean = mStoreCardsListBean.getDenomination_list().get(0);
        String maxPrice = decimalFormat.format(Tools.getFenYuan(denominationListBean.getMax_price()));
        String usePrice = decimalFormat.format(Tools.getFenYuan(denominationListBean.getUse_price()));
        tvActCondition.setText("满" + maxPrice + "元减" + usePrice + "元");
        tvActExplaination.setText(mStoreCardsListBean.getDescri() + "");    //如果没有活动描述，就""

        int state = mStoreCardsListBean.getState(); //1禁用、0未禁用
        long nowTime = System.currentTimeMillis();
        long start_time = mStoreCardsListBean.getStart_time();  //未开始、进行中、已暂停、已结束
        long end_time = mStoreCardsListBean.getEnd_time();

        int tv_red = getResources().getColor(R.color.tv_red);
        int tv_blue = getResources().getColor(R.color.guide_words);
        int tv_black = getResources().getColor(R.color.two_gray);
        int tv_gray = getResources().getColor(R.color.jiujiujiu);

        int grayColor = getResources().getColor(R.color.jiujiujiu);
        int whiteColor = getResources().getColor(R.color.white);

        if (nowTime < start_time) {
            tvStatus.setText("未开始");
            tvStatus.setTextColor(tv_black);
            tvLeftBtn.setBackgroundColor(whiteColor);
            tvRightBtn.setBackgroundColor(whiteColor);
            tvLeftBtnCan = true;
            tvRightBtnCan = true;
        } else if (nowTime >= start_time && nowTime <= end_time) {  //
            if (state == 0) {
                tvStatus.setText("进行中");
                tvStatus.setTextColor(tv_blue);
                tvLeftBtn.setBackgroundColor(grayColor);
                tvRightBtn.setBackgroundColor(grayColor);
                tvLeftBtnCan = false;
                tvRightBtnCan = false;
            } else {
                tvStatus.setText("已暂停");
                tvStatus.setTextColor(tv_red);
                tvLeftBtn.setBackgroundColor(grayColor);
                tvRightBtn.setBackgroundColor(grayColor);
                tvLeftBtnCan = false;
                tvRightBtnCan = false;
            }
        } else if (nowTime > end_time) {
            tvStatus.setText("已结束");
            tvStatus.setTextColor(tv_gray);
            tvLeftBtn.setBackgroundColor(grayColor);
            tvRightBtn.setBackgroundColor(whiteColor);
            tvLeftBtnCan = false;
            tvRightBtnCan = true;
        }

    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.optInt("status");
            String msg = json.optString("msg");
            if (url.contains(delCouponActs)) {
                if (status == 0) {
                    //删除成功、直接跳转活动列表
                    Tools.jump(this, CouponActivity.class, false);
                    finish();
                } else {
                    Tools.showToast(this, msg);
                }
            } else if (true) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    @OnClick({R.id.button_back, R.id.tv_left_btn, R.id.tv_right_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.tv_left_btn:
                //编辑
                if (tvLeftBtnCan) {
                    Intent intent = new Intent(CounponActsDetailActivity.this, AddCouponActivity.class);
                    intent.putExtra("mStoreCardsListBean", mStoreCardsListBean);
                    startActivity(intent);
                    this.finish();
                } else {
                    return;
                }
                break;
            case R.id.tv_right_btn:
                //删除操作
                if (tvRightBtnCan) {
                    showOperateDialog();
                } else {
                    return;
                }
                break;
        }
    }

    private void showOperateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 控件初始化
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_store_acts, null);
        //显示的内容
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_content.setText("您确定要删除该活动吗？");
        //对话按钮
        Button btn_colse = (Button) view.findViewById(R.id.btn_colse);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        builder.setView(view);

        mAlertDialog = builder.create();
        btn_colse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                delCouponActs();
                mAlertDialog.dismiss();
            }
        });
        mAlertDialog.show();
    }

    private void delCouponActs() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("activity_id", mStoreCardsListBean.getId() + "");
        mQueue.add(ParamTools.packParam(delCouponActs,
                CounponActsDetailActivity.this, CounponActsDetailActivity.this, map));
    }
}
