package com.lanking.uxb.service.sys.value;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.lanking.cloud.domain.yoo.common.BannerLocation;

public class VBanner implements Serializable {

	private static final long serialVersionUID = -3488831111366005001L;

	private Long id;
	@JSONField(serialize = false)
	private Long imageId;
	private String imageUrl;
	private String url;
	private Integer sequence;
	private BannerLocation bannerLocation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public BannerLocation getBannerLocation() {
		return bannerLocation;
	}

	public void setBannerLocation(BannerLocation bannerLocation) {
		this.bannerLocation = bannerLocation;
	}
}
