/**
 * 
 */
package com.juxun.business.street.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yl.ming.efengshe.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @author WuJianHua 找车弹框
 */
public class HotLineDialogActivity extends Activity {

	@ViewInject(R.id.tips)
	private TextView tips; // 提示
	@ViewInject(R.id.hotLine)
	private TextView hotLine; // 鸣笛
	@ViewInject(R.id.cancel)
	private TextView cancel; // 取消
	@ViewInject(R.id.dismissView)
	private TextView dismissView; //半透明区域

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_hotline);
		ViewUtils.inject(this);
		if(getIntent().getBooleanExtra("flag", false)){
			tips.setText(R.string.work_time1);
		}
		overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
	}

	/** 单击事件 */
	@OnClick({ R.id.hotLine, R.id.cancel, R.id.dismissView })
	public void clickMethod(View v) {
		switch (v.getId()) {
		case R.id.cancel:

			break;
		case R.id.hotLine:
			//用intent启动拨打电话  
            Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+"400-629-9903"));  
            startActivity(intent);  
			break;
		}
		finish();
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
	}
}
