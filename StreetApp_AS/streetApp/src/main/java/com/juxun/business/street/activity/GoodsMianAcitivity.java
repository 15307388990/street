/**
 * 
 */
package com.juxun.business.street.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.juxun.business.street.UILApplication;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.oubowu.slideback.SlideBackHelper;
import com.oubowu.slideback.SlideConfig;
import com.oubowu.slideback.widget.SlideBackLayout;
import com.yl.ming.efengshe.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 类名称：GoodsMianAcitivity 类描述：商品首页 首页 创建人：罗富贵 创建时间：2016年9月13日
 * 
 * @version
 * 
 */
public class GoodsMianAcitivity extends BaseActivity {

	@ViewInject(R.id.ll_inthesale)
	private LinearLayout ll_inthesale;// 出售中的商品
	@ViewInject(R.id.ll_noton)
	private LinearLayout ll_noton;// 未上架的商品
	@ViewInject(R.id.ll_understock)
	private LinearLayout ll_understock;// 库存不足的商品
	@ViewInject(R.id.ll_willexpire)
	private LinearLayout ll_willexpire;// 即将过期的商品
	@ViewInject(R.id.ll_otherstate)
	private LinearLayout ll_otherstate;// 其它状态的商品
	@ViewInject(R.id.iv_san)
	private ImageView iv_san;// 扫描添加商品
	@ViewInject(R.id.button_back)
	private ImageView button_back;// 返回
	@ViewInject(R.id.button_function)
	private ImageView button_function;// 查找
	@ViewInject(R.id.tv_understock)
	private TextView tv_understock;// 库存不足的商品数量
	@ViewInject(R.id.tv_willexpire)
	private TextView tv_willexpire;// 即将过期的商品数量
	private SlideBackLayout mSlideBackLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_goodsmian);
		mSlideBackLayout = SlideBackHelper.attach(
				// 当前Activity
				this,
				// Activity栈管理工具
				UILApplication.getActivityHelper(),
				// 参数的配置
				new SlideConfig.Builder()
						// 屏幕是否旋转
						.rotateScreen(false)
						// 是否侧滑
						.edgeOnly(false)
						// 是否禁止侧滑
						.lock(false)
						// 侧滑的响应阈值，0~1，对应屏幕宽度*percent
						.edgePercent(0.1f)
						// 关闭页面的阈值，0~1，对应屏幕宽度*percent
						.slideOutPercent(0.5f).create(),
				// 滑动的监听
				null);
		ViewUtils.inject(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getExpAndLessCnt();
	}

	// 获取过期商品和少库存商品数量
	private void getExpAndLessCnt() {
		Map<String, String> map = new HashMap<String, String>();
		// community_id int 社区id
		map.put("auth_token", partnerBean.getAuth_token());
		mQueue.add(ParamTools.packParam(Constants.getExpAndLessCnt, this, this, map));
	}

	/** 单击事件 */
	@OnClick({ R.id.ll_inthesale, R.id.ll_noton, R.id.ll_understock, R.id.ll_willexpire, R.id.ll_otherstate,
			R.id.iv_san, R.id.button_back, R.id.button_function })
	public void clickMethod(View v) {
		if (v.getId() == R.id.ll_inthesale) {
			Tools.jump(this, InthesaleAcitivity.class, false);
		} else if (v.getId() == R.id.ll_noton) {
			Tools.jump(this, NoGoodsShelvesAcitivity.class, false);
		} else if (v.getId() == R.id.ll_understock) {
			Tools.jump(this, UnderstockAcitivity.class, false);
		} else if (v.getId() == R.id.ll_willexpire) {
			Tools.jump(this, OverdueAcitivity.class, false);
		} else if (v.getId() == R.id.ll_otherstate) {
			Tools.jump(this, OtherStateAcitivity.class, false);
		} else if (v.getId() == R.id.iv_san) {
			Intent intent = new Intent(GoodsMianAcitivity.this, MipcaActivityCapture.class);
			intent.putExtra("type", 1);
			startActivity(intent);
		} else if (v.getId() == R.id.button_back) {
			this.finish();
		} else if (v.getId() == R.id.button_function) {
			Tools.jump(this, GoodsSearchAcitivity.class, false);
		}
	}

	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int stauts = json.optInt("stauts");
			String msg = json.optString("msg");
			if (stauts == 0) {
				// lessInvCnt int 库存不足商品数量
				// pastDateCnt int 即将过期商品数量
				int lessInvCnt = json.optInt("lessInvCnt");
				int pastDateCnt = json.optInt("pastDateCnt");

				if (lessInvCnt > 0) {
					tv_understock.setText(lessInvCnt + "");
					tv_understock.setTextColor(getResources().getColor(R.color.red));
				} else {
					tv_understock.setText("无");
					tv_understock.setTextColor(getResources().getColor(R.color.gray));

				}
				if (pastDateCnt > 0) {
					tv_willexpire.setText(pastDateCnt + "");
					tv_willexpire.setTextColor(getResources().getColor(R.color.red));
				} else {
					tv_willexpire.setText("无");
					tv_willexpire.setTextColor(getResources().getColor(R.color.gray));
				}

			} else if (stauts == -4004) {
				mSavePreferencesData.putStringData("json", "");
				Tools.jump(this, LoginActivity.class, true);
				Tools.showToast(this, "登录过期请重新登录");
				Tools.exit();
			} else {
				Tools.showToast(this, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Tools.showToast(this, R.string.tips_unkown_error);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		mSlideBackLayout.isComingToFinish();
	}
}
