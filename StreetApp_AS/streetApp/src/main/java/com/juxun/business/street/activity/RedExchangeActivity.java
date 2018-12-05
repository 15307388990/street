package com.juxun.business.street.activity;

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
 * @author 红包兑换
 */
public class RedExchangeActivity extends BaseActivity {

    @ViewInject(R.id.et_ma)
    private EditText et_ma;
    @ViewInject(R.id.btn_exchange)
    private Button btn_exchange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_red_exchange);
        ViewUtils.inject(this);
        initContentView();
        initTitle();
        title.setText("兑换");
    }

    /**
     * 红包兑换
     */
    private void drawRedPacketWithSN() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("sncode", et_ma.getText().toString());
        mQueue.add(ParamTools.packParam(Constants.drawRedPacketWithSN, this, this, map));
        loading();
    }

    private void initContentView() {
        et_ma = (EditText) findViewById(R.id.et_ma);
        btn_exchange = (Button) findViewById(R.id.btn_exchange);
        btn_exchange.setEnabled(false);
        et_ma.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (et_ma.getText().length() >= 1) {
                    btn_exchange.setBackgroundResource(R.drawable.button_bg1);
                    btn_exchange.setTextColor(getResources().getColor(android.R.color.white));
                    btn_exchange.setEnabled(true);
                } else {
                    btn_exchange.setBackgroundResource(R.drawable.button_bg);
                    btn_exchange.setTextColor(getResources().getColor(R.color.gray));
                    btn_exchange.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
        btn_exchange.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                drawRedPacketWithSN();
            }
        });

    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int resultCode = json.getInt("status");
            String msg = json.getString("msg");
            if (resultCode == 0) {
                if (url.contains(Constants.drawRedPacketWithSN)) {
                    Tools.showToast(getApplicationContext(), "兑换成功");
                    setResult(RESULT_OK);
                    finish();
                }
            } else if (resultCode == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(this, LoginActivity.class, true);
                Tools.showToast(this, "登录过期，重新登录");
            } else {
                Tools.showToast(getApplicationContext(), msg);
            }
        } catch (JSONException e) {
            Tools.showToast(getApplicationContext(), "解析数据错误");
        }
    }

}
