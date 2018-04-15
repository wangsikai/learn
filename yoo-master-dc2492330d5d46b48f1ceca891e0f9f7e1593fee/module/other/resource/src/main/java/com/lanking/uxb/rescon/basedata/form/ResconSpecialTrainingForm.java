package com.lanking.uxb.rescon.basedata.form;

import java.util.List;

import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingOperateType;
import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingStatus;

/**
 * 针对性训练提交数据
 * 
 * @since 2.0.1
 * @author wangsenhao
 *
 */
public class ResconSpecialTrainingForm {
	/**
	 * 针对性训练ID,增加时为空
	 */
	private Long id;
	/**
	 * 针对性训练名称
	 */
	private String name;
	/**
	 * 知识点集合
	 */
	private List<Long> knowPoints;
	/**
	 * 考点集合
	 */
	private List<Long> examIds;

	private Long userId;

	private SpecialTrainingStatus status;

	private SpecialTrainingOperateType operateType;

	private List<Long> questionIds;
	/**
	 * 关联的第二层知识点code
	 */
	private Long knowpointCode;

	private Long vendorId;
	/**
	 * 是否是编辑又一次拉取题目
	 */
	private boolean editPullQuestion = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Long> getKnowPoints() {
		return knowPoints;
	}

	public void setKnowPoints(List<Long> knowPoints) {
		this.knowPoints = knowPoints;
	}

	public List<Long> getExamIds() {
		return examIds;
	}

	public void setExamIds(List<Long> examIds) {
		this.examIds = examIds;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public SpecialTrainingStatus getStatus() {
		return status;
	}

	public void setStatus(SpecialTrainingStatus status) {
		this.status = status;
	}

	public SpecialTrainingOperateType getOperateType() {
		return operateType;
	}

	public void setOperateType(SpecialTrainingOperateType operateType) {
		this.operateType = operateType;
	}

	public List<Long> getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(List<Long> questionIds) {
		this.questionIds = questionIds;
	}

	public Long getKnowpointCode() {
		return knowpointCode;
	}

	public void setKnowpointCode(Long knowpointCode) {
		this.knowpointCode = knowpointCode;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public boolean isEditPullQuestion() {
		return editPullQuestion;
	}

	public void setEditPullQuestion(boolean editPullQuestion) {
		this.editPullQuestion = editPullQuestion;
	}

}
