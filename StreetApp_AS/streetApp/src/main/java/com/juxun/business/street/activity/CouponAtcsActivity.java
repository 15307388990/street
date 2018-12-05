package com.juxun.business.street.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxun.business.street.bean.CouponListBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.ChartPie;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.juxun.business.street.config.Constants.openOrPauseCouponActs;
import static com.yl.ming.efengshe.R.id.cb_swith;
import static com.yl.ming.efengshe.R.id.list;


/**
 * 代金券活动内页
 */
public class CouponAtcsActivity extends BaseActivity {

    @Bind(R.id.button_back)
    ImageView buttonBack;
    @Bind(R.id.textview_title)
    TextView textviewTitle;
    @Bind(R.id.rl_top)
    RelativeLayout rlTop;
    @Bind(R.id.circle_view)
    ChartPie circleView;
    @Bind(R.id.iv_yellow)
    ImageView ivYellow;
    @Bind(R.id.iv_green)
    ImageView ivGreen;
    @Bind(R.id.iv_blue)
    ImageView ivBlue;
    @Bind(R.id.iv_gray)
    ImageView ivGray;

    @Bind(R.id.tv_acts_time)
    TextView tv_acts_time;
    @Bind(R.id.ll_points)
    LinearLayout ll_points;
    @Bind(R.id.tv_used_pre)
    TextView tv_used_pre;
    @Bind(R.id.tv_used)
    TextView tv_used;

    @Bind(R.id.tv_nouse_pre)
    TextView tv_nouse_pre;
    @Bind(R.id.tv_nouse)
    TextView tv_nouse;

    @Bind(R.id.rl_has_gone)
    RelativeLayout rl_has_gone;
    @Bind(R.id.tv_hasgone_pre)
    TextView tv_hasgone_pre;
    @Bind(R.id.tv_hasgone)
    TextView tv_hasgone;

    @Bind(R.id.tv_notyet_pre)
    TextView tv_notyet_pre;
    @Bind(R.id.tv_notyet)
    TextView tv_notyet;

