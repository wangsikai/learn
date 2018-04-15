package com.lanking.uxb.service.counter.api.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.counter.CounterDetail;
import com.lanking.cloud.domain.type.Biz;

/**
 * 针对题目的相关计数detail
 * 
 * @author wangsenhao
 * @version 2017年2月14日
 */
@Component
public class QuestionUserCouterProvider extends AbstractBizCounterProvider {

	@Override
	public Biz getBiz() {
		return Biz.QUESTION;
	}

	@Override
	public Biz getOtherBiz() {
		return Biz.USER;
	}

	/**
	 * 获取当前用户题目布置次数(老师)
	 * 
	 * @param userId
	 * @param c
	 */
	public long getQuestionPublishCount(long questionId, long userId) {
		CounterDetail counterDetail = this.getCounterDetail(questionId, userId);
		return counterDetail == null ? 0 : counterDetail.getCount1();
	}

	/**
	 * 获取当前用户题目布置次数(老师)
	 * 
	 * @param userId
	 * @param c
	 */
	public Map<Long, Long> mgetQuestionPublishCount(Collection<Long> questionIds, long userId) {
		Map<Long, CounterDetail> conterDetails = this.getCounterDetailsByBizId(questionIds, userId);
		Map<Long, Long> returnMap = new HashMap<Long, Long>(conterDetails.size());
		for (Long qid : questionIds) {
			returnMap.put(qid, conterDetails.get(qid) == null ? 0 : conterDetails.get(qid).getCount1());
		}
		return returnMap;
	}

	/**
	 * 获取当前用户题目布置次数(学生)
	 * 
	 * @param userId
	 * @param c
	 */
	public long getQuestionExerciseCount(long questionId, long userId) {
		CounterDetail counterDetail = this.getCounterDetail(questionId, userId);
		return counterDetail == null ? 0 : counterDetail.getCount1();
	}
}
