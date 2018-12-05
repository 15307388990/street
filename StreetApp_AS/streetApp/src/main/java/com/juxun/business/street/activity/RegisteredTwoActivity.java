package com.juxun.business.street.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.juxun.business.street.bean.AddsBean;
import com.juxun.business.street.bean.ImageWithDelete;
import com.juxun.business.street.bean.ImageWithDelete.ICallBack;
import com.juxun.business.street.bean.StoreRegisterBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageCompress;
import com.juxun.business.street.util.ImageUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.util.UploadUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
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
 * @author luoming 注册流程第二步
 */

public class RegisteredTwoActivity extends BaseActivity implements TextWatcher, OnClickListener {

    @ViewInject(R.id.et_store_name)
    private EditText et_store_name;// 店铺名称
    @ViewInject(R.id.et_name)
    private EditText et_name;// 联系人
    @ViewInject(R.id.tv_store_adds)
    private TextView tv_store_adds;// 所在地区
    @ViewInject(R.id.et_store_adds)
    private EditText et_store_adds;// 详细地址
    @ViewInject(R.id.iv_store)
    private ImageView iv_store;// 店面门头照
    @ViewInject(R.id.iw_store)
    private ImageWithDelete iw_store;// 照片的右上角删除角标
    @ViewInject(R.id.btn_next)
    private Button btn_next;// 下一步

