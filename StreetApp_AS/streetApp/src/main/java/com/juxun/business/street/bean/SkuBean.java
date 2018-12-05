package com.juxun.business.street.bean;

import java.io.Serializable;

public class SkuBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 频道列表
     * "total_count": "integer,所有商品数量",
     * "saleing_count": "integer,出售中的商品数量",
     * "not_saleing_count": "integer,审核通过但未上架的商品数量",
     * "inventory_shortage_count": "integer,库存不足的商品数量",
     * "pending_review_count": "integer,待审核的商品数量",
     * "not_pass_count": "integer,审核未通过的商品",
     * "forced_off_the_shelf_count": "integer,强制下架的商品"
     */
    private int total_count;// 查询所有
    private int saleing_count;// 出售中的商品
    private int not_saleing_count;// 审核通过但未上架的商品
    private int inventory_shortage_count;// 库存不足的商品
    private int pending_review_count;// 待审核的商品
    private int not_pass_count;// 审核未通过的商品
    private int forced_off_the_shelf_count;// 强制下架的商品

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getSaleing_count() {
        return saleing_count;
    }

    public void setSaleing_count(int saleing_count) {
        this.saleing_count = saleing_count;
    }

    public int getNot_saleing_count() {
        return not_saleing_count;
    }

    public void setNot_saleing_count(int not_saleing_count) {
        this.not_saleing_count = not_saleing_count;
    }

    public int getInventory_shortage_count() {
        return inventory_shortage_count;
    }

    public void setInventory_shortage_count(int inventory_shortage_count) {
        this.inventory_shortage_count = inventory_shortage_count;
    }

    public int getPending_review_count() {
        return pending_review_count;
    }

    public void setPending_review_count(int pending_review_count) {
        this.pending_review_count = pending_review_count;
    }

    public int getNot_pass_count() {
        return not_pass_count;
    }

    public void setNot_pass_count(int not_pass_count) {
        this.not_pass_count = not_pass_count;
    }

    public int getForced_off_the_shelf_count() {
        return forced_off_the_shelf_count;
    }

    public void setForced_off_the_shelf_count(int forced_off_the_shelf_count) {
        this.forced_off_the_shelf_count = forced_off_the_shelf_count;
    }
}
