package com.juxun.business.street.activity;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.bean.ShopingCartBean;
import com.juxun.business.street.bean.ShopingCartBean2;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.juxun.business.street.config.Constants.clearGoodsToCart;
import static com.juxun.business.street.config.Constants.delGoodsToCart;

/**
 * @author ShoppingCartActivity 购物车商品列表 罗富贵 2016/11月/2日
 */
public class ShoppingCartActivity extends BaseActivity {

    @ViewInject(R.id.button_back)
    private ImageView button_back;// 返回
    @ViewInject(R.id.button_function)
    private TextView button_function;// 删除

    @ViewInject(R.id.tv_price)
    private TextView tv_price;// 合计
    @ViewInject(R.id.tv_jixu)
    private TextView tv_jixu;// 继续采购
    @ViewInject(R.id.btn_calculate)
    private Button btn_calculate;// 结算

    @ViewInject(R.id.lv_shoplist)
    private LinearLayout lv_shoplist;// 列表
    @ViewInject(R.id.ll_wu)
    private LinearLayout ll_wu;// 无商品

    List<ShopingCartBean2> mShopingCartBeans;
    List<Boolean> booleans = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();
    DecimalFormat df = new java.text.DecimalFormat("0.00");
    String idS;
    private double itemPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shiping_cart);
        ViewUtils.inject(this);
        Tools.acts.add(this);

    }
    @Override
    protected void onResume() {
        ShoppingCart();
        super.onResume();
    }
    private void initLayout() {
        //可选与不可选的
        if (booleans == null) {
            booleans = new ArrayList<>();
            for (int i = 0; i < mShopingCartBeans.size(); i++) {
                booleans.add(true);
            }
        }
        //商品列表展示
        lv_shoplist.removeAllViews();
        for (int i = 0; i < mShopingCartBeans.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.shopping_item, null);
            CheckBox add_chekbox = (CheckBox) view.findViewById(R.id.add_chekbox);
            ImageView iv_img = (ImageView) view.findViewById(R.id.icon);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_shopname);
            final EditText tv_number = (EditText) view.findViewById(R.id.et_number);
            TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
            TextView tv_heji = (TextView) view.findViewById(R.id.tv_heji);
            LinearLayout ll_add = (LinearLayout) view.findViewById(R.id.ll_add);    //添加
            LinearLayout ll_min = (LinearLayout) view.findViewById(R.id.ll_min);    //删除

            /*********************************************************************/
            add_chekbox.setTag(i);
            tv_heji.setTag(i);
            ll_add.setTag(i);
            ll_min.setTag(i);
            add_chekbox.setChecked(booleans.get(i));
            final ShopingCartBean2 shopingCartBean = mShopingCartBeans.get(i);
            tv_name.setText(shopingCartBean.getCommodity_name());
            int retail_price = shopingCartBean.getPrice_high();
            tv_price.setText("¥" + Tools.getFenYuan(retail_price));
            int msg_count = shopingCartBean.getMsg_count();
            tv_number.setText(msg_count + "");
            String[] cover = shopingCartBean.getCommodity_icon().split(",");
            imageLoader.displayImage(Constants.imageUrl + cover[0], iv_img, options);

            //单条总计初始化
            tv_heji.setText("小计：¥" + Tools.getFenYuan(retail_price * msg_count));

            /*********************************************************************/

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    delGoodsToCart(shopingCartBean.getId() + "");
                    return false;
                }
            });

            ll_min.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (shopingCartBean.getMsg_count() == 1) {
                        AlertDialog.Builder builder = new Builder(
                                ShoppingCartActivity.this);
                        builder.setMessage("确认要删除吗？");
                        builder.setTitle("提示");
                        builder.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        shopingCartBean.setMsg_count(0);
                                        onConfirm(shopingCartBean);
                                    }
                                });
                        builder.setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // do nothing
                                    }
                                });
                        builder.create().show();
                    } else {
                        shopingCartBean.setMsg_count(shopingCartBean.getMsg_count() - 1);
                        onConfirm(shopingCartBean);
                    }
                }
            });
            ll_add.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    shopingCartBean.setMsg_count(shopingCartBean.getMsg_count() + 1);
                    onConfirm(shopingCartBean);
                }
            });
            tv_number.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    tv_number.setText(tv_number.getText().toString());
                    tv_number.selectAll();
                }
            });
            tv_number.setOnEditorActionListener(new OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int actionId,
                                              KeyEvent event) {
                    if (actionId == 0 || actionId == 6) {
                        shopingCartBean.setMsg_count(Integer.valueOf(tv_number.getText().toString()));
                        onConfirm(shopingCartBean);
                        InputMethodManager imm = (InputMethodManager) getSystemService(ShoppingCartActivity.this.INPUT_METHOD_SERVICE);
                        boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
                        if (isOpen) {
                            imm.hideSoftInputFromWindow(
                                    tv_number.getWindowToken(), 0);
                        }
                        return true;
                    }
                    return false;
                }
            });
            tv_number.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (tv_number.getText().toString().equals("")) {
                        tv_number.setText("1");
                    }
                }
            });
            add_chekbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    booleans.set((Integer) buttonView.getTag(), isChecked);
                    initButton();
                }
            });
            lv_shoplist.addView(view);
        }
        initButton();
    }

    //总合计计算
    private void initButton() {
        // 是否有选中
        Boolean falg = false;
        for (int i = 0; i < booleans.size(); i++) {
            if (booleans.get(i)) {
                falg = true;
                break;
            }
        }

        int price = 0;
        for (int i = 0; i < mShopingCartBeans.size(); i++) {
            if (booleans.get(i)) {
                price = price + mShopingCartBeans.get(i).getMsg_count() * mShopingCartBeans.get(i).getPrice_high();
            }
        }
        tv_price.setText("¥" + Tools.getFenYuan(price));
        if (falg) {
            button_function.setTextColor(getResources().getColor(R.color.white));
            btn_calculate.setTextColor(getResources().getColor(R.color.white));
            btn_calculate.setBackgroundResource(R.color.blue);
        } else {
            button_function.setTextColor(getResources().getColor(R.color.gray));
            btn_calculate.setTextColor(getResources().getColor(R.color.gray));
            btn_calculate.setBackgroundResource(R.color.crimson);
        }
        button_function.setClickable(falg);
        btn_calculate.setClickable(falg);
    }

    private void updateGoodsToCart(ShopingCartBean2 shopingCartBean) {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("commodity_id", shopingCartBean.getId() + "");
        map.put("msg_count", shopingCartBean.getMsg_count() + "");
        mQueue.add(ParamTools.packParam(Constants.updateGoodsToCart, this, this, map));
    }

    // 删除购物车商品
    private void delGoodsToCart(String ids) {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        map.put("commodity_id", ids);
        mQueue.add(ParamTools.packParam(delGoodsToCart, this, this, map));
    }

    // 清空购物车
    private void clearGoodsToCart() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        mQueue.add(ParamTools.packParam(clearGoodsToCart, this, this, map));
    }

    // 获取购物车商品列表
    private void ShoppingCart() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token() + "");
        mQueue.add(ParamTools.packParam(Constants.shoppingCart, this, this, map));
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.button_back, R.id.button_function, R.id.tv_jixu,
            R.id.btn_calculate})
    public void clickMethod(View v) {
        if (v.getId() == R.id.button_back) {
            this.finish();
        } else if (v.getId() == R.id.button_function) {
            // 清空
            idS = "";
            int number = 0;
            for (int i = 0; i < booleans.size(); i++) {
                if (booleans.get(i)) {
                    if (i == 0) {
                        idS = idS + mShopingCartBeans.get(i).getId();
                    } else {
                        idS = idS + "," + mShopingCartBeans.get(i).getId();
                    }
                    number = number + 1;
                }
            }
            AlertDialog.Builder builder = new Builder(ShoppingCartActivity.this);
            builder.setMessage("确认要清空购物车吗？");
            builder.setTitle("提示");
            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            clearGoodsToCart();
                        }
                    });
            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    });
            builder.create().show();

        } else if (v.getId() == R.id.tv_jixu) {
            this.finish();
        } else if (v.getId() == R.id.btn_calculate) {
            List<ShopingCartBean2> list = new ArrayList<>();
            for (int i = 0; i < booleans.size(); i++) {
                if (booleans.get(i)) {
                    list.add(mShopingCartBeans.get(i));
                }
            }
            // 结算
            Intent intent = new Intent(ShoppingCartActivity.this,
                    ConfirmTheOrderActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("suplierList", (Serializable) list);
            intent.putExtras(bundle);
            intent.putExtra("create_type", 0);
            startActivity(intent);
        }
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int status = json.optInt("status");
            String msg = json.optString("msg");
            if (status == 0) {
                if (url.contains(Constants.shoppingCart)) {
                    String resultJson = json.optString("result");
                    mShopingCartBeans = JSON.parseArray(resultJson,
                            ShopingCartBean2.class);
                    if (mShopingCartBeans.size() > 0) {
                        lv_shoplist.setVisibility(View.VISIBLE);
                        ll_wu.setVisibility(View.GONE);
                        button_function.setTextColor(getResources().getColor(
                                R.color.white));
                        btn_calculate.setTextColor(getResources().getColor(
                                R.color.white));
                        btn_calculate.setBackgroundResource(R.color.blue);
                        btn_calculate.setClickable(true);
                        button_function.setClickable(true);
                    } else {
                        lv_shoplist.setVisibility(View.GONE);
                        ll_wu.setVisibility(View.VISIBLE);
                        button_function.setTextColor(getResources().getColor(
                                R.color.gray));
                        btn_calculate.setTextColor(getResources().getColor(
                                R.color.gray));
                        btn_calculate.setBackgroundResource(R.color.crimson);
                        btn_calculate.setClickable(false);
                        button_function.setClickable(false);
                    }
                    initLayout();
                } else if (url.contains(Constants.updateGoodsToCart)) {
                    ShoppingCart();
                } else if (url.contains(delGoodsToCart)) {
                    Tools.showToast(this, "删除成功");
                    booleans = null;
                    ShoppingCart();
                } else if (url.contains(clearGoodsToCart)) {
//                    lv_shoplist.removeAllViews();
                    ShoppingCart();
                }
            } else {
                Tools.showToast(getApplicationContext(), msg);
            }
        } catch (Exception e) {
            Tools.showToast(getApplicationContext(), "网络错误");
        }
    }

    public void onConfirm(ShopingCartBean2 shopingCartBean) {
        if (shopingCartBean.getMsg_count() == 0) {
            // 执行删除
            delGoodsToCart(shopingCartBean.getId() + "");
        } else {
            updateGoodsToCart(shopingCartBean);
        }
    }
}