package com.lanking.uxb.rescon.question.form;

/**
 * 基本选择数据表单.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年3月6日
 */
public class SelectDatasForm {

	private Boolean phaseEnable = false; // 阶段数据
	private Boolean subjectEnable = false; // 学科数据
	private Boolean phaseSubjectEnable = false; // 学科阶段合体
	private Boolean questionTypeEnable = false; // 学科题型
	private Boolean categoryEnable = false; // 教材数据
	private Boolean typeEnable = false; // 基本题目类型数据
	private Boolean statusEnable = false; // 检测状态数据
	private Boolean vendorEnable = false; // 录入员数据
	private Boolean resourceCategoryEnable = false; // 资源类型数据

	private Boolean questionCategoryEnable = false; // 习题类型
	private Boolean questionTagEnable = false; // 习题标签

	public Boolean getPhaseEnable() {
		return phaseEnable;
	}

	public void setPhaseEnable(Boolean phaseEnable) {
		this.phaseEnable = phaseEnable;
	}

	public Boolean getSubjectEnable() {
		return subjectEnable;
	}

	public void setSubjectEnable(Boolean subjectEnable) {
		this.subjectEnable = subjectEnable;
	}

	public Boolean getCategoryEnable() {
		return categoryEnable;
	}

	public void setCategoryEnable(Boolean categoryEnable) {
		this.categoryEnable = categoryEnable;
	}

	public Boolean getTypeEnable() {
		return typeEnable;
	}

	public void setTypeEnable(Boolean typeEnable) {
		this.typeEnable = typeEnable;
	}

	public Boolean getStatusEnable() {
		return statusEnable;
	}

	public void setStatusEnable(Boolean statusEnable) {
		this.statusEnable = statusEnable;
	}

	public Boolean getVendorEnable() {
		return vendorEnable;
	}

	public void setVendorEnable(Boolean vendorEnable) {
		this.vendorEnable = vendorEnable;
	}

	public Boolean getQuestionTypeEnable() {
		return questionTypeEnable;
	}

	public void setQuestionTypeEnable(Boolean questionTypeEnable) {
		this.questionTypeEnable = questionTypeEnable;
	}

	public Boolean getPhaseSubjectEnable() {
		return phaseSubjectEnable;
	}

	public void setPhaseSubjectEnable(Boolean phaseSubjectEnable) {
		this.phaseSubjectEnable = phaseSubjectEnable;
	}

	public Boolean getResourceCategoryEnable() {
		return resourceCategoryEnable;
	}

	public void setResourceCategoryEnable(Boolean resourceCategoryEnable) {
		this.resourceCategoryEnable = resourceCategoryEnable;
	}

	public Boolean getQuestionCategoryEnable() {
		return questionCategoryEnable;
	}

	public void setQuestionCategoryEnable(Boolean questionCategoryEnable) {
		this.questionCategoryEnable = questionCategoryEnable;
	}

	public Boolean getQuestionTagEnable() {
		return questionTagEnable;
	}

	public void setQuestionTagEnable(Boolean questionTagEnable) {
		this.questionTagEnable = questionTagEnable;
	}
}
