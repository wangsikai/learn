package com.lanking.uxb.service.homeworkRightRate.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkClassService;
import com.lanking.uxb.service.homeworkRightRate.api.TaskHomeworkRightRateStatService;
import com.lanking.uxb.service.lottery.api.TaskLotterySeasonService;

@RestController
@RequestMapping(value = "task/s/rightRate")
public class HomeworkRightRateStatController {
	@Autowired
	private TaskHomeworkRightRateStatService statService;
	@Autowired
	private TaskLotterySeasonService lotterySeasonService;

	private static final int CLASS_FETCH_SIZE = 200;
	private static final Logger logger = LoggerFactory.getLogger(HomeworkRightRateStatController.class);

	@Autowired
	private StaticHomeworkClassService homeworkClassService;

	@Autowired
	private TaskHomeworkRightRateStatService rightRateStatService;

	@RequestMapping(value = "data", method = { RequestMethod.GET, RequestMethod.POST })
	public Value data(Long classId) {
		statService.stat(classId);
		return new Value();
	}

	@RequestMapping(value = "test", method = { RequestMethod.GET, RequestMethod.POST })
	public Value test() {
		List<CoinsLotterySeason> list = lotterySeasonService.findNewList();
		for (CoinsLotterySeason season : list) {
			// 表明上一期已经结束，需要生成下一期，并将些期改为结束状态
			if (season != null && season.getEveryWeek() && season.getEndTime().getTime() <= System.currentTimeMillis()
					&& season.getStatus() == Status.ENABLED) {
				lotterySeasonService.save(season);
			}
		}
		return new Value();
	}

	@RequestMapping(value = "dataAll", method = { RequestMethod.GET, RequestMethod.POST })
	public Value dataAll() {
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
		return new Value();
	}

}
