/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.bean.CarshAccountBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.dialog.AuthCodeDialog.onConfirmListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 提现账号 账户列表
 */
public class CashAccountListActivity extends BaseActivity implements TextWatcher, onConfirmListener {
    @ViewInject(R.id.tv_add_cash)
    private LinearLayout tv_add_cash;// 添加账户
    @ViewInject(R.id.ll_bank)
    private LinearLayout ll_bank;// 银行卡layout
    @ViewInject(R.id.tv_bank_name)
    private TextView tv_bank_name;// 银行卡名字
    @ViewInject(R.id.tv_banl_delete)
    private TextView tv_banl_delete;// 银行卡删除
    @ViewInject(R.id.tv_banl_number)
    private TextView tv_banl_number;// 银行卡卡号
    @ViewInject(R.id.tv_banl_user)
    private TextView tv_banl_user;// 银行卡用户信息

    @ViewInject(R.id.ll_pay)
    private LinearLayout ll_pay;// 支付宝layout
    @ViewInject(R.id.tv_pay_name)
    private TextView tv_pay_name;// 支付宝名字
    @ViewInject(R.id.tv_pay_delete)
    private TextView tv_pay_delete;// 支付宝删除
    @ViewInject(R.id.tv_pay_number)
    private TextView tv_pay_number;// 支付宝账户
    @ViewInject(R.id.tv_pay_user)
    private TextView tv_pay_user;// 支付宝用户信息
    private List<CarshAccountBean> CarshAccountBeans;
    private int Accordintype = 0;// 新增判定值 1:两种账户都可以 2:银行卡 3:支付宝 //都有了就是0，不能再添加
    private int account_id;
    private int type;// 判断进入的路径 1为提现时进入

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_account_list);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        type = getIntent().getIntExtra("type", 0);
        title.setText("账户列表");
        initView();
    }

    private void initView() {
        tv_banl_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                for (CarshAccountBean cAccountBean : CarshAccountBeans) {
                    if (cAccountBean.getAccount_type() == 2) {
                        account_id = cAccountBean.getId();
                    }
                }
                // 删除银行卡账户
                Intent intent = new Intent(CashAccountListActivity.this, VerifyPaymentPasswordActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        tv_pay_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                for (CarshAccountBean cAccountBean : CarshAccountBeans) {
                    if (cAccountBean.getAccount_type() == 1) {
                        account_id = cAccountBean.getId();
                    }
                }
                // 删除支付宝账户
                Intent intent = new Intent(CashAccountListActivity.this, VerifyPaymentPasswordActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        tv_add_cash.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 跳去验证支付密码
                Intent intent = new Intent(CashAccountListActivity.this, VerifyPaymentPasswordActivity.class);
                startActivityForResult(intent, 2);
            }
        });
        ll_bank.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {   //银行卡
                if (type == 1) {
                    for (CarshAccountBean cAccountBean : CarshAccountBeans) {
                        if (cAccountBean.getAccount_type() == 2) {
                            account_id = cAccountBean.getId();
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra("withdraw_type", 2);
                    intent.putExtra("number", tv_banl_number.getText().toString());
                    intent.putExtra("account_id", account_id);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        ll_pay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {   //支付宝
                if (type == 1) {
                    for (CarshAccountBean cAccountBean : CarshAccountBeans) {
                        if (cAccountBean.getAccount_type() == 1) {
                            account_id = cAccountBean.getId();
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra("withdraw_type", 1);
                    intent.putExtra("number", tv_pay_number.getText().toString());
                    intent.putExtra("account_id", account_id);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    /**
     * 账户列表
     */
    private void accountList() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.accountList, this, this, map));
    }

    @Override
    protected void onResume() {
        super.onResume();
        accountList();
    }

    /**
     * 删除 提现账户
     */
    private void delAccount(String pay_pass_token) {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("account_id", account_id + "");
        map.put("pay_pass", pay_pass_token + "");
        mQueue.add(ParamTools.packParam(Constants.delAccount, this, this, map));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // 删除账户
                delAccount(data.getStringExtra("pay_pass"));
            } else if (requestCode == 2) {  //新增账户的接口数据
                Intent intent = new Intent();
                intent.setClass(CashAccountListActivity.this, AddAccount2Activity.class);
                intent.putExtra("pay_pass", data.getStringExtra("pay_pass"));   //已经md5操作
                intent.putExtra("Accordintype", Accordintype);// 新增判定值
                startActivity(intent);
            }
        }
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            Log.i("test", response);
            int status = json.getInt("status");
            String msg = json.getString("msg");
            if (status == 0) {
                if (url.contains(Constants.accountList)) {
                    CarshAccountBeans = JSON.parseArray(json.getString("result"), CarshAccountBean.class);
                    initDate();
                } else if (url.contains(Constants.delAccount)) {
                    Tools.showToast(getApplicationContext(), "删除成功");
                    ll_bank.setVisibility(View.GONE);
                    ll_pay.setVisibility(View.GONE);
                    tv_add_cash.setVisibility(View.VISIBLE);
                    accountList();
                }
            } else if (status == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(CashAccountListActivity.this, LoginActivity.class, false);
                Tools.showToast(CashAccountListActivity.this, "登录过期请重新登录");
                Tools.acts.clear();
            } else {
                Tools.showToast(CashAccountListActivity.this, msg);
            }
        } catch (JSONException e) {
            Tools.showToast(getApplicationContext(), "解析数据错误");
        }
    }

    /**
     * 初始化控件
     */
    private void initDate() {
        // 两种账户都存在 1:隐藏新增账户 2:显示账户信息
        if (CarshAccountBeans.size() > 1) {
            tv_add_cash.setVisibility(View.GONE);
            initCarsh();
        } else if (CarshAccountBeans.size() == 1) {
            initCarsh();
        } else {
            Accordintype = 1;
        }
    }

    // 显示数据
    private void initCarsh() {
        for (CarshAccountBean carshAccountBean : CarshAccountBeans) {
            if (carshAccountBean.getAccount_type() == 2) {//2为银行卡
                tv_bank_name.setText(carshAccountBean.getAccount_bank());
                tv_banl_number.setText(carshAccountBean.getAccount_card());
                tv_banl_user.setText(carshAccountBean.getAccount_name());
                ll_bank.setVisibility(View.VISIBLE);
                Accordintype = 3;   //允许为支付宝
            } else if (carshAccountBean.getAccount_type() == 1) {   //1为支付宝
                tv_pay_name.setText("支付宝");
                tv_pay_number.setText(carshAccountBean.getAccount_card());
                tv_pay_user.setText(carshAccountBean.getAccount_name());
                ll_pay.setVisibility(View.VISIBLE);
                Accordintype = 2;   //允许银行卡
            }
        }
    }

    @Override
    public void onConfirm(int id, String verifyCode) {
        if (id == R.id.btn_ok) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("phone", partnerBean.getSafe_phone());
            map.put("image_code", verifyCode);
            mQueue.add(ParamTools.packParam(Constants.sendPhoneMsg, this, this, map));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

}
