package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.juxun.business.street.bean.Agreement7;
import com.juxun.business.street.bean.StoreRegisterBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.MD5Util;
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

/**
 * @author luoming 注册流程第一步
 */

public class RegisteredOneActivity extends BaseActivity implements TextWatcher, onConfirmListener {
    @ViewInject(R.id.et_account)
    private EditText et_account;// 账号
    @ViewInject(R.id.et_phone)
    private EditText et_phone;// 手机号
    @ViewInject(R.id.et_code)
    private EditText et_code;// 验证码
    @ViewInject(R.id.et_password)
    private EditText et_password;// 密码
    @ViewInject(R.id.et_repeat_password)
    private EditText et_repeat_password;// 重复密码
    @ViewInject(R.id.btn_next)
    private Button btn_next;// 下一步
    @ViewInject(R.id.cb_cod)
    private CheckBox cb_cod;// 同意
    @ViewInject(R.id.tv_code)
    private TextView tv_code;// 获取验证码
    @ViewInject(R.id.ll_registerRules)
    private LinearLayout ll_registerRules;

    private MyCountTimer countTimer = null;
    private AuthCodeDialog authCodeDialog;
    private StoreRegisterBean storeRegisterBean;    //整个注册需要的内容信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_one);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        btn_next.setEnabled(initBtn());
        initTitle();
        title.setText("注册");
        initView();
        storeRegisterBean = new StoreRegisterBean();
    }

    private void initView() {
        et_account.addTextChangedListener(this);
        et_phone.addTextChangedListener(this);
        et_code.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        et_repeat_password.addTextChangedListener(this);
        countTimer = new MyCountTimer(this, tv_code, "获取验证码", R.color.tab_text_color_select, R.color.jiujiujiu);
        cb_cod.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btn_next.setEnabled(initBtn());

            }
        });
        ll_registerRules.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisteredOneActivity.this, WebviewActivity.class);
                Agreement7 agreement7 = new Agreement7();
                agreement7.setLink_url(Constants.mainUrl + Constants.registerRules);
                agreement7.setTitle("注册协议");
                intent.putExtra("agreement7", agreement7);
                startActivity(intent);
            }
        });
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.btn_next, R.id.tv_code})
    public void clickMethod(View v) {
        if (v.getId() == R.id.btn_next) {
            if (Tools.checkEmail(et_account.getText().toString())) {
                if (et_password.getText().toString().equals(et_repeat_password.getText().toString())) {
                    if (et_password.getText().length() < 6 || et_password.getText().length() >= 18) {
                        Tools.showToast(this, "密码不符合规则");
                        return;
                    }
                    if (et_phone.getText().length() != 11) {
                        Tools.showToast(this, "手机号输入不规范");
                        return;
                    }
                    validRegister();
                } else {
                    Tools.showToast(this, "密码不一致！");
                }
            } else {
                Tools.showToast(this, "邮箱不正确！");
            }
        } else if (v.getId() == R.id.tv_code) {
            sendPhoneMsg();
        }
    }

    private void sendPhoneMsg() {
        if (!tv_code.getText().toString().equals("获取验证码")) {
            return;
        }
        if (et_phone.getText().toString().length() == 0) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_phone.getText().length() != 11) {
            Toast.makeText(this, "手机号格式有误", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("phone", et_phone.getText().toString());
        mQueue.add(ParamTools.packParam(Constants.sendPhoneMsg, this, this, map));
        loading();
    }

    //首先验证账号是否被注册
    private void validRegister() {
        // admin_account 帐号
        // admin_phone 手机号
        // msg_code 短信验证码
        // pass_word 密码
        // confirm_pass 确认密码
        Map<String, String> map = new HashMap<>();
        storeRegisterBean.setAdmin_phone(et_phone.getText().toString());
        storeRegisterBean.setMsg_code(et_code.getText().toString());
        storeRegisterBean.setAdmin_pass(MD5Util.getMD5String(et_password.getText().toString()));
        storeRegisterBean.setConfirm_pass(MD5Util.getMD5String(et_repeat_password.getText().toString()));
        storeRegisterBean.setAdmin_account(et_account.getText().toString());
        map.put("admin_phone", storeRegisterBean.getAdmin_phone());
        map.put("msg_code", storeRegisterBean.getMsg_code());
        map.put("admin_pass", storeRegisterBean.getAdmin_pass());
        map.put("confirm_pass", storeRegisterBean.getConfirm_pass());
        map.put("admin_account", storeRegisterBean.getAdmin_account());
        mQueue.add(ParamTools.packParam(Constants.validRegister, this, this, map));
        loading();
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            Log.i("test", response);
            int status = json.getInt("status");
            String msg = json.getString("msg");
            if (url.contains(Constants.sendPhoneMsg)) {
                if (status == 0) {
                    if (authCodeDialog != null && authCodeDialog.isShowing()) {
                        authCodeDialog.dismiss();
                    }
                    countTimer.start();// 开启定时器
                    tv_code.setVisibility(View.VISIBLE);
                } else if (status == -60001) {
                    authCodeDialog = new AuthCodeDialog(this, RegisteredOneActivity.this,
                            et_phone.getText().toString());
                    authCodeDialog.showAtLocation(btn_next, Gravity.CENTER, 0, 0);
                } else {
                    if (authCodeDialog != null) {
                        authCodeDialog.Refresh();
                    }
                    Tools.showToast(this, msg);
                }
            } else if (url.contains(Constants.validRegister)) {
                if (status == 0) {
                    Intent intent = new Intent(RegisteredOneActivity.this, RegisteredTwoActivity.class);
                    intent.putExtra("storeRegisterBean", storeRegisterBean);
                    startActivity(intent);
                } else if (status == -1) {
                    Tools.showToast(this, msg);
                } else {
                    Tools.showToast(this, msg);
                }
            }
        } catch (JSONException e) {
            Tools.showToast(this, "解析数据错误");
        }
    }

    private boolean initBtn() {
        if (et_account.getText().length() > 0 && et_phone.getText().length() > 1 && et_code.getText().length() > 0
                && et_password.getText().length() >= 1 && et_repeat_password.getText().length() >= 1
                && cb_cod.isChecked() == true) {
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        btn_next.setEnabled(initBtn());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onConfirm(int id, String verifyCode) {
        if (id == R.id.btn_ok) {
            Map<String, String> map = new HashMap<>();
            map.put("phone", et_phone.getText().toString());
            map.put("verification_code", verifyCode);
            mQueue.add(ParamTools.packParam(Constants.sendPhoneMsg, this, this, map));
        }
    }
}
