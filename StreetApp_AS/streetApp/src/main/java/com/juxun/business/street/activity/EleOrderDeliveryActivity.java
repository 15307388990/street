package com.juxun.business.street.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yl.ming.efengshe.R;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.EleOrderBean;
import com.juxun.business.street.bean.ParseModel;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
/**
 * 
 * @author Lifucheng
 * 电商订单发货
 *
 */
public class EleOrderDeliveryActivity extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.express_delivery)
	private RadioButton express_delivery;//快递
	@ViewInject(R.id.without_logistics)
	private RadioButton without_logistics;//无需物流
	@ViewInject(R.id.selected)
	private TextView selected;//选择快递公司
	@ViewInject(R.id.express_delivery_number)
	private EditText express_delivery_number;//快递单号
	@ViewInject(R.id.order_scan)
	private ImageButton order_scan;//二维码扫描
	@ViewInject(R.id.relativeLayout)
	private RelativeLayout relativeLayout;
	@ViewInject(R.id.view1)
	private View view1;
	@ViewInject(R.id.view2)
	private View view2;
	@ViewInject(R.id.view3)
	private View view3;
	@ViewInject(R.id.view4)
	private View view4;
	private EleOrderBean bean;
	private boolean flag = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ele_order_delivery);
		ViewUtils.inject(this);
		initTitle();
		more.setText(getString(R.string.delivery));
		initValues();
		bindEvents();
		
		
	}
	private void initValues() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		bean = (EleOrderBean) bundle.getSerializable("bean");
	}
	private void bindEvents() {
		express_delivery.setOnClickListener(this);
		without_logistics.setOnClickListener(this);
		selected.setOnClickListener(this);
		order_scan.setOnClickListener(this);
		more.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.express_delivery){
			flag = true;
			express_delivery.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.order_check, 0);
			without_logistics.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.order_not_check, 0);
			selected.setVisibility(View.VISIBLE);
			relativeLayout.setVisibility(View.VISIBLE);
			view1.setVisibility(View.VISIBLE);
			view2.setVisibility(View.VISIBLE);
			view3.setVisibility(View.VISIBLE);
			view4.setVisibility(View.VISIBLE);
			
		}else if(v.getId() == R.id.without_logistics){
			flag = false;
			express_delivery.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.order_not_check, 0);
			without_logistics.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.order_check, 0);
			selected.setVisibility(View.INVISIBLE);
			relativeLayout.setVisibility(View.INVISIBLE);
			view1.setVisibility(View.INVISIBLE);
			view2.setVisibility(View.INVISIBLE);
			view3.setVisibility(View.INVISIBLE);
			view4.setVisibility(View.INVISIBLE);
			
		}else if(v.getId() == R.id.selected){
			Intent intent = new Intent(this,SeletedKDCompanyActivity.class);
			startActivityForResult(intent,1);
			overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
			
			
		}else if(v.getId() == R.id.order_scan){
			
			Intent intent = new Intent(this, MipcaActivityCapture.class);
        	startActivityForResult(intent, 2);
		}else if(v.getId() == R.id.right_view_text){
			if(flag){
				if(TextUtils.isEmpty(selected.getText().toString())){
					Toast.makeText(this, "请选择快递公司", 0).show();
					return;
				}
				if(TextUtils.isEmpty(express_delivery_number.getText().toString())){
					Toast.makeText(this, "快递单号不能为空", 0).show();
					return;
				}
				requestHttp();
				loading();
			}else {
				Toast.makeText(this, "快递单号错误", 0).show();
			}
			
		}
	}
	private void requestHttp() {
		Map<String , String> map = new HashMap<String, String>();
		map.put("userId", ParseModel.loginBean.getUserId()+"");
		map.put("authToken", ParseModel.loginBean.getAuthToken()+"");
		map.put("order_id", bean.getId()+"");
		map.put("delivery_name", selected.getText().toString());
		map.put("express_num", express_delivery_number.getText().toString());
		map.put("oper_id", ParseModel.loginBean.getOper_id() + "");
		mQueue.add(ParamTools.packParam(Constants.updateOrderExpress, this, this, map));
	}
	@Override
	public void onResponse(String response, String url) {
		dismissLoading();
	
		try {
			JSONObject json = new JSONObject(response);
			int status = json.getInt("stauts");
			String msg = json.getString("msg");
			if(status == 0){
				finish();
				Toast.makeText(this, msg, 0).show();
			}else {
		    	Tools.dealErrorMsg(this, url, status, msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == 2){
			String qCode = data.getStringExtra("Value");
			if(qCode == null || qCode.length() == 0){
				return ;
			}
			express_delivery_number.setText(qCode);
		}else if(resultCode == 2 && requestCode == 1){
			String kd_name = data.getStringExtra("kd_name");
			if(!TextUtils.isEmpty(kd_name)){
				selected.setText(kd_name);
			}
		}
	}
	

}
