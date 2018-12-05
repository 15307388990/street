package com.juxun.business.street.bean;

import java.io.Serializable;
import java.security.PrivateKey;
import java.util.List;

/**
 * Created by wood121 on 2018/1/15.
 * <p>
 * 代金券列表Bean对象
 */

public class CouponListBean implements Serializable {

    /**
     * "id":1,
     * "create_date":1516092454000,
     * "deleted":false,
     * "name":"东海龙宫",
     * "cover":"",
     * "descri":"东莞市人民医院",
     * "start_time":1517450400000,
     * "end_time":1519869600000,
     * "state":0,
     * "activity_type":2,
     * "agency_id":13,
     * "red_count":100,
     * "draw_statistics":null,
     * "canEditDel":true
     */

    private int id;
    private long create_date;
    private boolean deleted;
    private String name;
    private String cover;
    private String descri;
    private long start_time;   //代金券开始时间
    private long end_time;      //代金券结束时间
    private int state;  //1禁用、0未禁用
    private int activity_type;  //1平台红包线上、2商户代金券、3平台红包线下
    private int agency_id;
    private int red_count;  //红包数量
    private Draw_statistics draw_statistics;
    private boolean canEditDel;
    private List<DenominationListBean> denomination_list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(int activity_type) {
        this.activity_type = activity_type;
    }

    public int getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(int agency_id) {
        this.agency_id = agency_id;
    }

    public int getRed_count() {
        return red_count;
    }

    public void setRed_count(int red_count) {
        this.red_count = red_count;
    }

    public Draw_statistics getDraw_statistics() {
        return draw_statistics;
    }

    public void setDraw_statistics(Draw_statistics draw_statistics) {
        this.draw_statistics = draw_statistics;
    }

    public boolean isCanEditDel() {
        return canEditDel;
    }

    public void setCanEditDel(boolean canEditDel) {
        this.canEditDel = canEditDel;
    }

    public List<DenominationListBean> getDenomination_list() {
        return denomination_list;
    }

    public void setDenomination_list(List<DenominationListBean> denomination_list) {
        this.denomination_list = denomination_list;
    }

    public class Draw_statistics implements Serializable {
        //        "red_total_count":"string,红包总数量",
//                "red_draw_count":"string,已领取总个数",
//                "red_use_count":"string,已使用总个数",
//                "red_use_price":"string,已使用总金额",
//                "red_expired_price":"string, 已过期总金额"
        private int red_total_count;
        private int red_draw_count;
        private int red_use_count;
        private int red_use_price;
        private int red_expired_price;

        public int getRed_total_count() {
            return red_total_count;
        }

        public void setRed_total_count(int red_total_count) {
            this.red_total_count = red_total_count;
        }

        public int getRed_draw_count() {
            return red_draw_count;
        }

        public void setRed_draw_count(int red_draw_count) {
            this.red_draw_count = red_draw_count;
        }

        public int getRed_use_count() {
            return red_use_count;
        }

        public void setRed_use_count(int red_use_count) {
            this.red_use_count = red_use_count;
        }

        public int getRed_use_price() {
            return red_use_price;
        }

        public void setRed_use_price(int red_use_price) {
            this.red_use_price = red_use_price;
        }

        public int getRed_expired_price() {
            return red_expired_price;
        }

        public void setRed_expired_price(int red_expired_price) {
            this.red_expired_price = red_expired_price;
        }
    }

    public class DenominationListBean implements Serializable {
        /**
         * "id":7,
         * "create_date":1516092454000,
         * "deleted":false,
         * "activity_id":1,
         * "max_price":100,
         * "use_price":1,
         * "date_type":0,
         * "draw_days":null,
         * "valid_time":1519869600000,
         * "start_time":1517450400000,
         * "use_type":2,
         * "sea_amoy_use_type":3,
         * "sea_amoy_channelIds":null,
         * "sea_amoy_channelNames":null,
         * "special_selling_use_type":3,
         * "special_selling_channelIds":null,
         * "special_selling_channelNames":null,
         * "partner_use_type":2,
         * "agency_ids":"13",
         * "agency_names":"哈哈"
         */

