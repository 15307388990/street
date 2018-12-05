package com.juxun.business.street.bean;


import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.example.imagedemo.ImagePagerActivity;
import com.juxun.business.street.activity.BaseActivity;
import com.juxun.business.street.activity.ConfirmTheOrderActivity;
import com.juxun.business.street.activity.ShoppingCartActivity;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author PurchaseDetails 供应商品详细
 */
public class PurchaseDetailsBean extends BaseActivity {
    /**
     * 滚动图片模块
     */
    @ViewInject(R.id.vp)
    private ViewPager vp;
    @ViewInject(R.id.dots_group_parent)
    private LinearLayout dots_group_parent;
    @ViewInject(R.id.dots_group)
    private LinearLayout dots_group;
    /**
     *
     */
    @ViewInject(R.id.tv_name)
    private TextView tv_name;// 商品名
    @ViewInject(R.id.tv_desc)
    private TextView tv_desc;// 商品描述
    @ViewInject(R.id.tv_price)
    private TextView tv_price;// 商品区间价格
    @ViewInject(R.id.webview) // 详情网页
    private WebView webview;

    /**
     *
     */
    @ViewInject(R.id.iv_min)
    private ImageView iv_min;// 减
    @ViewInject(R.id.et_number)
    private EditText et_number;// 数量
    @ViewInject(R.id.iv_add)
    private ImageView iv_add;// 加
    @ViewInject(R.id.tv_add_cast)
    private TextView tv_add_cast;// 加入购物车
    @ViewInject(R.id.btn_buy)
    private Button btn_buy;// 立即购买

