package com.juxun.business.street.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.imagedemo.ImagePagerActivity;
import com.juxun.business.street.activity.ModifyTemplateAcitivity;
import com.juxun.business.street.activity.TemplatedetailsAcitivity;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.MallTemplateBean;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Administrator TemplateAdapter 模板选用
 * 
 */
public class TemplateAdapter extends BaseAdapter {
	private List<MallTemplateBean> list = null;
	private Context mContext;
	DecimalFormat df = new java.text.DecimalFormat("0.00");
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = ImageLoaderUtil.getOptions();

	public TemplateAdapter(Context mContext, List<MallTemplateBean> list) {

		this.mContext = mContext;
		this.list = list;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<MallTemplateBean> list) {
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
			view = LayoutInflater.from(mContext).inflate(R.layout.template_itm, null);
			viewHolder.iv_img = (ImageView) view.findViewById(R.id.iv_img);
			viewHolder.btn_xuanyong = (Button) view.findViewById(R.id.btn_xuanyong);
			viewHolder.tv_chenben = (TextView) view.findViewById(R.id.tv_chenben);
			viewHolder.tv_shichang = (TextView) view.findViewById(R.id.tv_shichang);
			viewHolder.tv_number = (TextView) view.findViewById(R.id.tv_number);
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final MallTemplateBean mallShoppingCartbean = list.get(position);
		viewHolder.tv_chenben.setText("成本价：¥" + mallShoppingCartbean.getTemplate_cost());
		viewHolder.tv_shichang.setText("市场价：¥" + mallShoppingCartbean.getTemplate_price());
		viewHolder.tv_name.setText(mallShoppingCartbean.getTemplate_name());
		imageLoader.displayImage(Constants.imageUrl + mallShoppingCartbean.getIcon(), viewHolder.iv_img, options);
		final String[] cover = mallShoppingCartbean.getCover().split(",");
		viewHolder.tv_number.setText(cover.length + "");
		viewHolder.iv_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imageBrower(0, cover);
			}
		});
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, TemplatedetailsAcitivity.class);
				intent.putExtra("Template_id", mallShoppingCartbean.getTemplate_id());
				mContext.startActivity(intent);
			}
		});
		viewHolder.btn_xuanyong.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, ModifyTemplateAcitivity.class);
				intent.putExtra("Template_id", mallShoppingCartbean.getTemplate_id());
				mContext.startActivity(intent);
			}
		});
		return view;

	}

	protected void imageBrower(int position, String[] cover) {
		ArrayList<String> urls2 = new ArrayList<String>();
		for (int i = 0; i < cover.length; i++) {
			urls2.add(cover[i]);
		}
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}

	final static class ViewHolder {
		/**
		 * 商品名称 成本价 市场价 图片数量 图片 选用
		 */
		TextView tv_name, tv_chenben, tv_shichang, tv_number;
		ImageView iv_img;
		Button btn_xuanyong;
	}

}