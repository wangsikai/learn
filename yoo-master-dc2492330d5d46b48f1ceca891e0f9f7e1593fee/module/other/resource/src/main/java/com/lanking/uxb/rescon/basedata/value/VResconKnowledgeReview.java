package com.lanking.uxb.rescon.basedata.value;

import java.io.Serializable;

import com.lanking.cloud.domain.common.baseData.StudyDifficulty;
import com.lanking.cloud.sdk.bean.Status;

import lombok.Getter;
import lombok.Setter;

/**
 * 复习知识点.
 * 
 * @author wlche
 *
 */
@Getter
@Setter
public class VResconKnowledgeReview implements Serializable {
	private static final long serialVersionUID = 4617902886488223550L;

	private Long code;
	private String name;
	private Long pcode;
	private Integer phaseCode;
	private Integer subjectCode;
	private StudyDifficulty studyDifficulty;
	private Integer sequence;
	private Status status;
	private Integer level;
}
