package com.lanking.uxb.service.homeworkRightRate.api.impl.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkClassService;
import com.lanking.uxb.service.homeworkRightRate.api.TaskHomeworkRightRateStatService;

public class HomeworkRightRateJob implements SimpleJob {
	private static final int CLASS_FETCH_SIZE = 200;
	private static final Logger logger = LoggerFactory.getLogger(HomeworkRightRateJob.class);

	@Autowired
	private StaticHomeworkClassService homeworkClassService;

	@Autowired
	private TaskHomeworkRightRateStatService rightRateStatService;

	@Override
	public void execute(ShardingContext shardingContext) {
		CursorPageable<Long> cursorPageable = CP.cursor(Long.MAX_VALUE, CLASS_FETCH_SIZE);
		CursorPage<Long, HomeworkClazz> cursorPage = homeworkClassService.findEnableClass(cursorPageable);

		// 全量统计近期N段时间的排名情况
		while (CollectionUtils.isNotEmpty(cursorPage.getItems())) {

			for (HomeworkClazz homeworkClazz : cursorPage.getItems()) {
				try {
					rightRateStatService.stat(homeworkClazz.getId());
				} catch (Exception e) {
					logger.error("static class {} has error {}", homeworkClazz.getId(), e);
				}
			}

			Long nextCursor = cursorPage.getNextCursor();
			cursorPageable = CP.cursor(nextCursor, CLASS_FETCH_SIZE);
			cursorPage = homeworkClassService.findEnableClass(cursorPageable);
		}

	}

}
