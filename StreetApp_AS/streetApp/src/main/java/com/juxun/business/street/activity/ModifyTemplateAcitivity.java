/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
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
import com.juxun.business.street.bean.mTemplateBean;
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
 * 类名称：ModifyGoodsAcitivity 类描述：模板商品信息 创建人：罗富贵 创建时间：2016年10月24日
 */
public class ModifyTemplateAcitivity extends BaseActivity implements ICallBack, WheelViewDialog.onConfirmListener {
    // 图片组
    @ViewInject(R.id.ll_photo_group)
    private LinearLayout ll_photo_group;
    @ViewInject(R.id.ll_photo_group2)
    private LinearLayout ll_photo_group2;
    @ViewInject(R.id.tv_date)
    private TextView tv_date;
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

    // 返回
    @ViewInject(R.id.button_back)
    private ImageView button_back;
    @ViewInject(R.id.textview_title)
    private TextView textview_title;

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
    private String ids;
    TimerDateUtil timerDate = new TimerDateUtil();
    // 频道ID
    private int chanenlID = -1;
    String icon_id = null;
    private int commodityId, inventoryId;
    private mTemplateBean goodDetails;
    private int Template_id;
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            bitmapInfos.add((Bitmap) msg.obj);
            initPhotos();
        }

        ;
    };
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifygoods);
        ViewUtils.inject(this);
        filePaths = new ArrayList<String>();
        icon_ids = new ArrayList<String>();
        bitmapInfos = new ArrayList<Bitmap>();
        images = new ArrayList<ImageWithDelete>();
        resultList = new ArrayList<String>();
        blank_views = new ArrayList<View>();
        initView();

    }

    private void initDate() {
        tv_coding.setText(goodDetails.getCode());
        tv_classification.setText(goodDetails.getChannelName());
        et_goodsname.setText(goodDetails.getTemplate_name());
        et_unit.setText(goodDetails.getUnit_name());
        et_commodity_info.setText(goodDetails.getTemplate_info());
        et_costprice.setText(goodDetails.getTemplate_cost() + "");
        et_marketprice.setText(goodDetails.getTemplate_price() + "");
        et_inventory.setText("");
        tv_date.setText("");
        chanenlID = goodDetails.getChannel_id();
        // 图片处理
        String[] cover = goodDetails.getCover().split(",");
        for (int i = 0; i < cover.length; i++) {
            resultList.add(cover[i]);
        }
        for (int k = 0; k < resultList.size(); k++) // 将数据重新整合
        {
            final String url = Constants.imageUrl + resultList.get(k);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Bitmap bitmap = ImageUtil.getbitmap(url);
                    Message msg = new Message();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);

                }
            }).start();
            icon_ids.add(resultList.get(k));
        }

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

    private void getCommodityInfo() {
        Map<String, String> map = new HashMap<String, String>();
        // commodityId int 商品id
        // agencyId int 机构id
        // inventoryId int 库存Id（获取上架或下架状态商品时才需传）
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("commodity_id", commodityId + "");
        mQueue.add(ParamTools.packParam(Constants.getCommodityInfo, this, this, map));
        loading();
    }

    private void initView() {
        Template_id = getIntent().getIntExtra("Template_id", 0);
        getTemplateInfo();
        iv_add_photo = new ImageView(this);
        iv_add_photo.setBackgroundResource(R.drawable.add_btn_addpic);
        iv_add_photo.setScaleType(ScaleType.FIT_XY);
        iv_add_photo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
//                    checkoutWritePermissions();
//                    checkoutWritePermissions();
                    intent = new Intent(ModifyTemplateAcitivity.this, MultiImageSelectorActivity.class);
                    // 是否显示拍摄图片
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                    // 最大可选择图片数量
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxSelectedCount - images.size());
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        tv_date.setText(timerDate.getNowDate());
        button_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        textview_title.setText("添加商品");
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
            startActivityForResult(intent, 0);
        }
    }

    @PermissionDenied(REQUEST_CODE_WRITE)
    public void requestWriteFailed() {
        Toast.makeText(this, "请打开读写手机存储权限", Toast.LENGTH_SHORT).show();
    }

    private void getTemplateInfo() {
        Map<String, String> map = new HashMap<String, String>();
        // mallTemplateId int 商品模板id
        map.put("mallTemplateId", Template_id + "");// 机构id
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.getTemplateInfo, this, this, map));
        loading();
    }

    private Boolean isInput() {
        if (chanenlID != -1) {
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
        } else {
            Tools.showToast(this, "未选择商品频道");
        }
        return false;

    }

    /**
     * 单击事件
     */
    @OnClick({R.id.btn_query, R.id.tv_date, R.id.tv_classification})
    public void clickMethod(View v) {
        if (v.getId() == R.id.btn_query) {
            if (isInput()) {
                // if (!TextUtils.isEmpty(et_inventory.getText())) {
                // int inven =
                // Integer.valueOf(et_inventory.getText().toString());
                // if (inven < goodDetails.getCommodity_inventory()) {
                // Tools.showToast(getApplicationContext(), "修改库存不能小于现有库存");
                // return;
                // }
                // }
                if (!Tools.isFastDoubleClick()) {
                    upLoadPhoto(filePaths);
                } else {
                    Toast.makeText(this, "操作太频繁", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (v.getId() == R.id.tv_date) {
            birth = new SelectBirthdayPop(ModifyTemplateAcitivity.this, timerDate.getNowDate(), tv_date);
            birth.showAtLocation(tv_date, Gravity.BOTTOM, 0, 0);
        } else if (v.getId() == R.id.tv_classification) {
            initChannel();
            loading();
        }
    }

    private void initChannel() {
        Map<String, String> map = new HashMap<String, String>();
        // agency_id int 机构id
        // auth_token String 登陆令牌
        // admin_id int 管理员id
        map.put("auth_token", partnerBean.getAuth_token());
//		map.put("hasAllChannel", "1");
        mQueue.add(ParamTools.packParam(Constants.getOpenChannels, this, this, map));
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getUploadToken();
        loading();

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
        Map<String, String> map = new HashMap<String, String>();
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
                                // TODO Auto-generated method stub
                                Log.d("yj", "onComplete");
                                if (arg1.statusCode == 200) {
                                    try {
                                        icon_id = icon_id + arg2.getString("key");
                                        if (y == (filePaths.size() - 1)) {
                                            addOrEditCommodity();
                                        }
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }, null);
            }
        } else {
            addOrEditCommodity();
        }

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
        // map.put("inventoryId",goodDetails.getCommodity_inventory());
        // map.put("commodityId", goodDetails.getCommodity_id() + "");
        if (chanenlID == 0) {
            map.put("channel_id", ids);
        } else {
            map.put("channel_id", chanenlID + "");
        }

        if (!goodDetails.getCode().equals("")) {
            map.put("code", goodDetails.getCode());
        }
        map.put("commodity_cost", et_costprice.getText().toString());
        map.put("cover", Tools.listToString(icon_ids));
        map.put("commodity_info", et_commodity_info.getText().toString());
        map.put("commodity_inventory", et_inventory.getText().toString());
        map.put("commodity_name", et_goodsname.getText().toString());
        map.put("commodity_price", et_marketprice.getText().toString());
        map.put("expirationTime", tv_date.getText().toString());
        map.put("unit_name", et_unit.getText().toString());
        map.put("auth_token", partnerBean.getAuth_token());
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
                dismissLoading();
                if (url.contains(Constants.getuploadtoken)) {
                    upload_token = json.getString("token");
                } else if (url.contains(Constants.getTemplateInfo)) {
                    String mCommodity = json.getString("mTemplate");
                    goodDetails = JSON.parseObject(mCommodity, mTemplateBean.class);
                    initDate();
                } else if (url.contains(Constants.addOrEditCommodity)) {
                    Tools.showToast(getApplicationContext(), "添加成功");
                    this.finish();
                } else if (url.contains(Constants.getOpenChannels)) {
                    String liString = json.getString("list");
                    List<ChannelBean> group = JSON.parseArray(liString, ChannelBean.class);
                    if (group.size() != 0) {
                        WheelViewDialog dialog = new WheelViewDialog(ModifyTemplateAcitivity.this, group,
                                group.get(0).getChildChannelList(), ModifyTemplateAcitivity.this);
                        dialog.showAtLocation(tv_classification, Gravity.BOTTOM, 0, 0);
                    }

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
//		tv_classification.setText(areaName);
//		this.chanenlID = id;
//        this.ids = ids;
    }

}
