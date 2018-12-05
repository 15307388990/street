package com.juxun.business.street.util;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.yl.ming.efengshe.R;

import android.graphics.Bitmap;

public class ImageLoaderUtil {
	// public static DisplayImageOptions options =
	// new DisplayImageOptions.Builder()
	// .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
	// .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
	// .build(); // 构建完成
	public static DisplayImageOptions options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
			.showImageForEmptyUri(R.drawable.ic_launcher).showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
			.cacheOnDisc(true).build();

	public static DisplayImageOptions getOptions() {
		return options;
	}
	
}
