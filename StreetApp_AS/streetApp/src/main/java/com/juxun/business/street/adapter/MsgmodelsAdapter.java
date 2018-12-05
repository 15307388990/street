package com.juxun.business.street.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxun.business.street.config.Constants;
import com.juxun.business.street.bean.Msgmodel;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import java.util.List;

/**
 * @author Administrator 分类列表
 */
public class MsgmodelsAdapter extends BaseAdapter {
    private List<Msgmodel> list = null;
    private Context mContext;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();

    public MsgmodelsAdapter(Context mContext, List<Msgmodel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<Msgmodel> list) {
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
        view = LayoutInflater.from(mContext).inflate(R.layout.buy_item, null);
        viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_pirce);
        viewHolder.tv_number = (TextView) view.findViewById(R.id.tv_number);
        viewHolder.iv_img = (ImageView) view.findViewById(R.id.iv_img);
        viewHolder.tv_specNames = (TextView) view
                .findViewById(R.id.tv_specNames);
        view.setTag(viewHolder);
        final Msgmodel msgmodel = list.get(position);
        imageLoader.displayImage(Constants.imageUrl + msgmodel.getCommodityICon(),
                viewHolder.iv_img, options);
        viewHolder.tv_name.setText(msgmodel.getCommodityName());
        viewHolder.tv_specNames.setText(msgmodel.getSpecNames());
        viewHolder.tv_price.setText(msgmodel.getPrice() + "元");
        viewHolder.tv_number.setText("x" + msgmodel.getGoodsCount());
        return view;

    }

    final static class ViewHolder {
        // 价格 ，已售，减，加，商品名称,数量 ,图片
        ImageView iv_img;
        TextView tv_name, tv_price, tv_number, tv_specNames;
        int number = 0;
    }

}