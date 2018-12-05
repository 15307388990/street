package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.MyCountTimer;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.dialog.AuthCodeDialog;
import com.juxun.business.street.widget.dialog.AuthCodeDialog.onConfirmListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.juxun.business.street.config.Constants.sendPhoneMsg;

/**
 * @version 验证手机
 */
public class MobilePhoneActivity extends BaseActivity implements TextWatcher, onConfirmListener {

    @ViewInject(R.id.et_phone)
    private EditText et_phone;// 手机号
    @ViewInject(R.id.et_code)
    private EditText et_code;// 验证码
    @ViewInject(R.id.tv_code)
    private TextView tv_code;// 获取验证码
    @ViewInject(R.id.btn_next)

    private Button btn_next;// 下一步
    private MyCountTimer countTimer = null;
    private AuthCodeDialog authCodeDialog;
    private int type;// 0为初次绑定手机用，1为验证身份设置支付密码 2为修改支付密码
    private String phone;// 需要发送验证码的手机

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_phone);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("验证手机");
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);

        if (type != 2) {    //修改支付密码不可以操作
            back.setVisibility(View.INVISIBLE);
        }
        initView();
    }

    private void initView() {
        et_code.addTextChangedListener(this);
        et_phone.addTextChangedListener(this);
        if (type == 1 || type == 2) {
            et_phone.setText(Tools.pNumber(partnerBean.getSafe_phone()));
            // 设置不可编辑
            et_phone.setFocusable(false);
            et_phone.setFocusableInTouchMode(false);
        }

        countTimer = new MyCountTimer(this, tv_code, "获取验证码", R.color.tab_text_color_select, R.color.jiujiujiu);
        tv_code.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (type == 1 || type == 2) {
                    phone = partnerBean.getSafe_phone();
                    sentSafeMsg();
                } else {
                    phone = et_phone.getText().toString();
                    sendPhoneMsg();
                }

            }
        });
        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (type == 1 || type == 2) {
                    validSafePhone();
                } else {
                    bindPhone();
                }
            }
        });
        btn_next.setEnabled(initBtn());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //手动输入的手机号
    private void sendPhoneMsg() {
        //防止重复点击操作
        if (!tv_code.getText().toString().equals("获取验证码")) {
            return;
        }
        if (et_phone.getText().toString().length() == 0) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (type != 1 && type != 2) {
            if (!Tools.isMobileNum(et_phone.getText().toString())) {
                Toast.makeText(this, "手机号格式有误", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Map<String, String> map = new HashMap<>();
        map.put("phone", et_phone.getText().toString());
        mQueue.add(ParamTools.packParam(sendPhoneMsg, this, this, map));
        loading();
    }

    //安全手机的验证码验证
    private void sentSafeMsg() {
        //防止重复点击操作
        if (!tv_code.getText().toString().equals("获取验证码")) {
            return;
        }
        if (et_phone.getText().toString().length() == 0) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (type != 1 && type != 2) {
            if (!Tools.isMobileNum(et_phone.getText().toString())) {
                Toast.makeText(this, "手机号格式有误", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.sendSafeMsg, this, this, map));
        loading();
    }

    // 为店铺绑定安全手机
    private void bindPhone() {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("phonemsg", et_code.getText().toString());
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.bindPhone, this, this, map));
        loading();
    }

    //密保手机验证
    private void validSafePhone() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("sms_code", et_code.getText().toString());
        mQueue.add(ParamTools.packParam(Constants.validSafePhone, this, this, map));
        loading();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // 验证成功 跳转至修改支付密码
                String pay_passMd5 = data.getStringExtra("pay_pass");
                partnerBean.setPay_password(1);
                partnerBean.setPass_pay(pay_passMd5);
                String jsonString = JSON.toJSONString(partnerBean);
                mSavePreferencesData.putStringData("mallSet", jsonString);

                Intent intent = new Intent(MobilePhoneActivity.this, SetPayPasswordOneActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("sms_code", et_code.getText().toString());

                intent.putExtra("pay_pass", pay_passMd5);   //已经经过md5处理的老密码
                startActivity(intent);
                finish();
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
                } else if (url.contains(Constants.sendPhoneMsg)) {
                    if (authCodeDialog != null && authCodeDialog.isShowing()) {
                        authCodeDialog.dismiss();
                    }
                    countTimer.start();// 开启定时器
                    tv_code.setVisibility(View.VISIBLE);
                } else if (url.contains(Constants.validSafePhone)) {
                    Intent intent = new Intent();
                    if (type == 1) {
                        intent.setClass(this, SetPayPasswordOneActivity.class);
                        intent.putExtra("sms_code", et_code.getText().toString());
                        startActivity(intent);
                    } else if (type == 2) {
                        intent.setClass(this, VerifyPaymentPasswordActivity.class);
                        startActivityForResult(intent, 1);
                    }
                } else if (url.contains(Constants.bindPhone)) {
                    // 绑定成功 1改变数据
                    partnerBean.setSafe_phone(phone);
                    String phone_token = json.getString("phone_token");
                    //partnerBean.setphone(phone_token);
                    String jsonString = JSON.toJSONString(partnerBean);
                    mSavePreferencesData.putStringData("json", jsonString);
                    // 2 跳转至成功界面
                    Intent intent = new Intent(MobilePhoneActivity.this, RegisteredSuccessfullyActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                    finish();
                }
//                else if (url.contains(Constants.verificationSettingPhone)) {
//                    // token 用来设置支付密码
//                    String phone_token = json.getString("phone_token");
//                    storeBean.setPhone_token(phone_token);
//                    String jsonString = JSON.toJSONString(storeBean);
//                    mSavePreferencesData.putStringData("json", jsonString);
//                    if (type == 1) {
//                        Tools.jump(MobilePhoneActivity.this, SetPayPasswordOneActivity.class, true);
//                    } else if (type == 2) {
//                        Intent intent = new Intent();
//                        intent.setClass(this, VerifyPaymentPasswordActivity.class);
//                        startActivityForResult(intent, 1);
//                    }
//                }
            } else if (status == -60001) {
                if (url.contains(Constants.sendSafeMsg)) {
                    authCodeDialog = new AuthCodeDialog(getApplicationContext(), MobilePhoneActivity.this, partnerBean.getAuth_token(), 100);
                    authCodeDialog.showAtLocation(btn_next, Gravity.CENTER, 0, 0);
                } else if (url.contains(Constants.sendPhoneMsg)) {
                    authCodeDialog = new AuthCodeDialog(getApplicationContext(), MobilePhoneActivity.this, phone);
                    authCodeDialog.showAtLocation(btn_next, Gravity.CENTER, 0, 0);
                } else {
                    Tools.showToast(getApplicationContext(), msg);
                }
            } else if (status == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(MobilePhoneActivity.this, LoginActivity.class, false);
                Tools.showToast(MobilePhoneActivity.this, "登录过期请重新登录");
                Tools.acts.clear();
            } else {
                if (url.contains(sendPhoneMsg) || url.contains(Constants.sendPhoneMsg)) {
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
    public void onConfirm(int id, String verifyCode) {
        if (id == R.id.btn_ok) {
            Map<String, String> map = new HashMap<>();
            map.put("auth_token", partnerBean.getAuth_token());
            map.put("verification_code", verifyCode);
            mQueue.add(ParamTools.packParam(Constants.sendSafeMsg, this, this, map));
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
        if (et_code.getText().length() > 0 && et_phone.getText().length() > 0) {
            btn_next.setBackgroundResource(R.drawable.button_bg1);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            return true;
        } else {
            btn_next.setBackgroundResource(R.drawable.button_bg);
            btn_next.setTextColor(getResources().getColor(R.color.jiujiujiu));
            return false;
        }
    }

}
