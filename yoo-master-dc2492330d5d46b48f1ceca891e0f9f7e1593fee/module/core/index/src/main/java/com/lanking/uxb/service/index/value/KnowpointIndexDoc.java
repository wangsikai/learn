package com.lanking.uxb.service.index.value;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.uxb.service.index.api.IndexMapping;
import com.lanking.uxb.service.index.api.IndexType;
import com.lanking.uxb.service.index.api.MappingType;

/**
 * 知识点索引DOC
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年3月30日
 */
@Component
@IndexType(type = com.lanking.cloud.domain.type.IndexType.KNOWPOINT)
public class KnowpointIndexDoc extends AbstraceIndexDoc {

	private static final long serialVersionUID = 8805935258768760347L;

	@IndexMapping(type = MappingType.INTEGER)
	private int knowpointCode;

	@IndexMapping(type = MappingType.TEXT, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String name;

	@IndexMapping(type = MappingType.INTEGER)
	private int subjectCode;

	@IndexMapping(ignore = true, type = MappingType.INTEGER)
	private int phaseCode;

	@IndexMapping(type = MappingType.INTEGER)
	private List<Integer> metaCodes = Lists.newArrayList();

	// 1:表示元知识点,0:表示知识点
	@IndexMapping(ignore = true, type = MappingType.INTEGER)
	private int isMeta;

	public int getKnowpointCode() {
		return knowpointCode;
	}

	public void setKnowpointCode(int knowpointCode) {
		this.knowpointCode = knowpointCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(int subjectCode) {
		this.subjectCode = subjectCode;
	}

	public int getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(int phaseCode) {
		this.phaseCode = phaseCode;
	}

	public List<Integer> getMetaCodes() {
		return metaCodes;
	}

	public void setMetaCodes(List<Integer> metaCodes) {
		this.metaCodes = metaCodes;
	}

	public int getIsMeta() {
		return isMeta;
	}

	public void setIsMeta(int isMeta) {
		this.isMeta = isMeta;
	}

}
