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

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yl.ming.efengshe.R;
import com.juxun.business.street.adapter.CommissionOrderDetailAdapter;
import com.juxun.business.street.bean.Analysisbean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.MallShoppingCartMode;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 
 * 类名称：CommissionOrderDetailAcitivity 类描述：订单分佣详情 首页 创建人：罗富贵 创建时间：2016年5月10日
 * 
 * @version
 * 
 */
public class CommissionOrderDetailAcitivity extends BaseActivity {

	@ViewInject(R.id.tv_sfxj)
	private TextView tv_sfxj;// 实付现金
	@ViewInject(R.id.tv_fyhj)
	private TextView tv_fyhj;// 分佣合计
	@ViewInject(R.id.lv_list)
	private ListView lv_list;
	private CommissionOrderDetailAdapter paidAdapter;
	private List<MallShoppingCartMode> mallShoppingCartbeans = new ArrayList<MallShoppingCartMode>();
	private String order_id;
	private Analysisbean analysisbean;
	DecimalFormat df = new java.text.DecimalFormat("0.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commission_order_detail);
		ViewUtils.inject(this);
		initTitle();
		title.setText("分佣订单详情");
		initValues();
	}

	private void initValues() {
		analysisbean = (Analysisbean) getIntent().getSerializableExtra("analysisbean");
		order_id = analysisbean.getOrder_id();
		paidAdapter = new CommissionOrderDetailAdapter(this, mallShoppingCartbeans);
		lv_list.setAdapter(paidAdapter);
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
//		map.put("admin_id", storeBean.getAdmin_id() + "");
		map.put("order_id", order_id);
		mQueue.add(ParamTools.packParam(Constants.businessWithOrder, this, this, map));
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
//				mallShoppingCartbeans = (ArrayList<MallShoppingCartMode>) JSON.parseArray(json.optString("analysis_list"),
//						MallShoppingCartMode.class);
				String analysis_list = json.getString("analysis_list");
				mallShoppingCartbeans = JSON.parseArray(analysis_list,
						MallShoppingCartMode.class);

				tv_sfxj.setText(df.format(analysisbean.getOrder_price() / 100));
				tv_fyhj.setText(df.format(analysisbean.getOrder_profit() / 100));
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
