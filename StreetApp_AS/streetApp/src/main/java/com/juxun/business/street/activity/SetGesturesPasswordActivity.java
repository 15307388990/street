/**
 * 
 */
package com.juxun.business.street.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.LockPatternView;
import com.juxun.business.street.widget.LockPatternView.Cell;
import com.juxun.business.street.widget.LockPatternView.DisplayMode;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 
 * 
 * @version 设置手势密码
 * 
 */
public class SetGesturesPasswordActivity extends BaseActivity
		implements LockPatternView.OnPatternListener, OnClickListener {
	@ViewInject(R.id.tv_tilte)
	private TextView tv_tilte;
	@ViewInject(R.id.lock_pattern)
	private LockPatternView lock_pattern;

	private static final int STEP_1 = 1; // 开始
	private static final int STEP_2 = 2; // 第一次设置手势完成
	private static final int STEP_3 = 3; // 按下继续按钮
	private static final int STEP_4 = 4; // 第二次设置手势完成
	private int step;

	private List<Cell> choosePattern;

	private boolean confirm = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_gestures_password);
		ViewUtils.inject(this);
		Tools.acts.add(this);
		initTitle();
		title.setText("设置手势密码");
		initView();
	}

	private void initView() {
		lock_pattern.setOnPatternListener(this);
		step = STEP_1;
		updateView();
	}

	

	private void updateView() {
		switch (step) {
		case STEP_1:
			choosePattern = null;
			confirm = false;
			lock_pattern.clearPattern();
			lock_pattern.enableInput();
			break;
		case STEP_2:
			lock_pattern.disableInput();
			tv_tilte.setText("请再次确认手势密码");
			step = STEP_3;
			updateView();
			break;
		case STEP_3:
			lock_pattern.clearPattern();
			lock_pattern.enableInput();
			break;
		case STEP_4:
			if (confirm) {
				lock_pattern.disableInput();
				mSavePreferencesData.putStringData("lock_key", LockPatternView.patternToString(choosePattern));
				Tools.showToast(SetGesturesPasswordActivity.this, "手势密码设置成功");
				finish();
			} else {
				lock_pattern.setDisplayMode(DisplayMode.Wrong);
				lock_pattern.enableInput();
				tv_tilte.setText("两次绘制图片不一样");
			}

			break;

		default:
			break;
		}
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
		tv_tilte.setText("完成后松开手指");
		if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
			tv_tilte.setText("至少连接4个点，请重试");
			lock_pattern.setDisplayMode(DisplayMode.Wrong);
			return;
		}

		if (choosePattern == null) {
			choosePattern = new ArrayList<Cell>(pattern);
			// Log.d(TAG, "choosePattern = "+choosePattern.toString());
			// Log.d(TAG, "choosePattern.size() = "+choosePattern.size());
			step = STEP_2;
			updateView();
			return;
		}
		// [(row=1,clmn=0), (row=2,clmn=0), (row=1,clmn=1), (row=0,clmn=2)]
		// [(row=1,clmn=0), (row=2,clmn=0), (row=1,clmn=1), (row=0,clmn=2)]

		if (choosePattern.equals(pattern)) {
			// Log.d(TAG, "pattern = "+pattern.toString());
			// Log.d(TAG, "pattern.size() = "+pattern.size());

			confirm = true;
		} else {
			confirm = false;
		}

		step = STEP_4;
		updateView();
	}

}
