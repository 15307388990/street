/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.ClearEditText;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 设置配送电话
 */
public class SetDeliveryPhoneActivity extends BaseActivity {
    @ViewInject(R.id.ed_clear)
    private ClearEditText ed_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_delivery_phone);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("配送电话");
        more.setText("保存");
        more.setVisibility(View.VISIBLE);
        more.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ed_clear.getText().length() > 1) {
                    updateDeliveryPhone();
                } else {
                    Tools.showToast(SetDeliveryPhoneActivity.this, "请输入配送电话");
                }

            }
        });
        initView();
    }

    private void initView() {
        String phoneString = getIntent().getStringExtra("phone");
        if (phoneString != null) {
            ed_clear.setText(getIntent().getStringExtra("phone"));
        }

    }

    // 修改配送电话
    private void updateDeliveryPhone() {
        Map<String, String> map = new HashMap<>();
        map.put("new_phone", ed_clear.getText().toString());
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.updateDeliveryPhone, this, this, map));
        loading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // obtainData();
    }

    /**
     * 单击事件
     */
    @OnClick({})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.exit:
                Intent intent1 = new Intent(this, LoginOutDialogActivity.class);
                startActivityForResult(intent1, 100);
                break;
        }
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
            int status = json.optInt("status");
            if (status == 0) {
                if (url.contains(Constants.updateDeliveryPhone)) {
                    Intent intent = new Intent();
                    intent.putExtra("phone", ed_clear.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } else if (status < 0) {
                Tools.dealErrorMsg(this, url, status, json.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

}
