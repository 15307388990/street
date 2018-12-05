/**
 *
 */
package com.juxun.business.street.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.imagedemo.ImagePagerActivity;
import com.juxun.business.street.bean.CommodityInfoBean;
import com.juxun.business.street.bean.GoodDetailsBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.DetailsDialog;
import com.juxun.business.street.util.DetailsDialog2;
import com.juxun.business.street.util.DetailsDialog3;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 类名称：CheckdetailsAcitivity 类描述：查看详情 创建人：罗富贵 创建时间：2016年10月8日
 */
public class CheckdetailsAcitivity extends BaseActivity
        implements DetailsDialog.onConfirmListener, DetailsDialog2.onConfirmListener, DetailsDialog3.onConfirmListener {
    // 图片组
    @ViewInject(R.id.ll_photo_group)
    private LinearLayout ll_photo_group;
    @ViewInject(R.id.ll_photo_group2)
    private LinearLayout ll_photo_group2;
    // 过期时间
    @ViewInject(R.id.tv_date)
    private TextView tv_date;
    // 商品频道
    @ViewInject(R.id.tv_classification)
    private TextView tv_classification;
    // 商品状态
    @ViewInject(R.id.tv_state)
    private TextView tv_state;
    // 发布时间
    @ViewInject(R.id.tv_releasetime)
    private TextView tv_releasetime;
    // 商品编码
    @ViewInject(R.id.tv_coding)
    private TextView tv_coding;
    // 商品名称
    @ViewInject(R.id.et_goodsname)
    private TextView et_goodsname;
    // 商品单位
    @ViewInject(R.id.et_unit)
    private TextView et_unit;
    // 商品摘要
    @ViewInject(R.id.et_commodity_info)
    private TextView et_commodity_info;

    // 商品成本价
    @ViewInject(R.id.et_costprice)
    private TextView et_costprice;
    // 商品市场价
    @ViewInject(R.id.et_marketprice)
    private TextView et_marketprice;
    // 商品库存
    @ViewInject(R.id.et_inventory)
    private TextView et_inventory;
    // 返回
    @ViewInject(R.id.button_back)
    private ImageView button_back;
    // 更多
    @ViewInject(R.id.button_function)
    private ImageView button_function;
    private GoodDetailsBean goodDetails;
    private int commodityId;// 商品ID
    private int inventoryId;// 库存Id（获取上架或下架状态商品时才需传）
    private CommodityInfoBean mCommodityInfoBean;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();

    private int type;// 商品类型 1:上架商品 2：下架商品 3：审核中的商品 4：审核失败的商品 5：撤销商品

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkdetails);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        initView();
    }

    private void initView() {
        mCommodityInfoBean = (CommodityInfoBean) getIntent().getSerializableExtra("mCommodityInfoBean");
        commodityId = mCommodityInfoBean.getId();
        inventoryId = mCommodityInfoBean.getInventory_id();
        type = getIntent().getIntExtra("type", 0);
    }

    private void initDate() {
        switch (type) {
            case 1:
                tv_state.setText("出售中的商品");
                break;
            case 2:
                tv_state.setText("下架商品");
                break;
            case 3:
                tv_state.setText("审核中的商品");
                break;
            case 4:
                tv_state.setText("已撤销的商品");
                break;
            case 5:
                tv_state.setText("审核失败的商品");
                break;
            default:
                break;
        }
        mCommodityInfoBean.setCommodity_inventory(goodDetails.getCommodity_inventory());
        tv_releasetime.setText(Tools.getDateformat2(goodDetails.getCommodity_create_date()));
        tv_coding.setText(goodDetails.getCode());
        tv_classification.setText(goodDetails.getChannelName());
        et_goodsname.setText(goodDetails.getCommodity_name());
        et_unit.setText(goodDetails.getUnit_name());
        et_commodity_info.setText(goodDetails.getCommodity_info());
        et_costprice.setText(goodDetails.getCommodity_cost() + "");
        et_marketprice.setText(goodDetails.getCommodity_price() + "");
        et_inventory.setText(goodDetails.getCommodity_inventory() + "");
        tv_date.setText(Tools.getDateformat(goodDetails.getExpirationTime()));
        // 图片处理
        initPhotos();
    }

    /**
     * 这里动态的去添加图片
     */
    private void initPhotos() {
        String[] covers = goodDetails.getCover().split(",");
        ll_photo_group.removeAllViewsInLayout();
        ll_photo_group2.removeAllViewsInLayout();
        int photo_width = Tools.dip2px(this, 65);
        int line_width = Tools.dip2px(this, 20);
        final ArrayList<String> imageUrls = Tools.StringArrayToList(covers);
        for (int i = 0; i < covers.length; i++) {
            ImageView iv_photo = new ImageView(this);
            final int position = i;
            iv_photo.setLayoutParams(new RelativeLayout.LayoutParams(photo_width, photo_width));
            View blank_view = new View(this);
            blank_view.setLayoutParams(new LayoutParams(line_width, line_width));
            if (i < 4) {
                ll_photo_group.addView(iv_photo);
                ll_photo_group.addView(blank_view);
            } else {
                ll_photo_group2.addView(iv_photo);
                ll_photo_group2.addView(blank_view);
            }
            imageLoader.displayImage(Constants.imageUrl + covers[i], iv_photo, options);
            iv_photo.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    imageBrower(position, imageUrls);
                }
            });
        }
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.button_back, R.id.button_function})
    public void clickMethod(View v) {
        if (v.getId() == R.id.button_back) {
            this.finish();
        } else if (v.getId() == R.id.button_function) {
            switch (type) {
                case 1:
                    DetailsDialog detailsDialog = new DetailsDialog(CheckdetailsAcitivity.this, CheckdetailsAcitivity.this);
                    detailsDialog.showAtLocation(button_function, Gravity.BOTTOM, 0, 0);
                    break;
                case 2:
                    DetailsDialog2 detailsDialog2 = new DetailsDialog2(CheckdetailsAcitivity.this,
                            CheckdetailsAcitivity.this);
                    detailsDialog2.showAtLocation(button_function, Gravity.BOTTOM, 0, 0);
                    break;
                case 3:

                case 4:
                case 5:
                    DetailsDialog3 detailsDialog3 = new DetailsDialog3(CheckdetailsAcitivity.this,
                            CheckdetailsAcitivity.this);
                    detailsDialog3.showAtLocation(button_function, Gravity.BOTTOM, 0, 0);
                    break;
                // DetailsDialog detailsDialog4 = new
                // DetailsDialog(CheckdetailsAcitivity.this,
                // CheckdetailsAcitivity.this);
                // detailsDialog4.showAtLocation(button_function, Gravity.BOTTOM, 0,
                // 0);
                // break;
                default:
                    break;
            }
        }
    }

    //商品详情列表
    private void getCommodityInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("commodity_id", commodityId + "");
        mQueue.add(ParamTools.packParam(Constants.getCommodityInfo, this, this, map));
        loading();
    }

    // 删除商品
    private void delInventory() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("commodity_id", mCommodityInfoBean.getId() + "");
        mQueue.add(ParamTools.packParam(Constants.delInventory, this, this, map));
    }

    // 下架商品
    private void downSelf() {
        Map<String, String> map = new HashMap<>();
        map.put("commodity_id", mCommodityInfoBean.getId() + "");
        mQueue.add(ParamTools.packParam(Constants.downSelf, this, this, map));
    }

    // 上架商品
    private void upShelf() {
        Map<String, String> map = new HashMap<>();
        map.put("commodity_id", mCommodityInfoBean.getId() + "");
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.upShelf, this, this, map));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCommodityInfo();
    }

    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(getApplicationContext(), ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            String msg = json.optString("msg");
            if (stauts == 0) {
                if (url.contains(Constants.getCommodityInfo)) {
                    String mCommodity = json.getString("result");
                    goodDetails = JSON.parseObject(mCommodity, GoodDetailsBean.class);
                    initDate();
                } else if (url.contains(Constants.delInventory)) {
                    Tools.showToast(getApplicationContext(), "删除商品成功");
                    this.finish();
                } else if (url.contains(Constants.downSelf)) {
                    Tools.showToast(getApplicationContext(), "下架商品成功");
                    this.finish();
                } else if (url.contains(Constants.upShelf)) {
                    Tools.showToast(getApplicationContext(), "上架商品成功");
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
    public void onConfirm(int id) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mCommodityInfoBean", mCommodityInfoBean);
        intent.putExtras(bundle);
        switch (id) {
            // 修改信息
            case R.id.ll_amend_message:
                intent.setClass(getApplicationContext(), ModifyGoodsAcitivity.class);
                startActivity(intent);
                break;
            // 商品上架
            case R.id.ll_shelves:
                AlertDialog.Builder builder2 = new Builder(CheckdetailsAcitivity.this);
                builder2.setMessage("确认要上架这件商品吗？");
                builder2.setTitle("提示");
                builder2.setPositiveButton("上架", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        upShelf();
                    }
                });
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
                builder2.create().show();
                break;
            // 商品下架
            case R.id.ll_soldout:
                AlertDialog.Builder builder = new Builder(CheckdetailsAcitivity.this);
                builder.setMessage("确认要下架这件商品吗？");
                builder.setTitle("提示");
                final AlertDialog alertDialog = builder.create();
                builder.setPositiveButton("下架", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downSelf();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                break;
            // 修改库存保质期
            case R.id.ll_amend_shelflife:
                intent.setClass(CheckdetailsAcitivity.this, AmendShekflifeAcitivity.class);
                startActivity(intent);
                break;
            // 删除商品
            case R.id.ll_delete:
                AlertDialog.Builder builder3 = new Builder(CheckdetailsAcitivity.this);
                builder3.setMessage("确认要删除这件商品吗？");
                builder3.setTitle("提示");
                builder3.setPositiveButton("删除", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delInventory();
                    }
                });
                builder3.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
                builder3.create().show();
                break;

            default:
                break;
        }

    }

}
