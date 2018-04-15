package com.lanking.uxb.rescon.exam.convert;

/**
 * 试卷转化 选项
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月23日 上午10:15:05
 */
public class ResconExamOption {

	private boolean initQuestion = false;
	private boolean initUpdateors = false;
	private boolean initRollBacks = false;
	private boolean initQuestionStatusCount = false;

	public ResconExamOption() {
		super();
	}

	public ResconExamOption(boolean initQuestion, boolean initUpdateors, boolean initRollBacks) {
		super();
		this.initQuestion = initQuestion;
		this.initUpdateors = initUpdateors;
		this.initRollBacks = initRollBacks;
	}

	public ResconExamOption(boolean initQuestion, boolean initUpdateors, boolean initRollBacks,
			boolean initQuestionStatusCount) {
		this.initQuestion = initQuestion;
		this.initUpdateors = initUpdateors;
		this.initRollBacks = initRollBacks;
		this.initQuestionStatusCount = initQuestionStatusCount;
	}

	public boolean isInitQuestionStatusCount() {
		return initQuestionStatusCount;
	}

	public void setInitQuestionStatusCount(boolean initQuestionStatusCount) {
		this.initQuestionStatusCount = initQuestionStatusCount;
	}

	public boolean isInitQuestion() {
		return initQuestion;
	}

	public void setInitQuestion(boolean initQuestion) {
		this.initQuestion = initQuestion;
	}

	public boolean isInitUpdateors() {
		return initUpdateors;
	}

	public void setInitUpdateors(boolean initUpdateors) {
		this.initUpdateors = initUpdateors;
	}

	public boolean isInitRollBacks() {
		return initRollBacks;
	}

	public void setInitRollBacks(boolean initRollBacks) {
		this.initRollBacks = initRollBacks;
	}

}
