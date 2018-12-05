package com.yl.ming.efengshe.wxapi;

import com.juxun.business.street.util.Tools;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.pay_result);
//		Tools.acts.add(this);
//		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
//		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		Intent intent = new Intent();
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			// AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// builder.setTitle("提示");
			// builder.setMessage(getString(R.string.pay_result_callback_msg,
			// resp.errStr + ";code=" + String.valueOf(resp.errCode)));
			// builder.show();
			switch (resp.errCode) {
			case -2:
				Tools.showToast(WXPayEntryActivity.this, "支付取消");
				break;
			case 0:
				Tools.showToast(WXPayEntryActivity.this, "支付成功");
				break;
			case -1:
				Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
				
				break;

			default:
				break;
			}
			startActivityForResult(intent, 1000);
			finish();
		}
	}
}