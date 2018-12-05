/**
 * 
 */
package com.juxun.business.street.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yl.ming.efengshe.R;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 
 * @version 推广店铺
 * 
 */
public class ToPromoteActivity extends BaseActivity {

	@ViewInject(R.id.tv_number)
	private TextView tv_number; // 已邀请人数
	@ViewInject(R.id.ll_wechat)
	private LinearLayout ll_wechat;// 微信分享
	@ViewInject(R.id.ll_friends)
	private LinearLayout ll_friends;// 朋友圈分享
	@ViewInject(R.id.ll_phone)
	private LinearLayout ll_phone;// 保存手机
	@ViewInject(R.id.iv_img)
	private ImageView iv_img;
	private Bitmap bitmap;
	IWXAPI msgApi;
	private String registerMemberCount;// 邀请的人数

	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = ImageLoaderUtil.getOptions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_promote);
		ViewUtils.inject(this);
		initTitle();
		title.setText("推广店铺");
		msgApi = WXAPIFactory.createWXAPI(this, null);
		msgApi.registerApp(Constants.APP_ID);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getRecommendedNumber();
	}

	/** 单击事件 */
	@OnClick({ R.id.tv_number, R.id.ll_wechat, R.id.ll_friends, R.id.ll_phone })
	public void clickMethod(View v) {
		switch (v.getId()) {
		case R.id.tv_number:
			Intent intent = new Intent(ToPromoteActivity.this,
					TheInvitationActivity.class);
			intent.putExtra("registerMemberCount", registerMemberCount);
			startActivity(intent);
			break;
		case R.id.ll_wechat:
			inireq(bitmap, 1);
			break;
		case R.id.ll_friends:
			inireq(bitmap, 2);
			break;
		case R.id.ll_phone:
			// 获取内部存储状态
			SavaImage(bitmap, Environment.getExternalStorageDirectory()
					.getPath() + "/Test");
			break;
		}
	}

	/* 统计合伙人邀请用户的数量 */
	private void getRecommendedNumber() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("auth_token", partnerBean.getAuth_token());
		mQueue.add(ParamTools.packParam(Constants.getRecommendedNumber, this,
				this, map));
	}

	/**
	 * 
	 * @param bitmap
	 * @param type
	 *            1为 微信聊天界面 2为微信朋友圈
	 */
	private void inireq(Bitmap bitmap, int type) {
		WXImageObject imgObject = new WXImageObject(bitmap);
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObject;
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.message = msg;
		if (type == 1) {
			req.scene = SendMessageToWX.Req.WXSceneSession;
		} else {
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
		}
		Boolean boolean1 = msgApi.sendReq(req);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	/***
	 * 
	 * @param src
	 *            本地图片
	 * @param logo
	 *            二维码图片
	 * @return
	 */
	private Bitmap addLogo(Bitmap src, Bitmap logo) {
		if (src == null) {
			return null;
		}

		if (logo == null) {
			return src;
		}
		Bitmap srcbitmap = Bitmap.createBitmap(720, 1280, Bitmap.Config.ARGB_8888);
		try {
			Canvas canvas = new Canvas(srcbitmap);
			RectF rectF = new RectF(0, 0, 720, 1280);   //w和h分别是屏幕的宽和高，也就是你想让图片显示的宽和高  
			canvas.drawBitmap(src, null, rectF, null);  
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
		} catch (Exception e) {
			srcbitmap = null;
			e.getStackTrace();
		}
		// 获取图片的宽高
		int srcWidth = srcbitmap.getWidth();
		int srcHeight = srcbitmap.getHeight();
		int logoWidth = logo.getWidth();
		int logoHeight = logo.getHeight();

		if (srcWidth == 0 || srcHeight == 0) {
			return null;
		}

		if (logoWidth == 0 || logoHeight == 0) {
			return src;
		}

		// logo大小为二维码整体大小的1/5
		float scaleFactor = srcWidth * 1.0f / 4 / logoWidth;
		// logo大小为二维码整体大小的1/5
		float scaleFactor2 = srcHeight * 1.0f / 7 / logoHeight;
		Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight,
				Bitmap.Config.ARGB_8888);
		try {
			Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(srcbitmap, 0, 0, null);
			canvas.scale(scaleFactor, scaleFactor2, srcWidth / 2, srcHeight / 2);
			canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, srcHeight
					+ logoHeight - (float) (srcHeight * 0.26), null);
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
		} catch (Exception e) {
			bitmap = null;
			e.getStackTrace();
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
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.optInt("status");
			String msg = json.optString("msg");
			if (stauts == 0) {
				if (url.contains(Constants.getRecommendedNumber)) {
					JSONObject jsonObject = json.getJSONObject("totalBean");
					registerMemberCount = jsonObject
							.getString("registerMemberCount");
					tv_number.setText("已邀请人数\n" + registerMemberCount + "人");
					// 推广店铺页面的二维码位置
					String imgUrl = Constants.imageUrl
							+ partnerBean.getPopularize_qrcode();
					imageLoader.displayImage(imgUrl, iv_img, options);
					// 生成二维码
					Bitmap mBitmap = imageLoader.loadImageSync(imgUrl);
					Bitmap promoteBitmap = ThumbnailUtils.extractThumbnail(
							mBitmap, 400, 400);
					// 分享出去的图片
					bitmap = addLogo(BitmapFactory.decodeResource(
							getResources(), R.drawable.share_pic_result),
							promoteBitmap);
				}
			} else {
				Tools.showToast(ToPromoteActivity.this, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
		}
	}
}