    @ViewInject(R.id.tv_number)
    private TextView tv_number;// 购物车数量
    @ViewInject(R.id.button_back)
    private ImageView button_back;// 返回
    @ViewInject(R.id.rl_shopcart)
    private RelativeLayout rl_shopcart;// 购物车

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();
    private List<ImageView> imageViews;
    private int currentItem = 0; // 当前图片的索引号
    private int id;
    private SupplierCommodityBean mSupplier;
    private ScheduledExecutorService scheduledExecutorService;
    DecimalFormat df = new java.text.DecimalFormat("0.00");
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            vp.setCurrentItem(currentItem);// 切换当前显示的图片
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_purchase_details);
        ViewUtils.inject(this);
        Tools.acts.add(this);
        id = getIntent().getIntExtra("id", 0);
        initView();
    }

    private void initView() {
        et_number.setText("0");
        iv_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int number = Integer.valueOf(et_number.getText().toString()) + 1;
                et_number.setText(number + "");

            }
        });
        iv_min.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int number = Integer.valueOf(et_number.getText().toString()) - 1;
                if (number < 0) {
                    return;
                }
                et_number.setText(number + "");
            }
        });
        et_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_number.getText().toString().equals("")) {
                    et_number.setText("0");
                }

            }
        });
        tv_add_cast.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int number = Integer.valueOf(et_number.getText().toString());
                if (number > 0) {
                    addOrUpdate(number);
                } else {
                    Tools.showToast(getApplicationContext(), "商品数量不能小于1");
                }

            }
        });
        btn_buy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int number = Integer.valueOf(et_number.getText().toString());
                if (number > 0) {
                    List<ShopingCartBean> list = new ArrayList<ShopingCartBean>();
                    ShopingCartBean shopingCartBean = new ShopingCartBean();
                    shopingCartBean.setCommodityICon(mSupplier.getCommodityICon());
                    shopingCartBean.setCommodityName(mSupplier.getCommodityName());
                    shopingCartBean.setId(mSupplier.getId());
//					shopingCartBean.setLadderBean(mSupplier.getLadderList());
                    shopingCartBean.setGoodsCount(number);
                    // shopingCartBean.setSupplier_id(mSupplier.getSupplierId());
                    double price = 0;
                    double value = 0;
                    if (mSupplier.getWholesaleType() == 1) {
                        List<LadderBean> ladderBeans = mSupplier.getLadderList();
                        for (int i = 0; i < ladderBeans.size(); i++) {
                            if (i + 1 < list.size()) {
                                if (number <= (ladderBeans.get(i + 1).getLadderCount() - 1)
                                        && number >= ladderBeans.get(i).getLadderCount()) {
                                    value = number * ladderBeans.get(i).getLadderPrice();
                                    price = ladderBeans.get(i).getLadderPrice();
                                    return;
                                }
                            } else {
                                value = number * ladderBeans.get(i).getLadderPrice();
                                price = ladderBeans.get(i).getLadderPrice();
                                return;
                            }
                        }
                    } else {
                        value = number * mSupplier.getPriceLow();
                        price = mSupplier.getPriceLow();
                    }
//                    shopingCartBean.setUnit_name(mSupplier.getCommodityUnit());
//                    shopingCartBean.setRetail_price((int) price);
                    list.add(shopingCartBean);
                    // 确认下单
                    Intent intent = new Intent(PurchaseDetailsBean.this, ConfirmTheOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("suplierList", (Serializable) list);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Tools.showToast(PurchaseDetailsBean.this, "购买数量不能小于1");
                }

            }
        });
        button_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_number.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                et_number.setText(et_number.getText().toString());
                et_number.selectAll();
            }
        });
        rl_shopcart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Tools.jump(PurchaseDetailsBean.this, ShoppingCartActivity.class, false);
            }
        });
    }

    // 判断购物车里是否存在该商品，存在则修改，否则添加。
    private void addOrUpdate(int number) {
        // commodityId 商品Id
        // agencyId 合伙人机构id
        // commodityCount 商品数量

        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("commodityId", mSupplier.getId() + "");
        map.put("commodityCount", number + "");
        mQueue.add(ParamTools.packParam(Constants.addItem, this, this, map));
    }

    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(getApplicationContext(), ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }

    // 得到合伙人购物车数量
    private void getPartnerShoppingCartNumber() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        mQueue.add(ParamTools.packParam(Constants.getPartnerShoppingCartNumber, this, this, map));
        loading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        findCommodityItem();
        getPartnerShoppingCartNumber();
    }

    // 获取供应商频道列表
    private void findCommodityItem() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id + "");
        map.put("auth_token", partnerBean.getAuth_token() + "");
        mQueue.add(ParamTools.packParam(Constants.findCommodityItem, this, this, map));
    }

    private void iniDate() {
        String[] cover = mSupplier.getCommodityICon().split(",");
        final ArrayList<String> strings = new ArrayList<String>();
        imageViews = new ArrayList<ImageView>();
        Collections.addAll(strings, cover);
        for (int i = 0; i < cover.length; i++) {
            ImageView imageView = new ImageView(this);
            imageLoader.displayImage(Constants.imageUrl + cover[i], imageView, // 动态初始化图片
                    options);
            imageView.setScaleType(ScaleType.FIT_XY);
            imageView.setTag(i);
            imageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag();
                    imageBrower(position, strings);
                }
            });
            imageViews.add(imageView);
        }
        initViewPager();
        tv_name.setText(mSupplier.getCommodityName());
        tv_desc.setText(mSupplier.getCommoditydesc());
        // 1为有阶梯价格
        tv_price.setText("¥" + mSupplier.getPriceLow());
        initWeb();
    }

    private void initWeb() {
        webview.setFocusable(false);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(false);// 设定缩放控件隐藏
        webSettings.setLoadWithOverviewMode(true);
        webview.loadUrl(Constants.mainUrl + Constants.commodityInfo + "?commodityId=" + mSupplier.getId());
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // progress_bar.setVisibility(View.GONE);
            }
        });
    }

    private void initViewPager() {
        if (imageViews.size() == 1) {// 1张图片的时候 隐藏下面的指示器
            dots_group_parent.setVisibility(View.GONE);
        }
        MyAdapter mAdapter = new MyAdapter(imageViews);
        vp.setAdapter(mAdapter);
        List<View> dots = new ArrayList<View>();
        dots_group.removeAllViews();
        int dotSize = 0;
        int blankSize = 0;
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int displayWidth = wm.getDefaultDisplay().getWidth();// 根据屏幕分辨率动态设置动画距离
        if (displayWidth == 1080) {
            dotSize = 24;
            blankSize = 9;

        } else {
            dotSize = 18;
            blankSize = 6;
        }
        View viewFirst = new View(this);
        viewFirst.setLayoutParams(new LayoutParams(dotSize, dotSize));
        View blackView = new View(this);
        blackView.setLayoutParams(new LayoutParams(blankSize, blankSize));
        viewFirst.setBackgroundResource(R.drawable.dot_focused);
        viewFirst.setPadding(5, 0, 5, 0);
        viewFirst.setId(0);
        dots_group.addView(viewFirst);
        dots.add(viewFirst);
        dots_group.addView(blackView);
        for (int i = 1; i < imageViews.size(); i++) {
            View view = new View(this);
            view.setLayoutParams(new LayoutParams(dotSize, dotSize));
            view.setPadding(5, 0, 5, 0);
            view.setId(i);
            View blackView2 = new View(this);
            blackView2.setLayoutParams(new LayoutParams(blankSize, blankSize));
            view.setBackgroundResource(R.drawable.dot_normal);
            dots.add(view);
            dots_group.addView(view);
            dots_group.addView(blackView2);
        }

        MyPageChangeListener mPageChangeListener = new MyPageChangeListener(dots);
        vp.setOnPageChangeListener(mPageChangeListener);
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("resultCode");
            String msg = json.optString("resultMsg");
            if (stauts == 0) {
                if (url.contains(Constants.findCommodityItem)) {
                    String liString = json.getString("resultJson");
                    mSupplier = JSON.parseObject(liString, SupplierCommodityBean.class);
                    iniDate();
                } else if (url.contains(Constants.addItem)) {
                    Tools.showToast(getApplicationContext(), "加入购物车成功");
                    getPartnerShoppingCartNumber();
                } else if (url.contains(Constants.getPartnerShoppingCartNumber)) {
                    JSONObject jsonObject = json.getJSONObject("resultJson");
                    int commodityNumber = jsonObject.getInt("commodityNumber");
                    if (commodityNumber > 0) {
                        tv_number.setText(commodityNumber + "");
                        tv_number.setVisibility(View.VISIBLE);
                    } else {
                        tv_number.setVisibility(View.GONE);
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

    /**
     * 当ViewPager中页面的状态发生改变时调用
     *
     * @author Administrator
     */
    private class MyPageChangeListener implements OnPageChangeListener, OnClickListener {
        private int oldPosition = 0;
        private List<View> dots;

        public MyPageChangeListener(List<View> dots) {
            this.dots = dots;
        }

        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
            currentItem = position;
            try {
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
                dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            onPageSelected(v.getId());
            vp.setCurrentItem(v.getId());

        }
    }

    /**
     * 填充ViewPager页面的适配器
     *
     * @author Administrator
     */
    private class MyAdapter extends PagerAdapter {
        private List<ImageView> imageViews;

        @Override
        public int getCount() {
            return imageViews.size();
        }

        public MyAdapter(List<ImageView> imageViews) {
            this.imageViews = imageViews;

        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(imageViews.get(arg1));
            return imageViews.get(arg1);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            Log.e("yj", "destroyItem" + imageViews.get(arg1));
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }
    }

    @Override
    public void onStart() {
        // 显示出来后，每两秒钟切换一次图片显示
        super.onStart();
        Log.d("HomeFragment", "onStart");
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 5, TimeUnit.SECONDS);
    }

    private class ScrollTask implements Runnable {

        public void run() {
            synchronized (vp) {
                Log.d("HomeFragment", "currentItem" + currentItem);
                System.out.println("currentItem: " + currentItem);
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }

}
