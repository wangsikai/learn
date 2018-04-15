package com.lanking.uxb.zycon.homework.api;

import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.homework.form.HomeworkQueryForm;
import com.lanking.uxb.zycon.homework.form.HomeworkQueryType;

import java.util.List;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
public interface ZycCorrectingService {

	/**
	 * 分页查询当前的作业
	 *
	 * @param pageable
	 *            分页条件
	 * @param type
	 *            {@link HomeworkQueryType}
	 * @return 分页数据
	 */
	Page<Homework> page(Pageable pageable, HomeworkQueryType type);

	/**
	 * 得到所有待批改的作业
	 *
	 * @param lastCommitMinute
	 *            最终提交时间
	 * @return List类型的数据
	 */
	List<Homework> list(int lastCommitMinute);

	/**
	 * 查询作业
	 *
	 * @param form
	 *            {@link HomeworkQueryForm}
	 * @return {@link Page}
	 */
	Page<Homework> page(HomeworkQueryForm form);
}
