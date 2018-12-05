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

public class ApplyActivity extends BaseActivity {
	
	@ViewInject(R.id.webview)
	private WebView webview; //网页区

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		ViewUtils.inject(this);
		initTitle();
		title.setText(R.string.apply);
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
		webview.loadUrl(Constants.settled);
	}

	@Override
	public void onResponse(String response, String url) {
		
	}
	
	/*@ViewInject(R.id.nameEdit)
	private EditText nameEdit; //名字
	@ViewInject(R.id.contactEdit)
	private EditText contactEdit; //联系方式
	@ViewInject(R.id.shopAddrEdit)
	private EditText shopAddrEdit; //联系方式
	@ViewInject(R.id.settled)
	private Button settled; //入驻
	
	private String name, contact, shopAddr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply);
		ViewUtils.inject(this);
		initTitle();
		title.setText(R.string.street);
	}
	
	*//** 单击事件 *//*
	@OnClick({ R.id.hotLine, R.id.settled })
    public void clickMethod(View v) {  
        if(v.getId() == R.id.login){
        	if(checkParams()){
        		toSettld(name, contact, shopAddr);
        	}
        }
    }
	
	  执行入驻操作 
	public void toSettld(String name, String contact, String shopAddr){
		Map<String , String> map = new HashMap<String, String>();
		map.put("name", name);
		map.put("contact", contact);
		map.put("shopAddr", shopAddr);
//		mQueue.add(ParamTools.packParam(Constants.login, this, this, map));
		loading();
	}
	
	*//** 检测参数合法性 *//*
	public boolean checkParams(){
		name = nameEdit.getText().toString();
		contact = contactEdit.getText().toString();
		shopAddr = shopAddrEdit.getText().toString();
		if(name == null || contact == null || shopAddr == null || name.length() == 0 || contact.length() == 0 || shopAddr.length() == 0){
			Tools.showToast(this, R.string.tips_param_error);
			return false;
		}
		return true;
	}

	@Override
	public void onResponse(String response, String url) {
		
	}*/
}
