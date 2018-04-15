package com.lanking.uxb.service.intelligentCorrection.api.impl.job;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.intelligentCorrection.api.AnswerArchiveWrongLibrary2AnswerArchiveService;

public class AnswerArchiveWrongLibrary2AnswerArchiveJob implements SimpleJob {
	private Logger logger = LoggerFactory.getLogger(AnswerArchiveWrongLibrary2AnswerArchiveJob.class);

	@Autowired
	private AnswerArchiveWrongLibrary2AnswerArchiveService aawl2aaService;

	@Override
	public void execute(ShardingContext shardingContext) {
		Long nextAnswerId = Long.MAX_VALUE;
		final int fetchCount = 200;
		List<Long> answerIds = aawl2aaService.queryAnswerId(nextAnswerId, fetchCount);
		while (CollectionUtils.isNotEmpty(answerIds)) {
			try {
				aawl2aaService.pushAnswerArchiveWrongLibrary2AnswerArchive(answerIds);
			} catch (Exception e) {
				logger.error("push AnswerArchiveWrongLibrary 2 AnswerArchive fail:", e);
			}
			answerIds = aawl2aaService.queryAnswerId(answerIds.get(answerIds.size() - 1), fetchCount);
		}
	}

}
