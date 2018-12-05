package com.juxun.business.street.adapter;


import java.util.List;

import com.juxun.business.street.bean.LogisticeDetailsModel;
import com.yl.ming.efengshe.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import me.nereo.multi_image_selector.bean.Image;

/**
 * 
 * @author Administrator 物流详情适配
 * 
 */
public class LogisticsDetalisAdapter extends BaseAdapter {
	private List<LogisticeDetailsModel> list = null;
	private Context mContext;

	public LogisticsDetalisAdapter(Context mContext, List<LogisticeDetailsModel> list) {
		this.mContext = mContext;
		this.list = list;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<LogisticeDetailsModel> list) {
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
			view = LayoutInflater.from(mContext).inflate(R.layout.logisticsdatalis_item, null);
			viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			viewHolder.tv_context = (TextView) view.findViewById(R.id.tv_context);
			viewHolder.tv_img = (ImageView) view.findViewById(R.id.tv_img);
			viewHolder.tv_xu = (TextView) view.findViewById(R.id.tv_xu);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		if (position == 0) {
			viewHolder.tv_img.setImageResource(R.drawable.location_buttonh);
		} else {
			viewHolder.tv_img.setImageResource(R.drawable.location_button);
		}
		if (position == list.size() - 1) {
			viewHolder.tv_xu.setVisibility(View.GONE);
		} else {
			viewHolder.tv_xu.setVisibility(View.VISIBLE);
		}

		viewHolder.tv_context.setText(list.get(position).getContext());
		viewHolder.tv_time.setText(list.get(position).getTime());
		return view;

	}

	final static class ViewHolder {
		// 时间 内容
		TextView tv_time, tv_context, tv_xu;
		ImageView tv_img;
	}

}