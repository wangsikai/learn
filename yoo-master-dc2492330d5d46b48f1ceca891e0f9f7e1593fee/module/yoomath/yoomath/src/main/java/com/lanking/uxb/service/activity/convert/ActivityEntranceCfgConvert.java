package com.lanking.uxb.service.activity.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.activity.ActivityEntranceCfg;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.activity.value.VActivityEntranceCfg;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.session.api.impl.Security;

@Component
public class ActivityEntranceCfgConvert extends Converter<VActivityEntranceCfg, ActivityEntranceCfg, Long> {

	@Override
	protected Long getId(ActivityEntranceCfg s) {
		return s.getId();
	}

	@Override
	protected VActivityEntranceCfg convert(ActivityEntranceCfg s) {
		VActivityEntranceCfg v = new VActivityEntranceCfg();
		v.setIcon(FileUtil.getUrl(s.getIcon()));
		if (StringUtils.isNotBlank(s.getUri())) {
			v.setUri(s.getUri().replaceAll("\\{S_T\\}", Security.getToken()));
		} else {
			v.setUri(StringUtils.EMPTY);
		}
		return v;
	}

}
