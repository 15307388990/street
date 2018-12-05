package com.yl.ming.efengshe.wxapi;

import org.json.JSONException;
import org.json.JSONObject;

import com.juxun.business.street.activity.BaseActivity;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.Tools;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;
	public static final int ERR_OK = 0;// (用户同意)
	public static final int ERR_AUTH_DENIED = -4;// （用户拒绝授权）
	public static final int ERR_USER_CANCEL = -2;// （用户取消）

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("yj", "onWechatLoginonCreate");
		// setContentView(R.layout.pay_result);
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
		api.handleIntent(getIntent(), this);
		Tools.acts.add(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		Toast.makeText(this, "onWeChatReq", Toast.LENGTH_SHORT).show();
		//BaseResp.ErrCode.ERR_AUTH_DENIED
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d("yj", "onWeChatResp, errCode = " + resp.errCode);
		// Toast.makeText(this, "onWeChatResp", Toast.LENGTH_SHORT).show();
		switch (resp.getType()) {

		case ConstantsAPI.COMMAND_SENDAUTH:
			switch (resp.errCode) {
			case ERR_OK:
				Log.d("yj", "onResp");
				break;
			case ERR_USER_CANCEL:
				// Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
				Tools.exit();
				finish();
				break;
			}
			break;

		case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
			switch (resp.errCode) {
			
			case ERR_OK:
				finish();
				Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
				break;
			case ERR_USER_CANCEL:
				finish();
				Toast.makeText(this, "取消分享", Toast.LENGTH_SHORT).show();
				break;
			}
			break;

		default:
			break;
		}
	}


	@Override
	public void onResponse(String response, String url) {
		try {
			JSONObject json = new JSONObject(response);

		} catch (JSONException e) {
			// TODO: handle exception
			Tools.showToast(getApplicationContext(), "解析数据错误");
		}

	}
}