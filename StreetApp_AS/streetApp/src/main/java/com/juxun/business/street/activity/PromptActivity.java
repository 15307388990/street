/**
 * 
 */
package com.juxun.business.street.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 
 * @version 重置密码提示框
 * 
 */
public class PromptActivity extends BaseActivity {
	@ViewInject(R.id.btn_colse)
	private Button btn_colse;
	@ViewInject(R.id.btn_ok)
	private Button btn_ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prompt);
		ViewUtils.inject(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/** 单击事件 */
	@OnClick({ R.id.btn_colse, R.id.btn_ok})
	public void clickMethod(View v) {
		switch (v.getId()) {
		case R.id.btn_colse:
			finish();
			break;
		case R.id.btn_ok:
			// 设置修改密码
			Intent intent = new Intent(PromptActivity.this,MobilePhoneActivity.class);
			intent.putExtra("type", 2);
			startActivity(intent);
			finish();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.optInt("status");
			if (stauts == 0) {
			} else if (stauts < 0) {
				Tools.dealErrorMsg(this, url, stauts, json.optString("msg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
		}
	}

	static class HideClick extends Thread {
		public static volatile int sIsAlive = 0;

		@Override
		public void run() {
			sIsAlive++;
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (sIsAlive > 0) {
				sIsAlive--;
			}
			super.run();

		}
	}
}
