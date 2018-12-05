package com.juxun.business.street.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yl.ming.efengshe.R;
import com.juxun.business.street.bean.BillBean;
import com.juxun.business.street.bean.CouponBean;
import com.juxun.business.street.util.BitmapUtil;

/**
 * @author WuJianHua
 * 账单列表适配器
 */
public class BillAdapter extends BaseAdapter {
	private Context mContext;
	private List<BillBean> dataSet;

	public BillAdapter(Context context , List<BillBean> dataSet){
		mContext = context ;
		this.dataSet = dataSet;
	}
	
	public void changeDiscussonList(List<BillBean> data){
		if(data!=null){
			dataSet = data;
			notifyDataSetChanged();
		}
	}
	
	public void addData(List<BillBean> data){
			dataSet.addAll(data);
			notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return dataSet.size();
	}

	@Override
	public BillBean getItem(int position) {
		return dataSet.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView,
			ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bill, null);
			holder.monthCountMoney = (TextView)convertView.findViewById(R.id.monthCountMoney);
			holder.orderId = (TextView)convertView.findViewById(R.id.orderId);
			holder.money = (TextView)convertView.findViewById(R.id.money);
			holder.payMethod = (TextView)convertView.findViewById(R.id.payMethod);
			holder.payTime = (TextView)convertView.findViewById(R.id.payTime);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		BillBean bean = dataSet.get(position);
		if(bean.getMonthCountMoney()>0){
			holder.monthCountMoney.setVisibility(View.VISIBLE);
			holder.monthCountMoney.setText(bean.getMonth()+mContext.getString(R.string.month_count)+" ¥"+bean.getMonthCountMoney());
		}else{
			holder.monthCountMoney.setVisibility(View.GONE);
		}
		holder.orderId.setText(bean.getOrder_id()+"");
		holder.money.setText(bean.getOrder_price() == 0 ? "+"+bean.getTotal_price() : "+"+bean.getOrder_price());
		if(bean.getOrder_pay_type() == 1){
			holder.payMethod.setText(R.string.ali_pay);
		}else if(bean.getOrder_pay_type() == 2){
			holder.payMethod.setText(R.string.weixin_pay);
		}else{
			holder.payMethod.setText(R.string.yinlian);
		}
		if(bean.getOrder_end_date()!=null){
			holder.payTime.setText(bean.getOrder_end_date());
		}else if(bean.getOrder_creat_date()!=null){
			holder.payTime.setText(bean.getOrder_creat_date());
		}else if(bean.getOrderCreateTime()!=null){
			holder.payTime.setText(bean.getOrderCreateTime());
		}else{
			holder.payTime.setText("");
		}
		return convertView;
	}
	
	class ViewHolder{
		public TextView monthCountMoney;
		public TextView orderId;
		public TextView money;
		public TextView payMethod;
		public TextView payTime;
	}
}
