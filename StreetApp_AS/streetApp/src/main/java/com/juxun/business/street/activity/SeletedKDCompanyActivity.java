package com.juxun.business.street.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.yl.ming.efengshe.R;
import com.juxun.business.street.adapter.KDSelectedAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
/**
 * 
 * @author Lifucheng
 * 选择快递公司
 *
 */

public class SeletedKDCompanyActivity extends Activity implements OnClickListener {
	private TextView cancle;
	private TextView right_view_text;
	private ListView kd_list;
	private String[] kd_arr = {"圆通快递","申通快递","中通快递","韵达快递","顺丰快递",
			"EMS","宅急送","天天快递","速尔快递","全峰快递","京东快递","如风达快递"};
	private KDSelectedAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_kd_type);
		initViews();
		initValues();
		bindEvents();
		
	}

	
	private void initViews() {
		cancle = (TextView) findViewById(R.id.cancel);
		right_view_text = (TextView) findViewById(R.id.right_view_text);
		kd_list = (ListView) findViewById(R.id.kd_list);
		kd_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				String kd_name = kd_arr[position];
			    Intent intent = getIntent();
				 intent.putExtra("kd_name",kd_name);
		    	 setResult(2, intent);
		    	 finish();
			}
			
			
		});
	}


	private void initValues() {
		cancle.setText("取消");
		right_view_text.setText("确认");
		adapter = new KDSelectedAdapter(this, kd_arr);
		kd_list.setAdapter(adapter);
		
		
	}
	private void bindEvents() {
		cancle.setOnClickListener(this);
		right_view_text.setOnClickListener(this);
		
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			finish();
			break;
       case R.id.right_view_text:
		   Intent intent = getIntent();
		   intent.putExtra("kd_name", kd_arr[0]);
    	   setResult(2, intent);
    	   finish();
    	   
			break;

		default:
			break;
		}
		
	}

}
