package com.juxun.business.street.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxun.business.street.bean.CommodityInfoBean;
import com.juxun.business.street.config.Constants;
import com.juxun.business.street.util.ImageLoaderUtil;
import com.juxun.business.street.util.Tools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.ming.efengshe.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author Administrator InthesaleAdapter 出售中的商品适配
 */
public class InthesaleAdapter2 extends BaseAdapter {
    private List<CommodityInfoBean> list = null;
    private Context mContext;

    DecimalFormat df = new java.text.DecimalFormat("0.00");
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderUtil.getOptions();

    public InthesaleAdapter2(Context mContext, List<CommodityInfoBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<CommodityInfoBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return list == null ? 0 : list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    public View getView(final int position, View view, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.inthesale2_itm, null);
            viewHolder.iv_img = (ImageView) view.findViewById(R.id.iv_img);
            viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_cost = (TextView) view.findViewById(R.id.tv_cost);
            viewHolder.tv_state = (TextView) view.findViewById(R.id.tv_state);
            viewHolder.tv_inventory = (TextView) view.findViewById(R.id.tv_inventory);
            viewHolder.tv_inventory_warning = (TextView) view.findViewById(R.id.tv_inventory_warning);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final CommodityInfoBean mallShoppingCartbean = list.get(position);
        viewHolder.tv_name.setText(mallShoppingCartbean.getCommodity_name());
        viewHolder.tv_price.setText("市场价：¥" + Tools.getFenYuan(mallShoppingCartbean.getPrice_high()));
        viewHolder.tv_cost.setText("成本价：¥" + Tools.getFenYuan(mallShoppingCartbean.getCommodity_cost()));
        viewHolder.tv_inventory.setText("库存：" + mallShoppingCartbean.getCommodity_inventory());
        String[] icon = mallShoppingCartbean.getCommodity_icon().split(",");
        imageLoader.displayImage(Constants.imageUrl + icon[0], viewHolder.iv_img, options);
        //判断是否库存不足
        if (mallShoppingCartbean.getCommodity_inventory() <= 10) {
            viewHolder.tv_inventory_warning.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_inventory_warning.setVisibility(View.GONE);
        }
        /**
         * commodity_state": "string,商品状态 1.审核中 2.审核通过 3.审核失败 4.撤销"
         * "shelves_status": "string,上架状态 1.未通过审核  2、下架中 3、上架 4、强制下架",
         */
        switch (mallShoppingCartbean.getCommodity_state()) {
            case 1:
                viewHolder.tv_state.setText("审核中");
                break;
            case 2:
                int shelves_status = mallShoppingCartbean.getShelves_status();
                if (shelves_status == 3) {
                    viewHolder.tv_state.setText("出售中");
                } else if (shelves_status == 1) {
                    viewHolder.tv_state.setText("未通过审核");
                } else if (shelves_status == 2) {
                    viewHolder.tv_state.setText("已下架");
                } else {
                    viewHolder.tv_state.setText("已撤销");
                }
                break;
            case 3:
                viewHolder.tv_state.setText("未通过");
                break;
            case 4:
                viewHolder.tv_state.setText("已撤销");
                break;
            default:
                break;
        }
        return view;

    }

    final static class ViewHolder {
        /**
         * 商品名称 市场价 成本价 库存 库存提醒 状态
         */
        TextView tv_name, tv_price, tv_cost, tv_inventory, tv_inventory_warning, tv_state;
        ImageView iv_img;
    }

}