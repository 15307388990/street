package com.juxun.business.street.adapter;

import java.util.List;

import com.juxun.business.street.bean.StoreCenterBean;
import com.juxun.business.street.util.Tools;
import com.yl.ming.efengshe.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StoreCenAndHisAdapter extends BaseAdapter {

    private Context mContext;
    private List<StoreCenterBean> mDataLists;
    private int mType; // 0储值中心，1历史储值活动

    public StoreCenAndHisAdapter(Context context,
                                 List<StoreCenterBean> storeCardsList) {
        this.mDataLists = storeCardsList;
        this.mContext = context;
    }

    public void updateHistoryList(List<StoreCenterBean> storeCardsList, int type) {
        this.mDataLists = storeCardsList;
        this.mType = type;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataLists == null ? 0 : mDataLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // viewHolder,convertView
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.store_value_cardview, null);
            holder.view_mark = convertView.findViewById(R.id.view_mark); // view_mark左侧的显示情况
            holder.tv_act_name = (TextView) convertView // 名字特么的！！！写错了，没找到控件
                    .findViewById(R.id.tv_act_name);
            holder.tv_act_status = (TextView) convertView
                    .findViewById(R.id.tv_act_status);
            holder.tv_card_type = (TextView) convertView
                    .findViewById(R.id.tv_card_type);
            holder.tv_card_num = (TextView) convertView
                    .findViewById(R.id.tv_card_num);
            holder.tv_act_time = (TextView) convertView
                    .findViewById(R.id.tv_act_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 数据绑定
        StoreCenterBean storeCenterBean = mDataLists.get(position);

        if (mType == 1) { // 历史活动进来的数据：灰色主题展示
            int color = mContext.getResources().getColor(R.color.jiujiujiu);
            holder.view_mark.setBackgroundColor(color);
            holder.tv_act_name.setTextColor(color);
            holder.tv_act_status.setTextColor(color);
            holder.tv_act_status.setText("已结束");
            holder.tv_card_type.setTextColor(color);
            holder.tv_card_num.setTextColor(color);
            holder.tv_act_time.setTextColor(color);
        } else {
            if (storeCenterBean.getState() == 1) {
                holder.tv_act_status.setTextColor(mContext.getResources().getColor(
                        R.color.two_gray));
            } else {
                holder.tv_act_status.setTextColor(mContext.getResources().getColor(
                        R.color.blue));
            }
        }

        holder.tv_act_name.setText(storeCenterBean.getName());
        if (storeCenterBean.getState() == 1) {
            holder.tv_act_status.setText("未开始");
        } else if (storeCenterBean.getState() == 2) {
            holder.tv_act_status.setText("进行中");
        } else if (storeCenterBean.getState() == 3) {
            holder.tv_act_status.setText("已结束");
        } else if (storeCenterBean.getState() == 4) {
            holder.tv_act_status.setText("已暂停");

        }

        holder.tv_card_type
                .setText("储值种类："
                        + storeCenterBean.getRechargeTypeCount() + "种");
        holder.tv_card_num.setText("购买张数："
                + storeCenterBean.getBuyCount() + "张");
        holder.tv_act_time.setText(Tools.getDateformat(storeCenterBean.getStart_time()) + " -- "
                + Tools.getDateformat(storeCenterBean.getEnd_time()));

        return convertView;
    }

    static class ViewHolder {
        TextView tv_act_name, tv_act_status, tv_card_type, tv_card_num,
                tv_act_time;
        View view_mark;
    }

}
