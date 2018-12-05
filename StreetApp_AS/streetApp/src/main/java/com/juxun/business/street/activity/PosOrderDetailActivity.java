package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yl.ming.efengshe.R;
import com.juxun.business.street.bean.PosOrderBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PosOrderDetailActivity extends BaseActivity {
	@ViewInject(R.id.order_price)
	private TextView order_price;
	@ViewInject(R.id.paid_state)
	private TextView paid_state;
	@ViewInject(R.id.order_number)
	private TextView order_number;
	@ViewInject(R.id.payments)
	private TextView payments;
	@ViewInject(R.id.order_time)
	private TextView order_time;
	@ViewInject(R.id.transaction_time)
	private TextView transaction_time;
	private PosOrderBean bean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pos_order_detail);
		ViewUtils.inject(this);
		initTitle();
		title.setText(getString(R.string.order_detail));
		initValues();
	}
    
	private void initValues() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		bean = (PosOrderBean) bundle.getSerializable("PosOrderBean");
		order_price.setText("¥"+bean.getOrder_price());
		order_number.setText(bean.getOrder_id());
		int state = bean.getOrder_state();
		//1：付款成功 2：付款失败 3：未付款
		String str1= null;
		if(state == 1){
			str1  = "付款成功";
		}else if(state ==2){
			str1 = "付款失败";
		}else if(state == 3){
			str1 = "未付款";
		}
		paid_state.setText(str1);
		int type = bean.getOrder_pay_type();
		String str = null;
		if(type == 1){
			str = "支付宝";
		}else if(type == 2){
			str = "微信";
		}else if(type == 3){
			str = "银联支付";
		}	
		payments.setText(str);
		order_time.setText(bean.getOrder_creat_date());
		transaction_time.setText(bean.getOrder_end_date());
	}

	@Override
	public void onResponse(String response, String url) {
		
		
	}

}
