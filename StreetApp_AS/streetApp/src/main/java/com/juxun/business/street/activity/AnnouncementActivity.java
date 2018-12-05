package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.juxun.business.street.config.Constants;
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
 * @author 店铺公告
 */
public class AnnouncementActivity extends BaseActivity {
    @ViewInject(R.id.et_feedback_name)
    private EditText et_feedback_name;
    @ViewInject(R.id.btn_next)
    private Button btn_next;
    private String shopNotice = "";// 店铺公告

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        ViewUtils.inject(this);
        initTitle();
        shopNotice = getIntent().getStringExtra("shopNotice");
        et_feedback_name.setText(shopNotice);
        title.setText("店铺公告");
        btn_next.setEnabled(initBtn());
        et_feedback_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_next.setEnabled(initBtn());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sendFeedBack();
            }
        });
    }

    private boolean initBtn() {
        if (et_feedback_name.getText().length() > 0) {
            btn_next.setBackgroundResource(R.drawable.button_bg1);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            return true;
        } else {
            btn_next.setBackgroundResource(R.drawable.button_bg);
            btn_next.setTextColor(getResources().getColor(R.color.jiujiujiu));
            return false;
        }
    }

    private void sendFeedBack() {
        if (et_feedback_name.getText().length() > 140) {
            Tools.showToast(this, "输入超过限制");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("notice", et_feedback_name.getText().toString());// 内容
        mQueue.add(ParamTools.packParam(Constants.setMallNotice, this, this, map));
        loading();
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int resultCode = json.getInt("status");
            String msg = json.getString("msg");

            if (resultCode == 0) {
                if (url.contains(Constants.setMallNotice)) {
                    Tools.showToast(this, "提交成功！");
                    Intent intent = new Intent();
                    intent.putExtra("shopNotice", et_feedback_name.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } else if (resultCode == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(this, LoginActivity.class, true);
                Tools.showToast(this, "登录过期请重新登录");
            } else {
                Tools.showToast(getApplicationContext(), msg);
            }
        } catch (JSONException e) {
            Tools.showToast(getApplicationContext(), "解析数据错误");
        }
    }
}
