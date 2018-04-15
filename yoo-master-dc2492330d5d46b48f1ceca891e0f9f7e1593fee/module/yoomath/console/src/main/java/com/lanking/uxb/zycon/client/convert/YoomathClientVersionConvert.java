package com.lanking.uxb.zycon.client.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.version.YoomathClientVersion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.zycon.client.value.VZycVersion;

@Component
public class YoomathClientVersionConvert extends Converter<VZycVersion, YoomathClientVersion, Long> {

	@Override
	protected Long getId(YoomathClientVersion s) {
		return s.getId();
	}

	@Override
	protected VZycVersion convert(YoomathClientVersion s) {
		VZycVersion v = new VZycVersion();
		v.setId(s.getId());
		v.setName(s.getName());
		v.setSize(s.getSize());
		v.setStatus(s.getStatus());
		v.setType(s.getType());
		v.setVersion(s.getVersion());
		v.setDescription(s.getDescription());
		v.setDownloadUrl(s.getDownloadUrl());
		v.setUpdateTime(s.getUpdateAt());
		v.setUpgradeFlag(s.getUpgradeFlag());
		v.setApp(s.getApp());
		v.setAppTitle(s.getApp().getTitle());
		v.setDeviceType(s.getDeviceType());
		return v;
	}
}
