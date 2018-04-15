package com.lanking.cloud.job.stuWeekReport.dao;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

public interface HomeworkClazzDAO {

	/**
	 * 获取班级Id
	 * 
	 * @param pageable
	 * @param modVal
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	CursorPage<Long, Map> queryClassIds(CursorPageable<Long> pageable, int modVal);

	Map<Long, HomeworkClazz> mget(Collection<Long> ids);

}
