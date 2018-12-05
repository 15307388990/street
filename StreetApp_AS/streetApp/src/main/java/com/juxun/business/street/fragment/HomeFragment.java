package com.juxun.business.street.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.activity.AredenvelopeDialogActivity;
import com.juxun.business.street.activity.CouponActivity;
import com.juxun.business.street.activity.DeccaDataActivity;
import com.juxun.business.street.activity.FinancialInfoActivity;
import com.juxun.business.street.activity.InputNumActivity;
import com.juxun.business.street.activity.InthesaleAcitivity;
import com.juxun.business.street.activity.LoginActivity;
import com.juxun.business.street.activity.MessageListActivity;
import com.juxun.business.street.activity.MainActivity;
import com.juxun.business.street.activity.MipcaActivityCapture;
import com.juxun.business.street.activity.StoreValueCenterActivity;
import com.juxun.business.street.activity.WebviewActivity;
import com.juxun.business.street.bean.Agreement7;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.dialog.PromptDialog;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author luoming 首页
 */
public class HomeFragment extends BaseFragment {
    private String url;
    private WebView mWebView;
    private float DownX;
    private float DownY;
    private long currentMS;
    private float moveX;
    private float moveY;

    private MainActivity mActivity;
    private SwipeRefreshLayout swipe_container;
    private ProgressBar pb_progressbar;

    public HomeFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @SuppressLint("ValidFragment")
    public HomeFragment(Activity context) {
        super(context);
        this.mActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initAdmin();
        initView(view);
        return view;
    }

    @Override
    public void onDetach() {
        // 当我们对Activity进行finish的时候，webview持有的页面并不会立即释放，如果页面中有在执行js等其他操作，仅仅进行finish是完全不够的。
        mWebView.loadUrl("about:blank");
        super.onDetach();
    }

    @Override
    public void onResume() {
        mWebView.loadUrl("javascript:onResume()");
        super.onResume();
    }

    private void initView(View view) {
        LinearLayout topviewBack = (LinearLayout) view.findViewById(R.id.top_view_back);
        topviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转商品管理
                startActivity(new Intent(mActivity, InthesaleAcitivity.class));
                //跳抓付款
//                startActivity(new Intent(mActivity, InputNumActivity.class));
            }
        });

        TextView tv_right_text = (TextView) view.findViewById(R.id.right_view_text);
        tv_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, CouponActivity.class));
            }
        });

        mWebView = (WebView) view.findViewById(R.id.webview);
        swipe_container = (SwipeRefreshLayout) view
                .findViewById(R.id.swipe_container);
        pb_progressbar = (ProgressBar) view.findViewById(R.id.pb_progressbar);
        String auth_token = mSavePreferencesData.getStringData("auth_token");
        if (auth_token != null) {
            url = Constants.mainUrl + Constants.posindex + "auth_token=" + auth_token;
            // url = "http://memberapitest.efengshe.com/3/mall/app_view_decoration.jsp?os=1&auth_token=14AEFC293113247DBD1D2FFF8042CF9C&v=1524825694639";
            System.out.println("url+" + url);
        }

        init();
        initOnClik();
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
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDatabaseEnabled(true);
            webSettings.setDefaultTextEncodingName("utf-8");
            showWebView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initOnClik() {
        swipe_container.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                showWebView();
            }
        });
    }

    @SuppressLint("JavascriptInterface")
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
            System.out.println("url+home=" + type + "json=" + json);
            Intent intent = new Intent();
            switch (type) {
                // 红包弹框
                case 1:
                    intent.setClass(mActivity, AredenvelopeDialogActivity.class);
                    double price = 0;
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        price = jsonObject.getDouble("price");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("price", price);
                    startActivity(intent);
                    break;
                // 跳转至消息中心
                case 2:
                    intent.setClass(mActivity, MessageListActivity.class);
                    startActivity(intent);
                    break;
                // 跳转至商品
                case 3:
                    Log.e("url", "InthesaleAcitivity");
                    intent.setClass(mActivity, InthesaleAcitivity.class);
                    startActivity(intent);
                    break;
                case 22: // 储值中心
                    Tools.jump(mActivity, StoreValueCenterActivity.class, false);
                    // intent.setClass(mActivity, StoreValueCenterActivity.class);
                    // startActivity(intent);
                    break;
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
                    initAdmin();
                    if (partnerBean.getApproval_status() == 1) {
                        intent.setClass(mActivity, InputNumActivity.class);
                        startActivity(intent);
                    } else {
                        Tools.showToast(mContxt, "账号未通过审核，该功能暂时无法使用");
                    }
                    break;
                // 跳转至代金券
                case 7:
                    intent.setClass(mActivity, CouponActivity.class);
//                    Agreement7 agreement7 = JSON
//                            .parseObject(json, Agreement7.class);
//                    agreement7.setLink_url(agreement7.getLink_url() + "?auth_token="
//                            + partnerBean.getAuth_token() + "&v="
//                            + System.currentTimeMillis());
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
                        promptDialog.showAtLocation(mWebView, Gravity.CENTER, 0, 0);
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
                // 定义外部app 检测是否安装 启动
                case 13:
                    String download_url = "";// 下载地址
                    String android_package = "";// 安卓包
                    try {
                        JSONObject downjson = new JSONObject(json);
                        download_url = downjson.getString("download_url");
                        android_package = downjson.getString("android_package");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (Tools.isAppInstalled(getActivity(), android_package)) {
                        Intent intent2 = getActivity().getPackageManager()
                                .getLaunchIntentForPackage(android_package);
                        startActivity(intent2);
                    } else {
                        Uri uri = Uri.parse(download_url);
                        Intent downloadintent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(downloadintent);
                    }
                    break;
                // 定义外部app 检测是否安装启动
                case 14:
                    intent.setClass(mActivity, DeccaDataActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
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
