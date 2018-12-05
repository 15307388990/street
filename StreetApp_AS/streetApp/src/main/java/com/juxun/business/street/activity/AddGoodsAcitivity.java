/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.bean.ChannelBean;
import com.juxun.business.street.bean.ImageWithDelete;
import com.juxun.business.street.bean.ImageWithDelete.ICallBack;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageCompress;
import com.juxun.business.street.util.ImageUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.TimerDateUtil;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.util.UploadUtil;
import com.juxun.business.street.util.WheelViewDialog;
import com.juxun.business.street.widget.SelectBirthdayPop;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.yl.ming.efengshe.R;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 类名称：AddGoodsAcitivity 类描述：添加商品 首页 创建人：罗富贵 创建时间：2016年9月23日
 */
public class AddGoodsAcitivity extends BaseActivity implements ICallBack, WheelViewDialog.onConfirmListener {
    // 图片组
    @ViewInject(R.id.ll_photo_group)
    private LinearLayout ll_photo_group;
    @ViewInject(R.id.ll_photo_group2)
    private LinearLayout ll_photo_group2;
    @ViewInject(R.id.tv_classification)
    private TextView tv_classification;
    // 商品编码
    @ViewInject(R.id.tv_coding)
    private TextView tv_coding;
    // 商品名称
    @ViewInject(R.id.et_goodsname)
    private EditText et_goodsname;
    // 商品单位
    @ViewInject(R.id.et_unit)
    private EditText et_unit;
    // 商品摘要
    @ViewInject(R.id.et_commodity_info)
    private EditText et_commodity_info;

