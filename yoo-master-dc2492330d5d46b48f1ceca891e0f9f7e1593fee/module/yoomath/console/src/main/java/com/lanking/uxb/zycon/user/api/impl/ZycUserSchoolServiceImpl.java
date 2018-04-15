package com.lanking.uxb.zycon.user.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.baseData.SchoolType;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.event.ClusterEvent;
import com.lanking.cloud.sdk.event.ClusterEventSender;
import com.lanking.cloud.sdk.event.LocalCacheEvent;
import com.lanking.uxb.service.code.api.BaseDataAction;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.zycon.user.api.ZycUserSchoolService;
import com.lanking.uxb.zycon.user.form.SchoolForm;
import com.lanking.uxb.zycon.user.util.ZycPinyin;

@Transactional(readOnly = true)
@Service
public class ZycUserSchoolServiceImpl implements ZycUserSchoolService {

	@Autowired
	@Qualifier("SchoolRepo")
	Repo<School, Long> schoolRepo;

	@Autowired
	@Qualifier(value = "clusterDataSender")
	private ClusterEventSender sender;

	@Transactional
	@Override
	public void saveSchool(SchoolForm form) {
		School school = null;
		if (form.getId() != null) {
			school = schoolRepo.get(form.getId());
			school.setUpdateAt(new Date());
		} else {
			school = new School();
			school.setCreateAt(new Date());
		}
		school.setDistrictCode(form.getAreaCode());
		school.setName(form.getSchoolName());
		school.setType(form.getPhaseCode() == PhaseService.PHASE_HIGH ? SchoolType.HIGH : SchoolType.MIDDLE);
		// 拼音首字母
		ZycPinyin cte = new ZycPinyin();
		school.setAcronym(cte.getAllFirstLetter(form.getSchoolName()));
		schoolRepo.save(school);
		school.setCode(school.getId().toString());
		schoolRepo.save(school);
	}

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
		return schoolRepo.find("$zyQuerySchool", params).fetch(p);
	}

	@Override
	public School getSchool(Long id) {
		return schoolRepo.get(id);
	}

	@Override
	public void syncData() {
		ClusterEvent<String> event1 = new LocalCacheEvent<String>(BaseDataAction.RELOAD.name(),
				BaseDataType.SCHOOL.name());
		sender.send(event1);
	}
}
