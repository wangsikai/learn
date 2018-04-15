package com.lanking.uxb.service.sys.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.common.Banner;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.sys.value.VBanner;

@Component
public class BannerConvert extends Converter<VBanner, Banner, Long> {

	@Override
	protected Long getId(Banner s) {
		return s.getId();
	}

	@Override
	protected VBanner convert(Banner s) {
		VBanner v = new VBanner();
		v.setId(s.getId());
		v.setImageUrl(FileUtil.getUrl(s.getImageId()));
		if (StringUtils.isNotBlank(s.getUrl())) {
			v.setUrl(s.getUrl().replaceAll("\\{S_T\\}", Security.getToken()));
		} else {
			v.setUrl(StringUtils.EMPTY);
		}
		v.setSequence(s.getSequence());
		v.setBannerLocation(s.getLocation());
		return v;
	}
}
