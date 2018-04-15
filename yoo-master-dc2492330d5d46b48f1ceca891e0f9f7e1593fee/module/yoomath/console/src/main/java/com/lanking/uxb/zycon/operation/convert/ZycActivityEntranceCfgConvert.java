package com.lanking.uxb.zycon.operation.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.activity.ActivityEntranceCfg;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.zycon.operation.value.VZycActivityEntranceCfg;

@Component
public class ZycActivityEntranceCfgConvert extends Converter<VZycActivityEntranceCfg, ActivityEntranceCfg, Long> {

	@Override
	protected Long getId(ActivityEntranceCfg s) {
		return s.getId();
	}

	@Override
	protected VZycActivityEntranceCfg convert(ActivityEntranceCfg s) {
		VZycActivityEntranceCfg v = new VZycActivityEntranceCfg();
		v.setApp(s.getApp());
		v.setIcon(s.getIcon() == null ? 0 : s.getIcon());
		v.setIconUrl(FileUtil.getUrl(s.getIcon()));
		v.setStatus(s.getStatus());
		v.setUri(validBlank(s.getUri()));
		return v;
	}

}
