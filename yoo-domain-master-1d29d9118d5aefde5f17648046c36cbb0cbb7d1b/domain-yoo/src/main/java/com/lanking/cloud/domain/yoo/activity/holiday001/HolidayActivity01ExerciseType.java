package com.lanking.cloud.domain.yoo.activity.holiday001;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 假期活动01-预置习题类型
 * 
 * <pre>
 * 1.活动名称：”这个暑假，有我，由你”(2017年6月13日)
 * </pre>
 * 
 * @since 4.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年6月13日
 */
public enum HolidayActivity01ExerciseType implements Valueable {
	/**
	 * 预置
	 * 
	 * <pre>
	 * 1.活动名称：”这个暑假，有我，由你”(2017年6月13日):暑假综合测试卷
	 * </pre>
	 */
	PRESET(0),
	/**
	 * 错题
	 * 
	 * <pre>
	 * 1.活动名称：”这个暑假，有我，由你”(2017年6月13日):个性化错题专练
	 * </pre>
	 */
	FALLIBLE(1),
	/**
	 * 知识点
	 * 
	 * <pre>
	 * 1.活动名称：”这个暑假，有我，由你”(2017年6月13日):薄弱知识点专练
	 * </pre>
	 */
	KNOWLEDGE_POINT(2);

	private int value;

	HolidayActivity01ExerciseType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

}
