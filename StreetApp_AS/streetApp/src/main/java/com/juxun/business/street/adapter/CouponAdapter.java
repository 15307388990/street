package com.juxun.business.street.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.juxun.business.street.bean.ListarchBean;
import com.yl.ming.efengshe.R;

import java.util.List;

/**
 * @author WuJianHua 时间列表适配器
 */
public class CouponAdapter extends BaseAdapter {
    private Context mContext;
    private List<ListarchBean> dataSet;
    private int index;
    private int[] bgs = new int[]{R.drawable.coupon_blue,
            R.drawable.coupon_yellow, R.drawable.coupon_green,
            R.drawable.coupon_gray};

    public CouponAdapter(Context context, List<ListarchBean> dataSet) {
        mContext = context;
        this.dataSet = dataSet;
    }

    public void changeDiscussonList(List<ListarchBean> data) {
        if (data != null) {
            dataSet = data;
            notifyDataSetChanged();
        }
    }

    public void addData(List<ListarchBean> data) {
        dataSet.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public ListarchBean getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_coupon, null);
            holder.content = (FrameLayout) convertView
                    .findViewById(R.id.content);
            holder.shopName = (TextView) convertView
                    .findViewById(R.id.shopName);
            holder.money = (TextView) convertView.findViewById(R.id.money);
            holder.max = (TextView) convertView.findViewById(R.id.condition);
            holder.start = (TextView) convertView.findViewById(R.id.start);
            holder.end = (TextView) convertView.findViewById(R.id.end);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ListarchBean bean = dataSet.get(position);
        if (position == 0 || index == 4) {
            index = 0;
        }
        holder.content.setBackgroundResource(bgs[index]);
        index++;
        // holder.shopName.setText(bean.getCoupon_name());
        // holder.max.setText(mContext.getString(R.string.full) +
        // bean.getCoupon_max()+mContext.getString(R.string.use));
        // holder.start.setText((""+bean.getCoupon_use_start_date()).split(" ")[0]);
        // holder.end.setText((""+bean.getCoupon_use_end_date()).split(" ")[0]);
        holder.money.setText("卡券ID:" + bean.getCard_id());
        return convertView;
    }

    class ViewHolder {
        public TextView shopName;
        public TextView money;
        public TextView max;
        public TextView start;
        public TextView end;
        public FrameLayout content;
    }
}
