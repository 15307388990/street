/**
 *
 */
package com.juxun.business.street.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.google.gson.Gson;
import com.juxun.business.street.UILApplication;
import com.juxun.business.street.bean.Msgmodel;
import com.juxun.business.street.bean.OrderModel;
import com.juxun.business.street.bean.ParseModel;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.service.DownloadService;
import com.juxun.business.street.service.MyMQTTService;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.SavePreferencesData;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：Street 类名称：StreetActivity 类描述： 街边TAB 创建人：WuJianhua 创建时间：2015年5月27日
 * 下午7:11:46 修改人：WuJianhua 修改时间：2015年5月27日 下午7:11:46 修改备注：
 */
public class StreetActivity extends BaseActivity {

    @ViewInject(R.id.scann)
    private TextView scann; // 验码
    @ViewInject(R.id.receiveMoney)
    private TextView receiveMoney; // 收款
    @ViewInject(R.id.customers)
    private TextView customers; // 团购
    @ViewInject(R.id.vip)
    private TextView vip; // 会员卡
    @ViewInject(R.id.coupon)
    private TextView coupon; // 优惠券
    @ViewInject(R.id.order)
    private TextView order; // 订单
    @ViewInject(R.id.bill)
    private TextView bill; // 账单
    @ViewInject(R.id.courier)
    private TextView courier; // 更多
    @ViewInject(R.id.number)
    private static TextView number; // 未发货数量
    @ViewInject(R.id.tv_more)
    private TextView tv_more; // 更多订单
    @ViewInject(R.id.tv_name)
    private static TextView tv_name; // 客户名称
    @ViewInject(R.id.tv_adds)
    private static TextView tv_adds; // 客户地址
    @ViewInject(R.id.tv_store)
    private static TextView tv_store; // 客户商品
    @ViewInject(R.id.tv_time)
    private static TextView tv_time; // 下单时间
    @ViewInject(R.id.ll_have)
    private static LinearLayout ll_have; // 有数据时候的布局
    @ViewInject(R.id.ll_wu)
    private static LinearLayout ll_wu; // 无数据时候的布局
    // 强制自动升级
    private Boolean ifNeedUpdate = true;
    private int serviceVersion;
    private int localVersion;
    private String mall_name;// 版本名称
    Runnable runnable;
    UILApplication app;
    OrderModel orderModel;
    Gson gson = new Gson();
    private long mExitTime;
    private SavePreferencesData savePreferencesData;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    // 更新你相应的UI
                {
                    showUpdateDialog();
                    mHandler.removeCallbacks(runnable);
                    break;
                }
            }

        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street);
        ViewUtils.inject(this);
        initTitle();
        back.setVisibility(View.GONE);
        title.setText("e蜂社商户版");
        more.setBackgroundResource(R.drawable.home_icon_setting_white);
        more.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Tools.jump(StreetActivity.this, MoreActivity.class, false);
            }
        });
        more.setVisibility(View.VISIBLE);
        savePreferencesData = new SavePreferencesData(this);
