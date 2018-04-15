package com.lanking.uxb.channelSales.base.api.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.baseData.SchoolType;
import com.lanking.cloud.domain.yoo.channel.ChannelSchool;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.channelSales.base.api.CsSchoolService;
import com.lanking.uxb.channelSales.base.form.SchoolForm;
import com.lanking.uxb.service.code.api.PhaseService;

/**
 * 基础数据--学校管理
 * 
 * @author wangsenhao
 *
 */
@SuppressWarnings("unchecked")
@Service
@Transactional(readOnly = true)
public class CsSchoolServiceImpl implements CsSchoolService {
	@Autowired
	@Qualifier("SchoolRepo")
	Repo<School, Long> schoolRepo;
	@Autowired
	@Qualifier("ChannelSchoolRepo")
	Repo<ChannelSchool, Long> channelSchoolRepo;

	@Override
	public Page<School> query(SchoolForm query, Pageable p) {
		Params params = Params.param();
		if (query.getAreaCode() != null) {
			params.put("code", query.getAreaCode());
		} else {
			if (query.getCityCode() != null) {
				params.put("code", query.getCityCode().toString().substring(0, 4) + "%");
			} else {
				if (query.getProvinceCode() != null) {
					params.put("code", query.getProvinceCode().toString().substring(0, 2) + "%");
				}
			}
		}
		if (query.getPhaseCode() != null) {
			params.put("type", query.getPhaseCode() == PhaseService.PHASE_HIGH ? SchoolType.HIGH.getValue()
					: SchoolType.MIDDLE.getValue());
		}
		if (query.getSchoolName() != null) {
			params.put("schoolName", "%" + query.getSchoolName() + "%");
		}
		return schoolRepo.find("$csQuerySchoolList", params).fetch(p);
	}

	@Transactional
	@Override
	public void bindChannel(Long schoolId, Integer channelCode, Long userId) {
		ChannelSchool cs = new ChannelSchool();
		cs.setChannelCode(channelCode);
		cs.setCreateAt(new Date());
		cs.setCreateId(userId);
		cs.setSchoolId(schoolId);
		cs.setStartAt(new Date());
		cs.setStatus(Status.ENABLED);
		channelSchoolRepo.save(cs);
	}

	@Override
	public boolean isExistChannel(Integer code) {
		Integer count = schoolRepo.find("$csChannelCount", Params.param("code", code)).get(Integer.class);
		return count != 0;
	}

	@Override
	public School findByTeacherId(long teacherId) {
		return schoolRepo.find("$csFindByTeacher", Params.param("teacherId", teacherId)).get();
	}

	@Override
	public Map<Long, School> findByTeacherIds(Collection<Long> teacherIds) {
		if (CollectionUtils.isEmpty(teacherIds)) {
			return Collections.EMPTY_MAP;
		}

		List<Map> queryResults = schoolRepo.find("$csFindByTeachers", Params.param("teacherIds", teacherIds)).list(
				Map.class);

		Map<Long, School> retMap = new HashMap<Long, School>(queryResults.size());
		for (Map m : queryResults) {
			Long teacherId = ((BigInteger) m.get("teacher_id")).longValue();
			Long schoolId = ((BigInteger) m.get("school_id")).longValue();
			String schoolName = (String) m.get("school_name");
			String schoolCode = (String) m.get("school_code");

			School school = new School();
			school.setId(schoolId);
			school.setName(schoolName);
			school.setCode(schoolCode);

			retMap.put(teacherId, school);
		}
		return retMap;
	}

	@Override
	public List<Map> countSchoolUserNum(Collection<Long> schoolIds, UserType userType) {
		if (CollectionUtils.isEmpty(schoolIds)) {
			return Collections.EMPTY_LIST;
		}
		return schoolRepo.find("$csCountUserNum",
				Params.param("schoolIds", schoolIds).put("userType", userType.getValue())).list(Map.class);
	}

	@Override
	public List<Map> countSchoolTeacherMemberNum(Collection<Long> schoolIds) {
		if (CollectionUtils.isEmpty(schoolIds)) {
			return Collections.EMPTY_LIST;
		}
		Params params = Params.param("nowDate", new Date());
		params.put("schoolIds", schoolIds);
		return schoolRepo.find("$csCountTeacherMemberNum", params).list(Map.class);
	}

	@Override
	public List<Map> countSchoolStudentMemberNum(Collection<Long> schoolIds) {
		if (CollectionUtils.isEmpty(schoolIds)) {
			return Collections.EMPTY_LIST;
		}

		Params params = Params.param("nowDate", new Date());
		params.put("schoolIds", schoolIds);
		return schoolRepo.find("$csCountStudentMemberNum", params).list(Map.class);
	}

}
