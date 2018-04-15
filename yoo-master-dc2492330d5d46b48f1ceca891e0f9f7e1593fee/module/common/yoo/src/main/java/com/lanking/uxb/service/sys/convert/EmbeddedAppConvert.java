package com.lanking.uxb.service.sys.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.common.EmbeddedApp;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.sys.value.VEmbeddedApp;

@Component
public class EmbeddedAppConvert extends Converter<VEmbeddedApp, EmbeddedApp, Long> {

	@Override
	protected Long getId(EmbeddedApp s) {
		return s.getId();
	}

	@Override
	protected VEmbeddedApp convert(EmbeddedApp s) {
		VEmbeddedApp v = new VEmbeddedApp();
		v.setId(s.getId());
		v.setName(validBlank(s.getName()));
		v.setImageUrl(FileUtil.getUrl(s.getImageId()));
		if (StringUtils.isNotBlank(s.getUrl())) {
			v.setUrl(s.getUrl().replaceAll("\\{S_T\\}", Security.getToken()));
		} else {
			v.setUrl(StringUtils.EMPTY);
		}
		v.setSequence(s.getSequence());
		return v;
	}
}
