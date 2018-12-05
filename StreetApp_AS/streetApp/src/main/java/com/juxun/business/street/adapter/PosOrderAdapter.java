package com.juxun.business.street.adapter;

import java.util.List;

import com.yl.ming.efengshe.R;
import com.juxun.business.street.bean.PosOrderBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PosOrderAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater inflater;
	private List<PosOrderBean> lists;
	public PosOrderAdapter(Context context,List<PosOrderBean> lists){
		this.context = context;
		this.lists = lists;
		inflater = LayoutInflater.from(context);
	}
	

	public void setLists(List<PosOrderBean> lists) {
		this.lists = lists;
	}


	@Override
	public int getCount() {
		return lists.size();
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
		ViewHolder holder = null;
		PosOrderBean bean = lists.get(position);
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.pos_order_item_list, null);
			holder.order_number  = (TextView) convertView.findViewById(R.id.order_number);
			holder.order_price = (TextView) convertView.findViewById(R.id.order_price);
			holder.trading_hours  = (TextView) convertView.findViewById(R.id.trading_hours);
			holder.payments = (TextView) convertView.findViewById(R.id.payments);
			convertView.setTag(holder);
			
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.order_number.setText(bean.getOrder_id());
		holder.order_price.setText("¥"+bean.getOrder_price());
		holder.trading_hours.setText(bean.getOrder_end_date());
		int type = bean.getOrder_pay_type();
		String str = null;
		if(type == 1){
			str = "支付宝";
		}else if(type == 2){
			str = "微信";
		}else if(type == 3){
			str = "银联支付";
		}
		holder.payments.setText(str);
		return convertView;
	}
	class ViewHolder{
		public TextView order_number;
		public TextView order_price;
		public TextView trading_hours;
		public TextView payments;
	}

}
