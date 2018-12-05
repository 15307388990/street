package com.juxun.business.street.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author luoming 注册成功界面
 */

public class RegisteredSuccessfullyActivity extends BaseActivity {
    @ViewInject(R.id.btn_next)
    private Button btn_next;// 下一步
    @ViewInject(R.id.tv_content)
    private TextView tv_content;
    private int type;// 0 为 注册成功 ， 1，为绑定密保手机成功 2，为设置支付密码成功

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_successfully);
        ViewUtils.inject(this);
        initTitle();
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            title.setText("注册");
        } else if (type == 1) {
            title.setText("验证成功");
            tv_content.setText("绑定手机号成功");
            back.setVisibility(View.INVISIBLE);
            btn_next.setText("下一步，设置支付密码");
        } else if (type == 2) {
            title.setText("设置成功");
            tv_content.setText("设置支付密码成功");
            back.setVisibility(View.INVISIBLE);
            btn_next.setText("完成");
        } else if (type == 3) {
            title.setText("提交资料");
            btn_next.setText("返回首页");
        } else if (type == 4) {
            title.setText("提交售后申请成功");
            tv_content.setText("您已成功提交售后申请！\n我们将在3个工作日内审核，敬请留意。");
            back.setVisibility(View.INVISIBLE);
            btn_next.setText("知道了");
        }

        initView();
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Tools.exit();
                Tools.jump(RegisteredSuccessfullyActivity.this, MainActivity.class, true);
            }
        });

    }

    private void initView() {

    }

    /**
     * 单击事件
     */
    @OnClick({R.id.btn_next})
    public void clickMethod(View v) {
        if (v.getId() == R.id.btn_next) {
            if (type == 0) {
                Tools.exit();
                Tools.jump(RegisteredSuccessfullyActivity.this, MainActivity.class, true);
            } else if (type == 1) {
                Tools.jump(RegisteredSuccessfullyActivity.this, SetPayPasswordOneActivity.class, true);
            } else if (type == 2) {
                Tools.jump(RegisteredSuccessfullyActivity.this, MainActivity.class, true);
            } else if (type == 3) {
                Tools.jump(RegisteredSuccessfullyActivity.this, MainActivity.class, true);
            } else if (type == 4) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            Tools.showToast(getApplicationContext(), "解析数据错误");
        }

    }

}
