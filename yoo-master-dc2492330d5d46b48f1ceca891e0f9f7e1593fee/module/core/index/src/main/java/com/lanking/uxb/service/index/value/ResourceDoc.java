package com.lanking.uxb.service.index.value;

import com.lanking.uxb.service.index.api.IndexMapping;
import com.lanking.uxb.service.index.api.MappingType;

/**
 * 资源索引对象.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年3月27日
 */
public abstract class ResourceDoc extends AbstraceIndexDoc {
	private static final long serialVersionUID = -3931207067479748108L;

	// ~供应商.
	@IndexMapping(type = MappingType.LONG)
	private Long vendorId;

	// 2016-10-14 删除未用字段
	// 资源类型.
	// @IndexMapping(type = MappingType.INTEGER)
	// private Integer resourceType;

	// ~资源对象ID.
	@IndexMapping(type = MappingType.LONG)
	private Long resourceId;

	// ~阶段.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer phaseCode;

	// ~科目.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer subjectCode;

	// ~教材类型.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer textbookCategoryCode;

	// ~教材版本.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer textbookCode;

	// ~创建人.
	@IndexMapping(type = MappingType.LONG)
	private Long createId;

	// ~创建时间戳.
	@IndexMapping(type = MappingType.LONG)
	private Long createAt;

	// 2016-10-14 删除未用字段
	// 更新人.
	// @IndexMapping(type = MappingType.LONG)
	// private Long updateId;

	// 2016-10-14 删除未用字段
	// 更新时间戳.
	// @IndexMapping(type = MappingType.LONG)
	// private Long updateAt;

	// ~习题：（题干 + 选项 + 子题题干 + 子题选项）、流媒体：名称.
	@IndexMapping(type = MappingType.TEXT, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String contents;

	// ~知识点字串拼接.
	@IndexMapping(type = MappingType.TEXT, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String metaKnowpoints;

	// ~新知识点字串拼接
	@IndexMapping(type = MappingType.TEXT, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String knowledgePoints;

	// ~同步知识点字串拼接
	@IndexMapping(type = MappingType.TEXT, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String knowledgeSyncs;

	// ~复习知识点字串拼接
	@IndexMapping(type = MappingType.TEXT, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
	private String knowledgeReviews;

	// ~检验状态.
	@IndexMapping(type = MappingType.INTEGER)
	private Integer checkStatus;

	// ~校验人（一校）
	@IndexMapping(type = MappingType.LONG)
	private Long verifyId;

	// ~校验时间戳（一校）
	@IndexMapping(type = MappingType.LONG)
	private Long verifyAt;

	// ~校验人（二校）
	@IndexMapping(type = MappingType.LONG)
	private Long verify2Id;

	// ~校验时间戳（二校）
	@IndexMapping(type = MappingType.LONG)
	private Long verify2At;

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	// public Integer getResourceType() {
	// return resourceType;
	// }
	//
	// public void setResourceType(Integer resourceType) {
	// this.resourceType = resourceType;
	// }

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

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

	public Integer getTextbookCategoryCode() {
		return textbookCategoryCode;
	}

	public void setTextbookCategoryCode(Integer textbookCategoryCode) {
		this.textbookCategoryCode = textbookCategoryCode;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	// public Long getUpdateId() {
	// return updateId;
	// }
	//
	// public void setUpdateId(Long updateId) {
	// this.updateId = updateId;
	// }
	//
	// public Long getUpdateAt() {
	// return updateAt;
	// }
	//
	// public void setUpdateAt(Long updateAt) {
	// this.updateAt = updateAt;
	// }

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getMetaKnowpoints() {
		return metaKnowpoints;
	}

	public void setMetaKnowpoints(String metaKnowpoints) {
		this.metaKnowpoints = metaKnowpoints;
	}

	public String getKnowledgePoints() {
		return knowledgePoints;
	}

	public void setKnowledgePoints(String knowledgePoints) {
		this.knowledgePoints = knowledgePoints;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Long getVerifyId() {
		return verifyId;
	}

	public void setVerifyId(Long verifyId) {
		this.verifyId = verifyId;
	}

	public Long getVerifyAt() {
		return verifyAt;
	}

	public void setVerifyAt(Long verifyAt) {
		this.verifyAt = verifyAt;
	}

	public Long getVerify2Id() {
		return verify2Id;
	}

	public void setVerify2Id(Long verify2Id) {
		this.verify2Id = verify2Id;
	}

	public Long getVerify2At() {
		return verify2At;
	}

	public void setVerify2At(Long verify2At) {
		this.verify2At = verify2At;
	}

	public String getKnowledgeSyncs() {
		return knowledgeSyncs;
	}

	public void setKnowledgeSyncs(String knowledgeSyncs) {
		this.knowledgeSyncs = knowledgeSyncs;
	}

	public String getKnowledgeReviews() {
		return knowledgeReviews;
	}

	public void setKnowledgeReviews(String knowledgeReviews) {
		this.knowledgeReviews = knowledgeReviews;
	}

}
