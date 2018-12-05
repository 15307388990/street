/**
 * 
 */
package com.juxun.business.street.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.RecommendedsBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * 
 * @version 邀请人数
 * 
 */
public class TheInvitationActivity extends BaseActivity {

	@ViewInject(R.id.lv_list)
	private ListView lv_list; // 列表
	@ViewInject(R.id.tv_text)
	private TextView tv_text;

	private String number;// 人数
	private List<RecommendedsBean> mRecommendeds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_the_invitation);
		ViewUtils.inject(this);
		initTitle();
		number = getIntent().getStringExtra("registerMemberCount");
		title.setText("已邀请人数(" + number + ")");
	}

	@Override
	protected void onResume() {
		super.onResume();
		getRecommendedMember();
	}

	/* 获取推荐用户列表 */
	private void getRecommendedMember() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("auth_token", partnerBean.getAuth_token());
		map.put("pageNumber", "1");
		map.put("pageSize", "1000");
		mQueue.add(ParamTools.packParam(Constants.getRecommendedMember, this,
				this, map));
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
			String msg = json.optString("msg");
			if (stauts == 0) {
				if (url.contains(Constants.getRecommendedMember)) {
					String recommendeds = json.optString("recommendeds");
					mRecommendeds = JSON.parseArray(recommendeds, RecommendedsBean.class);
					if (mRecommendeds.size() > 0) {
						lv_list.setVisibility(View.VISIBLE);
						tv_text.setVisibility(View.GONE);
					} else {
						lv_list.setVisibility(View.GONE);
						tv_text.setVisibility(View.VISIBLE);
					}
					lv_list.setAdapter(new Adpter());
				}
			} else {
				Tools.showToast(TheInvitationActivity.this, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
		}
	}

	class Adpter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mRecommendeds.size();
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(TheInvitationActivity.this)
						.inflate(R.layout.item_the_invitation, null);
				holder.tv_phone = (TextView) convertView
						.findViewById(R.id.tv_phone);
				holder.tv_date = (TextView) convertView
						.findViewById(R.id.tv_date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_phone.setText(mRecommendeds.get(position).getMember_phone());
			holder.tv_date.setText(Tools.getDateformat(mRecommendeds.get(position).getCreate_date()));
			return convertView;
		}
	}

	class ViewHolder {
		TextView tv_phone, tv_date;
	}
}
