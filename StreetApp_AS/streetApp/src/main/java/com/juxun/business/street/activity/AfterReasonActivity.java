package com.juxun.business.street.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import android.content.Context;
import android.content.Intent;
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

/**
 * AfterReasonActivity 申请售后理由 2017/8/9 罗明
 */
public class AfterReasonActivity extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.lv_list)
	private ListView lv_list;
	private View headView, footview;
	private AfterReasonAdatper mAdapter;
	private Button btn_next;
	private LinearLayout ll_why;// 其他原因
	private ImageView iv_why_img;// 其他原因的勾选
	private EditText et_why;// 其他原因的文本
	private List<String> sTringlist;
	private String commodity_json, order_id;
	private String whystring;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		ViewUtils.inject(this);
		Tools.acts.add(this);
		initTitle();
		title.setText("申请售后理由");
		initView();
	}

	public void initView() {
		commodity_json = getIntent().getStringExtra("commodity_json");
		order_id = getIntent().getStringExtra("order_id");
		mAdapter = new AfterReasonAdatper(this);
		lv_list.setAdapter(mAdapter);
		headView = LayoutInflater.from(this).inflate(R.layout.after_reason_headview, null);
		footview = LayoutInflater.from(this).inflate(R.layout.after_reason_footview, null);
		btn_next = (Button) footview.findViewById(R.id.btn_next);
		ll_why = (LinearLayout) footview.findViewById(R.id.ll_why);
		iv_why_img = (ImageView) footview.findViewById(R.id.iv_img);
		et_why = (EditText) footview.findViewById(R.id.et_why);
		lv_list.addHeaderView(headView);
		lv_list.addFooterView(footview);
		ll_why.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_why_img.setVisibility(View.VISIBLE);
				et_why.setVisibility(View.VISIBLE);
				mAdapter.ChooseId(-1);
				btn_next.setEnabled(initBtn(-1));

			}
		});
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				customerServiceApply();
			}
		});
		et_why.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				btn_next.setEnabled(initBtn(-1));
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getRefundRemarkList();
	}

	// 获取售后退款原因列表
	private void getRefundRemarkList() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("auth_token", partnerBean.getAuth_token());
		mQueue.add(ParamTools.packParam(Constants.getRefundRemarkList, this, this, map));
	}

	// 提交售后
	private void customerServiceApply() {
		// agency_id
		// auth_token
		// refund_remark 退款原因
		// commodity_json 退款商品json[{id:1,refund_commodity_num:1}]
		// order_num 订单号
		// order_id订单主键id
		Map<String, String> map = new HashMap<String, String>();
		map.put("auth_token", partnerBean.getAuth_token());
		map.put("reason", whystring);
		map.put("commodity_json", commodity_json);
		map.put("order_id", order_id);
		mQueue.add(ParamTools.packParam(Constants.customerServiceApply, this, this, map));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_back:
			AfterReasonActivity.this.finish();
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
				btn_next.setTextColor(getResources().getColor(R.color.jiujiujiu));
				return false;
			}
		}

	}

	@Override
	public void onResponse(String response, String url) {
		// TODO Auto-generated method stub
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.optInt("status");
			String resultMsg = json.optString("msg");
			if (stauts == 0) {
				if (url.contains(Constants.getRefundRemarkList)) {
					JSONArray jsonArray=json.optJSONArray("result");
					sTringlist =new ArrayList<String>();
					for (int i = 0; i <jsonArray.length() ; i++) {
						sTringlist.add(jsonArray.getJSONObject(i).optString("type_text"));
					}
					mAdapter.UpdateTheData(sTringlist);
				} else if (url.contains(Constants.customerServiceApply)) {
					Intent intent = new Intent(AfterReasonActivity.this, RegisteredSuccessfullyActivity.class);
					intent.putExtra("type", 4);
					startActivityForResult(intent, 1);
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
			// TODO Auto-generated method stub
			return sTringlist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		// 更新数据
		public void UpdateTheData(List<String> list) {
			// TODO Auto-generated method stub
			this.sTringlist = list;
			notifyDataSetChanged();

		}

		public void ChooseId(int chooseid) {
			// TODO Auto-generated method stub
			this.chooseid = chooseid;
			notifyDataSetChanged();

		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = LayoutInflater.from(mContext).inflate(R.layout.after_reason_itm, null);
			TextView tv_why_name = (TextView) convertView.findViewById(R.id.tv_why_name);
			ImageView iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
			tv_why_name.setText(sTringlist.get(position));
			if (chooseid == position) {
				iv_img.setVisibility(View.VISIBLE);
			} else {
				iv_img.setVisibility(View.GONE);
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
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