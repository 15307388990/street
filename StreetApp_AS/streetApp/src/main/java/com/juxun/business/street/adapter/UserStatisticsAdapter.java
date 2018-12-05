package com.juxun.business.street.adapter;

import java.util.List;

import com.juxun.business.street.bean.UserStatisticsBean;
import com.juxun.business.street.util.Tools;
import com.yl.ming.efengshe.R;

import android.content.Context;
import android.text.style.UpdateAppearance;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserStatisticsAdapter extends BaseAdapter {

	private Context mContext;
	private List<UserStatisticsBean> lists;

	public UserStatisticsAdapter(Context context, List<UserStatisticsBean> lists) {
		this.mContext = context;
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

	public void Update(List<UserStatisticsBean> lists) {
		this.lists = lists;
		notifyDataSetInvalidated();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.userstatistics_itm, null);
			holder.tv_iphone = (TextView) convertView.findViewById(R.id.tv_iphone);
			holder.tv_chongzhi = (TextView) convertView.findViewById(R.id.tv_chongzhi);
			holder.tv_xiaofei = (TextView) convertView.findViewById(R.id.tv_xiaofei);
			holder.tv_jine = (TextView) convertView.findViewById(R.id.tv_jine);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_iphone.setText(Tools.pNumber(lists.get(position).getMember_phone()));
		holder.tv_chongzhi.setText(Tools.getFenYuan(lists.get(position).getRecharge_money())+"");
		holder.tv_xiaofei.setText(Tools.getFenYuan(lists.get(position).getUse_money())+"");
		holder.tv_jine.setText(Tools.getFenYuan(lists.get(position).getBalance_money())+"");
		return convertView;
	}

	static class ViewHolder {
		TextView tv_iphone, tv_chongzhi, tv_xiaofei, tv_jine;

	}

}
