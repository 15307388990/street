package com.juxun.business.street.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.MD5Util;
import com.juxun.business.street.util.MyCountTimer;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.dialog.AuthCodeDialog.onConfirmListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

/**
 * 
 * @author luoming 忘记密码 获取验证码
 */

public class ForgotPassword2Activity extends BaseActivity implements
		onConfirmListener, TextWatcher {

	@ViewInject(R.id.et_code)
	private EditText et_code;// 验证码
	@ViewInject(R.id.tv_code)
	private TextView tv_code;// 获取验证码
	@ViewInject(R.id.login)
	private Button login; // 确定
	@ViewInject(R.id.et_password)
	private EditText et_password;
	@ViewInject(R.id.cb_img)
	private CheckBox cb_img;
	private String phone;//
	private String Account;// 账号
	// private AuthCodeDialog authCodeDialog;
	private MyCountTimer countTimer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot2);
		ViewUtils.inject(this);
		initTitle();
		title.setText("忘记密码");
		phone = getIntent().getStringExtra("phone");
		Account = getIntent().getStringExtra("Account");
		countTimer = new MyCountTimer(this, tv_code, "获取验证码",
				R.color.tab_text_color_select, R.color.jiujiujiu);
		login.setEnabled(initBtn());
		et_code.addTextChangedListener(this);
		et_password.addTextChangedListener(this);

		// authCodeDialog = new AuthCodeDialog(getApplicationContext(),
		// ForgotPassword2Activity.this, phone);
	}

	private boolean initBtn() {
		if (et_code.getText().length() >= 1
				&& et_password.getText().length() >= 1) {
			login.setBackgroundResource(R.drawable.button_bg1);
			login.setTextColor(getResources().getColor(android.R.color.white));
			return true;
		} else {
			login.setBackgroundResource(R.drawable.button_bg);
			login.setTextColor(getResources().getColor(R.color.gray));
			return false;
		}
	}

	/** 单击事件 */
	@OnClick({ R.id.login, R.id.tv_code, R.id.cb_img })
	public void clickMethod(View v) {
		if (v.getId() == R.id.login) {
			if (et_password.getText().length() > 5
					&& et_password.getText().length() < 19) {
				updatePassWord();
			} else {
				Tools.showToast(ForgotPassword2Activity.this, "密码不符合要求");
			}

		} else if (v.getId() == R.id.tv_code) {
			sendResCode();
		} else if (v.getId() == R.id.cb_img) {
			if (cb_img.isChecked()) {
				// 选择状态 显示明文--设置为可见的密码
				et_password
						.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				et_password.setSelection(et_password.getText().toString()
						.length());
			} else {
				// 默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT |
				// InputType.TYPE_TEXT_VARIATION_PASSWORD
				et_password.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				et_password.setSelection(et_password.getText().toString()
						.length());
			}

		}
	}

	private void sendResCode() {
		if (!tv_code.getText().toString().equals("获取验证码")) {
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", phone);
		map.put("msgCode", et_code.getText().toString());
		map.put("password", et_password.getText().toString());
		map.put("account", Account);
		mQueue.add(ParamTools
				.packParam(Constants.sendPhoneMsg, this, this, map));
		loading();
	}

	/* 查找帐号是否存在以及返回安全手机号 */
	public void updatePassWord() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", phone);
		map.put("msgCode", et_code.getText().toString());
		String password = MD5Util
				.getMD5String(et_password.getText().toString());
		map.put("password", password);
		map.put("account", Account);
		mQueue.add(ParamTools.packParam(Constants.updatePassWord, this, this,
				map));
	}

	@SuppressLint("NewApi")
	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.optInt("status");
			String msg = json.optString("msg");
			if (stauts == 0) {
				if (url.contains(Constants.sendPhoneMsg)) {
					// if (authCodeDialog != null && authCodeDialog.isShowing())
					// {
					// authCodeDialog.dismiss();
					// }

					if (mTempDialog != null && mTempDialog.isShowing()) {
						mTempDialog.dismiss();
					}
					countTimer.start();// 开启定时器
					tv_code.setVisibility(View.VISIBLE);

				} else if (url.contains(Constants.updatePassWord)) {
					Tools.showToast(getApplicationContext(), "密码修改成功");
					setResult(RESULT_OK);
					finish();
				}
			} else if (stauts == -1) {
				if (url.contains(Constants.sendPhoneMsg)) {
					// authCodeDialog.showAtLocation(login, Gravity.CENTER, 0,
					// 0);

					showMsgDialog();
				} else {
					Tools.showToast(getApplicationContext(), msg);
				}

			} else {
				if (url.contains(Constants.sendPhoneMsg)) {
					// if (authCodeDialog != null) {
					// authCodeDialog.Refresh();
					// }
					
					if (mTempDialog != null) {
						refreshMsg();
					}
				}
				Tools.showToast(getApplicationContext(), msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
		}
	}

	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = ImageLoaderUtil.getOptions();
	private String mUrlimg;
	private AlertDialog mTempDialog;
	private EditText mEt_code;
	private ImageView mIv_code;

	private void showMsgDialog() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// 控件初始化
		View view = LayoutInflater.from(this)
				.inflate(R.layout.msg_dialog, null);
		mIv_code = (ImageView) view.findViewById(R.id.iv_code);
		TextView tv_retry = (TextView) view.findViewById(R.id.tv_retry);
		Button btn_colse = (Button) view.findViewById(R.id.btn_colse);
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		mEt_code = (EditText) view.findViewById(R.id.et_code);

		mUrlimg = Constants.mainUrl + Constants.imageValidStream + "?phone="
				+ phone + "&time=" + System.currentTimeMillis();
		imageLoader.displayImage(mUrlimg, mIv_code, options);
		builder.setView(view);
		mTempDialog = builder.create();
		mTempDialog.show();

		// 控件点击事件
		tv_retry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				refreshMsg();
			}
		});
		btn_colse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTempDialog.dismiss();
			}
		});
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String etMsg = mEt_code.getText().toString();
				if (!TextUtils.isEmpty(etMsg)) {
					confirmMsg(etMsg);
				} else {
					Toast.makeText(ForgotPassword2Activity.this, "验证码不能为空", 1)
							.show();
				}
			}
		});

	}

	private void refreshMsg() {
		mUrlimg = Constants.mainUrl + Constants.imageValidStream + "?phone="
				+ phone + "&time=" + System.currentTimeMillis();

		imageLoader.displayImage(mUrlimg, mIv_code, options);
		mEt_code.setText("");
	}

	@Override
	public void onConfirm(int id, String verifyCode) {
		if (id == R.id.btn_ok) {
			confirmMsg(verifyCode);
		}

	}

	private void confirmMsg(String verifyCode) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", phone);
		map.put("image_code", verifyCode);
		mQueue.add(ParamTools
				.packParam(Constants.sendPhoneMsg, this, this, map));
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		login.setEnabled(initBtn());
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

}
