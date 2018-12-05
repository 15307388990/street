package com.juxun.business.street.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.CashFreezeAdapter;
import com.juxun.business.street.bean.SettleListBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshListView;
import com.ming.ui.PullToRefreshBase.OnRefreshListener;
import com.yl.ming.efengshe.R;

public class CashFreezeActivity extends BaseActivity {

	@ViewInject(R.id.ll_wu)
	private LinearLayout ll_wu;
	@ViewInject(R.id.lv_list)
	private PullToRefreshListView mPullToRefreshListView;

	private ListView mListView;
	private List<SettleListBean> financeSettles = new ArrayList<SettleListBean>();
	private int pagNumber = 1;// 页码
	private CashFreezeAdapter mCashFreezeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cash_freeze);
		ViewUtils.inject(this);
		Tools.acts.add(this);
		initTitle();
		title.setText("冻结金额记录");
		initView();
	}

	private void initView() {
		initPull();
		mCashFreezeAdapter = new CashFreezeAdapter(this, financeSettles);
		mListView.setAdapter(mCashFreezeAdapter);
		obtainData();
		loading();
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
						obtainData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						pagNumber++;
						obtainData();
					}
				});

	}

	/* 获取数据 */
	public void obtainData() {
		Map<String, String> map = new HashMap<String, String>();
		// agency_id int 机构id
		// auth_token String 登陆令牌
		// admin_id int 管理员id
		// pager.pageNumber int 页码
		// pager.pageSize int 每页显示数量
		map.put("auth_token", partnerBean.getAuth_token());
		map.put("settle_stauts", 0 + "");
		map.put("pageNumber", pagNumber + "");
		map.put("pageSize", 10 + "");
		mQueue.add(ParamTools.packParam(Constants.freezeList, this, this, map));
		loading();
	}

	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.optInt("status");
			String msg = json.optString("msg");
			if (stauts == 0) {
				JSONObject jsonObject=json.optJSONObject("result");
				String settle_list = jsonObject.optString("settle_list");
				List<SettleListBean> finance = JSON.parseArray(settle_list, SettleListBean.class);
				if (pagNumber > 1) {
					financeSettles.addAll(finance);
					if (finance.size() < 10) {
						mPullToRefreshListView.setHasMoreData(false); // 10个一页
					}
				} else {
					financeSettles = finance;
				}
				mPullToRefreshListView.setHasMoreData(true);
				if (financeSettles.size() < 10) {
					mPullToRefreshListView.setHasMoreData(false); // 10个一页
				}
				if (financeSettles.size() > 0) {
					mPullToRefreshListView.setVisibility(View.VISIBLE);
					ll_wu.setVisibility(View.GONE);
				} else {
					mPullToRefreshListView.setVisibility(View.GONE);
					ll_wu.setVisibility(View.VISIBLE);
				}

				mCashFreezeAdapter.updateListView(financeSettles);
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
