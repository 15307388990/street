/**
 * 
 */
package com.juxun.business.street.bean;

import java.io.Serializable;

/**   
 *    
 * 项目名称：Street   
 * 类名称：TicketBean   
 * 类描述：  团购券
 * 创建人：WuJianhua   
 * 创建时间：2015年6月2日 下午7:02:48   
 * 修改人：WuJianhua   
 * 修改时间：2015年6月2日 下午7:02:48   
 * 修改备注：   
 * @version    
 *    
 */
public class TicketBean implements Serializable {
	private int ticket_id;//团购券ID
	private String ticket_name;//团购券名称
	private String ticket_icon;//团购券图片
	private String ticket_phone;//电话
	private double ticket_price;//价格
	private String ticket_creat_date;//创建日期
	private String ticket_sn;//SN码
	private int ticket_state;//团购券状态
	private String ticket_start_date;//团购券使用开始日期
	private String ticket_end_date;//团购券使用结束日期
	public int getTicket_id() {
		return ticket_id;
	}
	public void setTicket_id(int ticket_id) {
		this.ticket_id = ticket_id;
	}
	public String getTicket_name() {
		return ticket_name;
	}
	public void setTicket_name(String ticket_name) {
		this.ticket_name = ticket_name;
	}
	public String getTicket_icon() {
		return ticket_icon;
	}
	public void setTicket_icon(String ticket_icon) {
		this.ticket_icon = ticket_icon;
	}
	public String getTicket_phone() {
		return ticket_phone;
	}
	public void setTicket_phone(String ticket_phone) {
		this.ticket_phone = ticket_phone;
	}
	public double getTicket_price() {
		return ticket_price;
	}
	public void setTicket_price(double ticket_price) {
		this.ticket_price = ticket_price;
	}
	public String getTicket_creat_date() {
		return ticket_creat_date;
	}
	public void setTicket_creat_date(String ticket_creat_date) {
		this.ticket_creat_date = ticket_creat_date;
	}
	public String getTicket_sn() {
		return ticket_sn;
	}
	public void setTicket_sn(String ticket_sn) {
		this.ticket_sn = ticket_sn;
	}
	public int getTicket_state() {
		return ticket_state;
	}
	public void setTicket_state(int ticket_state) {
		this.ticket_state = ticket_state;
	}
	public String getTicket_start_date() {
		return ticket_start_date;
	}
	public void setTicket_start_date(String ticket_start_date) {
		this.ticket_start_date = ticket_start_date;
	}
	public String getTicket_end_date() {
		return ticket_end_date;
	}
	public void setTicket_end_date(String ticket_end_date) {
		this.ticket_end_date = ticket_end_date;
	}
	
}
