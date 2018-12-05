package com.juxun.business.street.bean;


import java.io.Serializable;
import java.util.List;

public class ChannelPDBean implements Serializable {
	/**
	 * 供应商商品分类
	 */
	private static final long serialVersionUID = 1L;
	private int id;// 频道ID
	private String channelName;// 频道名称
	private List<ChannelPDBean> childChannelList;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public List<ChannelPDBean> getChildChannelList() {
		return childChannelList;
	}
	public void setChildChannelList(List<ChannelPDBean> childChannelList) {
		this.childChannelList = childChannelList;
	}

}