    private StoreRegisterBean storeRegisterBean;    //提供注册需要的个人信息
    private AddsBean mAddsBean;
    private String shop_icon = null;// shop_icon
    private String upload_token;
    private Intent intent;
    private String province;
    private String district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_two);
        ViewUtils.inject(this);
        Tools.acts.add(this);

        getUploadToken();
        initView();
    }

    private void getUploadToken() {
        Map<String, String> map = new HashMap<>();
        mQueue.add(ParamTools.packParam(Constants.getuploadtoken, this, this, map));
    }

    private void initView() {
        initTitle();
        title.setText("注册");
        btn_next.setEnabled(selectBtn());

        storeRegisterBean = (StoreRegisterBean) getIntent().getSerializableExtra("storeRegisterBean");

        et_store_name.addTextChangedListener(this);
        et_store_adds.addTextChangedListener(this);
        et_name.addTextChangedListener(this);

        tv_store_adds.setOnClickListener(this);
        iv_store.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        iw_store.setonClick(new ICallBack() {

            @Override
            public void onClickDeleteButton(int position) {
                iw_store.setVisibility(View.GONE);
                iv_store.setVisibility(View.VISIBLE);
                shop_icon = null;
            }
        });
    }

    /*
       et_store_name,et_store_adds,et_name
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        btn_next.setEnabled(selectBtn());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private boolean selectBtn() {
        if (et_store_adds.getText().length() > 0 && et_store_name.getText().length() >= 1
                && tv_store_adds.getText().length() > 1 && et_name.getText().length() >= 1) {
            btn_next.setBackgroundResource(R.drawable.button_bg1);
            btn_next.setTextColor(getResources().getColor(R.color.white));
            return true;
        } else {
            btn_next.setBackgroundResource(R.drawable.button_bg);
            btn_next.setTextColor(getResources().getColor(R.color.jiujiujiu));
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_store:
                intent.setClass(RegisteredTwoActivity.this, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                startActivityForResult(intent, 1);// 店面门头照
//                checkoutWritePermissions();
//                checkoutCameraPermissions();
                break;
            case R.id.tv_store_adds:
                // 选择地址
                intent.setClass(this, LocationMapActivity.class);
                checkoutLocationPermissions();
                break;
            case R.id.btn_next:
                if (shop_icon != null) {
                    register();
                } else {
                    Tools.showToast(RegisteredTwoActivity.this, "门头照没有选择");
                }
                break;
        }
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

    @Override
    protected void gotThePermission() {
        startActivityForResult(intent, 1);
        if (count > 1) {
            startActivityForResult(intent, 4);// 店面门头照
        }
    }

    @PermissionGrant(REQUEST_CODE_TAKE_PHOTE)
    public void requestCameraSuccess() {
        //扫描只有在被同意的情况下才能跳转
        count = count + 1;
        startActivityForResult(intent, 1);
        if (count > 1) {
            startActivityForResult(intent, 4);// 店面门头照
        }
    }

    @PermissionDenied(REQUEST_CODE_TAKE_PHOTE)
    public void requestCameraFailed() {
        Toast.makeText(this, "请打开定位权限以进行百度地图定位", Toast.LENGTH_SHORT).show();
    }

    // 上传图片得到字符串
    private void upLoadPhoto(String filePaths, final int id) {
        loading();
        UploadManager uploadManager = UploadUtil.getUploadManager();
        String key = Tools.getRandomFileName();
        // 这里压缩一下图片 不然上传过慢导致加载缓慢 by yejia
        Log.e("url", upload_token);
        uploadManager.put(ImageCompress.BitmapToBytes2(filePaths), key, upload_token,
                new UpCompletionHandler() {

                    @Override
                    public void complete(String arg0, ResponseInfo arg1, JSONObject arg2) {
                        Log.e("url", arg0 + "::::::" + arg1.toString() + ":::::" + arg2.toString());
                        if (arg1.statusCode == 200) {
                            dismissLoading();
                            Tools.showToast(RegisteredTwoActivity.this, "上传成功");
                            String icon_id = null;
                            try {
                                icon_id = arg2.getString("key");
                                switch (id) {
                                    case 1:
                                        shop_icon = icon_id;
                                        break;
                                    default:
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            dismissLoading();
                            Tools.showToast(RegisteredTwoActivity.this, "上传失败");
                        }
                    }

                }, null);
    }

    // 商户注册
    private void register() {
        Map<String, String> map = new HashMap<>();
        map.put("admin_phone", storeRegisterBean.getAdmin_phone());
        map.put("admin_account", storeRegisterBean.getAdmin_account());
        map.put("admin_pass", storeRegisterBean.getAdmin_pass());
        map.put("confirm_pass", storeRegisterBean.getConfirm_pass());
        map.put("msg_code", storeRegisterBean.getMsg_code());

        map.put("shop_name", et_store_name.getText().toString());
        map.put("partner_name", et_name.getText().toString());
        map.put("city_name", mAddsBean.getCity());
        map.put("shop_address", tv_store_adds.getText().toString() + et_store_adds.getText().toString());
        map.put("lat", mAddsBean.getLat());
        map.put("lng", mAddsBean.getLng());
        map.put("shop_icon", shop_icon);
        map.put("agency_province", province);
        map.put("agency_area", district);

        mQueue.add(ParamTools.packParam(Constants.register, this, this, map));
        loading();
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.getInt("status");
            String msg = json.getString("msg");
            if (status == 0) {
                if (url.contains(Constants.register)) {
                    mSavePreferencesData.putStringData("json", response);
                    mSavePreferencesData.putStringData("auth_token", json.optString("auth_token"));
                    registerInfoService();
                    Tools.jump(RegisteredTwoActivity.this, RegisteredSuccessfullyActivity.class, false);
                } else if (url.contains(Constants.getuploadtoken)) {
                    upload_token = json.optString("result");
                }
            } else {
                Tools.showToast(getApplicationContext(), msg);
            }
        } catch (JSONException e) {
            Tools.showToast(getApplicationContext(), "解析数据错误");
        }
    }

    /* 注册推送服务 */
    private void registerInfoService() {
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        // 用QID 绑定别名
        pushService.bindAccount("efengshepos_" + storeRegisterBean.getAdmin_phone(), new CommonCallback() {

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

    // 通过经纬度获取地址
    private void getAddress(String stringlat, String stringlng) {
        double lat = Double.parseDouble(stringlat);
        double lnt = Double.parseDouble(stringlng);
        LatLng latLng = new LatLng(lat, lnt);
        // 新建编码查询对象
        GeoCoder geocode = GeoCoder.newInstance();
        // 新建查询对象要查询的条件
        ReverseGeoCodeOption GeoOption = new ReverseGeoCodeOption().location(latLng);
        // 发起地理编码请求
        geocode.reverseGeoCode(GeoOption);

        // LatLngBounds bounds = new
        // LatLngBounds.Builder().include(latLng).include(latLng).build();
        // mPoiSearch.searchInBound(new
        // PoiBoundSearchOption().bound(bounds).keyword("小区").pageCapacity(5));
        geocode.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
                tv_store_adds.setText(arg0.getAddress());
                //获取省、区县
                ReverseGeoCodeResult.AddressComponent addressDetail = arg0.getAddressDetail();
                province = addressDetail.province;
                district = addressDetail.district;
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult arg0) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 3) {
            //返回基本信息
            mAddsBean = (AddsBean) data.getSerializableExtra("addsbean");
            //通过经纬度获取省、地区的数据
            if (mAddsBean != null) {
                getAddress(mAddsBean.getLat(), mAddsBean.getLng());
            }
        } else if (resultCode == RESULT_OK) {
            List<String> resultList = data.getExtras().getStringArrayList("select_result");
            String bitmapPath = resultList.get(0);
            Bitmap bitmap = ImageUtil.decodeImage(bitmapPath);

            upLoadPhoto(bitmapPath, 1);
            switch (requestCode) {
                case 1:
                    iw_store.setVisibility(View.VISIBLE);
                    iw_store.setBackgroundDrawable(bitmap);
                    iv_store.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
