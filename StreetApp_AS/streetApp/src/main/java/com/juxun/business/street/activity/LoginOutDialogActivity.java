/**
 * 
 */
package com.juxun.business.street.activity;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;
import com.yl.ming.efengshe.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * @author WuJianHua 登出提示框
 */
public class LoginOutDialogActivity extends BaseActivity {

	@ViewInject(R.id.tips)
	private TextView tips; // 提示
	@ViewInject(R.id.hotLine)
	private TextView hotLine; // 退出
	@ViewInject(R.id.cancel)
	private TextView cancel; // 取消
	@ViewInject(R.id.dismissView)
	private TextView dismissView; // 半透明区域

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_hotline);
		ViewUtils.inject(this);
		tips.setText(R.string.login_out_tips);
		hotLine.setText(R.string.login_out);
		overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
	}

	/** 单击事件 */
	@OnClick({ R.id.hotLine, R.id.cancel, R.id.dismissView })
	public void clickMethod(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.hotLine:
			setResult(RESULT_OK);
			mSavePreferencesData.putStringData("json", "");
			// 清空手势密码
			mSavePreferencesData.putStringData("lock_key", "");
			mSavePreferencesData.putBooleanData("isGesturesPassword", false);
			finish();
			break;
		}
		finish();
	}

	/* 解绑推送服务 */
	private void posUnReg() {
		CloudPushService pushService = PushServiceFactory.getCloudPushService();
		// 用QID 绑定别名
		pushService.unbindAccount(new CommonCallback() {

			@Override
			public void onSuccess(String arg0) {
				// TODO Auto-generated method stub
				Log.i("bind", "解绑成功" + arg0);
			}

			@Override
			public void onFailed(String arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.i("bind", "解绑失败" + arg0 + "---" + arg1);
			}
		});
	}

	@Override
	public void finish() {
		super.finish();
		posUnReg();
		overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
		MobclickAgent.onProfileSignOff();
	}

	@Override
	public void onResponse(String response, String url) {
		// TODO Auto-generated method stub

	}
}
