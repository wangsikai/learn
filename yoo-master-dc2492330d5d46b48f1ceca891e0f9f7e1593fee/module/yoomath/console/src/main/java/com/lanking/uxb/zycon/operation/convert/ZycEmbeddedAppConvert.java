package com.lanking.uxb.zycon.operation.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.common.EmbeddedApp;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.zycon.operation.value.VZycEmbeddedApp;

@Component
public class ZycEmbeddedAppConvert extends Converter<VZycEmbeddedApp, EmbeddedApp, Long> {

	@Override
	protected Long getId(EmbeddedApp s) {
		return s.getId();
	}

	@Override
	protected VZycEmbeddedApp convert(EmbeddedApp s) {
		VZycEmbeddedApp v = new VZycEmbeddedApp();
		v.setId(s.getId());
		v.setImageId(s.getImageId());
		v.setImageUrl(FileUtil.getUrl(s.getImageId()));
		v.setName(s.getName());
		v.setSequence(s.getSequence());
		v.setUrl(s.getUrl());
		v.setApp(s.getApp());
		v.setLocation(s.getLocation());
		return v;
	}

}
