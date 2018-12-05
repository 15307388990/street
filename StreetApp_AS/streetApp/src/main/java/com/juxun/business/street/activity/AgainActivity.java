package com.juxun.business.street.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.juxun.business.street.bean.AddsBean;
import com.juxun.business.street.bean.ImageWithDelete;
import com.juxun.business.street.bean.ImageWithDelete.ICallBack;
import com.juxun.business.street.bean.MallSetBean;
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

import static com.yl.ming.efengshe.R.id.iv_positive;

/**
 * @author 重新提交审核
 */

public class AgainActivity extends BaseActivity implements TextWatcher, OnClickListener {
    @ViewInject(R.id.et_store_name)
    private EditText et_store_name;// 店铺名称
    @ViewInject(R.id.et_store_adds)
    private EditText et_store_adds;// 详细地址
    @ViewInject(R.id.et_name)
    private EditText et_name;// 联系人

    @ViewInject(R.id.iv_store)
    private ImageView iv_store;// 店面门头照
    @ViewInject(R.id.iw_store)
    private ImageWithDelete iw_store;// 店面门头照 iw

    @ViewInject(R.id.tv_store_adds)
    private TextView tv_store_adds;// 地址

    @ViewInject(R.id.btn_next)
    private Button btn_next;// 下一步

    @ViewInject(R.id.tv_code)
    private TextView tv_code;// 获取验证码

    private AddsBean mAddsBean;

