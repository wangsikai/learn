package com.lanking.uxb.service.report.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 任务包里班级相关接口
 * 
 * @author wangsenhao
 *
 */
public interface TaskHomeworkClassService {

	HomeworkClazz get(long id);

	Map<Long, HomeworkClazz> mget(Collection<Long> ids);

	/**
	 * 获取老师当前有效班级
	 * 
	 * @param teacherId
	 * @return
	 */
	List<HomeworkClazz> listCurrentClazzs(long teacherId);

	CursorPage<Long, HomeworkClazz> getAll(CursorPageable<Long> cursorPageable);

}
