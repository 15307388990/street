package com.juxun.business.street.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.activity.AfterSaleListActivity;
import com.juxun.business.street.activity.CanCashActivity;
import com.juxun.business.street.activity.LoginActivity;
import com.juxun.business.street.activity.MainActivity;
import com.juxun.business.street.activity.MoreActivity;
import com.juxun.business.street.activity.OrderActivity;
import com.juxun.business.street.activity.PurchasingBalanceActivity;
import com.juxun.business.street.activity.RedPacketActivity;
import com.juxun.business.street.activity.ShopStatisticsActivity;
import com.juxun.business.street.activity.ShopWalletActivity;
import com.juxun.business.street.activity.StoreSetActivity;
import com.juxun.business.street.activity.ToPromoteActivity;
import com.juxun.business.street.bean.PartnerBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luoming 店铺
 */
@SuppressLint("ValidFragment")
public class StoreFragment extends BaseFragment {
    private MainActivity mActivity;

    private LinearLayout ll_top;// 店铺设置
    private ImageView iv_img;// 店面图片
    private TextView tv_store_name, tv_timer, tv_adds;// 店铺名称
    private TextView ll_set;// 设置

    private LinearLayout ll_purchasing;// 采购余额
    private LinearLayout ll_red;// 红包
    private LinearLayout ll_white;// 白条

    private TextView mTvShopStatistics, tv_shop_popularization;// 店铺统计 推广店铺

    private TextView tv_balance, tv_white_bar, tv_redpack_count;// 余额，白条，红包数量
    private ImageView tv_switch;
    private PartnerBean mPartnerBean;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();
    private TextView tv_obligation, tv_forthegoods, tv_have_the_goods,
            tv_return, tv_order_all;// 待付款，待收货，已收货，退换/售后，采购订单

    private RelativeLayout mRl_wallet;


    public StoreFragment(Activity context) {
        super(context);
        mActivity = (MainActivity) mcontext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        initView(view);
        initOnclik();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView(View view) {
        tv_obligation = (TextView) view.findViewById(R.id.tv_obligation);
        tv_forthegoods = (TextView) view.findViewById(R.id.tv_forthegoods);
        tv_have_the_goods = (TextView) view
                .findViewById(R.id.tv_have_the_goods);
        tv_return = (TextView) view.findViewById(R.id.tv_return);
        tv_order_all = (TextView) view.findViewById(R.id.tv_order_all);// 待付款，待收货，已收货，退换/售后，采购订单

        ll_set = (TextView) view.findViewById(R.id.ll_set);
        ll_red = (LinearLayout) view.findViewById(R.id.ll_red);
        ll_top = (LinearLayout) view.findViewById(R.id.ll_top);

        ll_purchasing = (LinearLayout) view.findViewById(R.id.ll_purchasing);
        ll_white = (LinearLayout) view.findViewById(R.id.ll_white);

        tv_balance = (TextView) view.findViewById(R.id.tv_balance);
        tv_white_bar = (TextView) view.findViewById(R.id.tv_white_bar);
        tv_redpack_count = (TextView) view.findViewById(R.id.tv_redpack_count);
        tv_switch = (ImageView) view.findViewById(R.id.tv_switch);
        iv_img = (ImageView) view.findViewById(R.id.iv_img);

        mTvShopStatistics = (TextView) view
                .findViewById(R.id.tv_shop_statistics);
        tv_shop_popularization = (TextView) view
                .findViewById(R.id.tv_shop_popularization);
        tv_store_name = (TextView) view.findViewById(R.id.tv_store_name);
        tv_timer = (TextView) view.findViewById(R.id.tv_timer);
        tv_adds = (TextView) view.findViewById(R.id.tv_adds);

        mRl_wallet = (RelativeLayout) view.findViewById(R.id.rl_wallet);
    }

    /* 获取店铺详情 */
    private void mallSetInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", partnerBean.getAuth_token());
        mQueue.add(ParamTools.packParam(Constants.mallSetInfo, this, this, map));
    }

    @Override
    public void onResume() {
        super.onResume();
        mallSetInfo();
    }