    @Bind(R.id.tv_act_status)
    TextView tv_act_status;
    @Bind(cb_swith)
    CheckBox cbSwith;   //isChecked选中，否则关闭状态
    @Bind(R.id.tv_history)
    TextView tvHistory;
    @Bind(R.id.tv_detail)
    TextView tvDetail;
    private CouponListBean mStoreCardsListBean;
    private AlertDialog mAlertDialog;
    private int popType = 0;    //0暂停，1开启，2已结束的按钮
    private boolean cbSwithChecked; //Checkbox状态显示、状态切换
    private int tv_red, tv_greenyellow, tv_blue, tv_black, tv_gray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_atcs);
        ButterKnife.bind(this);

        initViews();
        initCirc();
    }

    private void initViews() {
        //数据只从列表页面传递过来
        mStoreCardsListBean = (CouponListBean) getIntent().getSerializableExtra("mStoreCardsListBean");
        String startTime = Tools.getDateformat2(mStoreCardsListBean.getStart_time());
        String endTime = Tools.getDateformat2(mStoreCardsListBean.getEnd_time());
        tv_acts_time.setText(startTime + "-" + endTime);

        int state = mStoreCardsListBean.getState(); //1禁用、0未禁用
        long nowTime = System.currentTimeMillis();
        long start_time = mStoreCardsListBean.getStart_time();  //未开始、进行中、已暂停、已结束
        long end_time = mStoreCardsListBean.getEnd_time();

        tv_red = getResources().getColor(R.color.tv_red);
        tv_greenyellow = getResources().getColor(R.color.greenyellow);
        tv_blue = getResources().getColor(R.color.guide_words);
        tv_black = getResources().getColor(R.color.two_gray);
        tv_gray = getResources().getColor(R.color.jiujiujiu);

        if (nowTime < start_time) {
            //未开始：直接隐藏
            ll_points.setVisibility(View.GONE);

            tv_act_status.setText("未开始");
            tv_act_status.setTextColor(tv_black);
            cbSwith.setVisibility(View.GONE);
        } else if (nowTime >= start_time && nowTime <= end_time) {  //
            //已结束：已使用、未使用、已过期、未领取

            if (state == 0) {
                tv_act_status.setText("进行中");
                tv_act_status.setTextColor(tv_blue);
                cbSwith.setChecked(true);
                cbSwithChecked = true;
                popType = 1;
                //
            } else {
                tv_act_status.setText("已暂停");
                tv_act_status.setTextColor(tv_red);
                cbSwith.setChecked(false);
                cbSwithChecked = false;
            }
        } else if (nowTime > end_time) {    //已结束
            //已结束：已使用、未使用、已过期、未领取


            if (state == 0) {   //未禁用
                tv_act_status.setText("已结束");
                tv_act_status.setTextColor(tv_gray);
                cbSwith.setVisibility(View.GONE);
                // rl_has_gone.setVisibility(View.VISIBLE);
            } else {
                tv_act_status.setText("已结束");
                tv_act_status.setTextColor(tv_gray);
                cbSwithChecked = false;
            }
        }

    }

    public void initCirc() {
        int red_total_count = mStoreCardsListBean.getDraw_statistics().getRed_total_count();//红包总数量
        int red_use_count = mStoreCardsListBean.getDraw_statistics().getRed_use_count();//已使用总个数
        int red_draw_count = mStoreCardsListBean.getDraw_statistics().getRed_draw_count();//已领取总个数
        //已使用百分比
        float a = (float) red_use_count / (float) red_total_count * 100;
        //未使用百分比
        float b = (((float) red_draw_count - (float) red_use_count) / (float) red_total_count) * 100;
        //为领取百分比
        float c = 100 - a - b;
        tv_used_pre.setText("已使用：" + (int) a + "%");
        tv_nouse_pre.setText("未使用：" + (int) b + "%");
        tv_notyet_pre.setText("未领取：" + (int) c + "%");
        tv_used.setText(mStoreCardsListBean.getDraw_statistics().getRed_use_count() + "张/" + Tools.getFenYuan(mStoreCardsListBean.getDraw_statistics().getRed_use_price()) + "元");
        //未使用张数
        int nouse = red_draw_count-red_use_count;
        tv_nouse.setText(nouse + "张/" + Tools.getFenYuan(nouse * mStoreCardsListBean.getDenomination_list().get(0).getUse_price()) + "元");
        //未领取张数
        int notyet = red_total_count - red_draw_count;
        tv_notyet.setText((notyet + "张/" + Tools.getFenYuan(notyet * mStoreCardsListBean.getDenomination_list().get(0).getUse_price()) + "元"));

        ArrayList<ChartPie.Element> list = new ArrayList<ChartPie.Element>();
        ChartPie.Element element = new ChartPie.Element(tv_greenyellow, (float) (a * 3.6));
        ChartPie.Element element1 = new ChartPie.Element(tv_blue, (float) (b * 3.6));
        ChartPie.Element element2 = new ChartPie.Element(tv_gray, (float) (c * 3.6));
        list.add(element);
        list.add(element1);
        list.add(element2);
        circleView.setData(list, red_total_count + "张/" + Tools.getFenYuan(red_total_count * mStoreCardsListBean.getDenomination_list().get(0).getUse_price()) + "元", "代金券总量");

    }

    private void openOrPauseCouponActs() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("activity_id", mStoreCardsListBean.getId() + "");
        if (cbSwithChecked) {
            map.put("state", 1 + "");   //
        } else {
            map.put("state", 0 + "");
        }
        mQueue.add(ParamTools.packParam(openOrPauseCouponActs, CouponAtcsActivity.this, CouponAtcsActivity.this,
                map));
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.optInt("status");
            String msg = json.optString("msg");
            if (url.contains(openOrPauseCouponActs)) {
                if (status == 0) {
                    cbSwith.setChecked(!cbSwithChecked);
                    cbSwithChecked = !cbSwithChecked;
                    if (cbSwithChecked) {
                        mStoreCardsListBean.setState(0);
                    } else {
                        mStoreCardsListBean.setState(1);
                    }
                    initViews();
                } else {
                    cbSwith.setChecked(!cbSwithChecked);
                    cbSwithChecked = !cbSwithChecked;
                    Tools.showToast(this, msg);
                }
            } else if (true) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    @OnClick({R.id.button_back, R.id.tv_history, R.id.cb_swith, R.id.tv_detail})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.cb_swith:
                showOperateDialog();
                break;
            case R.id.tv_history:
                intent.setClass(this, CouponHistoryActivity.class);
                intent.putExtra("activity_id", mStoreCardsListBean.getId() + "");
                startActivity(intent);
                break;
            case R.id.tv_detail:
                intent.setClass(this, CounponActsDetailActivity.class);
                intent.putExtra("mStoreCardsListBean", mStoreCardsListBean);
                startActivity(intent);
                break;
        }
    }

    private void showOperateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String tvContent[] = {
                "您确定要开启活动吗？",
                "您确定要暂停该活动吗？已领代金券的用户仍将正常使用",
                "活动已到结束时间，未使用的代金券全部已过期"};
        // 控件初始化
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_store_acts, null);
        //显示的内容
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_content.setText(tvContent[popType]);
        //对话按钮
        LinearLayout ll_two = (LinearLayout) view.findViewById(R.id.ll_two);
        TextView tv_iknow = (TextView) view.findViewById(R.id.tv_iknow);
        if (popType == 2) {
            tv_iknow.setVisibility(View.VISIBLE);
            ll_two.setVisibility(View.GONE);
            tv_iknow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialog.dismiss();
                    cbSwith.setVisibility(View.GONE);   //暂停的，已结束的弹框后消失

                }
            });
        } else {
            tv_iknow.setVisibility(View.GONE);
            ll_two.setVisibility(View.VISIBLE);

            Button btn_colse = (Button) view.findViewById(R.id.btn_colse);
            Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
            builder.setView(view);

            btn_colse.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    cbSwith.setChecked(!cbSwith.isChecked());
                    mAlertDialog.dismiss();
                }
            });
            btn_ok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mAlertDialog.dismiss();
                    openOrPauseCouponActs();
                }
            });

        }
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }
}
