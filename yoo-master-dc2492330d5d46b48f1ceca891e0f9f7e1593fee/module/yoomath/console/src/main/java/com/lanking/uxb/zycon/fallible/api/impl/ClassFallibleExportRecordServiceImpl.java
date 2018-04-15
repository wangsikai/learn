package com.lanking.uxb.zycon.fallible.api.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.fallible.ClassFallibleExportRecord;
import com.lanking.cloud.domain.yoomath.fallible.ClassFallibleExportRecordStatus;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.fallible.api.ClassFallibleExportRecordService;

@Service
@Transactional(readOnly = true)
public class ClassFallibleExportRecordServiceImpl implements ClassFallibleExportRecordService {

	@Autowired
	@Qualifier("ClassFallibleExportRecord")
	private Repo<ClassFallibleExportRecord, Long> repo;

	@Override
	@Transactional
	public void save(ClassFallibleExportRecord record) {
		repo.save(record);
	}

	@Override
	public Page<ClassFallibleExportRecord> query(Pageable pageable) {
		return repo.find("$query").fetch(pageable);
	}

	@Override
	public ClassFallibleExportRecord get(long id) {
		return repo.get(id);
	}


	@Override
	@Transactional
	public void updateStatus(long id, List<String> noQuestionStuNames,ClassFallibleExportRecordStatus status) {
		ClassFallibleExportRecord record = repo.get(id);
		record.setStatus(status);
		if(null != noQuestionStuNames && noQuestionStuNames.size()>0){
			record.setNqStudentNameList(JSON.toJSONString(noQuestionStuNames));
		}
		repo.save(record);
	}

	@Override
	public ClassFallibleExportRecord findByClassIdandStatus(Long clazzId, int status) {
		Params param = Params.param("classId", clazzId).put("status", status);
		List<ClassFallibleExportRecord> list = repo.find("$queryByclassId", param).list(ClassFallibleExportRecord.class);
		if (null != list && list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
