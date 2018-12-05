package com.juxun.business.street.adapter;

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
import android.widget.TextView;

import com.juxun.business.street.activity.PaidDetailActivity;
import com.juxun.business.street.activity.PaidOrderDetailActivity;
import com.juxun.business.street.bean.FinanceSettleBean;
import com.juxun.business.street.bean.Msgmodel;
import com.juxun.business.street.bean.ParseModel;
import com.juxun.business.street.bean.SettleListBean;
import com.juxun.business.street.util.Tools;
import com.yl.ming.efengshe.R;

public class CashFreezeAdapter extends BaseAdapter {

    private List<SettleListBean> list = null;
    private Context mContext;

    public CashFreezeAdapter(Context mContext, List<SettleListBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<SettleListBean> list) {
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
        final ViewHolder viewHolder = new ViewHolder();
        // if (view == null) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_cash_freeze,
                null);

        viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
        viewHolder.tv_timer = (TextView) view.findViewById(R.id.tv_time);
        viewHolder.tv_zhangkai = (TextView) view.findViewById(R.id.tv_zhangkai);
        viewHolder.iv_zhangkai = (ImageView) view
                .findViewById(R.id.iv_zhangkai);
        viewHolder.ll_zhankai = (LinearLayout) view
                .findViewById(R.id.ll_zhankai);
        viewHolder.listView = (LinearLayout) view.findViewById(R.id.lv_list);
        view.setTag(viewHolder);
        // } else {
        // viewHolder = (ViewHolder) view.getTag();
        // }
        final SettleListBean financeSettle = list.get(position);
        viewHolder.tv_price.setText("+" + Tools.getFenYuan(financeSettle.getSettle_price()));
        viewHolder.tv_timer.setText(Tools.getDateformat(financeSettle.getOrder_date()));
        // 4 此商品为POS订单
        if (financeSettle.getSettle_type() == 4) {
            // 支付方式 //支付类型 1.微信支付.2.支付宝支付 3、盒子支付
            switch (financeSettle.getPay_type()) {
                case 1:
                    viewHolder.tv_name.setText("微信支付");
                    break;
                case 2:
                    viewHolder.tv_name.setText("支付宝支付");
                    break;
                case 3:
                    viewHolder.tv_name.setText("盒子支付");
                    break;
                default:
                    break;
            }
        } else if (financeSettle.getSettle_type() == 2) {// 2:特卖分佣订单
            viewHolder.tv_name.setText("特卖分佣订单所得");

        } else if (financeSettle.getSettle_type() == 3) {// 3:海淘分佣订单
            viewHolder.tv_name.setText("海淘分佣订单所得");
        } else if (financeSettle.getSettle_type() == 5) {
            viewHolder.tv_name.setText("海淘订单所得");
        } else if (financeSettle.getSettle_type() == 7) {
            viewHolder.tv_name.setText("快递收款订单所得");
        } else if (financeSettle.getSettle_type() == 9) {
            viewHolder.tv_name.setText("无人售货机分佣订单");
        } else if (financeSettle.getSettle_type() == 10) {
            viewHolder.tv_name.setText("供应链充值收入");
        } else if (financeSettle.getSettle_type() == 11) {
            viewHolder.tv_name.setText("无人售货机拓客收益");
        } else if (financeSettle.getSettle_type() == 12) {
            viewHolder.tv_name.setText("无人售货机一级分销收益");
        } else if (financeSettle.getSettle_type() == 13) {
            viewHolder.tv_name.setText("无人售货机订单");
        } else if (financeSettle.getSettle_type() == 14) {
            viewHolder.tv_name.setText("小程序充值订单");
        }else if (financeSettle.getSettle_type() == 15) {
            viewHolder.tv_name.setText("无人售货机二级分销收益");
        } else {
            String spec = financeSettle.getSpec();
            List<Msgmodel> msgmodels = new ArrayList<Msgmodel>();
            try {
                msgmodels = new ParseModel().getMsgmodel(spec);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (msgmodels != null) {
                if (msgmodels.size() > 1) {
                    for (int i = 1; i < msgmodels.size(); i++) {
                        TextView textView = new TextView(mContext);
                        textView.setText(msgmodels.get(i).getCommodityName());
                        textView.setTextColor(mContext.getResources().getColor(
                                R.color.black));
                        viewHolder.listView.addView(textView);
                    }
                    viewHolder.tv_name.setText(msgmodels.get(0)
                            .getCommodityName());
                    viewHolder.ll_zhankai.setVisibility(View.VISIBLE);
                } else if (msgmodels.size() == 1) {
                    viewHolder.tv_name.setText(msgmodels.get(0)
                            .getCommodityName());
                }
            }
        }
        viewHolder.ll_zhankai.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (viewHolder.tv_zhangkai.getText().toString().equals("展开")) {
                    viewHolder.tv_zhangkai.setText("收起");
                    viewHolder.iv_zhangkai.setImageDrawable(mContext
                            .getResources().getDrawable(R.drawable.icon_up));
                    viewHolder.listView.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.tv_zhangkai.setText("展开");
                    viewHolder.listView.setVisibility(View.GONE);
                    viewHolder.iv_zhangkai.setImageDrawable(mContext
                            .getResources().getDrawable(R.drawable.icon_down));
                }
            }
        });
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (financeSettle.getSettle_type() == 4) {
                    Intent intent = new Intent(mContext,
                            PaidDetailActivity.class);
                    intent.putExtra("financeSettle", financeSettle);
                    mContext.startActivity(intent);
                } else if (financeSettle.getSettle_type() == 1) {
                    Intent intent = new Intent(mContext,
                            PaidOrderDetailActivity.class);
                    intent.putExtra("settle_id", financeSettle.getOrder_id());
                    mContext.startActivity(intent);
                } else {
                    Tools.showToast(mContext, "没有权限查看此订单");
                }

            }
        });
        return view;

    }

    final static class ViewHolder {
        /**
         * 商品名称 商品价格 下单时间 展开 收起
         */
        TextView tv_name, tv_price, tv_timer, tv_zhangkai;
        LinearLayout listView;
        ImageView iv_zhangkai;
        LinearLayout ll_zhankai;
    }
}
