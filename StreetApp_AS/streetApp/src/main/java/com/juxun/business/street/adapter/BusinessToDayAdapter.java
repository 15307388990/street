package com.juxun.business.street.adapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.juxun.business.street.activity.BusinessOrderAcitivity;
import com.juxun.business.street.bean.Analysisbean;
import com.juxun.business.street.bean.Msgmodel;
import com.juxun.business.street.bean.ParseModel;
import com.yl.ming.efengshe.R;

/**
 * 
 * @author Administrator 分类列表
 * 
 */
public class BusinessToDayAdapter extends BaseAdapter {
	private List<Analysisbean> list = null;
	private Context mContext;
	DecimalFormat df = new java.text.DecimalFormat("0.00");

	public BusinessToDayAdapter(Context mContext, List<Analysisbean> list) {

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
			view = LayoutInflater.from(mContext).inflate(R.layout.business_itm, null);
			viewHolder.tv_date = (TextView) view.findViewById(R.id.tv_time);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.tv_state = (TextView) view.findViewById(R.id.tv_state);
			viewHolder.tv_margin = (TextView) view.findViewById(R.id.tv_margin);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final Analysisbean analysisbean = list.get(position);
		viewHolder.tv_price.setText("" + analysisbean.getOrder_price()/100);
		SimpleDateFormat siFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
		viewHolder.tv_date.setText(analysisbean.getOrder_id() + "\n" + siFormat.format(analysisbean.getOrder_date()));
		viewHolder.tv_state.setText("" + df.format(analysisbean.getOrder_cost()/100));
		viewHolder.tv_margin.setText("" + analysisbean.getOrder_profit()/100);

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(mContext, BusinessOrderAcitivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("analysisbean", analysisbean);
				intent.putExtras(bundle);
				mContext.startActivity(intent);

			}
		});
		return view;

	}

	final static class ViewHolder {
		/**
		 * 商品名称 商品价格 下单时间 展开 收起
		 */
		TextView tv_date, tv_price, tv_state, tv_margin;
	}

}