/**
 * 
 */
package com.juxun.business.street.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yl.ming.efengshe.R;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @author WuJianHua 分享弹框
 */
public class ShareChooseDialogActivity extends Activity {

	@ViewInject(R.id.weixin)
	private TextView weixin; // 微信
	@ViewInject(R.id.pengyouquan)
	private TextView pengyouquan; // 朋友圈
	@ViewInject(R.id.qq)
	private TextView qq; // QQ
	@ViewInject(R.id.sina)
	private TextView sina; // 新浪微博
	@ViewInject(R.id.cancel)
	private TextView cancel; // 取消
	@ViewInject(R.id.dismissView)
	private TextView dismissView; //半透明区域

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_share_choose);
		ViewUtils.inject(this);
		overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
	}

	/** 单击事件 */
	@OnClick({ R.id.weixin, R.id.pengyouquan, R.id.qq, R.id.sina, R.id.cancel, R.id.dismissView })
	public void clickMethod(View v) {
		switch (v.getId()) {
		case R.id.weixin:
//			Tools.showShare(this);
			break;
		case R.id.pengyouquan:
			break;
		case R.id.qq:
			break;
		case R.id.sina:
			break;
		}
		finish();
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
	}
}
