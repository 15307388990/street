package com.juxun.business.street.fragment;

import android.app.Activity;
import android.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.juxun.business.street.bean.PartnerBean;
import com.juxun.business.street.bean.StoreBean;
import com.juxun.business.street.util.SavePreferencesData;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.dialog.LoadDialog;

/**
 * @author 罗富�? Activity 基类
 */

public abstract class BaseFragment extends Fragment implements Listener<String>, ErrorListener {
	public SavePreferencesData mSavePreferencesData;
	private LoadDialog mloading;
	public RequestQueue mQueue; // 请求列队
	public Activity mcontext;
	protected PartnerBean partnerBean;
	
	public BaseFragment() {
		super();
	}

	public BaseFragment(Activity context) {
		this.mcontext = context;
		mSavePreferencesData = new SavePreferencesData(context);
		mQueue = Volley.newRequestQueue(context);
		initAdmin();
	}

	/** 弹出等待�? */
	public void loading() {
		if (mloading == null) {
			mloading = new LoadDialog(mcontext, "请稍后...");
			mloading.setCanceledOnTouchOutside(false);
		}
		if (mloading.isShowing()) {
			mloading.dismiss();
		}
		mloading.show();
	}

	/** 隐藏等待�? */
	public void dismissLoading() {
		if (mloading == null || !mloading.isShowing()) {
			return;
		}
		mloading.dismiss();
	}

	public void initAdmin() {
		String jsonString = mSavePreferencesData.getStringData("json");
		if (!jsonString.isEmpty()) {
			partnerBean = JSON.parseObject(jsonString, PartnerBean.class);
			partnerBean.setAuth_token(mSavePreferencesData.getStringData("auth_token"));
		}else {
			partnerBean=new PartnerBean();
		}
	};

	@Override
	public void onErrorResponse(VolleyError error) {
		dismissLoading();
		Tools.showToast(mcontext, "网络连接异常");
	}

	public abstract void onResponse(String response, String url);
}
