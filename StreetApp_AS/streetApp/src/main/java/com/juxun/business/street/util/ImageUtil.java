package com.juxun.business.street.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtil {
	public static Bitmap decodeImage(String path) {
		InputStream is = null;
		try {
			is = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 2.为位图设置100K的缓存

		BitmapFactory.Options opts = new BitmapFactory.Options();

		opts.inTempStorage = new byte[100 * 1024];

		// 3.设置位图颜色显示优化方式

		// ALPHA_8：每个像素占用1byte内存（8位）

		// ARGB_4444:每个像素占用2byte内存（16位）

		// ARGB_8888:每个像素占用4byte内存（32位）

		// RGB_565:每个像素占用2byte内存（16位）

		// Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。但同样的，占用的内存//也最大。也就意味着一个像素点占用4个字节的内存。我们来做一个简单的计算题：3200*2400*4
		// bytes //=30M。如此惊人的数字！哪怕生命周期超不过10s，Android也不会答应的。

		opts.inPreferredConfig = Bitmap.Config.RGB_565;

		// 4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收

		opts.inPurgeable = true;

		// 5.设置位图缩放比例

		// width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。

		opts.inSampleSize = 4;

		// 6.设置解码位图的尺寸信息

		opts.inInputShareable = true;

		// 7.解码位图

		Bitmap btp = BitmapFactory.decodeStream(is, null, opts);

		return btp;

	}
	/**
     * 根据一个网络连接(String)获取bitmap图像
     * 
     * @param imageUri
     * @return
     * @throws MalformedURLException
     */
    public static Bitmap getbitmap(String imageUri) {
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            bitmap = null;
        } catch (IOException e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }
}
