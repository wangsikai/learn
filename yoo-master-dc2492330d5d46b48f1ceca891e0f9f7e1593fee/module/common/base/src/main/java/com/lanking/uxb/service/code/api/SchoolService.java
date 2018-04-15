package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.baseData.SchoolType;

public interface SchoolService {

	School get(long id);

	School get(String code);

	Map<Long, School> mget(Collection<Long> ids);

	School getSchoolByName(String name);

	List<School> findSchoolByDistrictCode(Long districtCode, SchoolType schoolType);

	List<School> getSchoolByNameLike(String name);

}
