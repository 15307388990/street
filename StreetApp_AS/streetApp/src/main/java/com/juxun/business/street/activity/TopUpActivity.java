/**
 *
 */
package com.juxun.business.street.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.bean.TopUpClassBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.GridViewWithHeaderAndFooter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 余额充值
 */
public class TopUpActivity extends BaseActivity implements OnLayoutChangeListener {
    @ViewInject(R.id.rv_view)
    private GridViewWithHeaderAndFooter rv_view;
    @ViewInject(R.id.btn_next)
    private Button btn_next;
    @ViewInject(R.id.tv_text)
    private TextView tv_text;
    @ViewInject(R.id.ll_lyout)
    private LinearLayout ll_lyout;

    // 充值送红包
    String string3 = "说明：\n1、充值的现金记录请在“余额”中查看；\n2、余额充值所赠送的红包，采购商品时可抵扣货款；\n3、充值所赠送的红包，请在“我的红包”中进行查看；\n4、自定义充值金额必须为 100 的整数倍数；\n5、充值活动解释权归e蜂社所有。";
    // 充值打折扣
    String string2 = "说明：\n1、充值的现金记录请在“余额”中查看；\n2、充值活动解释权归e蜂社所有。";
    // 充值送金额
    String string1 = "说明：\n1、充值的现金记录请在“余额”中查看；\n2、充值活动解释权归e蜂社所有。";
    // 常态
    String string0 = "说明：\n1、充值的现金记录请在“余额”中查看；\n2、充值活动解释权归e蜂社所有。";

    private int recharge_activity_id = 0;// 充值活动id,如无活动则为0
    private List<TopUpClassBean> mTopUpClassBeanList;
    DecimalFormat df = new java.text.DecimalFormat("0.00");
    private Adpter mAdpter;
    private int selected = -1;
    private String price = "";// 输入的金额
    private int Amount;// 最终要充值的金额 int
    private int keyHeight;// 三分之一的高度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("充值");

