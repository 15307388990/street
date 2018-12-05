/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.juxun.business.street.bean.PartnerBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageCompress;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.ImageUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.util.UploadUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.yl.ming.efengshe.R;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * @version 店铺设置
 */
public class StoreSetActivity extends BaseActivity {

    @ViewInject(R.id.iv_img)
    private ImageView iv_img;
    @ViewInject(R.id.tv_account)
    private TextView tv_account;// 账号名称
    @ViewInject(R.id.tv_store_name)
    private TextView tv_store_name;// 店铺名称
    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;// 配送电话
    @ViewInject(R.id.tv_phone2)
    private TextView tv_phone2;// 设置配送电话
    @ViewInject(R.id.tv_timer)
    private TextView tv_timer;// 营业时间
    @ViewInject(R.id.cb_swith)
    private CheckBox cb_swith;// 营业开关
    @ViewInject(R.id.ll_timer)
    private LinearLayout ll_timer;// 设置营业时间
    @ViewInject(R.id.ll_set_phone)
    private LinearLayout ll_set_phone;// 设置配送手机

    @ViewInject(R.id.ll_freight)
    private LinearLayout ll_freight;// 设置运费
    @ViewInject(R.id.tv_freight)
    private TextView tv_freight;// 设置运费 tv

    @ViewInject(R.id.ll_set_code)
    private LinearLayout ll_set_code;// 店铺二维码
    @ViewInject(R.id.ll_adds)
    private TextView ll_adds;// 收货地址
    @ViewInject(R.id.ll_set_announcement)
    private TextView ll_set_announcement;// 店铺公告

    @ViewInject(R.id.ll_scope)
    private LinearLayout ll_scope;// 设置配送范围
    @ViewInject(R.id.tv_scope)
    private TextView tv_scope;// 设置配送范围

    @ViewInject(R.id.shipping_address)
    private LinearLayout shipping_address;// 修改头像

    @ViewInject(R.id.ll_set_small_code)
    private LinearLayout ll_set_small_code;//小程序二维码

