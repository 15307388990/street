package com.juxun.business.street.adapter;

import java.util.List;

import com.baidu.mapapi.search.core.PoiInfo;
import com.yl.ming.efengshe.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @author Administrator 定位周边地名
 *
 */
public class LocationNameAdapter extends BaseAdapter {
	private List<PoiInfo> list = null;
	private Context mContext;

	public LocationNameAdapter(Context mContext, List<PoiInfo> list) {
		this.mContext = mContext;
		this.list = list;
	}

	public void updateListView(List<PoiInfo> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.location_name_item, null);
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_name.setText(list.get(position).name);
		return view;

	}

	final static class ViewHolder {
		TextView tv_name;
	}

}