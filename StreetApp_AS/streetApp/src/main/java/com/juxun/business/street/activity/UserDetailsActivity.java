package com.juxun.business.street.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.TheDetailBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author luoming 用户详情
 *
 */
public class UserDetailsActivity extends BaseActivity {
	@ViewInject(R.id.tv_name)
	private TextView tv_name;
	@ViewInject(R.id.tv_chongzhi)
	private TextView tv_chongzhi;
	@ViewInject(R.id.tv_xiaofei)
	private TextView tv_xiaofei;
	@ViewInject(R.id.tv_yue)
	private TextView tv_yue;
	@ViewInject(R.id.ll_layout)
	private LinearLayout ll_layout;
	@ViewInject(R.id.tv_liushui)
	private TextView tv_liushui;

	private String member_id;
	private String nameString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_details);
		ViewUtils.inject(this);
		initTitle();
		title.setText("用户详情");
		member_id = getIntent().getStringExtra("member_id");
		nameString = getIntent().getStringExtra("nameString");
		initOclik();
		getMemberRecordInfo();

	}

	private void initOclik() {
	}

	// 获取用户统计详情
	private void getMemberRecordInfo() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("auth_token", partnerBean.getAuth_token() + "");
		map.put("member_id", member_id + "");// 用户id
		mQueue.add(ParamTools.packParam(Constants.getMemberRecordInfo, this, this, map));
	}

	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.getInt("status");
			String msg = json.getString("msg");
			if (stauts == 0) {
				if (url.contains(Constants.getMemberRecordInfo)) {
					tv_name.setText(Tools.pNumber(nameString));
					tv_chongzhi.setText((double) json.optInt("total") / 100 + "元");
					tv_xiaofei.setText((double) json.optInt("used") / 100 + "元");
					tv_yue.setText((double) json.optInt("noUse") / 100 + "元");
					List<TheDetailBean> theDetailBeans = JSON.parseArray(json.getString("memberRechargeRecordList"),
							TheDetailBean.class);
					tv_liushui.setText("流水明细(" + theDetailBeans.size() + ")");
					if (theDetailBeans.size() > 0) {
						for (TheDetailBean theDetailBean : theDetailBeans) {
							View convertView = LayoutInflater.from(UserDetailsActivity.this)
									.inflate(R.layout.userdetails_itm, null);
							TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);
							TextView tv_type = (TextView) convertView.findViewById(R.id.tv_type);
							TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
							TextView tv_money = (TextView) convertView.findViewById(R.id.tv_money);
							tv_number.setText("单号：" + theDetailBean.getTran_no());
							tv_type.setText(theDetailBean.getType() == 1 ? "消费" : "充值");
							tv_time.setText(Tools.getDateformat2(theDetailBean.getCreate_date()));
							double recharge_money = (double)theDetailBean.getRecharge_money()/100;
							double give_money = (double)theDetailBean.getGive_money()/100;
							String recharge = mDf.format(recharge_money);
							String giveM = mDf.format(give_money);
							if(theDetailBean.getType() != 1){
								tv_money.setText("充"+recharge+"元，送"+giveM+"元");
							}else{
								tv_money.setText(recharge+"元");
							}
							
							ll_layout.addView(convertView);
						}

					}

				}
			} else if (stauts == -4004) {
				mSavePreferencesData.putStringData("json", "");
				Tools.jump(UserDetailsActivity.this, LoginActivity.class, false);
				Tools.showToast(UserDetailsActivity.this, "登录过期请重新登录");
				Tools.acts.clear();
			} else {
				Tools.showToast(UserDetailsActivity.this, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
