package com.juxun.business.street.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.juxun.business.street.bean.AddressInfoModel;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.WheelCityDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author AddAddress 新增地址
 */
public class AddAddressActivity extends BaseActivity implements WheelCityDialog.onConfirmListener {
    @ViewInject(R.id.receiver_name)
    private EditText receiver_name; // 收件人
    @ViewInject(R.id.receiver_phone)
    private EditText receiver_phone; // 手机号
    @ViewInject(R.id.receiver_address_detail)
    private EditText receiver_address_detail; // 详细地址
    @ViewInject(R.id.tv_address_area)
    private TextView tv_address_area; // 地区
    @ViewInject(R.id.ll_address)
    private LinearLayout ll_address; // 地区
    @ViewInject(R.id.button_back)
    private ImageView button_back; // 地区
    @ViewInject(R.id.btn_save)
    private Button btn_save; // 保存
    private int defaultId = 1;// 地址ID
    private AddressInfoModel model = null;
    private int isUpdate = 0;
    private int addressId;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address);
        ViewUtils.inject(this);
        initView();
        model = (AddressInfoModel) getIntent().getSerializableExtra("AddressInfo");
        type = getIntent().getIntExtra("type", 0);
        if (model != null) {
            receiver_name.setText(model.getUser_name());
            receiver_phone.setText(model.getTel());
            tv_address_area.setText(model.getArea_name());
            receiver_address_detail.setText(model.getAddress());
            defaultId = model.getDefault_address_id();
            addressId = model.getId();
        }
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.button_back, R.id.btn_save, R.id.ll_address})
    public void clickMethod(View v) {
        if (v.getId() == R.id.button_back) {
            finish();
        } else if (v.getId() == R.id.btn_save) {
            addOrUpdateAddress();
        } else if (v.getId() == R.id.ll_address) {
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            boolean isOpen = imm.isActive();
            if (isOpen) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            WheelCityDialog myDialog = new WheelCityDialog(this, AddAddressActivity.this);
            myDialog.showAtLocation(ll_address, Gravity.BOTTOM, 0, 0);
        }
    }

    private void initView() {
        receiver_name.addTextChangedListener(new textWatcher());
        receiver_phone.addTextChangedListener(new textWatcher());
        receiver_address_detail.addTextChangedListener(new textWatcher());
        btn_save.setClickable(false);
    }


    private void addOrUpdateAddress() {
        if (receiver_name.getText().length() == 0 || receiver_phone.getText().length() == 0
                || receiver_address_detail.getText().length() == 0 || tv_address_area.getText().length() == 0) {
            Toast.makeText(AddAddressActivity.this, "信息不完整", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("user_name", receiver_name.getText().toString());
        map.put("tel", receiver_phone.getText().toString());
        map.put("address", receiver_address_detail.getText().toString());
        map.put("area_name", tv_address_area.getText().toString());
        map.put("default_address_id", defaultId + "");
        if (type == 1) {    //type是1的时候是修改地址，0的时候只是添加地址
            map.put("id", addressId + "");
            mQueue.add(ParamTools.packParam(Constants.updateAddress, this, this, map));
        } else {
            mQueue.add(ParamTools.packParam(Constants.addAddress, this, this, map));
        }
        loading();
    }

    @Override
    public void onConfirm(String area, String areaName, String id) {
        tv_address_area.setText(area);
        btn_save.setClickable(initBtn());
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.optInt("status");
            String msg = json.optString("msg");
            if (status == 0) {
                if (url.contains(Constants.addAddress)) {
                    Tools.showToast(this, "新增成功");
                    finish();
                } else if (url.contains(Constants.updateAddress)) {
                    Tools.showToast(this, "修改成功");
                    finish();
                }
            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    class textWatcher implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            btn_save.setClickable(initBtn());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private boolean initBtn() {
        if (receiver_name.getText().length() > 0 && receiver_address_detail.getText().length() > 0
                && receiver_phone.getText().length() > 0 && tv_address_area.getText().length() > 0) {
            btn_save.setBackgroundResource(R.drawable.button_bg1);
            btn_save.setTextColor(getResources().getColor(R.color.white));
            return true;
        } else {
            btn_save.setBackgroundResource(R.drawable.button_bg);
            btn_save.setTextColor(getResources().getColor(R.color.gray));
            return false;
        }
    }

}
