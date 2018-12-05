package com.juxun.business.street.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxun.business.street.bean.ShopingCartBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 
 * @author Administrator ShopingCartAdapter 购物车适配器
 * 
 */
public class ShopingCartAdapter extends BaseAdapter {
	private List<ShopingCartBean> list = null;
	private Activity mContext;
	DecimalFormat df = new java.text.DecimalFormat("0.00");
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = ImageLoaderUtil.getOptions();
	private onConfirmListener listener = null;

	public ShopingCartAdapter(Activity mContext, List<ShopingCartBean> list, onConfirmListener listener) {
		this.mContext = mContext;
		this.list = list;
		this.listener = listener;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<ShopingCartBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return list.size();
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
			view = LayoutInflater.from(mContext).inflate(R.layout.shopping_item, null);
			viewHolder.add_chekbox = (CheckBox) view.findViewById(R.id.add_chekbox);
			viewHolder.iv_img = (ImageView) view.findViewById(R.id.icon);
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_shopname);
			viewHolder.tv_number = (EditText) view.findViewById(R.id.et_number);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.tv_heji = (TextView) view.findViewById(R.id.tv_heji);
			viewHolder.ll_add = (LinearLayout) view.findViewById(R.id.ll_add);
			viewHolder.ll_min = (LinearLayout) view.findViewById(R.id.ll_min);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final ShopingCartBean shopingCartBean = list.get(position);
		viewHolder.tv_name.setText(shopingCartBean.getCommodityName());
		viewHolder.tv_price.setText("¥" + shopingCartBean.getPrice());
		viewHolder.tv_number.setText(shopingCartBean.getGoodsCount() + "");
		
		String[] cover = shopingCartBean.getCommodityICon().split(",");
		imageLoader.displayImage(Constants.imageUrl + cover[0], viewHolder.iv_img, options);
		viewHolder.ll_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int quntity = shopingCartBean.getGoodsCount() + 1;
				viewHolder.tv_number.setText(quntity + "");
			}
		});
		viewHolder.ll_min.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (shopingCartBean.getGoodsCount() == 1) {
					AlertDialog.Builder builder = new Builder(mContext);
					builder.setMessage("确认要删除吗？");
					builder.setTitle("提示");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							shopingCartBean.setGoodsCount(0);
							listener.onConfirm(shopingCartBean);
						}
					});
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// do nothing
						}
					});
					builder.create().show();
				} else {
					int quntity = shopingCartBean.getGoodsCount() - 1;
					viewHolder.tv_number.setText(quntity + "");
				}

			}
		});
		viewHolder.tv_number.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewHolder.tv_number.setText(viewHolder.tv_number.getText().toString());
				viewHolder.tv_number.selectAll();
				viewHolder.tv_number.requestFocus();
				viewHolder.tv_number.setFocusable(true);
				viewHolder.tv_number.setEnabled(true);
				viewHolder.tv_number.setFocusableInTouchMode(true);
				viewHolder.tv_number.requestFocus();
				viewHolder.tv_number.findFocus();
			}	
		});
		viewHolder.tv_number.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				((ViewGroup) v.getParent()).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
				return false;
			}
		});
		view.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				((ViewGroup) v).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
				return false;
			}
		});
		
		return view;

	}

	public interface onConfirmListener {
		public void onConfirm(ShopingCartBean shopingCartBean);
	}

	final static class ViewHolder {
		/**
		 * 多选按钮 图片 名称 价格 数量 减少 数量 增加
		 */
		CheckBox add_chekbox;
		ImageView iv_img;
		EditText tv_number;
		TextView tv_name, tv_price, tv_heji;
		LinearLayout ll_min, ll_add;
	}

	// 打开软键盘s
	private void openKeyBord(EditText editText) {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	// 关闭软键盘
	private void closeKeybord(EditText editText) {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

}