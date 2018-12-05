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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yl.ming.efengshe.R;
import com.juxun.business.street.adapter.MyOrderdapter;
import com.juxun.business.street.adapter.MyOrderdapter.onOperateOrder;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.OrderModel;
import com.juxun.business.street.bean.ParseModel;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshListView;
import com.ming.ui.PullToRefreshBase.OnRefreshListener;

/**
 * 
 * @author 电商订单
 * 
 */
public class ElectricitySupplierOrderActivity extends BaseActivity implements
		onOperateOrder {
	private RadioGroup cagetoryGroup;
	private RadioButton whole;// 全部
	private RadioButton to_be_shipped;// 待发货
	private RadioButton shipped;// 已发货
	private RadioButton completed;// 已完成
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	private MyOrderdapter adapter;
	private List<OrderModel> wholeList = new ArrayList<OrderModel>();
	private List<OrderModel> toBeshippedList = new ArrayList<OrderModel>();
	private List<OrderModel> shippedList = new ArrayList<OrderModel>();
	private List<OrderModel> completedList = new ArrayList<OrderModel>();
	private List<OrderModel> returnSingleList = new ArrayList<OrderModel>();
	// 订单状态//0 全部 1待发货 2已完成 3（退货单）
	private int pageNumber = 1;// 当前页码
	private int pageSize = 15;// 每页个数
	private int tag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_electricity_supplier);
		initTitle();
		title.setText(getString(R.string.electricity_supplier_order));
		initView();
		initValues();
		bindEvents();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (ParseModel.isdate) {
			pageNumber = 1;
			if (tag == 0) {
				wholeList.clear();
				requestHttp(0);
			} else if (tag == 1) {
				toBeshippedList.clear();
				requestHttp(1);
			} else if (tag == 2) {
				shippedList.clear();
				requestHttp(2);
			} else if (tag == 3) {
				completedList.clear();
				requestHttp(3);
			} else if (tag == 6) {
				returnSingleList.clear();
				requestHttp(6);
			}
		}

	}

	private void initView() {
		cagetoryGroup = (RadioGroup) findViewById(R.id.cagetoryGroup);
		whole = (RadioButton) findViewById(R.id.wholeItem);
		to_be_shipped = (RadioButton) findViewById(R.id.to_be_shipped);
		shipped = (RadioButton) findViewById(R.id.shipped);
		completed = (RadioButton) findViewById(R.id.completed);
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.eleListView);
	}

	private void initValues() {
		initPull();
		whole.setChecked(true);
		adapter = new MyOrderdapter(this, wholeList, this);
		mListView.setAdapter(adapter);
		requestHttp(0);
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
						pageNumber = 1;
						if (tag == 0) {
							wholeList.clear();
							requestHttp(0);
						} else if (tag == 1) {
							toBeshippedList.clear();
							requestHttp(1);
						} else if (tag == 2) {
							shippedList.clear();
							requestHttp(2);
						} else if (tag == 3) {
							completedList.clear();
							requestHttp(3);
						} else if (tag == 6) {
							returnSingleList.clear();
							requestHttp(6);
						}

					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						if (tag == 0) {
							pageNumber++;
							requestHttp(0);
						} else if (tag == 1) {
							pageNumber++;
							requestHttp(1);
						} else if (tag == 2) {
							pageNumber++;
							requestHttp(2);
						} else if (tag == 3) {
							pageNumber++;
							requestHttp(3);
						} else if (tag == 6) {
							pageNumber++;
							requestHttp(6);
						}
					}
				});

	}

	// 订单状态 已发货 3，已完成 4 待发货 2，退货单 6 全部 0
	private void requestHttp(int tag) {
		Map<String, String> map = new HashMap<String, String>();
		// community_id Integer 社区Id
		// order_state Integer 订单状态//订单状态【0.全部,1.未 完成,2.已完成,3.已关闭】
		// pageNumber Integer 页码（从1开始）
		// pageSize Integer 分页大小
		// auth_token String 授权码
		map.put("pageNumber", pageNumber + "");
		map.put("pageSize", pageSize + "");
		map.put("auth_token", partnerBean.getAuth_token() + "");
		map.put("order_state", tag + "");
		mQueue.add(ParamTools.packParam(Constants.shopOrderList, this, this,
				map));

	}

	private void bindEvents() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			// 订单状态 已发货 3，已完成 4 待发货 2，退货单 6 全部 0
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				OrderModel bean = null;
				if (tag == 0) {
					bean = wholeList.get(position - 1);
				} else if (tag == 1) {
					bean = toBeshippedList.get(position - 1);
				} else if (tag == 2) {
					bean = shippedList.get(position - 1);
				} else if (tag == 3) {
					bean = completedList.get(position - 1);
				} else if (tag == 6) {
					bean = returnSingleList.get(position - 1);
				}
				Intent intent = new Intent(
						ElectricitySupplierOrderActivity.this,
						EleOrderDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("bean", bean);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});
		cagetoryGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			// 订单状态 已发货 3，已完成 4 待发货 2，退货单 6 全部 0
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.wholeItem) {
					tag = 0;
					pageNumber = 1;
					wholeList.clear();
					adapter.updateListView(wholeList, 0);
					adapter.notifyDataSetChanged();
					requestHttp(0);
					loading();
				} else if (checkedId == R.id.to_be_shipped) {
					tag = 1;
					pageNumber = 1;
					toBeshippedList.clear();
					adapter.updateListView(toBeshippedList, 1);
					adapter.notifyDataSetChanged();
					requestHttp(1);
					loading();

				} else if (checkedId == R.id.shipped) {
					tag = 2;
					pageNumber = 1;
					shippedList.clear();
					adapter.updateListView(shippedList, 2);
					adapter.notifyDataSetChanged();
					requestHttp(2);
					loading();
				} else if (checkedId == R.id.completed) {
					tag = 3;
					pageNumber = 1;
					completedList.clear();
					adapter.updateListView(completedList, 3);
					adapter.notifyDataSetChanged();
					requestHttp(3);
					loading();
				}
			}
		});
	}

	@Override
	public void onResponse(String response, String url) {
		dismissLoading();

		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.getInt("status");
			// String msg = json.getString("msg");
			if (stauts == 0) {
				mPullToRefreshListView.onPullDownRefreshComplete();
				mPullToRefreshListView.onPullUpRefreshComplete();
				JSONArray jsonArray = json.getJSONArray("list");
				if (jsonArray.length() > 0) {
					Gson gson = new Gson();
					Type type = new TypeToken<ArrayList<OrderModel>>() {
					}.getType();
					List<OrderModel> list = gson.fromJson(jsonArray.toString(),
							type);
					// 订单状态 已发货 3，已完成 4 待发货 2，退货单 6 全部 0
					if (tag == 0) {
						wholeList.addAll(list);
						adapter.updateListView(wholeList, 0);

					} else if (tag == 1) {
						toBeshippedList.addAll(list);
						adapter.updateListView(toBeshippedList, 1);
					} else if (tag == 2) {
						shippedList.addAll(list);
						adapter.updateListView(shippedList, 2);
					} else if (tag == 3) {
						completedList.addAll(list);
						adapter.updateListView(completedList, 3);
					} else if (tag == 6) {
						returnSingleList.addAll(list);
						adapter.updateListView(returnSingleList, 6);
					}
					adapter.notifyDataSetChanged();

				} else {

				}

			} else if (stauts < 0) {
				Tools.dealErrorMsg(this, url, stauts, json.getString("msg"));

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	@Override
	public void deleteOrder(String string, int position, int orderTypeFrom) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendOrder(String string, int position, int orderTypeFrom) {
		// TODO Auto-generated method stub

	}

}
