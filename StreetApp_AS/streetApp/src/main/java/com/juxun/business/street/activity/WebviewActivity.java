package com.juxun.business.street.activity;


import android.app.AlertDialog;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.AuthTask;
import com.iboxpay.cashbox.minisdk.CashboxProxy;
import com.iboxpay.cashbox.minisdk.PayType;
import com.iboxpay.cashbox.minisdk.SignType;
import com.iboxpay.cashbox.minisdk.callback.ITradeCallback;
import com.iboxpay.cashbox.minisdk.exception.ConfigErrorException;
import com.iboxpay.cashbox.minisdk.model.ErrorMsg;
import com.iboxpay.cashbox.minisdk.model.ParcelableBitmap;
import com.iboxpay.cashbox.minisdk.model.ParcelableMap;
import com.juxun.business.street.bean.Agreement7;
import com.juxun.business.street.bean.ConfirmBean;
import com.juxun.business.street.bean.DiogBtnBean;
import com.juxun.business.street.bean.Order;
import com.juxun.business.street.bean.PayBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.ImageUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.StringUtil;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.util.alipay.PayResult;
import com.juxun.business.street.widget.dialog.PromptDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WebviewActivity
 */
public class WebviewActivity extends BaseActivity {

    @ViewInject(R.id.webview)
    private WebView mWebView;
    @ViewInject(R.id.pb_progressbar)
    private android.widget.ProgressBar ProgressBar;
    // @ViewInject(R.id.swipe_container)
    // private SwipeRefreshLayout swipe_container;
    private String pay_order_num;// 订单id
    private PromptDialog promptDialog;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    private Agreement7 agreement7;
    private String url;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();
    // private SlideBackLayout mSlideBackLayout;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult(
                            (Map<String, String>) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // if (isorder) {
                        // Tools.jump(WebviewActivity.this, OrderActivity.class,
                        // true);
                        // Tools.webexit();
                        // } else {
                        // mWebView.loadUrl("javascript:paySuccess(0)");
                        // }
                        mWebView.loadUrl("javascript:onPayResult(2,0,9000)");
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getApplicationContext(), "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();
                            mWebView.loadUrl("javascript:onPayResult(2,-1,8000)");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            mWebView.loadUrl("javascript:onPayResult(2,-1,9000)");
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
    private Handler mHandler2 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    Tools.jump(WebviewActivity.this, OrderActivity.class, true);
                    Tools.webexit();
                    break;
                }
                case 2: {
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                // 1.构建Bitmap
                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                int w = display.getWidth();
                int h = display.getHeight() - 50;

                Bitmap Bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Bmp = (Bitmap) msg.obj;
                // 获取内部存储状态
                SavaImage(Bmp, Environment.getExternalStorageDirectory()
                        .getPath() + "/Test");
            }
        }

        ;
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // mSlideBackLayout.isComingToFinish();
    }

    /**
     * 保存位图到本地
     *
     * @param bitmap
     * @param path   本地路径
     * @return void
     */
    public void SavaImage(Bitmap bitmap, String path) {
        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        // 文件夹不存在，则创建它
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            String fath = path + "/" + System.currentTimeMillis() + ".png";
            fileOutputStream = new FileOutputStream(fath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
            saveImgToGallery(fath);
            // 保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            Tools.showToast(getApplicationContext(), "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            Tools.showToast(getApplicationContext(), "保存失败");
        }
    }

    /**
     * 更新系统数据库找到图片
     *
     * @param fileName
     * @return
     */
    public boolean saveImgToGallery(String fileName) {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (!sdCardExist)
            return false;

        try {
            // String url = MediaStore.Images.Media.insertImage(cr, bmp,
            // fileName,
            // "");
            // app.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
            // .parse("file://"
            // + Environment.getExternalStorageDirectory())));
            // debug
            ContentValues values = new ContentValues();
            values.put("datetaken", new Date().toString());
            values.put("mime_type", "image/png");
            values.put("_data", fileName);
            // values.put("title", this.a.getString(2131230720));
            // values.put("_display_name", (String)localObject1);
            // values.put("orientation", "");
            // values.put("_size", Integer.valueOf(0));
            Application app = getApplication();
            ContentResolver cr = app.getContentResolver();
            cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_webview);
        // mSlideBackLayout = SlideBackHelper.attach(
        // // 当前Activity
        // this,
        // // Activity栈管理工具
        // UILApplication.getActivityHelper(),
        // // 参数的配置
        // new SlideConfig.Builder()
        // // 屏幕是否旋转
        // .rotateScreen(false)
        // // 是否侧滑
        // .edgeOnly(true)
        // // 是否禁止侧滑
        // .lock(true)
        // // 侧滑的响应阈值，0~1，对应屏幕宽度*percent
        // .edgePercent(0.3f)
        // // 关闭页面的阈值，0~1，对应屏幕宽度*percent
        // .slideOutPercent(0.5f).create(),
        // // 滑动的监听
        // null);

        ViewUtils.inject(this);
        Tools.webacts.add(this);
        Tools.acts.add(this);
        agreement7 = (Agreement7) getIntent()
                .getSerializableExtra("agreement7");
        url = agreement7.getLink_url();
        promptDialog = new PromptDialog(WebviewActivity.this);
        initTitle();
        title.setText(agreement7.getTitle());
        if (agreement7.getTitle_color() != null) {
            titleBar.setBackgroundColor(Color.parseColor(agreement7
                    .getTitle_color()));
        }
        if (agreement7.getRight_button() != null) {
            if (agreement7.getRight_button().getButton_icon() != null) {
                right_img.setVisibility(View.VISIBLE);
                imageLoader.displayImage(agreement7.getRight_button()
                        .getButton_icon(), right_img, options);
                right_img
                        .setOnClickListener(new android.view.View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                mWebView.loadUrl("javascript:"
                                        + agreement7.getRight_button()
                                        .getButton_method());
                            }
                        });
            } else {
                more.setVisibility(View.VISIBLE);
                more.setText(agreement7.getRight_button().getButton_name());
                more.setOnClickListener(new android.view.View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mWebView.loadUrl("javascript:"
                                + agreement7.getRight_button()
                                .getButton_method());
                    }
                });
                more.setTextColor(Color.parseColor(agreement7.getRight_button()
                        .getButton_text_color()));
            }
        }
        init();
        initOnClik();

    }

    @Override
    protected void onResume() {
        mWebView.loadUrl("javascript:onResume()");
        super.onResume();
    }

    @Override
    public void finish() {
        super.finish();
        try {
            if (promptDialog.isShowing()) {
                promptDialog.dismiss();
            }
        } catch (Exception e) {
        }
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
                    // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }
            });
            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        ProgressBar.setVisibility(View.GONE);
                    } else {
                        if (View.GONE == ProgressBar.getVisibility()) {
                            ProgressBar.setVisibility(View.VISIBLE);
                        }
                        ProgressBar.setProgress(newProgress);
                    }
                    super.onProgressChanged(view, newProgress);
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
            String appCachePath = getApplicationContext().getCacheDir()
                    .getAbsolutePath();
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
        mQueue.add(ParamTools.packParam(Constants.balancePayNotify, this, this,
                map));
        loading();
    }

    // 白条支付下单
    private void whitebarPayNotify(String pay_pass_token) {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("pay_token", pay_pass_token);
        map.put("pay_order_num", pay_order_num);// 订单号
        mQueue.add(ParamTools.packParam(Constants.whitebarPayNotify, this,
                this, map));
        loading();
    }

    public class JavaScriptObject {
        Context mContxt;

        public JavaScriptObject(Context mContxt) {
            this.mContxt = mContxt;
        }

        // 绑定事件
        public void transfer(int type, String json) {
            // Tools.showToast(WebviewActivity.this, "type=" + type + "json=" +
            // json);
            System.out.println("type=" + type + "json=" + json);
            Intent intent = new Intent();
            switch (type) {
                // 跳转至商品
                case 3:
                    intent.setClass(WebviewActivity.this, GoodsMianAcitivity.class);
                    startActivity(intent);
                    break;
                // 跳转财务
                case 4:
                    intent.setClass(WebviewActivity.this,
                            FinancialInfoActivity.class);
                    startActivity(intent);
                    break;
                // 跳转至扫码
                case 5:
                    intent.setClass(WebviewActivity.this,
                            MipcaActivityCapture.class);
                    startActivityForResult(intent, 1);
                    break;
                // 跳转至收款
                case 6:
                    intent.setClass(WebviewActivity.this, InputNumActivity.class);
                    startActivity(intent);
                    break;
                // 跳转至其它链接
                case 7:
                    intent.setClass(WebviewActivity.this, WebviewActivity.class);
                    Agreement7 agreement7 = JSON
                            .parseObject(json, Agreement7.class);
                    intent.putExtra("agreement7", agreement7);
                    startActivity(intent);
                    break;
                case 8:
                    ConfirmBean confirmBean = JSON.parseObject(json,
                            ConfirmBean.class);
                    int paytype = confirmBean.getPayType();
                    switch (paytype) {
                        case 1:
                            // 微信支付
                            if (mSavePreferencesData.getBooleanData("isSn")) {
                                try {
                                    JSONObject resultJson = new JSONObject(json);
                                    String qr_code = resultJson.getString("qr_code");
                                    String payOrderId = resultJson
                                            .getString("payOrderId");
                                    int payPrice = resultJson.getInt("payPrice");
                                    Intent intent1 = new Intent(WebviewActivity.this,
                                            OrderPayActivity.class);
                                    intent1.putExtra("qr_code", qr_code);
                                    intent1.putExtra("payOrderId", payOrderId);
                                    intent1.putExtra("payPrice", payPrice);
                                    intent1.putExtra("payType", 0);
                                    startActivity(intent1);
                                    Tools.webexit();
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            } else {
                                // initRay(confirmBean);
                            }

                            break;
                        case 2:
                            // 支付宝支付
                            if (mSavePreferencesData.getBooleanData("isSn")) {
                                try {
                                    JSONObject resultJson = new JSONObject(json);
                                    String qr_code = resultJson.getString("qr_code");
                                    String payOrderId = resultJson
                                            .getString("payOrderId");
                                    int payPrice = resultJson.getInt("payPrice");
                                    Intent intent1 = new Intent(WebviewActivity.this,
                                            OrderPayActivity.class);
                                    intent1.putExtra("qr_code", qr_code);
                                    intent1.putExtra("payOrderId", payOrderId);
                                    intent1.putExtra("payPrice", payPrice);
                                    intent1.putExtra("payType", 1);
                                    startActivity(intent1);
                                    Tools.webexit();
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            } else {
                                initRay(confirmBean);
                            }

                            break;
                        case 3:
                            // pos机支付
                            try {
                                JSONObject result = new JSONObject(json);
                                String payString = result.getString("boxPayResBean");
                                PayBean payBean = JSON.parseObject(payString,
                                        PayBean.class);
                                paySwipCard(payBean);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            break;
                        case 4:
                            // 货到付款
                            // Tools.jump(WebviewActivity.this, OrderActivity.class,
                            // true);
                            // Tools.webexit();
                            mWebView.loadUrl("javascript:onPayResult(7,0,9000)");
                            break;
                        case 5:
                            // 白条支付
                            intent.setClass(WebviewActivity.this,
                                    VerifyPaymentPasswordActivity.class);
                            pay_order_num = confirmBean.getPayOrderId();
                            startActivityForResult(intent, 2);
                            break;
                        case 6:
                            // 余额支付
                            intent.setClass(WebviewActivity.this,
                                    VerifyPaymentPasswordActivity.class);
                            pay_order_num = confirmBean.getPayOrderId();
                            startActivityForResult(intent, 1);
                            break;
                    }

                    break;
                case 9:
                    Tools.jump(WebviewActivity.this, TopUpActivity.class, false);
                    break;
                // 后退协议 0为关闭所有
                case 10:
                    JSONObject pagejson;
                    try {
                        pagejson = new JSONObject(json);
                        int page = pagejson.getInt("page");
                        if (page == 0) {
                            Tools.webexit();
                        } else {
                            Tools.webexit(page);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                // 多弹框协议
                case 11:
                    // JSONObject diogjson;
                    // try {
                    // diogjson = new JSONObject(json);
                    // promptDialog.setContent(diogjson.getString("text_name"));
                    // promptDialog.setText(diogjson.getString("button_name"), "");
                    // promptDialog.setFocusable(false);
                    // promptDialog.showAtLocation(mWebView, Gravity.CENTER, 0, 0);
                    // } catch (JSONException e) {
                    // // TODO Auto-generated catch block
                    // e.printStackTrace();
                    // }
                    final DiogBtnBean diogBtnBean = JSON.parseObject(json,
                            DiogBtnBean.class);
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            WebviewActivity.this);
                    if (diogBtnBean.getBtn_list().size() < 1) {
                        builder.setTitle(diogBtnBean.getTitle());
                        builder.setMessage(diogBtnBean.getMessage());
                        builder.setNegativeButton(
                                diogBtnBean.getCancel_btn().getName(),
                                new OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        mWebView.loadUrl("javascript:"
                                                + diogBtnBean.getCancel_btn()
                                                .getMethod());
                                    }
                                }).show();
                    } else if (diogBtnBean.getBtn_list().size() == 1) {
                        builder.setTitle(diogBtnBean.getTitle());
                        builder.setMessage(diogBtnBean.getMessage());
                        builder.setPositiveButton(
                                diogBtnBean.getCancel_btn().getName(),
                                new OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        mWebView.loadUrl("javascript:"
                                                + diogBtnBean.getCancel_btn()
                                                .getMethod());
                                    }
                                })
                                .setNegativeButton(
                                        diogBtnBean.getBtn_list().get(0).getName(),
                                        new OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                mWebView.loadUrl("javascript:"
                                                        + diogBtnBean.getBtn_list()
                                                        .get(0).getMethod());
                                            }
                                        }).show();
                    } else

                    {
                        diogBtnBean.getBtn_list().add(diogBtnBean.getCancel_btn());
                        // 创建数据
                        final String[] items = new String[diogBtnBean.getBtn_list()
                                .size()];
                        // List转换成数组
                        for (int i = 0; i < diogBtnBean.getBtn_list().size(); i++) {
                            items[i] = diogBtnBean.getBtn_list().get(i).getName();
                        }
                        // 创建对话框构建器
                        // 设置参数
                        builder.setIcon(null).setTitle(diogBtnBean.getTitle())
                                .setItems(items, new OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        mWebView.loadUrl("javascript:"
                                                + diogBtnBean.getBtn_list()
                                                .get(which).getMethod());
                                    }
                                });
                        builder.create().show();
                    }
                    break;
                // token过期处理
                case 12:
                    mSavePreferencesData.putStringData("json", "");
                    Tools.showToast(WebviewActivity.this, "登录已过期，请重新登录");
                    intent.setClass(WebviewActivity.this, LoginActivity.class);

                    startActivity(intent);
                    Tools.exit();
                    break;
                // 重新调用填写商户审核资料页面
                case 14:
                    intent.setClass(WebviewActivity.this, AgainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 15:
                    intent.setClass(WebviewActivity.this, DeccaDataActivity.class);
                    startActivity(intent);
                    break;
                // 保存图片到本地
                case 16:
                    JSONObject imgjson;
                    try {
                        imgjson = new JSONObject(json);
                        final String icon = imgjson.getString("src");
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                Bitmap bitmap = ImageUtil.getbitmap(icon);
                                Message msg = new Message();
                                msg.obj = bitmap;
                                msg.what = 1;
                                handler.sendMessage(msg);
                            }
                        }).start();

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    break;
                // 申请售后
                case 17:
                    JSONObject orderIdjson;
                    try {
                        orderIdjson = new JSONObject(json);
                        String orderId = orderIdjson.getString("orderId");
                        intent.setClass(WebviewActivity.this,
                                RequestAfterSaleActivity.class);
                        intent.putExtra("order_id", orderId);
                        startActivityForResult(intent, 4);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    break;
                // GET window.native.transfer(18,json); 获取设备版本信息
                case 18:
                    String app_version = getResources().getString(
                            R.string.versionName);
                    String os_version = Tools.getSystemVersion();
                    String device_version = Tools.getSystemModel();
                    String device_brand = Tools.getDeviceBrand();
                    String kString = "javascript:onGetDeviceCallBack('"
                            + app_version + "','" + os_version + "','"
                            + device_version + "','" + device_brand + "',1)";
                    mWebView.loadUrl(kString);
                    // onGetDeviceCallBack(app_version,os_version,device_version,device_brand,os_type)
                    break;
                // 跳转取消订单
                case 19:
                    JSONObject cansjson;
                    try {
                        cansjson = new JSONObject(json);
                        String orderId = cansjson.getString("orderId");
                        intent.setClass(WebviewActivity.this,
                                CancelReasonActivity.class);
                        intent.putExtra("order_id", orderId);
                        startActivityForResult(intent, 3);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                // 申请记录
                case 20:
                    JSONObject progressjson;
                    try {
                        progressjson = new JSONObject(json);
                        String orderId = progressjson.getString("orderId");
                        intent.setClass(WebviewActivity.this,
                                AfterApplyForActivity.class);
                        intent.putExtra("order_id", orderId);
                        startActivity(intent);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                // 改变订单状态
                case 21:
                    JSONObject orderjson;
                    try {
                        orderjson = new JSONObject(json);
                        int orderId = orderjson.getInt("orderState");
                        intent.putExtra("state", orderId);
                        setResult(RESULT_OK, intent);
                        finish();

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
            }

        }
    }

    // 支付宝支付
    private void initRay(final ConfirmBean confirmBean) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(WebviewActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(
                        confirmBean.getSign(), false);
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

    /**
     * 刷卡支付响应事件
     */
    private void paySwipCard(PayBean payBean) {
        order = new Order();
        // 获取当前交易时间
        order.setmDateTime(payBean.getOrderTime());
        order.setmGoodsName("");
        // ***********订单存储**************
        order.setmPayType(StringUtil.PAY_TYPE_SWIP_CARD);
        // *************订单存储结束**********************

        // 模拟生产一个第三方流水号
        // outTradeNo = "out_" + System.currentTimeMillis();

        order.setmPayType(StringUtil.PAY_TYPE_SWIP_CARD);
        try {
            String transactionId = System.currentTimeMillis() + "";
            ParcelableMap additionalMap = new ParcelableMap();
            additionalMap.put(ParcelableMap.TRANSACTION_ID,
                    payBean.getTransactionId());
            additionalMap.put(ParcelableMap.ORDER_TIME, payBean.getOrderTime());
            additionalMap.put(ParcelableMap.CALL_BACK_URL,
                    payBean.getNotify_url());
            // 需要参与签名的字段：appCode,partnerId,iboxMchtNo,partnerUserId,transactionId,orderNo(可不传),outTradeNo,tradeNo(没有时传null),transAmount,orderTime,callbackUrl;

            // ****设置打印出来的订单号*****
            additionalMap.put(ParcelableMap.PRINT_ORDER_NO, "交易自定义第三方流水号");
            // 发起交易
            CashboxProxy.getInstance(WebviewActivity.this).startTrading(
                    PayType.TYPE_CARD, payBean.getTotal_fee(),
                    payBean.getTradeNo(), payBean.getTransactionId(),
                    SignType.TYPE_MD5, payBean.getSign(), additionalMap,
                    new ITradeCallback() {
                        @Override
                        public void onTradeSuccess(ParcelableMap map) {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            mHandler2.sendEmptyMessage(1);
                            for (String key : map.getMap().keySet()) {
                            }
                            // mCbTradeNo = map.get(ParcelableMap.CB_TRADE_NO);
                            // outTradeNo =
                            // map.get(ParcelableMap.PARTNER_TRADE_NO);
                            // mAmount = map.get(ParcelableMap.TRANS_AMOUNT);
                            // mTradeTime = map.get("transaction_time");

                            // *******写入文件******
                            // order.setmCbTradeNo(mCbTradeNo);
                            // order.setmOutTradeNo(outTradeNo);
                            // order.setmAmount(toYuanAmount(mAmount));
                            // order.setmTradeStatus("交易成功");
                            // objToFile.writeObject(order);
                            mHandler2.sendEmptyMessage(1);

                            // ********写入文件结束************
                        }

                        // 设置了不显示盒子签购单时回调此方法。
                        @Override
                        public void onTradeSuccessWithSign(ParcelableMap map,
                                                           ParcelableBitmap signBitmap) {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            mHandler2.sendEmptyMessage(1);
                            Log.e("&&&&&", "onTradeSuccessWithSign");
                            for (String key : map.getMap().keySet()) {
                            }
                            // mCbTradeNo = map.get(ParcelableMap.CB_TRADE_NO);
                            // outTradeNo =
                            // map.get(ParcelableMap.PARTNER_TRADE_NO);
                            // mAmount = map.get(ParcelableMap.TRANS_AMOUNT);
                            // mTradeTime = map.get("transaction_time");

                            // // *******写入文件******
                            // order.setmCbTradeNo(mCbTradeNo);
                            // order.setmOutTradeNo(outTradeNo);
                            // order.setmAmount(toYuanAmount(mAmount));
                            // order.setmTradeStatus("交易成功");
                            // objToFile.writeObject(order);
                        }

                        @Override
                        public void onTradeFail(ErrorMsg msg) {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            mHandler2.sendEmptyMessage(1);
                            // *******写入文件******
                            // order.setmTradeStatus(msg.getErrorMsg());
                            // order.setmAmount(toYuanAmount(mAmount));
                            // order.setmTradeStatus("交易失败");
                            // order.setmCbTradeNo(mCbTradeNo);
                            // order.setmOutTradeNo(outTradeNo);
                            // objToFile.writeObject(order);
                            // ********写入文件结束************

                        }

                    });
        } catch (ConfigErrorException e) {
            e.printStackTrace();
        }
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
            } else if (requestCode == 3) {
                // 改变订单状态 取消订单 或者确认收货
                setResult(RESULT_OK, data);
                finish();
            } else if (requestCode == 4) {
                // 申请售后成功
                Intent intent = new Intent();
                intent.putExtra("state", 8);
                setResult(RESULT_OK, intent);
                finish();

            }
        } else if (resultCode == RESULT_CANCELED) {
            // Tools.jump(WebviewActivity.this, OrderActivity.class, true);
            // Tools.showToast(WebviewActivity.this, "取消支付");
            // Tools.webexit();
            if (requestCode == 1) {
                // 验证支付密码
                mWebView.loadUrl("javascript:onPayResult(5,-1,9000)");
            } else if (requestCode == 2) {
                // 验证支付密码
                mWebView.loadUrl("javascript:onPayResult(6,-1,9000)");
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
                    Tools.showToast(WebviewActivity.this, "余额支付成功");
                    // 支付方式1、微信支付，2、支付宝支付，3、银联刷卡
                    // 4、QQ钱包支付，5、余额支付、6白条支付，7、货到付款，其他支付方式后续补充
                    mWebView.loadUrl("javascript:onPayResult(5,0,9000)");
                } else if (url.contains(Constants.whitebarPayNotify)) {
                    Tools.showToast(WebviewActivity.this, "白条支付成功");
                    mWebView.loadUrl("javascript:onPayResult(6,0,9000)");
                }
            } else if (status == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(WebviewActivity.this, LoginActivity.class, false);
                Tools.showToast(WebviewActivity.this, "登录过期请重新登录");
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
