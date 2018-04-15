package com.lanking.uxb.rescon.basedata.convert;

import com.lanking.cloud.domain.common.baseData.Knowpoint;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.basedata.api.ResconPointType;
import com.lanking.uxb.rescon.basedata.value.VResconPoint;

import org.springframework.stereotype.Component;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Component
public class ResconKnowpointConvert extends Converter<VResconPoint, Knowpoint, Integer> {
	@Override
	protected Integer getId(Knowpoint knowpoint) {
		return knowpoint.getCode();
	}

	@Override
	protected VResconPoint convert(Knowpoint knowpoint) {
		VResconPoint v = new VResconPoint();
		v.setName(knowpoint.getName());
		v.setId(knowpoint.getCode());
		v.setSubjectCode(knowpoint.getSubjectCode());
		v.setPhaseCode(knowpoint.getPhaseCode());
		v.setParent(knowpoint.getPcode());
		v.setLevel(knowpoint.getLevel());
		v.setType(ResconPointType.KNOWPOINT);
		v.setStatus(knowpoint.getStatus());
		v.setSequence(knowpoint.getSequence());
		return v;
	}
}
