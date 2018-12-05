package com.juxun.business.street.bean;

import java.io.Serializable;

public class Agreement7 implements Serializable {
	/**
	 * 协议7对象
	 */
	private static final long serialVersionUID = 1L;
	private String title;// 头部显示标题
	private String link_url;// 外部链接地址
	private String title_color;// 头部颜色
	private RightButton right_button;//按钮

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink_url() {
		return link_url;
	}

	public void setLink_url(String link_url) {
		this.link_url = link_url;
	}

	public String getTitle_color() {
		return title_color;
	}

	public void setTitle_color(String title_color) {
		this.title_color = title_color;
	}

	public RightButton getRight_button() {
		return right_button;
	}

	public void setRight_button(RightButton right_button) {
		this.right_button = right_button;
	}

	public class RightButton implements Serializable  {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String button_icon;// 按钮图片 文字和图片2者至少有一个
		private String button_name;// 按钮文字 文字和图片2者至少有一个
		private String button_method;// 按钮点击后执行的js函数
		private String button_text_color;//按钮颜色
		public String getButton_text_color() {
			return button_text_color;
		}
		public void setButton_text_color(String button_text_color) {
			this.button_text_color = button_text_color;
		}
		public String getButton_icon() {
			return button_icon;
		}
		public void setButton_icon(String button_icon) {
			this.button_icon = button_icon;
		}
		public String getButton_name() {
			return button_name;
		}
		public void setButton_name(String button_name) {
			this.button_name = button_name;
		}
		public String getButton_method() {
			return button_method;
		}
		public void setButton_method(String button_method) {
			this.button_method = button_method;
		}
	}
}
