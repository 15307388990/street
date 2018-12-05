package com.juxun.business.street.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.juxun.business.street.activity.CommissionToDayAcitivity;
import com.juxun.business.street.bean.Analysisbean;
import com.juxun.business.street.bean.Msgmodel;
import com.juxun.business.street.bean.ParseModel;
import com.yl.ming.efengshe.R;

/**
 * 
 * @author Administrator 分类列表
 * 
 */
public class CommissionAdapter extends BaseAdapter {
	private List<Analysisbean> list = null;
	private Context mContext;
	SimpleDateFormat siFormat = new SimpleDateFormat("yyyy-MM-dd");

	public CommissionAdapter(Context mContext, List<Analysisbean> list) {

		this.mContext = mContext;
		this.list = list;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<Analysisbean> list) {
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
		final ViewHolder viewHolder;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.commission_itm, null);
			viewHolder.tv_date = (TextView) view.findViewById(R.id.tv_time);
			viewHolder.tv_margin = (TextView) view.findViewById(R.id.tv_margin);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final Analysisbean analysisbean = list.get(position);

		viewHolder.tv_date.setText(siFormat.format(analysisbean.getOrder_date()));
		viewHolder.tv_margin.setText("" + analysisbean.getOrder_profit() / 100);

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, CommissionToDayAcitivity.class);
				intent.putExtra("start_date", siFormat.format(analysisbean.getOrder_date()));
				mContext.startActivity(intent);

			}
		});
		return view;

	}

	final static class ViewHolder {
		/**
		 * 商品名称 商品价格 下单时间 展开 收起
		 */
		TextView tv_date, tv_margin;
	}

}