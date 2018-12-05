package com.juxun.business.street.activity;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.AftersaleHistoryAdapter;
import com.juxun.business.street.adapter.AftersaleHistoryAdapter.onCancelAfter;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.PurchaseOderBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

/**
 * AfterApplyForActivity 单个订单的申请记录
 */
public class AfterApplyForActivity extends BaseActivity implements OnClickListener, onCancelAfter {
	@ViewInject(R.id.lv_list)
	private ListView lv_list;
	private List<PurchaseOderBean> mAfterSaleList;
	private AftersaleHistoryAdapter mRequestAdapter;
	private String order_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		ViewUtils.inject(this);
		Tools.acts.add(this);
		initTitle();
		title.setText("申请记录");
		initView();
	}

	public void initView() {
		order_id = getIntent().getStringExtra("order_id");
		mRequestAdapter = new AftersaleHistoryAdapter(this, this);
		lv_list.setAdapter(mRequestAdapter);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getApplyRecordList();
	}

	/**
	 * 获取单个订单的申请记录
	 */
	private void getApplyRecordList() {
		Map<String, String> map = new HashMap<String, String>();
		// agency_id
		// auth_token
		// pageNumber
		map.put("auth_token", partnerBean.getAuth_token());
		map.put("order_id", order_id);
		mQueue.add(ParamTools.packParam(Constants.getOrderApplyRecordList, this, this, map));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_back:
			AfterApplyForActivity.this.finish();
			break;
		}
	}

	@Override
	public void onResponse(String response, String url) {
		// TODO Auto-generated method stub
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.optInt("resultCode");
			String resultMsg = json.optString("resultMsg");
			if (stauts == 0) {
				if (url.contains(Constants.getOrderApplyRecordList)) {
					String liString = json.getString("resultJson");
					mAfterSaleList = JSON.parseArray(liString, PurchaseOderBean.class);
					mRequestAdapter.updateListView(mAfterSaleList);
				} else if (url.contains(Constants.closeCustomerServiceApply)) {
					getApplyRecordList();
				}
			} else {
				Tools.showToast(this, resultMsg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
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
	public void onCancel(int order_id) {
		// TODO Auto-generated method stub
		// 取消售后
		Map<String, String> map = new HashMap<String, String>();
		// agency_id
		// auth_token
		// order_id
		map.put("auth_token", partnerBean.getAuth_token());
		map.put("order_id", order_id + "");
		mQueue.add(ParamTools.packParam(Constants.closeCustomerServiceApply, this, this, map));
	}
}