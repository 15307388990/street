package com.juxun.business.street.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.RequestAfterSaleAdapter;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.fragment.RequestAfterSaleBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

public class RequestAfterSaleActivity extends BaseActivity {
	/**
	 * 申请售后
	 */
	@ViewInject(R.id.button_back)
	private ImageView button_back;
	@ViewInject(R.id.lv_listview)
	private ListView mListview;

	private List<RequestAfterSaleBean> mAfterSaleList;
	private RequestAfterSaleAdapter mRequestAdapter;
	private View mFootView; // 尾部总金额，按钮
	private String mOrder_id; // 订单号
	private int mPay_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_request_aftersale);
		ViewUtils.inject(this);
		Tools.acts.add(this);
		getDatas();
		initView();
	}

	private void getDatas() {
		mOrder_id = getIntent().getStringExtra("order_id");
		mPay_type = getIntent().getIntExtra("pay_type",1);
		mAfterSaleList = new ArrayList<RequestAfterSaleBean>();

		Map<String, String> map = new HashMap<String, String>();
		// agency_id auth_token
		// order_id 主订单情况
		map.put("auth_token", partnerBean.getAuth_token());
		map.put("order_id", mOrder_id);
		mQueue.add(ParamTools.packParam(Constants.requestAfterSale, this, this,
				map));
	}

	private void initView() {
		mFootView = LayoutInflater.from(this).inflate(
				R.layout.request_footview, null);
		mRequestAdapter = new RequestAfterSaleAdapter(this, mAfterSaleList,
				mFootView, mOrder_id, mPay_type);
		mListview.addFooterView(mFootView);
		mListview.setAdapter(mRequestAdapter);
	}

	/*
	 * 最外层的点击事件处理
	 */
	@OnClick({ R.id.button_back })
	public void clickMethod(View v) {
		switch (v.getId()) {
		case R.id.button_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				setResult(RESULT_OK);
				finish();
			}
		}
	}

	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		// response:resultCode,resultMsg,resultJson
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.optInt("stauts");
			String msg = json.optString("msg");
			if (stauts == 0) {
				if (url.contains(Constants.requestAfterSale)) {
					String liString = json.optString("result");
					mAfterSaleList = JSON.parseArray(liString,
							RequestAfterSaleBean.class);

					// 请求到数据，刷新列表
					mRequestAdapter.updateListView(mAfterSaleList, mOrder_id,
							mPay_type);
				}
			} else if (stauts == -4004) {
				mSavePreferencesData.putStringData("json", "");
				Tools.jump(this, LoginActivity.class, true);
				Toast.makeText(this, "登录过期请重新登录", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, R.string.tips_unkown_error, Toast.LENGTH_SHORT)
					.show();
		}
	}
}
