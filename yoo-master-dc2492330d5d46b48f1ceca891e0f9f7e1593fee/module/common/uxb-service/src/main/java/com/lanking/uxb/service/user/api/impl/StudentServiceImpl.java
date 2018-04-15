package com.lanking.uxb.service.user.api.impl;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.form.EditProfileForm;

@Service("studentService")
@Transactional(readOnly = true)
public class StudentServiceImpl extends StudentUserServiceImpl implements StudentService {

	private Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

	@Transactional
	@Override
	public void updateStudent(EditProfileForm ef) {
		Student s = studentRepo.get(ef.getId());
		if (StringUtils.isNotBlank(ef.getName())) {
			s.setName(ef.getName());
			updateUsername(s.getId(), ef.getName());
		}
		if (StringUtils.isNotBlank(ef.getNickname())) {
			s.setNickname(ef.getNickname());
			updateNickname(s.getId(), ef.getNickname());
		}
		if (ef.getSex() != null) {
			s.setSex(ef.getSex());
		}
		if (StringUtils.isNotEmpty(ef.getBirthDay())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				s.setBirthday(sdf.parse(ef.getBirthDay()));
			} catch (Exception e) {
				logger.error("set birthDay failed...", e);
			}
		}
		if (ef.getSchoolCode() != null) {
			s.setSchoolId(ef.getSchoolCode());
		}
		if (ef.getPhaseCode() != null) {
			s.setPhaseCode(ef.getPhaseCode());
		}
		if (ef.getEnterYear() != null) {
			s.setYear(ef.getEnterYear());
		}
		studentRepo.save(s);
	}

	@Transactional
	@Override
	public void setTextbook(long userId, int phaseCode, int categoryCode, int textbookCode) {
		Student student = studentRepo.get(userId);
		student.setPhaseCode(phaseCode);
		student.setTextbookCategoryCode(categoryCode);
		student.setTextbookCode(textbookCode);
		studentRepo.save(student);
	}

	@Override
	public Page<Student> queryStudentByTextbookCode(Pageable p) {
		return studentRepo.find("$findByTextbookNotNull", Params.param()).fetch(p);
	}

	@Transactional
	@Override
	public void setYear(long userId, Integer enterYear) {
		Student student = studentRepo.get(userId);
		student.setYear(enterYear);
		studentRepo.save(student);
	}

}
