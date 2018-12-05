package com.juxun.business.street.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.juxun.business.street.activity.AfterSaleProgressActivity;
import com.juxun.business.street.bean.PurchaseOderBean;
import com.juxun.business.street.bean.ShopingBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.fragment.RequestAfterSaleBean;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.Tools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 销售记录Adapter
 *
 * @author wood121
 */
public class AftersaleHistoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<PurchaseOderBean> mList;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private onCancelAfter oncAfter;

    public AftersaleHistoryAdapter(Context context, onCancelAfter oncAfter) {
        this.mContext = context;
        this.oncAfter = oncAfter;
        mList = new ArrayList<PurchaseOderBean>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = ImageLoaderUtil.getOptions();
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     */
    public void updateListView(List<PurchaseOderBean> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
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
        // convertView,ViewHolder
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_aftersale_history, null);
            holder.tv_orderNo = (TextView) convertView.findViewById(R.id.tv_order_no);
            holder.tv_orderTime = (TextView) convertView.findViewById(R.id.tv_order_time);
            holder.orderContainer = (LinearLayout) convertView.findViewById(R.id.fl_list);
            holder.reqBtn = (Button) convertView.findViewById(R.id.btn_request);
            holder.ll_layout = (LinearLayout) convertView.findViewById(R.id.ll_layout);
            holder.rl_layout = (RelativeLayout) convertView.findViewById(R.id.rl_layout);
            holder.tv_state_text = (TextView) convertView.findViewById(R.id.tv_state_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 订单编号，时间显示
        final PurchaseOderBean purchaseOderBean = mList.get(position);
		holder.tv_orderNo.setText(purchaseOderBean.getCustomer_service_order_num());
        holder.tv_orderTime.setText(Tools.getDateformat(purchaseOderBean.getCreate_date()));
        // 是否可以申请售后申请操作说明与按钮
        // 1.审核中2.审核不通过3退款中4退款成功5退款失败6取消售后
		switch (purchaseOderBean.getCustomer_service_status()) {
		case 1:
			holder.rl_layout.setVisibility(View.VISIBLE);
			holder.tv_state_text.setText("售后进度：审核中 | 您的服务单正在审核中");
			break;
		case 2:
			holder.rl_layout.setVisibility(View.GONE);
			holder.tv_state_text.setText("售后进度：审核未通过 | 您的服务单未通过审核");
			break;
		case 3:
			holder.rl_layout.setVisibility(View.GONE);
			holder.tv_state_text.setText("售后进度：退款中 | 您的服务单正在退款中");
			break;
		case 4:
			holder.rl_layout.setVisibility(View.GONE);
			holder.tv_state_text.setText("售后进度：已退款 | 您的服务单已完成退款");
			break;
		case 5:
			holder.rl_layout.setVisibility(View.GONE);
			holder.tv_state_text.setText("售后进度：退款失败 | 您的服务单退款失败请联系客服");
			break;
		case 6:
			holder.rl_layout.setVisibility(View.GONE);
			holder.tv_state_text.setText("售后进度：取消售后申请 | 您的服务单已取消");
			break;
		}
        // 售后申请操作
        holder.reqBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 传递订单号
                oncAfter.onCancel(purchaseOderBean.getId());
            }
        });
        // 跳转至进度详情
        holder.ll_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AfterSaleProgressActivity.class);
                intent.putExtra("purchaseOderBean", purchaseOderBean);
                mContext.startActivity(intent);
            }
        });

        // itemView中间显示的商品条目
        String spec = purchaseOderBean.getCommodity_json();
        List<ShopingBean> suplierList = new ArrayList<ShopingBean>();
        suplierList = JSON.parseArray(spec, ShopingBean.class);

        holder.orderContainer.removeAllViews();
        for (int i = 0; i < suplierList.size(); i++) {
            View itemInnerView = LayoutInflater.from(mContext).inflate(R.layout.item_item_aftersale_request, null);
            ImageView imgView = (ImageView) itemInnerView.findViewById(R.id.iv_img);
            TextView tvName = (TextView) itemInnerView.findViewById(R.id.tv_name);
            TextView tvNumber = (TextView) itemInnerView.findViewById(R.id.tv_number);
            ShopingBean cartBean = suplierList.get(i);
            mImageLoader.displayImage(Constants.imageUrl + cartBean.getCommodity_icon(), imgView, mOptions);
            tvName.setText(cartBean.getCommodity_name());
            tvNumber.setText("x" + cartBean.getRefund_commodity_num());
            holder.orderContainer.addView(itemInnerView);
        }

        return convertView;
    }

    /*
     * 对findViewById（）查找view树进行优化
     */
    static class ViewHolder {
        // 服务单号 申请时间
        public TextView tv_orderNo, tv_orderTime;
        // 商品列表
        public LinearLayout orderContainer;
        // 取消售后申请
        public Button reqBtn;
        private RelativeLayout rl_layout;
        // 售后进度
        private TextView tv_state_text;
        private LinearLayout ll_layout;

    }

    public interface onCancelAfter {
        public void onCancel(int order_id);
    }
}
