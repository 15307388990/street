package com.juxun.business.street.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * @author luoming 忘记密码
 */

public class ForgotPasswordActivity extends BaseActivity {

	@ViewInject(R.id.et_login_account)
	private EditText etLoginAccount; // 用户名
	@ViewInject(R.id.login)
	private Button login; // 登陆
	private String Account;//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot);
		ViewUtils.inject(this);
		initTitle();
		title.setText("忘记密码");
		Account = getIntent().getStringExtra("Account");
		etLoginAccount.setText(Account);
		etLoginAccount.setSelection(Account.length());
		login.setEnabled(initBtn());
		etLoginAccount.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				login.setEnabled(initBtn());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	/** 单击事件 */
	@OnClick({ R.id.login })
	public void clickMethod(View v) {
		if (v.getId() == R.id.login) {
			findPhoneByAccount();
		}
	}

	private boolean initBtn() {
		if (etLoginAccount.getText().length() >= 1) {
			login.setBackgroundResource(R.drawable.button_bg1);
			login.setTextColor(getResources().getColor(android.R.color.white));
			return true;
		} else {
			login.setBackgroundResource(R.drawable.button_bg);
			login.setTextColor(getResources().getColor(R.color.gray));
			return false;
		}

	}

	/* 查找帐号是否存在以及返回安全手机号 */
	public void findPhoneByAccount() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("account", etLoginAccount.getText().toString());
		mQueue.add(ParamTools.packParam(Constants.findPhoneByAccount, this, this, map));
	}

	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.optInt("status");
			String msg = json.optString("msg");
			if (stauts == 0) {
				String phoneString = json.optString("data");
				Intent intent = new Intent(ForgotPasswordActivity.this, ForgotPassword2Activity.class);
				intent.putExtra("phone", phoneString);
				intent.putExtra("Account", etLoginAccount.getText().toString());
				startActivityForResult(intent, 1);
			} else {
				Tools.showToast(this, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				finish();
			}
		}
	}

}