//        // 判断是否有数据 如果没有 就去登录
//        if (storeBean.getAdmin_qid().equals("")) {
//            savePreferencesData.putStringData("pwd", null);
//            Tools.jump(StreetActivity.this, LoginActivity.class, true);
//            Tools.showToast(getApplicationContext(), "登录已经过时，请重新登录");
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        obtainData();
        updataApp();
        // 每次回到首页在绑定一下
        posReg();
    }

    /* 注册推送服务 */
    private void posReg() {
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        // 用QID 绑定别名
        pushService.bindAccount("efengshepos_" + partnerBean.getStore_id(), new CommonCallback() {

            @Override
            public void onSuccess(String arg0) {
                // TODO Auto-generated method stub
                System.out.println("绑定别名成功");

            }

            @Override
            public void onFailed(String arg0, String arg1) {
                // TODO Auto-generated method stub
                System.out.println("绑定别名失败" + arg0 + arg1);

            }
        });
    }

    private void showUpdateDialog() {
        Activity activity = StreetActivity.this;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("检测到新版本" + mall_name);
        builder.setMessage("需要下载更新");
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent it = new Intent(StreetActivity.this, NotificationUpdateActivity.class);
                startActivity(it);
                app.setDownload(true);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        builder.show();
    }

    /* 获取数据 */
    public void obtainData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        // map.put("community_id", storeBean.getAdmin_qid() + "");// 社区ID
        // map.put("admin_id", storeBean.getAdmin_id() + "");// 账号ID
        mQueue.add(ParamTools.packParam(Constants.totalmian, this, this, map));
    }

    /**
     * 检测版本
     **/
    public void updataApp() {
        app = (UILApplication) getApplication();
        loadVersion();
        Map<String, String> map = new HashMap<String, String>();
        mQueue.add(ParamTools.packParam(Constants.updateAPP, this, this, map));
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.scann, R.id.receiveMoney, R.id.customers, R.id.vip, R.id.coupon, R.id.order, R.id.bill,
            R.id.courier, R.id.tv_more, R.id.ll_have})
    public void clickMethod(View v) {
        if (v.getId() == R.id.scann) {
            Intent intent = new Intent(this, MipcaActivityCapture.class);
            startActivityForResult(intent, 1);

        } else if (v.getId() == R.id.receiveMoney) {
            Tools.jump(this, InputNumActivity.class, false);
        } else if (v.getId() == R.id.vip) {
            Tools.jump(this, FinancialInfoActivity.class, false);
            // Tools.showToast(getApplicationContext(), "暂未开放此功能");
        } else if (v.getId() == R.id.coupon) {
            // Tools.jump(this, CouponListActivity.class, false);

            // Tools.jump(this, PurchaseMianAcitivity.class, false);
        } else if (v.getId() == R.id.order) {
            Tools.jump(this, ElectricitySupplierOrderActivity.class, false);
        } else if (v.getId() == R.id.bill) {
            Tools.jump(this, GoodsMianAcitivity.class, false);
        } else if (v.getId() == R.id.courier) {
            Tools.showToast(getApplicationContext(), "暂未开放此功能");
        } else if (v.getId() == R.id.tv_more) {
            Tools.jump(this, ElectricitySupplierOrderActivity.class, false);
        } else if (v.getId() == R.id.ll_have) {
            if (orderModel != null) {
                Intent intent = new Intent(this, MyOrderDetailsActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("orderModel", orderModel);
                intent.putExtras(mBundle);
                startActivity(intent);
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            String result = data.getStringExtra("result");
            Intent intent = new Intent(this, FinancialInfoActivity.class);
            intent.putExtra("result", result);
            startActivity(intent);
        }
    }

    public static void initDate(OrderModel orderModel, int is) {
        List<Msgmodel> msgmodels = new ArrayList<Msgmodel>();
        try {
            msgmodels = new ParseModel().getMsgmodel(orderModel.getSpec());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < msgmodels.size(); i++) {
            sb.append(msgmodels.get(i).getCommodityName() + "x" + msgmodels.get(i).getGoodsCount());
        }

        tv_name.setText("客户：" + orderModel.getCommunity_name() + "     联系电话：" + orderModel.getCommunity_phone());
        tv_store.setText("商品：" + sb);
        tv_adds.setText("地址：" + orderModel.getConsignee_address());
        tv_time.setText("下单时间：" + orderModel.getCreate_date());
        if (is == 1) {
            ll_have.setVisibility(View.VISIBLE);
            ll_wu.setVisibility(View.GONE);
            String order_num = number.getText().toString();
            number.setVisibility(View.VISIBLE);
            if (!order_num.equals("")) {
                int num = Integer.parseInt(order_num) + 1;
                number.setText(num + "");
            } else {
                number.setText(1 + "");
            }
        }
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            if (stauts == 0) {
                if (url.contains(Constants.totalmian)) {
                    // stauts Integer 状态0为成功，0<为失败
                    // msg String 查询信息
                    // order_nums Integer 未发货订单数量
                    // has_last_order Integer 是否有最新订单0有，-1无
                    // last_order MallOrderBean 最新未发货订单

                    int order_num = json.getInt("order_nums");
                    if (order_num == 0) {
                        number.setVisibility(View.GONE);
                    } else {
                        number.setVisibility(View.VISIBLE);
                        number.setText(order_num + "");
                    }
                    int has_last_order = json.getInt("has_last_order");
                    if (has_last_order == -1) {
                        ll_wu.setVisibility(View.VISIBLE);
                        ll_have.setVisibility(View.GONE);
                    } else {
                        ll_wu.setVisibility(View.GONE);
                        ll_have.setVisibility(View.VISIBLE);
                        String jsonString = json.getString("last_order");
                        orderModel = gson.fromJson(jsonString, OrderModel.class);
                        initDate(orderModel, 0);
                    }
                } else if (url.contains(Constants.updateAPP)) {
                    serviceVersion = json.getInt("mall_version");
                    String downloadAddress = json.getString("mall_url");
                    mall_name = json.getString("mall_name");
                    DownloadService.apkUrl = Html.fromHtml(downloadAddress) + "";
                    System.out.println("com.juxun.business.street.activity.service" + "localVersion：  " + localVersion
                            + "serviceVersion：  " + serviceVersion);
                    if (localVersion < serviceVersion) {
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                // stub
                                // 要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作

                                mHandler.sendEmptyMessage(0);
                            }
                        };
                        mHandler.postDelayed(runnable, 1000);
                    }
                } else if (url.contains(Constants.posReg) && MyMQTTService.isStart == false) {
                    MyMQTTService.actionStart(getApplicationContext());
                }

            } else if (stauts == -1 && MyMQTTService.isStart == false) {
                MyMQTTService.actionStart(getApplicationContext());
            } else if (stauts == -4004) {
                Tools.jump(this, LoginActivity.class, true);
                mSavePreferencesData.putStringData("json", "");
                Toast.makeText(this, "您的账号在其他地方登录", Toast.LENGTH_SHORT).show();

            } else {
                // Tools.showToast(getApplicationContext(),
                // json.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    private void loadVersion() {
        PackageManager pm = this.getPackageManager();// context为当前Activity上下文
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(this.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        localVersion = pi.versionCode;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
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

}
