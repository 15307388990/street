package com.juxun.business.street.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.ParseModel;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

public class CancelOrderActivity extends BaseActivity implements
		OnClickListener {

	@ViewInject(R.id.lv_list)
	private ListView lv_list;
	private View headView, footview;
	private AfterReasonAdatper mAdapter;
	private Button btn_next;
	private LinearLayout ll_why;// 其他原因
	private ImageView iv_why_img;// 其他原因的勾选
	private EditText et_why;// 其他原因的文本
	private List<String> sTringlist=new ArrayList<String>();
	private String order_id;
	private String whystring;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list); // 与【采购订单】取消订单布局一致
		ViewUtils.inject(this);
		Tools.acts.add(this);
		initTitle();
		title.setText("取消订单");
		initView();
	}

	public void initView() {
		// 需要取消的订单id
		order_id = getIntent().getStringExtra("id");
		// 列表头部、底部
		headView = LayoutInflater.from(this).inflate(
				R.layout.after_reason_headview, null);
		footview = LayoutInflater.from(this).inflate(
				R.layout.cancel_order_footview, null);
		btn_next = (Button) footview.findViewById(R.id.btn_next);
		ll_why = (LinearLayout) footview.findViewById(R.id.ll_why);
		iv_why_img = (ImageView) footview.findViewById(R.id.iv_img);
		et_why = (EditText) footview.findViewById(R.id.et_why);
		// 列表
		lv_list.addHeaderView(headView);
		lv_list.addFooterView(footview);
		mAdapter = new AfterReasonAdatper(this);
		lv_list.setAdapter(mAdapter);

		/*
		 * 点击事件
		 */
		ll_why.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				iv_why_img.setVisibility(View.VISIBLE);
				et_why.setVisibility(View.VISIBLE);
				mAdapter.ChooseId(-1);
				btn_next.setEnabled(initBtn(-1));
			}
		});
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closeOrder();
			}
		});
		et_why.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				btn_next.setEnabled(initBtn(-1));
			}
		});
		getRefundRemarkList();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	// 获取售后退款原因列表
	private void getRefundRemarkList() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("auth_token", partnerBean.getAuth_token());
		mQueue.add(ParamTools.packParam(Constants.rejectOrdersInfo, this,
				this, map));
	}

	// 取消订单
	private void closeOrder() {
		// order_id:采购订单主键id,remark 取消原因,agency_id, auth_token
		Map<String, String> map = new HashMap<String, String>();
		map.put("auth_token", partnerBean.getAuth_token());
		map.put("reject_info", whystring);
		map.put("order_id", order_id);
		mQueue.add(ParamTools.packParam(Constants.orRejectOrder, this, this,
				map));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_back:
			CancelOrderActivity.this.finish();
			break;
		}
	}

	private boolean initBtn(int chooseid) {
		if (chooseid > -1) {
			btn_next.setBackgroundResource(R.drawable.button_bg1);
			btn_next.setTextColor(getResources().getColor(R.color.white));
			whystring = sTringlist.get(chooseid);
			return true;
		} else {
			if (et_why.getText().length() > 0) {
				btn_next.setBackgroundResource(R.drawable.button_bg1);
				btn_next.setTextColor(getResources().getColor(R.color.white));
				whystring = et_why.getText().toString();
				return true;
			} else {
				btn_next.setBackgroundResource(R.drawable.button_bg);
				btn_next.setTextColor(getResources()
						.getColor(R.color.jiujiujiu));
				return false;
			}
		}
	}

	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.optInt("status");
			String resultMsg = json.optString("msg");
			if (stauts == 0) {
				if (url.contains(Constants.rejectOrdersInfo)) {
					JSONArray resultJson = json.optJSONArray("result");
					for (int i = 0; i < resultJson.length(); i++) {
						if (resultJson.getJSONObject(i).optString("type_text") != null) {
							sTringlist.add(resultJson.getJSONObject(i).optString("type_text"));
						}
						mAdapter.UpdateTheData(sTringlist);
					}
				} else if (url.contains(Constants.orRejectOrder)) {
					Tools.showToast(CancelOrderActivity.this, "订单取消成功");
					ParseModel.isdate = true;
					finish();
				}
			} else {
				Tools.showToast(this, resultMsg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
		}
	}

	class AfterReasonAdatper extends BaseAdapter {

		private Context mContext;
		private int chooseid = -1;
		private List<String> sTringlist;

		public AfterReasonAdatper(Context context) {
			sTringlist = new ArrayList<String>();
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return sTringlist.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		// 更新数据
		public void UpdateTheData(List<String> list) {
			this.sTringlist = list;
			notifyDataSetChanged();
		}

		public void ChooseId(int chooseid) {
			this.chooseid = chooseid;
			notifyDataSetChanged();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.after_reason_itm, null);
			TextView tv_why_name = (TextView) convertView
					.findViewById(R.id.tv_why_name);
			ImageView iv_img = (ImageView) convertView
					.findViewById(R.id.iv_img);
			tv_why_name.setText(sTringlist.get(position));
			if (chooseid == position) {
				iv_img.setVisibility(View.VISIBLE);
			} else {
				iv_img.setVisibility(View.GONE);
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ChooseId(position);
					iv_why_img.setVisibility(View.GONE);
					et_why.setVisibility(View.GONE);
					btn_next.setEnabled(initBtn(position));
				}
			});
			return convertView;
		}
	}

}
