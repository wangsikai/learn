package com.lanking.uxb.service.holiday.value;

import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.common.value.VQuestionBase;

/**
 * 假日作业VO
 * 
 * @since yoomath V1.9
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月25日
 */
public class VHolidayQuestion extends VQuestionBase {

	private static final long serialVersionUID = -4974194746004129033L;
	/**
	 * 学生假期作业专项题目
	 */
	private VHolidayStuHomeworkItemQuestion holidayStuHomeworkItemQuestion;
	/**
	 * 学生假期作业专项题目答案
	 */
	private List<VHolidayStuHomeworkItemAnswer> holidayStuHomeworkItemAnswers = Lists.newArrayList();;

	/**
	 * 假期作业专项题目
	 */
	private VHolidayHomeworkItemQuestion holidayHomeworkItemQuestion;

	// 是否答了这道题,yoomath mobile V1.1.0
	private Boolean done;

	// 判断当前错题在错题本里 since 2.4.0
	private boolean inStuFallQuestion = false;
	// @since 3.9.0 学生此题做过的次数
	private Long doCount;
	// @since 3.9.0 学生此题历史正确率
	private Double questionRightRate;
	// @since 3.9.0 做错错误人数
	private Long wrongPeopleCount;

	public VHolidayStuHomeworkItemQuestion getHolidayStuHomeworkItemQuestion() {
		return holidayStuHomeworkItemQuestion;
	}

	public void setHolidayStuHomeworkItemQuestion(VHolidayStuHomeworkItemQuestion holidayStuHomeworkItemQuestion) {
		this.holidayStuHomeworkItemQuestion = holidayStuHomeworkItemQuestion;
	}

	public List<VHolidayStuHomeworkItemAnswer> getHolidayStuHomeworkItemAnswers() {
		return holidayStuHomeworkItemAnswers;
	}

	public void setHolidayStuHomeworkItemAnswers(List<VHolidayStuHomeworkItemAnswer> holidayStuHomeworkItemAnswers) {
		this.holidayStuHomeworkItemAnswers = holidayStuHomeworkItemAnswers;
	}

	public VHolidayHomeworkItemQuestion getHolidayHomeworkItemQuestion() {
		return holidayHomeworkItemQuestion;
	}

	public void setHolidayHomeworkItemQuestion(VHolidayHomeworkItemQuestion holidayHomeworkItemQuestion) {
		this.holidayHomeworkItemQuestion = holidayHomeworkItemQuestion;
	}

	public Boolean isDone() {
		if (done == null) {
			if (holidayStuHomeworkItemAnswers.size() > 0) {
				boolean $done = false;
				for (VHolidayStuHomeworkItemAnswer sa : holidayStuHomeworkItemAnswers) {
					if (StringUtils.isNotBlank(sa.getContent())) {
						$done = true;
						break;
					}
				}
				setDone($done);
			} else {
				setDone(false);
			}
		}
		return done;
	}

	public void setDone(Boolean done) {
		this.done = done;
	}

	public boolean isInStuFallQuestion() {
		return inStuFallQuestion;
	}

	public void setInStuFallQuestion(boolean inStuFallQuestion) {
		this.inStuFallQuestion = inStuFallQuestion;
	}

	public Long getDoCount() {
		return doCount;
	}

	public void setDoCount(Long doCount) {
		this.doCount = doCount;
	}

	public Double getQuestionRightRate() {
		return questionRightRate;
	}

	public void setQuestionRightRate(Double questionRightRate) {
		this.questionRightRate = questionRightRate;
	}

	public Long getWrongPeopleCount() {
		return wrongPeopleCount;
	}

	public void setWrongPeopleCount(Long wrongPeopleCount) {
		this.wrongPeopleCount = wrongPeopleCount;
	}
}
