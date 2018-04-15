package com.lanking.uxb.service.code.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.baseData.SchoolType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.SchoolService;

@Service
@Transactional(readOnly = true)
@ConditionalOnExpression("!${common.code.cache}")
public class SchoolServiceImpl implements SchoolService {

	@Autowired
	@Qualifier("SchoolRepo")
	private Repo<School, Long> schoolRepo;

	@Override
	public School get(long id) {
		return schoolRepo.get(id);
	}

	@Override
	public School get(String code) {
		return schoolRepo.find("$getSchoolByCode", Params.param("code", code)).get();
	}

	@Override
	public Map<Long, School> mget(Collection<Long> ids) {
		return schoolRepo.mget(ids);
	}

	@Override
	public School getSchoolByName(String name) {
		return schoolRepo.find("$getSchoolByName", Params.param("schoolName", name)).get();
	}

	@Override
	public List<School> findSchoolByDistrictCode(Long districtCode, SchoolType schoolType) {
		if (districtCode == null)
			return Lists.newArrayList();
		Params params = Params.param("districtCode", districtCode);
		if (schoolType != null) {
			if (schoolType != SchoolType.PRIMARY_MIDDLE_HIGH) {
				params.put("type", schoolType.getValue());
			}
		}
		return schoolRepo.find("$getSchoolByDistrict", params).list();
	}

	@Override
	public List<School> getSchoolByNameLike(String name) {
		Params params = Params.param("schoolName", "%" + name + "%");
		return schoolRepo.find("$getSchoolByNameLike", params).list();
	}
}
