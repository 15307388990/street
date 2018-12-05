package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.allenliu.versionchecklib.core.AllenChecker;
import com.allenliu.versionchecklib.core.VersionParams;
import com.allenliu.versionchecklib.core.http.HttpParams;
import com.allenliu.versionchecklib.core.http.HttpRequestMethod;
import com.juxun.business.street.bean.PartnerBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.service.VersionService;
import com.juxun.business.street.util.MD5Util;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.SavePreferencesData;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.juxun.business.street.config.Constants.mallSetInfo;

public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.et_login_account)
    private EditText etLoginAccount; // 用户名
    @ViewInject(R.id.et_login_password)
    private EditText et_login_password; // 密码
    @ViewInject(R.id.login)
    private Button login; // 登陆
    @ViewInject(R.id.hotLine)
    private TextView hotLine; // 注册
    @ViewInject(R.id.apply)
    private TextView apply; // 申请入驻
    @ViewInject(R.id.tv_forgot)
    private TextView tv_forgot;//忘记密码
    @ViewInject(R.id.tv_registered)
    private TextView tv_registered;
    private String name, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
        initTitle();
        back.setVisibility(View.GONE);
        title.setText("e蜂社 商家版");
        more.setText(R.string.apply);
        mSavePreferencesData = new SavePreferencesData(this);
        String json = mSavePreferencesData.getStringData("json");
        if (!TextUtils.isEmpty(json)) {
            // 开启了手势密码
            if (mSavePreferencesData.getBooleanData("isGesturesPassword")) {
                Tools.jump(this, GesturesLoginActivity.class, true);
            } else {
                Tools.jump(this, MainActivity.class, true);
            }
        }

        et_login_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_login_password.getText().length() >= 1) {
                    login.setBackgroundResource(R.drawable.button_bg1);
                    login.setTextColor(getResources().getColor(android.R.color.white));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //解绑
        posUnReg();
        updataApp();
    }

    /**
     * 检测版本
     **/
    private void updataApp() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("device_type", "0");//设备类型 0为Android 1为ios
        // httpParams.put("auth_token", partnerBean.getAuth_token());
        httpParams.put("app_type", "1");//1商户版2街边go3俊鹏
        VersionParams.Builder builder = new VersionParams.Builder().setRequestMethod(HttpRequestMethod.POST).setRequestParams(httpParams)
                .setRequestUrl(Constants.mainUrl + Constants.updateAPP)
                .setCustomDownloadActivityClass(CustomDialogActivity.class)
                .setService(VersionService.class);
        AllenChecker.startVersionCheck(this, builder.build());


    }

    /**
     * 单击事件
     */
    @OnClick({R.id.hotLine, R.id.login, R.id.right_view_text, R.id.apply, R.id.tv_registered, R.id.tv_forgot})
    public void clickMethod(View v) {
        if (v.getId() == R.id.login) {
            if (checkParams()) {
                loading();
                toLogin(name, pwd);
            }
        } else if (v.getId() == R.id.right_view_text) {
            Intent intent = new Intent(this, ApplyActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.hotLine) {
            // Tools.jump(this, HotLineDialogActivity.class, false);
        } else if (v.getId() == R.id.apply) {
            Intent intent = new Intent(this, ApplyActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tv_registered) {
            Intent intent = new Intent(this, RegisteredOneActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tv_forgot) {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            intent.putExtra("Account", etLoginAccount.getText().toString());
            startActivity(intent);
        }
    }

    /* 执行登录操作 */
    public void toLogin(String name, String pwd) {
        Map<String, String> map = new HashMap<>();
        map.put("admin_account", name);
        String password = MD5Util.getMD5String(pwd);
        map.put("admin_pass", password);
        mQueue.add(ParamTools.packParam(Constants.userLogin, this, this, map));
        loading();
    }

    /* 解绑推送服务 */
    private void posUnReg() {
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        // 用QID 绑定别名
        pushService.unbindAccount(new CommonCallback() {

            @Override
            public void onSuccess(String arg0) {
                Log.i("bind", "解绑成功" + arg0);
            }

            @Override
            public void onFailed(String arg0, String arg1) {
                Log.i("bind", "解绑失败" + arg0 + "---" + arg1);
            }
        });
    }

    /**
     * 检测参数合法性
     */
    public boolean checkParams() {
        name = etLoginAccount.getText().toString();
        pwd = et_login_password.getText().toString();
        if (name == null || pwd == null || name.length() == 0 || pwd.length() == 0) {
            Tools.showToast(this, R.string.tips_login_param_error);
            return false;
        }
        return true;
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            String msg = json.optString("msg");
            if (stauts == 0) {
                if (url.contains(Constants.userLogin)) {
                    MobclickAgent.onProfileSignIn(Tools.getDeviceBrand(), name);    //友盟统计
                    mSavePreferencesData.putStringData("auth_token", json.optString("auth_token"));
                    Tools.jump(LoginActivity.this, MainActivity.class, true);
                }
            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, "数据解析错误");
        }
    }

}
