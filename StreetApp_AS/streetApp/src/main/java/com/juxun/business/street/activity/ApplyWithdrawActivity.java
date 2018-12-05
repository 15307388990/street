package com.juxun.business.street.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.bean.CarshAccountBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 类名称：ApplyWithdrawActivity 类描述： 我要提现 首页 创建人：罗富贵 创建时间：2016年5月11日
 */
public class ApplyWithdrawActivity extends BaseActivity {

    @ViewInject(R.id.tv_kahao)
    private TextView tv_kahao;// 卡号 或支付宝账号
    @ViewInject(R.id.tv_kahao_name)
    private TextView tv_kahao_name;// 卡号 或支付宝账号
    @ViewInject(R.id.ll_kaohao)
    private LinearLayout ll_kaohao;
    @ViewInject(R.id.tv_can_withdraw_price)
    private TextView tv_can_withdraw_price;// 可提现金额
    @ViewInject(R.id.et_price)
    private EditText et_price;// 提现金额
    @ViewInject(R.id.et_beizhu)
    private EditText et_beizhu;// 提现备注
    @ViewInject(R.id.tv_all)
    private TextView tv_all;// 全部提现
    @ViewInject(R.id.iv_arrw)
    private TextView iv_arrw;
    @ViewInject(R.id.btn_tixian)
    private Button btn_tixian;// 提现
    private int sum;
    private PopupWindow popWindow;
    private int account_id;// 提现账户
    private boolean isXin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applywithdraw);
        ViewUtils.inject(this);
        initTitle();
        title.setText("我要提现");
        initValues();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (isXin) {
            accountList();
        }

    }

    private void initValues() {
        et_price.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (et_price.getText().length() >= 1) {
                    btn_tixian.setBackgroundResource(R.drawable.button_bg1);
                    btn_tixian.setTextColor(getResources().getColor(android.R.color.white));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });
        obtainDataWithdrawList();
        loading();

    }

    /**
     * 单击事件
     */
    @OnClick({R.id.tv_all, R.id.ll_kaohao, R.id.btn_tixian})
    public void clickMethod(View v) {
        if (v.getId() == R.id.tv_all) {
            et_price.setText(sum / 100 + "");
        } else if (v.getId() == R.id.btn_tixian) {
            if (et_price.getText().length() >= 1) {
                // 提现金额小于可提现金额
                if (Double.parseDouble(et_price.getText().toString()) <= sum) {
                    if (account_id == 0) {
                        Tools.showToast(getApplicationContext(), "未选择提现账户");
                        return;
                    }
                    Intent intent = new Intent(ApplyWithdrawActivity.this, VerifyPaymentPasswordActivity.class);
                    startActivityForResult(intent, 1);

                } else {
                    Toast.makeText(getApplicationContext(), "账户余额不足", Toast.LENGTH_LONG).show();
                }
            }
        } else if (v.getId() == R.id.ll_kaohao) {
            Intent intent = new Intent(getApplicationContext(), CashAccountListActivity.class);
            intent.putExtra("type", 1);
            startActivityForResult(intent, 2);
        }
    }

    /**
     * 账户列表
     */
    private void accountList() {
        Map<String, String> map = new HashMap<String, String>();
        // auth_token 登录令牌
        // agency_id 机构id
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.accountList, this, this, map));
        loading();
    }

    private void applyWithdraw(String pay_pass_token) {
        Map<String, String> map = new HashMap<String, String>();
        // agency_id 机构id
        // withdraw_price 提现金额
        // withdraw_leave_message 留言
        // account_id 提现账号id
        // auth_token 必输项
        // pay_pass_token 支付密码验证成功后的token
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("pay_pass", pay_pass_token);
        map.put("price", Tools.getYuanFen(et_price.getText().toString()) + "");
        // map.put("withdraw.withdraw_name", aFinancebean.getWithdrawal_name());
        map.put("account_id", account_id + "");
        map.put("info_message", et_beizhu.getText().toString());
        mQueue.add(ParamTools.packParam(Constants.applyWithdraw, this, this, map));
        loading();
    }

    /* 获取账户列表数据 */
    public void obtainData() {
        Map<String, String> map = new HashMap<String, String>();
        // agency_id int 机构id
        // auth_token String 登陆令牌
        // admin_id int 管理员id
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.withdrawAccess, this, this, map));
        loading();
    }

    /* 获取可提现金额数据 */
    public void obtainDataWithdrawList() {
        Map<String, String> map = new HashMap<String, String>();
        // agency_id int 机构id
        // auth_token String 登陆令牌
        // admin_id int 管理员id
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("withdraw_stauts", "0");
        map.put("pageNum", 1 + "");
        map.put("pageSize", 1000 + "");
        mQueue.add(ParamTools.packParam(Constants.withdrawList, this, this, map));
        loading();
    }

    private void popWindow() {
        // TODO Auto-generated method stub
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(R.layout.sale_pop_window, null, false);
        Button cancel_ceButton = (Button) popupWindow_view.findViewById(R.id.pop_window_cancel);
        cancel_ceButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                popWindow.dismiss();
            }
        });
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popWindow = new PopupWindow(popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        // 设置动画效果
        // popWindow.setAnimationStyle(R.style.AnimationFade);
        // 这里是位置显示方式,在屏幕的左侧
        popWindow.showAtLocation(tv_kahao, Gravity.CENTER, 0, 0);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                // if (popupWindow != null && popupWindow.isShowing()) {
                // popupWindow.dismiss();
                // popupWindow = null;
                // }
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                applyWithdraw(data.getStringExtra("pay_pass"));
            } else if (requestCode == 2) {
                int withdraw_type = data.getIntExtra("withdraw_type", 0);
                account_id = data.getIntExtra("account_id", 0);
                isXin = false;
                if (withdraw_type == 1) {
                    tv_kahao.setText(data.getStringExtra("number"));
                    tv_kahao_name.setText("(支付宝)");
                } else if (withdraw_type == 2) {
                    tv_kahao.setText(data.getStringExtra("number"));
                    tv_kahao_name.setText("(银行卡)");
                }
            }

        }
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            String msg = json.optString("msg");
            if (stauts == 0) {
                if (url.contains(Constants.withdrawList)) {
                    JSONObject jsonObject = json.optJSONObject("result");
                    sum = jsonObject.optInt("can_withdraw_price");
                    tv_can_withdraw_price.setText("(可提现金额：" + Tools.getFenYuan(sum) + ")");
                    obtainData();
                } else if (url.contains(Constants.applyWithdraw)) {
                    Tools.jump(this, SucceedWithdrawAccessActivity.class, false);
                    this.finish();
                } else if (url.contains(Constants.accountList)) {
                    String accountlist = json.getString("result");
                    List<CarshAccountBean> CarshAccountBeans = JSON.parseArray(accountlist, CarshAccountBean.class);
                    if (CarshAccountBeans.size() > 0) {
                        tv_kahao.setText(CarshAccountBeans.get(0).getAccount_card());
                        account_id = CarshAccountBeans.get(0).getId();
                        if (CarshAccountBeans.get(0).getAccount_type() == 1) {
                            tv_kahao_name.setText("(支付宝)");
                        } else {
                            tv_kahao_name.setText("(银行卡)");
                        }
                    } else {
                        tv_kahao.setText("暂无账户");
                        tv_kahao_name.setText("");
                        // popWindow();
                    }
                }

            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }
}
