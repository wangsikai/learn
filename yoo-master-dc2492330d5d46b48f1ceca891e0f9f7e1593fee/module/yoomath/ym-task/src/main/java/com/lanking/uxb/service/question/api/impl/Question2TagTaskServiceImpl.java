package com.lanking.uxb.service.question.api.impl;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.common.baseData.QuestionTag;
import com.lanking.cloud.domain.common.baseData.QuestionTagType;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.uxb.service.question.api.Question2TagTaskService;
import com.lanking.uxb.service.question.api.TaskQuestionTagManage;
import com.lanking.uxb.service.search.api.IndexService;

@Service
public class Question2TagTaskServiceImpl implements Question2TagTaskService {
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	@Autowired
	private TaskQuestionTagManage questionTagManage;
	@Autowired
	private IndexService indexService;

	@Override
	public void doTask() {
		List<QuestionTag> tags = questionTagManage.listAll(Status.ENABLED, QuestionTagType.SYSTEM);
		int pageSize = 200;
		int pageNo = 0;

		for (QuestionTag tag : tags) {
			if (tag.getCfg() == null) {
				continue;
			}
			if (tag.getCfg().get("minPulishCount") != null) {
				// 1、热门标签处理
				long t1 = System.currentTimeMillis();

				// 1)、去掉热门标签的习题
				int minPulishCount = Integer.parseInt(tag.getCfg().get("minPulishCount").toString());
				Page<Long> page = questionTagManage.findHotQuestionsForDEL(minPulishCount, P.offset(0, pageSize));
				questionTagManage.handleTaskTagQuestions(page.getItems(), null, tag.getCode());
				indexService.add(IndexType.QUESTION, page.getItems());
				while (page.isNotEmpty()) {
					page = questionTagManage.findHotQuestionsForDEL(minPulishCount, P.offset(0, pageSize));
					questionTagManage.handleTaskTagQuestions(page.getItems(), null, tag.getCode());
					indexService.add(IndexType.QUESTION, page.getItems());
				}

				long t2 = System.currentTimeMillis();
				logger.info("[TASK -> question-tag] 热门标签删除处理完成，耗时：" + (t2 - t1) / 1000);

				// 2)、新增热门标签的习题
				pageNo = 0;
				page = questionTagManage.findHotQuestionsForADD(minPulishCount, P.offset(pageNo, pageSize));
				questionTagManage.handleTaskTagQuestions(null, page.getItems(), tag.getCode());
				indexService.add(IndexType.QUESTION, page.getItems());
				while (page.isNotEmpty()) {
					pageNo += 1;
					page = questionTagManage.findHotQuestionsForADD(minPulishCount,
							P.offset(pageNo * pageSize, pageSize));
					questionTagManage.handleTaskTagQuestions(null, page.getItems(), tag.getCode());
					indexService.add(IndexType.QUESTION, page.getItems());
				}

				long t3 = System.currentTimeMillis();
				logger.info("[TASK -> question-tag] 热门标签新增处理完成，耗时：" + (t3 - t2) / 1000);
			} else if (tag.getCfg().get("maxRightRate") != null && tag.getCfg().get("minDoNum") != null) {
				// 2、易错题标签处理
				long t1 = System.currentTimeMillis();

				// 1)、去掉易错标签的习题
				int maxRightRate = Integer.parseInt(tag.getCfg().get("maxRightRate").toString());
				int minDoNum = Integer.parseInt(tag.getCfg().get("minDoNum").toString());
				Page<Long> page = questionTagManage.findFallQuestionsForDEL(maxRightRate, minDoNum,
						P.offset(0, pageSize));
				questionTagManage.handleTaskTagQuestions(page.getItems(), null, tag.getCode());
				indexService.add(IndexType.QUESTION, page.getItems());
				while (page.isNotEmpty()) {
					page = questionTagManage.findFallQuestionsForDEL(maxRightRate, minDoNum, P.offset(0, pageSize));
					questionTagManage.handleTaskTagQuestions(page.getItems(), null, tag.getCode());
					indexService.add(IndexType.QUESTION, page.getItems());
				}

				long t2 = System.currentTimeMillis();
				logger.info("[TASK -> question-tag] 易错标签删除处理完成，耗时：" + (t2 - t1) / 1000);

				// 2)、新增易错标签的习题
				pageNo = 0;
				page = questionTagManage.findFallQuestionsForADD(maxRightRate, minDoNum, P.offset(pageNo, pageSize));
				questionTagManage.handleTaskTagQuestions(null, page.getItems(), tag.getCode());
				indexService.add(IndexType.QUESTION, page.getItems());
				while (page.isNotEmpty()) {
					pageNo += 1;
					page = questionTagManage.findFallQuestionsForADD(maxRightRate, minDoNum,
							P.offset(pageNo * pageSize, pageSize));
					questionTagManage.handleTaskTagQuestions(null, page.getItems(), tag.getCode());
					indexService.add(IndexType.QUESTION, page.getItems());
				}

				long t3 = System.currentTimeMillis();
				logger.info("[TASK -> question-tag] 易错标签新增处理完成，耗时：" + (t3 - t2) / 1000);
			} else if (tag.getCfg().get("minCollectCount") != null) {
				// 3、好题标签处理
				long t1 = System.currentTimeMillis();

				// 1)、去掉好题标签的习题
				int minCollectCount = Integer.parseInt(tag.getCfg().get("minCollectCount").toString());
				Page<Long> page = questionTagManage.findGoodQuestionsForDEL(minCollectCount, P.offset(0, pageSize));
				questionTagManage.handleTaskTagQuestions(page.getItems(), null, tag.getCode());
				indexService.add(IndexType.QUESTION, page.getItems());
				while (page.isNotEmpty()) {
					page = questionTagManage.findGoodQuestionsForDEL(minCollectCount, P.offset(0, pageSize));
					questionTagManage.handleTaskTagQuestions(page.getItems(), null, tag.getCode());
					indexService.add(IndexType.QUESTION, page.getItems());
				}

				long t2 = System.currentTimeMillis();
				logger.info("[TASK -> question-tag] 好题标签删除处理完成，耗时：" + (t2 - t1) / 1000);

				// 2)、新增好题标签的习题
				page = questionTagManage.findGoodQuestionsForADD(minCollectCount, P.offset(pageNo, pageSize));
				questionTagManage.handleTaskTagQuestions(null, page.getItems(), tag.getCode());
				indexService.add(IndexType.QUESTION, page.getItems());
				while (page.isNotEmpty()) {
					pageNo += 1;
					page = questionTagManage.findGoodQuestionsForADD(minCollectCount,
							P.offset(pageNo * pageSize, pageSize));
					questionTagManage.handleTaskTagQuestions(null, page.getItems(), tag.getCode());
					indexService.add(IndexType.QUESTION, page.getItems());
				}

				long t3 = System.currentTimeMillis();
				logger.info("[TASK -> question-tag] 好题标签新增处理完成，耗时：" + (t3 - t2) / 1000);
			}
		}
	}
}
