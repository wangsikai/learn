package com.lanking.uxb.service.zuoye.api.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.school.SchoolBook;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZySchoolBookService;

@Service
@Transactional(readOnly = true)
public class ZySchoolBookServiceImpl implements ZySchoolBookService {
	@Autowired
	@Qualifier("SchoolBookRepo")
	Repo<SchoolBook, Long> schoolBookRepo;

	@Override
	public Integer getSchoolBookByStatus(long schoolId, Status status) {
		return schoolBookRepo
				.find("$getSchoolBookByStatus", Params.param("schoolId", schoolId).put("status", status.getValue()))
				.get(Integer.class);
	}

	@Override
	public Map<Long, Long> findBySchoolAndBooK(List<String> unionIds) {
		List<Map> schoolbookMap = schoolBookRepo.find("$findBySchoolAndBooK", Params.param("unionIds", unionIds))
				.list(Map.class);
		Map<Long, Long> maps = Maps.newHashMap();
		for (Map map : schoolbookMap) {
			maps.put(Long.valueOf(map.get("book_id").toString()), Long.valueOf(map.get("id").toString()));
		}
		return maps;
	}

}
