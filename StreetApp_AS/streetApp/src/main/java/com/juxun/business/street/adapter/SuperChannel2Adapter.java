package com.juxun.business.street.adapter;


import java.util.List;

import com.juxun.business.street.bean.ChannelPDBean;
import com.yl.ming.efengshe.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @author Administrator 分类列表
 * 
 */
public class SuperChannel2Adapter extends BaseAdapter {
	private List<ChannelPDBean> list = null;
	private Context mContext;
	private int type;// 0代表酵 1代表元素

	public SuperChannel2Adapter(Context mContext, List<ChannelPDBean> list, int type) {
		this.mContext = mContext;
		this.list = list;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<ChannelPDBean> list, int type) {
		this.list = list;
		this.type = type;
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
			view = LayoutInflater.from(mContext).inflate(R.layout.super_channel_item, null);
			viewHolder.tv_name = (TextView) view.findViewById(R.id.channel_name);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		if (type == 0) {
			if (position == selectItem) {
				viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.blue));
				viewHolder.tv_name.setBackgroundResource(R.color.white);
			} else {
				viewHolder.tv_name.setTextColor(Color.BLACK);
				viewHolder.tv_name.setBackgroundResource(R.drawable.channel_nomal);
			}
		} else {
			if (position == selectItem) {
				viewHolder.tv_name.setTextColor(mContext.getResources().getColor(R.color.blue));
				viewHolder.tv_name.setBackgroundResource(R.color.white);
			} else {
				viewHolder.tv_name.setTextColor(Color.BLACK);
				viewHolder.tv_name.setBackgroundResource(R.drawable.super_chanal_btn);
			}
		}

		viewHolder.tv_name.setText(list.get(position).getChannelName());
		return view;

	}

	final static class ViewHolder {
		TextView tv_name;
	}

	public void setSelectItem(int selectItem) {
		this.selectItem = selectItem;
		notifyDataSetChanged();
	}

	private int selectItem = 0;

}