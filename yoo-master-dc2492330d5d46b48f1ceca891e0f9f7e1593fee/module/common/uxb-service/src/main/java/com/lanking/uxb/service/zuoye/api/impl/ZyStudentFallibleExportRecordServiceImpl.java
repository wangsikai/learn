package com.lanking.uxb.service.zuoye.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleExportRecord;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleExportRecordService;

/**
 * 学生导出错题本服务service
 *
 * @author xinyu.zhou
 * @since yoomath V1.6
 */
@Service
@Transactional(readOnly = true)
public class ZyStudentFallibleExportRecordServiceImpl implements ZyStudentFallibleExportRecordService {

	@Autowired
	@Qualifier("StudentFallibleExportRecordRepo")
	private Repo<StudentFallibleExportRecord, Long> repo;

	@Override
	@Transactional
	public void save(StudentFallibleExportRecord record) {
		repo.save(record);
	}

	@Override
	public Page<StudentFallibleExportRecord> query(long studentId, Pageable pageable) {
		Params params = Params.param("studentId", studentId);
		return repo.find("$query", params).fetch(pageable);
	}

	@Override
	public StudentFallibleExportRecord get(long id) {
		return repo.get(id);
	}

	@Override
	@Transactional
	public void buy(long id) {
		StudentFallibleExportRecord record = repo.get(id);
		record.setBuy(true);
		repo.save(record);
	}

	@Override
	@Transactional
	public void updateStatus(long id, Status status) {
		StudentFallibleExportRecord record = repo.get(id);
		record.setStatus(status);
		repo.save(record);
	}

	@Override
	public StudentFallibleExportRecord findByHash(long studentId, int hash) {
		List<StudentFallibleExportRecord> list = repo
				.find("$findByHash", Params.param("studentId", studentId).put("hash", hash)).list();
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}
