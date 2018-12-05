package com.juxun.business.street.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.juxun.business.street.bean.CouponHisBean;
import com.juxun.business.street.util.Tools;
import com.yl.ming.efengshe.R;

import java.util.List;

/**
 * Created by wood121 on 2018/1/16.
 */

public class CouponHisAdapter extends BaseAdapter {


    private final Context mContext;
    private List<CouponHisBean> mList;

    public CouponHisAdapter(Context context) {
        this.mContext = context;
    }

    public void updateAdapter(List<CouponHisBean> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_coupon_history, null);

            holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            holder.tv_get_time = (TextView) convertView.findViewById(R.id.tv_get_time);
            holder.tv_use_time = (TextView) convertView.findViewById(R.id.tv_use_time);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CouponHisBean couponHisBean = mList.get(position);
        int draw_state = couponHisBean.getDraw_state(); //1未使用，2已使用
        if (draw_state == 1) {
            holder.tv_use_time.setVisibility(View.GONE);
        } else {
            holder.tv_use_time.setVisibility(View.VISIBLE);
            holder.tv_use_time.setText(Tools.getDateformat2(Long.valueOf(couponHisBean.getUse_time().toString())));
        }
        if (couponHisBean.getOrder_num() == null) {
            holder.tv_id.setText("无关联单号");
        } else {
            holder.tv_id.setText("关联单号：" + couponHisBean.getOrder_num());
        }

        holder.tv_name.setText(couponHisBean.getMember_name());
        holder.tv_phone.setText(Tools.pNumber(couponHisBean.getMember_phone()));
        //代金券领取时间的字段？
        holder.tv_get_time.setText(Tools.getDateformat2(couponHisBean.getCreate_date()));

        return convertView;
    }

    static class ViewHolder {
        TextView tv_id, tv_name, tv_phone, tv_get_time, tv_use_time;
    }
}
