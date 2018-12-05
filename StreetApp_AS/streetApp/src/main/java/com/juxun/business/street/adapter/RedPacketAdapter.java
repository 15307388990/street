package com.juxun.business.street.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iboxpay.print.IPrintJobStatusCallback;
import com.juxun.business.street.bean.RedPacketBean;
import com.juxun.business.street.util.Tools;
import com.yl.ming.efengshe.R;

import java.util.List;

/**
 * @author RedPacketAdapter 红包列表适配
 */
public class RedPacketAdapter extends BaseAdapter {
    private List<RedPacketBean> list = null;
    private Context mContext;
    String member_id;
    private int type;

    public RedPacketAdapter(Context mContext, List<RedPacketBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public RedPacketAdapter(Context mContext, List<RedPacketBean> list, int type) {
        this.mContext = mContext;
        this.list = list;
        this.type = type;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<RedPacketBean> list) {
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

    @SuppressLint("NewApi")
    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = new ViewHolder();
        view = LayoutInflater.from(mContext).inflate(R.layout.redpacket_itm, null);
        viewHolder.tv_t = (TextView) view.findViewById(R.id.tv_t);
        viewHolder.tv_available = (TextView) view.findViewById(R.id.tv_available);
        viewHolder.tv_money = (TextView) view.findViewById(R.id.tv_money);
        viewHolder.tv_date = (TextView) view.findViewById(R.id.tv_date);
        viewHolder.tv_scopeof = (TextView) view.findViewById(R.id.tv_scopeof);
        viewHolder.iv_img = (ImageView) view.findViewById(R.id.iv_img);
        final RedPacketBean orderModel = list.get(position);
        // 1.未使用2已使用3已过期
        if (orderModel.getState() == 1) {
            viewHolder.tv_t.setTextColor(mContext.getResources().getColor(R.color.red));
            viewHolder.tv_money.setTextColor(mContext.getResources().getColor(R.color.red));
            viewHolder.iv_img.setVisibility(View.GONE);
        } else if (orderModel.getState() == 2) {
            viewHolder.tv_t.setTextColor(mContext.getResources().getColor(R.color.gray));
            viewHolder.tv_money.setTextColor(mContext.getResources().getColor(R.color.gray));
            viewHolder.iv_img.setVisibility(View.VISIBLE);
            viewHolder.iv_img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.discount_sign_used));
        } else {
            viewHolder.tv_t.setTextColor(mContext.getResources().getColor(R.color.gray));
            viewHolder.tv_money.setTextColor(mContext.getResources().getColor(R.color.gray));
            viewHolder.iv_img.setVisibility(View.VISIBLE);
            viewHolder.iv_img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.discount_sign_overdue));
        }
        viewHolder.tv_money.setText(Tools.getFenYuan(orderModel.getRedpacket_price()) + "");
        viewHolder.tv_available.setText("单笔满" + Tools.getFenYuan(orderModel.getFull_price()) + "元可用");
        viewHolder.tv_date.setText("有效期" + orderModel.getStart_date() + "至" + orderModel.getEnd_date());
        if (orderModel.getUse_channel_names() != null) {
            viewHolder.tv_scopeof.setText("使用范围：" + orderModel.getUse_channel_names());
        }

        return view;
    }

    private IPrintJobStatusCallback mTaskCallback = new IPrintJobStatusCallback.Stub() {
        @Override
        public void onPrintJobStatusChange(int status, String taskId) throws RemoteException {
            Log.i("信息", "onPrintTaskStatusChange status = " + status + "taskId=" + taskId);
        }

    };

    final static class ViewHolder {
        // ¥ ，金额，可用，时间 使用范围
        TextView tv_t, tv_money, tv_available, tv_date, tv_scopeof;
        ImageView iv_img;
    }
}