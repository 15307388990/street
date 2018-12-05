package com.juxun.business.street.bean;

import com.yl.ming.efengshe.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImageWithDelete extends RelativeLayout implements OnClickListener {
	private ImageView delete_photo;
	private ImageView iv_photo;

	public ImageView getIv_photo() {
		return iv_photo;
	}

	public void setIv_photo(ImageView iv_photo) {
		this.iv_photo = iv_photo;
	}

	private int position;

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	ICallBack icallBack = null;

	public ImageWithDelete(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	private ImageView getDelete_photo() {
		return delete_photo;
	}

	public ImageWithDelete(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.imagebtnwithdelete, this, true);
		delete_photo = (ImageView) findViewById(R.id.delete_photo);
		delete_photo.setOnClickListener(this);
		iv_photo = (ImageView) findViewById(R.id.repair_photo);
		// TODO Auto-generated constructor stub
	}

	public ImageWithDelete(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.imagebtnwithdelete, this, true);
		delete_photo = (ImageView) findViewById(R.id.delete_photo);
		delete_photo.setOnClickListener(this);
		iv_photo = (ImageView) findViewById(R.id.repair_photo);
	}

	public void setBackgroundDrawable(Bitmap bm) {
		iv_photo.setImageBitmap(bm);
		this.invalidate();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d("yj", "deleteOnClick");
		icallBack.onClickDeleteButton(getPosition());
	}

	public void setonClick(ICallBack iBack) {
		icallBack = iBack;
	}

	public interface ICallBack {
		public void onClickDeleteButton(int position);
	}
}
