/**
 *
 */
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
import android.widget.EditText;
import android.widget.TextView;

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

/**
 * @version 修改密码
 */
public class ModifyPasswordActivity extends BaseActivity implements TextWatcher, onConfirmListener {
    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;// 手机号
    @ViewInject(R.id.et_code)
    private EditText et_code;// 验证码
    @ViewInject(R.id.tv_code)
    private TextView tv_code;// 获取验证码
    @ViewInject(R.id.btn_next)
    private Button btn_next;// 下一步
    private MyCountTimer countTimer = null;
    private AuthCodeDialog authCodeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("验证手机");
        initView();
    }

    private void initView() {
        et_code.addTextChangedListener(this);
        tv_phone.setText(partnerBean.getSafe_phone());

        countTimer = new MyCountTimer(this, tv_code, "获取验证码", R.color.tab_text_color_select, R.color.jiujiujiu);
        tv_code.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sendSafeMsg();
            }
        });
        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                validSafePhone();
            }
        });
        btn_next.setEnabled(initBtn());
    }

    // 修改登录密码发送手机验证码
    private void sendSafeMsg() {
        if (!tv_code.getText().toString().equals("获取验证码")) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.sendSafeMsg, this, this, map));
        loading();
    }

    // 验证安全手机验证码 [下一步按钮中操作]
    private void validSafePhone() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("sms_code", et_code.getText().toString());
        mQueue.add(ParamTools.packParam(Constants.validSafePhone, this, this, map));
        loading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // obtainData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                if (url.contains(Constants.sendSafeMsg)) {
                    if (authCodeDialog != null && authCodeDialog.isShowing()) {
                        authCodeDialog.dismiss();
                    }
                    countTimer.start();// 开启定时器
                    tv_code.setVisibility(View.VISIBLE);
                } else if (url.contains(Constants.validSafePhone)) {
                    Intent intent = new Intent(ModifyPasswordActivity.this, ModifyPasswordActivity2.class);
                    intent.putExtra("sms_code",et_code.getText().toString());
                    startActivity(intent);
                    finish();
                }
            } else if (status == -60001) {
                if (url.contains(Constants.sendSafeMsg)) {
                    authCodeDialog = new AuthCodeDialog(getApplicationContext(), ModifyPasswordActivity.this,
                            partnerBean.getAuth_token(), 100);
                    authCodeDialog.showAtLocation(btn_next, Gravity.CENTER, 0, 0);
                } else {
                    Tools.showToast(getApplicationContext(), msg);
                }
            } else if (status == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(ModifyPasswordActivity.this, LoginActivity.class, false);
                Tools.showToast(ModifyPasswordActivity.this, "登录过期请重新登录");
                Tools.acts.clear();
            } else {
                if (url.contains(Constants.sendSafeMsg)) {
                    if (authCodeDialog != null) {
                        authCodeDialog.Refresh();
                    }
                }
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            Tools.showToast(this, "解析数据错误");
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
        if (et_code.getText().length() > 0) {
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
