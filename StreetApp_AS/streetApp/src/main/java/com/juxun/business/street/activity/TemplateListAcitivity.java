/**
 * 
 */
package com.juxun.business.street.activity;

import java.util.List;

import com.juxun.business.street.adapter.TemplateAdapter;
import com.juxun.business.street.bean.MallTemplateBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * 类名称：TemplateListAcitivity 类描述：模板列表 首页 创建人：罗富贵 创建时间：2016年9月23日
 * 
 * @version
 * 
 */
public class TemplateListAcitivity extends BaseActivity {
	@ViewInject(R.id.button_function)
	private TextView button_function;// 手动添加
	@ViewInject(R.id.button_back)
	private ImageView button_back;// 返回
	@ViewInject(R.id.lv_list)
	private ListView lv_list;// 列表
	private List<MallTemplateBean> list;
	private TemplateAdapter mTemplateAdapter;
	String code;
	MallTemplateBean mallTemplateBean = new MallTemplateBean();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_templatelist);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		list = (List<MallTemplateBean>) getIntent().getSerializableExtra("list");
		code = getIntent().getStringExtra("code");
		mallTemplateBean.setCode(code);
		if (list != null) {
			mTemplateAdapter = new TemplateAdapter(TemplateListAcitivity.this, list);
			lv_list.setAdapter(mTemplateAdapter);
		}
	}

	/** 单击事件 */
	@OnClick({ R.id.button_function,R.id.button_back})
	public void clickMethod(View v) {
		if (v.getId() == R.id.button_function) {
			Intent intent = new Intent(TemplateListAcitivity.this, AddGoodsAcitivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("mallTemplateBean", mallTemplateBean);
			intent.putExtras(bundle);
			startActivity(intent);
		}else if(v.getId()==R.id.button_back){
			TemplateListAcitivity.this.finish();
		}
	}

	@Override
	public void onResponse(String response, String url) {
	}
}