    // 商品成本价
    @ViewInject(R.id.et_costprice)
    private EditText et_costprice;
    // 商品市场价
    @ViewInject(R.id.et_marketprice)
    private EditText et_marketprice;
    // 商品库存
    @ViewInject(R.id.et_inventory)
    private EditText et_inventory;
    // 提交按钮
    @ViewInject(R.id.btn_query)
    private Button btn_query;
    // 是否自动上架
    @ViewInject(R.id.checkbox)
    private CheckBox checkbox;
    // 返回
    @ViewInject(R.id.button_back)
    private ImageView button_back;
    private List<ImageWithDelete> images = null;
    private List<Bitmap> bitmapInfos = null;
    private List<String> resultList = null;
    private List<String> icon_ids = null;
    private ImageView iv_add_photo;
    private int maxSelectedCount = 8;
    private List<String> filePaths = null;
    private String upload_token;
    private SelectBirthdayPop birth;
    private String ids;
    TimerDateUtil timerDate = new TimerDateUtil();
    // 频道ID
    private String code;
    private List<View> blank_views = null;
    /**
     * 当前一级频道ID
     */
    protected int first_level_channel_id;
    /**
     * 当前二级频道ID
     */
    protected int second_level_channel_id;
    private Intent intent;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgoods);
        ViewUtils.inject(this);
        filePaths = new ArrayList<String>();
        icon_ids = new ArrayList<String>();
        bitmapInfos = new ArrayList<Bitmap>();
        images = new ArrayList<ImageWithDelete>();
        resultList = new ArrayList<String>();
        blank_views = new ArrayList<View>();
        initView();
    }

    @Override
    protected void gotThePermission() {
        if (count > 1) {
            startActivityForResult(intent, 0);
        }
    }

    @PermissionGrant(REQUEST_CODE_TAKE_PHOTE)
    public void requestCameraSuccess() {
        count = count + 1;
        if (count > 1) {
            startActivityForResult(intent, 0);
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
            startActivityForResult(intent, 0);  //跳转相册相片选择
        }
    }

    @PermissionDenied(REQUEST_CODE_WRITE)
    public void requestWriteFailed() {
        Toast.makeText(this, "请打开读写手机存储权限", Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        code = getIntent().getStringExtra("code");
        iv_add_photo = new ImageView(this);
        iv_add_photo.setBackgroundResource(R.drawable.add_btn_addpic);
        iv_add_photo.setScaleType(ScaleType.FIT_XY);
        iv_add_photo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    intent = new Intent(AddGoodsAcitivity.this, MultiImageSelectorActivity.class);
                    // 是否显示拍摄图片
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                    // 最大可选择图片数量
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxSelectedCount - images.size());
                    startActivityForResult(intent, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        initPhotos();
        // 判断 类是否为空 来判断是否为有条码还是无条码商品
        if (code != null && !code.isEmpty()) {
            tv_coding.setText(code);
        } else {
            tv_coding.setText("无条码商品");
        }
        button_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_costprice.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        et_costprice.setText(s);
                        et_costprice.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_costprice.setText(s);
                    et_costprice.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_costprice.setText(s.subSequence(0, 1));
                        et_costprice.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        et_marketprice.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        et_marketprice.setText(s);
                        et_marketprice.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_marketprice.setText(s);
                    et_marketprice.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_marketprice.setText(s.subSequence(0, 1));
                        et_marketprice.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

    }

    private Boolean isInput() {
        if (first_level_channel_id != 0) {
            if (!TextUtils.isEmpty(et_goodsname.getText())) {
                if (!TextUtils.isEmpty(et_costprice.getText())) {
                    if (!TextUtils.isEmpty(et_marketprice.getText())) {
                        if (!TextUtils.isEmpty(et_inventory.getText())) {
                            if (!TextUtils.isEmpty(et_unit.getText())) {
                                if (filePaths.size() == 0) {
                                    Tools.showToast(this, "请至少选择一张图片");
                                } else {
                                    return true;
                                }
                            } else {
                                Tools.showToast(this, "请输入商品单位");
                            }
                        } else {
                            Tools.showToast(this, "请输入商品库存");
                        }
                    } else {
                        Tools.showToast(this, "请输入商品市场价");
                    }
                } else {
                    Tools.showToast(this, "请输入商品成本价");
                }
            } else {
                Tools.showToast(this, "请输入商品名称");
            }
        } else {
            Tools.showToast(this, "未选择商品频道");
        }
        return false;

    }

    private boolean canClicked = true;

    /**
     * 单击事件
     */
    @OnClick({R.id.btn_query, R.id.tv_classification})
    public void clickMethod(View v) {
        if (v.getId() == R.id.btn_query) {

            if (isInput()) {
                // if (!Tools.isFastDoubleClick()) {
                if (canClicked) {
                    loading();
                    canClicked = false;
                    upLoadPhoto(filePaths);
                } else {
                    // Toast.makeText(this, "正在提交中...",
                    // Toast.LENGTH_SHORT).show();
                }
            }
        } else if (v.getId() == R.id.tv_classification) {
            initChannel();

        }
    }

    private void initChannel() {
        Map<String, String> map = new HashMap<String, String>();
        // agency_id int 机构id
        // auth_token String 登陆令牌
        // admin_id int 管理员id
        map.put("auth_token", partnerBean.getAuth_token());
        // map.put("hasAllChannel", "1");
        mQueue.add(ParamTools.packParam(Constants.getOpenChannels, this, this, map));
        loading();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getUploadToken();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            resultList = data.getExtras().getStringArrayList("select_result");
            for (int k = 0; k < resultList.size(); k++) // 将数据重新整合
            {
                Bitmap bitmap = ImageUtil.decodeImage(resultList.get(k));
                bitmapInfos.add(bitmap);
                filePaths.add(resultList.get(k));
            }
            initPhotos();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private Boolean checkAddphotoIsAble() {
        if (bitmapInfos.size() < 8)
            return true;
        else
            return false;

    }

    /**
     * 这里动态的去添加图片
     */
    private void initPhotos() {
        images = new ArrayList<ImageWithDelete>();
        ll_photo_group.removeAllViewsInLayout();
        ll_photo_group2.removeAllViewsInLayout();
        int photo_width = Tools.dip2px(this, 65);
        int line_width = Tools.dip2px(this, 20);
        for (int i = 0; i < bitmapInfos.size(); i++) {
            if (i < 8) {
                ImageWithDelete iv_photo = new ImageWithDelete(this);
                iv_photo.setonClick(this);
                iv_photo.getIv_photo().setScaleType(ScaleType.FIT_XY);
                LayoutParams params = new LayoutParams(photo_width, photo_width);
                View blank_view = new View(this);
                blank_view.setLayoutParams(new LayoutParams(line_width, line_width));
                iv_photo.setLayoutParams(params);
                iv_photo.getIv_photo().setLayoutParams(new RelativeLayout.LayoutParams(photo_width, photo_width));
                if (i < 4) {
                    ll_photo_group.addView(iv_photo);
                    ll_photo_group.addView(blank_view);
                } else {
                    ll_photo_group2.addView(iv_photo);
                    ll_photo_group2.addView(blank_view);
                }
                images.add(i, iv_photo);
                images.get(i).setBackgroundDrawable(bitmapInfos.get(i));
                blank_views.add(i, blank_view);
                iv_photo.setPosition(i);
            }

        }

        if (bitmapInfos.size() < 4) {
            ll_photo_group.addView(iv_add_photo);
            ll_photo_group2.setVisibility(View.GONE);
        } else {
            ll_photo_group2.addView(iv_add_photo);
            ll_photo_group2.setVisibility(View.VISIBLE);
        }
        iv_add_photo.setVisibility(checkAddphotoIsAble() ? View.VISIBLE : View.GONE);

    }

    private void setPhotosPos() {
        for (int i = 0; i < images.size(); i++) {

            images.get(i).setPosition(i);
        }

    }

    private void getUploadToken() {
        Map<String, String> map = new HashMap<String, String>();
        mQueue.add(ParamTools.packParam(Constants.getuploadtoken, this, this, map));
    }

    private void upLoadPhoto(List<String> filePaths) {
        UploadManager uploadManager = UploadUtil.getUploadManager();
        String key = null;
        for (int i = 0; i < filePaths.size(); i++) {

            key = Tools.getRandomFileName();
            icon_ids.add(key);
            // 这里压缩一下图片 不然上传过慢导致加载缓慢 by yejia
            uploadManager.put(ImageCompress.BitmapToBytes2(filePaths.get(i)), key, upload_token, // filePaths.get(i)
                    new UpCompletionHandler() {

                        @Override
                        public void complete(String arg0, ResponseInfo arg1, JSONObject arg2) {
                            // TODO Auto-generated method stub
                            Log.d("yj", "onComplete");
                            if (arg1.statusCode == 200) {
                                String icon_id = null;
                                try {
                                    icon_id = arg2.getString("key");
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }

                    }, null);
        }

        addOrEditCommodity();

    }

    // 如多规格商品生成后自动变为单规格
    // inventoryId与commodityId只在编辑时才传，优先传inventoryId，没有传commodityId
    // inventoryId int 库存id
    // commodityId int 商品id
    // agencyId int 机构id
    // channel_id int 频道id
    // code String 编码
    // commodity_cost double 商品成本
    // cover String
    // 图片集合（中间使用逗号分隔）073db8f5-eb06-47e8-8228-959144af98ce.jpg,5a18725b-2d37-4e40-940e-2cb7429b7681.jpg?imageMogr2/crop/!549x549a213a0
    // commodity_info String 商品摘要
    // commodity_inventory int 商品库存
    // commodity_name Strign 商品名
    // commodity_price double 商品价格
    // expirationTime String 过期时间 ‘yyyy-MM-dd’
    // unit_name String 单位
    private void addOrEditCommodity() {
        Map<String, String> map = new HashMap<String, String>();

        // 新增有条码商品
        map.put("commodity_name", et_goodsname.getText().toString());// 商品名称
        map.put("first_level_channel_id", first_level_channel_id + "");// 一级频道
        map.put("second_level_channel_id", second_level_channel_id + "");// 二级频道
        map.put("commodity_cost", Tools.getYuanFen(et_costprice.getText().toString()) + "");// 商品成本
        map.put("price_low", Tools.getYuanFen(et_marketprice.getText().toString()) + "");// 最低价
        // 分
        map.put("price_high", Tools.getYuanFen(et_marketprice.getText().toString()) + "");// 最高价
        if (code != null && !code.isEmpty()) {
            map.put("code_type", "0");// 0有条码1无条码 商品类型
            map.put("code", tv_coding.getText().toString());// 商品条码
        } else {
            map.put("code_type", "1");// 0有条码1无条码 商品类型
        }

        map.put("commodity_icon", Tools.listToString(icon_ids));// 商品图片集，以逗号隔开
        map.put("commodity_desc", et_commodity_info.getText().toString());// 描述
        map.put("shelving_mode", checkbox.isChecked() ? "0" : "1");// 上架模式默认：0审核通过自动上架1；手动上架
        map.put("unit_name", et_unit.getText().toString());
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("commodity_id", "0");// 商品id 需要新增的时候请勿传
        // 或者传0，修改的时候传真实的商品
        map.put("commodity_inventory", et_inventory.getText().toString());// 商品库存
        map.put("template_id", "0");// 0为无模板，有模板商品请传模板名称
        mQueue.add(ParamTools.packParam(Constants.addOrEditCommodity, this, this, map));

    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            String msg = json.optString("msg");
            if (stauts == 0) {
                // dismissLoading();
                if (url.contains(Constants.getuploadtoken)) {
                    upload_token = json.getString("result");
                } else if (url.contains(Constants.getOpenChannels)) {
                    String liString = json.getString("result");
                    List<ChannelBean> group = JSON.parseArray(liString, ChannelBean.class);
                    WheelViewDialog dialog = new WheelViewDialog(AddGoodsAcitivity.this, group,
                            group.get(0).getChildChannelList(), AddGoodsAcitivity.this);
                    dialog.showAtLocation(tv_classification, Gravity.BOTTOM, 0, 0);
                } else if (url.contains(Constants.addOrEditCommodity)) {
                    Tools.showToast(getApplicationContext(), "添加成功");
                    this.finish();
                }

            } else {
                Tools.showToast(this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    @Override
    public void onClickDeleteButton(int position) {
        images.get(position).setVisibility(View.GONE);
        filePaths.remove(position);
        bitmapInfos.remove(position);
        images.remove(position);
        blank_views.remove(position);
        initPhotos();

    }

    @Override
    public void onConfirm(String areaName, int first_level_channel_id, int second_level_channel_id) {
        tv_classification.setText(areaName);
        this.first_level_channel_id = first_level_channel_id;
        this.second_level_channel_id = second_level_channel_id;

    }

}
