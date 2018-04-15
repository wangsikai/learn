package com.lanking.uxb.zycon.homework.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
public interface ZycHomeworkClazzService {
	/**
	 * 根据id查询HomeworkClazz
	 *
	 * @param id
	 *            id
	 * @return 数据
	 */
	HomeworkClazz get(Long id);

	/**
	 * mgetList HomeworkClazz
	 *
	 * @param ids
	 *            ids
	 * @return 数据
	 */
	List<HomeworkClazz> mgetList(Collection<Long> ids);

	/**
	 * 教师当前有效班级
	 * 
	 * @param teacherId
	 * @return
	 */
	List<HomeworkClazz> listCurrentClazzs(long teacherId);

	/**
	 * 获取所有班级
	 * 
	 * @param cursorPageable
	 * 
	 * @return
	 */
	CursorPage<Long, HomeworkClazz> getAll(CursorPageable<Long> cursorPageable);
}
