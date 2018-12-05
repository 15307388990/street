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
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.PaidAdapter;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.FinanceSettleBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshBase.OnRefreshListener;
import com.ming.ui.PullToRefreshListView;
import com.yl.ming.efengshe.R;

/**
 * 
 * 类名称：FreezeActivity 类描述： 冻结金额 首页 创建人：罗富贵 创建时间：2016年5月11日
 * 
 * @version
 * 
 */
public class FreezeActivity extends BaseActivity {

	@ViewInject(R.id.tv_total_price)
	private TextView tv_total_price;// 合计
	@ViewInject(R.id.ll_wu)
	private LinearLayout ll_wu;
	@ViewInject(R.id.lv_list)
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	private PaidAdapter paidAdapter;
	private List<FinanceSettleBean> financeSettles = new ArrayList<FinanceSettleBean>();
	private int pagNumber = 1;// 页码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_freeze_list);
		ViewUtils.inject(this);
		initTitle();
		title.setText("冻结金额");
		initValues();
	}

	private void initValues() {
		initPull();
		paidAdapter = new PaidAdapter(this, financeSettles);
		mListView.setAdapter(paidAdapter);
		obtainData();
		loading();
	}

	private void initPull() {
		mPullToRefreshListView.setPullLoadEnabled(false);
		mPullToRefreshListView.setScrollLoadEnabled(true);
		mListView = mPullToRefreshListView.getRefreshableView();
		// mlistview.setSelector(R.color.gray);
		// mlistview.setDividerHeight(20);
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
			int stauts = json.optInt("stauts");
			String msg = json.optString("msg");
			if (stauts == 0) {
				tv_total_price.setText(json.optDouble("sum") / 100 + "");
				ArrayList<FinanceSettleBean> finance = (ArrayList<FinanceSettleBean>) JSON
						.parseArray(json.getString("finance"),
								FinanceSettleBean.class);
				if (pagNumber > 1) {
					financeSettles.addAll(finance);
				} else {
					financeSettles = finance;
				}

				if (financeSettles.size() > 0) {
					mPullToRefreshListView.setVisibility(View.VISIBLE);
					ll_wu.setVisibility(View.GONE);
				} else {
					mPullToRefreshListView.setVisibility(View.GONE);
					ll_wu.setVisibility(View.VISIBLE);

				}
				paidAdapter.updateListView(financeSettles);
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
