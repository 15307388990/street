package com.juxun.business.street.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.UserStatisticsAdapter;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.UserStatisticsBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.ClearEditText;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ming.ui.PullToRefreshListView;
import com.yl.ming.efengshe.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * 
 * @author luoming 用户统计 搜索页面
 *
 */
public class UserStatisticsSearchActivity extends BaseActivity {

	@ViewInject(R.id.ll_wu)
	private LinearLayout ll_wu;
	@ViewInject(R.id.lv_list)
	private PullToRefreshListView lv_list;
	private ListView mListView;
	private List<UserStatisticsBean> lists;
	private UserStatisticsAdapter userStatisticsAdapter;

	@ViewInject(R.id.button_back)
	 
	private ImageView button_back;
	@ViewInject(R.id.ed_clear)
	private ClearEditText ed_clear;// 搜索的条件
	@ViewInject(R.id.button_search)
	private TextView button_search;// 取消

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userstatistics_search);
		ViewUtils.inject(this);
		button_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ed_clear.setText("");

			}
		});
		button_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		initPull();
		initOclik();

	}

	private void initOclik() {
		ed_clear.setFocusable(true);
		lists = new ArrayList<UserStatisticsBean>();
		userStatisticsAdapter = new UserStatisticsAdapter(this, lists);
		mListView.setAdapter(userStatisticsAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(UserStatisticsSearchActivity.this, UserDetailsActivity.class);
				intent.putExtra("nameString", lists.get(position).getMember_phone());
				startActivity(intent);

			}
		});
		ed_clear.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					getMemberRecordStatices();
				}
				return false;
			}
		});
	}

	@SuppressLint("ResourceAsColor")
	private void initPull() {
		lv_list.setPullLoadEnabled(false);
		lv_list.setScrollLoadEnabled(false);
		mListView = lv_list.getRefreshableView();
		mListView.setDivider(new ColorDrawable(R.color.gray));
		mListView.setDividerHeight(0);

	}

	// 用户统计
	private void getMemberRecordStatices() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("auth_token", partnerBean.getAuth_token() + "");
		map.put("member_phone", ed_clear.getText().toString());// 按手机号码查询
		mQueue.add(ParamTools.packParam(Constants.getMemberRecordStatices, this, this, map));
	}

	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.getInt("status");
			String msg = json.getString("msg");
			if (stauts == 0) {
				if (url.contains(Constants.getMemberRecordStatices)) {
					String memberRechargeRecordList = json.getString("result");
					lists = JSON.parseArray(memberRechargeRecordList, UserStatisticsBean.class);
					if (lists.size() > 0) {
						ll_wu.setVisibility(View.GONE);
						lv_list.setVisibility(View.VISIBLE);
						userStatisticsAdapter.Update(lists);
					} else {
						ll_wu.setVisibility(View.VISIBLE);
						lv_list.setVisibility(View.GONE);
						Tools.showToast(UserStatisticsSearchActivity.this, "未找到相关记录");
					}

				}
			} else if (stauts == -4004) {
				mSavePreferencesData.putStringData("json", "");
				Tools.jump(UserStatisticsSearchActivity.this, LoginActivity.class, false);
				Tools.showToast(UserStatisticsSearchActivity.this, "登录过期请重新登录");
				Tools.acts.clear();
			} else {
				Tools.showToast(UserStatisticsSearchActivity.this, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
