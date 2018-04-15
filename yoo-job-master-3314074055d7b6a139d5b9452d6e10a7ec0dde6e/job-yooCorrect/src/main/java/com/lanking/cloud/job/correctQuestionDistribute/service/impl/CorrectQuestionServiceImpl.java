package com.lanking.cloud.job.correctQuestionDistribute.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.job.correctQuestionDistribute.dao.CorrectQuestionDao;
import com.lanking.cloud.job.correctQuestionDistribute.service.CorrectQuestionService;
import com.lanking.cloud.job.correctQuestionDistribute.service.CorrectUserService;
import com.lanking.cloud.job.correctQuestionDistribute.service.CorrectUserStatService;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestion;
import com.lanking.microservice.domain.yoocorrect.CorrectUser;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Description:
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月8日
 * @since 小优秀快批
 */
@Slf4j
@Service
@Transactional("yooCorrectTransactionManager")
public class CorrectQuestionServiceImpl implements CorrectQuestionService {

	@Autowired
	private CorrectQuestionDao correctQuestionDao;
	@Autowired
	private CorrectUserService correctUserService;
	@Autowired
	private CorrectUserStatService correctUserStatService;

	@Override
	public void distributeCorrectQuestion(CorrectQuestion question, CorrectUser correctUser) {
		log.info("[-paiti-][11] uid=" + correctUser.getId() + ",qid=" + question.getId());
		// 更新批改用户池中的状态为不可接收题状态
		correctUserService.distributeCorrectQuestion(question, correctUser.getId());
		correctUserStatService.increaseAllotQuestionCount(correctUser.getId(), question);
		Date firstAllotAt = null;
		if (null == question.getFirstAllotAt()) {
			firstAllotAt = new Date();
		}
		correctQuestionDao.disTributeCorrectQuestionToUser(question, correctUser, firstAllotAt, new Date());
	}

	@Override
	public void sendBackCorrectQuestions(List<Long> correctQuestionIds) {
		correctQuestionDao.sendBackCorrectQuestions(correctQuestionIds);
	}

	@Override
	public void batchSave(Collection<CorrectQuestion> entities) {
		correctQuestionDao.batchSave(entities);

	}

	@Override
	public List<CorrectQuestion> queryCorrectQuestionsToTeacher(int size, List<Long> exqids) {
		return correctQuestionDao.queryCorrectQuestionsToTeacher(size, exqids);
	}

	@Override
	public List<CorrectQuestion> queryCorrectQuestionsToAdmin(int size) {
		return correctQuestionDao.queryCorrectQuestionsToAdmin(size);
	}

	@Override
	public Page<CorrectQuestion> queryCorrectQuestions(Pageable page) {
		return correctQuestionDao.queryCorrectQuestions(page);
	}

	@Override
	public List<CorrectQuestion> queryAbnormalCorrectQuestions() {
		return correctQuestionDao.queryAbnormalCorrectQuestions();
	}

	@Override
	public void clearAbnormalCorrectQuestions(List<Long> questionIds) {
		correctQuestionDao.clearAbnormalCorrectQuestions(questionIds);
		
	}
}
