package com.juxun.business.street.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.juxun.business.street.bean.StoreCardBean;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author luoming 新增储值卡
 */
public class AddCardActivity extends BaseActivity implements OnClickListener, TextWatcher {
    @ViewInject(R.id.et_top_up_one)
    private EditText et_top_up_one;// 充值金额
    @ViewInject(R.id.et_giving_one)
    private EditText et_giving_one;// 赠送金额

    @ViewInject(R.id.layout2)
    private LinearLayout layout2;
    @ViewInject(R.id.iv_two)
    private ImageView iv_two;
    @ViewInject(R.id.et_top_up_two)
    private EditText et_top_up_two;// 充值金额
    @ViewInject(R.id.et_giving_two)
    private EditText et_giving_two;// 赠送金额

    @ViewInject(R.id.layout3)
    private LinearLayout layout3;
    @ViewInject(R.id.iv_three)
    private ImageView iv_three;
    @ViewInject(R.id.et_top_up_three)
    private EditText et_top_up_three;// 充值金额
    @ViewInject(R.id.et_giving_three)
    private EditText et_giving_three;// 赠送金额

    @ViewInject(R.id.layout4)
    private LinearLayout layout4;
    @ViewInject(R.id.iv_four)
    private ImageView iv_four;
    @ViewInject(R.id.et_top_up_four)
    private EditText et_top_up_four;// 充值金额
    @ViewInject(R.id.et_giving_four)
    private EditText et_giving_four;// 赠送金额

    @ViewInject(R.id.tv_add)
    private TextView tv_add;
    @ViewInject(R.id.btn_next)
    private Button btn_next;

