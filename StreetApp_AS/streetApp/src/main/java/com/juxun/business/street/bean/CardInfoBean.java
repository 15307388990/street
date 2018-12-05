package com.juxun.business.street.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 充值卡详情
 */
public class CardInfoBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * giveMoney : 0
     * denominationGroupData : [{"recharge_money":100000,"give_money":10000,"denoNum":0}]
     * realMoney : 0
     * rechargeNum : 0
     * rechargeMoney : 0
     */

    private int giveMoney;//累计充值
    private int realMoney;//累计张数
    private int rechargeNum;//赠送金额
    private int rechargeMoney;//实际充值
    private List<DenominationGroupDataBean> denominationGroupData;

    public int getGiveMoney() {
        return giveMoney;
    }

    public void setGiveMoney(int giveMoney) {
        this.giveMoney = giveMoney;
    }

    public int getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(int realMoney) {
        this.realMoney = realMoney;
    }

    public int getRechargeNum() {
        return rechargeNum;
    }

    public void setRechargeNum(int rechargeNum) {
        this.rechargeNum = rechargeNum;
    }

    public int getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(int rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public List<DenominationGroupDataBean> getDenominationGroupData() {
        return denominationGroupData;
    }

    public void setDenominationGroupData(List<DenominationGroupDataBean> denominationGroupData) {
        this.denominationGroupData = denominationGroupData;
    }

    public class DenominationGroupDataBean implements Serializable {
        /**
         * recharge_money : 100000
         * give_money : 10000
         * denoNum : 0
         */
        private static final long serialVersionUID = 1L;
        private int recharge_money;//面值金额
        private int give_money;//赠送金额
        private int denoNum;//充值张数

        public int getRecharge_money() {
            return recharge_money;
        }

        public void setRecharge_money(int recharge_money) {
            this.recharge_money = recharge_money;
        }

        public int getGive_money() {
            return give_money;
        }

        public void setGive_money(int give_money) {
            this.give_money = give_money;
        }

        public int getDenoNum() {
            return denoNum;
        }

        public void setDenoNum(int denoNum) {
            this.denoNum = denoNum;
        }
    }
}
