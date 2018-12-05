package com.juxun.business.street.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.yl.ming.efengshe.R;
import com.juxun.business.street.config.Constants;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AboutActivity extends BaseActivity {
	
	@ViewInject(R.id.webview)
	private WebView webview; //网页区
	
	private boolean isAboult;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		ViewUtils.inject(this);
		initTitle();
		isAboult = getIntent().getBooleanExtra("isAboult", false);
		if(isAboult){
			title.setText(R.string.about);
		}else{
			title.setText(R.string.problem);
		}
		String url = isAboult ? Constants.aboutUrl : Constants.problemUrl;
		webview.setWebViewClient(new WebViewClient() { // 通过webView打开链接，不调用系统浏览器
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		WebSettings webSettings = webview.getSettings();  
		webSettings.setJavaScriptEnabled(true); 
		webSettings.setBuiltInZoomControls(true);
		webview.loadUrl(url);
	}

	@Override
	public void onResponse(String response, String url) {
		
	}
}
