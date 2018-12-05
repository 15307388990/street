package com.juxun.business.street.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.MD5Util;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.dialog.PromptDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * *
 * ● @version 验证支付密码
 **/
public class VerifyPaymentPasswordActivity extends BaseActivity implements PromptDialog.onConfirmListener {
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
    @ViewInject(R.id.tv_forget)
    private TextView tv_forget;
    @ViewInject(R.id.tv_wangji)
    private TextView tv_wangji;
    private String addnumber = "";
    @ViewInject(R.id.ll_authen)
    private LinearLayout ll_authen;
    private String mPay_pass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("验证支付密码");
    }

    @Override
    public void finish() {
        super.finish();
        setResult(RESULT_CANCELED);
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight,
            R.id.nine, R.id.point, R.id.delete, R.id.clearing, R.id.tv_wangji})
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
            case R.id.tv_wangji:
               // initPromptDialog();
                break;
        }
    }

    public void add(String addStr) {
        addnumber = addnumber + addStr;
        initPassword();
        if (addnumber.length() == 6) {
            verificationPayPass(addnumber);
        }
    }

    private void initPromptDialog() {
        PromptDialog promptDialog = new PromptDialog(VerifyPaymentPasswordActivity.this);
        promptDialog.setContent("请拨打客服电话4006299903\n重置支付密码");
        promptDialog.setText("取消", "拨打电话");
        promptDialog.setFocusable(false);
        promptDialog.showAtLocation(tv_title, Gravity.CENTER, 0, 0);
        promptDialog.setVisibility(View.VISIBLE);
        promptDialog.setonConfirmListener(this);
    }

    /**
     * 验证支付密码
     */
    private void verificationPayPass(String pay_pass) {
        this.mPay_pass = pay_pass;  //在修改支付密码种 这属于旧的支付密码

        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("pay_pass", MD5Util.getMD5String(pay_pass));
        mQueue.add(ParamTools.packParam(Constants.verificationPayPass, this, this, map));
        loading();
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
                Intent intent = new Intent();
                intent.putExtra("pay_pass", MD5Util.getMD5String(mPay_pass));
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);// 加载动画资源文件
                ll_authen.startAnimation(shake); // 给组件播放动画效果
                Toast toast = Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                LinearLayout toastView = (LinearLayout) toast.getView();
                ImageView imageCodeProject = new ImageView(getApplicationContext());
                imageCodeProject.setImageResource(R.drawable.toast_wrong);
                toastView.addView(imageCodeProject, 0);
                toast.show();
                // 显示忘记密码
                // tv_forget.setVisibility(View.VISIBLE);
                // 清空密码
                addnumber = "";
                initPassword();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    @Override
    public void onConfirm(int id) {
        if (id == R.id.btn_two) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4006299903"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
