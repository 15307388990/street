package com.juxun.business.street.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.ExtractableAdapter;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.FinanceWithdrawBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshListView;
import com.ming.ui.PullToRefreshBase.OnRefreshListener;
import com.yl.ming.efengshe.R;

/**
 * 页面：提现记录
 * 
 * @author wood121
 * 
 */
public class CashRecordActivity extends BaseActivity {

	@ViewInject(R.id.lv_list)
	private PullToRefreshListView mPullToRefreshListView;

	@ViewInject(R.id.tv_wu)
	private TextView tv_wu;

	private List<FinanceWithdrawBean> financeWithdraws = new ArrayList<FinanceWithdrawBean>();
	private ExtractableAdapter mExtractableAdapter;

	private ListView mListView;
	private int pagNumber = 1;// 页码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cash_record);
		ViewUtils.inject(this);
		Tools.acts.add(this);
		initTitle();
		title.setText("提现记录");
		initView();
	}

	private void initView() {
		mExtractableAdapter = new ExtractableAdapter(CashRecordActivity.this,
				financeWithdraws);
		initPull();
		financeWithdraws = new ArrayList<FinanceWithdrawBean>();
		mExtractableAdapter = new ExtractableAdapter(this, financeWithdraws);
		mListView.setAdapter(mExtractableAdapter);
		initDate();
	}

	private void initPull() {
		mPullToRefreshListView.setPullLoadEnabled(false);
		mPullToRefreshListView.setScrollLoadEnabled(true);
		mListView = mPullToRefreshListView.getRefreshableView();
		mPullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						pagNumber = 1;
						initDate();

					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						pagNumber++;
						initDate();
					}
				});
	}

	private void initDate() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		// agency_id int 机构id
		// auth_token String 登陆令牌
		// admin_id int 管理员id
		map.put("auth_token", partnerBean.getAuth_token());
		// map.put("withdraw_stauts", "1"); //可以不传
		map.put("pageNum", pagNumber + "");
		map.put("pageSize", 1000 + "");
		mQueue.add(ParamTools.packParam(Constants.completeWithdraw, this, this,
				map));
	}

	@Override
	public void onResponse(String response, String url) {
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.optInt("status");
			String msg = json.optString("msg");
			if (stauts == 0) {

				ArrayList<FinanceWithdrawBean> arrayList = (ArrayList<FinanceWithdrawBean>) JSON
						.parseArray(json.getString("result"),
								FinanceWithdrawBean.class);
				if (pagNumber > 1) {
					financeWithdraws.addAll(arrayList);
					if (arrayList.size() < 10) {
						mPullToRefreshListView.setHasMoreData(false); // 10个一页
					}
				} else {
					financeWithdraws = arrayList;
				}
				mPullToRefreshListView.setHasMoreData(true);
				if (financeWithdraws.size() < 10) {
					mPullToRefreshListView.setHasMoreData(false); // 10个一页
				}
				// 是否有数据， 列表显示与空数据显示
				if (financeWithdraws.size() > 0) {
					mPullToRefreshListView.setVisibility(View.VISIBLE);
					tv_wu.setVisibility(View.GONE);
				} else {
					mPullToRefreshListView.setVisibility(View.GONE);
					tv_wu.setVisibility(View.VISIBLE);
				}
				mExtractableAdapter.updateListView(financeWithdraws);
				mPullToRefreshListView.onPullDownRefreshComplete();
				mPullToRefreshListView.onPullUpRefreshComplete();
			} else {
				Tools.showToast(this, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
		}
	}

}
