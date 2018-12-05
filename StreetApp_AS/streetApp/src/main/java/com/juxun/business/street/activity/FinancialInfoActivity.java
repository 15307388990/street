/**
 * 
 */
package com.juxun.business.street.activity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxun.business.street.UILApplication;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.FinanceBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oubowu.slideback.SlideBackHelper;
import com.oubowu.slideback.SlideConfig;
import com.oubowu.slideback.widget.SlideBackLayout;
import com.yl.ming.efengshe.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 项目名称：Street 类名称：CustomerInfoActivity 类描述： 财务首页 创建人：罗富贵 创建时间：2016年5月10日
 * 
 * @version
 * 
 */
public class FinancialInfoActivity extends BaseActivity {

	@ViewInject(R.id.ll_paid)
	private LinearLayout ll_paid;// 实收总金额
	@ViewInject(R.id.tv_paid)
	private TextView tv_paid;// 实收总金额
	@ViewInject(R.id.ll_extractable)
	private LinearLayout ll_extractable;// 可提取金额
	@ViewInject(R.id.tv_extractable)
	private TextView tv_extractable;// 可提取金额
	@ViewInject(R.id.ll_been_extracted)
	private LinearLayout ll_been_extracted;// 已提取金额
	@ViewInject(R.id.tv_been_extracted)
	private TextView tv_been_extracted;// 已提取金额
	@ViewInject(R.id.ll_freeze)
	private LinearLayout ll_freeze;// 冻结金额
	@ViewInject(R.id.tv_freeze)
	private TextView tv_freeze;// 冻结金额
	/** ------------------------------------------- **/
	@ViewInject(R.id.ll_turnover)
	private LinearLayout ll_turnover;// 总营业额
	@ViewInject(R.id.tv_turnover)
	private TextView tv_turnover;// 总营业额
	@ViewInject(R.id.ll_profit)
	private LinearLayout ll_profit;// 总利润
	@ViewInject(R.id.tv_profit)
	private TextView tv_profit;// 总利润
	@ViewInject(R.id.ll_order)
	private LinearLayout ll_order;// 总接单数
	@ViewInject(R.id.tv_order)
	private TextView tv_order;// 总接单数
	@ViewInject(R.id.ll_commission)
	private LinearLayout ll_commission;// 分佣总金额
	@ViewInject(R.id.tv_commission)
	private TextView tv_commission;// 分佣总金额
	/** ------------------------------------------- **/
	@ViewInject(R.id.ll_today_turnover)
	private LinearLayout ll_today_turnover;// 今日营业额
	@ViewInject(R.id.tv_today_turnover)
	private TextView tv_today_turnover;// 今日营业额
	@ViewInject(R.id.ll_today_profit)
	private LinearLayout ll_today_profit;// 今日利润
	@ViewInject(R.id.tv_today_profit)
	private TextView tv_today_profit;// 今日利润
	@ViewInject(R.id.ll_today_order)
	private LinearLayout ll_today_order;// 今日接单数
	@ViewInject(R.id.tv_today_order)
	private TextView tv_today_order;// 今日接单数
	@ViewInject(R.id.ll_today_commission)
	private LinearLayout ll_today_commission;// 今日分佣金额
	@ViewInject(R.id.tv_today_commission)
	private TextView tv_today_commission;// 今日分佣金额
	@ViewInject(R.id.ll_fenxi)
	private LinearLayout ll_fenxi;// 营业分析
	/** ------------------------------------------- **/
	@ViewInject(R.id.btn_withdrawal)
	private Button btn_withdrawal;// 我要提现
	private SlideBackLayout mSlideBackLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_financial_info);
		mSlideBackLayout = SlideBackHelper.attach(
				// 当前Activity
				this,
				// Activity栈管理工具
				UILApplication.getActivityHelper(),
				// 参数的配置
				new SlideConfig.Builder()
						// 屏幕是否旋转
						.rotateScreen(false)
						// 是否侧滑
						.edgeOnly(false)
						// 是否禁止侧滑
						.lock(false)
						// 侧滑的响应阈值，0~1，对应屏幕宽度*percent
						.edgePercent(0.1f)
						// 关闭页面的阈值，0~1，对应屏幕宽度*percent
						.slideOutPercent(0.5f).create(),
				// 滑动的监听
				null);

		ViewUtils.inject(this);
		initTitle();
		title.setText(R.string.customers_details);
	}

	@Override
	protected void onResume() {
		obtainData();
		super.onResume();
	}

	/** 单击事件 */
	@OnClick({ R.id.ll_paid, R.id.ll_extractable, R.id.ll_been_extracted, R.id.ll_freeze, R.id.ll_turnover,
			R.id.ll_profit, R.id.ll_order, R.id.ll_commission, R.id.ll_today_turnover, R.id.ll_today_profit,
			R.id.ll_today_order, R.id.ll_today_commission, R.id.btn_withdrawal, R.id.ll_fenxi })
	public void clickMethod(View v) {
		if (v.getId() == R.id.ll_paid) {
			Tools.jump(this, PaidActivity.class, false);
		} else if (v.getId() == R.id.ll_extractable) {
			Tools.jump(this, BeenExtractableActivity.class, false);
		} else if (v.getId() == R.id.ll_been_extracted) {
			Intent intent = new Intent(FinancialInfoActivity.this, ExtractableActivity.class);
			intent.putExtra("sum", tv_been_extracted.getText().toString());
			startActivity(intent);
		} else if (v.getId() == R.id.ll_freeze) {
			Tools.jump(this, FreezeActivity.class, false);
			// Tools.showToast(getApplicationContext(), "暂未开放此功能");
		} else if (v.getId() == R.id.ll_turnover) {
			// Tools.jump(this, CouponListActivity.class, false);
			Tools.jump(this, BusinessAnalysisAcitivity.class, false);
		} else if (v.getId() == R.id.ll_profit) {
			Tools.jump(this, BusinessAnalysisAcitivity.class, false);
		} else if (v.getId() == R.id.ll_order) {
			// Tools.jump(this, BillActivity.class, false);
			Tools.jump(this, BusinessAnalysisAcitivity.class, false);
		} else if (v.getId() == R.id.ll_commission) {
			// 分佣
			Tools.jump(this, CommissionAnalysisAcitivity.class, false);
		} else if (v.getId() == R.id.ll_today_turnover) {
			Tools.jump(this, BusinessAnalysisAcitivity.class, false);
		} else if (v.getId() == R.id.ll_today_profit) {
			Tools.jump(this, BusinessAnalysisAcitivity.class, false);
		} else if (v.getId() == R.id.ll_today_order) {
			Tools.jump(this, BusinessAnalysisAcitivity.class, false);
		} else if (v.getId() == R.id.ll_today_commission) {
			// 分佣
			Tools.jump(this, CommissionAnalysisAcitivity.class, false);
		} else if (v.getId() == R.id.btn_withdrawal) {
			// 我要提现
			Tools.jump(this, ApplyWithdrawActivity.class, false);
		} else if (v.getId() == R.id.ll_fenxi) {
			// 我要提现
			Tools.jump(this, BusinessAnalysisAcitivity.class, false);
		}
	}

	/* 获取数据 */
	public void obtainData() {
		Map<String, String> map = new HashMap<String, String>();
		// agency_id int 机构id
		// auth_token String 登陆令牌
		// admin_id int 管理员id
		map.put("auth_token", partnerBean.getAuth_token());
		// map.put("admin_id", storeBean.getAdmin_id() + "");
		mQueue.add(ParamTools.packParam(Constants.financeIndex, this, this, map));
		loading();
	}

	// /** 设置数据 */
	public void setData(FinanceBean bean) {
		if (bean == null)
			return;
		// total_price double 实收总收入
		// withdraw_price double 已提现金额
		// can_withdraw_price double 可提现金额
		// freeze_price double 冻结金额
		// total_business_price double 总营业额
		// total_order_count int 总订单数
		// total_commission_price double 分佣总额
		// today_business_price double 今日营业额
		// today_profit_price double 今日利润
		// today_order_count int 今日订单数
		// today_commission_price double 今日分佣总额
		tv_paid.setText(bean.getTotal_price() / 100.0 + "");// 实收总收入
		tv_extractable.setText(bean.getCan_withdraw_price() / 100.0 + "");// 可提取金额
		tv_been_extracted.setText(bean.getWithdraw_price() / 100.0 + "");// 已提取金额
		tv_freeze.setText(bean.getFreeze_price() / 100.0 + "");// 冻结金额
		tv_turnover.setText(bean.getTotal_business_price() / 100.0 + "");// 总营业额
		tv_profit.setText(bean.getTotal_profit_price() / 100.0 + "");// 总利润
		tv_order.setText(bean.getTotal_order_count() + "");// 总接单数
		tv_commission.setText(bean.getTotal_commission_price() / 100.0 + "");// 分佣总金额
		tv_today_turnover.setText(bean.getBusiness_price() / 100.0 + "");// 今日营业额
		tv_today_profit.setText(bean.getProfit_price() / 100.0 + "");// 今日利润
		tv_today_order.setText(bean.getOrder_count() + "");// 今日接单数
		tv_today_commission.setText(bean.getCommission_price() / 100.0 + "");// 今日分佣金额
	}

	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.optInt("status");
			String msg = json.optString("msg");
			if (stauts == 0) {
				Gson gson = new Gson();
				Type type = new TypeToken<FinanceBean>() {
				}.getType();
				FinanceBean financeBean = new FinanceBean();
				financeBean = gson.fromJson(json.getString("finance"), type);
				setData(financeBean);
			} else if (stauts == -4004) {
				mSavePreferencesData.putStringData("json", "");
				Tools.jump(this, LoginActivity.class, true);
				Tools.showToast(this, "登录过期请重新登录");
				Tools.exit();
			} else {
				Tools.showToast(this, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		mSlideBackLayout.isComingToFinish();
	}
}
