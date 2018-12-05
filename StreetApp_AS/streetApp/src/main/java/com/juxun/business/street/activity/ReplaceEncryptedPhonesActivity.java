/**
 * 
 */
package com.juxun.business.street.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.MyCountTimer;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.dialog.AuthCodeDialog;
import com.juxun.business.street.widget.dialog.AuthCodeDialog.onConfirmListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 
 * @version 更换密保手机
 * 
 */
public class ReplaceEncryptedPhonesActivity extends BaseActivity implements TextWatcher, onConfirmListener {
	@ViewInject(R.id.tv_phone)
	private TextView tv_phone;// 旧手机号
	@ViewInject(R.id.et_code_one)
	private EditText et_code_one;// 旧手机验证码
	@ViewInject(R.id.tv_code_one)
	private TextView tv_code_one;// 旧手机获取验证码

	@ViewInject(R.id.et_phone)
	private EditText et_phone;// 新手机号
	@ViewInject(R.id.et_code_two)
	private EditText et_code_two;// 新手机验证码
	@ViewInject(R.id.tv_code_two)
	private TextView tv_code_two;// 新手机获取验证码

	@ViewInject(R.id.btn_next)
	private Button btn_next;// 下一步
	private MyCountTimer countTimer = null;
	private AuthCodeDialog authCodeDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_replace_encrypted_phone);
		ViewUtils.inject(this);
		Tools.acts.add(this);
		initTitle();
		title.setText("更换手机号");
		initView();
	}

	private void initView() {
		tv_phone.setText(Tools.pNumber(partnerBean.getSafe_phone()));
		et_code_one.addTextChangedListener(this);
		et_code_two.addTextChangedListener(this);
		et_phone.addTextChangedListener(this);
		tv_code_two.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendResCode();
			}
		});
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// sendResCode();
			}
		});
		btn_next.setEnabled(initBtn());
	}

	private void sendResCode() {
		if (!tv_code_two.getText().toString().equals("获取验证码")) {
			return;
		}
		if (et_phone.getText().toString().length() == 0) {
			Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!Tools.isMobileNum(et_phone.getText().toString())) {
			Toast.makeText(this, "手机号格式有误", Toast.LENGTH_SHORT).show();
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", et_phone.getText().toString());
		mQueue.add(ParamTools.packParam(Constants.sendPhoneMsg, this, this, map));
		loading();
	}

	@Override
	protected void onResume() {
		super.onResume();
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
			Log.i("test", response);
			int status = json.getInt("status");
			String msg = json.getString("msg");
			if (status == 0) {
				if (url.contains(Constants.sendAdminSettingPhoneMsg)) {
					// TODO Auto-generated method stub
					if (authCodeDialog != null && authCodeDialog.isShowing()) {
						authCodeDialog.dismiss();
					}
					countTimer.start();// 开启定时器
					tv_code_two.setVisibility(View.VISIBLE);

				} else if (url.contains(Constants.verificationAdminSettingPhone)) {
					String phone_token = json.getString("phone_token");
					Intent intent = new Intent(ReplaceEncryptedPhonesActivity.this, ModifyPasswordActivity2.class);
					intent.putExtra("token", phone_token);
					startActivity(intent);
				}
			} else if (status == -1) {
				if (url.contains(Constants.sendAdminSettingPhoneMsg)) {
					authCodeDialog = new AuthCodeDialog(getApplicationContext(), ReplaceEncryptedPhonesActivity.this,
							partnerBean.getSafe_phone());
					authCodeDialog.showAtLocation(btn_next, Gravity.CENTER, 0, 0);
				} else {
					Tools.showToast(getApplicationContext(), msg);
				}

			} else if (status == -4004) {
				mSavePreferencesData.putStringData("json", "");
				Tools.jump(ReplaceEncryptedPhonesActivity.this, LoginActivity.class, false);
				Tools.showToast(ReplaceEncryptedPhonesActivity.this, "登录过期请重新登录");
				Tools.acts.clear();
			} else {
				if (url.contains(Constants.sendAdminSettingPhoneMsg)) {
					if (authCodeDialog != null) {
						authCodeDialog.Refresh();
					}
				}
				Tools.showToast(getApplicationContext(), msg);
			}
		} catch (JSONException e) {
			// TODO: handle exception
			Tools.showToast(getApplicationContext(), "解析数据错误");
		}

	}


	@Override
	public void onConfirm(int id, String verifyCode) {
		if (id == R.id.btn_ok) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("auth_token", partnerBean.getAuth_token());
			map.put("image_code", verifyCode);
			mQueue.add(ParamTools.packParam(Constants.sendAdminSettingPhoneMsg, this, this, map));
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		btn_next.setEnabled(initBtn());
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	private boolean initBtn() {
		if (et_code_one.getText().length() > 0 && et_phone.getText().length() > 0
				&& et_code_two.getText().length() > 0) {
			btn_next.setBackgroundResource(R.drawable.button_bg1);
			btn_next.setTextColor(getResources().getColor(R.color.white));
			return true;
		} else {
			btn_next.setBackgroundResource(R.drawable.button_bg);
			btn_next.setTextColor(getResources().getColor(R.color.jiujiujiu));
			return false;
		}

	}

}
