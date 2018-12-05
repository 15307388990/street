package com.juxun.business.street.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ● ● @version 设置支付密码 one ●
 **/
public class SetPayPasswordOneActivity extends BaseActivity {
    @ViewInject(R.id.zero)
    private TextView zero;// 0
    @ViewInject(R.id.one)
    private TextView one;// 1
    @ViewInject(R.id.two)
    private TextView two;// 2
    @ViewInject(R.id.three)
    private TextView three;// 3
    @ViewInject(R.id.four)
    private TextView four;// 4
    @ViewInject(R.id.five)
    private TextView five;// 5
    @ViewInject(R.id.six)
    private TextView six;// 6
    @ViewInject(R.id.seven)
    private TextView seven;// 7
    @ViewInject(R.id.eight)
    private TextView eight;// 8
    @ViewInject(R.id.nine)
    private TextView nine;// 9
    @ViewInject(R.id.point)
    private TextView point;// .
    @ViewInject(R.id.delete)
    private ImageView delete;// delete

    @ViewInject(R.id.tv_1)
    private TextView tv_1;
    @ViewInject(R.id.tv_2)
    private TextView tv_2;
    @ViewInject(R.id.tv_3)
    private TextView tv_3;
    @ViewInject(R.id.tv_4)
    private TextView tv_4;
    @ViewInject(R.id.tv_5)
    private TextView tv_5;
    @ViewInject(R.id.tv_6)
    private TextView tv_6;
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    private int type;// 0为设置支付密码 1为修改支付密码
    private String sms_code;
    private String oldPayPass;
    private String addnumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);  //type:0是设置支付密码，1是修改支付密码，为0的情况没有传type默认是设置支付密码
        sms_code = intent.getStringExtra("sms_code");
        oldPayPass = intent.getStringExtra("pay_pass");

        if (type == 0) {
            title.setText("设置支付密码");
            back.setVisibility(View.GONE);
        } else if (type == 1) {
            title.setText("修改支付密码");
        }
        tv_title.setText("请设置6位数字支付密码");

    }

    /**
     * 单击事件
     */
    @OnClick({R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight,
            R.id.nine, R.id.point, R.id.delete, R.id.clearing})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.zero:
                add("0");
                break;
            case R.id.one:
                add("1");
                break;
            case R.id.two:
                add("2");
                break;
            case R.id.three:
                add("3");
                break;
            case R.id.four:
                add("4");
                break;
            case R.id.five:
                add("5");
                break;
            case R.id.six:
                add("6");
                break;
            case R.id.seven:
                add("7");
                break;
            case R.id.eight:
                add("8");
                break;
            case R.id.nine:
                add("9");
                break;
            case R.id.delete:
                delete();
                break;
        }
    }

    public void add(String addStr) {
        addnumber = addnumber + addStr;
        initPassword();
        if (addnumber.length() == 6) {
            Intent intent = new Intent(SetPayPasswordOneActivity.this, SetPayPasswordTwoActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("oldPayPass", oldPayPass);
            intent.putExtra("addnumber", addnumber);
            intent.putExtra("sms_code", sms_code);
            startActivity(intent);
            finish();
        }
    }

    public void delete() {
        if (addnumber.length() > 0) {
            addnumber = addnumber.substring(0, addnumber.length() - 1);
            initPassword();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (type == 1) {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initPassword() {
        tv_6.setText("");
        tv_5.setText("");
        tv_4.setText("");
        tv_3.setText("");
        tv_2.setText("");
        tv_1.setText("");
        switch (addnumber.length()) {
            case 6:
                tv_6.setText("●");
            case 5:
                tv_5.setText("●");
            case 4:
                tv_4.setText("●");
            case 3:
                tv_3.setText("●");
            case 2:
                tv_2.setText("●");
            case 1:
                tv_1.setText("●");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.optInt("status");
            if (status == 0) {
            } else if (status < 0) {
                Tools.dealErrorMsg(this, url, status, json.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }
}
