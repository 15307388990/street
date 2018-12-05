package com.juxun.business.street.activity;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.AuthTask;
import com.juxun.business.street.bean.ConfirmBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.SwipeBackController;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.util.alipay.PayResult;
import com.juxun.business.street.widget.dialog.PromptDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * @author WhiteWebviewActivity 白条
 */
public class WhiteWebviewActivity extends BaseActivity {

    @ViewInject(R.id.webview)
    private WebView mWebView;
    // @ViewInject(R.id.swipe_container)
    // private SwipeRefreshLayout swipe_container;
    private String url;
    private String mNameString;
    private String pay_order_num;// 订单id
    private int type;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    public SwipeBackController swipeBackController;// 右滑关闭
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Tools.jump(WhiteWebviewActivity.this, OrderActivity.class, true);
                        Tools.webexit();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getApplicationContext(), "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Tools.jump(WhiteWebviewActivity.this, OrderActivity.class, true);
                            Tools.webexit();
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_webview_white);
        ViewUtils.inject(this);
        Tools.webacts.add(this);
        url = getIntent().getExtras().getString("url");
        mNameString = getIntent().getExtras().getString("name");
        type = getIntent().getIntExtra("type", 0);
        initTitle();
        swipeBackController = new SwipeBackController(this);
        title.setText(mNameString);
        if (type == 1) {
            more.setText("帮助");
            more.setVisibility(View.VISIBLE);
            more.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(WhiteWebviewActivity.this, WhiteWebviewActivity.class);
                    String urlString = Constants.mainUrl + Constants.help;
                    intent.putExtra("url", urlString);
                    intent.putExtra("name", "帮助");
                    startActivity(intent);

                }
            });

        }

        init();
        initOnClik();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        mWebView.loadUrl("javascript:onResume()");
        super.onResume();
    }

    private void initOnClik() {

        // swipe_container.setOnRefreshListener(new
        // SwipeRefreshLayout.OnRefreshListener() {
        //
        // @Override
        // public void onRefresh() {
        // // 重新刷新页面
        // url = getIntent().getExtras().getString("url") + "&v=" +
        // System.currentTimeMillis();
        // mWebView.loadUrl(url);
        // }
        // });

    }

    private void init() {
        try {
            mWebView.requestFocus();
            mWebView.setFocusableInTouchMode(true);// 设置可触摸
            mWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // TODO Auto-generated method stub
                    super.onPageFinished(view, url);
                }
            });
            mWebView.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (swipeBackController.processEvent(event)) {
                        return true;
                    } else {
                        return onTouchEvent(event);
                    }
                }
            });
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
            webSettings.setSupportZoom(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDatabaseEnabled(true);
            webSettings.setDefaultTextEncodingName("utf-8");
            // 开启 DOM storage API 功能
            webSettings.setDomStorageEnabled(true);
            // 开启 database storage API 功能
            webSettings.setDatabaseEnabled(true);
            String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
            // String cacheDirPath =
            // getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
            // 设置 Application Caches 缓存目录
            webSettings.setAppCachePath(appCachePath);
            // 开启 Application Caches 功能
            webSettings.setAppCacheEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setAppCachePath(appCachePath);
            webSettings.setAllowFileAccess(true);
            webSettings.setAppCacheEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            mWebView.setWebChromeClient(new WebChromeClient());
            showWebView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showWebView() { // webView与js交互代码
        // 设置本地调用对象及其接口
        mWebView.addJavascriptInterface(new JavaScriptObject(this), "native");
        mWebView.loadUrl(url);

    }

    // 余额支付下单
    private void balancePayNotify(String pay_pass_token) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("pay_token", pay_pass_token);
        map.put("pay_order_num", pay_order_num);// 订单号
        mQueue.add(ParamTools.packParam(Constants.balancePayNotify, this, this, map));
        loading();
    }

    // 白条支付下单
    private void whitebarPayNotify(String pay_pass_token) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("pay_token", pay_pass_token);
        map.put("pay_order_num", pay_order_num);// 订单号
        mQueue.add(ParamTools.packParam(Constants.whitebarPayNotify, this, this, map));
        loading();
    }

    public class JavaScriptObject {
        Context mContxt;

        public JavaScriptObject(Context mContxt) {
            this.mContxt = mContxt;
        }

        // 绑定事件
        public void transfer(int type, String json) {
            Tools.showToast(WhiteWebviewActivity.this, "type=" + type + "json=" + json);
            System.out.println("type=" + type + "json=" + json);
            Intent intent = new Intent();
            switch (type) {
                // 跳转至商品
                case 3:
                    intent.setClass(WhiteWebviewActivity.this, GoodsMianAcitivity.class);
                    startActivity(intent);
                    break;
                // 跳转财务
                case 4:
                    intent.setClass(WhiteWebviewActivity.this, FinancialInfoActivity.class);
                    startActivity(intent);
                    break;
                // 跳转至扫码
                case 5:
                    intent.setClass(WhiteWebviewActivity.this, MipcaActivityCapture.class);
                    startActivityForResult(intent, 1);
                    break;
                // 跳转至收款
                case 6:
                    intent.setClass(WhiteWebviewActivity.this, InputNumActivity.class);
                    startActivity(intent);
                    break;
                // 跳转至收款
                case 7:
                    intent.setClass(WhiteWebviewActivity.this, WhiteWebviewActivity.class);
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(json);
                        intent.putExtra("url", jsonObject.getString("link_url"));
                        intent.putExtra("name", jsonObject.getString("title"));
                        System.out.println("link_url" + jsonObject.getString("link_url"));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    startActivity(intent);
                    break;
                case 8:
                    ConfirmBean confirmBean = JSON.parseObject(json, ConfirmBean.class);
                    int paytype = confirmBean.getPayType();
                    switch (paytype) {
                        case 1:
                            // 微信支付

                            break;
                        case 2:
                            // 支付宝支付
                            initRay(confirmBean);
                            break;
                        case 3:
                            // pos机支付

                            break;
                        case 4:
                            // 货到付款
                            Tools.jump(WhiteWebviewActivity.this, OrderActivity.class, true);
                            Tools.webexit();
                            break;
                        case 5:
                            // 白条支付
                            intent.setClass(WhiteWebviewActivity.this, VerifyPaymentPasswordActivity.class);
                            pay_order_num = confirmBean.getPayOrderId();
                            startActivityForResult(intent, 2);
                            break;
                        case 6:
                            // 余额支付
                            intent.setClass(WhiteWebviewActivity.this, VerifyPaymentPasswordActivity.class);
                            pay_order_num = confirmBean.getPayOrderId();
                            startActivityForResult(intent, 1);
                            break;

                    }

                    break;
                case 9:
                    Tools.jump(WhiteWebviewActivity.this, TopUpActivity.class, false);
                    break;
                case 10:
                    break;
                case 11:
                    JSONObject diogjson;
                    try {
                        diogjson = new JSONObject(json);
                        PromptDialog promptDialog = new PromptDialog(WhiteWebviewActivity.this);
                        promptDialog.setContent(diogjson.getString("text_name"));
                        promptDialog.setText(diogjson.getString("button_name"), "");
                        promptDialog.setFocusable(false);
                        promptDialog.showAtLocation(title, Gravity.CENTER, 0, 0);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    break;
                case 12:
                    mSavePreferencesData.putStringData("json", "");
                    Tools.showToast(WhiteWebviewActivity.this, "登录已过期，请重新登录");
                    intent.setClass(WhiteWebviewActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Tools.exit();
                    finish();
                    break;
            }
        }
    }

    public void GoShop() {

    }

    // 支付宝支付
    private void initRay(final ConfirmBean confirmBean) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(WhiteWebviewActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(confirmBean.getSign(), true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // 验证支付密码
                balancePayNotify(data.getStringExtra("pay_pass_token"));
            } else if (requestCode == 2) {
                // 验证支付密码
                whitebarPayNotify(data.getStringExtra("pay_pass_token"));
            }
        }
    }

    @Override
    public void onResponse(String response, String url) {
        // TODO Auto-generated method stub
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            Log.i("test", response);
            int status = json.getInt("status");
            String msg = json.getString("msg");
            if (status == 0) {
                if (url.contains(Constants.balancePayNotify)) {
                    Tools.jump(WhiteWebviewActivity.this, OrderActivity.class, true);
                    Tools.showToast(WhiteWebviewActivity.this, "余额支付成功");
                    Tools.webexit();
                } else if (url.contains(Constants.whitebarPayNotify)) {
                    Tools.jump(WhiteWebviewActivity.this, OrderActivity.class, true);
                    Tools.showToast(WhiteWebviewActivity.this, "白条支付成功");
                    Tools.webexit();
                }
            } else if (status == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(WhiteWebviewActivity.this, LoginActivity.class, false);
                Tools.showToast(WhiteWebviewActivity.this, "登录过期请重新登录");
                Tools.acts.clear();
            } else {
                Tools.showToast(getApplicationContext(), msg);
            }
        } catch (JSONException e) {
            // TODO: handle exception
            Tools.showToast(getApplicationContext(), "解析数据错误");
        }

    }

}
