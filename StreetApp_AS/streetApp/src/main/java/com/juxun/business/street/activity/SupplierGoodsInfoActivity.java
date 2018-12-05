package com.juxun.business.street.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.adapter.GoodsInfoPagerAdapter;
import com.juxun.business.street.bean.ShopingCartBean;
import com.juxun.business.street.bean.ShopingCartBean2;
import com.juxun.business.street.bean.SupplierGoodsBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.juxun.business.street.widget.AutoPlayViewPager;
import com.tencent.smtt.sdk.WebView;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.juxun.business.street.config.Constants.shoppingCart;
import static com.taobao.accs.ACCSManager.mContext;

/**
 * 商品详情 页面
 */
public class SupplierGoodsInfoActivity extends BaseActivity {

    @Bind(R.id.button_back)
    ImageView buttonBack;
    @Bind(R.id.rl_top)
    RelativeLayout rlTop;

    @Bind(R.id.fl_ads)
    RelativeLayout fl_ads;
    @Bind(R.id.vp_ads)
    AutoPlayViewPager vp_ads;
    @Bind(R.id.ll_ads_container)
    LinearLayout llContainer;

    @Bind(R.id.tv_goods_name)
    TextView tvGoodsName;
    @Bind(R.id.tv_goods_digest)
    TextView tvGoodsDigest;
    @Bind(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @Bind(R.id.tv_goods_dat)
    TextView tvGoodsDat;
    @Bind(R.id.ll_goods_discount)
    LinearLayout llGoodsDiscount;

    @Bind(R.id.iv_plus)
    ImageView ivPlus;
    @Bind(R.id.tv_goods_num)
    TextView tvGoodsNum;
    @Bind(R.id.iv_minus)
    ImageView ivMinus;

    @Bind(R.id.view_divider)
    View viewDivider;
    @Bind(R.id.tencent_webview)
    WebView tencent_webview;

    @Bind(R.id.ll_buttons)
    LinearLayout llButtons;
    @Bind(R.id.tv_number)
    TextView tvNumber;  // 购物车中商品数量
    @Bind(R.id.rl_shopcart)
    RelativeLayout rlShopcart;  //购物车这一块的
    @Bind(R.id.tv_add_goods)
    TextView tvAddGoods;    //加入购物车操作
    @Bind(R.id.tv_buu_goods)
    TextView tvBuuGoods;    //立即购买操作

    private String commodity_id;
    private int goodsNum = 1;   //新添加购物车的商品数量
    private int goodsOrigin = 0; //购物车
    private SupplierGoodsBean supplierGoodsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_goods_info);
        ButterKnife.bind(this);
        Tools.webacts.add(this);
        initViews();
    }

    private void initViews() {
        commodity_id = getIntent().getStringExtra("commodity_id");
        getSupplierCommodityInfo();

        tvGoodsNum.setText(1 + "");   //加减之间的数据
    }

    @Override
    protected void onResume() {
        shoppingCart();
        super.onResume();
    }

    @Override
    protected void onPause() {
        goodsOrigin = 0;
        super.onPause();
    }

    private void shoppingCart() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        mQueue.add(ParamTools.packParam(shoppingCart, this, this, map));
    }

    //获取商品详情数据
    private void getSupplierCommodityInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("commodity_id", commodity_id);
        mQueue.add(ParamTools.packParam(Constants.getSupplierCommodityInfo, this, this, map));
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            String msg = json.optString("msg");
            if (url.contains(Constants.getSupplierCommodityInfo)) {
                if (stauts == 0) {
                    supplierGoodsBean = JSON.parseObject(json.getString("result"), SupplierGoodsBean.class);
                    if (supplierGoodsBean != null) {
                        bindViewData(supplierGoodsBean);
                    }
                } else {
                    Tools.showToast(this, msg);
                }
            } else if (url.contains(Constants.addGoodsToCart)) {
                if (stauts == 0) {
                    goodsOrigin = goodsOrigin + goodsNum;
                    Tools.showToast(this, "成功添加购物车");
                    tvNumber.setVisibility(View.VISIBLE);
                    tvNumber.setText(goodsOrigin + "");
                } else {
                    Tools.showToast(this, msg);
                }
            } else if (url.contains(shoppingCart)) {
                if (stauts == 0) {
                    List<ShopingCartBean2> mShopingCartBeans = JSON.parseArray(json.getString("result"),
                            ShopingCartBean2.class);
                    if (mShopingCartBeans != null && mShopingCartBeans.size() > 0) {
                        for (int i = 0; i < mShopingCartBeans.size(); i++) {
                            goodsOrigin = goodsOrigin + mShopingCartBeans.get(i).getMsg_count();
                        }
                        tvNumber.setVisibility(View.VISIBLE);
                        tvNumber.setText(goodsOrigin + "");
                    } else {
                        tvNumber.setVisibility(View.GONE);
                    }

                } else {
                    Tools.showToast(this, msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }

    private void bindViewData(SupplierGoodsBean supplierGoodsBean) {
        //图片详情
        String commodity_icon = supplierGoodsBean.getCommodity_icon();
        initViewPager(commodity_icon.split(","));

        //具体的数据展示
        tvGoodsName.setText(supplierGoodsBean.getCommodity_name());
        tvGoodsDigest.setText(supplierGoodsBean.getCommodity_desc());
        String priceLow = mDf.format(Tools.getFenYuan(supplierGoodsBean.getPrice_low()));
        String priceHigh = mDf.format(Tools.getFenYuan(supplierGoodsBean.getPrice_high()));
        tvGoodsPrice.setText(priceHigh);
        // ll_goods_discount  动态添加批发优惠

        //商品详情数据的显示
        tencent_webview.loadUrl("");
    }

    private int previousSelectedPosition = 0; // 当前选中广告Id

    private void initViewPager(String[] split) {
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        vp_ads.setLayoutParams(new RelativeLayout.LayoutParams(
                widthPixels, widthPixels * 3 / 5));

        //数组转换成集合
        final ArrayList<String> iconStrs = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            iconStrs.add(split[i]);
        }

        if (iconStrs.size() > 0) {
            // 轮播点位的添加
            if (iconStrs.size() > 1) {// 大于一张的时候
                vp_ads.setScanScroll(true);
                llContainer.removeAllViews();
                View pointView;
                LinearLayout.LayoutParams layoutParmas;
                for (int i = 0; i < iconStrs.size(); i++) {
                    pointView = new View(SupplierGoodsInfoActivity.this);// 加上小白点，指示器
                    pointView.setBackgroundResource(R.drawable.guidepage_point_n);
                    layoutParmas = new LinearLayout.LayoutParams(15, 15);
                    if (i != 0) {
                        layoutParmas.leftMargin = 10;
                    }
                    pointView.setEnabled(false);// 设置默认所有都不可用
                    llContainer.addView(pointView, layoutParmas);
                }
            }

            // 初始化广告图片
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            GoodsInfoPagerAdapter goodsInfoPagerAdapter = new GoodsInfoPagerAdapter(this, wm
                    .getDefaultDisplay().getWidth());
            goodsInfoPagerAdapter.update(iconStrs);
            vp_ads.setAdapter(goodsInfoPagerAdapter);
            vp_ads.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrollStateChanged(int arg0) {

                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (iconStrs.size() > 1) { // 新的条目被选中时调用
                        int newPosition = position % iconStrs.size();
                        if (previousSelectedPosition > iconStrs.size()) {
                            previousSelectedPosition = 0;
                        }
                        try {
                            vp_ads.getChildAt(previousSelectedPosition).setEnabled(false);
                            llContainer.getChildAt(newPosition).setEnabled(true);
                        } catch (NullPointerException e) {
                            System.out.println("没有广告图");
                        }
                        previousSelectedPosition = newPosition;// 记录之前的位置
                    }
                }

                @Override
                public void onPageSelected(int arg0) {
                }
            });

            // 轮播效果处理（1张的情况下没有轮动效果）
            if (iconStrs.size() == 1) {// 只有一张图片的时候不能划动
                vp_ads.stop();
                vp_ads.setScanScroll(false);
                vp_ads.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
            } else {
                vp_ads.setDirection(AutoPlayViewPager.Direction.LEFT);// 设置播放方向
                vp_ads.setCurrentItem(2000); // 设置每个Item展示的时间
                vp_ads.start(); // 开始轮播
            }
        } else {
            // 没有图片的时候显示的图片
            vp_ads.setBackgroundResource(R.drawable.banner_is_loading);
        }
    }


    @OnClick({R.id.button_back, R.id.iv_plus, R.id.iv_minus, R.id.rl_shopcart, R.id.tv_add_goods, R.id.tv_buu_goods})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.iv_plus:
                //商品添加，数量的增加
                if (goodsNum >= supplierGoodsBean.getCommodity_inventory()) {
                    Tools.showToast(this, "库存不足，不可再添加");
                } else {
                    goodsNum = goodsNum + 1;
                }
                tvGoodsNum.setText(goodsNum + "");
                break;
            case R.id.iv_minus:
                //商品递减
                if (goodsNum == 1) {
                    return;
                } else {
                    goodsNum = goodsNum - 1;
                }
                tvGoodsNum.setText(goodsNum + "");
                break;
            case R.id.rl_shopcart:
                //点击进入购物车,购物车中有查询接口
                intent.setClass(SupplierGoodsInfoActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_add_goods:
                //商品加入购物车（接口中上传，购物车图标进行显）
                addGoodsToCart();
                break;
            case R.id.tv_buu_goods:
                //立即购买
                intent.setClass(SupplierGoodsInfoActivity.this, ConfirmTheOrderActivity.class);
                ArrayList<ShopingCartBean2> shopingCartBeenList = new ArrayList<>();
                ShopingCartBean2 shopingCartBean = new ShopingCartBean2();
                shopingCartBean.setId(supplierGoodsBean.getId());
                shopingCartBean.setPrice_high(supplierGoodsBean.getPrice_high());
                shopingCartBean.setMsg_count(goodsNum);
                shopingCartBean.setCommodity_icon(supplierGoodsBean.getCommodity_icon());
                shopingCartBean.setCommodity_name(supplierGoodsBean.getCommodity_name());
                shopingCartBeenList.add(shopingCartBean);
                intent.putExtra("suplierList", shopingCartBeenList);
                intent.putExtra("create_type", 1);
                startActivity(intent);
                break;
        }
    }

    private void addGoodsToCart() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("commodity_id", supplierGoodsBean.getId() + "");
        map.put("msg_count", goodsNum + "");
        mQueue.add(ParamTools.packParam(Constants.addGoodsToCart, this, this, map));
    }
}
