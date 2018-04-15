package com.lanking.uxb.zycon.operation.convert;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.common.Banner;
import com.lanking.cloud.domain.yoo.common.BannerStatus;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.zycon.operation.value.VZycBanner;

@Component
public class ZycBannerConvert extends Converter<VZycBanner, Banner, Long> {

	@Override
	protected Long getId(Banner s) {
		return s.getId();
	}

	@Override
	protected VZycBanner convert(Banner s) {
		VZycBanner v = new VZycBanner();
		v.setId(s.getId());
		v.setSequence(s.getSequence());
		v.setUrl(s.getUrl());
		v.setImageUrl(FileUtil.getUrl(s.getImageId()));
		v.setImageId(s.getImageId());
		v.setApp(s.getApp());
		v.setLocation(s.getLocation());
		v.setStartAt(s.getStartAt());
		v.setCreateAt(s.getCreateAt());
		v.setEndAt(s.getEndAt());
		v.setStatus(s.getStatus());
		if (s.getStatus() == BannerStatus.PUBLISH) {
			if (s.getEndAt() == null && s.getStartAt() == null) {
				v.setRealStatus("已上架");
			} else {
				if (s.getEndAt() != null) {
					if (s.getStartAt().getTime() < new Date().getTime()
							&& s.getEndAt().getTime() > new Date().getTime()) {
						v.setRealStatus("已上架");
					} else if (s.getEndAt().getTime() < new Date().getTime()) {
						v.setRealStatus("已下架");
					} else {
						v.setRealStatus("待上架");
					}
				} else {
					if (s.getStartAt().getTime() < new Date().getTime()) {
						v.setRealStatus("已上架");
					} else {
						v.setRealStatus("待上架");
					}
				}
			}
		} else if (s.getStatus() == BannerStatus.UN_PUBLISH) {
			v.setRealStatus("已下架");
		} else if (s.getStatus() == BannerStatus.DRAFT) {
			if (s.getEndAt() != null) {
				if (s.getStartAt().getTime() < new Date().getTime() && s.getEndAt().getTime() > new Date().getTime()) {
					v.setRealStatus("已上架");
				} else if (s.getEndAt().getTime() < new Date().getTime()) {
					v.setRealStatus("已下架");
				} else {
					v.setRealStatus("待上架");
				}
			} else {
				if (s.getStartAt().getTime() < new Date().getTime()) {
					v.setRealStatus("已上架");
				} else {
					v.setRealStatus("待上架");
				}
			}
		}
		return v;
	}
}
