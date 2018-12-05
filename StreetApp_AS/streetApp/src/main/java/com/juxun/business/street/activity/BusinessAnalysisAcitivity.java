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
import com.juxun.business.street.adapter.BusinessAdapter;
import com.juxun.business.street.bean.Analysisbean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.TimerDateUtil;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.dialog.SelectBirthdayPop;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshListView;
import com.ming.ui.PullToRefreshBase.OnRefreshListener;
import com.yl.ming.efengshe.R;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * 
 * 类名称：BusinessAnalysisAcitivity 类描述： 营业分析 首页 创建人：罗富贵 创建时间：2016年5月10日
 * 
 * @version
 * 
 */
public class BusinessAnalysisAcitivity extends BaseActivity {

	@ViewInject(R.id.tv_yue_date)
	private TextView tv_yue_date;// 当月第一天
	@ViewInject(R.id.tv_current_date)
	private TextView tv_current_date;// 当前日期
	@ViewInject(R.id.tv_price)
	private TextView tv_price;// 营业额
	@ViewInject(R.id.tv_cost)
	private TextView tv_cost;// 订单成本
	@ViewInject(R.id.tv_profit)
	private TextView tv_profit;// 订单利润
	@ViewInject(R.id.btn_ok)
	private RadioButton btn_ok;// 确定
	@ViewInject(R.id.ll_wu)
	private LinearLayout ll_wu;// 无数据
	String start_date, end_date;

	TimerDateUtil timerDate = new TimerDateUtil();
	@ViewInject(R.id.lv_list)
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	private BusinessAdapter paidAdapter;
	private List<Analysisbean> analysisbeans = new ArrayList<Analysisbean>();
	private int pagNumber = 1;// 页码
	private SelectBirthdayPop birth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business);
		ViewUtils.inject(this);
		initTitle();
		title.setText("营业分析");
		initValues();
	}

	private void initValues() {
		end_date = timerDate.getNowDate();
		start_date = timerDate.getDate(7);
		tv_current_date.setText(end_date);
		tv_yue_date.setText(start_date);
		initPull();
		paidAdapter = new BusinessAdapter(this, analysisbeans);
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
	/** 单击事件 */
	@OnClick({ R.id.rbtn_zhou, R.id.rbtn_yue, R.id.rbtn_sanyue,
			R.id.tv_yue_date, R.id.tv_current_date, R.id.btn_ok })
	public void clickMethod(View v) {
		if (v.getId() == R.id.rbtn_zhou) {
			pagNumber = 1;
			start_date = timerDate.getDate(7);
			end_date = timerDate.getNowDate();
			tv_current_date.setText(end_date);
			tv_yue_date.setText(start_date);
			obtainData();
		} else if (v.getId() == R.id.rbtn_yue) {
			pagNumber = 1;
			end_date = timerDate.getNowDate();
			start_date = timerDate.getDate(30);
			tv_current_date.setText(end_date);
			tv_yue_date.setText(start_date);
			obtainData();
		} else if (v.getId() == R.id.rbtn_sanyue) {
			pagNumber = 1;
			end_date = timerDate.getNowDate();
			start_date = timerDate.getDate(90);
			tv_current_date.setText(end_date);
			tv_yue_date.setText(start_date);
			obtainData();
		} else if (v.getId() == R.id.tv_yue_date) {
			birth = new SelectBirthdayPop(BusinessAnalysisAcitivity.this,
					timerDate.getNowDate(), tv_yue_date);
			birth.showAtLocation(tv_yue_date, Gravity.BOTTOM, 0, 0);

		} else if (v.getId() == R.id.tv_current_date) {
			birth = new SelectBirthdayPop(BusinessAnalysisAcitivity.this,
					timerDate.getNowDate(), tv_current_date);
			birth.showAtLocation(tv_yue_date, Gravity.BOTTOM, 0, 0);
		} else if (v.getId() == R.id.btn_ok) {
			pagNumber = 1;
			start_date = tv_yue_date.getText().toString();
			end_date = tv_current_date.getText().toString();
			obtainData();
		}
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
		// end_date String 结束时间格式yyyy-MM-dd
		map.put("auth_token", partnerBean.getAuth_token());
		map.put("start_date", start_date);
		map.put("end_date", end_date);
		map.put("pageNumber", pagNumber + "");
		map.put("pageSize", 10 + "");
		mQueue.add(ParamTools.packParam(Constants.businessAnalysis, this, this,
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
				Analysisbean analysisbean = new Analysisbean();
				analysisbean = JSON.parseObject(json.optString("sum"),
						Analysisbean.class);
				tv_price.setText(analysisbean.getOrder_price()/100 + "");
				tv_cost.setText(analysisbean.getOrder_cost()/100 + "");
				tv_profit.setText(analysisbean.getOrder_profit()/100 + "");
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
