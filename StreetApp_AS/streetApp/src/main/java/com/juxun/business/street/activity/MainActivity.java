package com.juxun.business.street.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.allenliu.versionchecklib.core.AllenChecker;
import com.allenliu.versionchecklib.core.VersionParams;
import com.allenliu.versionchecklib.core.http.HttpParams;
import com.allenliu.versionchecklib.core.http.HttpRequestMethod;
import com.juxun.business.street.UILApplication;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.fragment.CartFragment;
import com.juxun.business.street.fragment.HomeFragment;
import com.juxun.business.street.fragment.OrderFragment;
import com.juxun.business.street.fragment.StoreFragment;
import com.juxun.business.street.service.DownloadService;
import com.juxun.business.street.service.VersionService;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.dialog.PromptDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.juxun.business.street.config.Constants.mallSetInfo;

/**
 * @author 底部导航主页
 */
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.tab_rb_home)
    private RadioButton tab_rb_home;
    @ViewInject(R.id.tab_rb_cart)
    private RadioButton tab_rb_cart;
    @ViewInject(R.id.tab_rb_order)
    private RadioButton tab_rb_order;
    @ViewInject(R.id.tab_rb_store)
    private RadioButton tab_rb_store;
    @ViewInject(R.id.tab_rg_menu)
    private RadioGroup tab_rg_menu;
    @ViewInject(R.id.rl_order)
    private RelativeLayout rl_order;
    @ViewInject(R.id.fragment_container)
    private FrameLayout fragment_container;
    @ViewInject(R.id.tv_number)
    private TextView tv_number;

    private int serviceVersion;
    private int localVersion;
    private Fragment mHomeFragment, mCartFragment, mOrderFragment, mStoreFragment;
    Runnable runnable;
    private long mExitTime;
    private String mall_name;// 版本名称
    UILApplication app;
    private boolean isopen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        Tools.acts.add(this);

        bindView();
        updataApp();       //获取app版本
        isopen = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mallSetInfo(mSavePreferencesData.getStringData("auth_token"));
    }

    /* 注册推送服务 */
    private void posReg() {
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        // 用QID 绑定别名
        pushService.bindAccount("efengshepos_" + partnerBean.getStore_id(), new CommonCallback() {

            @Override
            public void onSuccess(String arg0) {
                System.out.println("绑定别名成功");
            }

            @Override
            public void onFailed(String arg0, String arg1) {
                System.out.println("绑定别名失败" + arg0 + arg1);
            }
        });
    }

    /* 获取店铺详情 */
    private void mallSetInfo(String auth_token) {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", auth_token);
        mQueue.add(ParamTools.packParam(Constants.mallSetInfo, this, this, map));
    }


    /**
     * 检测是否弹框
     */
    private void DetectBounced() {
        if (partnerBean.getSafe_phone() == null) {
            // 如果没有密保手机 弹框密保手机的框架
            PromptDialog promptDialog = new PromptDialog(this);
            promptDialog.setContent("账号尚未绑定手机号，为了安全请立即绑定");
            promptDialog.setText("绑定手机", "");
            promptDialog.setFocusable(false);
            promptDialog.setonConfirmListener(new PromptDialog.onConfirmListener() {

                @Override
                public void onConfirm(int id) {
                    Intent intent = new Intent(MainActivity.this, MobilePhoneActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
            promptDialog.showAtLocation(tab_rb_home, Gravity.CENTER, 0, 0);
        } else if (partnerBean.getPay_password() == 0) {
            PromptDialog promptDialog = new PromptDialog(this);
            promptDialog.setContent("为了您的支付安全，请设置支付密码！");
            promptDialog.setText("立即设置", "");
            promptDialog.setFocusable(false);
            promptDialog.setonConfirmListener(new PromptDialog.onConfirmListener() {

                @Override
                public void onConfirm(int id) {
                    Intent intent = new Intent(MainActivity.this, MobilePhoneActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                }
            });
            promptDialog.showAtLocation(tab_rb_home, Gravity.CENTER, 0, 0);
        }
    }


    private void loadVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(this.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        localVersion = pi.versionCode;
    }

    /**
     * 检测版本
     **/
    private void updataApp() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("device_type", "0");//设备类型 0为Android 1为ios
        httpParams.put("app_type", "1");//1商户版2街边go3俊鹏
        VersionParams.Builder builder = new VersionParams.Builder().setRequestMethod(HttpRequestMethod.POST).setRequestParams(httpParams)
                .setRequestUrl(Constants.mainUrl + Constants.updateAPP)
                .setCustomDownloadActivityClass(CustomDialogActivity.class)
                .setService(VersionService.class);
        AllenChecker.startVersionCheck(this, builder.build());


    }

    /* 获取订单数量 */
    private void obtainData() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("order_state", "2");
        mQueue.add(ParamTools.packParam(Constants.shopOrderList, this, this, map));
    }

    // UI组件初始化与事件绑定
    private void bindView() {
        tab_rg_menu.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TOD
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                hideAllFragment(transaction);
                switch (checkedId) {
                    case R.id.tab_rb_home:
                        if (mHomeFragment == null) {
                            mHomeFragment = new HomeFragment(MainActivity.this);
                            transaction.add(R.id.fragment_container, mHomeFragment);
                        } else {
                            transaction.show(mHomeFragment);
                        }
                        break;
                    case R.id.tab_rb_cart:
                        if (mCartFragment == null) {
                            mCartFragment = new CartFragment(MainActivity.this);
                            transaction.add(R.id.fragment_container, mCartFragment);
                        } else {
                            transaction.show(mCartFragment);
                        }
                        break;
                    case R.id.tab_rb_order:
                        if (mOrderFragment == null) {
                            mOrderFragment = new OrderFragment(MainActivity.this);
                            transaction.add(R.id.fragment_container, mOrderFragment);
                        } else {
                            transaction.show(mOrderFragment);
                        }
                        break;
                    case R.id.tab_rb_store:
                        if (mStoreFragment == null) {
                            mStoreFragment = new StoreFragment(MainActivity.this);
                            transaction.add(R.id.fragment_container, mStoreFragment);
                            transaction.show(mStoreFragment);
                        } else {
                            transaction.show(mStoreFragment);
                        }
                        break;

                    default:
                        break;
                }
                transaction.commit();

            }
        });
        tab_rg_menu.check(R.id.tab_rb_home);
        loadVersion();
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            if (stauts == 0) {
                if (url.contains(Constants.updateAPP)) {

                } else if (url.contains(Constants.shopOrderList)) {
                    int order_num = json.optJSONArray("result").length();
                    if (order_num == 0) {
                        tv_number.setVisibility(View.GONE);
                    } else {
                        tv_number.setVisibility(View.VISIBLE);
                        tv_number.setText(order_num + "");
                    }
                    posReg();
                } else if (url.contains(mallSetInfo)) {
                    mSavePreferencesData.putStringData("json", json.optString("result"));
                    initAdmin();
                    obtainData();   //获取首页的数据
                    DetectBounced();
                }
            } else if (stauts == -40004) {
                Tools.showToast(getApplicationContext(), "登录过期请重新登录");
                mSavePreferencesData.putStringData("json", json.optString(""));
                Tools.jump(MainActivity.this, LoginActivity.class, true);
            } else {
                Tools.showToast(getApplicationContext(), json.getString("msg"));
            }
        } catch (JSONException e) {
            Tools.showToast(getApplicationContext(), "解析数据错误");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // if ((System.currentTimeMillis() - mExitTime) > 2000)
            // {Toast.makeText(this,
            // getResources().getString(R.string.exit).toString(),
            // Toast.LENGTH_SHORT).show();
            // mExitTime = System.currentTimeMillis();}

            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
                intent.addCategory(Intent.CATEGORY_HOME);
                this.startActivity(intent);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 隐藏�?有Fragment
    public void hideAllFragment(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mCartFragment != null) {
            transaction.hide(mCartFragment);
        }
        if (mOrderFragment != null) {
            transaction.hide(mOrderFragment);
        }
        if (mStoreFragment != null) {
            transaction.hide(mStoreFragment);
        }
    }
}
