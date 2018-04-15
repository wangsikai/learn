package com.lanking.cloud.domain.yoomath.examPaper;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 组卷配置
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "custom_exampaper_cfg")
public class CustomExampaperCfg implements Serializable {

	private static final long serialVersionUID = 5053144878762345675L;

	/**
	 * 组卷ID
	 */
	@Id
	@Column(name = "custom_exampaper_id")
	private Long customExampaperId;

	/**
	 * 选择题每题分值
	 */
	@Column(name = "singlechoice_score", precision = 3)
	private Integer singleChoiceScore;

	/**
	 * 填空题每空分值
	 */
	@Column(name = "fillblank_score", precision = 3)
	private Integer fillBlankScore;

	public Long getCustomExampaperId() {
		return customExampaperId;
	}

	public void setCustomExampaperId(Long customExampaperId) {
		this.customExampaperId = customExampaperId;
	}

	public Integer getSingleChoiceScore() {
		return singleChoiceScore;
	}

	public void setSingleChoiceScore(Integer singleChoiceScore) {
		this.singleChoiceScore = singleChoiceScore;
	}

	public Integer getFillBlankScore() {
		return fillBlankScore;
	}

	public void setFillBlankScore(Integer fillBlankScore) {
		this.fillBlankScore = fillBlankScore;
	}

}
