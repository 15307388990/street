/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.MyCountTimer;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.dialog.AuthCodeDialog;
import com.juxun.business.street.widget.dialog.AuthCodeDialog.onConfirmListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.juxun.business.street.config.Constants.sendSafeMsg;

/**
 * @version 添加账户
 */
public class AddAccount2Activity extends BaseActivity implements TextWatcher, onConfirmListener {

    @ViewInject(R.id.ll_banl_cb)
    private LinearLayout ll_banl_cb;
    @ViewInject(R.id.iv_banl)
    private ImageView iv_banl;

    @ViewInject(R.id.ll_alipay_cb)
    private LinearLayout ll_alipay_cb;
    @ViewInject(R.id.iv_alipay)
    private ImageView iv_alipay;

    @ViewInject(R.id.ll_alipay)
    private LinearLayout ll_alipay;// 支付宝
    @ViewInject(R.id.et_alipay_number)
    private EditText et_alipay_number;// 支付宝账户
    @ViewInject(R.id.et_alipay_name)
    private EditText et_alipay_name;// 支付宝账户 真实姓名

    @ViewInject(R.id.ll_banl)
    private LinearLayout ll_banl;// 银行卡
    @ViewInject(R.id.tv_banl_adds)
    private TextView tv_banl_adds;// 银行卡 所属银行
    @ViewInject(R.id.et_banl_number)
    private EditText et_banl_number;// 银行卡 卡号
    @ViewInject(R.id.tv_banl_line)
    private TextView tv_banl_line;// 银行卡 开户行名称
    @ViewInject(R.id.et_banl_name)
    private EditText et_banl_name;// 银行卡 真实姓名

    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;// 手机号
    @ViewInject(R.id.et_code)
    private EditText et_code;// 手机验证码
    @ViewInject(R.id.tv_code)
    private TextView tv_code;// 获取验证码
    @ViewInject(R.id.btn_next)
    private Button btn_next;// 完成
    private int account_type = 1; //  1为支付宝、2银行卡
    private int Accordintype = 1;// 新增判定值 1:两种账户都可以 2:银行卡 3:支付宝

    private MyCountTimer countTimer = null;
    private AuthCodeDialog authCodeDialog;

