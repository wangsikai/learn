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
public class VResconKnowledgeSync implements Serializable {
	private static final long serialVersionUID = 8437415247900062866L;

	private Long code;
	private Long pcode;
	private String name;
	private Integer phaseCode;
	private Integer subjectCode;
	private StudyDifficulty studyDifficulty;
	private Integer sequence;
	private Status status;
	private Integer level;
}
