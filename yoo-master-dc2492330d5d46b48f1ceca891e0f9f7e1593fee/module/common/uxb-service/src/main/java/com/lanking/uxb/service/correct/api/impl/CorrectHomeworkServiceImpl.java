package com.lanking.uxb.service.correct.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkCorrectStatus;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.correct.api.CorrectHomeworkService;
import com.lanking.uxb.service.correct.api.CorrectStudentHomeworkService;

@Service
public class CorrectHomeworkServiceImpl implements CorrectHomeworkService {

	@Autowired
	private CorrectStudentHomeworkService correctStudentHomeworkService;

	@Autowired
	@Qualifier("HomeworkRepo")
	Repo<Homework, Long> homeworkRepo;

	@Override
	@Transactional(readOnly = true)
	public Homework get(long id) {
		return homeworkRepo.get(id);
	}

	@Override
	@Transactional
	public void setTobeCorrected(long homeworkId, Boolean tobeCorrected) {
		Homework homework = homeworkRepo.get(homeworkId);
		homework.setTobeCorrected(tobeCorrected);
		homeworkRepo.save(homework);
	}

	@Override
	@Transactional
	public void addCorrectingCount(long homeworkId) {
		homeworkRepo.execute("$addCorrectingCount", Params.param("homeworkId", homeworkId));
	}

	@Override
	@Transactional
	public void allCorrectComplete(long homeworkId) {
		Homework homework = homeworkRepo.get(homeworkId);
		homework.setIssueAt(new Date());
		homework.setAllCorrectComplete(true);
		homeworkRepo.save(homework);
	}

	@Override
	@Transactional
	public void checkAndSaveHomeworkCorrectComplete(long homeworkId) {
		List<StudentHomework> studentHomeworks = correctStudentHomeworkService.listByHomework(homeworkId);
		int completeCount = 0;
		for (StudentHomework studentHomework : studentHomeworks) {
			if (studentHomework.getRightRate() != null
					|| studentHomework.getCorrectStatus() == StudentHomeworkCorrectStatus.COMPLETE
					|| (studentHomework.getSubmitAt() != null && studentHomework.getStuSubmitAt() == null)) {
				// 计算出正确率的作业、设置为已批改完成的作业、被强制提交并且学生没有做答的作业
				completeCount++;
			}
		}
		if (completeCount == studentHomeworks.size()) {
			Homework homework = homeworkRepo.get(homeworkId);
			homework.setIssueAt(new Date());
			homework.setAllCorrectComplete(true);
			homeworkRepo.save(homework);
		}
	}
}
