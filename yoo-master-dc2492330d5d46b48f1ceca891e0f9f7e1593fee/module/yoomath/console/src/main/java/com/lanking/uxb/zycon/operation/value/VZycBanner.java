package com.lanking.uxb.zycon.operation.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.common.BannerLocation;
import com.lanking.cloud.domain.yoo.common.BannerStatus;

public class VZycBanner implements Serializable {

	private static final long serialVersionUID = 6084816590482029274L;

	private Long id;

	private Integer sequence;

	// 跳转的链接
	private String url;
	// 图片地址
	private String imageUrl;

	private Long imageId;
	/**
	 * 位置
	 */
	private BannerLocation location;
	/**
	 * 状态
	 */
	private BannerStatus status;
	/**
	 * 开始时间
	 */
	private Date startAt;
	/**
	 * 结束时间
	 */
	private Date endAt;
	// 为空时表示为web端的
	private YooApp app;

	private Date createAt;
	// 真实的状态,存在手动设置上架和下架时间的情况
	private String realStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public BannerLocation getLocation() {
		return location;
	}

	public void setLocation(BannerLocation location) {
		this.location = location;
	}

	public BannerStatus getStatus() {
		return status;
	}

	public void setStatus(BannerStatus status) {
		this.status = status;
	}

	public Date getStartAt() {
		return startAt;
	}

	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public YooApp getApp() {
		return app;
	}

	public void setApp(YooApp app) {
		this.app = app;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getRealStatus() {
		return realStatus;
	}

	public void setRealStatus(String realStatus) {
		this.realStatus = realStatus;
	}

}
