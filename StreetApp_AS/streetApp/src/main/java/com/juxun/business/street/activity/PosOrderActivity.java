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
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yl.ming.efengshe.R;
import com.juxun.business.street.adapter.PosOrderAdapter;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.ParseModel;
import com.juxun.business.street.bean.PosOrderBean;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.ming.ui.PullToRefreshBase;
import com.ming.ui.PullToRefreshListView;
import com.ming.ui.PullToRefreshBase.OnRefreshListener;
/**
 * 
 * @author Lifuhcheng
 * pos订单
 *
 */
public class PosOrderActivity extends BaseActivity {
	private PullToRefreshListView posListView;
	private ListView mlistView;
	private PosOrderAdapter adapter;
	private int pageNumber=1;//当前页码
	private int pageSize = 15;//每页个数
	private List<PosOrderBean> lists = new ArrayList<PosOrderBean>();
	
	
     @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_pos_order);
    	initTitle();
    	title.setText(getString(R.string.pos_order));
    	initViews();
    	initValues();
    	bindEvents();
    }
	
	private void initViews() {
		posListView = (PullToRefreshListView) findViewById(R.id.posListView);
		
	}
	private void initValues() {
		initPull();
		adapter = new PosOrderAdapter(this,lists);
		mlistView.setAdapter(adapter);
		requestHttp();
		loading();
		
	}
	private void initPull() {
		posListView.setPullLoadEnabled(false);
		posListView.setScrollLoadEnabled(true);
		mlistView = posListView.getRefreshableView();
		// mlistview.setSelector(R.color.gray);
		// mlistview.setDividerHeight(20);
		posListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				pageNumber = 1;
				requestHttp();

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				pageNumber++;
				requestHttp();
			}
		});

	}
	private void requestHttp() {
		Map<String , String> map = new HashMap<String, String>();
		map.put("pager.pageNumber", pageNumber+"");
		map.put("pager.pageSize", pageSize+"");
		map.put("order_shop", ParseModel.loginBean.getStoreId()+"");
		map.put("userId", ParseModel.loginBean.getUserId()+"");
		map.put("authToken", ParseModel.loginBean.getAuthToken()+"");
		map.put("oper_id", ParseModel.loginBean.getOper_id() + "");
		mQueue.add(ParamTools.packParam(Constants.lineOrderList, this, this, map));
	}
	 private void bindEvents() {
		 mlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PosOrderBean bean = lists.get(position-1);
				Intent intent = new Intent(PosOrderActivity.this, PosOrderDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("PosOrderBean", bean);
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
		});
	 }
	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
		    int stauts = json.getInt("status");
		    String msg = json.getString("msg");
		    if(stauts == 0){
		    	posListView.onPullDownRefreshComplete();
		    	posListView.onPullUpRefreshComplete();
		    	JSONArray jsonArray = json.getJSONArray("list");
		    	if(jsonArray.length()>0){
		    		Gson gson = new Gson();
					Type type = new TypeToken<ArrayList<PosOrderBean>>(){}.getType();  
					List<PosOrderBean> list = gson.fromJson(jsonArray.toString(), type);
					lists.addAll(list);
					adapter.setLists(lists);
					adapter.notifyDataSetChanged();
		    	}else{
		    	}
		    	
		    }else if(stauts < 0){
		    	Tools.dealErrorMsg(this, url, stauts, msg);


		    }
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
