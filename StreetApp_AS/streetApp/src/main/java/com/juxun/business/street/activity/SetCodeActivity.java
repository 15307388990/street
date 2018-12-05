/**
 * 
 */
package com.juxun.business.street.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.juxun.business.street.util.BitmapUtil;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.SwipeBackController;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 
 * 
 * @version 店铺二维码
 * 
 */
public class SetCodeActivity extends BaseActivity {
	@ViewInject(R.id.iv_img)
	private ImageView iv_img; // 店铺二维码
	@ViewInject(R.id.btn_next)
	private Button btn_next;
	@ViewInject(R.id.iv_store)
	private ImageView iv_store; // 店铺logo
	@ViewInject(R.id.rl_img)
	private RelativeLayout rl_img;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = ImageLoaderUtil.getOptions();
	private String shopicon;
	public SwipeBackController swipeBackController;// 右滑关闭
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x123) {
				// 1.构建Bitmap
				WindowManager windowManager = getWindowManager();
				Display display = windowManager.getDefaultDisplay();
				int w = display.getWidth();
				int h = display.getHeight() - 50;

				Bitmap Bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);

				// 2.获取屏幕
				// View decorview = rl_img.getDecorView();
				rl_img.setDrawingCacheEnabled(true);
				Bmp = rl_img.getDrawingCache();
				// 获取内部存储状态
				SavaImage(Bmp, Environment.getExternalStorageDirectory()
						.getPath() + "/Test");
			}
		};
	};
	private String mCashier_code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_set_code);
		ViewUtils.inject(this);
		Tools.acts.add(this);
		swipeBackController = new SwipeBackController(this);
		initTitle();
		title.setText("自助收银二维码");
		shopicon = getIntent().getStringExtra("shopicon");
		mCashier_code = getIntent().getStringExtra("cashier_code");
		initView();
	}

	private void initView() {
		// imageLoader
		// .displayImage(Constants.imageUrl + storeBean.getQrcode_url(),
		// iv_img, options);
		// imageLoader.displayImage(Constants.imageUrl + shopicon, iv_store,
		// options);
		
		int imgInch = Tools.dip2px(this, 260);
		Bitmap mBitmap = BitmapUtil.createQRImage(mCashier_code, imgInch, imgInch);
		iv_img.setImageBitmap(mBitmap);
		
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(0x123);
			}
		});
	}

	// /**
	// * 异步线程下载图片
	// *
	// */
	// class Task extends AsyncTask<String, Integer, Void> {
	//
	// protected Void doInBackground(String... params) {
	// bitmap = GetImageInputStream((String) params[0]);
	// return null;
	// }
	//
	// protected void onPostExecute(Void result) {
	// super.onPostExecute(result);
	// Message message = new Message();
	// message.what = 0x123;
	// handler.sendMessage(message);
	// }
	//
	// }

	/**
	 * 获取网络图片
	 * 
	 * @param imageurl
	 *            图片网络地址
	 * @return Bitmap 返回位图
	 */
	public Bitmap GetImageInputStream(String imageurl) {
		URL url;
		HttpURLConnection connection = null;
		Bitmap bitmap = null;
		try {
			url = new URL(imageurl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(6000); // 超时设置
			connection.setDoInput(true);
			connection.setUseCaches(false); // 设置不使用缓存
			InputStream inputStream = connection.getInputStream();
			bitmap = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bitmap;
	}

	/**
	 * 保存位图到本地
	 * 
	 * @param bitmap
	 * @param path
	 *            本地路径
	 * @return void
	 */
	public void SavaImage(Bitmap bitmap, String path) {
		File file = new File(path);
		FileOutputStream fileOutputStream = null;
		// 文件夹不存在，则创建它
		if (!file.exists()) {
			file.mkdir();
		}
		try {
			String fath = path + "/" + System.currentTimeMillis() + ".png";
			fileOutputStream = new FileOutputStream(fath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
			fileOutputStream.close();
			saveImgToGallery(fath);
			// 保存图片后发送广播通知更新数据库
			Uri uri = Uri.fromFile(file);
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
			Tools.showToast(getApplicationContext(), "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			Tools.showToast(getApplicationContext(), "保存失败");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// obtainData();
	}

	/**
	 * 更新系统数据库找到图片
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean saveImgToGallery(String fileName) {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (!sdCardExist)
			return false;

		try {
			// String url = MediaStore.Images.Media.insertImage(cr, bmp,
			// fileName,
			// "");
			// app.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
			// .parse("file://"
			// + Environment.getExternalStorageDirectory())));

			// debug
			ContentValues values = new ContentValues();
			values.put("datetaken", new Date().toString());
			values.put("mime_type", "image/png");
			values.put("_data", fileName);
			// values.put("title", this.a.getString(2131230720));
			// values.put("_display_name", (String)localObject1);
			// values.put("orientation", "");
			// values.put("_size", Integer.valueOf(0));
			Application app = getApplication();
			ContentResolver cr = app.getContentResolver();
			cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.optInt("status");
			String msg = json.getString("msg");
			if (stauts == 0) {
			} else if (stauts == -4004) {
				mSavePreferencesData.putStringData("json", "");
				Tools.jump(this, LoginActivity.class, false);
				Tools.showToast(this, "登录过期请重新登录");
				Tools.acts.clear();
			} else {
				Tools.showToast(SetCodeActivity.this, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (swipeBackController.processEvent(event)) {
			return true;
		} else {
			return onTouchEvent(event);
		}

	}
}
