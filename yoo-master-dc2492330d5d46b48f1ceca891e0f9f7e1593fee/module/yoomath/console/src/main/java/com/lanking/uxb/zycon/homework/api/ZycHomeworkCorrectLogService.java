package com.lanking.uxb.zycon.homework.api;

import com.lanking.cloud.domain.support.console.common.HomeworkCorrectLog;
import com.lanking.cloud.domain.support.console.common.HomeworkCorrectLogType;
import com.lanking.uxb.zycon.homework.form.HomeworkCorrectLogForm;

import java.util.Collection;

/**
 * 作业批改日志
 *
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
public interface ZycHomeworkCorrectLogService {
	/**
	 * 保存批改日志
	 *
	 * @param userId
	 *            批改人id
	 * @param form
	 *            {@link HomeworkCorrectLogForm}
	 * @param type
	 *            批改的作业类型
	 * @return {@link HomeworkCorrectLog}
	 */
	HomeworkCorrectLog save(long userId, HomeworkCorrectLogForm form, HomeworkCorrectLogType type);

	/**
	 * 批量保存批改日志
	 *
	 * @param userId
	 *            批改人id
	 * @param forms
	 *            {@link HomeworkCorrectLogForm}
	 * @param type
	 *            批改的作业类型
	 */
	void save(long userId, Collection<HomeworkCorrectLogForm> forms, HomeworkCorrectLogType type);
}
