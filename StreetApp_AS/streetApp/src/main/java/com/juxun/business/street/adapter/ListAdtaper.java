package com.juxun.business.street.adapter;

import java.util.List;

import com.juxun.business.street.bean.Msgmodel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdtaper extends BaseAdapter {
	private List<Msgmodel> list = null;
	private Context mContext;

	public ListAdtaper(Context mContext, List<Msgmodel> list) {
		this.list = list;
		this.mContext = mContext;
	};

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		TextView textView = new TextView(mContext);
		textView.setText(list.get(arg0).getCommodityName());
		textView.setTextColor(mContext.getResources().getColor(
				android.R.color.black));
		return textView;
	}

}
