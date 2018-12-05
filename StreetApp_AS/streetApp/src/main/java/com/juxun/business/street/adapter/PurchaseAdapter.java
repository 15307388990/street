package com.juxun.business.street.adapter;


import java.text.DecimalFormat;
import java.util.List;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.PurchaseBean;
import com.juxun.business.street.bean.PurchaseDetailsBean;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author Administrator PurchaseAdapter 供应商首页适配
 * 
 */
public class PurchaseAdapter extends BaseAdapter {
	private List<PurchaseBean> list = null;
	private Context mContext;
	DecimalFormat df = new java.text.DecimalFormat("0.00");
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = ImageLoaderUtil.getOptions();
	private int index;

	public PurchaseAdapter(Context mContext, List<PurchaseBean> list) {

		this.mContext = mContext;
		this.list = list;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<PurchaseBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		if (list.size() == 0)
			return 0;
		if (list.size() <= 3) {
			return 1;
		} else {
			return (int) Math.ceil((double) list.size() / 3);
		}
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
			view = LayoutInflater.from(mContext).inflate(R.layout.purchase_itm, null);
			viewHolder.tv_text1 = (TextView) view.findViewById(R.id.tv_text1);
			viewHolder.tv_text2 = (TextView) view.findViewById(R.id.tv_text2);
			viewHolder.tv_text3 = (TextView) view.findViewById(R.id.tv_text3);
			viewHolder.tv_price1 = (TextView) view.findViewById(R.id.tv_price1);
			viewHolder.tv_price2 = (TextView) view.findViewById(R.id.tv_price2);
			viewHolder.tv_price3 = (TextView) view.findViewById(R.id.tv_price3);
			viewHolder.iv_img1 = (ImageView) view.findViewById(R.id.iv_img1);
			viewHolder.iv_img2 = (ImageView) view.findViewById(R.id.iv_img2);
			viewHolder.iv_img3 = (ImageView) view.findViewById(R.id.iv_img3);
			viewHolder.ll_layout1 = (LinearLayout) view.findViewById(R.id.ll_layout1);
			viewHolder.ll_layout2 = (LinearLayout) view.findViewById(R.id.ll_layout2);
			viewHolder.ll_layout3 = (LinearLayout) view.findViewById(R.id.ll_layout3);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		if (position == 0) {
			index = position;
		} else {
			index = position * 3;
		}
		viewHolder.tv_text1.setText(list.get(index).getCommodityName());
		if (list.get(index).getPriceLow() != list.get(index).getPriceHigh()) {
			viewHolder.tv_price1.setText("¥" + list.get(index).getPriceLow() + "-" + list.get(index).getPriceHigh());
		} else {
			viewHolder.tv_price1.setText("¥" + list.get(index).getPriceLow());
		}
		String[] icon = list.get(index).getCommodityICon().split(",");
		imageLoader.displayImage(Constants.imageUrl + icon[0], viewHolder.iv_img1, options);
		viewHolder.ll_layout1.setTag(list.get(index).getId());

		if (index + 1 < list.size()) {
			viewHolder.tv_text2.setText(list.get(index + 1).getCommodityName());
			if (list.get(index + 1).getPriceLow() != list.get(index + 1).getPriceHigh()) {
				viewHolder.tv_price2
						.setText("¥" + list.get(index + 1).getPriceLow() + "-" + list.get(index + 1).getPriceHigh());
			} else {
				viewHolder.tv_price2.setText("¥" + list.get(index + 1).getPriceLow());
			}
			String[] icon2 = list.get(index + 1).getCommodityICon().split(",");
			imageLoader.displayImage(Constants.imageUrl + icon2[0], viewHolder.iv_img2, options);
			viewHolder.tv_text2.setVisibility(View.VISIBLE);
			viewHolder.tv_price2.setVisibility(View.VISIBLE);
			viewHolder.ll_layout2.setTag(list.get(index + 1).getId());
			viewHolder.iv_img2.setVisibility(View.VISIBLE);
		} else {
			viewHolder.tv_text2.setVisibility(View.GONE);
			viewHolder.tv_price2.setVisibility(View.GONE);
			viewHolder.iv_img2.setVisibility(View.GONE);
		}

		if (index + 2 < list.size()) {
			viewHolder.tv_text3.setText(list.get(index + 2).getCommodityName());
			viewHolder.tv_price3
					.setText("¥" + list.get(index + 2).getPriceLow() + "-" + list.get(index + 2).getPriceHigh());
			if (list.get(index + 2).getPriceLow() != list.get(index + 2).getPriceHigh()) {
				viewHolder.tv_price3
						.setText("¥" + list.get(index + 2).getPriceLow() + "-" + list.get(index + 2).getPriceHigh());
			} else {
				viewHolder.tv_price3.setText("¥" + list.get(index + 2).getPriceLow());
			}
			String[] icon3 = list.get(index + 2).getCommodityICon().split(",");
			imageLoader.displayImage(Constants.imageUrl + icon3[0], viewHolder.iv_img3, options);
			viewHolder.tv_text3.setVisibility(View.VISIBLE);
			viewHolder.tv_price3.setVisibility(View.VISIBLE);
			viewHolder.ll_layout3.setTag(list.get(index + 2).getId());
			viewHolder.iv_img3.setVisibility(View.VISIBLE);
		} else {
			viewHolder.tv_text3.setVisibility(View.GONE);
			viewHolder.tv_price3.setVisibility(View.GONE);
			viewHolder.iv_img3.setVisibility(View.GONE);

		}
		viewHolder.ll_layout1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, PurchaseDetailsBean.class);
				intent.putExtra("id", (Integer) v.getTag());
				mContext.startActivity(intent);

			}
		});
		viewHolder.ll_layout2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, PurchaseDetailsBean.class);
				intent.putExtra("id", (Integer) v.getTag());
				mContext.startActivity(intent);
			}
		});
		viewHolder.ll_layout3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, PurchaseDetailsBean.class);
				intent.putExtra("id", (Integer) v.getTag());
				mContext.startActivity(intent);
			}
		});
		return view;

	}

	final static class ViewHolder {
		/**
		 * 商品名称 商品价格 时间 库存 库存预警
		 */
		TextView tv_text1, tv_text2, tv_text3;
		TextView tv_price1, tv_price2, tv_price3;
		ImageView iv_img1, iv_img2, iv_img3;
		LinearLayout ll_layout1, ll_layout2, ll_layout3;
	}

}