package com.lanking.uxb.service.user.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.user.value.VUserState;

@Component
public class UserStateConvert extends Converter<VUserState, Counter, Long> {

	@Override
	protected Long getId(Counter s) {
		return s.getBizId();
	}

	@Override
	protected VUserState convert(Counter s) {
		VUserState v = new VUserState();
		if (s != null) {
			v.setBuyResCount(s.getCount8());
			v.setFanCount(s.getCount6());
			v.setFollowCount(s.getCount5());
			v.setGroupCount(s.getCount3() + s.getCount4());
			v.setPostCount(s.getCount1());
			v.setSelfResCount(s.getCount7());
			v.setCourseCount(s.getCount9());
			v.setContributeCount(s.getCount15());
			v.setCircleCount(s.getCount18());
		}
		return v;
	}
}
