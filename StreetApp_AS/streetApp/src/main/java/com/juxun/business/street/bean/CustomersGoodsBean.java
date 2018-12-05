/**
 * 
 */
package com.juxun.business.street.bean;

import java.io.Serializable;

/**   
 * 项目名称：Street   
 * 类名称：CustomersGoodsBean   
 * 类描述：   团购商品BEAN
 * 创建人：WuJianhua   
 * 创建时间：2015年6月1日 下午2:48:20   
 * 修改人：WuJianhua   
 * 修改时间：2015年6月1日 下午2:48:20   
 * 修改备注：   
 * @version    
 *    
 */
@SuppressWarnings("serial")
public class CustomersGoodsBean implements Serializable{
	//团购ID
	private String teamBuyId;
	//团购名称
	private String teamBuyName;
	//现价
	private String teamBuyPrice;
	//原价
	private String teamBuy_cost;
	//开始时间
	private String teambuy_start_date;
	//结束时间
	private String teambuy_end_date;
	//团购券使用开始时间
	private String teambuy_ticket_start_date;
	//团购券使用结束时间
	private String teambuy_ticket_end_date;
	//团购券图片
	private String teambuy_icon;
	//团购券状态
	private int teambuy_state;
	//团购详情URL
	private String teambuy_detail_url;
	public CustomersGoodsBean(String teamBuyId, String teamBuyName,
			String teamBuyPrice, String teamBuy_cost,
			String teambuy_start_date, String teambuy_end_date,
			String teambuy_ticket_start_date, String teambuy_ticket_end_date,
			String teambuy_icon, int teambuy_state) {
		super();
		this.teamBuyId = teamBuyId;
		this.teamBuyName = teamBuyName;
		this.teamBuyPrice = teamBuyPrice;
		this.teamBuy_cost = teamBuy_cost;
		this.teambuy_start_date = teambuy_start_date;
		this.teambuy_end_date = teambuy_end_date;
		this.teambuy_ticket_start_date = teambuy_ticket_start_date;
		this.teambuy_ticket_end_date = teambuy_ticket_end_date;
		this.teambuy_icon = teambuy_icon;
		this.teambuy_state = teambuy_state;
	}
	public CustomersGoodsBean() {
		super();
	}
	public String getTeamBuyId() {
		return teamBuyId;
	}
	public void setTeamBuyId(String teamBuyId) {
		this.teamBuyId = teamBuyId;
	}
	public String getTeamBuyName() {
		return teamBuyName;
	}
	public void setTeamBuyName(String teamBuyName) {
		this.teamBuyName = teamBuyName;
	}
	public String getTeamBuyPrice() {
		return teamBuyPrice;
	}
	public void setTeamBuyPrice(String teamBuyPrice) {
		this.teamBuyPrice = teamBuyPrice;
	}
	public String getTeamBuy_cost() {
		return teamBuy_cost;
	}
	public void setTeamBuy_cost(String teamBuy_cost) {
		this.teamBuy_cost = teamBuy_cost;
	}
	public String getTeambuy_start_date() {
		return teambuy_start_date;
	}
	public void setTeambuy_start_date(String teambuy_start_date) {
		this.teambuy_start_date = teambuy_start_date;
	}
	public String getTeambuy_end_date() {
		return teambuy_end_date;
	}
	public void setTeambuy_end_date(String teambuy_end_date) {
		this.teambuy_end_date = teambuy_end_date;
	}
	public String getTeambuy_ticket_start_date() {
		return teambuy_ticket_start_date;
	}
	public void setTeambuy_ticket_start_date(String teambuy_ticket_start_date) {
		this.teambuy_ticket_start_date = teambuy_ticket_start_date;
	}
	public String getTeambuy_ticket_end_date() {
		return teambuy_ticket_end_date;
	}
	public void setTeambuy_ticket_end_date(String teambuy_ticket_end_date) {
		this.teambuy_ticket_end_date = teambuy_ticket_end_date;
	}
	public String getTeambuy_icon() {
		return teambuy_icon;
	}
	public void setTeambuy_icon(String teambuy_icon) {
		this.teambuy_icon = teambuy_icon;
	}
	public int getTeambuy_state() {
		return teambuy_state;
	}
	public void setTeambuy_state(int teambuy_state) {
		this.teambuy_state = teambuy_state;
	}
	public String getTeambuy_detail_url() {
		return teambuy_detail_url;
	}
	public void setTeambuy_detail_url(String teambuy_detail_url) {
		this.teambuy_detail_url = teambuy_detail_url;
	}
	
}
