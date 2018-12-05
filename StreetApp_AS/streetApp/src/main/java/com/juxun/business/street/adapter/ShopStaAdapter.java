package com.juxun.business.street.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxun.business.street.bean.ShopStaBean;
import com.juxun.business.street.bean.ShopStaBean.TopBean;
import com.juxun.business.street.bean.ShopStaBean.TopChannel;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

public class ShopStaAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Object> mListDatas;
	private ArrayList<Bitmap> mBitmaps;

	public ShopStaAdapter(Context context) {
		this.mContext = context;
		initBitmaps();
	}

	private void initBitmaps() {
		Bitmap b1 = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.statistics_sign_no1);
		Bitmap b2 = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.statistics_sign_no2);
		Bitmap b3 = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.statistics_sign_no3);
		Bitmap b4 = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.statistics_sign_no4);
		Bitmap b5 = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.statistics_sign_no5);
		mBitmaps = new ArrayList<Bitmap>();
		mBitmaps.add(b1);
		mBitmaps.add(b2);
		mBitmaps.add(b3);
		mBitmaps.add(b4);
		mBitmaps.add(b5);
	}

	public void updateAdapter(ArrayList<Object> listDatas) {
		this.mListDatas = listDatas;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mListDatas == null ? 0 : mListDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mListDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_shop_sta, null);

			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 获取对象
		holder.iv_icon.setImageBitmap(mBitmaps.get(position));
		Object object = mListDatas.get(position);
		if (object instanceof TopBean) {
			TopBean topBean = (TopBean) object;
			holder.tv_name.setText(topBean.getCommodity_name());
			holder.tv_num.setText(topBean.getMsg_count());
		} else  if (object instanceof ShopStaBean.TopBean2) {
			ShopStaBean.TopBean2 topBean = (ShopStaBean.TopBean2) object;
			holder.tv_name.setText(topBean.getCommodityName());
			holder.tv_num.setText(topBean.getMsg_count());
		} else {
			TopChannel topChanel = (TopChannel) object;
			String f_channel_name = topChanel.getF_channel_name();
			String channel_name = topChanel.getChannel_name();

			if (f_channel_name == null) {
				holder.tv_name.setText(channel_name);
			} else if (channel_name == null) {
				holder.tv_name.setText(f_channel_name);
			} else {
				holder.tv_name.setText(f_channel_name + ">" + channel_name);
			}
			holder.tv_num.setText(topChanel.getGcount());
		}

		return convertView;
	}

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name, tv_num;
	}

}
