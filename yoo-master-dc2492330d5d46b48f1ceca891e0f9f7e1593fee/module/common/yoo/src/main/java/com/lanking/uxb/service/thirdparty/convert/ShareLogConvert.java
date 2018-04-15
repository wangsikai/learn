package com.lanking.uxb.service.thirdparty.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.thirdparty.ShareLog;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.thirdparty.value.VShareLog;

@Component
public class ShareLogConvert extends Converter<VShareLog, ShareLog, Long> {

	@Override
	protected Long getId(ShareLog s) {
		return s.getId();
	}

	@Override
	protected VShareLog convert(ShareLog s) {
		VShareLog v = new VShareLog();
		v.setBiz(s.getBiz());
		v.setBizId(s.getBizId());
		v.setP0(validBlank(s.getP0()));
		v.setContent(validBlank(s.getContent()));
		v.setCreateAt(s.getCreateAt());
		v.setId(s.getId());
		v.setType(s.getType());
		return v;
	}
}
