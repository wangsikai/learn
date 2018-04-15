package com.lanking.uxb.zycon.homework.api;

import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author xinyu.zhou
 * @since yoomath V1.5
 */
public interface ZycStudentService {
	Student get(long id);

	List<Student> mgetList(Collection<Long> ids);

	Map<Long, Student> mget(Collection<Long> ids);

	CursorPage<Long, Student> query(CursorPageable<Long> pageable);
}
