package com.lanking.uxb.service.code.cache;

import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.baseData.SchoolType;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Service
public class SchoolCacheService extends AbstractCacheService {
	private ValueOperations<String, List<School>> schoolsOpt;

	private ValueOperations<String, Map<String, School>> stringMapSchoolOpt;
	private ValueOperations<String, Map<Long, School>> longMapSchoolOpt;

	private static final String DISTRICT_SCHOOL_KEY = "dsk";
	private static final String MAP_SCHOOL_KEY = "map";

	private String getDistrictSchoolCacheKey(Long districtCode, SchoolType type) {
		return assemblyKey(DISTRICT_SCHOOL_KEY, districtCode, type.getValue());
	}

	public void setDistrictSchool(Long districtCode, List<School> schools, SchoolType type) {
		schoolsOpt.set(getDistrictSchoolCacheKey(districtCode, type), schools);
	}

	public List<School> getDistrictSchool(Long districtCode, SchoolType type) {
		return schoolsOpt.get(getDistrictSchoolCacheKey(districtCode, type));
	}

	public List<School> getAllSchool() {
		return schoolsOpt.get(getDistrictSchoolCacheKey(0L, SchoolType.NULL));
	}

	public void setAllSchool(List<School> schools) {
		schoolsOpt.set(getDistrictSchoolCacheKey(0L, SchoolType.NULL), schools);
	}

	private String getStringMapSchoolCacheKey(String type) {
		return assemblyKey(MAP_SCHOOL_KEY, type);
	}

	public void setStringMapSchoolCache(String type, Map<String, School> schools) {
		stringMapSchoolOpt.set(getStringMapSchoolCacheKey(type), schools);
	}

	public Map<String, School> getStringMapSchoolCache(String type) {
		return stringMapSchoolOpt.get(getStringMapSchoolCacheKey(type));
	}

	private String getLongMapSchoolCacheKey(String type) {
		return assemblyKey(MAP_SCHOOL_KEY, type);
	}

	public void setLongMapSchoolCache(String type, Map<Long, School> schools) {
		longMapSchoolOpt.set(getLongMapSchoolCacheKey(type), schools);
	}

	public Map<Long, School> getLongMapSchoolCache(String type) {
		return longMapSchoolOpt.get(getLongMapSchoolCacheKey(type));
	}

	@Override
	public String getNs() {
		return "scs";
	}

	@Override
	public String getNsCn() {
		return "学校";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		schoolsOpt = getRedisTemplate().opsForValue();
		stringMapSchoolOpt = getRedisTemplate().opsForValue();
		longMapSchoolOpt = getRedisTemplate().opsForValue();
	}
}
