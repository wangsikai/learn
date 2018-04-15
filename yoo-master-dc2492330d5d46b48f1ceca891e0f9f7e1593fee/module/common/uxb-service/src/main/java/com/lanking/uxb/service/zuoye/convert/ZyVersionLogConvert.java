package com.lanking.uxb.service.zuoye.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.version.YoomathVersionLog;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.zuoye.value.VVersionLog;

/**
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version V1.0.0,2015年1月22日 上午10:52:26
 *
 */
@Component
public class ZyVersionLogConvert extends Converter<VVersionLog, YoomathVersionLog, Long> {

	@Override
	protected Long getId(YoomathVersionLog s) {
		return s.getId();
	}

	@Override
	protected VVersionLog convert(YoomathVersionLog s) {
		VVersionLog version = new VVersionLog();
		version.setDesc(s.getDescription());
		version.setId(s.getId());
		version.setPublishDate(s.getPublishAt());
		version.setVersion(s.getVersion());
		return version;
	}

}
