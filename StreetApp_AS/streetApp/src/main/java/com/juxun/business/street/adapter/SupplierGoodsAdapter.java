package com.juxun.business.street.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxun.business.street.bean.SupplierGoodsBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.Tools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wood121 on 2018/1/8.
 */

public class SupplierGoodsAdapter extends BaseAdapter {

    private Context mContext;
    private List<SupplierGoodsBean> mList;
    private final DecimalFormat decimalFormat;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();

    public SupplierGoodsAdapter(Context context) {
        this.mContext = context;
        decimalFormat = new DecimalFormat("0.00");
    }

    public void updateAdapter(List<SupplierGoodsBean> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_supplier_shoplist, null);
            holder.goodsIcon = (ImageView) convertView.findViewById(R.id.goodsIcon);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SupplierGoodsBean supplierGoodsBean = mList.get(position);

        imageLoader.displayImage(Constants.imageUrl + supplierGoodsBean.getCommodity_icon(), holder.goodsIcon, options);

        holder.tvName.setText(supplierGoodsBean.getCommodity_name());
       // String priceLow = decimalFormat.format(Tools.getFenYuan(supplierGoodsBean.getPrice_low()));
        String priceHigh = decimalFormat.format(Tools.getFenYuan(supplierGoodsBean.getPrice_high()));
        holder.tvPrice.setText("¥" + priceHigh);
        return convertView;
    }

    static class ViewHolder {
        ImageView goodsIcon;
        TextView tvName, tvPrice;
    }
}