    private PartnerBean mPartnerBean;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();
    private String upload_token;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_set);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initTitle();
        title.setText("店铺设置");
        initView();
    }

    private void initView() {
        mPartnerBean = (PartnerBean) getIntent().getSerializableExtra("mPartnerBean");
        if (mPartnerBean != null) {
            initDate();
        }

        ll_adds.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Tools.jump(StoreSetActivity.this, AddressListActivity.class, false);
            }
        });

        // 店铺公告
        ll_set_announcement.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreSetActivity.this, AnnouncementActivity.class);
                intent.putExtra("shopNotice", mPartnerBean.getShop_notice());
                startActivityForResult(intent, 6);
            }
        });

        // 店铺二维码
        ll_set_code.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreSetActivity.this, SetCodeActivity.class);
                intent.putExtra("shopicon", mPartnerBean.getShop_icon());
                intent.putExtra("cashier_code", mPartnerBean.getCashier_qrcode());
                startActivity(intent);
            }
        });
        ll_set_small_code.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreSetActivity.this, SmallCodeActivity.class);
                startActivity(intent);
            }
        });
        // 设置营业时间
        ll_timer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreSetActivity.this, SetBussinessTimeActivity.class);
                intent.putExtra("mPartnerBean", mPartnerBean);
                Log.e("url", "传给营业时间之前" + mPartnerBean);
                startActivityForResult(intent, 1);
            }
        });
        // 设置配送电话
        ll_set_phone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreSetActivity.this, SetDeliveryPhoneActivity.class);
                intent.putExtra("phone", mPartnerBean.getStore_phone());
                startActivityForResult(intent, 2);

            }
        });
        // 设置运费
        ll_freight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreSetActivity.this, SetFreighteActivity.class);
                intent.putExtra("mPartnerBean", mPartnerBean);
                startActivityForResult(intent, 3);

            }
        });
        // 修改头像
        shipping_address.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getUploadToken();   //需要的时候请求七牛token
                intent = new Intent(StoreSetActivity.this, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                checkoutCameraPermissions();
                checkoutWritePermissions();
            }
        });
        // 设置营业范围
        ll_scope.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreSetActivity.this, SetScopeActivity.class);
                intent.putExtra("mPartnerBean", mPartnerBean);
                startActivityForResult(intent, 4);

            }
        });
        cb_swith.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Map<String, String> map = new HashMap<>();
                map.put("auth_token", partnerBean.getAuth_token());
                if (isChecked) {
                    map.put("business_status", 0 + "");
                    Tools.showToast(StoreSetActivity.this, "营业中");
                } else {
                    map.put("business_status", 1 + "");
                    Tools.showToast(StoreSetActivity.this, "暂停营业");
                }
                mQueue.add(ParamTools.packParam(Constants.changeBusiness, StoreSetActivity.this, StoreSetActivity.this,
                        map));
            }
        });
    }

    @Override
    protected void gotThePermission() {
        if (count > 1) {
            startActivityForResult(intent, 5);
        }
    }

    @PermissionGrant(REQUEST_CODE_TAKE_PHOTE)
    public void requestCameraSuccess() {
        count = count + 1;
        if (count > 1) {
            startActivityForResult(intent, 5);
        }
    }

    @PermissionDenied(REQUEST_CODE_TAKE_PHOTE)
    public void requestCameraFailed() {
        Toast.makeText(this, "请打开拍照权限", Toast.LENGTH_SHORT).show();
    }

    @PermissionGrant(REQUEST_CODE_WRITE)
    public void requestWriteSuccess() {
        count = count + 1;
        if (count > 1) {
            startActivityForResult(intent, 5);
        }
    }

    @PermissionDenied(REQUEST_CODE_WRITE)
    public void requestWriteFailed() {
        Toast.makeText(this, "请打开读写手机存储权限", Toast.LENGTH_SHORT).show();
    }

    private void getUploadToken() {
        Map<String, String> map = new HashMap<>();
        mQueue.add(ParamTools.packParam(Constants.getuploadtoken, this, this, map));
    }

    // 上传头像
    private void updateShopIcon(String shop_icon) {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("shop_icon", shop_icon);
        mQueue.add(ParamTools.packParam(Constants.updateShopIcon, this, this, map));
    }

    private void initDate() {
        cb_swith.setChecked(mPartnerBean.getBusiness_status() == 0 ? true : false);
//        // 0、未审核 1、审核通过 2、审核失败
//        switch (partnerBean.getApproval_status()) {
//            case 0:
//                tv_account.setText("未审核");
//                break;
//            case 1:
//                tv_account.setText("审核通过");
//                break;
//            case 2:
//                tv_account.setText("审核失败");
//                break;
//
//        }
        tv_account.setText(mPartnerBean.getAdmin_accout());
        tv_store_name.setText(mPartnerBean.getStore_name());
        if (mPartnerBean.getStore_phone() == null) {
            tv_phone.setVisibility(View.GONE);
            tv_phone2.setVisibility(View.VISIBLE);
        } else {
            tv_phone.setVisibility(View.VISIBLE);
            tv_phone2.setVisibility(View.GONE);
            tv_phone.setText(mPartnerBean.getStore_phone());
        }

        if (mPartnerBean.getIs_24hour_business() == 1) {
            tv_timer.setText("24小时");
        } else {
            tv_timer.setText(mPartnerBean.getBusiness_start_date() + "-" + mPartnerBean.getBusiness_end_date());
        }
        // 有运费
        if (mPartnerBean.getDelivery_price() > 0) {
            if (mPartnerBean.getDelivery_full_free() > 0) {
                tv_freight.setText("运费：" + Tools.getFenYuan(mPartnerBean.getDelivery_price()) + "元，满"
                        + Tools.getFenYuan(mPartnerBean.getDelivery_full_free()) + "免运费");
            } else {
                tv_freight.setText("运费：" + Tools.getFenYuan(mPartnerBean.getDelivery_price()) + "元，无满免运费");
            }
        } else {
            tv_freight.setText("免运费");
        }
        tv_scope.setText(mPartnerBean.getDelivery_distance() + "公里");
        imageLoader.displayImage(Constants.imageUrl + mPartnerBean.getShop_icon(), iv_img, options);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // obtainData();
    }

    /**
     * 单击事件
     */
    @OnClick({})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.exit:
                Intent intent1 = new Intent(this, LoginOutDialogActivity.class);
                startActivityForResult(intent1, 100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // 修改时间（20180104对象已修改完成，不需要传递？）
                PartnerBean fromBean = (PartnerBean) data.getSerializableExtra("mPartnerBean");
                mPartnerBean.setIs_24hour_business(fromBean.getIs_24hour_business());
                mPartnerBean.setBusiness_start_date(fromBean.getBusiness_start_date());
                mPartnerBean.setBusiness_end_date(fromBean.getBusiness_end_date());
                initDate();
            } else if (requestCode == 2) {
                // 修改配送电话
                String phoneString = data.getStringExtra("phone");
                // 修改本地数据
                mPartnerBean.setStore_phone(phoneString);
                initDate();
            } else if (requestCode == 3) {
                // 修改运费
                PartnerBean fromBean = (PartnerBean) data.getSerializableExtra("mPartnerBean");
                mPartnerBean.setDelivery_price(fromBean.getDelivery_price());
                mPartnerBean.setDelivery_full_free(fromBean.getDelivery_full_free());
                initDate();
            } else if (requestCode == 4) {
                // 修改营业范围
                String rangeString = data.getStringExtra("range");
                // 修改本地数据
                mPartnerBean.setDelivery_distance(Double.valueOf(rangeString));
                initDate();
            } else if (requestCode == 5) {
                List<String> resultList = data.getExtras().getStringArrayList("select_result");
                Bitmap bitmap = ImageUtil.decodeImage(resultList.get(0));
                iv_img.setImageBitmap(bitmap); //本地设置图片
                if (upload_token != null) {
                    upLoadPhoto(resultList.get(0));
                } else {
                    Tools.showToast(this, "上传失误，请重新上传");
                }
            } else if (requestCode == 6) {
                // 修改店铺公告
                String shopNotice = data.getStringExtra("shopNotice");
                mPartnerBean.setShop_notice(shopNotice);
            }
        }
    }

    // 上传图片得到字符串
    private void upLoadPhoto(String filePaths) {
        loading();
        UploadManager uploadManager = UploadUtil.getUploadManager();
        String key = null;
        key = Tools.getRandomFileName();
        // 这里压缩一下图片 不然上传过慢导致加载缓慢 by yejia
        uploadManager.put(ImageCompress.BitmapToBytes2(filePaths), key, upload_token,
                new UpCompletionHandler() {

                    @Override
                    public void complete(String arg0, ResponseInfo arg1, JSONObject arg2) {
                        Log.e("url", upload_token + ":::原始的东西");
                        Log.e("url", arg0 + ":::" + arg1.toString());
                        if (arg1.statusCode == 200) {
                            String icon_id = null;
                            try {
                                icon_id = arg2.getString("key");
                                updateShopIcon(icon_id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            dismissLoading();
                            Tools.showToast(StoreSetActivity.this, "上传失败");
                        }
                    }
                }, null);
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.optInt("status");
            if (status == 0) {
                if (url.contains(Constants.getuploadtoken)) {
                    upload_token = json.getString("result");
                } else if (url.contains(Constants.updateShopIcon)) {
                    dismissLoading();
                    Tools.showToast(StoreSetActivity.this, "上传成功");
                }
            } else if (status < 0) {
                Tools.dealErrorMsg(this, url, status, json.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

}
