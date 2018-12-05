/**
 * 
 */
package com.juxun.business.street.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yl.ming.efengshe.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @author WuJianHua
 * 退出登录弹框
 */
public class DownlineTipsDialogActivity extends Activity{
	
	@ViewInject(R.id.title)
	private TextView title; //标题
	@ViewInject(R.id.content)
	private TextView content; //内容
	@ViewInject(R.id.sure)
	private TextView sure; //确认
	@ViewInject(R.id.cancel)
	private TextView cancel; //取消
	@ViewInject(R.id.divder_v)
	private TextView divder_v; //分割线
	private boolean isdownlineView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_tips);
		ViewUtils.inject(this);
		isdownlineView = getIntent().getBooleanExtra("isdownlineView", false);
		if(isdownlineView){
			divder_v.setVisibility(View.GONE);
			cancel.setVisibility(View.GONE);
		}else{
			title.setText(R.string.update_tips);
			content.setText(R.string.update_tips_content);
		}
	}
	
	/** 单击事件 */
	@OnClick({R.id.sure, R.id.cancel})  
	public void clickMethod(View v) {
		Intent intent = new Intent();
		if (v.getId() == R.id.sure) {
			intent.putExtra("isSure", true);
			setResult(Activity.RESULT_OK, intent);
			finish();
		} else if (v.getId() == R.id.cancel) {
			intent.putExtra("isSure", false);
			setResult(Activity.RESULT_CANCELED, intent);
			finish();
		}
	}
}
