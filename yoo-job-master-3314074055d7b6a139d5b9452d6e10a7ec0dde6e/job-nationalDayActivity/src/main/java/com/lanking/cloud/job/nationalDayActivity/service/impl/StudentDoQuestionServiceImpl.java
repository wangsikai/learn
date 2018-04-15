package com.lanking.cloud.job.nationalDayActivity.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Stu;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01DAO;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01QuestionDAO;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01StuDAO;
import com.lanking.cloud.job.nationalDayActivity.service.NationalDayActivity01Service;
import com.lanking.cloud.job.nationalDayActivity.service.StudentDoQuestionService;

@Transactional(readOnly = true)
@Service("nda01StudentDoQuestionService")
public class StudentDoQuestionServiceImpl implements StudentDoQuestionService {

	@Autowired
	@Qualifier("nda01NationalDayActivity01StuDAO")
	private NationalDayActivity01StuDAO nationalDayActivity01StuDAO;
	@Autowired
	@Qualifier("nda01NationalDayActivity01QuestionDAO")
	private NationalDayActivity01QuestionDAO nationalDayActivity01QuestionDAO;
	@Autowired
	@Qualifier("nda01NationalDayActivity01DAO")
	private NationalDayActivity01DAO nda01DAO;

	@Transactional
	@Override
	public void doQuestionRightCount(long studentId, long questionId, long doAt) {
		NationalDayActivity01 nda01 = nda01DAO.get(NationalDayActivity01Service.NATIONAL_DAY_ACTIVITY_ID);
		if (doAt >= nda01.getStartTime().getTime() && doAt <= nda01.getEndTime().getTime()) {
			boolean exist = nationalDayActivity01QuestionDAO.exist(studentId, questionId);
			if (!exist) {
				nationalDayActivity01QuestionDAO.create(studentId, questionId);
				NationalDayActivity01Stu nda01Stu = nationalDayActivity01StuDAO.get(studentId);
				if (nda01Stu == null) {
					nationalDayActivity01StuDAO.create(studentId, 1);
				} else {
					nationalDayActivity01StuDAO.incrRightCount(studentId, 1);
				}
			}
		}
	}

}
