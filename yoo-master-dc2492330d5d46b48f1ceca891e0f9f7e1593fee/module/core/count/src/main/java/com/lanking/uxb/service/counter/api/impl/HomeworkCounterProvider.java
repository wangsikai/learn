package com.lanking.uxb.service.counter.api.impl;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.domain.type.Biz;

@Component
public class HomeworkCounterProvider extends AbstractBizCounterProvider {

	@Override
	public Biz getBiz() {
		return Biz.HOMEWORK;
	}

	@Override
	public Biz getOtherBiz() {
		return null;
	}

	/**
	 * 记录作业习题数量
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param c
	 *            数量
	 */
	@Deprecated
	private void incrQuestionCount(long homeworkId, int c) {
		this.counter(homeworkId, Count.COUNTER_1, c);
	}

	/**
	 * 获取作业里面的习题数量
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @return 习题数量
	 */
	@Deprecated
	private long getQuestionCount(long homeworkId) {
		return this.getCount(homeworkId, Count.COUNTER_1).longValue();
	}

	/**
	 * 批量获取作业的习题数量
	 * 
	 * @param homeworkIds
	 *            作业IDs
	 * @return 数量集合
	 */
	@Deprecated
	private Map<Long, Long> mgetQuestionCount(Set<Long> homeworkIds) {
		Map<Long, Long> questionCounts = Maps.newHashMap();
		Map<Long, Counter> counterMap = this.getCounters(homeworkIds);
		for (Long homeworkId : homeworkIds) {
			Counter counter = counterMap.get(homeworkId);
			if (counter == null) {
				questionCounts.put(homeworkId, 0L);
			} else {
				questionCounts.put(homeworkId, counter.getCount1());
			}
		}
		return questionCounts;
	}

	/**
	 * 操作评价为简单的人数
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param c
	 *            计数
	 */
	public void incrEvaluationEasy(long homeworkId, int c) {
		this.counter(homeworkId, Count.COUNTER_2, c);
	}

	/**
	 * 操作评价为普通的人数
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param c
	 *            计数
	 */
	public void incrEvaluationOrdinary(long homeworkId, int c) {
		this.counter(homeworkId, Count.COUNTER_3, c);
	}

	/**
	 * 操作评价为困难的人数
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param c
	 *            计数
	 */
	public void incrEvaluationDifficult(long homeworkId, int c) {
		this.counter(homeworkId, Count.COUNTER_4, c);
	}

}