    private String phoneString;// 密保手机号
    private String pay_pass;
    private String account_bank_branch_code;
    private String account_bank_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("添加账户");
        initView();
    }

    private void initView() {
//        pay_pass_token = getIntent().getStringExtra("pay_pass_token");

        Intent intent = getIntent();
        pay_pass = intent.getStringExtra("pay_pass");
        Accordintype = intent.getIntExtra("Accordintype", 0);  //账户修改
        phoneString = partnerBean.getSafe_phone();
        tv_phone.setText(Tools.pNumber(phoneString));

        countTimer = new MyCountTimer(this, tv_code, "获取验证码", R.color.tab_text_color_select, R.color.jiujiujiu);

        // 三种布局 默认两种都可以选择 默认选择支付宝
        // 新增判定值 1:两种账户都可以 2:银行卡 3:支付宝，account_type中1为支付宝2为银行卡
        switch (Accordintype) {
            case 1:
                break;
            case 2:
                ll_alipay.setVisibility(View.GONE);
                ll_banl.setVisibility(View.VISIBLE);
                ll_alipay_cb.setVisibility(View.GONE);
                iv_banl.setImageDrawable(getResources().getDrawable(R.drawable.login_icon_select));
                ll_banl_cb.setClickable(false);
                account_type = 2;
                break;
            case 3:
                ll_banl.setVisibility(View.GONE);
                ll_alipay.setVisibility(View.VISIBLE);
                ll_banl_cb.setVisibility(View.GONE);
                ll_alipay_cb.setClickable(false);
                account_type = 1;
                break;
            default:
                break;
        }

        btn_next.setEnabled(false);
        et_alipay_name.addTextChangedListener(this);
        et_alipay_number.addTextChangedListener(this);
        et_banl_name.addTextChangedListener(this);
        et_code.addTextChangedListener(this);
        et_banl_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btn_next.setEnabled(initBtn());
                String string = et_banl_number.getText().toString().replace(" ", "");
                if (string.length() >= 7) {
                    getBankName(string.subSequence(0, 6).toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // 添加账户
    private void addAccount() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("pay_pass", pay_pass);    //支付密码
        map.put("sms_code", et_code.getText().toString());
        map.put("account_type", account_type + "");

        if (account_type == 1) {    //1为支付宝，2为银行卡
            map.put("account_name", et_alipay_name.getText().toString());
            map.put("account_card", et_alipay_number.getText().toString());
        } else if (account_type == 2) {
            map.put("account_name", et_banl_name.getText().toString());
            map.put("account_card", et_banl_number.getText().toString().replace(" ", ""));
            map.put("account_bank", tv_banl_adds.getText().toString());
            map.put("account_bank_branch", tv_banl_adds.getText().toString());
            map.put("account_bank_branch_code", account_bank_branch_code);
            if (account_bank_address == "") {
                account_bank_address = tv_banl_adds.getText().toString();
            }
            map.put("account_bank_address", account_bank_address);
        }
        mQueue.add(ParamTools.packParam(Constants.addAccount, this, this, map));
        loading();
    }

    // 根据卡号前6位 获取所属银行
    private void getBankName(String card_no) {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("card_no", card_no);
        mQueue.add(ParamTools.packParam(Constants.getBankName, this, this, map));
    }

    // 修改登录密码发送手机验证码
    private void sendSafeMsg() {
        if (!tv_code.getText().toString().equals("获取验证码")) {    //在发送的过程中无法进行添加
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(sendSafeMsg, this, this, map));
        loading();
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.btn_next, R.id.ll_banl_cb, R.id.ll_alipay_cb, R.id.tv_code, R.id.tv_banl_line})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.btn_next: // 完成
                addAccount();
                break;
            case R.id.tv_code:  // 获取手机验证码
                sendSafeMsg();
                break;
            case R.id.ll_banl_cb:   //bank的操作
                iv_banl.setImageDrawable(getResources().getDrawable(R.drawable.login_icon_select));
                iv_alipay.setImageDrawable(getResources().getDrawable(R.drawable.login_icon_select2));
                ll_alipay.setVisibility(View.GONE);
                ll_banl.setVisibility(View.VISIBLE);
                account_type = 2;
                break;
            case R.id.ll_alipay_cb: //支付宝
                iv_banl.setImageDrawable(getResources().getDrawable(R.drawable.login_icon_select2));
                iv_alipay.setImageDrawable(getResources().getDrawable(R.drawable.login_icon_select));
                ll_alipay.setVisibility(View.VISIBLE);
                ll_banl.setVisibility(View.GONE);
                account_type = 1;
                break;
            case R.id.tv_banl_line: //输入开户行名称
                if (tv_banl_adds.getText().toString().length() > 1) {
                    Intent intent = new Intent(AddAccount2Activity.this, SelectCityActivity.class);
                    intent.putExtra("belongs_bank", tv_banl_adds.getText().toString());// 所属银行
                    startActivityForResult(intent, 1);
                } else {
                    Tools.showToast(getApplicationContext(), "请先输入正确的银行卡号");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                tv_banl_line.setText(data.getStringExtra("cname"));
                account_bank_branch_code = data.getStringExtra("account_bank_branch_code");
                account_bank_address = data.getStringExtra("account_bank_address");
            }
        }
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.getInt("status");
            String msg = json.getString("msg");
            if (status == 0) {
                if (url.contains(Constants.sendSafeMsg)) {
                    if (authCodeDialog != null && authCodeDialog.isShowing()) {
                        authCodeDialog.dismiss();
                    }
                    countTimer.start();// 开启定时器
                    tv_code.setVisibility(View.VISIBLE);
                } else if (url.contains(Constants.getBankName)) {
                    tv_banl_adds.setText(json.getString("result"));
                } else if (url.contains(Constants.addAccount)) {
                    Tools.showToast(getApplicationContext(), "添加成功");
                    finish();
                }
            } else if (status == -60001) {
                if (url.contains(Constants.sendSafeMsg)) {
                    authCodeDialog = new AuthCodeDialog(getApplicationContext(), AddAccount2Activity.this, partnerBean.getAuth_token(), 100);
                    authCodeDialog.showAtLocation(btn_next, Gravity.CENTER, 0, 0);
                } else {
                    Tools.showToast(this, msg);
                }
            } else if (status == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(AddAccount2Activity.this, LoginActivity.class, false);
                Tools.showToast(AddAccount2Activity.this, "登录过期请重新登录");
                Tools.acts.clear();
            } else {
                if (url.contains(Constants.sendSafeMsg)) {
                    if (authCodeDialog != null) {
                        authCodeDialog.Refresh();
                    }
                }
                Tools.showToast(getApplicationContext(), msg);
            }
        } catch (JSONException e) {
            Tools.showToast(getApplicationContext(), "解析数据错误");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        btn_next.setEnabled(initBtn());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private boolean initBtn() {
        if (et_code.getText().length() > 0 && et_alipay_name.getText().length() > 0
                && et_alipay_number.getText().length() > 0) {
            btn_next.setBackgroundResource(R.drawable.button_bg1);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            return true;
        } else if (et_code.getText().length() > 0 && tv_banl_line.getText().length() > 0
                && et_banl_name.getText().length() > 0 && et_banl_number.getText().length() > 0
                && tv_banl_adds.getText().length() > 0) {
            btn_next.setBackgroundResource(R.drawable.button_bg1);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            return true;

        } else {
            btn_next.setBackgroundResource(R.drawable.button_bg);
            btn_next.setTextColor(getResources().getColor(R.color.jiujiujiu));
            return false;
        }

    }

    @Override
    public void onConfirm(int id, String verifyCode) {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("verification_code", verifyCode);
        mQueue.add(ParamTools.packParam(Constants.sendSafeMsg, this, this, map));
    }
}
