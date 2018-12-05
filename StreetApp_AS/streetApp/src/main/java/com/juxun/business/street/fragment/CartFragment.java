package com.juxun.business.street.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.activity.FinancialInfoActivity;
import com.juxun.business.street.activity.GoodsMianAcitivity;
import com.juxun.business.street.activity.InputNumActivity;
import com.juxun.business.street.activity.LoginActivity;
import com.juxun.business.street.activity.MainActivity;
import com.juxun.business.street.activity.MipcaActivityCapture;
import com.juxun.business.street.activity.SearchWebviewActivity;
import com.juxun.business.street.activity.SupplierGoodsInfoActivity;
import com.juxun.business.street.activity.SupplierShopListActivity;
import com.juxun.business.street.activity.WebviewActivity;
import com.juxun.business.street.bean.Agreement7;
import com.juxun.business.street.bean.SupplierGoodsBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.dialog.PromptDialog;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author luoming 采购
 */
@SuppressLint("ValidFragment")
public class CartFragment extends BaseFragment {
    private String url;
    private WebView mWebView;
    private float DownX;
    private float DownY;
    private long currentMS;
    private float moveX;
    private float moveY;
    private MainActivity mActivity;
    private RelativeLayout rl_top;
    private SwipeRefreshLayout swipe_container;
    private ProgressBar pb_progressbar;


    public CartFragment(Activity context) {
        super(context);
        mActivity = (MainActivity) mcontext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onDetach() {
        // 当我们对Activity进行finish的时候，webview持有的页面并不会立即释放，如果页面中有在执行js等其他操作，仅仅进行finish是完全不够的。
        mWebView.loadUrl("about:blank");
        super.onDetach();
    }

    private void initView(View view) {
        mWebView = (WebView) view.findViewById(R.id.webview);
        TextView tv_shoplist = (TextView) view.findViewById(R.id.tv_shoplist);
        tv_shoplist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.jump(mActivity, SupplierShopListActivity.class, false);
            }
        });
        rl_top = (RelativeLayout) view.findViewById(R.id.rl_top);
        swipe_container = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        pb_progressbar = (ProgressBar) view.findViewById(R.id.pb_progressbar);
        if (mSavePreferencesData.getBooleanData("isSn")) {
            url = Constants.mainUrl + Constants.purchase + "&auth_token="
                    + partnerBean.getAuth_token() + "&v=" + System.currentTimeMillis();
        } else {
            url = Constants.mainUrl + Constants.purchase + "&auth_token="
                    + partnerBean.getAuth_token() + "&os_type=0" + "&v=" + System.currentTimeMillis();
        }

        System.out.println("purchaes_url" + url);
        init();
        initOnClik();
    }

    @Override
    public void onResume() {
        mWebView.loadUrl("javascript:onResume()");
        super.onResume();
    }

    private void init() {
        try {
            mWebView.setFocusable(true);// 设置有焦点
            mWebView.setFocusableInTouchMode(true);// 设置可触摸
            // 设置进度条
            mWebView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        pb_progressbar.setVisibility(View.GONE);
                        swipe_container.setRefreshing(false);
                    } else {
                        if (View.GONE == pb_progressbar.getVisibility()) {
                            pb_progressbar.setVisibility(View.VISIBLE);
                        }
                        pb_progressbar.setProgress(newProgress);
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
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 设置
            // 缓存模式
            // 开启 DOM storage API 功能
            mWebView.getSettings().setDomStorageEnabled(true);
            // 开启 database storage API 功能
            mWebView.getSettings().setDatabaseEnabled(true);
            String appCachePath = getActivity().getCacheDir().getAbsolutePath();
            // String cacheDirPath =
            // getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
            // 设置 Application Caches 缓存目录
            mWebView.getSettings().setAppCachePath(appCachePath);
            // 开启 Application Caches 功能
            mWebView.getSettings().setAppCacheEnabled(true);
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().setAppCachePath(appCachePath);
            mWebView.getSettings().setAllowFileAccess(true);
            mWebView.getSettings().setAppCacheEnabled(true);
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            showWebView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initOnClik() {

        rl_top.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Tools.jump(mActivity, SearchWebviewActivity.class, false);

            }
        });
        mWebView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        DownX = event.getX();// float DownX
                        DownY = event.getY();// float DownY
                        currentMS = System.currentTimeMillis();// long currentMS
                        // 获取系统时间
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // X轴距离
                        moveX = event.getX() - DownX;
                        // y轴距离
                        moveY = event.getY() - DownY;

                        long moveTime = System.currentTimeMillis() - currentMS;// 移动时间
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }

                if (Math.abs(moveX) > Math.abs(moveY)) {
                    swipe_container.setEnabled(false);
                    mWebView.requestDisallowInterceptTouchEvent(true);
                } else {
                    swipe_container.setEnabled(true);
                    mWebView.requestDisallowInterceptTouchEvent(false);
                }

                return false;
            }
        });
        swipe_container.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                showWebView();
            }
        });
    }

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    private void showWebView() { // webView与js交互代码
        // 设置本地调用对象及其接口
        mWebView.addJavascriptInterface(new JavaScriptObject(mcontext), "native");
        mWebView.loadUrl(url);
    }

    public class JavaScriptObject {
        Context mContxt;

        public JavaScriptObject(Context mContxt) {
            this.mContxt = mContxt;
        }

        // 绑定事件
        @JavascriptInterface
        public void transfer(int type, String json) {
            //Tools.showToast(mActivity, "type=" + type + "json=" + json);
            Intent intent = new Intent();
            switch (type) {
                // 跳转至商品
                case 3:

                    // 跳转财务
                case 4:
                    intent.setClass(mActivity, FinancialInfoActivity.class);
                    startActivity(intent);
                    break;
                // 跳转至扫码
                case 5:
                    intent.setClass(mActivity, MipcaActivityCapture.class);
                    startActivityForResult(intent, 1);
                    break;
                // 跳转至收款
                case 6:
                    intent.setClass(mActivity, InputNumActivity.class);
                    startActivity(intent);
                    break;
                // 跳转至其它链接
                case 7:
                    intent.setClass(mActivity, WebviewActivity.class);
                    Agreement7 agreement7 = JSON.parseObject(json, Agreement7.class);
                    intent.putExtra("agreement7", agreement7);
                    startActivity(intent);
                    break;
                case 11:
                    JSONObject diogjson;
                    try {
                        diogjson = new JSONObject(json);
                        PromptDialog promptDialog = new PromptDialog(getActivity());
                        promptDialog.setContent(diogjson.getString("text_name"));
                        promptDialog.setText(diogjson.getString("button_name"), "");
                        promptDialog.setFocusable(false);
                        promptDialog.showAtLocation(mActivity.back, Gravity.CENTER, 0, 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                // token过期处理
                case 12:
                    mSavePreferencesData.putStringData("json", "");
                    Tools.showToast(mActivity, "登录已过期，请重新登录");
                    intent.setClass(mActivity, LoginActivity.class);
                    startActivity(intent);
                    Tools.exit();
                    break;
                // token过期处理
                case 23:
                    intent.setClass(mActivity, SupplierShopListActivity.class);
                    intent.putExtra("json", json);
                    startActivity(intent);
                    break;
                //商品详情
                case 24:
                    intent.setClass(mActivity, SupplierGoodsInfoActivity.class);
                    if (json != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            intent.putExtra("commodity_id", jsonObject.getString("commodity_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }

        public void GoShop() {

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResponse(String response, String url) {
    }

}
