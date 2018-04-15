package com.lanking.uxb.service.intelligentCorrection.api.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.QuestionAutoCorrectMethod;
import com.lanking.uxb.service.intelligentCorrection.api.IntelligentCorrectionHandle;
import com.lanking.uxb.service.intelligentCorrection.value.CorrectResult;

/**
 * 智能批改链
 * 
 * @since 3.9.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年2月8日
 */
public class IntelligentCorrectionHandleChain {

	private Logger logger = LoggerFactory.getLogger(IntelligentCorrectionHandleChain.class);

	private List<IntelligentCorrectionHandle> handles = Lists.newArrayList();

	public IntelligentCorrectionHandleChain() {
		super();
	}

	public IntelligentCorrectionHandleChain(List<IntelligentCorrectionHandle> handles) {
		super();
		this.handles = handles;
	}

	public IntelligentCorrectionHandleChain addHandle(IntelligentCorrectionHandle handle) {
		handles.add(handle);
		return this;
	}

	Map<Long, CorrectResult> correct(List<Long> queryIds, List<Long> answerIds, List<String> targets,
			List<String> querys) {
		Map<Long, CorrectResult> results = Maps.newHashMap();
		for (IntelligentCorrectionHandle intelligentCorrectionHandle : handles) {
			try {
				intelligentCorrectionHandle.handle(queryIds, answerIds, targets, querys, results);
			} catch (Exception e) {
				logger.error("intelligent correction handle error:", e);
			}
		}
		for (Long queryId : queryIds) {
			if (results.get(queryId) == null) {
				results.put(queryId,
						new CorrectResult(HomeworkAnswerResult.UNKNOW, false, QuestionAutoCorrectMethod.AUTO_CHECK));
			}
		}
		return results;
	}

}
