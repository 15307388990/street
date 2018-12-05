package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author 台卡设置
 *
 */

public class DeccaSetActivity extends BaseActivity {
	@ViewInject(R.id.rb_d0)
	private RadioButton rb_d0;// d0模式
	@ViewInject(R.id.rb_d1)
	private RadioButton rb_d1;// d1模式
	@ViewInject(R.id.rg_mu)
	private RadioGroup rg_mu;//
	@ViewInject(R.id.btn_next)
	private Button btn_next;// 下一步

	private int decometer_type = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decca_set);
		ViewUtils.inject(this);
		Tools.acts.add(this);
		btn_next.setEnabled(initBtn());
		initTitle();
		title.setText("台卡设置");
		initView();
	}

	private void initView() {
		rg_mu.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_d0) {
					decometer_type = 0;
				} else {
					decometer_type = 1;
				}
				btn_next.setEnabled(initBtn());
			}
		});
	}

	/** 单击事件 */
	@OnClick({ R.id.btn_next })
	public void clickMethod(View v) {
		if (v.getId() == R.id.btn_next) {
			Intent intent = new Intent();
			intent.setClass(DeccaSetActivity.this, AddAccountActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int status = json.getInt("status");
			String msg = json.getString("msg");
			if (status == 0) {
				if (url.contains(Constants.register)) {
				}
			} else {
				Tools.showToast(getApplicationContext(), msg);
			}
		} catch (JSONException e) {
			Tools.showToast(getApplicationContext(), "解析数据错误");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private boolean initBtn() {
		if (decometer_type != -1) {
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
