package com.juxun.business.street.bean;

import java.io.Serializable;
import java.util.List;


public class ShopStaBean implements Serializable {
    /*
     * { "today_total_price": "integer,今日总收入 单位分", "today_commissions_price":
     * "integer,今日分佣金额 单位分", "today_pos_price": "integer,今日pos收入 （包含台卡收入） 单位分",
     * "today_order_price": "integer,今日订单收入 单位分", "today_order_count":
     * "integer, 今日订单总数", "total_order_count": "integer,历史订单总数",
     * "order_count_list": [ { "date": "string,日期", "count": "string,数量" } ],
     * "today_top5_list": [ { "commodityName": "string,商品名字", "msg_count":
     * "string,商品数量" } ], "total_top5_list": [ { "commodityName": "string,商品名字",
     * "msg_count": "string,商品数量" } ], "channel_top5_list": [ {
     * "f_channel_name": "string,一级频道名称", "channel_name": "string,二级频道名称",
     * "gcount": "string,销量总数" } ] }
     */
    private int today_total_price;

    private int today_commissions_price;
    private int today_pos_price;
    private int today_order_price;

    private int today_order_count;
    private int total_order_count;

    private List<ShopStaModel> order_count_list;
    private List<TopBean> today_top5_list;
    private List<TopBean2> total_top5_list;
    private List<TopChannel> channel_top5_list;

    public int getToday_total_price() {
        return today_total_price;
    }

    public void setToday_total_price(int today_total_price) {
        this.today_total_price = today_total_price;
    }

    public int getToday_commissions_price() {
        return today_commissions_price;
    }

    public void setToday_commissions_price(int today_commissions_price) {
        this.today_commissions_price = today_commissions_price;
    }

    public int getToday_pos_price() {
        return today_pos_price;
    }

    public void setToday_pos_price(int today_pos_price) {
        this.today_pos_price = today_pos_price;
    }

    public int getToday_order_price() {
        return today_order_price;
    }

    public void setToday_order_price(int today_order_price) {
        this.today_order_price = today_order_price;
    }

    public int getToday_order_count() {
        return today_order_count;
    }

    public void setToday_order_count(int today_order_count) {
        this.today_order_count = today_order_count;
    }

    public int getTotal_order_count() {
        return total_order_count;
    }

    public void setTotal_order_count(int total_order_count) {
        this.total_order_count = total_order_count;
    }

    public List<ShopStaModel> getOrder_count_list() {
        return order_count_list;
    }

    public void setOrder_count_list(List<ShopStaModel> order_count_list) {
        this.order_count_list = order_count_list;
    }

    public List<TopBean> getToday_top5_list() {
        return today_top5_list;
    }

    public void setToday_top5_list(List<TopBean> today_top5_list) {
        this.today_top5_list = today_top5_list;
    }

    public List<TopBean2> getTotal_top5_list() {
        return total_top5_list;
    }

    public void setTotal_top5_list(List<TopBean2> total_top5_list) {
        this.total_top5_list = total_top5_list;
    }

    public List<TopChannel> getChannel_top5_list() {
        return channel_top5_list;
    }

    public void setChannel_top5_list(List<TopChannel> channel_top5_list) {
        this.channel_top5_list = channel_top5_list;
    }

    public class TopChannel implements Serializable {
        /*
         * "f_channel_name": "string,一级频道名称", "channel_name": "string,二级频道名称",
         * "gcount": "string,销量总数"
         */
        private String f_channel_name;
        private String channel_name;
        private String gcount;

        public String getF_channel_name() {
            return f_channel_name;
        }

        public void setF_channel_name(String f_channel_name) {
            this.f_channel_name = f_channel_name;
        }

        public String getChannel_name() {
            return channel_name;
        }

        public void setChannel_name(String channel_name) {
            this.channel_name = channel_name;
        }

        public String getGcount() {
            return gcount;
        }

        public void setGcount(String gcount) {
            this.gcount = gcount;
        }
    }

    public class TopBean implements Serializable {
        private String commodity_name;
        private String msg_count;

        public String getCommodity_name() {
            return commodity_name;
        }

        public void setCommodity_name(String commodity_name) {
            this.commodity_name = commodity_name;
        }

        public String getMsg_count() {
            return msg_count;
        }

        public void setMsg_count(String msg_count) {
            this.msg_count = msg_count;
        }

    }

    public class TopBean2 implements Serializable {
        private String commodityName;
        private String msg_count;

        public String getCommodityName() {
            return commodityName;
        }

        public void setCommodityName(String commodityName) {
            this.commodityName = commodityName;
        }

        public String getMsg_count() {
            return msg_count;
        }

        public void setMsg_count(String msg_count) {
            this.msg_count = msg_count;
        }
    }


}
