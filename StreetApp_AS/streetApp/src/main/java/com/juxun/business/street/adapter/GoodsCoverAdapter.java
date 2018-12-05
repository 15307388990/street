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
import com.juxun.business.street.bean.EleGoodListBean;
import com.juxun.business.street.util.BitmapUtil;

public class GoodsCoverAdapter extends BaseAdapter {
	private Context context;
	private List<EleGoodListBean> goodslist;
	private LayoutInflater inflater;
	public GoodsCoverAdapter(Context context,List<EleGoodListBean> goodslist){
		this.context = context;
		this.goodslist = goodslist;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return goodslist.size();
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
		EleGoodListBean bean = goodslist.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.ele_goods_cover_item, null);
			holder.goods_cover = (ImageView) convertView.findViewById(R.id.goods_cover);
			holder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
			holder.goods_number = (TextView) convertView.findViewById(R.id.goods_number);
			holder.goods_catagory = (TextView) convertView.findViewById(R.id.goods_catagory);
			holder.goods_price = (TextView) convertView.findViewById(R.id.goods_price);
			convertView.setTag(holder);

		}else {
			holder  = (ViewHolder) convertView.getTag();
		}
		
		BitmapUtil.dispalyHttpBitmap(holder.goods_cover, bean.getCover(), context);
		holder.goods_name.setText(bean.getName());
		holder.goods_number.setText("x"+bean.getGoods_count());
		holder.goods_catagory.setText("规格："+bean.getDisplaySpecOrCategory());
		holder.goods_price.setText("¥"+bean.getTotal_price());
		return convertView;
	}
	class ViewHolder{
		public ImageView goods_cover;
		public TextView goods_name;
		public TextView goods_number;
		public TextView goods_catagory;
		public TextView goods_price;
	}

}
