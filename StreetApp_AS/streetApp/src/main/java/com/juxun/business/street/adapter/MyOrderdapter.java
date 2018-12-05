package com.juxun.business.street.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iboxpay.print.IPrintJobStatusCallback;
import com.iboxpay.print.PrintManager;
import com.iboxpay.print.model.CharacterParams;
import com.iboxpay.print.model.PrintItemJobInfo;
import com.iboxpay.print.model.PrintJobInfo;
import com.juxun.business.street.activity.CancelOrderActivity;
import com.juxun.business.street.activity.MyOrderDetailsActivity;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.Msgmodel;
import com.juxun.business.street.bean.OrderModel;
import com.juxun.business.street.bean.ParseModel;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.SavePreferencesData;
import com.juxun.business.street.util.Tools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

/**
 * @author Administrator 分类列表
 */
public class MyOrderdapter extends BaseAdapter {
    private List<OrderModel> list = null;
    private Context mContext;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();
    String member_id;
    private PrintManager mPrintManager;
    private SavePreferencesData mSavePreferencesData;
    DecimalFormat df = new java.text.DecimalFormat("0.00");
    private int mOrderTypeFrom; //订单状态【1.待付款,2.待发货,3.已发货,4.交易完成,5.已取消,6.已退货】0全部

    public MyOrderdapter(Context mContext, List<OrderModel> list,
                         onOperateOrder orderListener) {
        this.mContext = mContext;
        this.list = list;
//        mPrintManager = (PrintManager) mContext
//                .getSystemService("iboxpay_print");
        mSavePreferencesData = new SavePreferencesData(mContext);
        this.orderListener = orderListener;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     * @param orderTypeFrom
     */
    public void updateListView(List<OrderModel> list, int orderTypeFrom) {
        this.mOrderTypeFrom = orderTypeFrom;
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

    public int totalPrice;
    private onOperateOrder orderListener;

    public View getView(final int position, View view, ViewGroup arg2) {
        /*
         * viewHolder
		 */
        ViewHolder viewHolder = new ViewHolder();
        view = LayoutInflater.from(mContext).inflate(R.layout.myorder_item,
                null);
        viewHolder.tv_ordernumber = (TextView) view
                .findViewById(R.id.tv_ordernumber);
        viewHolder.tv_status = (TextView) view.findViewById(R.id.tv_status);
        viewHolder.tv_total_price = (TextView) view
                .findViewById(R.id.tv_total_price);
        viewHolder.btn_one = (Button) view.findViewById(R.id.btn_one);
        viewHolder.btn_two = (Button) view.findViewById(R.id.btn_two);
        viewHolder.btn_three = (Button) view.findViewById(R.id.btn_three);
        viewHolder.listView = (LinearLayout) view.findViewById(R.id.lv_list);
        viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
        viewHolder.tv_number = (TextView) view.findViewById(R.id.tv_number);
        viewHolder.tv_yunfei = (TextView) view.findViewById(R.id.tv_yunfei);
        viewHolder.tv_youhui = (TextView) view.findViewById(R.id.tv_youhui);
        viewHolder.iv_callphone = (ImageView) view
                .findViewById(R.id.iv_callphone);
        /*
         * 数据绑定
		 */
        final OrderModel orderModel = list.get(position);
        // 订单编号
        viewHolder.tv_ordernumber.setText("订单编号：" + orderModel.getOrder_num());
        // 订单状态与按钮显隐
        String statu = "";
        viewHolder.btn_one.setVisibility(View.GONE);
        viewHolder.btn_two.setVisibility(View.GONE);
        viewHolder.btn_three.setVisibility(View.GONE);
        switch (orderModel.getOrder_state()) {
            case 1:
                statu = "待付款";

                viewHolder.btn_two.setText("打印订单");
                break;
            case 2:
                statu = "待发货";
                viewHolder.btn_one.setVisibility(View.VISIBLE);

                viewHolder.btn_three.setVisibility(View.VISIBLE);
                viewHolder.btn_one.setText("发货");
                viewHolder.btn_two.setText("打印订单");
                viewHolder.btn_three.setText("取消订单");
                break;
            case 3:
                statu = "已发货";
                // delivery_type Integer 快递类型【1.快递发货,2.到店自取】
                viewHolder.btn_two.setText("打印订单");
                break;
            case 4:
                statu = "已完成";

                viewHolder.btn_two.setText("打印订单");

                break;
            case 5:
                statu = "已取消";

                viewHolder.btn_two.setText("打印订单");

                break;
            case 6:
                statu = "已退货 ";
                break;
            case 7:
                statu = "退款中 ";

                viewHolder.btn_two.setText("打印订单");

                break;
            case 8:
                statu = "已退款 ";

                viewHolder.btn_two.setText("打印订单");

                break;
            default:
                break;
        }
        viewHolder.tv_status.setText(statu);
        // item小结
        totalPrice = orderModel.getTotal_price()
                + orderModel.getDelivery_price() - orderModel.getRedpacket_price()
                - orderModel.getGold_price();
        viewHolder.tv_total_price.setText("￥" + Tools.getFenYuan(totalPrice));
        viewHolder.tv_yunfei.setText("(含运费￥" + Tools.getFenYuan(orderModel.getDelivery_price())
                + ")");
        viewHolder.tv_youhui.setText("红包优惠￥" + Tools.getFenYuan(orderModel.getRedpacket_price())
                + "   e蜂币抵扣￥" + Tools.getFenYuan(orderModel.getGold_price()));
        // 订单中商品
        String spec = orderModel.getSpec();
        List<Msgmodel> msgmodels = new ArrayList<Msgmodel>();
        try {
            msgmodels = new ParseModel().getMsgmodel(spec);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (msgmodels.size() > 0) { // 商品数量大于0
            int number = 0;
            for (Msgmodel msgmodel : msgmodels) {
                number = msgmodel.getGoodsCount() + number;
            }
            View lview = null;
            if (msgmodels.size() == 1) {
                lview = LayoutInflater.from(mContext).inflate(
                        R.layout.buy_item, null);
                TextView tv_name = (TextView) lview.findViewById(R.id.tv_name);
                TextView tv_price = (TextView) lview
                        .findViewById(R.id.tv_pirce);
                TextView tv_number = (TextView) lview
                        .findViewById(R.id.tv_number);
                // TextView tv_specNames = (TextView)
                // lview.findViewById(R.id.tv_specNames);
                ImageView imageView = (ImageView) lview
                        .findViewById(R.id.iv_img);
                Msgmodel msgmodel = msgmodels.get(0);
                imageLoader.displayImage(
                        Constants.imageUrl + msgmodel.getCommodityICon(),
                        imageView, options);
                tv_name.setText(msgmodel.getCommodityName());
                // tv_specNames.setText(msgmodel.getSpecNames());
                tv_price.setText("¥" + Tools.getFenYuan(msgmodel.getPrice()));
                tv_number.setText("x" + msgmodel.getGoodsCount());
            } else if (msgmodels.size() == 2) {
                lview = LayoutInflater.from(mContext).inflate(
                        R.layout.buy_img_item, null);
                TextView tv_number = (TextView) lview
                        .findViewById(R.id.tv_number);
                ImageView imageView = (ImageView) lview
                        .findViewById(R.id.iv_img);
                ImageView imageView2 = (ImageView) lview
                        .findViewById(R.id.iv_img2);
                imageLoader.displayImage(Constants.imageUrl
                                + msgmodels.get(0).getCommodityICon(), imageView,
                        options);
                imageLoader.displayImage(Constants.imageUrl
                                + msgmodels.get(1).getCommodityICon(), imageView2,
                        options);
                tv_number.setText("共" + number + "件商品");
            } else if (msgmodels.size() >= 3) {
                lview = LayoutInflater.from(mContext).inflate(
                        R.layout.buy_img_item, null);
                TextView tv_number = (TextView) lview
                        .findViewById(R.id.tv_number);
                ImageView imageView = (ImageView) lview
                        .findViewById(R.id.iv_img);
                ImageView imageView2 = (ImageView) lview
                        .findViewById(R.id.iv_img2);
                ImageView imageView3 = (ImageView) lview
                        .findViewById(R.id.iv_img3);
                imageLoader.displayImage(Constants.imageUrl
                                + msgmodels.get(0).getCommodityICon(), imageView,
                        options);
                imageLoader.displayImage(Constants.imageUrl
                                + msgmodels.get(1).getCommodityICon(), imageView2,
                        options);
                imageLoader.displayImage(Constants.imageUrl
                                + msgmodels.get(2).getCommodityICon(), imageView3,
                        options);
                tv_number.setText("共" + number + "件商品");
            }
            viewHolder.listView.addView(lview);
            viewHolder.tv_number.setText("共" + number + "件商品   合计：");
        }
        /*
         * 点击事件
		 */
        viewHolder.listView.setOnClickListener(new OnClickListener() { // item跳转订单详情

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext
                        .getApplicationContext(), MyOrderDetailsActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("orderModel", orderModel);
                intent.putExtras(mBundle);
                mContext.startActivity(intent);
            }
        });
        viewHolder.btn_one.setOnClickListener(new OnClickListener() { // 按钮1

            @Override
            public void onClick(View v) {
                switch (orderModel.getOrder_state()) {
                    // 发货
                    case 2:
                        // Intent intent = new Intent(mContext
                        // .getApplicationContext(),
                        // MyOrderDetails.class);
                        // Bundle mBundle = new Bundle();
                        // mBundle.putSerializable("orderModel",
                        // orderModel);
                        // intent.putExtras(mBundle);
                        // mContext.startActivity(intent);

                        orderListener.sendOrder(list.get(position).getId()
                                + "", position, mOrderTypeFrom); // 全部列表中的发货
                        break;
                    default:
                        break;
                }
            }
        });
        viewHolder.btn_two.setOnClickListener(new OnClickListener() { // 按钮2

            @Override
            public void onClick(View v) {
//                if (mSavePreferencesData.getBooleanData("isSn")) {
//                    // 打印订单
//                    mPrintManager.printLocaleJob(
//                            createPrintReceiptTask222(orderModel),
//                            mTaskCallback);
//                } else {
//                    Toast.makeText(mContext, "此设备不支持打印功能", 1).show();
//                }
            }
        });
        viewHolder.btn_three.setOnClickListener(new OnClickListener() { // 按钮3

            @Override
            public void onClick(View v) {

                switch (orderModel.getOrder_state()) {
                    case 2: // 待发货状态下有取消订单操作
                        // 取消订单操作
                        Intent intent = new Intent(mContext,
                                CancelOrderActivity.class);
                        intent.putExtra("id", orderModel.getId() + "");
                        mContext.startActivity(intent);
                        break;
                    case 4: // 已完成
                    case 5: // 已取消
                    case 8: // 已退款
                    case 7: // 退款中
                        deleteOrderDialog(position, mOrderTypeFrom);
                        break;
                    default:
                        break;
                }
            }
        });

        // viewHolder.iv_callphone.setOnClickListener(new OnClickListener() { //
        // 商家电话拨打
        //
        // @Override
        // public void onClick(View arg0) {
        // // 用intent启动拨打电话
        // Intent intent = new Intent(Intent.ACTION_CALL,
        // Uri.parse("tel:"
        // + orderModel.getCommunity_phone()));
        // mContext.startActivity(intent);
        // }
        // });

        return view;
    }

    public interface onOperateOrder {
        // 删除订单
        void deleteOrder(String string, int position, int orderTypeFrom);

        // 订单发货
        void sendOrder(String string, int position, int orderTypeFrom);
    }

    private void deleteOrderDialog(final int position, final int orderTypeFrom) {
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
                // 请求删除，在onResponse得到删除成功信息后list.remove()操作刷新列表
                orderListener.deleteOrder(list.get(position).getId() + "",
                        position, orderTypeFrom);
            }
        });
        builder2.create().show();
    }

    private IPrintJobStatusCallback mTaskCallback = new IPrintJobStatusCallback.Stub() {
        @Override
        public void onPrintJobStatusChange(int status, String taskId)
                throws RemoteException {
            Log.i("信息", "onPrintTaskStatusChange status = " + status
                    + "taskId=" + taskId);
        }
    };

    final static class ViewHolder {
        /**
         * 订单号 状态 名称 价格 图片 数量 总价格 按钮1 按钮2
         */
        TextView tv_ordernumber, tv_status, tv_total_price;
        Button btn_one, btn_two, btn_three;
        LinearLayout listView;
        ImageView iv_callphone;
        // 运费 价格 数量
        TextView tv_yunfei, tv_price, tv_number, tv_youhui;
    }

    // add by zhengzhenxing
    private PrintJobInfo createPrintReceiptTask222(OrderModel odModel) {
        PrintJobInfo receiptTask = new PrintJobInfo();
        try {
            receiptTask.addPrintItemJobTask(new PrintItemJobInfo(
                    "   商品订单      \n", new CharacterParams(2, 2)));
            receiptTask.addPrintItemJobTask(new PrintItemJobInfo(
                    "------------------------------\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(new PrintItemJobInfo("  客户名称:"
                    + odModel.getConsignee_name() + "\n\n",
                    new CharacterParams()));
            receiptTask.addPrintItemJobTask(new PrintItemJobInfo("  联系电话:"
                    + odModel.getConsignee_phone() + "\n\n",
                    new CharacterParams()));
            receiptTask.addPrintItemJobTask(new PrintItemJobInfo("  地址:"
                    + odModel.getConsignee_address() + "\n\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(new PrintItemJobInfo(
                    "------------------------------\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(new PrintItemJobInfo("  商品清单:"
                    + "\n\n", new CharacterParams()));
            List<Msgmodel> msgmodels = new ArrayList<Msgmodel>();
            try {
                msgmodels = new ParseModel().getMsgmodel(odModel.getSpec());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < msgmodels.size(); i++) {
                receiptTask.addPrintItemJobTask(new PrintItemJobInfo("  "
                        + msgmodels.get(i).getCommodityName() + "("
                        + msgmodels.get(i).getSpecNames() + ")" + " x "
                        + msgmodels.get(i).getGoodsCount() + "\n\n",
                        new CharacterParams()));
            }
            receiptTask.addPrintItemJobTask(new PrintItemJobInfo(
                    "------------------------------\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(new PrintItemJobInfo("  订单单号:"
                    + odModel.getOrder_id() + "\n\n", new CharacterParams()));
            receiptTask
                    .addPrintItemJobTask(new PrintItemJobInfo("  下单时间:"
                            + odModel.getCreate_date() + "\n\n",
                            new CharacterParams()));

            receiptTask
                    .addPrintItemJobTask(new PrintItemJobInfo("  付款时间:"
                            + odModel.getPay_end_time() + "\n\n",
                            new CharacterParams()));
            if (odModel.getPay_type() == 1) {
                receiptTask.addPrintItemJobTask(new PrintItemJobInfo(
                        "         支付方式: 微信支付" + "\n\n", new CharacterParams()));
            } else {
                receiptTask
                        .addPrintItemJobTask(new PrintItemJobInfo(
                                "         支付方式: 支付宝支付" + "\n\n",
                                new CharacterParams()));
            }
            receiptTask.addPrintItemJobTask(new PrintItemJobInfo(
                    "------------------------------\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(new PrintItemJobInfo("  RMB:"
                    + odModel.getTotal_price() + "元\n\n\n\n\n\n",
                    new CharacterParams(2, 2)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiptTask;
    }
}