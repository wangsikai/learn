package com.lanking.uxb.service.index.value;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.uxb.service.index.api.IndexMapping;
import com.lanking.uxb.service.index.api.IndexType;
import com.lanking.uxb.service.index.api.MappingType;

/**
 * 相似题组索引.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月18日
 */
@Component
@IndexType(type = com.lanking.cloud.domain.type.IndexType.QUESTION_SIMILAR)
public class QuestionSimilarIndexDoc extends AbstraceIndexDoc {
	private static final long serialVersionUID = -7241202406000572992L;

	// 阶段.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer phaseCode;

	// 科目.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer subjectCode;

	// 索引创建时间戳.
	@IndexMapping(type = MappingType.LONG)
	private Long createAt;

	// 题组题目集合
	@IndexMapping(type = MappingType.LONG)
	private List<Long> questionIds = Lists.newArrayList();

	// 判断题组唯一性使用
	@IndexMapping(type = MappingType.TEXT)
	private String md5;

	// 取相似题时的基准题目
	@IndexMapping(type = MappingType.LONG)
	private Long baseQuestionId;

	// VENDOR_ID
	@IndexMapping(type = MappingType.LONG)
	private Long vendorId;

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	public List<Long> getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(List<Long> questionIds) {
		this.questionIds = questionIds;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public Long getBaseQuestionId() {
		return baseQuestionId;
	}

	public void setBaseQuestionId(Long baseQuestionId) {
		this.baseQuestionId = baseQuestionId;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
}
