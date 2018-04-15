package com.lanking.uxb.ycoorect.form;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;

import lombok.Getter;
import lombok.Setter;

/**
 * 答案批改结果.
 * 
 * @author wanlong.che
 *
 */
@Getter
@Setter
public class YooCorrectAnswerForm {

	/**
	 * 学生习题答案ID.
	 */
	private Long id;

	/**
	 * 批改结果.
	 */
	private HomeworkAnswerResult result;
}