    private String positive_icon = "";// 身份证正面
    private String reverse_icon = "";// 身份证反面
    private String hand_idnumber_icon = "";// 手拿身份证icon
    private String shop_icon = null;// shop_icon
    private String business_licence_icon = "";// 营业执照icon
    private String upload_token;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();
    private Intent intent;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_two);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        btn_next.setEnabled(initBtn());
        initTitle();
        title.setText("提交资料");
        getUploadToken();
        initView();
        getMallSet();
    }

    private void getUploadToken() {
        Map<String, String> map = new HashMap<String, String>();
        mQueue.add(ParamTools.packParam(Constants.getuploadtoken, this, this, map));
    }

    // 重新审核获取相应信息
    private void getMallSet() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.getMallSet, this, this, map));
    }

    private void initView() {
        et_store_name.addTextChangedListener(this);
        et_store_adds.addTextChangedListener(this);
        et_name.addTextChangedListener(this);
        iw_store.setonClick(new ICallBack() {

            @Override
            public void onClickDeleteButton(int position) {
                iw_store.setVisibility(View.GONE);
                iv_store.setVisibility(View.VISIBLE);
                shop_icon = null;

            }
        });
        iv_store.setOnClickListener(this);
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.btn_next, R.id.tv_store_adds})
    public void clickMethod(View v) {
        if (v.getId() == R.id.tv_store_adds) {
            // 选择地址
            Intent intent = new Intent(AgainActivity.this, LocationMapActivity.class);
            startActivityForResult(intent, 1);
        } else if (v.getId() == R.id.btn_next) {
            if (shop_icon != null) {
                reExamination();
            } else {
                Tools.showToast(AgainActivity.this, "必填图片没有选择");
            }
        }
    }

    // 上传图片得到字符串
    private void upLoadPhoto(String filePaths, final int id) {
        loading();
        UploadManager uploadManager = UploadUtil.getUploadManager();
        String key = null;
        key = Tools.getRandomFileName();
        // 这里压缩一下图片 不然上传过慢导致加载缓慢 by yejia
        uploadManager.put(ImageCompress.BitmapToBytes2(filePaths), key, upload_token, // filePaths.get(i)
                new UpCompletionHandler() {

                    @Override
                    public void complete(String arg0, ResponseInfo arg1, JSONObject arg2) {

                        // TODO Auto-generated method stub
                        if (arg1.statusCode == 200) {
                            dismissLoading();
                            Tools.showToast(AgainActivity.this, "上传成功");
                            String icon_id = null;
                            try {
                                icon_id = arg2.getString("key");
                                switch (id) {
                                    case 1:
                                        hand_idnumber_icon = icon_id;
                                        break;
                                    case 2:
                                        positive_icon = icon_id;
                                        break;
                                    case 3:
                                        reverse_icon = icon_id;
                                        break;
                                    case 4:
                                        shop_icon = icon_id;
                                        break;
                                    case 5:
                                        business_licence_icon = icon_id;
                                        break;
                                    default:
                                        break;
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            dismissLoading();
                            Tools.showToast(AgainActivity.this, "上传失败");
                        }
                    }

                }, null);
    }

    /**
     * 重新提交审核
     */
    private void reExamination() {
        // agency_id
        // shop_name
        // shop_area_name
        // shop_address
        // id_number
        // id_number_positiveandnegative_icon
        // hand_idnumber_icon
        // business_licence_icon
        // lat
        // lng
        // shop_icon
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("shop_name", et_store_name.getText().toString());
        map.put("shop_area_name", tv_store_adds.getText().toString());
        map.put("shop_address", et_store_adds.getText().toString());
        map.put("id_number_positiveandnegative_icon", positive_icon + "," + reverse_icon);
        map.put("hand_idnumber_icon", hand_idnumber_icon);
        if (business_licence_icon != null) {
            map.put("business_licence_icon", business_licence_icon);
        }
        map.put("lat", mAddsBean.getLat());
        map.put("lng", mAddsBean.getLng());
        map.put("shop_icon", shop_icon);
        map.put("partner_name", et_name.getText().toString());
        if (mAddsBean.getCity() != null) {
            map.put("city_name", mAddsBean.getCity());
        }
        mQueue.add(ParamTools.packParam(Constants.reExamination, this, this, map));
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
                if (url.contains(Constants.reExamination)) {
                    Tools.jump(AgainActivity.this, RegisteredSuccessfullyActivity.class, false);
                } else if (url.contains(Constants.getuploadtoken)) {
                    upload_token = json.getString("token");
                } else if (url.contains(Constants.getMallSet)) {
                    MallSetBean mallSetBean = JSON.parseObject(response, MallSetBean.class);
                    initDate(mallSetBean);
                }
            } else if (status == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.jump(this, LoginActivity.class, false);
                Tools.showToast(this, "登录过期请重新登录");
                Tools.acts.clear();
            } else {
                Tools.showToast(getApplicationContext(), msg);
            }
        } catch (JSONException e) {
            // TODO: handle exception
            Tools.showToast(getApplicationContext(), "解析数据错误");
        }

    }

    // 初始化数据
    private void initDate(MallSetBean mallSetBean) {
        et_store_name.setText(mallSetBean.getShop_name());
        et_store_adds.setText(mallSetBean.getShop_address());
        tv_store_adds.setText(mallSetBean.getShop_area_name());
        et_name.setText(mallSetBean.getPartner_name());
        if (mallSetBean.getShop_icon() != null) {
            imageLoader.displayImage(Constants.imageUrl + mallSetBean.getShop_icon(), iv_store, options);
            shop_icon = mallSetBean.getShop_icon();
        }
        mAddsBean = new AddsBean();
        mAddsBean.setLat(mallSetBean.getLat());
        mAddsBean.setLng(mallSetBean.getLng());
        if (mallSetBean.getLat() != null && mallSetBean.getLng() != null) {
            initAdds(mAddsBean.getLat(), mAddsBean.getLng());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 3) {
            mAddsBean = (AddsBean) data.getSerializableExtra("addsbean");
            if (mAddsBean != null) {
                initAdds(mAddsBean.getLat(), mAddsBean.getLng());

            }
        } else if (resultCode == RESULT_OK) {
            List<String> resultList = data.getExtras().getStringArrayList("select_result");
            Bitmap bitmap = ImageUtil.decodeImage(resultList.get(0));
            upLoadPhoto(resultList.get(0), requestCode);
            switch (requestCode) {
                case 4:
                    iw_store.setVisibility(View.VISIBLE);
                    iw_store.setBackgroundDrawable(bitmap);
                    iv_store.setVisibility(View.GONE);
                    break;

            }

        }
    }

    // 通过经纬度获取地址
    private void initAdds(String stringlat, String stringlng) {
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
                btn_next.setEnabled(initBtn());
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private boolean initBtn() {
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        btn_next.setEnabled(initBtn());
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        intent = new Intent(AgainActivity.this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        switch (v.getId()) {
            case iv_positive:
                checkoutCameraPermissions();
                checkoutWritePermissions();
                type = "iv_positive";
//			startActivityForResult(intent, 2);// 身份证正面
                break;
            case R.id.iv_store:
                checkoutCameraPermissions();
                checkoutWritePermissions();
                type = "iv_store";
//			startActivityForResult(intent, 4);// 店面门头照
                break;
            default:
                break;
        }
    }

    @Override
    protected void gotThePermission() {
        if (count > 1) {
            if (type.equals(iv_positive)) {
                startActivityForResult(intent, 2);
            } else {
                startActivityForResult(intent, 4);
            }
        }
    }

    @PermissionGrant(REQUEST_CODE_TAKE_PHOTE)
    public void requestCameraSuccess() {
        count = count + 1;
        if (count > 1) {
            if (type.equals(iv_positive)) {
                startActivityForResult(intent, 2);
            } else {
                startActivityForResult(intent, 4);
            }
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
            if (type.equals(iv_positive)) {
                startActivityForResult(intent, 2);
            } else {
                startActivityForResult(intent, 4);
            }
        }
    }

    @PermissionDenied(REQUEST_CODE_WRITE)
    public void requestWriteFailed() {
        Toast.makeText(this, "请打开读写手机存储权限", Toast.LENGTH_SHORT).show();
    }

}
