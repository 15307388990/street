/**
 *
 */
package com.juxun.business.street.bean;

import java.text.DecimalFormat;
import java.util.Date;


/**
 * 类名称：MallShoppingCartMode 类描述：订单详情 首页 创建人：罗富贵 创建时间：2016年5月10日
 */
public class MallShoppingCartMode extends Msgmodel {
    private static final long serialVersionUID = 1L;
    private int total_price;
    private double zongly;// 总利润
    private double zongjj;// 总成本
    private double fyhj;// 分佣合计

    public double getFyhj() {
        fyhj = getPrice() * getCommission() * getGoodsCount();
        return fyhj;
    }

    public void setFyhj(double fyhj) {
        this.fyhj = fyhj;
    }

    public double getZongjj() {
        zongjj = getCommodityCost() * getGoodsCount();
        return zongjj;
    }

    public void setZongjj(double zongjj) {
        this.zongjj = zongjj;
    }

    public double getZongly() {
        // 总价-成本
        zongly = getTotal_price() - (getCommodityCost() * getGoodsCount());
        return zongly;
    }

    public void setZongly(double zongly) {
        this.zongly = zongly;
    }

    public void setPrice(Integer price) {
        if (this.getCommodityCost() == null || this.getCommodityCost() == 0) {
            this.setCommodityCost(price);
        }
        super.setPrice(price);
    }

    public int getTotal_price() {
        total_price = getPrice() * getGoodsCount();
        return total_price;
    }

    public void setTotal_price(Integer total_price) {
        this.total_price = total_price;
    }


}
