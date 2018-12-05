package com.juxun.business.street.activity;

/**
 * 我的订单详情
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.iboxpay.print.IPrintJobStatusCallback;
import com.iboxpay.print.PrintManager;
import com.iboxpay.print.model.CharacterParams;
import com.iboxpay.print.model.PrintItemJobInfo;
import com.iboxpay.print.model.PrintJobInfo;
import com.juxun.business.street.adapter.MsgmodelsAdapter;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.Msgmodel;
import com.juxun.business.street.bean.OrderModel;
import com.juxun.business.street.bean.ParseModel;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.ParamTools;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyOrderDetailsActivity extends BaseActivity implements OnClickListener {
    /**
     * ----------------------- 取消订单理由--------------------------------
     **/
    @ViewInject(R.id.ll_cancel_reason)
    private LinearLayout ll_cancel_reason;// 拒单理由整个状态条
    @ViewInject(R.id.tv_top_reason)
    private TextView tv_top_reason;// 取消理由
    @ViewInject(R.id.tv_top_state)
    private TextView tv_top_state;// 取消成功后的状态

    @ViewInject(R.id.btn_back)
    private ImageView btn_back;// 后退按键
    @ViewInject(R.id.tv_print)
    private TextView tv_print;// 后退按键

    /**
     * ----------------------- 商品清单--------------------------------
     **/
    @ViewInject(R.id.ll_list)
    private LinearLayout ll_list;// 商品列表
    @ViewInject(R.id.tv_describe)
    private TextView tv_describe;// 共1件商品 运费10元 优惠5元 合计
    @ViewInject(R.id.tv_total_price)
    private TextView tv_total_price;// 总价 红色的
    /**
     * ----------------------- 订单信息--------------------------------
     **/
    @ViewInject(R.id.tv_jiaoyi_number)
    private TextView tv_jiaoyi_number;// 订单编号
    @ViewInject(R.id.tv_state)
    private TextView tv_state;// 资金状态
    @ViewInject(R.id.tv_paytype)
    private TextView tv_paytype;// 支付方式
    @ViewInject(R.id.tv_ordercreateTime)
    private TextView tv_ordercreateTime;// 下单时间
    @ViewInject(R.id.tv_order_end_date)
    private TextView tv_order_end_date;// 付款时间
    @ViewInject(R.id.tv_delivery_date)
    private TextView tv_delivery_date;// 发货时间
    @ViewInject(R.id.tv_deal_date)
    private TextView tv_deal_date;// 成交时间
    /**
     * -----------------------收货信息--------------------------------
     **/
    @ViewInject(R.id.ll_adds)
    private LinearLayout ll_adds;//
    @ViewInject(R.id.tv_name)
    private TextView tv_name;// 收货人
    @ViewInject(R.id.tv_ipone)
    private TextView tv_ipone;// 联系电话
    @ViewInject(R.id.tv_adds)
    private TextView tv_adds;// 收货地址
    @ViewInject(R.id.tv_note)
    private TextView tv_note;// 备注
    @ViewInject(R.id.ll_note)
    private LinearLayout ll_note;// 备注布局
    @ViewInject(R.id.tv_heji)
    private TextView tv_heji;// 合计
    @ViewInject(R.id.btn_one)
    private Button btn_one;// 按钮
    @ViewInject(R.id.btn_two)
    private Button btn_two;// 按钮2
    @ViewInject(R.id.tv_youhui)
    private TextView tv_youhui;// 优惠
    /**
     * 初始化数据 状态 订单金额 运费 收件人 手机 地址
     */
    OrderModel mallOrder;
    ListView lv_list;
    MsgmodelsAdapter msgmodelsAdapter;
    private PrintManager mPrintManager;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();
    DecimalFormat df = new java.text.DecimalFormat("0.00");

    public int totalPrice;
    private OrderModel orderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_details);
        ViewUtils.inject(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderInfo();
    }

    private void getOrderInfo() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("order_id", orderModel.getId() + "");
        //  map.put("order_num", orderModel.getMember_id() + "");
        map.put("auth_token", partnerBean.getAuth_token());// 管理员设备id
        mQueue.add(ParamTools
                .packParam(Constants.getOrderInfo, this, this, map));
    }

    public void initView() {
        orderModel = (OrderModel) getIntent()
                .getSerializableExtra("orderModel");
        // ui
        tv_jiaoyi_number = (TextView) findViewById(R.id.tv_jiaoyi_number);
        tv_ordercreateTime = (TextView) findViewById(R.id.tv_ordercreateTime);
        tv_order_end_date = (TextView) findViewById(R.id.tv_order_end_date);
        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        tv_print.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    private void initDate() {
        /*
         * 订单状态的管理
		 */
        bindState();
        /*
         * 中间的商品信息
		 */
        String spec = mallOrder.getSpec();
        List<Msgmodel> msgmodels = new ArrayList<Msgmodel>();
        try {
            msgmodels = new ParseModel().getMsgmodel(spec);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (msgmodels.size() > 0) {
            int number = 0;
            ll_list.removeAllViews();
            for (int i = 0; i < msgmodels.size(); i++) {
                number = msgmodels.get(i).getGoodsCount() + number;
                View lview = LayoutInflater.from(MyOrderDetailsActivity.this).inflate(
                        R.layout.buy_item, null);
                TextView tv_name = (TextView) lview.findViewById(R.id.tv_name);
                TextView tv_price = (TextView) lview
                        .findViewById(R.id.tv_pirce);
                TextView tv_number = (TextView) lview
                        .findViewById(R.id.tv_number);
                ImageView imageView = (ImageView) lview
                        .findViewById(R.id.iv_img);
                Msgmodel msgmodel = msgmodels.get(i);
                imageLoader.displayImage(
                        Constants.imageUrl + msgmodel.getCommodityICon(),
                        imageView, options);
                tv_name.setText(msgmodel.getCommodityName());
                tv_price.setText("¥" + Tools.getFenYuan(msgmodel.getPrice()));
                tv_number.setText("x" + msgmodel.getGoodsCount());
                ll_list.addView(lview);
            }
            tv_describe.setText("共" + number + "件商品   运费 ￥"
                    + Tools.getFenYuan(mallOrder.getDelivery_price()) + "合计：");
            tv_youhui.setText("红包优惠￥" + Tools.getFenYuan(mallOrder.getRedpacket_price()) + "   e蜂币抵扣￥"
                    + Tools.getFenYuan(mallOrder.getGold_price()));
        }
        totalPrice = mallOrder.getTotal_price() + mallOrder.getDelivery_price()
                - mallOrder.getGold_price() - mallOrder.getRedpacket_price();

        tv_total_price.setText("￥" + Tools.getFenYuan(totalPrice));
        tv_heji.setText("￥" + Tools.getFenYuan(totalPrice));

        tv_jiaoyi_number.setText("订单编号:" + mallOrder.getOrder_num());
        // 订单状态【1.待付款,2.待发货,3.已发货,4.交易完成,5.已取消,6.已退货】
        String[] orderStates = {"", "待付款", "待发货", "已发货", "已完成", "已取消", "已退货",
                "退款中", "已退款"};
        tv_state.setText(orderStates[mallOrder.getOrder_state()]);
        // 支付方式 //支付类型 1.微信支付.2.支付宝支付 3、盒子支付
        String[] payTypes = {"", "微信支付", "支付宝支付", "盒子支付"};
        tv_paytype.setText("支付方式：" + payTypes[mallOrder.getPay_type()]);
        tv_ordercreateTime.setText("下单时间：" + Tools.getDateformat2(mallOrder.getCreate_date()));
        tv_order_end_date.setText("付款时间：" + Tools.getDateformat2(mallOrder.getPay_finish_time()));
        if (mallOrder.getReal_delivery_time() != null) {
            tv_delivery_date.setText("发货时间：" + Tools.getDateformat2(Long.valueOf(mallOrder.getReal_delivery_time().toString())));
        } else {
            tv_delivery_date.setText("发货时间：");
        }

        tv_deal_date.setText("成交时间：" + Tools.getDateformat2(mallOrder.getConfirm_finish_time()));

		/*
         * 其它类型的处理
		 */
        if (mallOrder.getOrder_type() == 6) {
            // 6为自助订单 没有地址
            ll_adds.setVisibility(View.GONE);
        } else {
            ll_adds.setVisibility(View.VISIBLE);
            tv_name.setText(mallOrder.getConsignee_name());
            tv_ipone.setText(mallOrder.getConsignee_phone());
            tv_adds.setText(mallOrder.getConsignee_address());
        }

        if (mallOrder.getBuyer_remark().length() > 0) {
            ll_note.setVisibility(View.VISIBLE);
            tv_note.setText(mallOrder.getBuyer_remark());
        }
    }

    /* 发货 */
    private void sendOut() {
        // orderId Integer 订单id
        // community_id Integer 社区id
        // delivery_type Integer 快递类型1.快递发货,2.到店自取
        Map<String, String> map = new HashMap<String, String>();
        map.put("order_id", mallOrder.getId() + "");
        map.put("auth_token", partnerBean.getAuth_token());// 管理员设备id
        mQueue.add(ParamTools.packParam(Constants.sendOut, this, this, map));
    }

    private void bindState() {
        String statu = "";
        switch (mallOrder.getOrder_state()) {
            case 1:
                statu = "待付款";
                btn_one.setVisibility(View.GONE);
                btn_two.setVisibility(View.GONE);
                break;
            case 2:
                statu = "待发货";
                btn_one.setText("发货");
                btn_two.setText("取消订单");
                break;
            case 3:
                statu = "已发货";
                // delivery_type Integer 快递类型【1.快递发货,2.到店自取】
                btn_one.setVisibility(View.GONE);
                btn_two.setVisibility(View.GONE);
                break;
            case 4:
                statu = "已完成";
                btn_one.setVisibility(View.GONE);
                btn_two.setVisibility(View.GONE);
                break;
            case 5:
                statu = "已取消";
                btn_one.setVisibility(View.GONE);
                btn_two.setVisibility(View.GONE);
                break;
            case 6:
                statu = "已退货";
                btn_one.setVisibility(View.GONE);
                btn_two.setVisibility(View.GONE);
                break;
            case 7:
                statu = "退款中 ";
                btn_one.setVisibility(View.GONE);
                btn_two.setVisibility(View.GONE);
                ll_cancel_reason.setVisibility(View.VISIBLE);
                tv_top_reason.setText("取消理由："
                        + mallOrder.getReject_document_remark());
                tv_top_state.setText("退款中");
                break;
            case 8:
                statu = "已退款 ";
                btn_one.setVisibility(View.GONE);
                btn_two.setVisibility(View.GONE);
                ll_cancel_reason.setVisibility(View.VISIBLE);
                tv_top_state.setText("已退款");
                tv_top_reason.setText("取消理由："
                        + mallOrder.getReject_document_remark());
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back:
                MyOrderDetailsActivity.this.finish();
                break;
            case R.id.btn_one:
                switch (mallOrder.getOrder_state()) {
                    // 发货
                    case 2:
                        sendOut();
                        break;
                    default:
                        break;
                }
                break;
            case R.id.btn_two:
                switch (mallOrder.getOrder_state()) {
                    case 2: // 待发货
                        // 取消订单操作
                        Intent intent = new Intent(this, CancelOrderActivity.class);
                        intent.putExtra("id", mallOrder.getId() + "");
                        startActivity(intent);
                        break;
                    case 4: // 已完成
                    case 5:// 已取消
                    case 8: // 已退款
                    case 7: // 退款中
                        // 删除订单操作
                        deleteOrderDialog();
                        break;
                    default:
                        break;
                }
                break;
            case R.id.tv_print:
//                // 打印订单
//                if (mSavePreferencesData.getBooleanData("isSn")) {
//                    // 打印订单
//                    mPrintManager.printLocaleJob(
//                            createPrintReceiptTask222(orderModel), mTaskCallback);
//                } else {
//                    Toast.makeText(this, "此设备不支持打印功能", 1).show();
//                }
//                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void deleteOrderDialog() {
        AlertDialog.Builder builder2 = new Builder(this);
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
                // 服务器删除，删除后回到订单页面刷新列表
                deleteOrder(mallOrder.getId() + "");
            }
        });
        builder2.create().show();
    }

    public void deleteOrder(String order_id) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_token", partnerBean.getAuth_token());
        map.put("order_id", order_id);
        mQueue.add(ParamTools.packParam(Constants.orDeleteOrder, this, this,
                map));
    }

    private IPrintJobStatusCallback mTaskCallback = new IPrintJobStatusCallback.Stub() {
        @Override
        public void onPrintJobStatusChange(int status, String taskId)
                throws RemoteException {
            Log.i("信息", "onPrintTaskStatusChange status = " + status
                    + "taskId=" + taskId);
        }
    };

    /**
     * scrollview嵌套listview显示不全解决 * * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

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
                    .addPrintItemJobTask(new PrintItemJobInfo("   下单时间:"
                            + odModel.getCreate_date() + "\n\n",
                            new CharacterParams()));
            receiptTask
                    .addPrintItemJobTask(new PrintItemJobInfo("   付款时间:"
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

    @Override
    public void onResponse(String response, String url) {
        dismissLoading();
        try {
            JSONObject json = new JSONObject(response);
            int stauts = json.optInt("status");
            if (stauts == 0) {
                if (url.contains(Constants.getOrderInfo)) {
                    mallOrder = JSON.parseObject(json.getString("result"),
                            OrderModel.class);
                    if (mallOrder != null) {
                        initDate();
                    }
                } else if (url.contains(Constants.sendOut)) {
                    Tools.showToast(this, "发货成功");
                    // delivery_type Integer 快递类型【1.快递发货,2.到店自取】
                    btn_one.setVisibility(View.GONE);
                    btn_two.setText("打印订单");
                    tv_state.setText("已发货");
                    ParseModel.isdate = true;
                } else if (url.contains(Constants.orDeleteOrder)) {
                    Tools.showToast(this, "删除订单成功");
                    ParseModel.isdate = true;
                    finish();
                }
            } else {
                Tools.showToast(this, json.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Tools.showToast(this, R.string.tips_unkown_error);
        }
    }
}