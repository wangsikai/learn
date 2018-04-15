package com.lanking.cloud.job.correctQuestionDistribute.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.job.correctQuestionDistribute.service.CorrectUserStatService;
import com.lanking.cloud.job.correctUserDayStat.DAO.CorrectUserStatDAO;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestion;
import com.lanking.microservice.domain.yoocorrect.CorrectUserStat;

/**
 * <p>
 * Description:批改用户统计服务
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月19日
 * @since 小优秀快批
 */
@Service
public class CorrectUserStatServiceImpl implements CorrectUserStatService {
	@Qualifier("StatCorrectUserStatDAO")
	@Autowired
	private CorrectUserStatDAO correctUserStatDAO;

	@Override
	@Transactional("yooCorrectTransactionManager")
	public void increaseAllotQuestionCount(Long correctUserId,CorrectQuestion question) {
		if(null == question){
			return;
		}
		// 以前分配过的批改人
		List<Long> historyCorrectUserList = question.getCorrectUserIds();
		//该题目如果之前分配给该用户，则不执行下面的加1操作
		if(null != historyCorrectUserList && historyCorrectUserList.contains(correctUserId)){
			return;
		}
		CorrectUserStat correctUserStat = correctUserStatDAO.get(correctUserId);
		if(null != correctUserStat){
			Long allotQuestionCount = correctUserStat.getAllotQuestionCount();
			if(null == allotQuestionCount){
				allotQuestionCount = 0l;
			}
			correctUserStat.setAllotQuestionCount(allotQuestionCount+1);
			correctUserStatDAO.save(correctUserStat);
		}
	}

}
