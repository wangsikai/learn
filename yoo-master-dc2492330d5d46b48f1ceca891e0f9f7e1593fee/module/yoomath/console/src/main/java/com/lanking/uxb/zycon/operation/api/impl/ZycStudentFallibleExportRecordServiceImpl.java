package com.lanking.uxb.zycon.operation.api.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleExportRecord;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.operation.api.ZycStudentFallibleExportRecordService;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class ZycStudentFallibleExportRecordServiceImpl implements ZycStudentFallibleExportRecordService {

	@Autowired
	@Qualifier("StudentFallibleExportRecordRepo")
	Repo<StudentFallibleExportRecord, Long> recordRepo;

	@Override
	public StudentFallibleExportRecord getByOrderId(Long fallibleQuestionPrintOrderId) {
		return recordRepo.find("$getByOrderId",
				Params.param("fallibleQuestionPrintOrderId", fallibleQuestionPrintOrderId)).get();
	}

	@Override
	public Map<Long, StudentFallibleExportRecord> mgetByOrderIds(Collection<Long> fallibleQuestionPrintOrderIds) {
		List<StudentFallibleExportRecord> list = recordRepo.find("$mgetByOrderIds",
				Params.param("fallibleQuestionPrintOrderIds", fallibleQuestionPrintOrderIds)).list();
		Map<Long, StudentFallibleExportRecord> map = new HashMap<Long, StudentFallibleExportRecord>();
		for (StudentFallibleExportRecord r : list) {
			map.put(r.getFallibleQuestionPrintOrderId(), r);
		}
		return map;
	}
}
