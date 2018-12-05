package com.juxun.business.street.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.bean.CardInfoBean;
import com.juxun.business.street.bean.StoreCardBean;
import com.juxun.business.street.bean.StoreCenterBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 储值活动内容页面
 *
 * @author wood121
 */
public class StoreActsActivity extends BaseActivity implements OnClickListener {

    @ViewInject(R.id.textview_title)
    private TextView textview_title; // 页面标题
    @ViewInject(R.id.button_back)
    private ImageView button_back; // 页面标题

    @ViewInject(R.id.tv_total_num)
    private TextView tv_total_num; // 累计充值
    @ViewInject(R.id.tv_total_num_small)
    private TextView tv_total_num_small; // 累计充值
    @ViewInject(R.id.tv_total_num_after)
    private TextView tv_total_num_after; // 补充说明

    @ViewInject(R.id.tv_store_statistics)
    private TextView tv_store_statistics; // 查看明细
    @ViewInject(R.id.tv_acts_status)
    private TextView tv_acts_status; // 活动状态
    @ViewInject(R.id.tv_acts_time)
    private TextView tv_acts_time; // 活动时间

    @ViewInject(R.id.ll_cards_container)
    private LinearLayout ll_cards_container; // 展示列表

    @ViewInject(R.id.tv_left_btn)
    private TextView tv_left_btn; // 左侧编辑按钮
    @ViewInject(R.id.tv_right_btn)
    private TextView tv_right_btn; // 右侧暂停按钮

    private AlertDialog mAlertDialog;

    private int mIfLeftCan; // 左侧按钮是否可编辑
    private int btnType = 0; // 右侧按钮操作类型

