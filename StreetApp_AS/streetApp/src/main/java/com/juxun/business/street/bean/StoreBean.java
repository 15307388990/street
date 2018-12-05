/**
 *
 */
package com.juxun.business.street.bean;

import java.io.Serializable;

/**
 * 项目名称：Street 类名称：UserBean 类描述： 创建人：WuJianhua 创建时间：2015年6月2日 下午4:25:59
 * 修改人：WuJianhua 修改时间：2015年6月2日 下午4:25:59 修改备注：
 */
@SuppressWarnings("serial")
public class StoreBean implements Serializable {

    private String auth_token;// 授权令牌
    // admin_id int 管理员id
    private String admin_id;// 管理员id
    // admin_agency int 机构id
    private String admin_agency;// 机构id
    private String admin_qid;// 社区ID
    private String agency_name;// 商户名称
    private boolean isSn;// 设备是否为POS机
    private String phone;
    private String admin_account;// 登录账号
    private PaySet pay_set;
    private Agency agency;// 机构
    private String qrcode_url;// 店铺图片二维码
    private String phone_token;//// token 用来设置支付密码

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public String getPhone_token() {
        return phone_token;
    }

    public void setPhone_token(String phone_token) {
        this.phone_token = phone_token;
    }

    public String getQrcode_url() {
        return qrcode_url;
    }

    public void setQrcode_url(String qrcode_url) {
        this.qrcode_url = qrcode_url;
    }

    public String getAdmin_account() {
        return admin_account;
    }

    public PaySet getPay_set() {
        return pay_set;
    }

    public void setPay_set(PaySet pay_set) {
        this.pay_set = pay_set;
    }

    public void setAdmin_account(String admin_account) {
        this.admin_account = admin_account;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSn() {
        return isSn;
    }

    public void setSn(boolean isSn) {
        this.isSn = isSn;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getAdmin_agency() {
        if (admin_agency == null || admin_agency.isEmpty()) {
            return "";
        } else {
            return admin_agency;
        }

    }

    public void setAdmin_agency(String admin_agency) {
        this.admin_agency = admin_agency;
    }

    public String getAdmin_qid() {
        return admin_qid;
    }

    public void setAdmin_qid(String admin_qid) {
        this.admin_qid = admin_qid;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
    }

    /**
     *
     */
    public class PaySet implements Serializable {
        private static final long serialVersionUID = 1L;
        private String pay_password;// 是否包含支付密码如果为null为尚未设置支付密码 如果为0则支付密码以及设置过
        private String safe_phone;// 密保手机
        private String remaining_balance;// 账户余额

        public String getPay_password() {
            return pay_password;
        }

        public void setPay_password(String pay_password) {
            this.pay_password = pay_password;
        }

        public String getSafe_phone() {
            return safe_phone;
        }

        public void setSafe_phone(String safe_phone) {
            this.safe_phone = safe_phone;
        }

        public String getRemaining_balance() {
            return remaining_balance;
        }

        public void setRemaining_balance(String remaining_balance) {
            this.remaining_balance = remaining_balance;
        }

    }

}
