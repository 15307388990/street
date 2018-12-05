package com.juxun.business.street.adapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.juxun.business.street.bean.StoreStaBean;
import com.juxun.business.street.util.Tools;
import com.yl.ming.efengshe.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StoreCardsAdapter extends BaseAdapter {

    private Context mContext;
    private List<StoreStaBean> mDatas;
    private SimpleDateFormat mSdf;
    private DecimalFormat mDecimalFormat;

    public StoreCardsAdapter(Context context) {
        this.mContext = context;
        mSdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        mDecimalFormat = new DecimalFormat("0.00");
    }

    public void updateCardsAdapter(List<StoreStaBean> storeStasList) {
        this.mDatas = storeStasList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_store_statistics, null);
            holder.tv_phone_num = (TextView) convertView
                    .findViewById(R.id.tv_phone_num);
            holder.tv_acts_content = (TextView) convertView
                    .findViewById(R.id.tv_acts_content);
            holder.tv_cardid = (TextView) convertView
                    .findViewById(R.id.tv_cardid);
            holder.tv_card_time = (TextView) convertView
                    .findViewById(R.id.tv_card_time);
            holder.view_divider = convertView.findViewById(R.id.view_divider);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 最后一条的分割线处理
        holder.view_divider.setVisibility(View.VISIBLE);
        if (position == mDatas.size() - 1) {
            holder.view_divider.setVisibility(View.GONE);
        }
        // 数据处理
        StoreStaBean storeStaBean = mDatas.get(position);
        String member_phone = storeStaBean.getMember_phone();
        StringBuilder sb = new StringBuilder(member_phone);
        sb.replace(3, 7, "****");
        holder.tv_phone_num.setText(sb.toString());
        holder.tv_acts_content.setText("充"
                + Tools.getFenYuan(storeStaBean.getMoney()) + "送"
                + Tools.getFenYuan(storeStaBean.getGive_money()) + "元");
        holder.tv_cardid.setText("单号: " + storeStaBean.getTran_no());
        holder.tv_card_time.setText(mSdf.format(new Date(storeStaBean
                .getCreate_date())));
        return convertView;
    }

    static class ViewHolder {
        TextView tv_phone_num, tv_acts_content, tv_cardid, tv_card_time;
        View view_divider;
    }

}
