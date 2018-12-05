package com.juxun.business.street.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yl.ming.efengshe.R;
import com.juxun.business.street.adapter.GoodsCoverAdapter;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.EleGoodListBean;
import com.juxun.business.street.bean.EleOrderBean;
import com.juxun.business.street.bean.ParseModel;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.CustomListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 
 * @author Lifucheng 电商订单详情
 */
public class EleOrderDetailActivity extends BaseActivity implements
		OnClickListener {
	@ViewInject(R.id.delivery)
	private TextView delivery;// 发货
	@ViewInject(R.id.order_number)
	private TextView order_number;// 单号
	@ViewInject(R.id.payments)
	private TextView payments;// 支付方式
	@ViewInject(R.id.order_time)
	private TextView order_time;// 下单时间
	@ViewInject(R.id.transaction_time)
	private TextView transaction_time;// 成交时间
	@ViewInject(R.id.customer_name)
	private TextView customer_name;// 客户名称
	@ViewInject(R.id.customer_address)
	private TextView customer_address;// 客户地址
	@ViewInject(R.id.goods_list)
	private CustomListView goods_list;// 商品列表
	@ViewInject(R.id.goods_total_price)
	private TextView goods_total_price;// 商品总额
	@ViewInject(R.id.cash_back)
	private TextView cash_back;// 返现
	@ViewInject(R.id.shipment)
	private TextView shipment;// 运费
	@ViewInject(R.id.customer_phone)
	private TextView customer_phone;
	@ViewInject(R.id.disbursements)
	private TextView disbursements;// 实付款

	private EleOrderBean bean;
	private List<EleGoodListBean> lists = new ArrayList<EleGoodListBean>();
	private GoodsCoverAdapter adapter;
	private int tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ele_order_detail);
		ViewUtils.inject(this);
		initTitle();
		title.setText(getString(R.string.order_detail));
		initValues();
		bindEvents();
	}

	private void initValues() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		bean = (EleOrderBean) bundle.getSerializable("bean");
		requestHttp();
	}

	private void requestHttp() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", ParseModel.loginBean.getUserId() + "");
		map.put("authToken", ParseModel.loginBean.getAuthToken() + "");
		map.put("order_id", bean.getId() + "");
		map.put("oper_id", ParseModel.loginBean.getOper_id() + "");
		mQueue.add(ParamTools.packParam(Constants.shopOrderInfo, this, this,
				map));
		loading();
	}

	private void bindEvents() {
		delivery.setOnClickListener(this);
	}

	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.getInt("status");
			String msg = json.getString("msg");
			if (stauts == 0) {
				// 支付类型 【1.微信支付,2.到店付款,3.支付宝】
				JSONObject jsonOrder = json.getJSONObject("order");
				int pay_type = json.optInt("pay_type");
				String strPayType = null;
				if (pay_type == 1) {
					strPayType = "微信支付";
				} else if (pay_type == 2) {
					strPayType = "到店付款";
				} else if (pay_type == 3) {
					strPayType = "支付宝支付";
				} else if (pay_type == 3) {
					strPayType = "积分支付";
				} else {
					strPayType = "支付宝支付";
				}
				payments.setText(strPayType);
				String order_id = jsonOrder.getString("order_id");
				order_number.setText(order_id);
				int status = jsonOrder.getInt("status");
				// 状态订单状态【1.待付款,2.待发货,3.已发货,4.交易完成,5.已取消,6.已退货,7.临时单
				String strStatus = null;
				if (status == 1) {
					tag = 1;
					strStatus = "待付款";
				} else if (status == 2) {
					tag = 2;
					strStatus = "发货";
				} else if (status == 3) {
					tag = 3;
					strStatus = "订单追踪";
				} else if (status == 4) {
					tag = 4;
					strStatus = "交易完成";
				} else if (status == 5) {
					tag = 5;
					strStatus = "已取消";
				} else if (status == 6) {
					tag = 6;
					strStatus = "已退货";
				} else if (status == 7) {
					tag = 7;
					strStatus = "临时单";
				}
				delivery.setText(strStatus);

				String orderCreateTime = jsonOrder.getString("orderCreateTime");
				order_time.setText(orderCreateTime);
				String order_end_date = jsonOrder.getString("order_end_date");
				if (order_end_date != null && order_end_date.length() > 0
						&& !order_end_date.equals("null")) {
					transaction_time.setText(order_end_date);
				}
				String consigneeName = jsonOrder.getString("consigneeName");
				customer_name.setText(consigneeName);
				String consigneePhone = jsonOrder.getString("consigneePhone");
				customer_phone.setText(consigneePhone);
				String address = jsonOrder.getString("address");
				customer_address.setText(address);
				String total_price = jsonOrder.getString("total_price");
				goods_total_price.setText("¥" + total_price);
				String delivery_price = jsonOrder.getString("delivery_price");
				shipment.setText("¥" + delivery_price);
				String coupon_price = jsonOrder.getString("coupon_price");
				cash_back.setText("¥" + coupon_price);
				double total = Double.parseDouble(total_price);
				double delivery = Double.parseDouble(delivery_price);
				double coupon = Double.parseDouble(coupon_price);
				double dm = total + delivery - coupon;
				disbursements.setText("¥" + dm);
				JSONArray jsonArray = jsonOrder.getJSONArray("goodslist");
				if (jsonArray != null && jsonArray.length() > 0) {
					Gson gson = new Gson();
					Type type = new TypeToken<ArrayList<EleGoodListBean>>() {
					}.getType();
					List<EleGoodListBean> list = gson.fromJson(
							jsonArray.toString(), type);
					lists.addAll(list);
					adapter = new GoodsCoverAdapter(this, lists);
					goods_list.setAdapter(adapter);
				}
			} else {
				Tools.dealErrorMsg(this, url, stauts, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		if (tag == 2) {
			Intent intent = new Intent(this, EleOrderDeliveryActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("bean", bean);
			intent.putExtras(bundle);
			startActivity(intent);
		} else if (tag == 3) {
			Intent intent = new Intent(this, OrderLogisticsSearchActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("bean", bean);
			intent.putExtras(bundle);
			startActivity(intent);
		}

	}

}
