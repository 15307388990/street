package com.juxun.business.street.adapter;


import java.text.DecimalFormat;
import java.util.List;

import com.juxun.business.street.bean.CommodityInfo2Bean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.Tools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Administrator InthesaleAdapter 出售中的商品适配
 * 
 */
public class InthesaleAdapter extends BaseAdapter {
	private List<CommodityInfo2Bean> list = null;
	private Context mContext;
	DecimalFormat df = new java.text.DecimalFormat("0.00");
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = ImageLoaderUtil.getOptions();
	private int commodityState = 0;// 如果值为4的时候显示撤销原因

	public InthesaleAdapter(Context mContext, List<CommodityInfo2Bean> list) {

		this.mContext = mContext;
		this.list = list;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<CommodityInfo2Bean> list, int commodityState) {
		this.commodityState = commodityState;
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

	@SuppressLint("ResourceAsColor")
	public View getView(final int position, View view, ViewGroup arg2) {
		final ViewHolder viewHolder;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.inthesale_itm, null);
			viewHolder.iv_img = (ImageView) view.findViewById(R.id.iv_img);
			viewHolder.iv_overdue = (ImageView) view.findViewById(R.id.iv_overdue);
			viewHolder.iv_expirated = (ImageView) view.findViewById(R.id.iv_expirated);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			viewHolder.tv_inventory = (TextView) view.findViewById(R.id.tv_inventory);
			viewHolder.tv_revoke_reason = (TextView) view.findViewById(R.id.tv_revoke_reason);
			viewHolder.tv_inventory_warning = (TextView) view.findViewById(R.id.tv_inventory_warning);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final CommodityInfo2Bean mallShoppingCartbean = list.get(position);

		viewHolder.tv_name.setText(mallShoppingCartbean.getCommodity_name());
		viewHolder.tv_revoke_reason.setText("撤销原因：" + mallShoppingCartbean.getRevokeReason());
		if (mallShoppingCartbean.getCommodity_price_low() == 0) {
			viewHolder.tv_price.setText("¥" + mallShoppingCartbean.getCommodity_price());
		} else {
			viewHolder.tv_price.setText("¥" + mallShoppingCartbean.getCommodity_price_low());
		}
		if (commodityState == 4 && mallShoppingCartbean.getRevokeReason() != null) {
			viewHolder.tv_revoke_reason.setVisibility(View.VISIBLE);
		} else {
			viewHolder.tv_revoke_reason.setVisibility(View.GONE);
		}

		viewHolder.tv_inventory.setText(mallShoppingCartbean.getCommodity_inventory() + "");
		imageLoader.displayImage(Constants.imageUrl + mallShoppingCartbean.getCommodity_icon(), viewHolder.iv_img,
				options);
		/***
		 * 判断是否要过期了
		 * 
		 */
		if (mallShoppingCartbean.isExpiration()) {
			viewHolder.tv_time.setText("过期日期：" + Tools.getDateformat(mallShoppingCartbean.getExpirationTime()));
			viewHolder.tv_time.setVisibility(View.VISIBLE);
			viewHolder.iv_overdue.setVisibility(View.VISIBLE);
		} else {
			viewHolder.tv_time.setVisibility(View.INVISIBLE);
			viewHolder.iv_overdue.setVisibility(View.GONE);
		}
		/***
		 * 判断是否已过期了
		 * 
		 */
		if (mallShoppingCartbean.isExpirated()) {
			viewHolder.iv_expirated.setVisibility(View.VISIBLE);
		} else {
			viewHolder.iv_expirated.setVisibility(View.GONE);
		}
		/***
		 * 判断是否库存不足
		 * 
		 */
		if (mallShoppingCartbean.isLessInventory()) {
			viewHolder.tv_inventory.setTextColor(mContext.getResources().getColor(R.color.red));
			viewHolder.tv_inventory_warning.setVisibility(View.VISIBLE);
		} else {
			viewHolder.tv_inventory.setTextColor(mContext.getResources().getColor(R.color.black));
			viewHolder.tv_inventory_warning.setVisibility(View.GONE);
		}
		return view;

	}

	final static class ViewHolder {
		/**
		 * 商品名称 商品价格 时间 库存 库存预警 撤销原因
		 */
		TextView tv_name, tv_price, tv_time, tv_inventory, tv_inventory_warning, tv_revoke_reason;
		ImageView iv_img, iv_overdue, iv_expirated;
	}

}