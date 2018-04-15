package com.lanking.cloud.job.paperReport.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReportRecord;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReportStatus;
import com.lanking.cloud.job.paperReport.dao.StudentPaperReportRecordDAO;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;

@Component
public class StudentPaperReportRecordDAOImpl extends AbstractHibernateDAO<StudentPaperReportRecord, Long>
		implements StudentPaperReportRecordDAO {

	@Autowired
	@Qualifier("StudentPaperReportRecordRepo")
	@Override
	public void setRepo(Repo<StudentPaperReportRecord, Long> repo) {
		this.repo = repo;
	}

	@Override
	public List<StudentPaperReportRecord> findDataProductingList() {
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(StudentPaperReportStatus.DATA_PRODUCING.ordinal());
		statusList.add(StudentPaperReportStatus.FAIL.ordinal());
		return repo.find("$findDataProductingList", Params.param("statusList", statusList)).list();

	}

	@Override
	public List<StudentPaperReportRecord> findDataToFileList() {
		return repo
				.find("$findDataToFileList", Params.param("status", StudentPaperReportStatus.FILE_PRODUCING.ordinal()))
				.list();
	}

	@Override
	public void successFile(Collection<Long> recordIds) {
		if (CollectionUtils.isNotEmpty(recordIds)) {
			repo.execute("$successFile",
					Params.param("recordIds", recordIds).put("status", StudentPaperReportStatus.SUCCESS.ordinal()));
		}
	}
}
