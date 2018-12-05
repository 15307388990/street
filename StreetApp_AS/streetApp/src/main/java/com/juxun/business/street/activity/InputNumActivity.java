/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxun.business.street.UILApplication;
import com.juxun.business.street.bean.ParseModel;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oubowu.slideback.SlideBackHelper;
import com.oubowu.slideback.SlideConfig;
import com.oubowu.slideback.widget.SlideBackLayout;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：Street 类名称：VipListActivity 类描述： 输入金额 创建人：WuJianhua 创建时间：2015年5月28日
 * 上午9:40:39 修改人：WuJianhua 修改时间：2015年5月28日 上午9:40:39 修改备注：
 */
public class InputNumActivity extends BaseActivity {

    @ViewInject(R.id.countMoney)
    private TextView countMoney;// 总金额
    @ViewInject(R.id.zero)
    private TextView zero;// 0
    @ViewInject(R.id.one)
    private TextView one;// 1
    @ViewInject(R.id.two)
    private TextView two;// 2
    @ViewInject(R.id.three)
    private TextView three;// 3
    @ViewInject(R.id.four)
    private TextView four;// 4
    @ViewInject(R.id.five)
    private TextView five;// 5
    @ViewInject(R.id.six)
    private TextView six;// 6
    @ViewInject(R.id.seven)
    private TextView seven;// 7
    @ViewInject(R.id.eight)
    private TextView eight;// 8
    @ViewInject(R.id.nine)
    private TextView nine;// 9
    @ViewInject(R.id.point)
    private TextView point;// .
    @ViewInject(R.id.delete)
    private ImageView delete;// delete
    @ViewInject(R.id.clearing)
    private Button clearing;// 结算

    private String count;
    private boolean canClearing;
    /**
     * 0 微信支付 1 支付宝支付 2 选择支付
     */
    private int payMethod;
    private SlideBackLayout mSlideBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_num);

        mSlideBackLayout = SlideBackHelper.attach(
                // 当前Activity
                this,
                // Activity栈管理工具
                UILApplication.getActivityHelper(),
                // 参数的配置
                new SlideConfig.Builder()
                        // 屏幕是否旋转
                        .rotateScreen(false)
                        // 是否侧滑
                        .edgeOnly(false)
                        // 是否禁止侧滑
                        .lock(false)
                        // 侧滑的响应阈值，0~1，对应屏幕宽度*percent
                        .edgePercent(0.1f)
                        // 关闭页面的阈值，0~1，对应屏幕宽度*percent
                        .slideOutPercent(0.5f).create(),
                // 滑动的监听
                null);

        ViewUtils.inject(this);
        initTitle();
        title.setText(R.string.receive_money);

        // obtainData();
    }


    public void add(String addStr) {
        String str = countMoney.getText().toString();
        if (str == null || str.equals("")) {
            countMoney.setText(addStr);
        } else if (str.equals("0")) {
            addStr = addStr.equals(".") ? str + addStr : addStr;
            countMoney.setText(addStr);
        } else if (str.contains(".") && str.length() - str.indexOf(".") > 2) {
            return;
        } else {
            if (str.contains(".") && addStr.equals("."))
                return;
            addStr = str + addStr;
            double d = Double.parseDouble(addStr);
            if (!addStr.endsWith(".") && d > 99999.99)
                return;
            countMoney.setText(addStr);
        }
        double d = Double.parseDouble(addStr);
        if (!addStr.endsWith(".") && d <= 99999.99 && d >= 0.01) {
            clearing.setBackgroundResource(R.drawable.button_bg1);
            canClearing = true;
        } else {
            clearing.setBackgroundResource(R.drawable.button_bg);
            canClearing = false;
        }
        count = addStr;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mSlideBackLayout.isComingToFinish();
    }

    public void delete() {
        String str = countMoney.getText().toString();
        if (str == null || str.equals("") || str.equals("0"))
            return;
        if (str.length() == 1) {
            str = "0";
        } else {
            str = str.substring(0, str.length() - 1);
        }
        countMoney.setText(str);
        double d = Double.parseDouble(str);
        if (!str.endsWith(".") && d <= 9999.99 && d >= 0.01) {
            clearing.setBackgroundResource(R.drawable.button_bg1);
            canClearing = true;
        } else {
            clearing.setBackgroundResource(R.drawable.button_bg);
            canClearing = false;
        }
        count = str;
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight,
            R.id.nine, R.id.point, R.id.delete, R.id.clearing})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.zero:
                add("0");
                break;
            case R.id.one:
                add("1");
                break;
            case R.id.two:
                add("2");
                break;
            case R.id.three:
                add("3");
                break;
            case R.id.four:
                add("4");
                break;
            case R.id.five:
                add("5");
                break;
            case R.id.six:
                add("6");
                break;
            case R.id.seven:
                add("7");
                break;
            case R.id.eight:
                add("8");
                break;
            case R.id.nine:
                add("9");
                break;
            case R.id.point:
                add(".");
                break;
            case R.id.delete:
                delete();
                break;
            case R.id.clearing:
                if (!canClearing)
                    return;
                Intent intent = new Intent(this, PayDialogActivity.class);
                intent.putExtra("count", count);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    /* 获取数据 */
    public void obtainData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", ParseModel.loginBean.getUserId() + "");
        map.put("authToken", ParseModel.loginBean.getAuthToken() + "");
        map.put("oper_id", ParseModel.loginBean.getOper_id() + "");
        mQueue.add(ParamTools.packParam(Constants.paySetting, this, this, map));
        mQueue.start();
        loading();
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("stauts");
            if (stauts == 0) {
                boolean ali = json.optBoolean("alipaySwitch");
                boolean weixin = json.optBoolean("wepaySwitch");
                if (ali && weixin)
                    payMethod = 2;
                else if (ali)
                    payMethod = 1;
                else if (weixin)
                    payMethod = 0;
            } else {
                Tools.showToast(getApplicationContext(), "账户错误");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

}
