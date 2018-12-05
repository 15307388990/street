package com.juxun.business.street.bean;

import java.io.Serializable;

public class MessageBean implements Serializable {
	/**
	 * 消息实体
	 */
	private static final long serialVersionUID = 1L;
	private int id;// string,id",
	private String message_abstract;// string,消息摘要",
	private long createDate;// string,创建时间",
	private String message_url;// string,消息跳转url",
	private String message_context;// string,消息富文本内容",
	private String message_type;// integer,1.系统通知 2.活动通知 0普通通知",
	private String message_name;// string,消息名称",
	private String message_icon;// string,消息icon",
	private int context_type;// integer,1.纯文本2超链接3富文本"

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage_abstract() {
		return message_abstract;
	}

	public void setMessage_abstract(String message_abstract) {
		this.message_abstract = message_abstract;
	}

	public String getMessage_url() {
		return message_url;
	}

	public void setMessage_url(String message_url) {
		this.message_url = message_url;
	}

	public String getMessage_context() {
		return message_context;
	}

	public void setMessage_context(String message_context) {
		this.message_context = message_context;
	}

	public String getMessage_type() {
		return message_type;
	}

	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}

	public String getMessage_name() {
		return message_name;
	}

	public void setMessage_name(String message_name) {
		this.message_name = message_name;
	}

	public String getMessage_icon() {
		return message_icon;
	}

	public void setMessage_icon(String message_icon) {
		this.message_icon = message_icon;
	}

	public int getContext_type() {
		return context_type;
	}

	public void setContext_type(int context_type) {
		this.context_type = context_type;
	}

}
