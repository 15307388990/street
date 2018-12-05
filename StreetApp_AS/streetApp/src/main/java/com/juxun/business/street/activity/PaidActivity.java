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

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yl.ming.efengshe.R;
import com.juxun.business.street.adapter.PaidAdapter;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.FinanceSettleBean;
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

/**
 * 
 * 类名称：PaidActivity 类描述： 实收总金额 首页 创建人：罗富贵 创建时间：2016年5月10日
 * 
 * @version
 * 
 */
public class PaidActivity extends BaseActivity {

	@ViewInject(R.id.tv_yue_date)
	private TextView tv_yue_date;// 当月第一天
	@ViewInject(R.id.tv_current_date)
	private TextView tv_current_date;// 当前日期
	@ViewInject(R.id.tv_total_price)
	private TextView tv_total_price;// 总金额
	@ViewInject(R.id.rbtn_zhou)
	private RadioButton rbtn_zhou;// 近一周
	@ViewInject(R.id.rbtn_yue)
	private RadioButton rbtn_yue;// 近一月
	@ViewInject(R.id.rbtn_sanyue)
	private RadioButton rbtn_sanyue;// 近三月
	@ViewInject(R.id.btn_ok)
	private RadioButton btn_ok;// 确定
	@ViewInject(R.id.ll_wu)
	private LinearLayout ll_wu;// 无数据
	String start_date, end_date;

	TimerDateUtil timerDate = new TimerDateUtil();
	@ViewInject(R.id.lv_list)
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	private PaidAdapter paidAdapter;
	private List<FinanceSettleBean> financeSettles = new ArrayList<FinanceSettleBean>();
	private int pagNumber = 1;// 页码
	private SelectBirthdayPop birth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paid);
		ViewUtils.inject(this);
		initTitle();
		title.setText("实收总金额");
		initValues();
	}

	private void initValues() {
		end_date = timerDate.getNowDate();
		start_date = timerDate.getDate(7);
		tv_current_date.setText(end_date);
		tv_yue_date.setText(start_date);
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
	@OnClick({ R.id.rbtn_zhou, R.id.rbtn_yue, R.id.rbtn_sanyue, R.id.tv_yue_date, R.id.tv_current_date, R.id.btn_ok })
	public void clickMethod(View v) {
		if (v.getId() == R.id.rbtn_zhou) {
			pagNumber = 1;
			start_date = timerDate.getDate(7);
			end_date = timerDate.getNowDate();
			tv_current_date.setText(end_date);
			tv_yue_date.setText(start_date);
			obtainData();
			mListView.setSelection(0);
		} else if (v.getId() == R.id.rbtn_yue) {
			pagNumber = 1;
			end_date = timerDate.getNowDate();
			start_date = timerDate.getDate(30);
			tv_current_date.setText(end_date);
			tv_yue_date.setText(start_date);
			obtainData();
			mListView.setSelection(0);
		} else if (v.getId() == R.id.rbtn_sanyue) {
			pagNumber = 1;
			end_date = timerDate.getNowDate();
			start_date = timerDate.getDate(90);
			tv_current_date.setText(end_date);
			tv_yue_date.setText(start_date);
			obtainData();
			mListView.setSelection(0);
		} else if (v.getId() == R.id.tv_yue_date) {
			birth = new SelectBirthdayPop(PaidActivity.this, timerDate.getNowDate(), tv_yue_date);
			birth.showAtLocation(tv_yue_date, Gravity.BOTTOM, 0, 0);
		} else if (v.getId() == R.id.tv_current_date) {
			birth = new SelectBirthdayPop(PaidActivity.this, timerDate.getNowDate(), tv_current_date);
			birth.showAtLocation(tv_yue_date, Gravity.BOTTOM, 0, 0);
		} else if (v.getId() == R.id.btn_ok) {
			pagNumber = 1;
			start_date = tv_yue_date.getText().toString();
			end_date = tv_current_date.getText().toString();
			obtainData();
			mListView.setSelection(0);
			rbtn_zhou.setChecked(false);
			rbtn_yue.setChecked(false);
			rbtn_sanyue.setChecked(false);
		}
	}

	/* 获取数据 */
	public void obtainData() {
		Map<String, String> map = new HashMap<String, String>();
		// agency_id int 机构id
		// auth_token String 登陆令牌
		// admin_id int 管理员id
		// start_date String 开始时间格式yyyy-MM-dd
		// end_date String 结束时间 格式yyyy-MM-dd
		// pager.pageNumber int 页码
		// pager.pageSize int 每页显示数量
		map.put("auth_token", partnerBean.getAuth_token());
		// map.put("admin_id", storeBean.getAdmin_id() + "");
		map.put("start_date", start_date);
		map.put("end_date", end_date);
		// map.put("settle_stauts", 0);
		map.put("pageNumber", pagNumber + "");
		map.put("pageSize", 10 + "");
		mQueue.add(ParamTools.packParam(Constants.financePaid, this, this, map));
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
				tv_total_price.setText(json.optDouble("sum") / 100.0 + "");
				ArrayList<FinanceSettleBean> finance = (ArrayList<FinanceSettleBean>) JSON.parseArray(json.getString("finance"),
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
