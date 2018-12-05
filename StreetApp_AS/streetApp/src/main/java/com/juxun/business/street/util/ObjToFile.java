package com.juxun.business.street.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.juxun.business.street.bean.Order;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by George on 16/2/26.
 */
public class ObjToFile {
	private static final String fileName = "OrderList.txt";
	private static final String Obj = "Order_List";
	private static final String OBJ_KEY = "orderList";
	private Context mContext;

	public ObjToFile(Context context) {
		this.mContext = context;
	}

	public void writeObject(Order order) {
		SharedPreferences preferences = mContext.getSharedPreferences(fileName, Context.MODE_APPEND);

		SharedPreferences.Editor editor = preferences.edit();
		String str;
		if (TextUtils.isEmpty(readObject())) {
			str = order.toString();
		} else {
			str = readObject() + "," + order.toString();
		}
		editor.putString(Obj, str);
		editor.commit();
	}

	public String readObject() {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(fileName, Context.MODE_APPEND);

		String objs = sharedPreferences.getString(Obj, "");
		return objs;
	}

	public List<Order> getObjList() {
		List<Order> datas = null;
		String str = "{\"" + OBJ_KEY + "\":[" + readObject() + "]}";
		try {
			JSONObject jsonObject = new JSONObject(str);
			JSONArray jsonArray = jsonObject.getJSONArray(OBJ_KEY);
			datas = new ArrayList<Order>();
			int len = jsonArray.length();
			for (int i = 0; i < len; i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				Gson gson = new Gson();
				Order order = gson.fromJson(object.toString(), Order.class);
				if (order != null) {
					datas.add(order);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return datas;
	}
}
