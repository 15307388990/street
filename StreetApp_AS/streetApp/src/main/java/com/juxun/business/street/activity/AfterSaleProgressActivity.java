package com.juxun.business.street.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.bean.PurchaseOderBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.fragment.ApplyRecordInfoBean;
import com.juxun.business.street.fragment.RequestAfterSaleBean;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * AfterSaleProgress 售后进度 2017/8/9 罗明
 */
public class AfterSaleProgressActivity extends BaseActivity implements OnClickListener {
	/** ----------------------- 商品清单-------------------------------- **/
	@ViewInject(R.id.ll_list)
	private LinearLayout ll_list;// 商品列表
	@ViewInject(R.id.tv_preferential)
	private TextView tv_preferential;// 优惠金额
	@ViewInject(R.id.tv_heji)
	private TextView tv_heji;// 总价 红色的
	/** ----------------------- 订单信息-------------------------------- **/
	@ViewInject(R.id.tv_refund_price)
	private TextView tv_refund_price;// 实退金额af
	@ViewInject(R.id.tv_pay_type)
	private TextView tv_pay_type;// 退款账户
	@ViewInject(R.id.tv_create_date)
	private TextView tv_create_date;// 申请时间
	@ViewInject(R.id.tv_customer_service_order_num)
	private TextView tv_customer_service_order_num;// 服务单号
	@ViewInject(R.id.tv_supplier_order_num)
	private TextView tv_supplier_order_num;// 订单编号
	@ViewInject(R.id.tv_refund_remark)
	private TextView tv_refund_remark;// 售后留言
	@ViewInject(R.id.tv_platform_remark)
	private TextView tv_platform_remark;// 客服留言
	/** ----------------------- 进度-------------------------------- **/
	@ViewInject(R.id.tv_state)
	private TextView tv_state;// 售后进度
	@ViewInject(R.id.iv_1)
	private ImageView iv_1;
	@ViewInject(R.id.iv_2)
	private ImageView iv_2;
	@ViewInject(R.id.iv_3)
	private ImageView iv_3;
	@ViewInject(R.id.iv_4)
	private ImageView iv_4;
	@ViewInject(R.id.iv_5)
	private ImageView iv_5;
	@ViewInject(R.id.tv_1)
	private TextView tv_1;
	@ViewInject(R.id.tv_2)
	private TextView tv_2;
	@ViewInject(R.id.tv_3)
	private TextView tv_3;
	@ViewInject(R.id.tv_4)
	private TextView tv_4;
	@ViewInject(R.id.tv_5)
	private TextView tv_5;
	@ViewInject(R.id.button_back)
	private ImageView button_back;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = ImageLoaderUtil.getOptions();