    private void initDate() {
        tv_balance.setText(Tools.getFenYuan(mPartnerBean.getRemaining_balance()) + ""); // 采购余额
        tv_redpack_count.setText(mPartnerBean.getRed_count() + ""); // 红包
        tv_white_bar.setText(Tools.getFenYuan(mPartnerBean
                .getCan_withdraw_price()) + ""); // 可提现金额
        int approval_status = mPartnerBean.getApproval_status();    //审核状态，0未审核，1审核通过，2审核失败
        if (approval_status == 1) {
            tv_switch.setVisibility(View.GONE);
        } else if (approval_status == 2) {
            tv_switch.setVisibility(View.VISIBLE);
            tv_switch.setImageDrawable(mcontext.getResources().getDrawable(
                    R.drawable.store_sign_review2));
        } else {
            tv_switch.setVisibility(View.VISIBLE);
        }
        if (mPartnerBean.getShop_icon() != null) {
            imageLoader
                    .displayImage(Constants.imageUrl
                                    + mPartnerBean.getShop_icon(), iv_img,
                            options);
        }
        tv_store_name.setText(mPartnerBean.getStore_name());
        if (mPartnerBean.getIs_24hour_business() == 1) {
            tv_timer.setText("营业时间：24小时营业");
        } else {
            tv_timer.setText("营业时间："
                    + mPartnerBean.getBusiness_start_date() + "-"
                    + mPartnerBean.getBusiness_end_date());
        }
        tv_adds.setText("地址：" + mPartnerBean.getShop_address());
    }

    private void initOnclik() {
        // 订单状态1.待付款,2.待发货,3.已发货,4.交易完成,5.已取消,6.已退货",
        // 钱包流程
        mRl_wallet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ShopWalletActivity.class);
                startActivity(intent);
            }
        });
        // 待付款
        tv_obligation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, OrderActivity.class);
                intent.putExtra("orderState", 1);
                startActivity(intent);
            }
        });
        // 待收货
        tv_forthegoods.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, OrderActivity.class);
                intent.putExtra("orderState", 3);
                startActivity(intent);
            }
        });
        // 已收货
        tv_have_the_goods.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, OrderActivity.class);
                intent.putExtra("orderState", 4);
                startActivity(intent);

            }
        });
        // 退换／售后
        tv_return.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Tools.jump(mActivity, AfterSaleListActivity.class, false);
            }
        });
        // 采购订单
        tv_order_all.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, OrderActivity.class);
                intent.putExtra("orderState", 0);
                startActivity(intent);
            }
        });
        // 店铺统计
        mTvShopStatistics.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Tools.jump(mActivity, ShopStatisticsActivity.class, false);
            }
        });
        tv_shop_popularization.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Tools.jump(mActivity, ToPromoteActivity.class, false);

            }
        });
        // 设置
        ll_set.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MoreActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        ll_red.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Tools.jump(mActivity, RedPacketActivity.class, false);
            }
        });
        // 店铺设置
        ll_top.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, StoreSetActivity.class);
                intent.putExtra("mPartnerBean", mPartnerBean);
                startActivityForResult(intent, 1);
            }
        });
        // 采购余额
        ll_purchasing.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Tools.jump(mActivity, PurchasingBalanceActivity.class, false);
            }
        });
        // 白条
        ll_white.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // String urlString = Constants.mainUrl + Constants.whiteBar
                // + "agency_id=" + storeBean.getAdmin_agency()
                // + "&auth_token=" + storeBean.getAuth_token()
                // + "&os_type=0";
                // Intent intent = new Intent(mActivity,
                // WhiteWebviewActivity.class);
                // intent.putExtra("name", "白条");
                // intent.putExtra("url", urlString);
                // intent.putExtra("type", 1);
                // startActivity(intent);

                Intent intent = new Intent(mActivity, CanCashActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            String msg = json.optString("msg");
            if (stauts == 0) {
                mPartnerBean = JSON.parseObject(json.optString("result"), PartnerBean.class);
                initDate();
            } else if (stauts == -4004) {
                mSavePreferencesData.putStringData("json", "");
                Tools.showToast(getActivity(), "登录过期请重新登录");
                Tools.jump(getActivity(), LoginActivity.class, true);
            } else {
                Tools.showToast(getActivity(), msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(getActivity(), "解析错误");
        }
    }
}
