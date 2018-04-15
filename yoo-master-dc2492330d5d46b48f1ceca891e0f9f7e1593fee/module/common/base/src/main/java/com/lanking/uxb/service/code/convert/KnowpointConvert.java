package com.lanking.uxb.service.code.convert;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.Knowpoint;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.api.KnowpointService;
import com.lanking.uxb.service.code.value.VKnowpoint;

@Component
public class KnowpointConvert extends Converter<VKnowpoint, Knowpoint, Integer> {

	@Autowired
	private KnowpointService knowpointService;

	@Override
	protected Integer getId(Knowpoint s) {
		return s.getCode();
	}

	@Override
	protected VKnowpoint convert(Knowpoint s) {
		VKnowpoint v = new VKnowpoint();
		v.setCode(s.getCode());
		v.setLevel(s.getLevel());
		v.setName(s.getName());
		v.setPcode(s.getPcode());
		v.setSubjectCode(s.getSubjectCode());
		return v;
	}

	@Override
	protected Knowpoint internalGet(Integer id) {
		return knowpointService.get(id);
	}

	@Override
	protected Map<Integer, Knowpoint> internalMGet(Collection<Integer> ids) {
		return knowpointService.mget(ids);
	}

}
