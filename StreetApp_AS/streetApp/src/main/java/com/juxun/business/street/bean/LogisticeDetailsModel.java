package com.juxun.business.street.bean;


/**
 * ��������
 */
import java.io.Serializable;

public class LogisticeDetailsModel implements Serializable {

	private static final long serialVersionUID = -1804430668719164058L;
	private String time;
	private String context;
	private String ftime;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getFtime() {
		return ftime;
	}
	public void setFtime(String ftime) {
		this.ftime = ftime;
	}
}
