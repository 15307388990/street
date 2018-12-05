package com.juxun.business.street.util;

import java.util.Map;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.juxun.business.street.config.Constants;

/**
 * @author WuJianHua 参数配置工具类
 */
public class ParamTools {
	/** 生成参数 */
	public static StringRequest packParam(String url,
			Listener<String> listener, ErrorListener errorListener,
			final Map<String, String> map) {
		if (!url.contains("http")) {
			url = Constants.mainUrl + url;
		}
		String string = "";
		for (Map.Entry<String, String> entry : map.entrySet()) {
			string = string + "&" + entry.getKey() + "=" + entry.getValue();
		}
		System.out.println("url" + url + string);
		StringRequest stringRequest = new StringRequest(Method.POST, url,
				listener, errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return map;
			}
		};
		stringRequest.setCharset("UTF-8");
		stringRequest
				.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
		return stringRequest;
	}

	private static long lastClickTime;

	/**
	 * 
	 * @return
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 800) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

}
