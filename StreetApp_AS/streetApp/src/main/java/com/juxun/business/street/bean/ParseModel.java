/**
 * 
 */
package com.juxun.business.street.bean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author WuJianHua 解析数据工具
 */
public class ParseModel {

	public static UserBean loginBean;
	public static StoreBean storeBean;
	public static boolean isdate=false;

	/** 解析登录返回的数据 */
	public static void parseLoginData(String data) {
		Gson gson = new Gson();
		//loginBean = gson.fromJson(data, UserBean.class);
		storeBean = gson.fromJson(data, StoreBean.class);
	}

	/** 解析团购券信息 */
	public static TicketBean parseTicket(String data) {
		Gson gson = new Gson();
		return gson.fromJson(data, TicketBean.class);
	}

	/** 解析团购列表 */
	public static List<CustomersGoodsBean> parseCustomersGoodsBeanList(
			String data) {
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<CustomersGoodsBean>>() {
		}.getType();
		return gson.fromJson(data, type);
	}

	/** 解析会员卡列表 */
	public static List<VipBean> parseVipBeanList(String data) {
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<VipBean>>() {
		}.getType();
		return gson.fromJson(data, type);
	}

	/** 解析优惠券列表 */
	public static List<CouponBean> parseCouponBeanList(String data) {
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<CouponBean>>() {
		}.getType();
		return gson.fromJson(data, type);
	}

	/** 解析微信卡劵列表 */
	public static List<ListarchBean> giveVoucherList(String data) {
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<ListarchBean>>() {
		}.getType();
		return gson.fromJson(data, type);
	}
	
	public static List<FinanceWithdrawBean> parseWithdrawList(String data){
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<FinanceWithdrawBean>>(){}.getType();
		return gson.fromJson(data, type);
	}

	/** 解析账单列表 */
	public static List<BillBean> parseBillBeanList(String data) {
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<BillBean>>() {
		}.getType();
		List<BillBean> list = gson.fromJson(data, type);
		if (list == null)
			list = new ArrayList<BillBean>();
		return list;
	}
	/**
	 * 
	 * @param json
	 * @return 获取默认地址
	 * @throws JSONException
	 *             spec[{community_id(社区id),cover(缩略图),goods_count(商品数量),msg_id(
	 *             商品id,当订单为爆品时为爆品id),name(商品名称),price(商品单价),specIds(规格值id),
	 *             specNames(规格值名称),total_price(总价)},] ps:可参照购物车存储
	 */
	public List<Msgmodel> getMsgmodel(String json) throws JSONException {
		List<Msgmodel> msgmodels = JSON.parseArray(json, Msgmodel.class);
		/*JSONArray jsonArray = new JSONArray(json);
		for (int i = 0; i < jsonArray.length(); i++) {
			Msgmodel msgmodel = new Msgmodel();
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			msgmodel.setCommodityICon(jsonObject.getString("cover"));
			msgmodel.setPrice(jsonObject.getDouble("price"));
			msgmodel.setMsg_id(jsonObject.getInt("msg_id"));
			msgmodel.setName(jsonObject.getString("name"));
			msgmodel.setTotal_price(jsonObject.getDouble("total_price"));
			msgmodel.setSpecNames(jsonObject.getString("specNames"));
			msgmodel.setGoods_count(jsonObject.getInt("goods_count"));
			msgmodels.add(msgmodel);
		}
*/
		return msgmodels;

	}
}