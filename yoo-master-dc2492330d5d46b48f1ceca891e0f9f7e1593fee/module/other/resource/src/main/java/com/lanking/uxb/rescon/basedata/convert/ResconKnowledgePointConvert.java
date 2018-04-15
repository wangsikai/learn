package com.lanking.uxb.rescon.basedata.convert;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointService;
import com.lanking.uxb.rescon.basedata.value.VKnowledgePoint;

/**
 * 知识点convert
 * 
 * @since 2.0.1
 * @author wangsenhao
 *
 */
@Component
public class ResconKnowledgePointConvert extends Converter<VKnowledgePoint, KnowledgePoint, Long> {

	@Autowired
	private ResconKnowledgePointService resconKnowledgePointService;

	@Override
	protected Long getId(KnowledgePoint s) {
		return s.getCode();
	}

	@Override
	protected VKnowledgePoint convert(KnowledgePoint s) {
		VKnowledgePoint v = new VKnowledgePoint();
		v.setId(s.getCode());
		v.setPcode(s.getPcode());
		v.setDifficulty(s.getDifficulty());
		v.setName(s.getName());
		v.setFocalDifficult(s.getFocalDifficult());
		v.setStudyDifficulty(s.getStudyDifficulty());
		return v;
	}

	@Override
	protected KnowledgePoint internalGet(Long id) {
		return resconKnowledgePointService.get(id);
	}

	@Override
	protected Map<Long, KnowledgePoint> internalMGet(Collection<Long> ids) {
		return resconKnowledgePointService.mget(ids);
	}
}
