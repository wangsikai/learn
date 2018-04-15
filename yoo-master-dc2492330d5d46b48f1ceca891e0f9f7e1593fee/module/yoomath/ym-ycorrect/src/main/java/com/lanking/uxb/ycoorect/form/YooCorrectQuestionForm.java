package com.lanking.uxb.ycoorect.form;

import java.io.Serializable;
import java.util.List;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;

import lombok.Getter;
import lombok.Setter;

/**
 * 小悠快批教师批改提交参数.
 * 
 * @author wanlong.che
 *
 */
@Getter
@Setter
public class YooCorrectQuestionForm implements Serializable {
	private static final long serialVersionUID = -4063894096160794706L;

	/**
	 * 批改的学生作业习题ID.
	 */
	private Long studentHomeworkQuestionId;

	/**
	 * 题目的正确率(只解答题传).
	 */
	private Integer rightRate;

	/**
	 * 题目的批改结果(解答题和填空题不传).
	 */
	private HomeworkAnswerResult result;

	/**
	 * 答案批改结果.
	 */
	private List<YooCorrectAnswerForm> answerResults;

	/**
	 * 批注集合.
	 */
	private List<String> notations;

	/**
	 * 批改后合成图片id集合
	 */
	private List<Long> notationImageIds;

	/**
	 * 批注前的原图id集合.
	 */
	private List<Long> answerImgIds;

	/**
	 * 批改花费的前端时间（秒）.
	 */
	private Integer costTime;
}
