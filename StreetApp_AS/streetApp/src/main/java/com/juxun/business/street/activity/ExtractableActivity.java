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
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshListView;
import com.ming.ui.PullToRefreshBase.OnRefreshListener;
import com.yl.ming.efengshe.R;

/**
 * 
 * 类名称：ExtractableActivity 类描述： 已提现金额 首页 创建人：罗富贵 创建时间：2016年5月11日
 * 
 * @version
 * 
 */
public class ExtractableActivity extends BaseActivity {

	@ViewInject(R.id.lv_list)
	private PullToRefreshListView mPullToRefreshListView;
	@ViewInject(R.id.tv_total_price)
	private TextView tv_total_price;// 合计
	@ViewInject(R.id.tv_wu)
	private TextView tv_wu;
	private List<FinanceWithdrawBean> financeWithdraws;
	private ExtractableAdapter extractableAdapter;
	private ListView mListView;
	String sum;
	private int pagNumber = 1;// 页码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_extractable_list);
		ViewUtils.inject(this);
		initTitle();
		title.setText("已提现金额");
		initView();
	}

	private void initView() {
		sum = getIntent().getStringExtra("sum");
		initPull();
		financeWithdraws = new ArrayList<FinanceWithdrawBean>();
		extractableAdapter = new ExtractableAdapter(ExtractableActivity.this, financeWithdraws);
		mListView.setAdapter(extractableAdapter);
		initDate();
		loading();
	}
	
	private void initPull() {
		mPullToRefreshListView.setPullLoadEnabled(false);
		mPullToRefreshListView.setScrollLoadEnabled(true);
		mListView = mPullToRefreshListView.getRefreshableView();
		// mlistview.setSelector(R.color.gray);
		// mlistview.setDividerHeight(20);
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				pagNumber = 1;
				initDate();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				pagNumber++;
				initDate();
			}
		});

	}
	
	private void initDate() {
		// TODO Auto-generated method stub
		tv_total_price.setText(sum);
		Map<String, String> map = new HashMap<String, String>();
		// agency_id int 机构id
		// auth_token String 登陆令牌
		// admin_id int 管理员id
		map.put("auth_token", partnerBean.getAuth_token());
//		map.put("withdraw_stauts", "1");
		map.put("pageNum", pagNumber+"");
		map.put("pageSize", 1000+"");
		mQueue.add(ParamTools.packParam(Constants.completeWithdraw, this, this, map));
		loading();
	}

	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.optInt("stauts");
			String msg = json.optString("msg");
			if (stauts == 0) {

				ArrayList<FinanceWithdrawBean> arrayList = (ArrayList<FinanceWithdrawBean>) JSON
						.parseArray(json.getString("withdrawList"), FinanceWithdrawBean.class);
				if (pagNumber > 1) {
					financeWithdraws.addAll(arrayList);
				} else {
					financeWithdraws = arrayList;
				}
				if (financeWithdraws.size() > 0) {
					mPullToRefreshListView.setVisibility(View.VISIBLE);
					tv_wu.setVisibility(View.GONE);
				} else {
					mPullToRefreshListView.setVisibility(View.GONE);
					tv_wu.setVisibility(View.VISIBLE);

				}
				extractableAdapter.updateListView(financeWithdraws);
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

	@OnClick({ R.id.tv_commit })
	void clickMethod(View v) {
		switch (v.getId()) {
		case R.id.tv_commit:
			break;
		}

	}

}
