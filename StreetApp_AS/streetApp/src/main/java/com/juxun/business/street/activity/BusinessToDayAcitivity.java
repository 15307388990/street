/**
 * 
 */
package com.juxun.business.street.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.BusinessToDayAdapter;
import com.juxun.business.street.bean.Analysisbean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshListView;
import com.ming.ui.PullToRefreshBase.OnRefreshListener;
import com.yl.ming.efengshe.R;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 
 * 类名称：BusinessAnalysisAcitivity 类描述：当日 营业分析 首页 创建人：罗富贵 创建时间：2016年5月10日
 * 
 * @version
 * 
 */
public class BusinessToDayAcitivity extends BaseActivity {

	@ViewInject(R.id.ll_wu)
	private LinearLayout ll_wu;// 无数据
	@ViewInject(R.id.lv_list)
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	private BusinessToDayAdapter paidAdapter;
	private List<Analysisbean> analysisbeans = new ArrayList<Analysisbean>();
	private int pagNumber = 1;// 页码
	private String start_date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_today_business);
		ViewUtils.inject(this);
		initTitle();
		title.setText("当日营业分析");
		initValues();
	}

	private void initValues() {
		initPull();
		start_date = getIntent().getStringExtra("start_date");
		paidAdapter = new BusinessToDayAdapter(this, analysisbeans);
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
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				pagNumber = 1;
				obtainData();

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
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
		// start_date String 开始时间格式yyyy-MM-dd
		map.put("auth_token", partnerBean.getAuth_token());
//		map.put("admin_id", storeBean.getAdmin_id() + "");
		map.put("start_date", start_date);
		map.put("pageNumber", pagNumber + "");
		map.put("pageSize", 10 + "");
		mQueue.add(ParamTools.packParam(Constants.businessWithDay, this, this,
				map));
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
				ArrayList<Analysisbean> finance = (ArrayList<Analysisbean>) JSON
						.parseArray(json.getString("analysis_list"),
								Analysisbean.class);
				if (pagNumber > 1) {
					analysisbeans.addAll(finance);
				} else {
					analysisbeans = finance;
				}

				if (analysisbeans.size() > 0) {
					mPullToRefreshListView.setVisibility(View.VISIBLE);
					ll_wu.setVisibility(View.GONE);
				} else {
					mPullToRefreshListView.setVisibility(View.GONE);
					ll_wu.setVisibility(View.VISIBLE);

				}
				paidAdapter.updateListView(analysisbeans);
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
