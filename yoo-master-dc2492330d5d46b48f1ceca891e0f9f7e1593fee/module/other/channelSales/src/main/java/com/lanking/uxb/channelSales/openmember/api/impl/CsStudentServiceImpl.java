package com.lanking.uxb.channelSales.openmember.api.impl;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.channelSales.openmember.api.CsStudentService;
import com.lanking.uxb.channelSales.openmember.form.UserQueryForm;

/**
 * @see CsStudentService
 * @author xinyu.zhou
 * @since 2.5.0
 */
@Service
@Transactional(readOnly = true)
public class CsStudentServiceImpl implements CsStudentService {
	@Autowired
	@Qualifier("StudentRepo")
	private Repo<Student, Long> studentRepo;

	@Override
	public Page<Student> query(UserQueryForm queryForm) {
		Pageable pageable = P.index(queryForm.getPage(), queryForm.getSize());

		Params params = Params.param();
		if (StringUtils.isNotBlank(queryForm.getAccountName())) {
			params.put("accountName", "%" + queryForm.getAccountName() + "%");
		}
		if (StringUtils.isNotBlank(queryForm.getChannelName())) {
			params.put("channelName", "%" + queryForm.getChannelName() + "%");
		}
		if (StringUtils.isNotBlank(queryForm.getSchoolName())) {
			params.put("schoolName", "%" + queryForm.getSchoolName() + "%");
		}
		if (StringUtils.isNotBlank(queryForm.getUserName())) {
			params.put("userName", "%" + queryForm.getUserName() + "%");
		}
		if (queryForm.getPhaseCode() != null) {
			params.put("phaseCode", queryForm.getPhaseCode());
		}
		if (queryForm.getSchoolId() != null) {
			params.put("schoolId", queryForm.getSchoolId());
		}
		if (queryForm.getMemberType() != null) {
			params.put("memberType", queryForm.getMemberType().getValue());
			params.put("nowDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		}
		if (queryForm.getClassId() != null) {
			params.put("classId", queryForm.getClassId());
		}

		return studentRepo.find("$csQuery", params).fetch(pageable);
	}

	@Override
	public List<Student> findChannelScoolStudent(long schoolId) {
		return studentRepo.find("$findByChannelSchool", Params.param("schoolId", schoolId)).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Student> mgetList(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.EMPTY_LIST;
		}
		return studentRepo.mgetList(ids);
	}
}
