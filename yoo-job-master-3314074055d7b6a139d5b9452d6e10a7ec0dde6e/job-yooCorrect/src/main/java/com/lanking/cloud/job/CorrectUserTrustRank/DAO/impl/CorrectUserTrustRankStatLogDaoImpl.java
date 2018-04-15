package com.lanking.cloud.job.CorrectUserTrustRank.DAO.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.job.CorrectUserTrustRank.DAO.CorrectUserTrustRankStatLogDao;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.microservice.domain.yoocorrect.TrustRankStatLog;

/**
 * <p>Title:</p>
 * <p>Description:<p>
 * @author pengcheng.yu
 * @date 2018年3月26日
 * @since 小优秀快批
 */
@Component
public class CorrectUserTrustRankStatLogDaoImpl implements CorrectUserTrustRankStatLogDao {

	@Autowired
	@Qualifier("TrustRankStatLogRepo")
	private Repo<TrustRankStatLog,Long> repo;
	@Override
	public TrustRankStatLog add(TrustRankStatLog trustRankStatLog) {
		return repo.save(trustRankStatLog);
	}
	@Override
	public TrustRankStatLog get(Long id) {
		return repo.get(id);
	}
	@Override
	public TrustRankStatLog getByCorrectUserId(Long correctUserId) {
		return repo.find("$findByCorrectUserId", Params.param("correctUserId", correctUserId)).get();
	}
	@Override
	public TrustRankStatLog update(Long id,Integer curQuestionCount, Integer curErrorCount) {
		TrustRankStatLog trustRankStatLog = get(id);
		if(null != trustRankStatLog){
			int oldQueryOffset = trustRankStatLog.getQueryOffset();
			int oldTotalErrCount = trustRankStatLog.getTotalErrorCount();
			int oldCurErrorCount = trustRankStatLog.getCurErrorCount();
			
			trustRankStatLog.setQueryOffset(oldQueryOffset+curQuestionCount);
			trustRankStatLog.setTotalErrorCount(oldTotalErrCount+oldCurErrorCount);
			trustRankStatLog.setCurErrorCount(curErrorCount);
			
			trustRankStatLog = repo.save(trustRankStatLog);
		}
		return trustRankStatLog;
	}
	@Override
	public void clear(Long id) {
		TrustRankStatLog trustRankStatLog = get(id);
		if(null != trustRankStatLog){
			trustRankStatLog.setTotalErrorCount(0);
			trustRankStatLog.setCurErrorCount(0);
			repo.save(trustRankStatLog);
		}
	}

}
