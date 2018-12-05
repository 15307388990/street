package com.juxun.business.street.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.activity.AfterReasonActivity;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.fragment.RequestAfterSaleBean;
import com.juxun.business.street.fragment.ToRefundReasonBean;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.Tools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

public class RequestAfterSaleAdapter extends BaseAdapter {

    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private DecimalFormat mDf;

    private Activity mActivity;
    private List<RequestAfterSaleBean> mList; // 退货集合
    private String mOrder_num; // 订单编号

    private ArrayList<Boolean> mBooleans; // checkbox是否被选中对象
    private int mReQuantity; // itemView中的退货量

    /*
     * footView部分
     */
    private View mFootView;
    private Button mBtnToReason; // 下一步按钮
    private TextView mTvPayway; // 退款方式
    private TextView mTvTotalMoney; // 退款总额显示
    private RequestAfterSaleBean mRequestAfterSaleBean;
    private int mPay_type;
    private String[] mPayTypes;

    public RequestAfterSaleAdapter(Context context, List<RequestAfterSaleBean> afterSaleList, View footView,
                                   String order_num, int pay_type) {
        this.mActivity = (Activity) context;
        this.mList = afterSaleList;
        this.mFootView = footView;
        this.mOrder_num = order_num;
        this.mPay_type = pay_type;
        mImageLoader = ImageLoader.getInstance();
        mOptions = ImageLoaderUtil.getOptions();
        mDf = new java.text.DecimalFormat("0.00");
        mPayTypes = new String[]{"", "微信支付", "支付宝支付", "pos", "货到付款", "白条支付", "余额支付"};
        initBooleans();
        initList();
        initFootViews();
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param order_num
     * @param pay_type  付款方式
     */
    public void updateListView(List<RequestAfterSaleBean> list, String order_num, int pay_type) {
        this.mList = list;
        this.mOrder_num = order_num;
        this.mPay_type = pay_type;
        initBooleans();
        initList();
        notifyDataSetChanged();
    }

    /*
     * 默认初始状态：未选中
     */
    private void initBooleans() {
        mBooleans = new ArrayList<Boolean>();
        for (int i = 0; i < mList.size(); i++) {
            mBooleans.add(false);
        }
    }

    /*
     * 默认显示数量：1
     */
    private void initList() {
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setRefund_goods(1);
        }
    }

    /*
     * 尾部事件处理
     */
    private void initFootViews() {
        mTvPayway = (TextView) mFootView.findViewById(R.id.tv_payway);
        mTvTotalMoney = (TextView) mFootView.findViewById(R.id.tv_money);
        mBtnToReason = (Button) mFootView.findViewById(R.id.btn_next_toreason);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convertView,ViewHolder
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_request_aftersale, null);
            holder.addBox = (CheckBox) convertView.findViewById(R.id.add_chekbox);
            holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.ivAdd = (ImageView) convertView.findViewById(R.id.iv_add);
            holder.ivMin = (ImageView) convertView.findViewById(R.id.iv_min);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvPirce = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvNums = (TextView) convertView.findViewById(R.id.tv_nums);
            holder.tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
            holder.viewDivider = convertView.findViewById(R.id.view_divider);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // itemDiveder，最后一条显示处理
        holder.viewDivider.setVisibility(View.VISIBLE);
        if (position == mList.size() - 1) {
            holder.viewDivider.setVisibility(View.GONE);
        }

        // 获取操作对象
        mRequestAfterSaleBean = mList.get(position);

        // 与退货操作无关:图片，名称，单价
        mImageLoader.displayImage(Constants.imageUrl + mRequestAfterSaleBean.getCommodity_icon(), holder.ivIcon,
                mOptions);
        holder.tvName.setText(mRequestAfterSaleBean.getCommodity_name());
        double unitPirce = Tools.getFenYuan(mRequestAfterSaleBean.getUnit_price());
        holder.tvPirce.setText(unitPirce + "");
        // 与退货操作有关：勾选框，退货量，退货数量，单条退货总价
        holder.addBox.setTag(position);
        holder.addBox.setChecked(mBooleans.get(position));
        mReQuantity = mRequestAfterSaleBean.getRefund_goods();
        holder.tvNumber.setText(mReQuantity + ""); // 选择退换数量
        holder.tvNums.setText("已选择退货 " + mReQuantity + "件,共退款");
        holder.tvMoney.setText("¥" + mDf.format(mReQuantity * unitPirce)); // 单条退换总价

