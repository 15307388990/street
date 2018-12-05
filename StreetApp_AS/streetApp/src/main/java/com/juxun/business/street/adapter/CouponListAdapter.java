package com.juxun.business.street.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.juxun.business.street.bean.CouponListBean;
import com.juxun.business.street.util.Tools;
import com.yl.ming.efengshe.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.yl.ming.efengshe.R.id.view_mark;

/**
 * Created by wood121 on 2018/1/15.
 */

public class CouponListAdapter extends BaseAdapter {

    private final Context mConetxt;
    private final DecimalFormat mDecimalFormat;
    private final SimpleDateFormat mSdf;
    private List<CouponListBean> mList;


    public CouponListAdapter(Context context) {
        this.mConetxt = context;
        mSdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        mDecimalFormat = new DecimalFormat("0.00");
    }

    public void updateAdapter(List<CouponListBean> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mConetxt).inflate(R.layout.item_coupon_list, null);
            holder.view_mark = convertView.findViewById(view_mark);
            holder.tv_act_name = (TextView) convertView.findViewById(R.id.tv_act_name);
            holder.tv_act_status = (TextView) convertView.findViewById(R.id.tv_act_status);
            holder.tv_card_type = (TextView) convertView.findViewById(R.id.tv_card_type);
            holder.tv_card_num = (TextView) convertView.findViewById(R.id.tv_card_num);
            holder.tv_act_time = (TextView) convertView.findViewById(R.id.tv_act_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CouponListBean couponListBean = mList.get(position);

        holder.tv_act_name.setText(couponListBean.getName());

        int tv_red = mConetxt.getResources().getColor(R.color.tv_red);
        int tv_blue = mConetxt.getResources().getColor(R.color.guide_words);
        int tv_black = mConetxt.getResources().getColor(R.color.two_gray);
        int tv_gray = mConetxt.getResources().getColor(R.color.jiujiujiu);

        if (couponListBean.getState() == 1) {
            holder.tv_act_status.setText("已暂停");
            holder.tv_act_status.setTextColor(tv_red);
            holder.view_mark.setBackgroundColor(tv_blue);
        } else {
            long start_time = couponListBean.getStart_time();
            long end_time = couponListBean.getEnd_time();
            long timeMillis = System.currentTimeMillis();

            if (timeMillis < start_time) {
                holder.tv_act_status.setText("未开始");
                holder.tv_act_status.setTextColor(tv_black);
                holder.view_mark.setBackgroundColor(tv_blue);
            } else if (timeMillis >= start_time && timeMillis <= end_time) {
                holder.tv_act_status.setText("进行中");
                holder.tv_act_status.setTextColor(tv_blue);
                holder.view_mark.setBackgroundColor(tv_blue);
            } else {
                holder.tv_act_status.setText("已结束");
                holder.view_mark.setBackgroundColor(tv_gray);
                holder.tv_act_name.setTextColor(tv_gray);
                holder.tv_act_status.setTextColor(tv_gray);
                holder.tv_card_type.setTextColor(tv_gray);
                holder.tv_card_num.setTextColor(tv_gray);
                holder.tv_act_time.setTextColor(tv_gray);
            }
        }

        CouponListBean.DenominationListBean denominationListBean = couponListBean.getDenomination_list().get(0);
        String maxPrice = mDecimalFormat.format(Tools.getFenYuan(denominationListBean.getMax_price()));
        String usePrice = mDecimalFormat.format(Tools.getFenYuan(denominationListBean.getUse_price()));
        holder.tv_card_type.setText("优惠条件：满" + maxPrice + "减" + usePrice);

        holder.tv_card_num.setText("发卡总量：" + couponListBean.getRed_count() + "张");

        holder.tv_act_time.setText(mSdf.format(couponListBean.getStart_time())
                + "-" + mSdf.format(couponListBean.getEnd_time()));
        return convertView;
    }


    static class ViewHolder {
        View view_mark;
        TextView tv_act_name, tv_act_status, tv_card_type, tv_card_num, tv_act_time;
    }
}
