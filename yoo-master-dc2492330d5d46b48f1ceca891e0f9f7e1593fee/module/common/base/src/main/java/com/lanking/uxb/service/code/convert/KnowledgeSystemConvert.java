package com.lanking.uxb.service.code.convert;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.value.VKnowledgeSystem;

/**
 * KnowledgeSystem -> VKnowledgeSystem
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
@Component
public class KnowledgeSystemConvert extends Converter<VKnowledgeSystem, KnowledgeSystem, Long> {
	@Override
	protected Long getId(KnowledgeSystem knowledgeSystem) {
		return knowledgeSystem.getCode();
	}

	@Override
	protected VKnowledgeSystem convert(KnowledgeSystem knowledgeSystem) {
		VKnowledgeSystem v = new VKnowledgeSystem();
		v.setCode(knowledgeSystem.getCode());
		v.setLevel(knowledgeSystem.getLevel());
		v.setPcode(knowledgeSystem.getPcode());
		v.setPhaseCode(knowledgeSystem.getPhaseCode());
		v.setSequence(knowledgeSystem.getSequence());
		v.setSubjectCode(knowledgeSystem.getSubjectCode());
		v.setName(knowledgeSystem.getName());

		return v;
	}

	/**
	 * 组装树型
	 *
	 * @param from
	 *            {@link List}
	 * @return {@link List}
	 */
	public List<VKnowledgeSystem> assembleTree(List<VKnowledgeSystem> from) {
		List<VKnowledgeSystem> retList = Lists.newArrayList();
		for (VKnowledgeSystem v : from) {
			assemble(v, retList);
		}
		return retList;
	}

	/**
	 * 对树型结构进行组装
	 *
	 * @param v
	 *            {@link VKnowledgeSystem}
	 * @param retList
	 *            {@link List}
	 */
	private void assemble(VKnowledgeSystem v, List<VKnowledgeSystem> retList) {
		if (v.getLevel() == 1) {
			retList.add(v);
		} else {
			for (VKnowledgeSystem s : retList) {
				if (s.getCode() == v.getPcode()) {
					s.getChildren().add(v);
				} else {
					this.assemble(v, s.getChildren());
				}
			}
		}
	}

	public List<VKnowledgeSystem> assembleTree2(List<VKnowledgeSystem> from) {
		List<VKnowledgeSystem> retList = Lists.newArrayList();
		for (VKnowledgeSystem v : from) {
			assemble(v, retList);
		}
		return retList;
	}

}
