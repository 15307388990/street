/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.dialog.PromptDialog;
import com.juxun.business.street.widget.dialog.PromptDialog.onConfirmListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @version 支付安全设置
 */
public class SetPaymentSecurityActivity extends BaseActivity {
    @ViewInject(R.id.ll_phone)
    private LinearLayout ll_phone;// 修改密保手机

    @ViewInject(R.id.ll_pay_password)
    private LinearLayout ll_pay_password; // 修改支付密码
    @ViewInject(R.id.ll_gestures_password)
    private LinearLayout ll_gestures_password; // 修改手势密码
    @ViewInject(R.id.cb_swith)
    private CheckBox cb_swith; // 手势密码开关

    @ViewInject(R.id.tv_phone)
    private TextView tv_phone; // 密保手机

    @ViewInject(R.id.v_1)
    private View v_1;
    @ViewInject(R.id.tv_ismima)
    private TextView tv_ismima;

    private boolean isGesturesPassword;// 是否开启手势密码
    private String lock_key;// 手势密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_payment_security);
        ViewUtils.inject(this);
        initTitle();
        title.setText("支付安全设置");

        isGesturesPassword = mSavePreferencesData.getBooleanData("isGesturesPassword");
        initView();
        cb_swith.setChecked(isGesturesPassword);
    }

    private void initView() {
        tv_phone.setText(Tools.pNumber(partnerBean.getSafe_phone()));
        cb_swith.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //  如果手势密码未设置 跳转至手势密码设置界面
                lock_key = mSavePreferencesData.getStringData("lock_key");
                if (lock_key.equals("")) {
                    DetectBounced();
                    cb_swith.setChecked(false);
                    return;
                }
                if (isChecked) {
                    Tools.showToast(SetPaymentSecurityActivity.this, "开启手势密码");
                    ll_gestures_password.setVisibility(View.VISIBLE);
                    v_1.setVisibility(View.VISIBLE);
                    mSavePreferencesData.putBooleanData("isGesturesPassword", true);
                } else {
                    Tools.showToast(SetPaymentSecurityActivity.this, "关闭手势密码");
                    ll_gestures_password.setVisibility(View.GONE);
                    mSavePreferencesData.putBooleanData("isGesturesPassword", false);
                    v_1.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 检测是否弹框
     */
    private void DetectBounced() {
        // 如果没有密保手机 弹框密保手机的框
        PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.setContent("账号尚未设置手势密码");
        promptDialog.setText("取消", "去设置");
        promptDialog.setVisibility(View.VISIBLE);
        promptDialog.setonConfirmListener(new onConfirmListener() {

            @Override
            public void onConfirm(int id) {
                if (id == R.id.btn_two) {
                    Intent intent = new Intent(SetPaymentSecurityActivity.this, SetGesturesPasswordActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });
        promptDialog.showAtLocation(ll_phone, Gravity.CENTER, 0, 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSavePreferencesData.getBooleanData("isMima")) {
            tv_ismima.setVisibility(View.VISIBLE);
        } else {
            tv_ismima.setVisibility(View.GONE);
        }
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.ll_gestures_password, R.id.ll_phone, R.id.ll_pay_password})
    public void clickMethod(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            // 修改手势密码
            case R.id.ll_gestures_password:
                intent.setClass(this, GesturesLoginActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            // 更换密保手机
            case R.id.ll_phone:
                // intent.setClass(this, ReplaceEncryptedPhonesActivity.class);
                // startActivityForResult(intent, 1);
                break;
            // 修改支付密码
            case R.id.ll_pay_password:
                intent.setClass(this, MobilePhoneActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                tv_phone.setText(Tools.pNumber(partnerBean.getSafe_phone()));
            } else if (requestCode == 2) {
                // 修改支付密码
                // 验证成功 跳转至修改支付密码
                Intent intent = new Intent();
                intent.setClass(SetPaymentSecurityActivity.this, SetPayPasswordOneActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            if (stauts == 0) {
                if (url.contains(Constants.updateBusinessRange)) {
                }
            } else if (stauts < 0) {
                Tools.dealErrorMsg(this, url, stauts, json.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }
}
