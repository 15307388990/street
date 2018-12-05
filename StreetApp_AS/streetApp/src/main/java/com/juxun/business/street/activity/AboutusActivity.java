/**
 *
 */
package com.juxun.business.street.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @version 关于我们
 */
public class AboutusActivity extends BaseActivity {

    @ViewInject(R.id.tv_vison)
    private TextView tv_vison; // 当前版本
    @ViewInject(R.id.ll_phone)
    private LinearLayout ll_phone;// 拨打客服电话
    @ViewInject(R.id.iv_img)
    private ImageView iv_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ViewUtils.inject(this);
        initTitle();
        title.setText("关于我们");
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        tv_vison.setText("版本号 " + info.versionName);
        iv_img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new HideClick().start();
                if (HideClick.sIsAlive >= 5) {
                    Tools.showToast(AboutusActivity.this, "机构ID=" + partnerBean.getStore_id());
                } else if (HideClick.sIsAlive == 2) {
                    Tools.showToast(AboutusActivity.this, "连续点击5次获取机构ID");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // obtainData();
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.ll_phone})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.ll_phone:
                // 用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4006299903"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
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
            int stauts = json.optInt("status");
            if (stauts == 0) {
            } else if (stauts < 0) {
                Tools.dealErrorMsg(this, url, stauts, json.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    static class HideClick extends Thread {
        public static volatile int sIsAlive = 0;

        @Override
        public void run() {
            sIsAlive++;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (sIsAlive > 0) {
                sIsAlive--;
            }
            super.run();

        }
    }
}
