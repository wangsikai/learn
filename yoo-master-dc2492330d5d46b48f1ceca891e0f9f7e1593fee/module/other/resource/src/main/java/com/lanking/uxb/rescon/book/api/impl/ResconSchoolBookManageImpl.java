package com.lanking.uxb.rescon.book.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoomath.school.SchoolBook;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.book.api.ResconSchoolBookManage;

/**
 * 书本学校信息接口实现.
 * 
 * @author wlche
 *
 */
@Service
@Transactional(readOnly = true)
public class ResconSchoolBookManageImpl implements ResconSchoolBookManage {
	@Autowired
	@Qualifier("SchoolBookRepo")
	Repo<SchoolBook, Long> schoolBookRepo;
	@Autowired
	@Qualifier("SchoolRepo")
	Repo<School, Long> schoolRepo;

	@Override
	public List<School> listSchool(long bookid) {
		List<Long> schoolIds = schoolBookRepo.find("$getSchoolIdsByBookId", Params.param("bookid", bookid)).list(
				Long.class);
		if (schoolIds.size() > 0) {
			return schoolRepo.mgetList(schoolIds);
		}
		return Lists.newArrayList();
	}
}