        private int id;
        private long create_date;
        private boolean deleted;
        private int activity_id;
        private int max_price;  //满多少使用
        private int use_price;  //送多少
        private int date_type;
        private String draw_days;
        private long valid_time;    //有效结束时间
        private long start_time;    //有效开始时间
        private int use_type;
        private int sea_amoy_use_type;
        private String sea_amoy_channelIds;
        private String sea_amoy_channelNames;
        private int special_selling_use_type;
        private String special_selling_channelIds;
        private String special_selling_channelNames;
        private int partner_use_type;
        private int agency_ids;
        private String agency_names;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getCreate_date() {
            return create_date;
        }

        public void setCreate_date(long create_date) {
            this.create_date = create_date;
        }

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }

        public int getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(int activity_id) {
            this.activity_id = activity_id;
        }

        public int getMax_price() {
            return max_price;
        }

        public void setMax_price(int max_price) {
            this.max_price = max_price;
        }

        public int getUse_price() {
            return use_price;
        }

        public void setUse_price(int use_price) {
            this.use_price = use_price;
        }

        public int getDate_type() {
            return date_type;
        }

        public void setDate_type(int date_type) {
            this.date_type = date_type;
        }

        public String getDraw_days() {
            return draw_days;
        }

        public void setDraw_days(String draw_days) {
            this.draw_days = draw_days;
        }

        public long getValid_time() {
            return valid_time;
        }

        public void setValid_time(long valid_time) {
            this.valid_time = valid_time;
        }

        public long getStart_time() {
            return start_time;
        }

        public void setStart_time(long start_time) {
            this.start_time = start_time;
        }

        public int getUse_type() {
            return use_type;
        }

        public void setUse_type(int use_type) {
            this.use_type = use_type;
        }

        public int getSea_amoy_use_type() {
            return sea_amoy_use_type;
        }

        public void setSea_amoy_use_type(int sea_amoy_use_type) {
            this.sea_amoy_use_type = sea_amoy_use_type;
        }

        public String getSea_amoy_channelIds() {
            return sea_amoy_channelIds;
        }

        public void setSea_amoy_channelIds(String sea_amoy_channelIds) {
            this.sea_amoy_channelIds = sea_amoy_channelIds;
        }

        public String getSea_amoy_channelNames() {
            return sea_amoy_channelNames;
        }

        public void setSea_amoy_channelNames(String sea_amoy_channelNames) {
            this.sea_amoy_channelNames = sea_amoy_channelNames;
        }

        public int getSpecial_selling_use_type() {
            return special_selling_use_type;
        }

        public void setSpecial_selling_use_type(int special_selling_use_type) {
            this.special_selling_use_type = special_selling_use_type;
        }

        public String getSpecial_selling_channelIds() {
            return special_selling_channelIds;
        }

        public void setSpecial_selling_channelIds(String special_selling_channelIds) {
            this.special_selling_channelIds = special_selling_channelIds;
        }

        public String getSpecial_selling_channelNames() {
            return special_selling_channelNames;
        }

        public void setSpecial_selling_channelNames(String special_selling_channelNames) {
            this.special_selling_channelNames = special_selling_channelNames;
        }

        public int getPartner_use_type() {
            return partner_use_type;
        }

        public void setPartner_use_type(int partner_use_type) {
            this.partner_use_type = partner_use_type;
        }

        public int getAgency_ids() {
            return agency_ids;
        }

        public void setAgency_ids(int agency_ids) {
            this.agency_ids = agency_ids;
        }

        public String getAgency_names() {
            return agency_names;
        }

        public void setAgency_names(String agency_names) {
            this.agency_names = agency_names;
        }
    }

}
