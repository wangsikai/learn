package com.lanking.cloud.domain.yoo.order.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lanking.cloud.domain.common.baseData.Express;
import com.lanking.cloud.domain.yoo.order.GoodsOrderBaseInfo;
import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;

/**
 * 金币兑换订单基类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public abstract class CoinsGoodsOrderBaseInfo extends GoodsOrderBaseInfo {

	private static final long serialVersionUID = 393476299979028495L;

	/**
	 * 订单状态
	 */
	@Column(precision = 3, nullable = false)
	private GoodsOrderStatus status;

	/**
	 * 金币商品ID
	 * 
	 * <pre>
	 * 1.当为抽奖活动商品订单时，此处存储的是抽奖活动商品ID
	 * </pre>
	 */
	@Column(name = "coins_goods_id")
	private long coinsGoodsId;

	/**
	 * 金币商品快照ID
	 * 
	 * <pre>
	 *   1.当为抽奖活动商品订单时，此处存储的是抽奖活动商品快照的ID
	 * </pre>
	 */
	@Column(name = "conis_goods_snapshot_id")
	private long coinsGoodsSnapshotId;

	/**
	 * 特殊参数0
	 * 
	 * <pre>
	 * 	 1.当兑换的为手机话费的话此字段存被充值的手机号
	 * 	 2.当兑换的qq特权的话此字段存被充值的qq号
	 * 	 3.当为现金红包的时候,此字段存储账户名
	 * 	 4.当为手机流量的时候,此字段存储手机号
	 * 	 5.当为卡券的时候,此字段存储手机号
	 * </pre>
	 */
	@Column(name = "p0", length = 250)
	private String p0;

	/**
	 * 特殊参数1
	 * 
	 * <pre>
	 *   1.当为抽奖订单，此字段存储期别的ID
	 * </pre>
	 */
	@Column(name = "p1", length = 20)
	private String p1;

	/**
	 * 快递
	 */
	@Column(name = "express", precision = 3)
	private Express express;

	/**
	 * 快递号
	 */
	@Column(name = "express_code", length = 64)
	private String expressCode;

	/**
	 * 收货人
	 */
	@Column(name = "contact_name", length = 64)
	private String contactName;

	/**
	 * 收货人电话
	 */
	@Column(name = "contact_phone", length = 32)
	private String contactPhone;

	/**
	 * 收货地区
	 */
	@Column(name = "district_code")
	private Long districtCode;

	/**
	 * 收货详细地址
	 */
	@Column(name = "contact_address", length = 512)
	private String contactAddress;

	/**
	 * 来源
	 */
	@Column(name = "source", precision = 3, columnDefinition = "tinyint default 0")
	private CoinsGoodsOrderSource source = CoinsGoodsOrderSource.EXCHANGE;

	public GoodsOrderStatus getStatus() {
		return status;
	}

	public void setStatus(GoodsOrderStatus status) {
		this.status = status;
	}

	public long getCoinsGoodsId() {
		return coinsGoodsId;
	}

	public void setCoinsGoodsId(long coinsGoodsId) {
		this.coinsGoodsId = coinsGoodsId;
	}

	public long getCoinsGoodsSnapshotId() {
		return coinsGoodsSnapshotId;
	}

	public void setCoinsGoodsSnapshotId(long coinsGoodsSnapshotId) {
		this.coinsGoodsSnapshotId = coinsGoodsSnapshotId;
	}

	public String getP0() {
		return p0;
	}

	public void setP0(String p0) {
		this.p0 = p0;
	}

	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public CoinsGoodsOrderSource getSource() {
		return source;
	}

	public void setSource(CoinsGoodsOrderSource source) {
		this.source = source;
	}

	public Express getExpress() {
		return express;
	}

	public void setExpress(Express express) {
		this.express = express;
	}

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public Long getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(Long districtCode) {
		this.districtCode = districtCode;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

}
