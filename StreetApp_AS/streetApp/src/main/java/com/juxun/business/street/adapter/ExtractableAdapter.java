package com.juxun.business.street.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.juxun.business.street.activity.ExtractableDetailActivity;
import com.juxun.business.street.bean.FinanceWithdrawBean;
import com.juxun.business.street.bean.Msgmodel;
import com.juxun.business.street.bean.ParseModel;
import com.juxun.business.street.util.Tools;
import com.yl.ming.efengshe.R;

/**
 * @author Administrator 分类列表
 */
public class ExtractableAdapter extends BaseAdapter {
    private List<FinanceWithdrawBean> list = null;
    private Context mContext;
    // SimpleDateFormat dateFormat = new
    // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 可以方便地修改日期格式

    public ExtractableAdapter(Context mContext, List<FinanceWithdrawBean> list) {

        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<FinanceWithdrawBean> list) {
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

    public View getView(final int position, View view, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.extractable_itm, null);
            viewHolder.tv_date = (TextView) view.findViewById(R.id.tv_time);
            viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
            viewHolder.tv_state = (TextView) view.findViewById(R.id.tv_state);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        // 数据绑定
        final FinanceWithdrawBean financeWithdraw = list.get(position);

        viewHolder.tv_date.setText(""
                + Tools.getDateformat(Long.parseLong(financeWithdraw
                .getWithdraw_date())));
        viewHolder.tv_price.setText("" + Tools.getFenYuan(financeWithdraw.getWithdraw_price()));
        // 提现状态 1； 提现中 2；提现成功 -1提现失败",
        switch (financeWithdraw.getWithdraw_stauts()) {
            case 1:
                viewHolder.tv_state.setTextColor(mContext.getResources().getColor(
                        R.color.blue));
                viewHolder.tv_state.setText("提现中 ");
                break;
            case 2:
                viewHolder.tv_state.setTextColor(mContext.getResources().getColor(
                        R.color.actionbar_title_color));
                viewHolder.tv_state.setText("提现成功 ");
                break;
            case -1:
                viewHolder.tv_state.setTextColor(mContext.getResources().getColor(
                        R.color.red));
                viewHolder.tv_state.setText("提现失败 ");
                break;
            default:
                break;
        }

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext,
                        ExtractableDetailActivity.class);
                intent.putExtra("finance", financeWithdraw);
                mContext.startActivity(intent);

            }
        });
        return view;

    }

    final static class ViewHolder {
        /**
         * 商品名称 商品价格 下单时间 展开 收起
         */
        TextView tv_date, tv_price, tv_state;
    }

}