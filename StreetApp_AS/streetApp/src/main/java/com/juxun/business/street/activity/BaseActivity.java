/**
 *
 */
package com.juxun.business.street.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.iboxpay.cashbox.minisdk.CashboxProxy;
import com.iboxpay.cashbox.minisdk.callback.IAuthCallback;
import com.iboxpay.cashbox.minisdk.exception.ConfigErrorException;
import com.iboxpay.cashbox.minisdk.model.Config;
import com.iboxpay.cashbox.minisdk.model.ErrorMsg;
import com.iboxpay.cashbox.minisdk.model.PrintPreference;
import com.juxun.business.street.bean.Order;
import com.juxun.business.street.bean.PartnerBean;
import com.juxun.business.street.bean.StoreBean;
import com.juxun.business.street.util.ObjToFile;
import com.juxun.business.street.util.SavePreferencesData;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.dialog.LoadDialog;
import com.umeng.analytics.MobclickAgent;
import com.yl.ming.efengshe.R;
import com.zhy.m.permission.MPermissions;

import java.text.DecimalFormat;

/**
 * @author WuJianHua Activity 基类
 */
public abstract class BaseActivity extends Activity implements
        Listener<String>, ErrorListener {
    protected static final int REQUEST_CODE_TAKE_PHOTE = 0;
    protected static final int REQUEST_CODE_LOCATION = 1;
    protected static final int REQUEST_CODE_WRITE = 2;
    public View titleBar;
    // @ViewInject(R.id.top_view_back)
    public View back; // 返回
    // @ViewInject(R.id.top_view_text)
    public TextView title; // 标题
    // @ViewInject(R.id.right_view_text)
    public TextView more; // 更多
    public ImageView right_img;// 右边上角图片
    private LoadDialog loading;
    public RequestQueue mQueue; // 请求列队
    PowerManager pm;
    WakeLock mWakeLock;
    public SavePreferencesData mSavePreferencesData;
    protected int count = 0;

    public Order order;
    // 订单存储
    public ObjToFile objToFile;
    // 账号
    private String mAppCode = "2001171";
    // 三选一 partnerId、iboxMchtNo和partnerUserId至少有一个必填
    private String mMerchantNo = "014440396079006";// xqs
    private PrintPreference printPreference;
    protected int displayWidth;
    //private String mGenerateDeviceUuid;
    protected int mDisplayheight;
    protected DecimalFormat mDf;
    protected PartnerBean partnerBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQueue = Volley.newRequestQueue(this);
        mDf = new DecimalFormat("0.00");

        // 存储对象类
        objToFile = new ObjToFile(this);
        /**
         * 设置设备常亮
         */
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        mSavePreferencesData = new SavePreferencesData(this);
        isPos();
        initAdmin();
    }

    //暂时未使用
    private void checkoutPermissions() {
        int CAMERA_Permissions = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int LOCATION_Permissions = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int WRITE_Permissions = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (CAMERA_Permissions != PackageManager.PERMISSION_GRANTED) {
            MPermissions.requestPermissions(this, REQUEST_CODE_TAKE_PHOTE, Manifest.permission.CAMERA);
        }
        if (LOCATION_Permissions != PackageManager.PERMISSION_GRANTED) {
            MPermissions.requestPermissions(this, REQUEST_CODE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (WRITE_Permissions != PackageManager.PERMISSION_GRANTED) {
            MPermissions.requestPermissions(this, REQUEST_CODE_WRITE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    //扫码、拍照
    protected void checkoutCameraPermissions() {
        int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            MPermissions.requestPermissions(this, REQUEST_CODE_TAKE_PHOTE, Manifest.permission.CAMERA);
        } else {
            count = count + 1;
            gotThePermission();
        }
    }

    //百度定位权限
    protected void checkoutLocationPermissions() {
        int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            MPermissions.requestPermissions(this, REQUEST_CODE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
            gotThePermission();
        }
    }

    //相册读取权限
    protected void checkoutWritePermissions() {
        int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            MPermissions.requestPermissions(this, REQUEST_CODE_WRITE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            count = count + 1;
            gotThePermission();
        }
    }

    protected void gotThePermission() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initWindowConfig() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        displayWidth = wm.getDefaultDisplay().getWidth();
        mDisplayheight = wm.getDefaultDisplay().getHeight();
    }

    // 判断该设备 为手机 还是POS机
    private void isPos() {
        String SN = CashboxProxy.getBoxSn(this);    //isSn
        if (SN.equals("false")) {
            mSavePreferencesData.putBooleanData("isSn", false);
        } else {
            mSavePreferencesData.putBooleanData("isSn", true);
            initPay();
        }
    }

    /**
     * 初始化盒子支付
     */
    private void initPay() {
        // 初始化配置
        printPreference = new PrintPreference();
        // printPreference.setDisplayIBoxPaySaleSlip(PrintPreference.SALESLIP_HIDE);
        printPreference.setMerchantName("e蜂社");
        printPreference.setOperatorNo("001");
        // printPreference.setOrderTitle("订单title");
        // mAppCode必填，partnerId、iboxMchtNo和partnerUserId至少有一个必填
        Config.config = new Config(mAppCode, printPreference);
        Config.config.setIboxMchtNo(mMerchantNo);

        // **************系统服务请求方式**************
        try {
            CashboxProxy.getInstance(this).initAppInfo(Config.config, new IAuthCallback() {
                @Override
                public void onAuthSuccess() {
                    // runOnUiThread(new Runnable() {
                    // @Override
                    // public void run() {
                    // Toast.makeText(getApplicationContext(), "刷卡机初始化成功",
                    // Toast.LENGTH_LONG).show();
                    // }
                    // });
                    System.out.println("刷卡机初始化成功");
                }

                @Override
                public void onAuthFail(final ErrorMsg msg) {
                    // runOnUiThread(new Runnable() {
                    // @Override
                    // public void run() {
                    // Toast.makeText(getApplicationContext(),
                    // "签到失败:" + msg.getErrorCode() + " ," + msg.getErrorMsg(),
                    // Toast.LENGTH_LONG).show();
                    // }
                    // });
                    System.out.println("刷卡机初始化失败");
                }
            });
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
        MobclickAgent.onResume(this);
        initAdmin();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWakeLock.release();
        MobclickAgent.onPause(this);
    }

    /**
     * 初始化标题栏
     */
    public void initTitle() {
        titleBar = findViewById(R.id.titleBar);
        back = findViewById(R.id.top_view_back);
        title = (TextView) findViewById(R.id.top_view_text);
        more = (TextView) findViewById(R.id.right_view_text);
        right_img = (ImageView) findViewById(R.id.right_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQueue.stop();
                finish();
            }
        });
        //
        // initMargin();
    }

    public void initAdmin() {
        String mallSet = mSavePreferencesData.getStringData("json");
        if (mallSet != null) {
            synchronized (BaseActivity.this) {
                try {
                    partnerBean = JSON.parseObject(mallSet, PartnerBean.class);
                    partnerBean.setAuth_token(mSavePreferencesData.getStringData("auth_token"));
                } catch (Exception e) {
                    mSavePreferencesData.putStringData("json", "");
                }
            }
        }
    }

    /**
     * // * 设置状态栏背景状态 //
     */
    // private void setTranslucentStatus() {
    // if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
    // Window window = getWindow();
    // //
    // window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    // window.setStatusBarColor(getResources().getColor(R.color.blue));
    // // window.setNavigationBarColor(Color.TRANSPARENT);
    // }
    // }

    /**
     * 弹出等待框
     */
    public void loading() {
        if (loading == null) {
            loading = new LoadDialog(this, "请稍后...");
            loading.setCanceledOnTouchOutside(false);
        }
        if (loading.isShowing()) {
            loading.dismiss();
        }
        loading.show();// 由于客户不喜欢弹框样式,顾先隐藏
    }

    /**
     * 隐藏等待框
     */
    public void dismissLoading() {
        if (loading == null || !loading.isShowing()) {
            return;
        }
        loading.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** 被挤下线 */
        if (requestCode == 10000) {
            Tools.jump(this, LoginActivity.class, true);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        dismissLoading();
        Tools.showToast(getApplicationContext(), "网络连接异常");
    }

    public abstract void onResponse(String response, String url);

}
