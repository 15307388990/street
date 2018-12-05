package com.juxun.business.street.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.bean.AgencyFinancebean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.AlwaysMarqueeTextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

/**
 * 
 * 类名称：WithdrawAccessActivity 类描述：提现账户列表 创建人：罗富贵 创建时间：2016年5月11日
 * 
 * @version
 * 
 */
public class WithdrawAccessActivity extends BaseActivity {

	@ViewInject(R.id.ll_zhifubao)
	private LinearLayout ll_zhifubao;// 支付宝
	@ViewInject(R.id.ll_yinhang)
	private LinearLayout ll_yinhang;// 银行
	@ViewInject(R.id.tv_zfb_name)
	private TextView tv_zfb_name;// 支付宝账号名
	@ViewInject(R.id.tv_yh_number)
	private AlwaysMarqueeTextView tv_yh_number;// 银行卡号
	@ViewInject(R.id.tv_yh_name)
	private TextView tv_yh_name;// 所属银行
	@ViewInject(R.id.tv_zhifubao_img)
	private TextView tv_zhifubao_img;// 支付宝勾选
	@ViewInject(R.id.tv_yinhangka_img)
	private TextView tv_yinhangka_img;// 银行卡勾选
	@ViewInject(R.id.tv_withdrawal_name)
	private TextView tv_withdrawal_name;// 支付宝账号信息
	@ViewInject(R.id.tv_withdrawal_name2)
	private TextView tv_withdrawal_name2;// 银行卡账号信息
	private AgencyFinancebean aFinancebean = new AgencyFinancebean();
	private int withdraw_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw);
		ViewUtils.inject(this);
		initTitle();
		title.setText("账号列表");
		initValues();
	}

	private void initValues() {
		withdraw_type = getIntent().getIntExtra("withdraw_type", 0);
		if (withdraw_type == 0) {
			tv_zhifubao_img.setVisibility(View.VISIBLE);
			tv_yinhangka_img.setVisibility(View.GONE);
		} else {
			tv_zhifubao_img.setVisibility(View.GONE);
			tv_yinhangka_img.setVisibility(View.VISIBLE);
		}
		obtainData();
		loading();

	}

	/** 单击事件 */
	@OnClick({ R.id.ll_zhifubao, R.id.ll_yinhang })
	public void clickMethod(View v) {
		if (v.getId() == R.id.ll_zhifubao) {
			this.setResult(0);
		} else if (v.getId() == R.id.ll_yinhang) {
			this.setResult(1);
		}
		this.finish();
	}

	/* 获取账户列表数据 */
	public void obtainData() {
		Map<String, String> map = new HashMap<String, String>();
		// agency_id int 机构id
		// auth_token String 登陆令牌
		// admin_id int 管理员id
		map.put("auth_token", partnerBean.getAuth_token());
		mQueue.add(ParamTools.packParam(Constants.withdrawAccess, this, this, map));
		loading();
	}

	/* 获取可提现金额数据 */
	public void obtainDataWithdrawList() {
		Map<String, String> map = new HashMap<String, String>();
		// agency_id int 机构id
		// auth_token String 登陆令牌
		// admin_id int 管理员id
		map.put("auth_token", partnerBean.getAuth_token());
		map.put("withdraw_stauts", "0");
		map.put("pageNum", 1+"");
		map.put("pageSize", 1000+"");
		mQueue.add(ParamTools.packParam(Constants.withdrawList, this, this, map));
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
				// 首先对象不为空
				if (!json.isNull("profit")) {
					aFinancebean = JSON.parseObject(json.getString("profit"), AgencyFinancebean.class);
					// 如果支付宝账号不为空
					if (!aFinancebean.getWithdrawal_alipay().equals("")) {
						ll_zhifubao.setVisibility(View.VISIBLE);
						tv_zfb_name.setText(aFinancebean.getWithdrawal_alipay());
						tv_withdrawal_name.setText(aFinancebean.getWithdrawal_name());

					}
					if (!aFinancebean.getWithdrawal_card().equals("")) {
						ll_yinhang.setVisibility(View.VISIBLE);
						tv_yh_number.setText(aFinancebean.getWithdrawal_card());
						tv_yh_name.setText(aFinancebean.getWithdrawal_bank_name());
						tv_withdrawal_name2.setText(aFinancebean.getWithdrawal_name());
					}
				} else {

				}

			} else {
				Tools.showToast(this, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
		}
	}
}
