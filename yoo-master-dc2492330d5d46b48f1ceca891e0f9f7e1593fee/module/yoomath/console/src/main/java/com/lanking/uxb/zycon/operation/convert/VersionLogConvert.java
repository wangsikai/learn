package com.lanking.uxb.zycon.operation.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.version.YoomathVersionLog;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.zycon.operation.value.CVersionLog;

/**
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version V1.0.0,2015年1月22日 上午10:52:26
 *
 */
@Component
public class VersionLogConvert extends Converter<CVersionLog, YoomathVersionLog, Long> {

	@Override
	protected Long getId(YoomathVersionLog s) {
		return s.getId();
	}

	@Override
	protected CVersionLog convert(YoomathVersionLog s) {
		CVersionLog version = new CVersionLog();
		version.setDesc(s.getDescription());
		version.setId(s.getId());
		version.setPublishDate(s.getPublishAt());
		version.setStatus(s.getStatus());
		version.setVersion(s.getVersion());
		return version;
	}

}
