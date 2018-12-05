package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.BnakAdapter;
import com.juxun.business.street.bean.BankBranchs;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.CharacterParser;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luoming 支行列表
 */
public class BankListActivity extends BaseActivity implements OnItemClickListener {
    private EditText searchView = null;
    private ListView city_list = null;
    private BnakAdapter adapter = null;
    private List<BankBranchs> bankBranchs = null;
    private CharacterParser characterParser = null;
    public static BankListActivity selectCity = null;
    private TextView dialog;
    private List<BankBranchs> bankBranchs2 = null;

    // 传递过来 所属银行 城市code；
    private String belongs_bank;
    private String city_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banl_list);
        ViewUtils.inject(this);
        initTitle();
        title.setText("支行选择");
        belongs_bank = getIntent().getStringExtra("belongs_bank");
        city_code = getIntent().getStringExtra("city_code");

        //初始化控件
        city_list = (ListView) findViewById(R.id.city_list);
        searchView = (EditText) findViewById(R.id.et_search);
        searchView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterData(searchView.getText().toString());

            }
        });
        dialog = (TextView) findViewById(R.id.dialog);

        characterParser = CharacterParser.getInstance();
        bankBranchs = new ArrayList<>();
        getBankBranchsByCityCode();
    }

    private void getBankBranchsByCityCode() {
        Map<String, String> map = new HashMap<>();
        map.put("city_code", city_code);    //城市列表
        map.put("bank_name", belongs_bank);    //银行的名字
        map.put("branch_name", "");        //支行的名字
       // map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.getBankBranchsByCityCode, this, this, map));
        loading();
    }

    /**
     * 根据输入框中的�?�来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<BankBranchs> filterDateList = new ArrayList<BankBranchs>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = bankBranchs;
        } else {
            filterDateList.clear();
            for (BankBranchs bankBranch : bankBranchs) {
                String name = bankBranch.getBranch_bank_name();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(bankBranch);
                }
            }
        }

        // 根据a-z进行排序
        // Collections.sort(filterDateList, pinyinComparator);
        bankBranchs2 = filterDateList;
        adapter.updateListView(filterDateList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String cityname = bankBranchs2.get(position).getBranch_bank_name();
        Intent intent = new Intent();
        intent.putExtra("cname", cityname);
        intent.putExtra("account_bank_branch_code", bankBranchs2.get(position).getBranch_bank_no());
        intent.putExtra("account_bank_address", bankBranchs2.get(position).getBranch_bank_address());

        setResult(RESULT_OK, intent);// 定义返回的参数parama
        finish();
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int resultCode = json.getInt("status");
            if (resultCode == 0) {
                if (url.contains(Constants.getBankBranchsByCityCode)) {
                    String jsonString = json.getString("result");
                    bankBranchs = JSON.parseArray(jsonString, BankBranchs.class);
                    adapter = new BnakAdapter(BankListActivity.this, bankBranchs);
                    bankBranchs2 = bankBranchs;
                    city_list.setAdapter(adapter);
                    city_list.setOnItemClickListener(BankListActivity.this);
                }
            } else if (resultCode == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(this, LoginActivity.class, true);
                Tools.showToast(this, "登录过期请重新登录?");
            } else {
                Tools.showToast(getApplicationContext(), "接口错误");
            }
        } catch (JSONException e) {
            Tools.showToast(getApplicationContext(), "解析数据错误");
        }
    }
}
