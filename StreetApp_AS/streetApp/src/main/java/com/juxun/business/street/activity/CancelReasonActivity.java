package com.juxun.business.street.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CancelReasonActivity 取消订单原因 2017/8/16 罗明
 */
public class CancelReasonActivity extends BaseActivity implements
        OnClickListener {
    @ViewInject(R.id.lv_list)
    private ListView lv_list;
    private View headView, footview;
    private AfterReasonAdatper mAdapter;
    private Button btn_next;
    private LinearLayout ll_why;// 其他原因
    private ImageView iv_why_img;// 其他原因的勾选
    private EditText et_why;// 其他原因的文本
    private List<String> sTringlist=new ArrayList<String>();
    private String order_id;
    private String whystring;
    private int abolish_type = -1;  //-1,1,2,3,4,5,0其它  由列表确认？

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("取消订单");
        initView();
    }

    public void initView() {
        order_id = getIntent().getStringExtra("order_id");
        mAdapter = new AfterReasonAdatper(this);
        lv_list.setAdapter(mAdapter);
        headView = LayoutInflater.from(this).inflate(
                R.layout.after_reason_headview, null);
        footview = LayoutInflater.from(this).inflate(
                R.layout.after_cancel_reason_footview, null);
        btn_next = (Button) footview.findViewById(R.id.btn_next);
        ll_why = (LinearLayout) footview.findViewById(R.id.ll_why);
        iv_why_img = (ImageView) footview.findViewById(R.id.iv_img);
        et_why = (EditText) footview.findViewById(R.id.et_why);
        lv_list.addHeaderView(headView);
        lv_list.addFooterView(footview);
        ll_why.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                iv_why_img.setVisibility(View.VISIBLE);
                et_why.setVisibility(View.VISIBLE);
                mAdapter.ChooseId(-1);
                btn_next.setEnabled(initBtn(-1));
            }
        });
        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                closeOrder();
            }
        });
        et_why.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btn_next.setEnabled(initBtn(-1));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRefundRemarkList();
    }

    // 获取售后退款原因列表
    private void getRefundRemarkList() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.getCloseOrderTextList, this,
                this, map));
    }

    // 取消订单
    private void closeOrder() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token());
        //取消类型 -1：管理员主动取消 1：重复下单/误下单 2：不想买了
        // 3：操作有误（商品、地址选错） 4、其他渠道价格更低 5、订单不能按照预计时间送达 * 0：其他
        map.put("abolish_type", 0 + "");
        if (abolish_type == 0) {
            map.put("abolish", whystring);
        }
        map.put("order_id", order_id);
        mQueue.add(ParamTools.packParam(Constants.closeOrder, this, this, map));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                CancelReasonActivity.this.finish();
                break;
        }
    }

    private boolean initBtn(int chooseid) {
        if (chooseid > -1) {
            btn_next.setBackgroundResource(R.drawable.button_bg1);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            whystring = sTringlist.get(chooseid);
            return true;
        } else {
            if (et_why.getText().length() > 0) {
                btn_next.setBackgroundResource(R.drawable.button_bg1);
                btn_next.setTextColor(getResources().getColor(R.color.white));
                whystring = et_why.getText().toString();
                return true;
            } else {
                btn_next.setBackgroundResource(R.drawable.button_bg);
                btn_next.setTextColor(getResources()
                        .getColor(R.color.jiujiujiu));
                return false;
            }
        }
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.optInt("status");
            String resultMsg = json.optString("msg");
            if (status == 0) {
                if (url.contains(Constants.getCloseOrderTextList)) {
                    JSONArray resultJson = json.optJSONArray("result");
                    for (int i = 0; i < resultJson.length(); i++) {
                        if (resultJson.getJSONObject(i).optString("type_text") != null) {
                            sTringlist.add(resultJson.getJSONObject(i).optString("type_text"));
                        }
                    }
                    mAdapter.UpdateTheData(sTringlist);
                } else if (url.contains(Constants.closeOrder)) {
                    Tools.showToast(CancelReasonActivity.this, "取消订单成功");
                    int order_state = json.optInt("order_state");
                    Intent intent = new Intent();
                    intent.putExtra("state", order_state);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } else {
                Tools.showToast(this, resultMsg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    class AfterReasonAdatper extends BaseAdapter {

        private Context mContext;
        private int chooseid = -1;
        private List<String> sTringlist;

        public AfterReasonAdatper(Context context) {
            sTringlist = new ArrayList<String>();
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return sTringlist.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // 更新数据
        public void UpdateTheData(List<String> list) {
            this.sTringlist = list;
            notifyDataSetChanged();
        }

        public void ChooseId(int chooseid) {
            this.chooseid = chooseid;
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.after_reason_itm, null);
            TextView tv_why_name = (TextView) convertView
                    .findViewById(R.id.tv_why_name);
            ImageView iv_img = (ImageView) convertView
                    .findViewById(R.id.iv_img);
            tv_why_name.setText(sTringlist.get(position));
            if (chooseid == position) {
                iv_img.setVisibility(View.VISIBLE);
            } else {
                iv_img.setVisibility(View.GONE);
            }
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ChooseId(position);
                    iv_why_img.setVisibility(View.GONE);
                    et_why.setVisibility(View.GONE);
                    btn_next.setEnabled(initBtn(position));
                }
            });
            return convertView;
        }
    }
}