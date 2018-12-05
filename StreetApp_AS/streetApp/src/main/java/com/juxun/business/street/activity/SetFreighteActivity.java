/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.juxun.business.street.bean.PartnerBean;
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
 * @version 设置运费
 */
public class SetFreighteActivity extends BaseActivity {
    @ViewInject(R.id.cb_swith_one)
    private CheckBox cb_swith_one;
    @ViewInject(R.id.cb_swith_two)
    private CheckBox cb_swith_two;
    @ViewInject(R.id.ll_content)
    private LinearLayout ll_content;
    @ViewInject(R.id.ll_two)
    private LinearLayout ll_two;
    @ViewInject(R.id.et_one)
    private EditText et_one; // 运费
    @ViewInject(R.id.et_two)
    private EditText et_two;// 满减
    @ViewInject(R.id.v_1)
    private View v_1;
    @ViewInject(R.id.v_2)
    private View v_2;
    @ViewInject(R.id.v_3)
    private View v_3;
    private PartnerBean mPartnerBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_freight);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("运费设置");
        more.setText("保存");
        more.setVisibility(View.VISIBLE);
        more.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (et_one.getText().length() > 0 && et_two.getText().length() > 0) {
                    updateDeliveryPrice();
                } else {
                    Tools.showToast(SetFreighteActivity.this, "不能输入空");
                }

            }
        });
        initView();
    }

    private void initView() {
        mPartnerBean = (PartnerBean) getIntent().getSerializableExtra("mPartnerBean");

        cb_swith_one.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll_content.setVisibility(View.VISIBLE);
                    v_2.setVisibility(View.VISIBLE);
                    Tools.showToast(SetFreighteActivity.this, "有运费");
                } else {
                    v_2.setVisibility(View.GONE);
                    ll_content.setVisibility(View.GONE);
                    Tools.showToast(SetFreighteActivity.this, "无运费");
                }

            }
        });
        cb_swith_two.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll_two.setVisibility(View.VISIBLE);
                    v_1.setVisibility(View.VISIBLE);
                    v_3.setVisibility(View.VISIBLE);
                    Tools.showToast(SetFreighteActivity.this, "有满免");
                } else {
                    ll_two.setVisibility(View.GONE);
                    v_1.setVisibility(View.GONE);
                    v_3.setVisibility(View.GONE);
                    Tools.showToast(SetFreighteActivity.this, "无满免");
                }

            }
        });
        // 有运费
        if (mPartnerBean != null) {
            int delivery_price = mPartnerBean.getDelivery_price();
            int delivery_full_free = mPartnerBean.getDelivery_full_free();
            if (delivery_price > 0) {
                cb_swith_one.setChecked(true);
                if (delivery_full_free > 0) {
                    et_one.setText(Tools.getFenYuan(delivery_price) + "");
                    cb_swith_two.setChecked(true);
                    et_two.setText(Tools.getFenYuan(delivery_full_free) + "");
                } else {
                    et_one.setText(Tools.getFenYuan(delivery_price) + "");
                    cb_swith_two.setChecked(false);
                }
            } else {
                cb_swith_one.setChecked(false);
            }
        }
    }

    // 修改运费
    private void updateDeliveryPrice() {
        Map<String, String> map = new HashMap<>();

        String one=et_one.getText().toString();
        String two=et_two.getText().toString();
        if (cb_swith_one.isChecked()) {
            if (!one.equals(""))
            {
                map.put("delivery_price", Tools.getYuanFen(one)+"");
            }

            if (cb_swith_two.isChecked()&&!two.equals("")) {
                map.put("max_total_price",  Tools.getYuanFen(two)+"");
            } else {
                map.put("max_total_price", "0");
            }
        } else {
            map.put("delivery_price", "0");
            map.put("max_total_price", "0");
        }
        map.put("auth_token",mSavePreferencesData.getStringData("auth_token"));
        mQueue.add(ParamTools.packParam(Constants.updateDeliveryPrice, this, this, map));
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
                if (url.contains(Constants.updateDeliveryPrice)) {
                    if (cb_swith_one.isChecked()) {
                        try {
                            // 传递 回去的时候记得转换成元
                            mPartnerBean.setDelivery_price((int) (Double.valueOf(et_one.getText().toString()) * 100));
                            if (cb_swith_two.isChecked()) {
                                mPartnerBean.setDelivery_full_free((int) (Double.valueOf(et_two.getText().toString()) * 100));
                            } else {
                                mPartnerBean.setDelivery_full_free(0);
                            }
                        } catch (Exception e) {
                        }
                    } else {
                        mPartnerBean.setDelivery_price(0);
                        mPartnerBean.setDelivery_full_free(0);
                    }
                    Intent intent = new Intent();
                    intent.putExtra("mPartnerBean", mPartnerBean);
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
