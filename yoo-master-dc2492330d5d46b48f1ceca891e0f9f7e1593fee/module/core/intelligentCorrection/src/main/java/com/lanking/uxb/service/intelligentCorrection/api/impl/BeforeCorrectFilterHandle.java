package com.lanking.uxb.service.intelligentCorrection.api.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.intelligentCorrection.ex.IntelligentCorrectionException;
import com.lanking.uxb.service.intelligentCorrection.util.AnswerFilterUtil;
import com.lanking.uxb.service.intelligentCorrection.value.CorrectResult;

/**
 * 批改前过滤(过滤tag|检查参数有效性|初始化results)
 * 
 * @since 3.9.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年2月9日
 */
public class BeforeCorrectFilterHandle extends AbstractIntelligentCorrectionHandle {

	private Logger logger = LoggerFactory.getLogger(BeforeCorrectFilterHandle.class);

	@Override
	public void handle(List<Long> queryIds, List<Long> answerIds, List<String> targets, List<String> querys,
			Map<Long, CorrectResult> results) throws IntelligentCorrectionException, IllegalArgException {
		logger.info("intelligent correction[before correct filter] start");
		// 检查参数有效性
		if (queryIds == null || targets == null || querys == null) {
			throw new IllegalArgException();
		}
		int queryIdsSize = queryIds.size();
		int targetSize = targets.size();
		int querySize = querys.size();
		if (queryIdsSize != targetSize || targetSize != querySize || querySize != queryIdsSize || queryIdsSize == 0) {
			throw new IllegalArgException();
		}
		if (answerIds != null) {
			int answerIdSize = answerIds.size();
			if (answerIdSize != queryIdsSize) {
				throw new IllegalArgException();
			}
			// answerIds是否有重复
			Set<Long> answerIdSet = Sets.newHashSet(answerIds);
			answerIdSet.remove(null);
			if (answerIdSet.size() != answerIdSize) {
				throw new IllegalArgException();
			}
		}
		// queryIds是否有重复
		Set<Long> queryIdSet = Sets.newHashSet(queryIds);
		queryIdSet.remove(null);
		if (queryIdSet.size() != queryIdsSize) {
			throw new IllegalArgException();
		}
		if (targets.contains(null)) {
			throw new IllegalArgException();
		}
		// 处理特殊字符
		for (int i = 0; i < queryIdsSize; i++) {
			String target = targets.get(i);
			if (StringUtils.isNotBlank(target)) {
				targets.add(i, AnswerFilterUtil.tagFilter(target));
				targets.remove(i + 1);
			}
			String query = querys.get(i);
			if (StringUtils.isNotBlank(query)) {
				querys.add(i, AnswerFilterUtil.tagFilter(query));
				querys.remove(i + 1);
			}

		}
		logger.info("intelligent correction[before correct filter] end");
	}

}
