package com.juxun.business.street.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.juxun.business.street.bean.BankBranchs;
import com.yl.ming.efengshe.R;

import java.util.List;

/**
 * 
 * @author luoming 支行适配器
 *
 */
public class BnakAdapter extends BaseAdapter  {
	private List<BankBranchs> list = null;
	private Context mContext;

	public BnakAdapter(Context mContext, List<BankBranchs> list) {
		this.mContext = mContext;
		this.list = list;
	}

	/**
	 * 当ListView数据发生变化�?,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<BankBranchs> list) {
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
		ViewHolder viewHolder = null;
		final BankBranchs mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.city_item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		// Boolean hasShow = false;
		// String sortLetters = mContent.getSortLetters();
		// for(int i = 0;i<position;i++)
		// {
		// if (sortLetters == list.get(i).getSortLetters() )
		// hasShow = true;
		// }
		//
		// if (!hasShow) {
		// viewHolder.tvLetter.setVisibility(View.VISIBLE);
		// viewHolder.tvLetter.setText(mContent.getSortLetters());
		// } else {
		// viewHolder.tvLetter.setVisibility(View.GONE);
		// }

		// 根据position获取分类的首字母的char ascii?
		viewHolder.tvLetter.setVisibility(View.GONE);
		viewHolder.tvTitle.setText(this.list.get(position).getBranch_bank_name());

		return view;
	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
	}


}