package com.lanking.uxb.zycon.qs.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.School;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
public interface ZycSchoolService {
	School get(long id);

	List<School> mgetList(Collection<Long> ids);

	Map<Long, School> mget(Collection<Long> ids);
}
