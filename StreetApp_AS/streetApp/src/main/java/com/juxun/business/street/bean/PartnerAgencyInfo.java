package com.juxun.business.street.bean;

/**
 * 合伙人机构详细资料
 */
public class PartnerAgencyInfo {

	/**
	 * 身份证号
	 */
	private String idNumber;

	/**
	 * 身份证正反图片，以逗号隔开
	 */
	private String idNumberPositiveAndNegativeIcon;

	/**
	 * 手拿身份证icon
	 */
	private String handIdNumberIcon;

	/**
	 * 营业执照icon
	 */
	private String businessLicenceIcon;

	/**
	 * 机构id
	 */
	private Integer agency_id;

	/**
	 * 姓名
	 */
	private String partner_name;

	/**
	 * 身份证有效期开始
	 */
	private String id_number_valid_start;

	/**
	 * 身份证有效期结束
	 */
	private String id_number_valid_end;

	/**
	 * 营业执照号
	 */
	private String business_licence_code;

	/**
	 * 邮箱
	 */
	private String partner_email;

	/**
	 * 店铺名
	 */
	private String mall_name;

	/**
	 * 获取身份证号
	 * 
	 * @return idNumber 身份证号
	 */
	public String getIdNumber() {
		return idNumber;
	}

	/**
	 * 设置身份证号
	 * 
	 * @param idNumber
	 *            身份证号
	 */
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	/**
	 * 获取身份证正反图片，以逗号隔开
	 * 
	 * @return idNumberPositiveAndNegativeIcon 身份证正反图片，以逗号隔开
	 */
	public String getIdNumberPositiveAndNegativeIcon() {
		return idNumberPositiveAndNegativeIcon;
	}

	/**
	 * 设置身份证正反图片，以逗号隔开
	 * 
	 * @param idNumberPositiveAndNegativeIcon
	 *            身份证正反图片，以逗号隔开
	 */
	public void setIdNumberPositiveAndNegativeIcon(String idNumberPositiveAndNegativeIcon) {
		this.idNumberPositiveAndNegativeIcon = idNumberPositiveAndNegativeIcon;
	}

	/**
	 * 获取手拿身份证icon
	 * 
	 * @return handIdNumberIcon 手拿身份证icon
	 */
	public String getHandIdNumberIcon() {
		return handIdNumberIcon;
	}

	/**
	 * 设置手拿身份证icon
	 * 
	 * @param handIdNumberIcon
	 *            手拿身份证icon
	 */
	public void setHandIdNumberIcon(String handIdNumberIcon) {
		this.handIdNumberIcon = handIdNumberIcon;
	}

	/**
	 * 获取营业执照icon
	 * 
	 * @return businessLicenceIcon 营业执照icon
	 */
	public String getBusinessLicenceIcon() {
		return businessLicenceIcon;
	}

	/**
	 * 设置营业执照icon
	 * 
	 * @param businessLicenceIcon
	 *            营业执照icon
	 */
	public void setBusinessLicenceIcon(String businessLicenceIcon) {
		this.businessLicenceIcon = businessLicenceIcon;
	}

	/**
	 * 获取机构id
	 * 
	 * @return agency_id 机构id
	 */
	public Integer getAgency_id() {
		return agency_id;
	}

	/**
	 * 设置机构id
	 * 
	 * @param agency_id
	 *            机构id
	 */
	public void setAgency_id(Integer agency_id) {
		this.agency_id = agency_id;
	}

	public String getPartner_name() {
		return partner_name;
	}

	public void setPartner_name(String partner_name) {
		this.partner_name = partner_name;
	}

	public String getId_number_valid_start() {
		return id_number_valid_start;
	}

	public void setId_number_valid_start(String id_number_valid_start) {
		this.id_number_valid_start = id_number_valid_start;
	}

	public String getId_number_valid_end() {
		return id_number_valid_end;
	}

	public void setId_number_valid_end(String id_number_valid_end) {
		this.id_number_valid_end = id_number_valid_end;
	}

	public String getBusiness_licence_code() {
		return business_licence_code;
	}

	public void setBusiness_licence_code(String business_licence_code) {
		this.business_licence_code = business_licence_code;
	}

	public String getPartner_email() {
		return partner_email;
	}

	public void setPartner_email(String partner_email) {
		this.partner_email = partner_email;
	}

	public String getMall_name() {
		return mall_name;
	}

	public void setMall_name(String mall_name) {
		this.mall_name = mall_name;
	}

}
