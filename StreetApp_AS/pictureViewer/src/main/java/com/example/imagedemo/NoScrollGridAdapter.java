package com.example.imagedemo;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class NoScrollGridAdapter extends BaseAdapter {

	/** 上下文 */
	private Context ctx;
	/** 图片Url集合 */
	private ArrayList<String> imageUrls;

	private int width = 90;

	public NoScrollGridAdapter(Context ctx, ArrayList<String> urls) {
		this.ctx = ctx;
		this.imageUrls = urls;
	}

	public NoScrollGridAdapter(Context ctx, ArrayList<String> urls, int width) {
		this.ctx = ctx;
		this.imageUrls = urls;
		this.width = width;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageUrls == null ? 0 : imageUrls.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imageUrls.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(ctx, R.layout.item_gridview, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
		imageView.getLayoutParams().height = dip2px(ctx, width);
		imageView.getLayoutParams().width = dip2px(ctx, width);
		DisplayImageOptions options = new DisplayImageOptions.Builder()//
				.cacheInMemory(true)//
				.cacheOnDisk(true)//
				.bitmapConfig(Config.RGB_565)//
				.build();
		ImageLoader.getInstance().displayImage("http://7tsxz7.com1.z0.glb.clouddn.com/" + imageUrls.get(position),
				imageView, options);
		return view;
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}
