package com.lanking.uxb.zycon.operation.form;

import java.util.List;

import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.common.BannerLocation;
import com.lanking.cloud.domain.yoo.common.BannerStatus;

public class BannerForm {

	private Long imageId;
	private String url;
	private Integer sequence;
	private YooApp app;
	private BannerLocation location;
	private BannerStatus status;
	private Long userId;
	// 如果是编辑页面需要传值
	private Long id;
	private String startAt;
	private String endAt;
	private List<Long> ids;
	// 是否需要校验banner数量(只有等编辑时候，没有修改位置的时候才不需要校验banner数量)
	private boolean needCheckCount = true;
	// 真实的状态,存在手动设置上架和下架时间的情况
	private String realStatus;

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public YooApp getApp() {
		return app;
	}

	public void setApp(YooApp app) {
		this.app = app;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStartAt() {
		return startAt;
	}

	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}

	public String getEndAt() {
		return endAt;
	}

	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public boolean isNeedCheckCount() {
		return needCheckCount;
	}

	public void setNeedCheckCount(boolean needCheckCount) {
		this.needCheckCount = needCheckCount;
	}

	public String getRealStatus() {
		return realStatus;
	}

	public void setRealStatus(String realStatus) {
		this.realStatus = realStatus;
	}

}
