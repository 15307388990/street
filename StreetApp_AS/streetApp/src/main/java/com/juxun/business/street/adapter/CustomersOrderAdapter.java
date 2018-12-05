package com.juxun.business.street.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yl.ming.efengshe.R;
import com.juxun.business.street.bean.QuanTeamBuyOrderBean;
import com.juxun.business.street.util.BitmapUtil;
/**
 * 团购全部adapter
 * @author Lifucheng
 * **/
public class CustomersOrderAdapter extends BaseAdapter{
	private List<QuanTeamBuyOrderBean> list;
	private Context context;
	private LayoutInflater inflater;
	public CustomersOrderAdapter(Context context,List<QuanTeamBuyOrderBean> list) {
		this.list  = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
    
	public void setList(List<QuanTeamBuyOrderBean> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
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
		QuanTeamBuyOrderBean bean = list.get(position);
		if(convertView == null){
			holder = new ViewHolder();
			convertView  = inflater.inflate(R.layout.order_whole_item, null);
			holder.order_icon = (ImageView) convertView.findViewById(R.id.order_icon);
			holder.order_name = (TextView) convertView.findViewById(R.id.order_name);
			holder.order_num = (TextView) convertView.findViewById(R.id.order_num);
			holder.order_price = (TextView) convertView.findViewById(R.id.order_price);
			holder.order_state = (TextView) convertView.findViewById(R.id.order_state);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		BitmapUtil.dispalyHttpBitmap(holder.order_icon, bean.getOrder_icon(), context);
		holder.order_name.setText(bean.getOrder_name());
		holder.order_num.setText(bean.getOrder_num()+"张");
		holder.order_price.setText("¥"+bean.getOrder_price());
		//订单状态：1，成功；2，付款失败；3，未付款；4，已退款
		int state = bean.getOrder_state();
		if(state == 1){
			holder.order_state.setText("已付款");
			holder.order_state.setTextColor(context.getResources().getColor(R.color.order_state_success));
			holder.order_state.setBackgroundResource(R.drawable.order_paid_success);
		}else if(state == 2){
			holder.order_state.setText("付款失败");
			holder.order_state.setTextColor(context.getResources().getColor(R.color.order_state_failure));
			holder.order_state.setBackgroundResource(R.drawable.order_paid_failure);
		}else if(state == 3){
			holder.order_state.setText("未付款");
			holder.order_state.setTextColor(context.getResources().getColor(R.color.order_state_failure));
			holder.order_state.setBackgroundResource(R.drawable.order_paid_failure);
		}else if(state == 4){
			holder.order_state.setText("已退款");
			holder.order_state.setTextColor(context.getResources().getColor(R.color.order_state_failure));
			holder.order_state.setBackgroundResource(R.drawable.order_paid_failure);
		}
		return convertView;
	}
	
	static class ViewHolder{
		public ImageView order_icon;
		public TextView order_name;
		public TextView order_num;
		public TextView order_price;
		public TextView order_state;
	}

}
