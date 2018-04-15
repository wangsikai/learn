package com.lanking.uxb.service.user.api.impl;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.common.baseData.Phase;
import com.lanking.cloud.domain.common.baseData.Subject;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.ex.UserException;
import com.lanking.uxb.service.user.form.EditProfileForm;
import com.lanking.uxb.service.zuoye.api.ZyUserSchoolBookService;

@Service("teacherService")
@Transactional(readOnly = true)
public class TeacherServiceImpl extends TeacherUserService implements TeacherService {

	private Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);
	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private ZyUserSchoolBookService userSbService;
	@Autowired
	private TextbookService textbookService;

	@Transactional
	@Override
	public void setPhaseSubject(long userId, int phaseCode, int subjectCode) throws UserException {
		Teacher teacher = teacherRepo.get(userId);
		if ((teacher.getPhaseCode() == null || teacher.getPhaseCode() == 0)
				&& (teacher.getSubjectCode() == null || teacher.getSubjectCode() == 0)) {
			Phase phase = phaseService.get(phaseCode);
			if (phase == null) {
				throw new NullPointerException();
			}
			Subject subject = subjectService.get(subjectCode);
			if (subject == null) {
				throw new NullPointerException();
			}
			if (subject.getPhaseCode() != phaseCode) {
				throw new ServerException();
			}
			teacher.setPhaseCode(phaseCode);
			teacher.setSubjectCode(subjectCode);
			teacherRepo.save(teacher);
		} else {
			throw new ServerException();
		}
	}

	@Transactional
	@Override
	public Teacher updateTeacher(EditProfileForm ef) {
		Teacher t = teacherRepo.get(ef.getId());
		Teacher teacher = null;
		if (StringUtils.isNotBlank(ef.getName())) {
			t.setName(ef.getName());
			updateUsername(t.getId(), ef.getName());
		}
		if (StringUtils.isNotBlank(ef.getNickname())) {
			t.setNickname(ef.getNickname());
			updateNickname(t.getId(), ef.getNickname());
		}
		if (ef.getSex() != null) {
			t.setSex(ef.getSex());
		}
		if (ef.getSchoolName() != null) {
			t.setSchoolName(ef.getSchoolName());
		}
		if (ef.getDutyCode() != null) {
			t.setDutyCode(ef.getDutyCode());
		}
		if (ef.getTitleCode() != null) {
			t.setTitleCode(ef.getTitleCode());
		}
		if (ef.getSchoolCode() != null) {
			t.setSchoolId(ef.getSchoolCode());
		}
		if (StringUtils.isNotEmpty(ef.getWorkAt())) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				t.setWorkAt(sdf.parse(ef.getWorkAt()));
			} catch (Exception e) {
				logger.error("set workAt failed...", e);
			}
		}
		if (ef.getTextBookCategoryCode() != null) {
			t.setTextbookCategoryCode(ef.getTextBookCategoryCode());
			// 修改版本后教师的教材默认为版本下第一个教材
			t.setTextbookCode(textbookService.find(ef.getPhaseCode(), ef.getTextBookCategoryCode(), t.getSubjectCode())
					.get(0).getCode());
			// 如果更新的版本与之前的不一致，需要同步清楚用户在布置作业页面，已选择的教辅图书
			if (!ef.getTextBookCategoryCode().equals(t.getTextbookCategoryCode())) {
				userSbService.updateTeacherChoosedBook(ef.getId(), Status.DELETED);
			}
		}
		if (ef.getTextBookCode() != null) {
			t.setTextbookCode(ef.getTextBookCode());
		}
		if (ef.getPhaseCode() != null) {
			t.setPhaseCode(ef.getPhaseCode());
			t.setSubjectCode(ef.getPhaseCode() == PhaseService.PHASE_HIGH ? SubjectService.PHASE_3_MATH
					: SubjectService.PHASE_2_MATH);
		}
		teacher = teacherRepo.save(t);
		return teacher;
	}

	@Override
	public Long isUserName(String name) {
		Teacher teacher = teacherRepo.find("$getTeacherByName", Params.param("name", name)).get();
		if (teacher != null) {
			return teacher.getId();
		} else {
			return null;
		}
	}

	@Override
	public CursorPage<Long, Teacher> getAll(CursorPageable<Long> cursorPageable) {
		return teacherRepo.find("$getAllByPage").fetch(cursorPageable);
	}

	@Transactional
	@Override
	public void updateCategory(long userId, long textbookCategoryCode, long textBookCode) {
		teacherRepo.execute(
				"$uptTeacherCategory",
				Params.param("userId", userId).put("textbookCategoryCode", textbookCategoryCode)
						.put("textBookCode", textBookCode));
	}

}
