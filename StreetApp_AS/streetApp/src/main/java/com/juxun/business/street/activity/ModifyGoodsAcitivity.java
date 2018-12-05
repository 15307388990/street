/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.juxun.business.street.bean.MallNewTemplateBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageCompress;
import com.juxun.business.street.util.ImageUtil;
import com.juxun.business.street.util.ParamTools;
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
 * 类名称：ModifyGoodsAcitivity 类描述：修改商品信息 创建人：罗富贵 创建时间：2017年9月8日
 */
public class ModifyGoodsAcitivity extends BaseActivity implements ICallBack, WheelViewDialog.onConfirmListener {
    @ViewInject(R.id.textview_title)
    private TextView textview_title;
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
    private List<View> blank_views = null;
    private List<Bitmap> bitmapInfos = null;
    private List<String> resultList = null;
    private List<String> icon_ids = null;
    private ImageView iv_add_photo;
    private int maxSelectedCount = 8;
    private List<String> filePaths = null;
    private String upload_token;
    private SelectBirthdayPop birth;
    String icon_id = null;
    private MallNewTemplateBean mallNewTemplateBean;
    private int commodityId;// 商品id 如果是修改信息进入
    /**
     * 当前一级频道ID
     */
    protected int first_level_channel_id;
    /**
     * 当前二级频道ID
     */
    protected int second_level_channel_id;
    List<ChannelBean> group;// 频道列表

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            bitmapInfos.add((Bitmap) msg.obj);
            initPhotos();
        }
    };
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifygoods);
        ViewUtils.inject(this);
        Tools.acts.add(this);

        filePaths = new ArrayList<>();
        icon_ids = new ArrayList<>();
        bitmapInfos = new ArrayList<>();
        images = new ArrayList<>();
        resultList = new ArrayList<>();
        blank_views = new ArrayList<>();
        initView();
    }

    @Override
    protected void gotThePermission() {
        if (count > 1) {
            startActivityForResult(intent, 2);
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
            startActivityForResult(intent, 0);
        }
    }

    @PermissionDenied(REQUEST_CODE_WRITE)
    public void requestWriteFailed() {
        Toast.makeText(this, "请打开读写手机存储权限", Toast.LENGTH_SHORT).show();
    }

    /**
     * 几种 进入该页面的情况
     *
     * @author 1:扫码模版进入
     * @author 2:修改信息进入
     * @author 3:新增商品 （带条形码进入） （无条形码进入）
     */
    private void initView() {
        mallNewTemplateBean = (MallNewTemplateBean) getIntent().getSerializableExtra("mallNewTemplateBean");
        commodityId = getIntent().getIntExtra("commodity_id", 0);
        if (mallNewTemplateBean != null) {
            textview_title.setText("添加商品");
            initChannel();
        } else if (commodityId != 0) {
            textview_title.setText("修改商品");
            getCommodityInfo();
        }

        iv_add_photo = new ImageView(this);
        iv_add_photo.setBackgroundResource(R.drawable.add_btn_addpic);
        iv_add_photo.setScaleType(ScaleType.FIT_XY);
        iv_add_photo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                try {
                    Intent intent = new Intent(ModifyGoodsAcitivity.this, MultiImageSelectorActivity.class);
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

            }

            @Override
            public void afterTextChanged(Editable s) {

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

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initDate() {
        tv_coding.setText(mallNewTemplateBean.getCode());
//        if (mallNewTemplateBean.getSecond_level_channel_name() != null) {
//            tv_classification.setText(mallNewTemplateBean.getFirst_level_channel_name() + ">>" + mallNewTemplateBean.getSecond_level_channel_name());
//        } else {
//            if (mallNewTemplateBean.getSecond_level_channel_id() != -1) {
//                tv_classification.setText(group.get(mallNewTemplateBean.getFirst_level_channel_id()).getChannel_name());
//            } else {
//                tv_classification.setText(group.get(mallNewTemplateBean.getFirst_level_channel_id()).getChannel_name() + ">>" + group.get(mallNewTemplateBean.getSecond_level_channel_id()).getChannel_name());
//            }
//
//        }
        for (ChannelBean channelBean : group) {
            if (channelBean.getId() == mallNewTemplateBean.getFirst_level_channel_id()) {
                if (channelBean.getChildChannelList().size()>0) {
                    for (ChannelBean channelBean1 : channelBean.getChildChannelList()) {
                        if (channelBean1.getId() == mallNewTemplateBean.getSecond_level_channel_id()) {
                            tv_classification.setText(channelBean.getChannel_name() + ">>" + channelBean1.getChannel_name());
                        }
                    }
                } else {
                    tv_classification.setText(channelBean.getChannel_name());
                }
            }
        }

        et_goodsname.setText(mallNewTemplateBean.getCommodity_name());
        et_unit.setText(mallNewTemplateBean.getUnit_name());
        et_commodity_info.setText(mallNewTemplateBean.getCommodity_desc());
        et_costprice.setText(Tools.getFenYuan(mallNewTemplateBean.getCommodity_cost()) + "");
        et_marketprice.setText(Tools.getFenYuan(mallNewTemplateBean.getPrice_low()) + "");
        // 库存 单位
        et_unit.setText(mallNewTemplateBean.getUnit_name());
        et_inventory.setText(mallNewTemplateBean.getCommodity_inventory());

        // 图片处理
        String[] cover = mallNewTemplateBean.getCommodity_icon().split(",");    //获取的上个页面的传递过来的对象icon
        for (int i = 0; i < cover.length; i++) {
            resultList.add(cover[i]);
        }
        for (int k = 0; k < resultList.size(); k++) // 将数据重新整合
        {
            final String url = Constants.imageUrl + resultList.get(k);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    Bitmap bitmap = ImageUtil.getbitmap(url);
                    Message msg = new Message();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);

                }
            }).start();
            icon_ids.add(resultList.get(k));
        }
        this.first_level_channel_id = mallNewTemplateBean.getFirst_level_channel_id();
        this.second_level_channel_id = mallNewTemplateBean.getSecond_level_channel_id();
    }

    /**
     * 这里动态的去添加图片
     */
    private void initPhotos() {
        images = new ArrayList<>();
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

    /**
     * 获取商品详情
     */
    private void getCommodityInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("commodity_id", commodityId + "");
        mQueue.add(ParamTools.packParam(Constants.getCommodityInfo, this, this, map));
        loading();
    }

    private Boolean isInput() {
        if (!TextUtils.isEmpty(et_goodsname.getText())) {
            if (!TextUtils.isEmpty(et_costprice.getText())) {
                if (!TextUtils.isEmpty(et_marketprice.getText())) {
                    if (!TextUtils.isEmpty(et_inventory.getText())) {
                        if (!TextUtils.isEmpty(et_unit.getText())) {
                            if (images.size() == 0) {
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
        return false;

    }

    /**
     * 单击事件
     */
    @OnClick({R.id.btn_query, R.id.tv_classification})
    public void clickMethod(View v) {
        if (v.getId() == R.id.btn_query) {
            if (isInput()) {
//                 if (!TextUtils.isEmpty(et_inventory.getText())) {
//                 int inven =
//                 Integer.valueOf(et_inventory.getText().toString());
//                 if (inven < goodDetails.getCommodity_inventory()) {
//                 Tools.showToast(getApplicationContext(), "修改库存不能小于现有库存");
//                 return;
//                 }
//                 }
                if (!Tools.isFastDoubleClick()) {
                    upLoadPhoto(filePaths);
                } else {
                    Toast.makeText(this, "操作太频繁", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (v.getId() == R.id.tv_classification) {
            if (group != null) {
                WheelViewDialog dialog = new WheelViewDialog(ModifyGoodsAcitivity.this, group,
                        group.get(0).getChildChannelList(), ModifyGoodsAcitivity.this);
                dialog.showAtLocation(tv_classification, Gravity.BOTTOM, 0, 0);
            }
        }
    }

    private void initChannel() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.getOpenChannels, this, this, map));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUploadToken();

    }

    private Boolean checkAddphotoIsAble() {
        if (bitmapInfos.size() < 8)
            return true;
        else
            return false;
    }

    private void setPhotosPos() {
        for (int i = 0; i < images.size(); i++) {
            images.get(i).setPosition(i);
        }

    }

    private void getUploadToken() {
        Map<String, String> map = new HashMap<>();
        mQueue.add(ParamTools.packParam(Constants.getuploadtoken, this, this, map));
    }

    private void upLoadPhoto(final List<String> filePaths) {
        UploadManager uploadManager = UploadUtil.getUploadManager();
        String key = null;
        loading();
        if (filePaths.size() > 0) {
            for (int i = 0; i < filePaths.size(); i++) {
                final int y = i;
                key = Tools.getRandomFileName();
                icon_ids.add(key);
                // 这里压缩一下图片 不然上传过慢导致加载缓慢 by yejia
                uploadManager.put(ImageCompress.BitmapToBytes2(filePaths.get(i)), key, upload_token, // filePaths.get(i)
                        new UpCompletionHandler() {

                            @Override
                            public void complete(String arg0, ResponseInfo arg1, JSONObject arg2) {
                                Log.d("url", arg0 + ":::" + arg1.toString() + ":::" + arg2.toString());
                                if (arg1.statusCode == 200) {
                                    try {
                                        icon_id = icon_id + arg2.getString("key");
                                        if (y == (filePaths.size() - 1)) {
                                            if (textview_title.getText().equals("添加商品")) {
                                                addOrEditCommodity();
                                            } else {
                                                update();
                                            }

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Tools.showToast(ModifyGoodsAcitivity.this, "图片上传失败" + e.toString());
                                    }
                                } else {
                                    Tools.showToast(ModifyGoodsAcitivity.this, "图片上传失败");
                                }
                            }

                        }, null);
            }
        } else {
            if (textview_title.getText().equals("添加商品")) {
                addOrEditCommodity();
            } else {
                update();
            }
        }
    }

    private void addOrEditCommodity() {
        Map<String, String> map = new HashMap<>();
        //基本信息
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("template_id", mallNewTemplateBean.getId() + "");// 0为无模板，有模板商品请传模板名称
        map.put("commodity_inventory", et_inventory.getText().toString());// 商品库存
        map.put("shelving_mode", checkbox.isChecked() ? "0" : "1");// 上架模式默认：0审核通过自动上架1；手动上架
        map.put("commodity_name", et_goodsname.getText().toString());// 商品名称
        map.put("first_level_channel_id", first_level_channel_id + "");// 一级频道
        map.put("second_level_channel_id", second_level_channel_id + "");// 二级频道
        map.put("commodity_cost", Tools.getYuanFen(et_costprice.getText().toString()) + "");// 商品成本
        map.put("price_low", Tools.getYuanFen(et_marketprice.getText().toString()) + "");// 最低价
        map.put("price_high", Tools.getYuanFen(et_marketprice.getText().toString()) + "");// 最高价
        // 判断是否有条形码
        if (mallNewTemplateBean.getCode() == null || mallNewTemplateBean.getCode().isEmpty()) {
            map.put("code_type", "1");// 0有条码1无条码 商品类型
        } else {
            map.put("code_type", "0");// 0有条码1无条码 商品类型
            map.put("code", mallNewTemplateBean.getCode());// 商品条码
        }
        map.put("commodity_icon", Tools.listToString(icon_ids));// 商品图片集，以逗号隔开
        map.put("commodity_desc", et_commodity_info.getText().toString());// 描述
        map.put("unit_name", et_unit.getText().toString());
        mQueue.add(ParamTools.packParam(Constants.addOrEditCommodity, this, this, map));
    }

    /**
     * 修改商品
     */
    private void update() {
        Map<String, String> map = new HashMap<>();
        //基本信息
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("id", mallNewTemplateBean.getId() + "");// 0为无模板，有模板商品请传模板名称
        map.put("commodity_inventory", et_inventory.getText().toString());// 商品库存
        map.put("shelving_mode", checkbox.isChecked() ? "0" : "1");// 上架模式默认：0审核通过自动上架1；手动上架
        map.put("commodity_name", et_goodsname.getText().toString());// 商品名称
        map.put("first_level_channel_id", first_level_channel_id + "");// 一级频道
        map.put("second_level_channel_id", second_level_channel_id + "");// 二级频道
        map.put("commodity_cost", Tools.getYuanFen(et_costprice.getText().toString()) + "");// 商品成本
        map.put("price_low", Tools.getYuanFen(et_marketprice.getText().toString()) + "");// 最低价
        map.put("price_high", Tools.getYuanFen(et_marketprice.getText().toString()) + "");// 最高价
        // 判断是否有条形码
        if (mallNewTemplateBean.getCode() == null || mallNewTemplateBean.getCode().isEmpty()) {
            map.put("code_type", "1");// 0有条码1无条码 商品类型
        } else {
            map.put("code_type", "0");// 0有条码1无条码 商品类型
            map.put("code", mallNewTemplateBean.getCode());// 商品条码
        }
        map.put("commodity_icon", Tools.listToString(icon_ids));// 商品图片集，以逗号隔开
        map.put("commodity_desc", et_commodity_info.getText().toString());// 描述
        map.put("unit_name", et_unit.getText().toString());
        mQueue.add(ParamTools.packParam(Constants.update, this, this, map));
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            String msg = json.optString("msg");
            if (stauts == 0) {
                dismissLoading();
                if (url.contains(Constants.getuploadtoken)) {
                    upload_token = json.getString("result");
                } else if (url.contains(Constants.addOrEditCommodity) || url.contains(Constants.update)) {
                    finish();
                } else if (url.contains(Constants.getOpenChannels)) {
                    group = JSON.parseArray(json.getString("result"), ChannelBean.class);
                    initDate();
                } else if (url.contains(Constants.getCommodityInfo)) {
                    mallNewTemplateBean = JSON.parseObject(json.getString("result"), MallNewTemplateBean.class);
                    initChannel();
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
        if (position < icon_ids.size()) {
            icon_ids.remove(position);
        } else {
            filePaths.remove(position - icon_ids.size());
        }
        bitmapInfos.remove(position);
        images.remove(position);
        blank_views.remove(position);
        initPhotos();
        // setPhotosPos();
        // iv_add_photo.setVisibility(checkAddphotoIsAble() ? View.VISIBLE :
        // View.GONE);
    }

    @Override
    public void onConfirm(String areaName, int first_level_channel_id, int second_level_channel_id) {
        tv_classification.setText(areaName);
        this.first_level_channel_id = first_level_channel_id;
        this.second_level_channel_id = second_level_channel_id;
    }

}
