/**
 * 
 */
package com.juxun.business.street.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.juxun.business.street.config.Constants;
import com.lidroid.xutils.BitmapUtils;

/**
 * 
 * 项目名称：Street 类名称：BitmapUtils 类描述： 图片工具类 创建人：WuJianhua 创建时间：2015年5月28日
 * 下午12:21:11 修改人：WuJianhua 修改时间：2015年5月28日 下午12:21:11 修改备注：
 * 
 * @version
 * 
 */
public class BitmapUtil {
	public static void dispalyHttpBitmap(ImageView iv, String url, Context context) {
		BitmapUtils bitmapUtils = new BitmapUtils(context, context.getFilesDir().getAbsolutePath());
		bitmapUtils.display(iv,url);
	}

	public static String obtainImagePath(Context context, String url) throws Exception {
		BitmapUtils bitmapUtils = new BitmapUtils(context, context.getFilesDir().getAbsolutePath());
		File file = bitmapUtils.getBitmapFileFromDiskCache(Constants.imageUrl + url);
		if (file != null && file.exists()) {
			String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "3.png";
			file.renameTo(new File(path));
			File file1 = new File(path);
			InputStream in = new FileInputStream(file);
			OutputStream out = new FileOutputStream(file1);
			int length = 0;
			byte[] cache = new byte[1024 * 50];
			while ((length = in.read(cache)) != -1) {
				out.write(cache, 0, length);
			}
			in.close();
			out.close();
			return file1.getAbsolutePath();
		} else {
			return null;
		}
	}

	/**
	 * 生成二维码 要转换的地址或字符串,可以是中文
	 * 
	 * @param url
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap createQRImage(String url, final int width, final int height) {
		try {
			// 判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1) {
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
			int[] pixels = new int[width * height];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					} else {
						pixels[y * width + x] = 0xffffffff;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}
}
