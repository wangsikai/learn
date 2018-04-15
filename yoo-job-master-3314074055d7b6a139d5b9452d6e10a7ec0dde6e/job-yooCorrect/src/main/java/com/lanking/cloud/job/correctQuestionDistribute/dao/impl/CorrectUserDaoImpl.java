package com.lanking.cloud.job.correctQuestionDistribute.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.job.correctQuestionDistribute.dao.CorrectUserDao;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestion;
import com.lanking.microservice.domain.yoocorrect.CorrectUser;
import com.lanking.microservice.domain.yoocorrect.CorrectUserPool;
import com.lanking.microservice.domain.yoocorrect.CorrectUserType;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月8日
 * @since 小优秀快批
 */
@Component
public class CorrectUserDaoImpl implements CorrectUserDao {

	@Autowired
	@Qualifier("CorrectUserPoolRepo")
	private Repo<CorrectUserPool, Long> correctUserPoolRepo;

	@Autowired
	@Qualifier("CorrectUserRepo")
	private Repo<CorrectUser, Long> correctUserRepo;

	@Override
	public List<CorrectUser> queryWaitToCoorecUsersByType(CorrectUserType type) {
		Params params = Params.param();
		if (type != null) {
			params = params.put("correctUserType", type.getValue());
		}
		return correctUserRepo.find("$queryWaitToCoorecUsersByType", params).list();
	}

	@Override
	public void distributeCorrectQuestion(CorrectQuestion question, Long correctUsreId) {
		Params params = Params.param("correctQuestionId", question.getId());
		params.put("correctUserId", correctUsreId);
		params.put("startAt", new Date());
		params.put("questionType", question.getType().getValue());
		correctUserPoolRepo.execute("$distributeCorrectQuestionToUser", params);
	}

	@Override
	public List<CorrectUserPool> findHasDistributedUsers() {
		return correctUserPoolRepo.find("$findHasDistributedUsers").list();
	}

	@Override
	public void deleteTimeOutCorrectUserPools(List<Long> correctUserPoolIds) {
		correctUserPoolRepo.execute("$deleteTimeOutCorrectUserPools",
				Params.param("correctUserPoolIds", correctUserPoolIds));
	}

	@Override
	public List<Long> findAllCorrectUserIds() {
		return correctUserRepo.find("$findAllCorrectUserIds").list(Long.class);
	}

	@Override
	public void updateTrustRank(Long id, Integer trustRank) {
		CorrectUser user = correctUserRepo.get(id);
		if(null != user){
			user.setTrustRank(user.getTrustRank()+trustRank);
		}
		correctUserRepo.save(user);
		
	}
}
