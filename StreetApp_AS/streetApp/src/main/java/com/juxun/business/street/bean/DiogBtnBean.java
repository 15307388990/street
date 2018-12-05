package com.juxun.business.street.bean;

import java.io.Serializable;
import java.util.List;

public class DiogBtnBean implements Serializable {
	/**
	 * 弹框 按钮实体类
	 */
	private static final long serialVersionUID = 1L;
	private String message;// 消息摘要
	private String title;// 弹框标题
	private BtnBean cancel_btn;// 取消按钮
	private List<BtnBean> btn_list;// 按钮集合

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BtnBean getCancel_btn() {
		return cancel_btn;
	}

	public void setCancel_btn(BtnBean cancel_btn) {
		this.cancel_btn = cancel_btn;
	}


	public List<BtnBean> getBtn_list() {
		return btn_list;
	}

	public void setBtn_list(List<BtnBean> btn_list) {
		this.btn_list = btn_list;
	}


	public class BtnBean {
		private String name;// 按钮名称
		private String method;// 需要调用的函数
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getMethod() {
			return method;
		}
		public void setMethod(String method) {
			this.method = method;
		}
		
	}
}