    private CardInfoBean mcardInfoBean;
    private StoreCenterBean mStoreCenterBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acts); // 与【采购订单】取消订单布局一致
        ViewUtils.inject(this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestActs();
    }

    private void initView() {
        mStoreCenterBean = (StoreCenterBean) getIntent().getSerializableExtra("stores");
        String name = getIntent().getStringExtra("name");
        textview_title.setText(name);
        button_back.setOnClickListener(this);
        tv_store_statistics.setOnClickListener(this);
        tv_left_btn.setOnClickListener(this);
        tv_right_btn.setOnClickListener(this);
    }

    private void viewBindDatas() {
        //1未开始2进行中3已结束4已暂停",
        tv_total_num.setText(Tools.getFenYuan(mcardInfoBean.getRechargeMoney()) + "");
        tv_total_num_small.setText("/" + mcardInfoBean.getRechargeNum());
        tv_total_num_after.setText("充" + Tools.getFenYuan(mcardInfoBean.getRealMoney())
                + ",送" + Tools.getFenYuan(mcardInfoBean.getGiveMoney()));

        String actsStatus[] = {"未开始", "进行中", "已结束", "已暂停"};
        tv_acts_status.setText(actsStatus[mStoreCenterBean.getState() - 1]);
        if (mStoreCenterBean.getState() == 2
                || mStoreCenterBean.getState() == 4) {
            tv_acts_status.setTextColor(getResources().getColor(R.color.blue));
        } else if (mStoreCenterBean.getState() == 1) {
            tv_acts_status.setTextColor(getResources().getColor(
                    R.color.two_gray));
        } else if (mStoreCenterBean.getState() == 3) {
            tv_acts_status.setTextColor(getResources().getColor(
                    R.color.jiujiujiu));
        }
        tv_acts_time.setText(Tools.getDateformat2(mStoreCenterBean.getStart_time()) + " -- "
                + Tools.getDateformat2(mStoreCenterBean.getEnd_time()));
        // 中间列表
        addCards();
        // 下面按钮：
        if (mStoreCenterBean.getState() == 1) {
            leftBtn(1); // 未开始状态可以进行操作
            tv_right_btn.setText("删除");
        } else if (mStoreCenterBean.getState() == 2) {
            leftBtn(0);
            tv_right_btn.setText("暂停");
        } else if (mStoreCenterBean.getState() == 4) {
            leftBtn(0);
            tv_right_btn.setText("开启");
        } else if (mStoreCenterBean.getState() == 3) {
            leftBtn(0);
            tv_right_btn.setText("删除");
        }
    }


    /*
     * 左侧按钮是否可操作
     */
    private void leftBtn(int ifLeftCan) {
        mIfLeftCan = ifLeftCan;
        int bgWhite = getResources().getColor(R.color.white);
        int textBlue = getResources().getColor(R.color.blue);
        int bgGray = getResources().getColor(R.color.back_d8);
        int textGray = getResources().getColor(R.color.two_gray);

        tv_left_btn.setBackgroundColor(ifLeftCan == 1 ? bgWhite : bgGray);
        tv_left_btn.setTextColor(ifLeftCan == 1 ? textBlue : textGray);
    }

    private void addCards() {
        ll_cards_container.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        for (int i = 0; i < mcardInfoBean.getDenominationGroupData().size(); i++) {
            // 初始化控件
            final View cardView = layoutInflater.inflate(
                    R.layout.store_acts_card, null);
            TextView tv_card_name = (TextView) cardView
                    .findViewById(R.id.tv_card_name);
            TextView tv_card_money = (TextView) cardView
                    .findViewById(R.id.tv_card_money);
            TextView tv_card_num = (TextView) cardView
                    .findViewById(R.id.tv_card_num);
            TextView tv_card_gift = (TextView) cardView
                    .findViewById(R.id.tv_card_gift);
            // 数据绑定
            CardInfoBean.DenominationGroupDataBean storeCardBean = mcardInfoBean.getDenominationGroupData().get(i);
            tv_card_name.setText("储值卡" + (i + 1));
            tv_card_money.setText(Tools.getFenYuan(storeCardBean.getRecharge_money()) + "");
            tv_card_num.setText("（" + storeCardBean.getDenoNum() + "）张");
            tv_card_gift.setText("¥" + Tools.getFenYuan(storeCardBean.getGive_money()));

            // 事件处理
            cardView.setId(i);
            cardView.setTag("第:::" + i + ":::块");
            cardView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String content = (String) cardView.getTag();
                }
            });
            ll_cards_container.addView(cardView);
        }
    }

    private void requestActs() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("activityId", mStoreCenterBean.getId() + "");
        mQueue.add(ParamTools.packParam(
                Constants.getMemberRechargeActivityInfo, this, this, map));
        loading();
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.optInt("status");
            String msg = json.optString("msg");
            if (url.contains(Constants.getMemberRechargeActivityInfo)) {
                if (status == 0) {
                    mcardInfoBean = JSON.parseObject(
                            json.optString("result"),
                            CardInfoBean.class);
                    if (mcardInfoBean != null) {
                        viewBindDatas();
                    }
                } else {
                    Tools.showToast(this, msg);
                }

            } else if (url.contains(Constants.delMemberReChargeActivity)) {
                if (status == 0) {
                    // 删除成功的操作
                    finish();
                } else {
                    Tools.showToast(this, msg);
                }
            } else if (url.contains(Constants.enbleMemberReChargeActivity)) {
                if (status == 0) {
                    if (mStoreCenterBean.getState() == 2) {
                        tv_acts_status.setText("已暂停");
                        mStoreCenterBean.setState(4);
                        tv_right_btn.setText("开启");
                    } else {
                        tv_acts_status.setText("进行中");
                        mStoreCenterBean.setState(2);
                        tv_right_btn.setText("暂停");
                    }
                } else {
                    Tools.showToast(this, msg);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(StoreActsActivity.this, "解析错误");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.tv_store_statistics: // 储值说明 -》跳转储值统计
                intent.setClass(this, StoreValueStaActivity.class);
                intent.putExtra("activity_id", mStoreCenterBean.getId() + "");
                startActivity(intent);
                break;
            case R.id.tv_left_btn: // 左侧按钮
                if (mIfLeftCan == 1) {
                    intent.setClass(this, AddStoreActivity.class);
                    if (mStoreCenterBean != null) {
                        intent.putExtra("actInfo", mStoreCenterBean);
                        intent.putExtra("card", mcardInfoBean);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.tv_right_btn: // 右侧按钮
                String btnStr = tv_right_btn.getText().toString();
                showOperateDialog(btnStr);
                break;
            default:
                break;
        }
    }

    /*
     * 开闭活动操作
     */
    private void enableActs(int i) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("activity_id", mStoreCenterBean.getId() + "");
        map.put("status", i + "");//1禁用0开启
        mQueue.add(ParamTools.packParam(Constants.enbleMemberReChargeActivity,
                this, this, map));
        loading();
    }

    /*
     * 删除活动操作
     */
    private void delActs() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("activity_id", mStoreCenterBean.getId() + "");
        mQueue.add(ParamTools.packParam(Constants.delMemberReChargeActivity,
                this, this, map));
        loading();
    }

    private void showOperateDialog(String btnStr) {
        String tvContent[] = {"您确定要删除该活动吗？", "您确定要开启该活动吗？", "您确定要暂停该活动吗？"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 控件初始化
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_store_acts, null);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        if (btnStr.equals("删除")) {
            tv_content.setText(tvContent[0]);
            btnType = 0;
        } else if (btnStr.equals("开启")) {
            tv_content.setText(tvContent[1]);
            btnType = 1;
        } else { // 暂停
            tv_content.setText(tvContent[2]);
            btnType = 2;
        }

        Button btn_colse = (Button) view.findViewById(R.id.btn_colse);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        builder.setView(view);

        mAlertDialog = builder.create();
        mAlertDialog.show();

        btn_colse.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (btnType) {
                    case 0:
                        delActs();// 删除操作
                        break;
                    case 1:
                        enableActs(0);// 开启操作
                        break;
                    case 2:
                        enableActs(1);// 暂停操作
                        break;
                    default:
                        break;
                }
                mAlertDialog.dismiss();
            }
        });

    }
}
