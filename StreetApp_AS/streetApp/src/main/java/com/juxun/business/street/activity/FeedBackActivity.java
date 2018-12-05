package com.juxun.business.street.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

/**
 * 
 * @author luoming 意见反馈
 *
 */
public class FeedBackActivity extends BaseActivity {
	@ViewInject(R.id.et_feedback_name)
	private EditText et_feedback_name;
	@ViewInject(R.id.et_feedback_detail)
	private EditText et_feedback_detail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		ViewUtils.inject(this);
		initTitle();
		title.setText("意见反馈");
		more.setText("提交");
		more.setVisibility(View.VISIBLE);
		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendFeedBack();
			}
		});
	}

	@Override
	public void overridePendingTransition(int enterAnim, int exitAnim) {
		// TODO Auto-generated method stub
		super.overridePendingTransition(android.R.anim.fade_in, exitAnim);
	}

	private void sendFeedBack() {
		if (et_feedback_name.getText().length() == 0) {
			Tools.showToast(this, "请补充完整信息");
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("memberId", partnerBean.getAuth_token() + "");
		map.put("contact", et_feedback_detail.getText().toString());// 联系方式
		map.put("opinion", et_feedback_name.getText().toString());// 意见详情
		// mQueue.add(ParamTools.packParam(cont.addFeedBack, this, this, map));
		loading();
	}

	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int resultCode = json.getInt("resultCode");
			String msg = json.getString("resultMsg");

			if (resultCode == 0) {
				// if (url.contains(Const.addFeedBack)) {
				// Tools.showToast(this, "提交成功！");
				// finish();
				// }

			} else if (resultCode == -4004) {
				mSavePreferencesData.putStringData("json", "");
				Tools.jump(this, LoginActivity.class, true);
				Tools.showToast(this, "登录过期请重新登录");
			} else {
				Tools.showToast(getApplicationContext(), msg);
			}
		} catch (JSONException e) {
			// TODO: handle exception
			Tools.showToast(getApplicationContext(), "解析数据错误");
		}
	}
}
