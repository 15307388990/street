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
import android.widget.Button;
import android.widget.EditText;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.MD5Util;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 修改登录密码[前面一步先验证支付密码的情况]
 */
public class ModifyPasswordActivity2 extends BaseActivity implements TextWatcher {
    @ViewInject(R.id.et_old)
    private EditText et_old;// 旧密码
    @ViewInject(R.id.et_new)
    private EditText et_new;// 新密码
    @ViewInject(R.id.et_new_to)
    private EditText et_new_to;// 新密码验证
    @ViewInject(R.id.btn_next)
    private Button btn_next;// 保存
    String sms_code;// 验证手机成功后返回后的令牌对应phone_token

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password2);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("修改登录密码");
        initView();
    }

    private void initView() {
        sms_code = getIntent().getStringExtra("sms_code");

        et_old.addTextChangedListener(this);
        et_new.addTextChangedListener(this);
        et_new_to.addTextChangedListener(this);
        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (et_new.getText().length() < 6 || et_new.getText().length() > 18) {
                    Tools.showToast(ModifyPasswordActivity2.this, "密码6-18位之间");
                    return;
                }
                if (et_new.getText().toString().equals(et_new_to.getText().toString())) {
                    updateAdminPass();
                } else {
                    Tools.showToast(ModifyPasswordActivity2.this, "新密码确认输入不一致");
                }

            }
        });
    }

    // 验证手机验证码 下一步
    private void updateAdminPass() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("oldpass", MD5Util.getMD5String(et_old.getText().toString()));
        map.put("newpass", MD5Util.getMD5String(et_new.getText().toString()));
        map.put("confirm_pass", MD5Util.getMD5String(et_new.getText().toString()));
        map.put("sms_code", sms_code);
        mQueue.add(ParamTools.packParam(Constants.updateAdminPass, this, this, map));
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
                if (url.contains(Constants.updateAdminPass)) {
                    Tools.showToast(this, "修改成功");
                    finish();
                }
            } else if (status == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(ModifyPasswordActivity2.this, LoginActivity.class, false);
                Tools.showToast(ModifyPasswordActivity2.this, "登录过期请重新登录");
                Tools.acts.clear();
            } else {
                Tools.showToast(this, msg);
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
        if (et_old.getText().length() >= 1 && et_new.getText().length() >= 1 && et_new_to.getText().length() >= 1) {
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
