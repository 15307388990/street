/**
 * 
 */
package com.juxun.business.street.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.example.imagedemo.ImagePagerActivity;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.mTemplateBean;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * 类名称：TemplatedetailsAcitivity 类描述：模板详情 创建人：罗富贵 创建时间：2016年11月22日
 * 
 * @version
 * 
 */
public class TemplatedetailsAcitivity extends BaseActivity {
	// 图片组
	@ViewInject(R.id.ll_photo_group)
	private LinearLayout ll_photo_group;
	@ViewInject(R.id.ll_photo_group2)
	private LinearLayout ll_photo_group2;
	// 过期时间
	@ViewInject(R.id.tv_date)
	private TextView tv_date;
	// 商品频道
	@ViewInject(R.id.tv_classification)
	private TextView tv_classification;
	// 商品编码
	@ViewInject(R.id.tv_coding)
	private TextView tv_coding;
	// 商品名称
	@ViewInject(R.id.et_goodsname)
	private TextView et_goodsname;
	// 商品单位
	@ViewInject(R.id.et_unit)
	private TextView et_unit;
	// 商品摘要
	@ViewInject(R.id.et_commodity_info)
	private TextView et_commodity_info;

	// 商品成本价
	@ViewInject(R.id.et_costprice)
	private TextView et_costprice;
	// 商品市场价
	@ViewInject(R.id.et_marketprice)
	private TextView et_marketprice;
	// 商品库存
	@ViewInject(R.id.et_inventory)
	private TextView et_inventory;
	// 返回
	@ViewInject(R.id.button_back)
	private ImageView button_back;
	// 选用此模板
	@ViewInject(R.id.btn_xuanyong)
	private Button btn_xuanyong;
	// 更多
	@ViewInject(R.id.button_function)
	private ImageView button_function;
	private mTemplateBean goodDetails;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = ImageLoaderUtil.getOptions();
	private int Template_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_templated_details);
		ViewUtils.inject(this);
		initView();

	}

	private void initView() {
		Template_id = getIntent().getIntExtra("Template_id", 0);
		getTemplateInfo();
	}

	private void initDate() {
		tv_coding.setText(goodDetails.getCode());
		tv_classification.setText(goodDetails.getChannelName());
		et_goodsname.setText(goodDetails.getTemplate_name());
		et_unit.setText(goodDetails.getUnit_name());
		et_commodity_info.setText(goodDetails.getTemplate_info());
		et_costprice.setText(goodDetails.getTemplate_cost() + "");
		et_marketprice.setText(goodDetails.getTemplate_price() + "");
		et_inventory.setText("");
		tv_date.setText(Tools.getDateformat(goodDetails.getCreate_date()));
		// 图片处理
		initPhotos();

	}

	/**
	 * 这里动态的去添加图片
	 */
	private void initPhotos() {
		String[] covers = goodDetails.getCover().split(",");
		ll_photo_group.removeAllViewsInLayout();
		ll_photo_group2.removeAllViewsInLayout();
		int photo_width = Tools.dip2px(this, 65);
		int line_width = Tools.dip2px(this, 20);
		final ArrayList<String> imageUrls = Tools.StringArrayToList(covers);
		for (int i = 0; i < covers.length; i++) {
			ImageView iv_photo = new ImageView(this);
			final int position = i;
			iv_photo.setLayoutParams(new RelativeLayout.LayoutParams(photo_width, photo_width));
			View blank_view = new View(this);
			blank_view.setLayoutParams(new LayoutParams(line_width, line_width));
			if (i < 4) {
				ll_photo_group.addView(iv_photo);
				ll_photo_group.addView(blank_view);
			} else {
				ll_photo_group2.addView(iv_photo);
				ll_photo_group2.addView(blank_view);
			}
			imageLoader.displayImage(Constants.imageUrl + covers[i], iv_photo, options);
			iv_photo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					imageBrower(position, imageUrls);
				}
			});
		}

	}

	/** 单击事件 */
	@OnClick({ R.id.button_back, R.id.button_function, R.id.btn_xuanyong })
	public void clickMethod(View v) {
		if (v.getId() == R.id.button_back) {
			this.finish();
		} else if (v.getId() == R.id.button_function) {

		} else if (v.getId() == R.id.btn_xuanyong) {
			Intent intent = new Intent(TemplatedetailsAcitivity.this, ModifyTemplateAcitivity.class);
			intent.putExtra("Template_id", goodDetails.getTemplate_id());
			startActivity(intent);
			finish();
		}
	}

	private void getTemplateInfo() {
		Map<String, String> map = new HashMap<String, String>();
		// mallTemplateId int 商品模板id
		map.put("mallTemplateId", Template_id + "");// 机构id
		map.put("auth_token", partnerBean.getAuth_token());
		mQueue.add(ParamTools.packParam(Constants.getTemplateInfo, this, this, map));
		loading();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(getApplicationContext(), ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
	}

	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.optInt("status");
			String msg = json.optString("msg");
			if (stauts == 0) {
				if (url.contains(Constants.getTemplateInfo)) {
					String mCommodity = json.getString("mTemplate");
					goodDetails = JSON.parseObject(mCommodity, mTemplateBean.class);
					initDate();
				}

			} else {
				Tools.showToast(this, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
		}
	}

}
