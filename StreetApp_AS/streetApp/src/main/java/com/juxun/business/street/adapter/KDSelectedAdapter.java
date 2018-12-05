package com.juxun.business.street.adapter;

import com.yl.ming.efengshe.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
public class KDSelectedAdapter extends BaseAdapter {
	private Context context;
	private String[] arr;
	private LayoutInflater inflater;
	public KDSelectedAdapter(Context context,String[] arr){
		this.arr = arr;
		this.context = context;
		inflater = inflater.from(context);
	}

	@Override
	public int getCount() {
		
		return arr.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.kd_selected_item, null);
		TextView tv = (TextView) view.findViewById(R.id.kd_company);
		tv.setText(arr[position]);
		return view;
	}

	

}
