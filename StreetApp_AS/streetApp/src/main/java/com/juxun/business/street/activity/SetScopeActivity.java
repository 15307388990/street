/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 设置营业范围
 */
public class SetScopeActivity extends BaseActivity {
    @ViewInject(R.id.et_one)
    private EditText et_one;// 配送范围

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_scope);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("配送范围");
        more.setText("保存");
        more.setVisibility(View.VISIBLE);
        more.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (et_one.getText().length() > 0) {
                    if (Double.valueOf(et_one.getText().toString()) > 0) {
                        updateBusinessRange();
                    } else {
                        Tools.showToast(SetScopeActivity.this, "营业范围需要大于0");
                    }

                } else {
                    Tools.showToast(SetScopeActivity.this, "请输入配送范围");
                }

            }
        });
        initView();
    }

    private void initView() {
        String range = getIntent().getStringExtra("range");
        if (range != null) {
            et_one.setText(range);
        }
    }

    // 修改营业范围
    private void updateBusinessRange() {
        Map<String, String> map = new HashMap<>();
        map.put("range", et_one.getText().toString());
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.updateBusinessRange, this, this, map));
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
            int stauts = json.optInt("status");
            if (stauts == 0) {
                if (url.contains(Constants.updateBusinessRange)) {
                    Intent intent = new Intent();
                    intent.putExtra("range", et_one.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
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
