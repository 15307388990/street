/**
 * 
 */
package com.juxun.business.street.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.BusinessOrderAdapter;
import com.juxun.business.street.bean.Analysisbean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.MallShoppingCartMode;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * 类名称：BusinessOrderAcitivity 类描述：订单盈利详情 创建人：罗富贵 创建时间：2016年5月10日
 * 
 * @version
 * 
 */
public class BusinessOrderAcitivity extends BaseActivity {

	@ViewInject(R.id.ll_wu)
	private LinearLayout ll_wu;// 无数据
	@ViewInject(R.id.lv_list)
	private ListView mListView;
	@ViewInject(R.id.tv_zongsj)
	private TextView tv_zongsj;
	@ViewInject(R.id.tv_zongjj)
	private TextView tv_zongjj;
	@ViewInject(R.id.tv_zongly)
	private TextView tv_zongly;
	private BusinessOrderAdapter paidAdapter;
	private List<MallShoppingCartMode> mallShoppingCartbeans = new ArrayList<MallShoppingCartMode>();
	private Analysisbean analysisbean;
	DecimalFormat df = new java.text.DecimalFormat("0.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_business);
		ViewUtils.inject(this);
		initTitle();
		title.setText("订单盈利详情");
		initValues();
	}

	private void initValues() {
		Intent intent = this.getIntent();
		analysisbean = (Analysisbean) intent
				.getSerializableExtra("analysisbean");
		paidAdapter = new BusinessOrderAdapter(this, mallShoppingCartbeans);
		mListView.setAdapter(paidAdapter);
		obtainData();
		loading();

	}

	/* 获取数据 */
	public void obtainData() {
		Map<String, String> map = new HashMap<String, String>();
		// agency_id int 机构id
		// auth_token String 登陆令牌
		// admin_id int 管理员id
		// pager.pageNumber int 页码
		// pager.pageSize int 每页显示数量
		// order_id String 订单id
		map.put("auth_token", partnerBean.getAuth_token());
		// map.put("admin_id", storeBean.getAdmin_id() + "");
		map.put("order_id", analysisbean.getOrder_id());
		mQueue.add(ParamTools.packParam(Constants.businessWithOrder, this,
				this, map));
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
				// Map<String, Object> map = JSON.parseObject(response,
				// Map.class);
				// com.alibaba.fastjson.JSONArray obj
				// =(com.alibaba.fastjson.JSONArray)map.get("analysis_list");
				// JSON.parseArray(obj.toJSONString(),List.class);
				String analysis_list = json.getString("analysis_list");
				mallShoppingCartbeans = JSON.parseArray(analysis_list,
						MallShoppingCartMode.class);

				if (mallShoppingCartbeans.size() > 0) {
					mListView.setVisibility(View.VISIBLE);
					ll_wu.setVisibility(View.GONE);
				} else {
					mListView.setVisibility(View.GONE);
					ll_wu.setVisibility(View.VISIBLE);

				}
				tv_zongsj.setText(df.format(analysisbean.getOrder_price()/100));
				tv_zongjj.setText(df.format(analysisbean.getOrder_cost()/100));
				tv_zongly.setText(df.format(analysisbean.getOrder_profit()/100));
				paidAdapter.updateListView(mallShoppingCartbeans);
			} else {
				Tools.showToast(this, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
		}
	}
}
