package com.lanking.uxb.service.code.convert;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.Phase;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.value.VPhase;

@Component
public class PhaseConvert extends Converter<VPhase, Phase, Integer> {

	@Autowired
	private PhaseService phaseService;

	@Override
	protected Integer getId(Phase s) {
		return s.getCode();
	}

	@Override
	protected VPhase convert(Phase s) {
		VPhase v = new VPhase();
		v.setCode(s.getCode());
		v.setName(s.getName());
		v.setSequence(s.getSequence());
		v.setAcronym(s.getAcronym());
		return v;
	}

	@Override
	protected Phase internalGet(Integer id) {
		return phaseService.get(id);
	}

	@Override
	protected Map<Integer, Phase> internalMGet(Collection<Integer> ids) {
		return phaseService.mget(ids);
	}

}
