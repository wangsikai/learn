package com.lanking.uxb.service.examPaper.convert;

import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.examPaper.value.VKnowledgeTreeNode;

import org.springframework.stereotype.Component;

/**
 * 新知识点树节点转换
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
@Component
public class KnowledgePointTreeNodeConvert extends Converter<VKnowledgeTreeNode, KnowledgePoint, Long> {

	@Override
	protected Long getId(KnowledgePoint knowledgePoint) {
		return knowledgePoint.getCode();
	}

	@Override
	protected VKnowledgeTreeNode convert(KnowledgePoint knowledgePoint) {
		VKnowledgeTreeNode v = new VKnowledgeTreeNode();
		v.setCode(knowledgePoint.getCode());
		v.setName(knowledgePoint.getName());
		v.setPcode(knowledgePoint.getPcode());
		v.setSequence(knowledgePoint.getSequence());
		v.setSystem(false);
		v.setLevel(4);
		return v;
	}
}
