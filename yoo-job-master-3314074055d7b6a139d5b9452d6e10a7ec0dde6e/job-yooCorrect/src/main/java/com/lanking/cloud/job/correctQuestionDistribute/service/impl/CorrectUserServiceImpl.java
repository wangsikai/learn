package com.lanking.cloud.job.correctQuestionDistribute.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.job.correctQuestionDistribute.dao.CorrectUserDao;
import com.lanking.cloud.job.correctQuestionDistribute.service.CorrectUserService;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestion;
import com.lanking.microservice.domain.yoocorrect.CorrectUser;
import com.lanking.microservice.domain.yoocorrect.CorrectUserPool;
import com.lanking.microservice.domain.yoocorrect.CorrectUserType;

/**
 * <p>
 * Description:
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月8日
 * @since 小优秀快批
 */
@Service
@Transactional("yooCorrectTransactionManager")
public class CorrectUserServiceImpl implements CorrectUserService {

	@Autowired
	private CorrectUserDao correctUserDao;

	@Override
	public List<CorrectUser> queryWaitToCoorecUsersByType(CorrectUserType type) {
		return correctUserDao.queryWaitToCoorecUsersByType(type);
	}

	@Override
	public void distributeCorrectQuestion(CorrectQuestion question, Long correctUsreId) {
		correctUserDao.distributeCorrectQuestion(question, correctUsreId);
	}

	@Override
	public List<CorrectUserPool> findHasDistributedUsers() {
		return correctUserDao.findHasDistributedUsers();
	}

	@Override
	public void deleteTimeOutCorrectUserPools(List<Long> correctUserPoolIds) {
		correctUserDao.deleteTimeOutCorrectUserPools(correctUserPoolIds);		
	}

	@Override
	public List<Long> findAllCorrectUserIds(){
		return correctUserDao.findAllCorrectUserIds();
	}

	@Override
	public void updateTrustRank(Long id, Integer trustRank) {
		correctUserDao.updateTrustRank(id, trustRank);
	}

}
