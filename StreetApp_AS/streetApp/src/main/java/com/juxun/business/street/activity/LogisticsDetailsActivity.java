package com.juxun.business.street.activity;

/**
 * �����������
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.LogisticsDetalisAdapter;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.LogisticeDetailsModel;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class LogisticsDetailsActivity extends BaseActivity {
	@ViewInject(R.id.button_back)
	private ImageView button_back;// ����
	@ViewInject(R.id.listView)
	private ListView listView;
	@ViewInject(R.id.tv_type)
	private TextView tv_type;
	@ViewInject(R.id.tv_number)
	private TextView tv_number;
	@ViewInject(R.id.tv_state)
	private TextView tv_state;
	// ������� ��ݵ���
	private String delivery_name;
	private String express_num;
	private List<LogisticeDetailsModel> list = new ArrayList<LogisticeDetailsModel>();
	private LogisticsDetalisAdapter logisticsDetalisAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logistices_details);
		ViewUtils.inject(this);
		initView();
		initData();
	}

	public void initView() {
		delivery_name = getIntent().getStringExtra("deliveryName");
		express_num = getIntent().getStringExtra("expressNum");

		logisticsDetalisAdapter = new LogisticsDetalisAdapter(getApplicationContext(), list);
		listView.setAdapter(logisticsDetalisAdapter);
		button_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (delivery_name != null && delivery_name != null) {
			searchExpress();
		} else {
			Tools.showToast(getApplicationContext(), "������Ϣ�д���");
		}

	}

	public void initData() {
		tv_type.setText("��Ϣ��Դ��" + delivery_name + "���");
		tv_number.setText("�˵��ţ�" + express_num);
	}

	private void searchExpress() {
		String expressName = getPingYin(delivery_name);
		Map<String, String> map = new HashMap<String, String>();
		map.put("expressName", expressName);
		map.put("express_num", express_num);
		mQueue.add(ParamTools.packParam(Constants.searchExpress, this, this, map));
		loading();
	}

	private void initState(int state) {
		switch (state) {
		/**
		 * 0����;�������ﴦ����������У� 1���������������ɿ�ݹ�˾���ղ��Ҳ����˵�һ��������Ϣ�� 2�����ѣ�������͹��̳������⣻
		 * 3��ǩ�գ��ռ�����ǩ�գ� 4����ǩ�������������û���ǩ��������ԭ���˻أ����ҷ������Ѿ�ǩ�գ� 5���ɼ�����������ڽ���ͬ���ɼ���
		 * 6���˻أ������������˻ط����˵�;�У�
		 **/
		case 0:
			tv_state.setText("����;��");

			break;
		case 1:
			tv_state.setText("������");

			break;
		case 2:
			tv_state.setText("����");

			break;
		case 3:
			tv_state.setText("ǩ�ճɹ�");

			break;
		case 4:
			tv_state.setText("�û���ǩ");

			break;
		case 5:
			tv_state.setText("�ɼ���");

			break;
		case 6:
			tv_state.setText("���˻�");

			break;

		default:
			break;
		}
	}

	private String getPingYin(String src) {
		if (!src.equals("EMS")) {
			char[] t1 = null;
			t1 = src.toCharArray();
			String[] t2 = new String[t1.length];
			HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
			t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
			t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			t3.setVCharType(HanyuPinyinVCharType.WITH_V);
			String t4 = "";
			int t0 = t1.length;
			try {
				for (int i = 0; i < t0; i++) {
					// �ж��Ƿ�Ϊ�����ַ�
					if (java.lang.Character.toString(t1[i]).matches("[\\һ-\\��]+")) {
						t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
						t4 += t2[0];
					} else
						t4 += java.lang.Character.toString(t1[i]);
				}
				// System.out.println(t4);
				return t4;
			} catch (BadHanyuPinyinOutputFormatCombination e1) {
				e1.printStackTrace();
			}
			return t4;
		} else {
			return "ems";
		}
	}

	@Override
	public void onResponse(String response, String url) {
		// TODO Auto-generated method stub
		dismissLoading();
		try {
			JSONObject json = new JSONObject(response);
			int status = json.getInt("status");
			String msg = json.getString("message");
			if (status == 200) {
				if (url.contains(Constants.searchExpress)) {
					initState(json.getInt("state"));
					String contextString = json.getString("data");
					list = JSON.parseArray(contextString, LogisticeDetailsModel.class);
					logisticsDetalisAdapter.updateListView(list);
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
