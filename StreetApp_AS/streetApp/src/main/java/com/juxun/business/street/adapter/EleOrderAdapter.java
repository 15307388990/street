package com.juxun.business.street.adapter;

import java.util.List;

import com.juxun.business.street.activity.EleOrderDeliveryActivity;
import com.juxun.business.street.activity.EleOrderDetailActivity;
import com.juxun.business.street.activity.ElectricitySupplierOrderActivity;
import com.juxun.business.street.activity.OrderLogisticsSearchActivity;
import com.juxun.business.street.bean.EleGoodListBean;
import com.juxun.business.street.bean.EleOrderBean;
import com.juxun.business.street.util.BitmapUtil;
import com.juxun.business.street.util.Tools;
import com.yl.ming.efengshe.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 
 * @author Lifucheng
 * 电商订单列表adapter
 *
 */
public class EleOrderAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<EleOrderBean> list;
	private int tag ;
	public EleOrderAdapter(Context context,List<EleOrderBean> list){
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	
	public void setList(List<EleOrderBean> list) {
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
		final EleOrderBean bean = list.get(position);
		if(convertView == null){
			holder = new ViewHolder();
			convertView  = inflater.inflate(R.layout.ele_order_list_item, null);
			holder.order_number = (TextView) convertView.findViewById(R.id.order_number);
			holder.order_state  = (TextView) convertView.findViewById(R.id.order_state);
			holder.goods_icon = (ImageView) convertView.findViewById(R.id.goods_icon);
			holder.total_price = (TextView) convertView.findViewById(R.id.total_price);
			holder.order_operation = (Button) convertView.findViewById(R.id.order_operation);
			holder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
			holder.ll_cover = (LinearLayout) convertView.findViewById(R.id.ll_cover);
			holder.hs = (HorizontalScrollView) convertView.findViewById(R.id.hs);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.order_number.setText(bean.getOrder_id());
		//状态订单状态【1.待付款,2.待发货,3.已发货,4.交易完成,5.已取消,6.已退货,7.临时单
		final int status = bean.getStatus();
		String strStatus = null;
		String nextAction = null;
		if(status == 1){
			strStatus = "待付款";
			nextAction = "";
		}else if(status == 2){
			strStatus = "待发货";
			nextAction = "发货";
		}else if(status == 3){
			strStatus = "已发货";
			nextAction = "订单追踪";
		}else if(status == 4){
			strStatus = "交易完成";
			nextAction="";
		}else if(status == 5){
			strStatus = "已取消";
			nextAction="";

		}else if(status == 6){
			strStatus = "已退货";
			nextAction="";

		}else if(status == 7){
			strStatus = "临时单";
			nextAction="";

		}
		holder.order_state.setText(strStatus);
		if(!TextUtils.isEmpty(nextAction)){
			holder.order_operation.setVisibility(View.VISIBLE);
			holder.order_operation.setText(nextAction);
			
				holder.order_operation.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
					    if(status == 2){
					    	Intent intent = new Intent(context,EleOrderDeliveryActivity.class);
					    	Bundle bundle = new Bundle();
					    	bundle.putSerializable("bean", bean);
					    	intent.putExtras(bundle);
					    	context.startActivity(intent);
					    	
					    }else if(status == 3){
					    	Intent intent = new Intent(context,OrderLogisticsSearchActivity.class);
							Bundle bundle = new Bundle();
					    	bundle.putSerializable("bean", bean);
					    	intent.putExtras(bundle);
					    	context.startActivity(intent);
					    	
					    }
						
					}
				});
			
		}else {
			holder.order_operation.setVisibility(View.INVISIBLE);
		}
		holder.total_price.setText(bean.getTotal_price());
		List<EleGoodListBean> goodslist = bean.getGoodslist();
		if(goodslist.size()>1){
			holder.ll_cover.removeAllViews();
			tag = position;
			holder.ll_cover.setVisibility(View.VISIBLE);
			holder.goods_icon.setVisibility(View.GONE);
			holder.goods_name.setVisibility(View.GONE);

			for(int i=0;i<goodslist.size();i++){
				EleGoodListBean goodListBean = goodslist.get(i);
				ImageView iv = new ImageView(context);
				
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(Tools.dip2px(context, 90),Tools.dip2px(context, 100));
				param.setMargins(0, 0, Tools.dip2px(context, 15), 0);
				iv.setLayoutParams(param);
				iv.setFocusable(false);
				iv.setClickable(false);
				iv.setFocusableInTouchMode(false);
				iv.setBackgroundResource(R.drawable.image_bg);
				iv.setPadding(Tools.dip2px(context, 1), Tools.dip2px(context, 1), Tools.dip2px(context, 1), Tools.dip2px(context, 1));
				iv.setScaleType(ScaleType.FIT_XY);
				BitmapUtil.dispalyHttpBitmap(iv, goodListBean.getCover(), context);
				holder.ll_cover.addView(iv);
				
			}
			holder.ll_cover.setOnClickListener(new MyOnClick(position));
		}else{
			holder.ll_cover.setVisibility(View.GONE);
			holder.goods_icon.setVisibility(View.VISIBLE);
			holder.goods_name.setVisibility(View.VISIBLE);
			EleGoodListBean listBean = goodslist.get(0);
			BitmapUtil.dispalyHttpBitmap(holder.goods_icon, listBean.getCover(), context);
			holder.goods_name.setText(listBean.getName());
			
		}
		
		
		return convertView;
	}
	class ViewHolder{
		
		public TextView order_number;
		public TextView order_state;
		public ImageView goods_icon;
		public TextView total_price;
		public Button order_operation;
		public TextView goods_name;
		public LinearLayout ll_cover;
		public HorizontalScrollView hs;
		
	}
	
	private class MyOnClick implements OnClickListener{
		private int position;
		public MyOnClick(int position) {
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			EleOrderBean bean = list.get(position);
			Intent intent = new Intent(context, EleOrderDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("bean", bean);
			intent.putExtras(bundle);
			context.startActivity(intent);
		}
	}

}
