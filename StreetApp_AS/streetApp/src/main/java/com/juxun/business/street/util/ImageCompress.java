package com.juxun.business.street.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.util.Log;

/**
 * 图片压缩工具类
 * 
 * @author Administrator
 * 
 */
public class ImageCompress {

	public static final String CONTENT = "content";
	public static final String FILE = "file";

	/**
	 * 图片压缩参数
	 * 
	 * @author Administrator
	 * 
	 */
	public static class CompressOptions {
		public static final int DEFAULT_WIDTH = 400;
		public static final int DEFAULT_HEIGHT = 800;

		public int maxWidth = DEFAULT_WIDTH;
		public int maxHeight = DEFAULT_HEIGHT;
		/**
		 * 压缩后图片保存的文件
		 */
		public File destFile;
		/**
		 * 图片压缩格式,默认为jpg格式
		 */
		public CompressFormat imgFormat = CompressFormat.JPEG;

		/**
		 * 图片压缩比例 默认为30
		 */
		public int quality = 30;

		public Uri uri;
	}

	public Bitmap compressFromUri(Context context,
			CompressOptions compressOptions) {

		// uri指向的文件路径
		String filePath = getFilePath(context, compressOptions.uri);

		if (null == filePath) {
			return null;
		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		Bitmap temp = BitmapFactory.decodeFile(filePath, options);

		int actualWidth = options.outWidth;
		int actualHeight = options.outHeight;

		int desiredWidth = getResizedDimension(compressOptions.maxWidth,
				compressOptions.maxHeight, actualWidth, actualHeight);
		int desiredHeight = getResizedDimension(compressOptions.maxHeight,
				compressOptions.maxWidth, actualHeight, actualWidth);

		options.inJustDecodeBounds = false;
		options.inSampleSize = findBestSampleSize(actualWidth, actualHeight,
				desiredWidth, desiredHeight);

		Bitmap bitmap = null;

		Bitmap destBitmap = BitmapFactory.decodeFile(filePath, options);

		// If necessary, scale down to the maximal acceptable size.
		if (destBitmap.getWidth() > desiredWidth
				|| destBitmap.getHeight() > desiredHeight) {
			bitmap = Bitmap.createScaledBitmap(destBitmap, desiredWidth,
					desiredHeight, true);
			destBitmap.recycle();
		} else {
			bitmap = destBitmap;
		}

		// compress file if need
		if (null != compressOptions.destFile) {
			compressFile(compressOptions, bitmap);
		}

		return bitmap;
	}

	/**
	 * compress file from bitmap with compressOptions
	 * 
	 * @param compressOptions
	 * @param bitmap
	 */
	private void compressFile(CompressOptions compressOptions, Bitmap bitmap) {
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(compressOptions.destFile);
		} catch (FileNotFoundException e) {
			Log.e("ImageCompress", e.getMessage());
		}

		bitmap.compress(compressOptions.imgFormat, compressOptions.quality,
				stream);
	}
	
	
	//压缩后再上传 by yejia
	public static byte[] BitmapToBytes(Bitmap bm) {
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
	       return baos.toByteArray();
	 }
	
	public static byte[] BitmapToBytes2(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
		
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 1080f;//这里设置高度为800f
		float ww = 720f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
		
		return baos.toByteArray();//压缩好比例大小后再进行质量压缩
	}

	private static int findBestSampleSize(int actualWidth, int actualHeight,
			int desiredWidth, int desiredHeight) {
		double wr = (double) actualWidth / desiredWidth;
		double hr = (double) actualHeight / desiredHeight;
		double ratio = Math.min(wr, hr);
		float n = 1.0f;
		while ((n * 2) <= ratio) {
			n *= 2;
		}

		return (int) n;
	}

	private static int getResizedDimension(int maxPrimary, int maxSecondary,
			int actualPrimary, int actualSecondary) {
		// If no dominant value at all, just return the actual.
		if (maxPrimary == 0 && maxSecondary == 0) {
			return actualPrimary;
		}

		// If primary is unspecified, scale primary to match secondary's scaling
		// ratio.
		if (maxPrimary == 0) {
			double ratio = (double) maxSecondary / (double) actualSecondary;
			return (int) (actualPrimary * ratio);
		}

		if (maxSecondary == 0) {
			return maxPrimary;
		}

		double ratio = (double) actualSecondary / (double) actualPrimary;
		int resized = maxPrimary;
		if (resized * ratio > maxSecondary) {
			resized = (int) (maxSecondary / ratio);
		}
		return resized;
	}

	/**
	 * 获取文件的路径
	 * 
	 * @param scheme
	 * @return
	 */
	private String getFilePath(Context context, Uri uri) {

		String filePath = null;

		if (CONTENT.equalsIgnoreCase(uri.getScheme())) {

			Cursor cursor = context.getContentResolver().query(uri,
					new String[] { Images.Media.DATA }, null, null, null);

			if (null == cursor) {
				return null;
			}

			try {
				if (cursor.moveToNext()) {
					filePath = cursor.getString(cursor
							.getColumnIndex(Images.Media.DATA));
				}
			} finally {
				cursor.close();
			}
		}

		// 从文件中选择
		if (FILE.equalsIgnoreCase(uri.getScheme())) {
			filePath = uri.getPath();
		}

		return filePath;
	}
}