        final int clickPostion = position;
        final ViewHolder fHolder = holder;
        holder.addBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBooleans.set((Integer) buttonView.getTag(), isChecked);
                totalMoney();
            }

        });
        holder.ivMin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                RequestAfterSaleBean saleBean = mList.get(clickPostion);
                // 数量为1的时候不可以操作
                if (saleBean.getRefund_goods() > 1) {
                    saleBean.setRefund_goods(saleBean.getRefund_goods() - 1);
                    itemMoney(fHolder, saleBean, clickPostion);
                    totalMoney();
                }
            }
        });
        holder.ivAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                RequestAfterSaleBean saleBean = mList.get(clickPostion);
                // 数量已经是订单中最大数量，不能再进行操作
                int canReFundGoods = saleBean.getPurchase_quantity() - saleBean.getRefund_commodity_num();
                if (saleBean.getRefund_goods() == canReFundGoods) {
                    Tools.showToast(mActivity, "超过最大限制数量");
                    return;
                } else {
                    saleBean.setRefund_goods(saleBean.getRefund_goods() + 1);
                    itemMoney(fHolder, saleBean, clickPostion);
                    totalMoney();
                }
            }

        });

        totalMoney();
        return convertView;
    }

    /**
     * 总的价格
     */
    protected void totalMoney() {
        ArrayList<RequestAfterSaleBean> hasCheckedList = new ArrayList<RequestAfterSaleBean>();
        double totalPrice = 0;
        for (int i = 0; i < mList.size(); i++) {
            // 需要退换的条目
            RequestAfterSaleBean saleBean = mList.get(i);
            if (mBooleans.get(i)) {
                // 勾选了，但是没有选退货数量，这样等对象不传递过去
                hasCheckedList.add(saleBean);
                totalPrice = totalPrice + saleBean.getRefund_goods() * Tools.getFenYuan(saleBean.getUnit_price());
            }
        }

		/*
         * 只需要商品id，商品数量refund_commodity_num
		 */
        String commodity_json = null;
        if (hasCheckedList.size() != 0) {
            ArrayList<ToRefundReasonBean> toReasonList = new ArrayList<ToRefundReasonBean>();
            for (int i = 0; i < hasCheckedList.size(); i++) {
                ToRefundReasonBean toRefundReasonBean = new ToRefundReasonBean();
                RequestAfterSaleBean requestAfterSaleBean = hasCheckedList.get(i);
                toRefundReasonBean.setSupplier_order_commodity_id(requestAfterSaleBean.getSupplier_order_commodity_id());
                toRefundReasonBean.setRefund_commodity_num(requestAfterSaleBean.getRefund_goods());
                toRefundReasonBean.setCommodity_icon(requestAfterSaleBean.getCommodity_icon());
                toRefundReasonBean.setUnit_price(requestAfterSaleBean.getUnit_price());
                toRefundReasonBean.setCommodity_name(requestAfterSaleBean.getCommodity_name());
                toReasonList.add(toRefundReasonBean);
            }
            commodity_json = JSON.toJSONString(toReasonList);
        }

        mTvPayway.setText(mPayTypes[mPay_type]);
        mTvTotalMoney.setText("¥" + mDf.format(totalPrice));

        final String reasonCommodity_json = commodity_json;
        // 按钮跳转到申请售后的理由页面
        if (totalPrice > 0) {
            mBtnToReason.setEnabled(true);
            mBtnToReason.setBackgroundResource(R.drawable.button_bg1);
            mBtnToReason.setTextColor(mActivity.getResources().getColor(R.color.white));
            mBtnToReason.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, AfterReasonActivity.class);
                    intent.putExtra("order_id", mOrder_num);
                    intent.putExtra("commodity_json", reasonCommodity_json);
                    mActivity.startActivityForResult(intent, 1);
                }
            });
        } else {
            mBtnToReason.setEnabled(false);
            mBtnToReason.setBackgroundResource(R.drawable.button_bg);
            mBtnToReason.setTextColor(mActivity.getResources().getColor(R.color.jiujiujiu));
        }
    }

    /*
     * 每个条目上数量与价格的显示
     */
    private void itemMoney(final ViewHolder fHolder, RequestAfterSaleBean saleBean, int clickPostion) {
        if (saleBean.getRefund_goods() == 0) {
            fHolder.addBox.setChecked(false);
            mBooleans.set(clickPostion, false);
        } else {
            fHolder.addBox.setChecked(true);
            mBooleans.set(clickPostion, true);
        }
        int refundGoods = saleBean.getRefund_goods();
        double unit_price = Tools.getFenYuan(saleBean.getUnit_price());
        fHolder.tvNumber.setText(refundGoods + "");
        fHolder.tvNums.setText("已选择退货 " + refundGoods + " 件,共退款");
        fHolder.tvMoney.setText("¥" + mDf.format(unit_price * refundGoods));
    }

    static class ViewHolder {
        View viewDivider;
        CheckBox addBox;
        ImageView ivIcon, ivMin, ivAdd;
        TextView tvName, tvPirce, tvNums, tvMoney, tvNumber; // tvNums总的数量，tvNumber单个的选择数量
    }
}
