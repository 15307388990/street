package com.juxun.business.street.bean;


import java.io.Serializable;
import java.util.List;


/**
 * 供应商商品实体，由城市服务商添加供应商商品
 * 
 * @author 
 *
 */
public class SupplierCommodityBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer supplierId;// 供应商Id
	private Integer serviceAgencyId;// 服务商机构ID
	private String commodityName;// 商品名称
	private String commoditydesc;// 商品描述
	private String commodityUnit;// 单位名称
	private Double retailPrice;// 建议零售价
	private Integer wholesaleType = 0;// 批发价定义类型0统一价1阶梯价
	private Double priceLow;// 最低价 无规格时low = high = 真实价格
	private Double priceHigh;// 最高价 无规格时low = high = 真实价格
	private Integer firstLevelChannelId = 0;// 一级频道
	private Integer secodeLevelChannelId = 0;// 二级频道
	private Integer codeType = 0;// 0有条码1无条码
	private String code;// 条码
	private Integer commodityInventory;// 总库存（当前库存等于总库存-销售），修改总库存只能大于当前库存
	private Integer commoditySales;// 销售
	private Integer vitualSales;// 虚拟销量
	private Integer commodityState = 1;// 商品状态 1.审核中 2.审核通过 3.审核失败 4.撤销
	private String commodityICon;// 图片集，以逗号隔开
	private List<LadderBean> ladderList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getServiceAgencyId() {
		return serviceAgencyId;
	}

	public void setServiceAgencyId(Integer serviceAgencyId) {
		this.serviceAgencyId = serviceAgencyId;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getCommoditydesc() {
		return commoditydesc;
	}

	public void setCommoditydesc(String commoditydesc) {
		this.commoditydesc = commoditydesc;
	}

	public String getCommodityUnit() {
		return commodityUnit;
	}

	public void setCommodityUnit(String commodityUnit) {
		this.commodityUnit = commodityUnit;
	}

	public Double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}

	public Integer getWholesaleType() {
		return wholesaleType;
	}

	public void setWholesaleType(Integer wholesaleType) {
		this.wholesaleType = wholesaleType;
	}

	public Double getPriceLow() {
		return priceLow;
	}

	public void setPriceLow(Double priceLow) {
		this.priceLow = priceLow;
	}

	public Double getPriceHigh() {
		return priceHigh;
	}

	public void setPriceHigh(Double priceHigh) {
		this.priceHigh = priceHigh;
	}

	public Integer getFirstLevelChannelId() {
		return firstLevelChannelId;
	}

	public void setFirstLevelChannelId(Integer firstLevelChannelId) {
		this.firstLevelChannelId = firstLevelChannelId;
	}

	public Integer getSecodeLevelChannelId() {
		return secodeLevelChannelId;
	}

	public void setSecodeLevelChannelId(Integer secodeLevelChannelId) {
		this.secodeLevelChannelId = secodeLevelChannelId;
	}

	public Integer getCodeType() {
		return codeType;
	}

	public void setCodeType(Integer codeType) {
		this.codeType = codeType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getCommodityInventory() {
		return commodityInventory;
	}

	public void setCommodityInventory(Integer commodityInventory) {
		this.commodityInventory = commodityInventory;
	}

	public Integer getCommoditySales() {
		return commoditySales;
	}

	public void setCommoditySales(Integer commoditySales) {
		this.commoditySales = commoditySales;
	}

	public Integer getVitualSales() {
		return vitualSales;
	}

	public void setVitualSales(Integer vitualSales) {
		this.vitualSales = vitualSales;
	}

	public Integer getCommodityState() {
		return commodityState;
	}

	public void setCommodityState(Integer commodityState) {
		this.commodityState = commodityState;
	}

	public String getCommodityICon() {
		return commodityICon;
	}

	public void setCommodityICon(String commodityICon) {
		this.commodityICon = commodityICon;
	}

	public List<LadderBean> getLadderList() {
		return ladderList;
	}

	public void setLadderList(List<LadderBean> ladderList) {
		this.ladderList = ladderList;
	}

}
