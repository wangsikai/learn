package com.lanking.cloud.job.stuWeekReport.dao;

import java.util.Map;

import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

public interface UserDAO {

	/**
	 * 获取学生Id
	 * 
	 * @param pageable
	 * @param modVal
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	CursorPage<Long, Map> queryUserId(CursorPageable<Long> pageable, int modVal);

}
