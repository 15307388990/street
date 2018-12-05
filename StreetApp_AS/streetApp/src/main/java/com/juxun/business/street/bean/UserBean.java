/**
 * 
 */
package com.juxun.business.street.bean;

import java.io.Serializable;

/**
 * 
 * 项目名称：Street 类名称：UserBean 类描述： 创建人：WuJianhua 创建时间：2015年6月2日 下午4:25:59
 * 修改人：WuJianhua 修改时间：2015年6月2日 下午4:25:59 修改备注：
 * 
 * @version
 * 
 */
@SuppressWarnings("serial")
public class UserBean implements Serializable {
	// private int userId;// 用户ID
	// private String userName;// 用户名
	// private String userNickname;// 用户昵称
	// private int storeId;// 商铺ID
	private String authToken;// 授权令牌
	private int userid;// 商户id
	private int oper_id;// 操作员id
	private int out_id;// 门店id
	private int storeId;// 商铺ID
	private String storename;// 商户名称
	public String getStorename() {
		return storename;
	}

	public void setStorename(String storename) {
		this.storename = storename;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public int getOut_id() {
		return out_id;
	}

	public void setOut_id(int out_id) {
		this.out_id = out_id;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public int getUserId() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}


	public int getOper_id() {
		return oper_id;
	}

	public void setOper_id(int oper_id) {
		this.oper_id = oper_id;
	}

}
