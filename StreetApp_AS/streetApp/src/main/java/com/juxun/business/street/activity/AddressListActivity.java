package com.juxun.business.street.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.AddressInfoAdapter;
import com.juxun.business.street.bean.AddressInfoModel;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author AddressListActivity 地址管理
 */

public class AddressListActivity extends BaseActivity implements AddressInfoAdapter.DeleteCallBack,
        AddressInfoAdapter.EditCallBack, AddressInfoAdapter.SetDefaultAddressCallBack {
    @ViewInject(R.id.ll_bottom)
    private LinearLayout ll_bottom; // 新增收货地址
    @ViewInject(R.id.address_list_view)
    private ListView address_list_view; // 收货地址列表
    @ViewInject(R.id.button_back)
    private ImageView button_back; // 收货地址列表
    private AddressInfoAdapter mAddressInfoAapter;
    private List<AddressInfoModel> mList;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_manager);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        type = getIntent().getIntExtra("type", 0);
        mList = new ArrayList<>();
        mAddressInfoAapter = new AddressInfoAdapter(this, mList, this, this, this);
        address_list_view.setAdapter(mAddressInfoAapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findAddressList();
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.button_back, R.id.ll_bottom})
    public void clickMethod(View v) {
        if (v.getId() == R.id.button_back) {
            finish();
        } else if (v.getId() == R.id.ll_bottom) {
            Intent intent = new Intent(AddressListActivity.this, AddAddressActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 地址列表获取
     */
    private void findAddressList() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.findAddressList, this, this, map));
    }

    /**
     * 设置默认地址
     */
    private void defaultAddressSet(int id) {
        Map<String, String> map = new HashMap<>();
        map.put("address_id", id + "");
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.defaultAddressSet, this, this, map));
    }

    /**
     * 删除地址
     */
    private void delAddress(int id) {
        Map<String, String> map = new HashMap<>();
        map.put("address_id", id + "");
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.delAddress, this, this, map));
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.optInt("status");
            String msg = json.optString("msg");
            if (status == 0) {
                if (url.contains(Constants.findAddressList)) {
                    String liString = json.getString("result");
                    mList = JSON.parseArray(liString, AddressInfoModel.class);
                    mAddressInfoAapter.updateListView(mList);
                } else if (url.contains(Constants.delAddress)) {
                    Tools.showToast(AddressListActivity.this, "删除地址成功");
                    findAddressList();
                } else if (url.contains(Constants.defaultAddressSet)) {
                    Tools.showToast(AddressListActivity.this, "设置成功");
                    findAddressList();
                    if (type == 1) {
                        finish();
                    }

                }
            } else if (status == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(this, LoginActivity.class, true);
                Tools.showToast(this, "登录过期请重新登录");
                Tools.exit();
            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    @Override
    public void onEdit(AddressInfoModel model) {
        Intent intent = new Intent();
        intent.putExtra("AddressInfo", model);
        intent.putExtra("type",1);
        intent.setClass(this, AddAddressActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDelete(final AddressInfoModel model) {
        AlertDialog.Builder builder = new Builder(this);
        builder.setMessage("你确认要删除该地址吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delAddress(model.getId());
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
        builder.create().show();
    }

    @Override
    public void onSetDefAddress(AddressInfoModel model) {
        defaultAddressSet(model.getId());
    }
}
