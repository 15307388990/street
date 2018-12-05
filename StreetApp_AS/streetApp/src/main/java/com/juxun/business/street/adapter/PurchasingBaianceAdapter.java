package com.juxun.business.street.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.juxun.business.street.activity.PurchasingDetailsActivity;
import com.juxun.business.street.bean.TopUpBean;
import com.juxun.business.street.util.Tools;
import com.yl.ming.efengshe.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author 充值记录
 */
public class PurchasingBaianceAdapter extends BaseAdapter {
    private List<TopUpBean> list = null;
    private Context mContext;
    private final DecimalFormat decimalFormat;

    public PurchasingBaianceAdapter(Context mContext, List<TopUpBean> list) {

        this.mContext = mContext;
        this.list = list;
        decimalFormat = new DecimalFormat("0.00");
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<TopUpBean> list) {
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

    public View getView(int position, View view, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.top_up_itm, null);
            viewHolder.tv_recharge_price = (TextView) view.findViewById(R.id.tv_recharge_price);
            viewHolder.tv_recharge_remark = (TextView) view.findViewById(R.id.tv_recharge_remark);
            viewHolder.tv_timer = (TextView) view.findViewById(R.id.tv_timer);
            viewHolder.tv_type = (TextView) view.findViewById(R.id.tv_type);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final TopUpBean topUpBean = list.get(position);
        //充值备注
        if (topUpBean.getRecharge_remark() != null) {
            viewHolder.tv_recharge_remark.setText(topUpBean.getRecharge_remark());
        } else {
            viewHolder.tv_recharge_remark.setText("");
        }
        //充值时间
        viewHolder.tv_timer.setText(Tools.getDateformat(topUpBean.getRecharge_time()));

        switch (topUpBean.getRecharge_type()) {  //充值类型 \t * 1.充值送现金 \t * 2.充值折扣 \t * 3.充值送红包 \t * 4.支付消费 \t * 5.订单售后退款",
            case 0:
            case 1:
                viewHolder.tv_type.setText("余额充值");
                viewHolder.tv_recharge_price.setText("+" + Tools.getFenYuan(topUpBean.getRecharge_price()));
                break;
            case 2:
                viewHolder.tv_type.setText("余额充值");
                viewHolder.tv_recharge_price.setText("+" + Tools.getFenYuan(topUpBean.getRecharge_price()));
                break;
            case 3:
                viewHolder.tv_type.setText("余额充值");
                viewHolder.tv_recharge_price.setText("+" + Tools.getFenYuan(topUpBean.getRecharge_price()));
                break;
            case 4:
                viewHolder.tv_type.setText("采购订单");
                viewHolder.tv_recharge_price.setText("-" + Tools.getFenYuan(topUpBean.getPay_price()));
                break;
            case 5:
                viewHolder.tv_type.setText("采购订单退款");
                viewHolder.tv_recharge_price.setText("+" + Tools.getFenYuan(topUpBean.getRecharge_price()));
                break;
            default:
                break;
        }

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PurchasingDetailsActivity.class);
                intent.putExtra("topup", topUpBean);
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    final static class ViewHolder {
        /**
         * 充值类型 充值金额 日期 备注
         */
        TextView tv_recharge_price, tv_timer, tv_recharge_remark, tv_type;
    }

}