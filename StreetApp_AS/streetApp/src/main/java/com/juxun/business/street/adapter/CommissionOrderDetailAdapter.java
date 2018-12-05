package com.juxun.business.street.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.MallShoppingCartMode;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

/**
 * 
 * @author Administrator 分佣订单详情列表
 * 
 */
public class CommissionOrderDetailAdapter extends BaseAdapter {
	private List<MallShoppingCartMode> list = null;
	private Context mContext;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = ImageLoaderUtil.getOptions();
	DecimalFormat df = new java.text.DecimalFormat("0.00");

	public CommissionOrderDetailAdapter(Context mContext, List<MallShoppingCartMode> list) {

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
			view = LayoutInflater.from(mContext).inflate(R.layout.commission_order_item, null);
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_pirce);
			viewHolder.tv_number = (TextView) view.findViewById(R.id.tv_number);
			viewHolder.tv_specNames = (TextView) view.findViewById(R.id.tv_specNames);
			viewHolder.tv_fy_text = (TextView) view.findViewById(R.id.tv_fy_text);
			viewHolder.tv_fy_price = (TextView) view.findViewById(R.id.tv_fy_price);
			viewHolder.imageView = (ImageView) view.findViewById(R.id.iv_img);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final MallShoppingCartMode msgmodel = list.get(position);

		imageLoader.displayImage(Constants.imageUrl + msgmodel.getCommodityICon(), viewHolder.imageView, options);
		viewHolder.tv_name.setText(msgmodel.getCommodityName());
		viewHolder.tv_specNames.setText(msgmodel.getSpecNames());
		viewHolder.tv_price.setText(msgmodel.getPrice() + "元");
		viewHolder.tv_number.setText("x" + msgmodel.getGoodsCount());
		viewHolder.tv_fy_text.setText("总价¥" + df.format(msgmodel.getTotal_price()) + "  分佣比例："
				+ msgmodel.getCommission() * 100 + "%" + "  分佣收入");
		viewHolder.tv_fy_price.setText("¥" + df.format(msgmodel.getFyhj()));
		return view;

	}

	final static class ViewHolder {
		/**
		 * 
		 */
		TextView tv_name, tv_price, tv_number, tv_specNames, tv_fy_text, tv_fy_price;
		ImageView imageView;
	}

}