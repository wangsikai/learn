package com.lanking.uxb.service.intelligentCorrection.api.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.QuestionAutoCorrectMethod;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.intelligentCorrection.ex.IntelligentCorrectionException;
import com.lanking.uxb.service.intelligentCorrection.value.CorrectResult;

/**
 * 简单文本比较
 * 
 * @since 3.9.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年2月9日
 */
public class TextComparisonIntelligentCorrectionHandle extends AbstractIntelligentCorrectionHandle {

	private Logger logger = LoggerFactory.getLogger(TextComparisonIntelligentCorrectionHandle.class);

	@Override
	public void handle(List<Long> queryIds, List<Long> answerIds, List<String> targets, List<String> querys,
			Map<Long, CorrectResult> results) throws IntelligentCorrectionException, IllegalArgException {
		logger.info("intelligent correction[text comparison] start");
		int querySize = queryIds.size();
		for (int i = 0; i < querySize; i++) {
			String query = querys.get(i);
			if (StringUtils.isBlank(query)) {
				results.put(queryIds.get(i),
						new CorrectResult(HomeworkAnswerResult.WRONG, true, QuestionAutoCorrectMethod.AUTO_CHECK));
				continue;
			}
			String target = targets.get(i);
			if (target.equals(query)) {
				results.put(queryIds.get(i),
						new CorrectResult(HomeworkAnswerResult.RIGHT, true, QuestionAutoCorrectMethod.AUTO_CHECK));
				continue;
			}
		}
		logger.info("intelligent correction[text comparison] end");
	}

}
