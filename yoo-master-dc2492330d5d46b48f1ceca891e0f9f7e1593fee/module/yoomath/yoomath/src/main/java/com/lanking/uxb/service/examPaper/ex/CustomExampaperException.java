package com.lanking.uxb.service.examPaper.ex;

import com.lanking.cloud.ex.AbstractException;

/**
 * 组卷相关Exception 2751 - 2800
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
public class CustomExampaperException extends AbstractException {
	private static final long serialVersionUID = 1755253081391188807L;

	private static final int CEP_ERROR = 2750;

	/**
	 * 对已经开卷过的组卷再开卷
	 */
	public static final int CUSTOM_EXAMPAPER_OPENED = CEP_ERROR + 1;
	/**
	 * 对在草稿中组卷进行开卷操作
	 */
	public static final int CUSTOM_EXAMPAPER_OPEN_DRAFT = CEP_ERROR + 2;
	/**
	 * 智能组卷选择题数量不足
	 */
	public static final int SMART_EXAMPAPER_CHOICENUM_NOTENOUGH = CEP_ERROR + 3;
	/**
	 * 智能组卷填空题数量不足
	 */
	public static final int SMART_EXAMPAPER_FILLBLANKNUM_NOTENOUGH = CEP_ERROR + 4;
	/**
	 * 智能组卷解答题数量不足
	 */
	public static final int SMART_EXAMPAPER_ANSWERNUM_NOTENOUGH = CEP_ERROR + 5;
	/**
	 * 智能组卷知识点数量不足
	 */
	public static final int SMART_EXAMPAPER_KNOWLEDGE_NOTENOUGH = CEP_ERROR + 6;
	/**
	 * 该组卷已删除
	 */
	public static final int CUSTOM_EXAMPAPER_DELETE = CEP_ERROR + 7;

	/**
	 * 该组卷已生成正式组卷
	 */
	public static final int CUSTOM_EXAMPAPER_OPEN = CEP_ERROR + 8;

	public CustomExampaperException() {
		super();
	}

	public CustomExampaperException(int code, Object... args) {
		super(code, args);
	}

	public CustomExampaperException(String defaultMessage, int code, Object... args) {
		super(defaultMessage, code, args);
	}

	public CustomExampaperException(Throwable cause, int code, Object... args) {
		super(cause, code, args);
	}

}