    private int Thelayernumber = 1;
    private List<StoreCardBean> mCardBeans = new ArrayList<StoreCardBean>();

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcard);
        ViewUtils.inject(this);
        initTitle();
        title.setText("编辑储值卡");
        initOclik();
        btn_next.setEnabled(initBtn());
        mCardBeans = (List<StoreCardBean>) getIntent().getSerializableExtra("CardBeans");
        if (mCardBeans.size() > 0) {
            Thelayernumber = mCardBeans.size();
            initContent();
        }
    }

    private boolean isOk(double top, double giving) {
        if (top > giving) {
            return true;
        } else {
            Tools.showToast(AddCardActivity.this, "赠送金额不可大于储值金额");
            return false;
        }
    }


    private void initOclik() {
        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mCardBeans = new ArrayList<StoreCardBean>();
                switch (Thelayernumber) {
                    case 4:
                        StoreCardBean cardBean4 = new StoreCardBean();
                        double top_up_four = Double.valueOf(et_top_up_four.getText().toString());
                        double giving_four = Double.valueOf(et_giving_four.getText().toString());
                        if (isOk(top_up_four, giving_four)) {
                            cardBean4.setRecharge_money((int) top_up_four * 100);
                            cardBean4.setGive_money((int) giving_four * 100);
                            mCardBeans.add(cardBean4);
                        }

                    case 3:
                        if (isOk(Double.valueOf(et_top_up_three.getText().toString()), Double.valueOf(et_giving_three.getText().toString()))) {
                            StoreCardBean cardBean3 = new StoreCardBean();
                            cardBean3.setRecharge_money((int) (Double.valueOf(et_top_up_three.getText().toString()) * 100));
                            cardBean3.setGive_money((int) (Double.valueOf(et_giving_three.getText().toString()) * 100));
                            mCardBeans.add(cardBean3);
                        }

                    case 2:
                        if (isOk(Double.valueOf(et_top_up_two.getText().toString()), Double.valueOf(et_giving_two.getText().toString()))) {
                            StoreCardBean cardBean2 = new StoreCardBean();
                            cardBean2.setRecharge_money((int) (Double.valueOf(et_top_up_two.getText().toString()) * 100));
                            cardBean2.setGive_money((int) (Double.valueOf(et_giving_two.getText().toString()) * 100));
                            mCardBeans.add(cardBean2);
                        }

                    case 1:
                        if (isOk(Double.valueOf(et_top_up_one.getText().toString()), Double.valueOf(et_giving_one.getText().toString()))) {
                            StoreCardBean cardBean = new StoreCardBean();
                            cardBean.setRecharge_money((int) (Double.valueOf(et_top_up_one.getText().toString()) * 100));
                            cardBean.setGive_money((int) (Double.valueOf(et_giving_one.getText().toString()) * 100));
                            mCardBeans.add(cardBean);
                        }
                        break;
                    default:
                        break;
                }
                if (mCardBeans.size() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("cardbeans", (Serializable) mCardBeans);
                    setResult(RESULT_OK, intent);
                    finish();
                }


            }
        });
        // 继续添加
        tv_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Thelayernumber < 4) {
                    Thelayernumber++;
                    initContent();
                } else {
                    Tools.showToast(AddCardActivity.this, "最多添加4张储值卡");
                }

            }
        });
        iv_two.setOnClickListener(this);
        iv_three.setOnClickListener(this);
        iv_four.setOnClickListener(this);
        et_giving_one.addTextChangedListener(this);
        et_giving_two.addTextChangedListener(this);
        et_giving_three.addTextChangedListener(this);
        et_giving_four.addTextChangedListener(this);
        et_top_up_one.addTextChangedListener(this);
        et_top_up_two.addTextChangedListener(this);
        et_top_up_three.addTextChangedListener(this);
        et_top_up_four.addTextChangedListener(this);

    }

    private void initContent() {
        switch (Thelayernumber) {
            case 1:
                layout2.setVisibility(View.GONE);
                layout3.setVisibility(View.GONE);
                layout4.setVisibility(View.GONE);
                break;
            case 2:
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.GONE);
                layout4.setVisibility(View.GONE);
                break;
            case 3:
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.GONE);
                break;
            case 4:
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }

        switch (mCardBeans.size()) {
            case 4:
                et_top_up_four.setText(Tools.getFenYuan(mCardBeans.get(3).getRecharge_money()) + "");
                et_giving_four.setText(Tools.getFenYuan(mCardBeans.get(3).getGive_money()) + "");
            case 3:
                et_top_up_three.setText(Tools.getFenYuan(mCardBeans.get(2).getRecharge_money()) + "");
                et_giving_three.setText(Tools.getFenYuan(mCardBeans.get(2).getGive_money()) + "");
            case 2:
                et_top_up_two.setText(Tools.getFenYuan(mCardBeans.get(1).getRecharge_money()) + "");
                et_giving_two.setText(Tools.getFenYuan(mCardBeans.get(1).getGive_money()) + "");
            case 1:
                et_top_up_one.setText(Tools.getFenYuan(mCardBeans.get(0).getRecharge_money()) + "");
                et_giving_one.setText(Tools.getFenYuan(mCardBeans.get(0).getGive_money()) + "");
                break;
            default:
                break;
        }
        btn_next.setEnabled(initBtn());
    }

    private boolean initBtn() {
        if (Thelayernumber == 1 && et_giving_one.getText().length() > 0 && et_top_up_one.getText().length() > 0) {
            btn_next.setBackgroundResource(R.drawable.button_bg1);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            return true;
        } else if (Thelayernumber == 2 && et_giving_one.getText().length() > 0 && et_top_up_one.getText().length() > 0
                && et_giving_two.getText().length() > 0 && et_top_up_two.getText().length() > 0) {
            btn_next.setBackgroundResource(R.drawable.button_bg1);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            return true;
        } else if (Thelayernumber == 3 && et_giving_one.getText().length() > 0 && et_top_up_one.getText().length() > 0
                && et_giving_two.getText().length() > 0 && et_top_up_two.getText().length() > 0
                && et_giving_three.getText().length() > 0 && et_top_up_three.getText().length() > 0) {
            btn_next.setBackgroundResource(R.drawable.button_bg1);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            return true;
        } else if (Thelayernumber == 4 && et_giving_one.getText().length() > 0 && et_top_up_one.getText().length() > 0
                && et_giving_two.getText().length() > 0 && et_top_up_two.getText().length() > 0
                && et_giving_three.getText().length() > 0 && et_top_up_three.getText().length() > 0
                && et_giving_four.getText().length() > 0 && et_top_up_four.getText().length() > 0) {
            btn_next.setBackgroundResource(R.drawable.button_bg1);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            return true;
        } else {
            btn_next.setBackgroundResource(R.drawable.button_bg);
            btn_next.setTextColor(getResources().getColor(R.color.jiujiujiu));
            return false;
        }

    }

    @Override
    public void onResponse(String response, String url) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_two:
                Thelayernumber = 1;
                break;
            case R.id.iv_three:
                Thelayernumber = 2;
                break;
            case R.id.iv_four:
                Thelayernumber = 3;
                break;
            default:
                break;
        }
        initContent();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
        btn_next.setEnabled(initBtn());
    }

}
