package com.juxun.business.street.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.activity.PurchaseOrderDetailsActivity;
import com.juxun.business.street.activity.WebviewActivity;
import com.juxun.business.street.bean.Agreement7;
import com.juxun.business.street.bean.PurchaseOderBean;
import com.juxun.business.street.bean.ShopingBean;
import com.juxun.business.street.bean.ShopingCartBean;
import com.juxun.business.street.bean.StoreBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.SavePreferencesData;
import com.juxun.business.street.util.Tools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author PurchaseOrderdapter 供应商订单列表
 */
public class PurchaseOrderdapter extends BaseAdapter {
    private List<PurchaseOderBean> list = null;
    private Activity mContext;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();
    String member_id;
    private OneCallBack oneCallBack;
    private TwoCallBack twoCallBack;
    DecimalFormat df = new java.text.DecimalFormat("0.00");
    public SavePreferencesData mSavePreferencesData;

    public PurchaseOrderdapter(Activity mContext, List<PurchaseOderBean> list, OneCallBack oneCallBack,
                               TwoCallBack twoCallBack) {
        this.mContext = mContext;
        this.list = list;
        this.oneCallBack = oneCallBack;
        this.twoCallBack = twoCallBack;
        mSavePreferencesData = new SavePreferencesData(mContext);
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<PurchaseOderBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = new ViewHolder();
        view = LayoutInflater.from(mContext).inflate(R.layout.purchase_order_item, null);
        viewHolder.tv_total_of = (TextView) view.findViewById(R.id.tv_total_of);
        viewHolder.tv_rest_of = (TextView) view.findViewById(R.id.tv_rest_of);
        viewHolder.btn_one = (Button) view.findViewById(R.id.btn_one);
        viewHolder.btn_two = (Button) view.findViewById(R.id.btn_two);
        viewHolder.listView = (LinearLayout) view.findViewById(R.id.lv_list);
        viewHolder.tv_order_no = (TextView) view.findViewById(R.id.tv_order_no);
        viewHolder.tv_status = (TextView) view.findViewById(R.id.tv_status);

        final PurchaseOderBean purchaseOderBean = list.get(position);
        viewHolder.btn_one.setVisibility(View.VISIBLE);
        viewHolder.btn_two.setVisibility(View.VISIBLE);
        viewHolder.tv_order_no.setText("订单编号：" + purchaseOderBean.getOrder_num());
        if (purchaseOderBean.getCustomer_service_status() == 0) {
//        【1.待付款,2.待发货,3.已发货,4.交易完成,5.已取消,6.已退货,7退款中 8退款失败】"
        switch (purchaseOderBean.getOrder_state()) {
            case 1:
                viewHolder.tv_status.setText("待付款");
                viewHolder.btn_one.setText("取消订单");
                viewHolder.btn_two.setText("立即支付");
                viewHolder.tv_rest_of.setVisibility(View.GONE);
                // if
                // (Tools.getDateformat(orderModel.getCreate_date()).equals("已经超时了"))
                // {
                // oneCallBack.onOne(1, position);
                // } else {
                // viewHolder.tv_rest_of.setText(Tools.getDateformat(orderModel.getCreate_date()));
                // }

                break;
            case 2:
                viewHolder.tv_status.setText("待发货");
                viewHolder.btn_one.setText("取消订单");
                viewHolder.btn_two.setVisibility(View.GONE);
                viewHolder.tv_rest_of.setVisibility(View.GONE);
                break;
            case 3:
                viewHolder.tv_status.setText("已发货");
                viewHolder.tv_rest_of.setVisibility(View.GONE);
                // 判断是否有快递单号 是否显示查看物流
                viewHolder.btn_one.setVisibility(View.GONE);
                viewHolder.btn_two.setText("确认收货");
                break;
            case 4:
                viewHolder.tv_status.setText("交易完成");
                viewHolder.tv_rest_of.setVisibility(View.GONE);
                // // 判断是否有快递单号 是否显示查看物流
                // if (orderModel.getExpressNum() == null ||
                // orderModel.getExpressNum().isEmpty()) {
                // viewHolder.btn_one.setVisibility(View.GONE);
                // viewHolder.btn_two.setVisibility(View.GONE);
                // } else {
                // viewHolder.btn_one.setText("查看物流");
                // viewHolder.btn_one.setVisibility(View.GONE);
                // viewHolder.btn_two.setVisibility(View.GONE);
                // }
                viewHolder.btn_one.setVisibility(View.VISIBLE);
                viewHolder.btn_one.setText("删除订单");
                viewHolder.btn_two.setVisibility(View.GONE);
                break;
            case 5:
                viewHolder.tv_status.setText("已取消");
                viewHolder.tv_rest_of.setVisibility(View.GONE);

                viewHolder.btn_one.setVisibility(View.VISIBLE);
                viewHolder.btn_one.setText("删除订单");
                viewHolder.btn_two.setVisibility(View.GONE);
                break;
            case 6:
                viewHolder.tv_status.setText("已退款");
                viewHolder.tv_rest_of.setVisibility(View.GONE);

                viewHolder.btn_one.setVisibility(View.VISIBLE);
                viewHolder.btn_one.setText("删除订单");
                viewHolder.btn_two.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        } else {
            switch (purchaseOderBean.getCustomer_service_status()) {
                case 1:
                    viewHolder.tv_status.setText("审核中");
                    break;
                case 2:
                    // 1.审核中2.审核不通过3退款中4退款成功5退款失败6取消售后
                    viewHolder.tv_status.setText("审核不通过");
                    break;
                case 3:
                    viewHolder.tv_status.setText("退款中");
                    break;
                case 4:
                    viewHolder.tv_status.setText("退款成功");
                    break;
                case 5:
                    viewHolder.tv_status.setText("退款失败");
                    break;
                case 6:
                    viewHolder.tv_status.setText("取消售后申请");
                    break;
                default:
                    break;
            }
            viewHolder.btn_one.setVisibility(View.VISIBLE);
            viewHolder.btn_one.setText("删除订单");
            viewHolder.btn_two.setVisibility(View.GONE);
            viewHolder.tv_rest_of.setVisibility(View.GONE);
        }

        String spec = purchaseOderBean.getCommodity_json();
        List<ShopingBean> suplierList = JSON.parseArray(spec, ShopingBean.class);

        // 商品数量大于0
        if (suplierList != null && suplierList.size() > 0) {
            int number = 0;
            for (ShopingBean shopingBean : suplierList) {
                number = shopingBean.getMsg_count() + number;
            }
            View lview = null;
            if (suplierList.size() == 1) {
                lview = LayoutInflater.from(mContext).inflate(R.layout.buy_item, null);
                TextView tv_name = (TextView) lview.findViewById(R.id.tv_name);
                TextView tv_price = (TextView) lview.findViewById(R.id.tv_pirce);
                TextView tv_number = (TextView) lview.findViewById(R.id.tv_number);
                ImageView imageView = (ImageView) lview.findViewById(R.id.iv_img);
                ShopingBean msgmodel = suplierList.get(0);
                imageLoader.displayImage(Constants.imageUrl + msgmodel.getCommodity_icon(), imageView, options);
                tv_name.setText(msgmodel.getCommodity_name());
                // tv_specNames.setText(msgmodel.getCommodityName());
                tv_price.setText("¥" + Tools.getFenYuan(msgmodel.getPrice_high()));
                tv_number.setText("x" + msgmodel.getMsg_count());
            } else if (suplierList.size() == 2) {
                lview = LayoutInflater.from(mContext).inflate(R.layout.buy_img_item, null);
                TextView tv_number = (TextView) lview.findViewById(R.id.tv_number);
                ImageView imageView = (ImageView) lview.findViewById(R.id.iv_img);
                ImageView imageView2 = (ImageView) lview.findViewById(R.id.iv_img2);
                imageLoader.displayImage(Constants.imageUrl + suplierList.get(0).getCommodity_icon(), imageView,
                        options);
                imageLoader.displayImage(Constants.imageUrl + suplierList.get(1).getCommodity_icon(), imageView2,
                        options);
                tv_number.setText("共" + number + "件商品");
            } else if (suplierList.size() >= 3) {
                lview = LayoutInflater.from(mContext).inflate(R.layout.buy_img_item, null);
                TextView tv_number = (TextView) lview.findViewById(R.id.tv_number);
                ImageView imageView = (ImageView) lview.findViewById(R.id.iv_img);
                ImageView imageView2 = (ImageView) lview.findViewById(R.id.iv_img2);
                ImageView imageView3 = (ImageView) lview.findViewById(R.id.iv_img3);
                imageLoader.displayImage(Constants.imageUrl + suplierList.get(0).getCommodity_icon(), imageView,
                        options);
                imageLoader.displayImage(Constants.imageUrl + suplierList.get(1).getCommodity_icon(), imageView2,
                        options);
                imageLoader.displayImage(Constants.imageUrl + suplierList.get(2).getCommodity_icon(), imageView3,
                        options);
                tv_number.setText("共" + number + "件商品");
            }
            viewHolder.listView.addView(lview);
            viewHolder.tv_total_of.setText("共" + number + "件商品 合计：￥" + df.format(Tools.getFenYuan(purchaseOderBean.getTotal_price())));
        }

        // 按钮1
        viewHolder.btn_one.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (purchaseOderBean.getOrder_state()) {
                    // 取消订单
                    case 1:
                    case 2:
                        AlertDialog.Builder builder = new Builder(mContext);
                        builder.setMessage("您确定要取消订单吗？");
                        builder.setTitle("提示");
                        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                oneCallBack.onOne(1, position);
                            }
                        });
                        builder.create().show();
                        break;
                    case 3:
                        break;
                    case 4:
                        // Intent intent = new Intent(mContext,
                        // LogisticsDetails.class);
                        // intent.putExtra("expressNum",
                        // orderModel.getExpressNum());
                        // intent.putExtra("deliveryName",
                        // orderModel.getDeliveryName());
                        // mContext.startActivity(intent);
                    case 5:
                    case 6:
                        // 删除订单操作
                        AlertDialog.Builder builder2 = new Builder(mContext);
                        builder2.setMessage("您确定要删除该订单?");
                        builder2.setTitle("提示");
                        builder2.setPositiveButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder2.setNegativeButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                oneCallBack.onOne(2, position);
                            }
                        });
                        builder2.create().show();
                        break;

                    default:
                        break;
                }
            }
        });
        viewHolder.btn_two.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 立即支付
                switch (purchaseOderBean.getOrder_state()) {
                    case 1:
                        Intent intent = new
                                Intent(mContext.getApplicationContext(),
                                PurchaseOrderDetailsActivity.class);
                        intent.putExtra("mPurchaseOderBean", purchaseOderBean);
                        mContext.startActivity(intent);
                        break;
                    // 确认收货
                    case 3:
                        AlertDialog.Builder builder = new Builder(mContext);
                        builder.setMessage("您确定已经收到货物吗?");
                        builder.setTitle("提示");
                        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                twoCallBack.onTwo(1, position);
                            }
                        });
                        builder.create().show();
                        break;
                    default:
                        break;
                }
            }
        });
        return view;

    }

    // 按钮1
    public interface OneCallBack {
        /**
         * int type 类型 1为取消订单 2为删除订单 int position 定位
         */
        void onOne(int type, int position);
    }

    // 按钮1
    public interface TwoCallBack {
        void onTwo(int type, int position);
    }

    final static class ViewHolder {
        /**
         * 总计 按钮1 按钮2 剩余支付时间
         */
        TextView tv_total_of, tv_order_no, tv_status, tv_rest_of;
        Button btn_one, btn_two;
        LinearLayout listView;

    }

}