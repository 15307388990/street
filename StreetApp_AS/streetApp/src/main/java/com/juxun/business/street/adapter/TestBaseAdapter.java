package com.juxun.business.street.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.yl.ming.efengshe.R;
import com.juxun.business.street.adapter.BillAdapter.ViewHolder;
import com.juxun.business.street.bean.BillBean;

public class TestBaseAdapter extends BaseAdapter implements
		com.juxun.business.street.widget.addressbook.StickyListHeadersAdapter, SectionIndexer {

	/** 内容 */
	private List<BillBean> countries;
	/** head */
	private ArrayList<String> sections;

	private LayoutInflater inflater;
	private Context mContext;

	public TestBaseAdapter(Context context, List<BillBean> countries) {
		inflater = LayoutInflater.from(context);
		this.mContext = context;
		this.countries = countries;
	}
	
	public void changeDiscussonList(List<BillBean> data){
		if(data!=null){
			countries = data;
			notifyDataSetChanged();
		}
	}
	
	public void addData(List<BillBean> data){
			countries.addAll(data);
			notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return countries.size();
	}

	@Override
	public Object getItem(int position) {
		return countries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bill, null);
			holder.orderId = (TextView)convertView.findViewById(R.id.orderId);
			holder.money = (TextView)convertView.findViewById(R.id.money);
			holder.payMethod = (TextView)convertView.findViewById(R.id.payMethod);
			holder.payTime = (TextView)convertView.findViewById(R.id.payTime);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		BillBean bean = countries.get(position);
//		if(bean.getMonthCountMoney()>0){
//			holder.monthCountMoney.setVisibility(View.VISIBLE);
//			holder.monthCountMoney.setText(bean.getMonth()+mContext.getString(R.string.month_count)+" ￥"+bean.getMonthCountMoney());
//		}else{
//			holder.monthCountMoney.setVisibility(View.GONE);
//		}
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

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = inflater.inflate(R.layout.item_bill_title, parent, false);
			holder.monthCountMoney = (TextView) convertView.findViewById(R.id.monthCountMoney);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}
		BillBean bean = countries.get(position);
		holder.monthCountMoney.setText(bean.getMonth()+mContext.getString(R.string.month_count)+" ￥"+bean.getMonthCountMoney());
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		return position;
	}

	class HeaderViewHolder {
		public TextView monthCountMoney;
	}

	class ViewHolder {
		public TextView orderId;
		public TextView money;
		public TextView payMethod;
		public TextView payTime;
	}

	@Override
	public int getPositionForSection(int section) {
		if (section >= sections.size()) {
			section = sections.size() - 1;
		} else if (section < 0) {
			section = 0;
		}

		int position = 0;
		String sectionStr = sections.get(section);
		for (int i = 0; i < countries.size(); i++) {
			if (sectionStr.contains(countries.get(position).getMonthCountMoney()+"")) {
				position = i;
				break;
			}
		}
		return position;
	}

	@Override
	public int getSectionForPosition(int position) {
		if (position >= countries.size()) {
			position = countries.size() - 1;
		} else if (position < 0) {
			position = 0;
		}
		return sections.indexOf(countries.get(position).getMonth()+mContext.getString(R.string.month_count)+" ￥"+countries.get(position).getMonthCountMoney());
	}

	@Override
	public Object[] getSections() {
		return sections.toArray(new String[sections.size()]);
	}

	public void clearAll() {
		countries.clear();
		sections.clear();
	}

}
