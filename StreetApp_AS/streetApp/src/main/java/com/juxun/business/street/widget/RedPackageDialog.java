package com.juxun.business.street.widget;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.juxun.business.street.bean.RedPacketBean;
import com.juxun.business.street.util.Tools;
import com.yl.ming.efengshe.R;

import java.util.List;

/**
 * @author RedPackageDialog 红包弹框
 */
public class RedPackageDialog extends PopupWindow implements OnClickListener {

    protected Context mContext;
    private View mMenuView;
    private ViewFlipper viewfipper;

    // 取消
    private ImageView iv_colse;
    private ListView lv_list;
    private TextView tv_top;
    private onConfirmListener listener = null;
    private List<RedPacketBean> redPacketBeans;
    private Adapter adapter;
    private int totalPrice;// 订单金额

    public RedPackageDialog(Context context, onConfirmListener listener, List<RedPacketBean> redPacketBeans,
                            int totalPrice) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.red_package_dialog, null);
        viewfipper = new ViewFlipper(context);
        viewfipper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        viewfipper.addView(mMenuView);
        viewfipper.setFlipInterval(6000000);
        this.setContentView(viewfipper);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        this.update();
        this.listener = listener;
        this.redPacketBeans = redPacketBeans;
        this.totalPrice = totalPrice;
        setUpViews();

    }

    private void setUpViews() {
        lv_list = (ListView) mMenuView.findViewById(R.id.lv_lsit);
        iv_colse = (ImageView) mMenuView.findViewById(R.id.iv_colse);
        iv_colse.setOnClickListener(this);
        View view = LayoutInflater.from(mContext).inflate(R.layout.text, null);
        lv_list.addHeaderView(view);
        adapter = new Adapter(redPacketBeans);
        lv_list.setAdapter(adapter);
        tv_top = (TextView) mMenuView.findViewById(R.id.tv_top);
        tv_top.setOnClickListener(this);
        lv_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (position != 0) {
                    if (redPacketBeans.get(position - 1).getState() == 1) {
                        dismiss();
                        listener.onConfirm(position - 1);
                    } else {
                        Tools.showToast(mContext, "该红包不满足使用条件");
                    }
                } else {
                    dismiss();
                    listener.onConfirm(-1);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_colse: {
                this.dismiss();
                break;
            }
            case R.id.tv_top: {
                this.dismiss();
                break;
            }
            default:
                break;
        }

    }

    public interface onConfirmListener {
        public void onConfirm(int id);
    }

    public class Adapter extends BaseAdapter {
        private List<RedPacketBean> dataSet;
        private int selected = -1;

        public Adapter(List<RedPacketBean> dataSet) {
            this.dataSet = dataSet;
        }

        public void changeDiscussonList(List<RedPacketBean> data) {
            if (data != null) {
                dataSet = data;
                notifyDataSetChanged();
            }
        }

        public void addData(List<RedPacketBean> data) {
            dataSet.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (dataSet != null) {
                return dataSet.size();
            }
            return 0;
        }

        @Override
        public RedPacketBean getItem(int position) {
            return dataSet.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setSelectedId(int selected) {
            this.selected = selected;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.red_pachage_dialog_itm, null);
                holder.tv_redpacketPrice = (TextView) convertView.findViewById(R.id.tv_redpacketPrice);
                holder.tv_fullPrice = (TextView) convertView.findViewById(R.id.tv_fullPrice);
                holder.tv_expdate = (TextView) convertView.findViewById(R.id.tv_expdate);
                holder.iv_selected = (ImageView) convertView.findViewById(R.id.iv_selected);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            RedPacketBean bean = dataSet.get(position);
            holder.tv_redpacketPrice.setText("￥" + bean.getRedpacket_price());
            holder.tv_fullPrice.setText("单笔满" + bean.getFull_price() + "可用");
            holder.tv_expdate.setText("有效期：" + bean.getEnd_date());
//			if (bean.getIsxuan() == 1) {
//				holder.iv_selected.setVisibility(View.VISIBLE);
//			} else {
//				holder.iv_selected.setVisibility(View.GONE);
//			}
//			if (bean.getIske() == 1) {
//				convertView.setBackgroundResource(R.drawable.colse_btn);
//			} else {
//				convertView.setBackgroundResource(R.color.gray);
//			}
            return convertView;
        }

    }

    class ViewHolder {
        // 面值 满多少可用 有效期
        public TextView tv_redpacketPrice, tv_fullPrice, tv_expdate;
        // 是否选中
        private ImageView iv_selected;
    }
}
