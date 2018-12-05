package com.juxun.business.street.adapter;

import java.text.DecimalFormat;
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

import com.juxun.business.street.bean.MallShoppingCartMode;
import com.juxun.business.street.bean.Msgmodel;
import com.juxun.business.street.bean.ParseModel;
import com.yl.ming.efengshe.R;

/**
 * 
 * @author Administrator 订单盈利详情 分类列表
 * 
 */
public class BusinessOrderAdapter extends BaseAdapter {
	private List<MallShoppingCartMode> list = null;
	private Context mContext;
	DecimalFormat df = new java.text.DecimalFormat("0.00");

	public BusinessOrderAdapter(Context mContext, List<MallShoppingCartMode> list) {

		this.mContext = mContext;
		this.list = list;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<MallShoppingCartMode> list) {
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
			view = LayoutInflater.from(mContext).inflate(R.layout.business_order_itm, null);
			viewHolder.tv_date = (TextView) view.findViewById(R.id.tv_time);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.tv_state = (TextView) view.findViewById(R.id.tv_state);
			viewHolder.tv_margin = (TextView) view.findViewById(R.id.tv_margin);
			viewHolder.tv_ly = (TextView) view.findViewById(R.id.tv_ly);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final MallShoppingCartMode mallShoppingCartbean = list.get(position);

		viewHolder.tv_date.setText(mallShoppingCartbean.getCommodityName());
		viewHolder.tv_price.setText("" + mallShoppingCartbean.getGoodsCount());
		viewHolder.tv_state.setText(df.format(mallShoppingCartbean.getTotal_price()));
		viewHolder.tv_margin.setText(df.format(mallShoppingCartbean.getZongjj()));
		viewHolder.tv_ly.setText(df.format(mallShoppingCartbean.getZongly()));

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Intent intent = new Intent(mContext,
				// ExtractableDetailActivity.class);
				// intent.putExtra("withdraw_id", Analysisbean.getId());
				// mContext.startActivity(intent);

			}
		});
		return view;

	}

	final static class ViewHolder {
		/**
		 * 商品名称 商品价格 下单时间 展开 收起
		 */
		TextView tv_date, tv_price, tv_state, tv_margin, tv_ly;
	}

}