/**
 * 
 */
package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.LockPatternView;
import com.juxun.business.street.widget.LockPatternView.Cell;
import com.juxun.business.street.widget.LockPatternView.DisplayMode;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 
 * 
 * @version 手势登录
 * 
 */
public class GesturesLoginActivity extends BaseActivity implements LockPatternView.OnPatternListener, OnClickListener {
	@ViewInject(R.id.tv_tilte)
	private TextView tv_tilte;
	@ViewInject(R.id.lock_pattern)
	private LockPatternView lock_pattern;

	private List<Cell> lockPattern;
	private int type;//0为登录时候显示的页面 1为修改手势密码时候显示的页面

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_gestures_password);
		ViewUtils.inject(this);
		Tools.acts.add(this);
		initTitle();
		type=getIntent().getIntExtra("type", 0);
		if (type==0) {
			title.setText("身份验证");
			back.setVisibility(View.INVISIBLE);
		}else if (type==1) {
			title.setText("验证手势密码");
		}
	
		initView();
	}

	private void initView() {
		
		lock_pattern.setOnPatternListener(this);
		lockPattern=LockPatternView.stringToPattern(mSavePreferencesData.getStringData("lock_key"));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// disable back key
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/** 单击事件 */
	@OnClick({})
	public void clickMethod(View v) {
		switch (v.getId()) {
		case R.id.exit:
			Intent intent1 = new Intent(this, LoginOutDialogActivity.class);
			startActivityForResult(intent1, 100);
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
				if (url.contains(Constants.updateBusinessRange)) {
				}
			} else if (stauts < 0) {
				Tools.dealErrorMsg(this, url, stauts, json.optString("msg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPatternStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPatternCleared() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPatternCellAdded(List<Cell> pattern) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPatternDetected(List<Cell> pattern) {
		if (pattern.equals(lockPattern)) {
			if (type==0) {
				Tools.jump(this, MainActivity.class, true);
			}else if (type==1) {
				Tools.jump(this, SetGesturesPasswordActivity.class, true);
			}
			
		} else {
			lock_pattern.setDisplayMode(DisplayMode.Wrong);
			tv_tilte.setText("密码验证错误");
		}

	}

}
