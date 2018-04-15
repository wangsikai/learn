package com.lanking.cloud.domain.yoo.goods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

/**
 * 商品信息基类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public class GoodsBaseInfo implements Serializable {

	private static final long serialVersionUID = 2290785208067860L;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 简介描述
	 */
	@Column(name = "introduction", length = 1024)
	private String introduction;

	/**
	 * 详情描述
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "content")
	private String content;

	/**
	 * 图片
	 */
	@Column(name = "image")
	private Long image;

	/**
	 * 金币价格(历史原因，保持不变，0表示免费,null表示不支持)
	 */
	@Column(name = "price", scale = 2)
	private BigDecimal price;

	/**
	 * 人民币价格(0表示免费,null表示不支持)
	 */
	@Column(name = "price_rmb", scale = 2)
	private BigDecimal priceRMB;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 创建人ID
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	/**
	 * 创建人ID
	 */
	@Column(name = "update_id")
	private Long updateId;

	/**
	 * 上架时间
	 */
	@Column(name = "sales_time", columnDefinition = "datetime(3)")
	private Date salesTime;

	/**
	 * 下架时间
	 */
	@Column(name = "soldout_time", columnDefinition = "datetime(3)")
	private Date soldOutTime;

	/**
	 * 商品类型(系统内通过表分开了)
	 */
	@Column(name = "type", precision = 3, columnDefinition = "tinyint default 0")
	private GoodsType type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getImage() {
		return image;
	}

	public void setImage(Long image) {
		this.image = image;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPriceRMB() {
		return priceRMB;
	}

	public void setPriceRMB(BigDecimal priceRMB) {
		this.priceRMB = priceRMB;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public Date getSalesTime() {
		return salesTime;
	}

	public void setSalesTime(Date salesTime) {
		this.salesTime = salesTime;
	}

	public Date getSoldOutTime() {
		return soldOutTime;
	}

	public void setSoldOutTime(Date soldOutTime) {
		this.soldOutTime = soldOutTime;
	}

	public GoodsType getType() {
		return type;
	}

	public void setType(GoodsType type) {
		this.type = type;
	}

}
