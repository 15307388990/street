package com.juxun.business.street.adapter;


import android.app.Activity;
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
import com.juxun.business.street.activity.RequestAfterSaleActivity;
import com.juxun.business.street.bean.PurchaseOderBean;
import com.juxun.business.street.bean.ShopingBean;
import com.juxun.business.street.bean.ShopingCartBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.Tools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 售后申请的Adapter
 *
 * @author wood121
 */
public class AftersaleRequestAdapter extends BaseAdapter {

    private Activity mContext;
    private List<PurchaseOderBean> mList; // 传递进来的数据
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private List<ShopingBean> mSuplierList;
    private PurchaseOderBean mPurchaseOderBean;

    public AftersaleRequestAdapter(Activity context,
                                   List<PurchaseOderBean> afterSaleList) {
        this.mContext = context;
        this.mList = afterSaleList;
        mImageLoader = ImageLoader.getInstance();
        mOptions = ImageLoaderUtil.getOptions();
        mSuplierList = new ArrayList<ShopingBean>();
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     */
    public void updateListView(List<PurchaseOderBean> list) {
        this.mList = list;
        notifyDataSetChanged();
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
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_aftersale_request, null);
            holder.tv_orderNo = (TextView) convertView
                    .findViewById(R.id.tv_order_no);
            holder.tv_orderTime = (TextView) convertView
                    .findViewById(R.id.tv_order_time);
            holder.tv_request_reason = (TextView) convertView
                    .findViewById(R.id.tv_request_reason);
            holder.orderContainer = (LinearLayout) convertView
                    .findViewById(R.id.fl_list);
            holder.reqBtn = (Button) convertView.findViewById(R.id.btn_request);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        mPurchaseOderBean = mList.get(position);
        holder.tv_orderNo.setText(mPurchaseOderBean.getOrder_num());
        String create_date = Tools.getDateformat(mPurchaseOderBean.getCreate_date());
        if (create_date.contains(".0")) {
            holder.tv_orderTime.setText(create_date.substring(0, create_date.length() - 2));
        } else {
            holder.tv_orderTime.setText(create_date);
        }

        // 售后申请通过订单状态确定，原因通过"""reason"来区别
        holder.tv_request_reason.setText("");
        holder.reqBtn.setVisibility(View.INVISIBLE);

        // 是否可以申请售后申请操作说明与按钮 1可以申请售后，2不可以申请售后
        int customer_status = mPurchaseOderBean.getCustomer_status();
        if (customer_status == 1) {
            if (mPurchaseOderBean.getCan_refund_num() > 0) {
                holder.reqBtn.setVisibility(View.VISIBLE);
                holder.tv_request_reason.setText("");
            } else {
                holder.reqBtn.setVisibility(View.INVISIBLE);
                holder.tv_request_reason.setText("全部商品已申请售后完毕");
            }
        } else if (customer_status == 2) {
            holder.reqBtn.setVisibility(View.INVISIBLE);
            holder.tv_request_reason.setText("已超过7天售后申请时间");
        }

        // itemView中间显示的商品条目
        String spec = mPurchaseOderBean.getCommodity_json();
        mSuplierList = JSON.parseArray(spec, ShopingBean.class);

        holder.orderContainer.removeAllViews();
        for (int i = 0; i < mSuplierList.size(); i++) {
            View itemInnerView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_item_aftersale_request, null);
            ImageView imgView = (ImageView) itemInnerView
                    .findViewById(R.id.iv_img);
            TextView tvName = (TextView) itemInnerView
                    .findViewById(R.id.tv_name);
            TextView tvNumber = (TextView) itemInnerView
                    .findViewById(R.id.tv_number);

            ShopingBean cartBean = mSuplierList.get(i);
            mImageLoader.displayImage(
                    Constants.imageUrl + cartBean.getCommodity_icon(), imgView,
                    mOptions);
            tvName.setText(cartBean.getCommodity_name());
            tvNumber.setText("x" + cartBean.getMsg_count());
            holder.orderContainer.addView(itemInnerView);
        }

        final int clickPosition = position;
        // 售后申请操作
        holder.reqBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 对应的订单id传递给申请售后操作
                Intent intent = new Intent();
                intent.setClass(mContext, RequestAfterSaleActivity.class);
                intent.putExtra("order_num", mList.get(clickPosition).getOrder_num()
                        + "");
                intent.putExtra("order_id", mList.get(clickPosition).getId()
                        + "");
                intent.putExtra("pay_type", mList.get(clickPosition).getPay_type());

                mContext.startActivityForResult(intent, 1);
            }
        });

        return convertView;
    }

    /*
     * 对findViewById（）查找view树进行优化
     */
    static class ViewHolder {
        public TextView tv_orderNo, tv_orderTime, tv_request_reason;
        public LinearLayout orderContainer;
        public Button reqBtn;
    }
}