        initView();
    }

    private void initView() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        keyHeight = metric.heightPixels / 3;
        ll_lyout.addOnLayoutChangeListener(this);

        mTopUpClassBeanList = new ArrayList<>();
        mAdpter = new Adpter(mTopUpClassBeanList);
        rv_view.setAdapter(mAdpter);
        rv_view.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
                mAdpter.updateListView(mTopUpClassBeanList);
                btn_next.setClickable(initBtn());
                // 关闭软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
            }
        });
        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (selected == mTopUpClassBeanList.size()) {
                    // 如果选中的是最后那一个 则是输入的金额
                    Amount = Integer.valueOf(price) * 100;
                } else {
                    Amount = mTopUpClassBeanList.get(selected).getRecharge_price();
                }
                Intent intent = new Intent(TopUpActivity.this, TopUpPayDialogActivity.class);
                intent.putExtra("Amount", Amount);
                intent.putExtra("recharge_activity_id", recharge_activity_id);
                startActivityForResult(intent, 2);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    // 充值活动选项列表
    private void findRechargeRecordList() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.findRechargeItemList, this, this, map));
        loading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        price = "";
        btn_next.setClickable(false);
        findRechargeRecordList();
    }

    private boolean initBtn() {
        if (selected >= 0) {
            if (selected == mTopUpClassBeanList.size()) {
                if (price.length() > 0) {
                    btn_next.setBackgroundResource(R.drawable.button_bg1);
                    btn_next.setTextColor(getResources().getColor(R.color.white));
                    return true;
                } else {
                    btn_next.setBackgroundResource(R.drawable.button_bg);
                    btn_next.setTextColor(getResources().getColor(R.color.jiujiujiu));
                    return false;
                }
            } else {
                btn_next.setBackgroundResource(R.drawable.button_bg1);
                btn_next.setTextColor(getResources().getColor(R.color.white));
                return true;
            }
        } else {
            btn_next.setBackgroundResource(R.drawable.button_bg);
            btn_next.setTextColor(getResources().getColor(R.color.jiujiujiu));
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                finish();
            }
        }
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            Log.i("test", response);
            int status = json.getInt("status");
            String msg = json.getString("msg");
            if (status == 0) {
                if (url.contains(Constants.findRechargeItemList)) {
                    mTopUpClassBeanList = JSON.parseArray(json.getString("result"), TopUpClassBean.class);
                    recharge_activity_id = mTopUpClassBeanList.get(0).getRecharge_activity_id();

                    TopUpClassBean topUpClassBean = new TopUpClassBean();
                    topUpClassBean.setId(-1);
                    mTopUpClassBeanList.add(topUpClassBean);
                    mAdpter.updateListView(mTopUpClassBeanList);
                    selected = mTopUpClassBeanList.size();
                }
            } else if (status == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(TopUpActivity.this, LoginActivity.class, false);
                Tools.showToast(TopUpActivity.this, "登录过期请重新登录");
                Tools.acts.clear();
            } else {
                Tools.showToast(getApplicationContext(), msg);
            }
        } catch (JSONException e) {
            Tools.showToast(getApplicationContext(), "解析数据错误");
        }
    }

    class Adpter extends BaseAdapter {
        private List<TopUpClassBean> list;

        public Adpter(List<TopUpClassBean> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return mTopUpClassBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 当ListView数据发生变化时,调用此方法来更新ListView
         *
         * @param list
         */
        public void updateListView(List<TopUpClassBean> list) {
            notifyDataSetChanged();
            this.list = list;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(TopUpActivity.this).inflate(R.layout.top_up_class_itm, null);
            viewHolder.tv_price_one = (TextView) convertView.findViewById(R.id.tv_price_one);
            viewHolder.tv_one = (TextView) convertView.findViewById(R.id.tv_one);
            viewHolder.ll_one = (LinearLayout) convertView.findViewById(R.id.ll_one);

            viewHolder.et_price = (EditText) convertView.findViewById(R.id.et_price);
            convertView.setTag(viewHolder);

            TopUpClassBean topUpClassBean = list.get(position);
            if (topUpClassBean.getId() == -1) {
                viewHolder.et_price.setVisibility(View.VISIBLE);
                viewHolder.ll_one.setVisibility(View.GONE);
            } else {
                viewHolder.et_price.setVisibility(View.GONE);
                viewHolder.ll_one.setVisibility(View.VISIBLE);
                viewHolder.tv_price_one.setText("¥" + Tools.getFenYuan(topUpClassBean.getRecharge_price()));
                //0、默认充值项目\t  \t * 1.充值送现金 \t * 2.充值折扣 \t * 3.充值送红包"
                switch (topUpClassBean.getItem_type()) {
                    case 0:
                        tv_text.setText(string0);
                        viewHolder.tv_one.setVisibility(View.GONE);
                        break;
                    case 1:
                        tv_text.setText(string1);
                        viewHolder.tv_one.setText("充值" + Tools.getFenYuan(topUpClassBean.getRecharge_price()) + "送"
                                + Tools.getFenYuan(topUpClassBean.getLargess_price()) + "元");
                        break;
                    case 2:
                        tv_text.setText(string2);
                        viewHolder.tv_one.setText(topUpClassBean.getDiscount() * 10 + "折，只要" + df.format(
                                Tools.getFenYuan(topUpClassBean.getRecharge_price()) * topUpClassBean.getDiscount()));
                        break;
                    case 3:
                        tv_text.setText(string3);
                        viewHolder.tv_one.setText("送红包" + Tools.getFenYuan(topUpClassBean.getRedpacket_price()));
                        break;
                    default:
                        break;
                }
            }
            if (selected == position) {
                viewHolder.tv_price_one.setTextColor(getResources().getColor(R.color.white));
                viewHolder.tv_one.setTextColor(getResources().getColor(R.color.white));
                viewHolder.ll_one.setBackground(getResources().getDrawable(R.drawable.recharge_btn_n));

            } else {
                viewHolder.tv_price_one.setTextColor(getResources().getColor(R.color.blue));
                viewHolder.tv_one.setTextColor(getResources().getColor(R.color.jiujiujiu));
                viewHolder.ll_one.setBackground(getResources().getDrawable(R.drawable.recharge_btn_d));
                viewHolder.et_price.setText("");
            }
            viewHolder.et_price.addTextChangedListener(textWatcher);
            return convertView;
        }
    }

    class ViewHolder {
        TextView tv_price_one;
        TextView tv_one;
        LinearLayout ll_one;
        EditText et_price;
    }

    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            price = s.toString();
            btn_next.setClickable(initBtn());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
                               int oldBottom) {

        // old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
        // 现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            // 监听到软键盘弹起
            selected = mTopUpClassBeanList.size();
            mAdpter.updateListView(mTopUpClassBeanList);
            price = "";
            btn_next.setClickable(initBtn());

        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            // 监听到软件盘关闭
        }
    }
}
