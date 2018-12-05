/**
 *
 */
package com.juxun.business.street.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yl.ming.efengshe.R;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.DataClearManager;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @version 设置
 */
public class MoreActivity extends BaseActivity {

    @ViewInject(R.id.exit)
    private TextView exit; // 退出登录
    @ViewInject(R.id.suggestView)
    private LinearLayout suggestView; // 意见反馈
    @ViewInject(R.id.clearView)
    private RelativeLayout clearView; // 清除缓存
    @ViewInject(R.id.rl_cash)
    private RelativeLayout rl_cash;// 提现账户设置
    @ViewInject(R.id.rl_set_pay)
    private RelativeLayout ll_set_pay;// 支付安全设置

    @ViewInject(R.id.cacheSize)
    private TextView cacheSize; // 缓存大小
    @ViewInject(R.id.aboutView)
    private RelativeLayout aboutView; // 关于我们
    @ViewInject(R.id.recommView)
    private RelativeLayout recommView; // 推荐给小伙伴
    @ViewInject(R.id.updateView)
    private RelativeLayout updateView; // 更新
    @ViewInject(R.id.version)
    private TextView version; // 当前版本

    @ViewInject(R.id.rl_modify_password)
    private RelativeLayout rl_modify_password;// 修改登录密码

    private boolean needUpdate;// 是否需要更新
    private String updateUrl;// 更新地址

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
            } else if (msg.what == 2) {
                pd.dismiss();
                File file = (File) msg.obj;
                installApk(file);
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ViewUtils.inject(this);
        initTitle();
        title.setText(R.string.more);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        version.setText("v_" + info.versionName);
        try {
            cacheSize.setText(DataClearManager.getCacheSize(new File("/data/data/" + getPackageName())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // obtainData();
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.exit, R.id.rl_modify_password, R.id.rl_cash, R.id.clearView, R.id.recommView, R.id.aboutView,
            R.id.problemView, R.id.updateView, R.id.shipping_address, R.id.rl_set_pay, R.id.suggestView})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.exit:
                Intent intent1 = new Intent(this, LoginOutDialogActivity.class);
                startActivityForResult(intent1, 100);
                break;
            case R.id.rl_modify_password:
                Intent intent = new Intent(this, ModifyPasswordActivity.class);
                intent.putExtra("flag", true);
                startActivity(intent);
                break;
            case R.id.clearView:
                DataClearManager.cleanApplicationData(this, (String[]) null);
                final Dialog dialog = ProgressDialog.show(this, "", "正在清除...");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        try {
                            cacheSize.setText(DataClearManager.getCacheSize(new File("/data/data/" + getPackageName())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);
                break;
            case R.id.rl_cash:
                Tools.jump(MoreActivity.this, CashAccountListActivity.class, false);
                break;
            // 支付安全设置
            case R.id.rl_set_pay:
                Tools.jump(MoreActivity.this, SetPaymentSecurityActivity.class, false);
                break;
            case R.id.problemView:
                Intent intent2 = new Intent(this, AboutActivity.class);
                intent2.putExtra("isAboult", false);
                startActivity(intent2);
                break;
            case R.id.aboutView:
                Intent intent3 = new Intent(this, AboutusActivity.class);
                intent3.putExtra("isAboult", true);
                startActivity(intent3);
                break;
            // 意见反馈
            case R.id.suggestView:
                Tools.jump(MoreActivity.this, FeedBackActivity.class, false);
                break;
            case R.id.updateView:
                if (needUpdate) {
                    Intent intent4 = new Intent(this, DownlineTipsDialogActivity.class);
                    startActivityForResult(intent4, 101);
                }
                break;
            case R.id.shipping_address:
                Tools.jump(this, AddressListActivity.class, false);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            Tools.jump(this, LoginActivity.class, true);
            Tools.exit();
        } else if (resultCode == RESULT_OK && requestCode == 101) {
            // updateUrl =
            // "http://www.apk.anzhi.com/apk/201412/24/com.huaqgzab.client_98348197.apk";
            downloadApk(updateUrl);
        }
    }

    private ProgressDialog pd;

    /**
     * 下载apk
     **/
    private void downloadApk(final String url) {
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    @SuppressWarnings("unused")
                    File file = getFileFromServer(url, pd);

                    Message msg = new Message();
                    msg.obj = file;
                    msg.what = 2;
                    handler.sendMessageDelayed(msg, 3000);

                } catch (Exception e) {

                    handler.sendEmptyMessage(1);
                    e.printStackTrace();
                }
            }

        }.start();

    }

    public static File getFileFromServer(String path, ProgressDialog pd) {

        try {
            // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                int length = conn.getContentLength();
                // 获取到文件的大小
                pd.setMax(100);
                if (conn.getResponseCode() == 200) {
                    InputStream is = conn.getInputStream();
                    String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/street";
                    File fileDir = new File(dir);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    File file = new File(fileDir, "update.apk");
                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] byt = new byte[1024];
                    int len = 0;
                    int total = 0;
                    while ((len = is.read(byt)) != -1) {
                        total = total + len;
                        int progress = 100 * total / length;
                        pd.setProgress(progress);
                        fos.write(byt, 0, len);
                    }

                    return file;
                }

            } else {
                return null;
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    // 安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /* 获取数据 */
    public void obtainData() {
        Map<String, String> map = new HashMap<String, String>();
        mQueue.add(ParamTools.packParam(Constants.updateAPP, this, this, map));
        // loading();
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            if (stauts == 0) {
                String serverVersionCode = json.optString("version", "");
                if (serverVersionCode.compareTo(getPackageInfo(this).versionName) > 0) {
                    needUpdate = true;
                    version.setText(R.string.need_update);
                    version.setTextColor(Color.RED);
                }
            } else if (stauts < 0) {
                Tools.dealErrorMsg(this, url, stauts, json.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

}
