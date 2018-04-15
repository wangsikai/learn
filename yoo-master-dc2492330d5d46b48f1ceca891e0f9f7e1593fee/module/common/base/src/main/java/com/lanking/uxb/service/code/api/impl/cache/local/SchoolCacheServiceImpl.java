package com.lanking.uxb.service.code.api.impl.cache.local;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.baseData.SchoolType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.cache.SchoolCacheService;

/**
 * 集群环境下的缓存实现
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月6日
 */
@SuppressWarnings("unchecked")
@Service
@ConditionalOnExpression("${common.code.cache}")
public class SchoolCacheServiceImpl extends AbstractBaseDataHandle implements SchoolService {

	@Autowired
	@Qualifier("SchoolRepo")
	private Repo<School, Long> schoolRepo;

	private Map<Long, School> allMapById = null;
	private Map<String, School> allMapByCode = null;
	private Map<String, School> allMapByName = null;
	@Autowired
	private SchoolCacheService schoolCacheService;

	@Override
	public School get(long id) {
		if (allMapById == null) {
			reload();
		}
		return allMapById.get(id);
	}

	@Override
	public School get(String code) {
		if (allMapByCode == null) {
			reload();
		}
		return allMapByCode.get(code);
	}

	@Override
	public Map<Long, School> mget(Collection<Long> ids) {
		if (allMapById == null) {
			reload();
		}
		Map<Long, School> map = Maps.newHashMap();
		for (Long id : ids) {
			School school = allMapById.get(id);
			if (school != null) {
				map.put(id, school);
			}
		}
		return map;
	}

	@Override
	public School getSchoolByName(String name) {
		if (allMapByName == null) {
			reload();
		}
		return allMapByName.get(name);
	}

	@Transactional(readOnly = true)
	@Override
	public List<School> findSchoolByDistrictCode(Long districtCode, SchoolType schoolType) {
		if (districtCode == null) {
			return Collections.EMPTY_LIST;
		}
		List<School> schools = Lists.newArrayList();
		if (schoolType != null) {
			schools = schoolCacheService.getDistrictSchool(districtCode, schoolType);
		} else {
			for (SchoolType type : SchoolType.values()) {
				if (schoolCacheService.getDistrictSchool(districtCode, type) != null) {
					schools.addAll(schoolCacheService.getDistrictSchool(districtCode, type));
				}
			}
		}
		if (CollectionUtils.isEmpty(schools)) {
			Params params = Params.param("districtCode", districtCode);
			if (schoolType != null && schoolType != SchoolType.PRIMARY_MIDDLE_HIGH) {
				params.put("type", schoolType.getValue());
			}
			schools = schoolRepo.find("$getSchoolByDistrict", params).list();
			schoolCacheService.setDistrictSchool(districtCode, schools, schoolType);
		}
		return schools;
	}

	@Override
	public List<School> getSchoolByNameLike(String name) {
		if (allMapByName == null)
			reload();

		List<School> schools = Lists.newArrayList();
		for (String n : allMapByName.keySet()) {
			if (n.contains(name)) {
				schools.add(allMapByName.get(n));
			}
		}
		return schools;
	}

	@Override
	public BaseDataType getType() {
		return BaseDataType.SCHOOL;
	}

	@Transactional(readOnly = true)
	@Override
	public void reload() {
		Map<Long, School> tmpAllMapById = Maps.newHashMap();
		Map<String, School> tmpAllMapByCode = Maps.newHashMap();
		Map<String, School> tmpAllMapByName = Maps.newHashMap();
		Map<String, List<School>> districtSchool = Maps.newHashMap();
		List<School> list = schoolRepo.find("$findAllSchool").list();
		for (School school : list) {
			school.setCreateAt(null);
			school.setCloseAt(null);
			school.setOpenAt(null);
			tmpAllMapById.put(school.getId(), school);
			tmpAllMapByCode.put(school.getCode(), school);
			tmpAllMapByName.put(school.getName(), school);
			if (school.getDistrictCode() != null && school.getType() != null) {
				List<School> districtList = districtSchool
						.get(school.getDistrictCode() + "_" + school.getType().getValue());
				if (districtList == null) {
					districtList = Lists.newArrayList();
				}
				districtList.add(school);
				districtSchool.put(school.getDistrictCode() + "_" + school.getType().getValue(), districtList);
			}
		}
		for (String key : districtSchool.keySet()) {
			long districtCode = Long.parseLong(key.split("_")[0]);
			SchoolType schoolType = SchoolType.findByValue(Integer.parseInt(key.split("_")[1]));
			schoolCacheService.setDistrictSchool(districtCode, districtSchool.get(key), schoolType);
		}
		allMapById = tmpAllMapById;
		allMapByCode = tmpAllMapByCode;
		allMapByName = tmpAllMapByName;
		schoolCacheService.setAllSchool(list);
		schoolCacheService.setLongMapSchoolCache("id", allMapById);
		schoolCacheService.setStringMapSchoolCache("code", allMapByCode);
		schoolCacheService.setStringMapSchoolCache("name", allMapByName);
	}

	@Transactional(readOnly = true)
	@Override
	public void init() {
		allMapById = schoolCacheService.getLongMapSchoolCache("id");
		allMapByCode = schoolCacheService.getStringMapSchoolCache("code");
		allMapByName = schoolCacheService.getStringMapSchoolCache("name");
		if (allMapById == null || allMapByCode == null || allMapByName == null) {
			reload();
		}
	}

	@Override
	public long size() {
		Long allMapByIdSize = getObjectDeepSize(allMapById);
		Long allMapByCodeSize = getObjectDeepSize(allMapByCode);
		Long allMapByNameSize = getObjectDeepSize(allMapByName);

		return allMapByIdSize + allMapByCodeSize + allMapByNameSize;
	}
}
