package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.yl.ming.efengshe.R;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.EleOrderBean;

public class OrderLogisticsSearchActivity extends BaseActivity {
	private WebView web;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_logistics_search);
		initTitle();
		title.setText("订单追踪");
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		EleOrderBean bean = (EleOrderBean) bundle.getSerializable("bean");
		web = (WebView) findViewById(R.id.web_html);
		
		web.loadUrl(Constants.mainUrl+"/loginUserAction.action?order_id="+bean.getId());
	}

	@Override
	public void onResponse(String response, String url) {
		

	}

}
