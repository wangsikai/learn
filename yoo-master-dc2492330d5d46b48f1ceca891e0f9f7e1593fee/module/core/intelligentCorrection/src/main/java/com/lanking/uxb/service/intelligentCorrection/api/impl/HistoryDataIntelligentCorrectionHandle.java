package com.lanking.uxb.service.intelligentCorrection.api.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.QuestionAutoCorrectMethod;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.intelligentCorrection.api.AnswerArchiveService;
import com.lanking.uxb.service.intelligentCorrection.ex.IntelligentCorrectionException;
import com.lanking.uxb.service.intelligentCorrection.value.CorrectResult;

/**
 * 通过海量历史数据进行批改
 * 
 * @since 3.9.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年2月9日
 */
public class HistoryDataIntelligentCorrectionHandle extends AbstractIntelligentCorrectionHandle {

	private Logger logger = LoggerFactory.getLogger(HistoryDataIntelligentCorrectionHandle.class);

	private AnswerArchiveService answerArchiveService;

	@Override
	public void handle(List<Long> queryIds, List<Long> answerIds, List<String> targets, List<String> querys,
			Map<Long, CorrectResult> results) throws IntelligentCorrectionException, IllegalArgException {
		logger.info("intelligent correction[history data] start");
		if (CollectionUtils.isNotEmpty(answerIds)) {
			int querySize = queryIds.size();
			for (int i = 0; i < querySize; i++) {
				long answerId = answerIds.get(i);
				String query = querys.get(i);
				HomeworkAnswerResult result = answerArchiveService.getArchiveResult(answerId, query);
				if (result != null) {
					results.put(queryIds.get(i),
							new CorrectResult(result, true, QuestionAutoCorrectMethod.CONTRAST_HISTORY));
				}
			}
		}
		logger.info("intelligent correction[history data] end");
	}

	public AnswerArchiveService getAnswerArchiveService() {
		return answerArchiveService;
	}

	public void setAnswerArchiveService(AnswerArchiveService answerArchiveService) {
		this.answerArchiveService = answerArchiveService;
	}

}
