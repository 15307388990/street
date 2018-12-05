/**
 * 
 */
package com.juxun.business.street.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.juxun.business.street.bean.CommodityInfoBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.TimerDateUtil;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.SelectBirthdayPop;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * 类名称：AmendShekflifeAcitivity 类描述：修改库存及过期日期 创建人：罗富贵 创建时间：2016年10月9日
 * 
 * @version
 * 
 */
public class AmendShekflifeAcitivity extends BaseActivity {
	// 过期时间
	@ViewInject(R.id.tv_date)
	private TextView tv_date;
	// 商品库存
	@ViewInject(R.id.et_inventory)
	private TextView et_inventory;
	// 返回
	@ViewInject(R.id.button_back)
	private ImageView button_back;
	// 提交
	@ViewInject(R.id.btn_query)
	private Button btn_query;

	private int inventoryId;// 库存Id（获取上架或下架状态商品时才需传）
	private CommodityInfoBean mCommodityInfoBean;
	private SelectBirthdayPop birth;
	TimerDateUtil timerDate = new TimerDateUtil();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_amend_shekflife);
		ViewUtils.inject(this);
		initView();
		initDate();

	}

	private void initView() {
		mCommodityInfoBean = (CommodityInfoBean) getIntent()
				.getSerializableExtra("mCommodityInfoBean");
		inventoryId = mCommodityInfoBean.getInventory_id();
	}

	private void initDate() {
		et_inventory.setHint("当前库存"
				+ mCommodityInfoBean.getCommodity_inventory() + "");
		tv_date.setText(Tools.getDateformat(mCommodityInfoBean
				.getExpirationTime()));

	}

	/** 单击事件 */
	@OnClick({ R.id.button_back, R.id.tv_date, R.id.btn_query })
	public void clickMethod(View v) {
		if (v.getId() == R.id.button_back) {
			this.finish();
		} else if (v.getId() == R.id.tv_date) {
			InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
			boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
			if (isOpen) {
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
			birth = new SelectBirthdayPop(AmendShekflifeAcitivity.this,
					timerDate.getNowDate(), tv_date);
			birth.showAtLocation(tv_date, Gravity.BOTTOM, 0, 0);
		} else if (v.getId() == R.id.btn_query) {
			// 重复点击判断
			if (!Tools.isFastDoubleClick()) {
				// 不为空
				if (!TextUtils.isEmpty(et_inventory.getText())) {
					int inven = Integer.valueOf(et_inventory.getText()
							.toString());
					if (inven < mCommodityInfoBean.getCommodity_inventory()) {
						Tools.showToast(getApplicationContext(), "修改库存不能小于现有库存");
					} else {
						if (mCommodityInfoBean.getSale_state() != 1) {
							// 判断当前时间是否大于过期时间 如果大于那商品需要重新上架弹框 否则不需要
							if (mCommodityInfoBean.isExpirated()
									&& !tv_date
											.getText()
											.toString()
											.equals(Tools
													.getDateformat(mCommodityInfoBean
															.getExpirationTime()))) {
								//判断商品是否处于上架状态
								if (mCommodityInfoBean.getSale_state() == 2) {
									getCommodityInfo();
								}else {
								initDialog();
								}
							} else {
								getCommodityInfo();
							}
						} else {
							getCommodityInfo();
						}

					}
				} else {
					if (mCommodityInfoBean.getSale_state() != 1) {
						// 判断当前时间是否大于过期时间 如果大于那商品需要重新上架弹框 否则不需要
						if (mCommodityInfoBean.isExpirated()&&!tv_date
								.getText()
								.toString()
								.equals(Tools
										.getDateformat(mCommodityInfoBean
												.getExpirationTime()))) {
							//判断商品是否处于上架状态
							if (mCommodityInfoBean.getSale_state() == 2) {
								getCommodityInfo();
							}else {
							initDialog();
							}
						} else {
							getCommodityInfo();
						}
					} else {
						getCommodityInfo();
					}
				}
			}

		}
	}

	private void initDialog() {
		AlertDialog.Builder builder = new Builder(this,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		builder.setMessage("此商品是否需要重新上架？");
		builder.setTitle("重新上架");
		builder.setPositiveButton("重新上架",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						getCommodityInfo();
						upShelf();
					}
				});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// do nothing
				getCommodityInfo();
			}
		});

		builder.create().show();

	}

	private void getCommodityInfo() {
		Map<String, String> map = new HashMap<String, String>();
		// inventoryId int 商品货架id
		// commodity_inventory int 商品库存 无论是否更改都要传
		// expirationTime String 过期时间 ‘yyyy-MM-dd’ 无论是否更改都要传
		map.put("auth_token", partnerBean.getAuth_token()+"");
		if (TextUtils.isEmpty(et_inventory.getText())) {
			map.put("commodity_inventory",
					mCommodityInfoBean.getCommodity_inventory() + "");
		} else {
			map.put("commodity_inventory", et_inventory.getText().toString());
		}
		map.put("commodity_id", mCommodityInfoBean.getCommodity_id() + "");
		map.put("expirationTime", tv_date.getText().toString());
		mQueue.add(ParamTools
				.packParam(Constants.editInvOrExp, this, this, map));
		loading();
	}

	private void upShelf() {
		Map<String, String> map = new HashMap<String, String>();
		// agencyId int 机构id
		// inventoryId int 商品货架id
		// map.put("inventoryId", inventoryId + "");
		map.put("commodity_id", mCommodityInfoBean.getCommodity_id() + "");
		map.put("auth_token", partnerBean.getAuth_token());
		mQueue.add(ParamTools.packParam(Constants.upShelf, this, this, map));
		loading();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.optInt("status");
			String msg = json.optString("msg");
			if (stauts == 0) {
				if (url.contains(Constants.editInvOrExp)) {
					// 判断当前时间是否大于过期时间 如果大于那商品需要重新上架弹框 否则不需要
					Tools.showToast(getApplicationContext(), "修改成功");
					finish();
				} else if (url.contains(Constants.upShelf)) {
					Tools.showToast(getApplicationContext(), "上架成功");
					finish();
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
