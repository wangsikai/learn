package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 考场
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 */
@Setter
@Getter
public final class ImperialExaminationRoom implements Serializable {

	private static final long serialVersionUID = -2856530673901077879L;

	/**
	 * 考场
	 */
	private int room;

	/**
	 * 支持的版本
	 */
	private List<Integer> textbookCategoryCodes;
}
