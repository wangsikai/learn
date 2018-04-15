package com.lanking.uxb.rescon.basedata.convert;

import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.basedata.api.ResconPointType;
import com.lanking.uxb.rescon.basedata.value.VResconPoint;

import org.springframework.stereotype.Component;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Component
public class ResconMetaKnowpointConvert2 extends Converter<VResconPoint, MetaKnowpoint, Integer> {
	@Override
	protected Integer getId(MetaKnowpoint metaKnowpoint) {
		return metaKnowpoint.getCode();
	}

	@Override
	protected VResconPoint convert(MetaKnowpoint metaKnowpoint) {
		VResconPoint v = new VResconPoint();
		v.setId(metaKnowpoint.getCode());
		v.setPhaseCode(metaKnowpoint.getPhaseCode());
		v.setSubjectCode(metaKnowpoint.getSubjectCode());
		v.setType(ResconPointType.METAKNOWPOINT);
		v.setName(metaKnowpoint.getName());
		v.setParent(metaKnowpoint.getCode() / 100);
		v.setStatus(metaKnowpoint.getStatus());
		v.setSequence(metaKnowpoint.getSequence());
		return v;
	}
}