	private PurchaseOderBean mApplyRecordInfoBean;
	private int order_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aftersale_progress);
		ViewUtils.inject(this);
		Tools.acts.add(this);
		order_id = getIntent().getIntExtra("order_id", 0);
		button_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mApplyRecordInfoBean= (PurchaseOderBean) getIntent().getSerializableExtra("purchaseOderBean");
		initDate();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


	private void initDate() {
		String spec = mApplyRecordInfoBean.getCommodity_json();
		List<RequestAfterSaleBean> suplierList = new ArrayList<RequestAfterSaleBean>();
		suplierList = JSON.parseArray(spec, RequestAfterSaleBean.class);
		ll_list.removeAllViews();
		if (suplierList.size() > 0) {
			for (int i = 0; i < suplierList.size(); i++) {
				View lview = LayoutInflater.from(this).inflate(R.layout.oder_item3, null);
				TextView tv_name = (TextView) lview.findViewById(R.id.tv_name);
				TextView tv_purchase_quantity = (TextView) lview.findViewById(R.id.tv_purchase_quantity);
				TextView tv_total_price = (TextView) lview.findViewById(R.id.tv_total_price);
				TextView tv_price = (TextView) lview.findViewById(R.id.tv_price);
				ImageView imageView = (ImageView) lview.findViewById(R.id.iv_img);
				RequestAfterSaleBean msgmodel = suplierList.get(i);
				String[] cover = msgmodel.getCommodity_icon().split(",");
				imageLoader.displayImage(Constants.imageUrl + cover[0], imageView, options);
				tv_name.setText(msgmodel.getCommodity_name());
				tv_purchase_quantity.setText("x" + msgmodel.getRefund_commodity_num());
				tv_total_price.setText(
						"小计：¥" + Tools.getFenYuan(msgmodel.getUnit_price()) * msgmodel.getRefund_commodity_num());
				tv_price.setText("¥" + Tools.getFenYuan(msgmodel.getUnit_price()));
				ll_list.addView(lview);
			}
		}
		initState(mApplyRecordInfoBean.getCustomer_service_status());
		// 1.审核中2.审核不通过3退款中4退款成功5退款失败6取消售后
		switch (mApplyRecordInfoBean.getCustomer_service_status()) {
		case 1:
			tv_state.setText("审核中");
			break;
		case 2:
			tv_state.setText("审核不通过");
			break;
		case 3:
			tv_state.setText("退款中");
			break;
		case 4:
			tv_state.setText("退款成功");
			break;
		case 5:
			tv_state.setText("退款失败");
			break;
		case 6:
			tv_state.setText("取消售后");
			break;
		default:
			break;
		}
		tv_refund_price.setText("¥" + Tools.getFenYuan(mApplyRecordInfoBean.getReality_refund_price()));
		// 1.微信支付.2.支付宝支付 3.pos 4货到付款 5白条支付 6余额支付
		switch (mApplyRecordInfoBean.getPay_type()) {
		case 1:
			tv_pay_type.setText("微信支付");
			break;
		case 2:
			tv_pay_type.setText("支付宝支付");
			break;
		case 3:
			tv_pay_type.setText("pos");
			break;
		case 4:
			tv_pay_type.setText("货到付款");
			break;
		case 5:
			tv_pay_type.setText("白条支付");
			break;
		case 6:
			tv_pay_type.setText("余额支付");
			break;
		default:
			break;
		}

		tv_create_date.setText(Tools.getDateformat2(mApplyRecordInfoBean.getCreate_date()));
		tv_customer_service_order_num.setText(mApplyRecordInfoBean.getCustomer_service_order_num());
		tv_platform_remark.setText(mApplyRecordInfoBean.getPlatform_remark());
		tv_refund_remark.setText(mApplyRecordInfoBean.getRefund_remark());
		tv_supplier_order_num.setText(mApplyRecordInfoBean.getSupplier_order_num());
	}

	// 1.审核中2.审核不通过3退款中4退款成功5退款失败6取消售后
	private void initState(int state) {
		switch (state) {
		case 1:
			iv_1.setImageResource(R.drawable.aftersale_plan_sign_success);
			iv_2.setImageResource(R.drawable.aftersale_plan_sign_success);
			iv_3.setImageResource(R.drawable.aftersale_plan_sign_none);
			iv_4.setImageResource(R.drawable.aftersale_plan_sign_none);
			iv_5.setImageResource(R.drawable.aftersale_plan_sign_none);
			break;
		case 2:
			iv_1.setImageResource(R.drawable.aftersale_plan_sign_success);
			iv_2.setImageResource(R.drawable.aftersale_plan_sign_success);
			iv_3.setImageResource(R.drawable.aftersale_plan_sign_fail);
			tv_3.setText("审核不通过");
			iv_4.setImageResource(R.drawable.aftersale_plan_sign_none);
			iv_5.setImageResource(R.drawable.aftersale_plan_sign_none);
			break;
		case 3:
			iv_1.setImageResource(R.drawable.aftersale_plan_sign_success);
			iv_2.setImageResource(R.drawable.aftersale_plan_sign_success);
			iv_3.setImageResource(R.drawable.aftersale_plan_sign_success);
			tv_3.setText("审核通过");
			iv_4.setImageResource(R.drawable.aftersale_plan_sign_none);
			iv_5.setImageResource(R.drawable.aftersale_plan_sign_none);
			break;
		case 4:
			iv_1.setImageResource(R.drawable.aftersale_plan_sign_success);
			iv_2.setImageResource(R.drawable.aftersale_plan_sign_success);
			iv_3.setImageResource(R.drawable.aftersale_plan_sign_success);
			iv_4.setImageResource(R.drawable.aftersale_plan_sign_success);
			iv_5.setImageResource(R.drawable.aftersale_plan_sign_success);
			break;
		case 5:
			iv_1.setImageResource(R.drawable.aftersale_plan_sign_success);
			iv_2.setImageResource(R.drawable.aftersale_plan_sign_success);
			iv_3.setImageResource(R.drawable.aftersale_plan_sign_success);
			iv_4.setImageResource(R.drawable.aftersale_plan_sign_fail);
			tv_4.setText("退款失败");
			iv_5.setImageResource(R.drawable.aftersale_plan_sign_success);
			break;
		case 6:
			iv_1.setImageResource(R.drawable.aftersale_plan_sign_success);
			iv_2.setImageResource(R.drawable.aftersale_plan_sign_success);
			iv_3.setImageResource(R.drawable.aftersale_plan_sign_fail);
			iv_4.setImageResource(R.drawable.aftersale_plan_sign_none);
			tv_3.setText("已取消申请");
			iv_5.setImageResource(R.drawable.aftersale_plan_sign_none);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_back:
			AfterSaleProgressActivity.this.finish();
			break;
		}
	}

	@Override
	public void onResponse(String response, String url) {
		// TODO Auto-generated method stub
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.optInt("resultCode");
			if (stauts == 0) {
				if (url.contains(Constants.getApplyRecordInfo)) {

				}
			} else if (stauts == -4004) {
				mSavePreferencesData.putStringData("json", "");
				Tools.jump(AfterSaleProgressActivity.this, LoginActivity.class, true);
				Toast.makeText(AfterSaleProgressActivity.this, "登录过期请重新登录", Toast.LENGTH_SHORT).show();
			} else {
				Tools.showToast(this, json.optString("resultMsg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
		}
	}

}