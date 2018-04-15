package com.lanking.uxb.service.question.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.baseData.QuestionTag;
import com.lanking.cloud.domain.common.baseData.QuestionTagType;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.question.api.Question2TagTaskService;
import com.lanking.uxb.service.question.api.TaskQuestionTagManage;
import com.lanking.uxb.service.search.api.IndexService;

/**
 * 习题标签相关.
 * 
 * @author wlche
 *
 */
@RestController
@RequestMapping(value = "task/q/tag")
public class TaskQuestionTagController {
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TaskQuestionTagManage taskQuestionTagManage;
	@Autowired
	private IndexService indexService;
	@Autowired
	private Question2TagTaskService question2TagTaskService;

	/**
	 * 初始化标签数据.
	 * 
	 * @return
	 */
	@RequestMapping(value = "initManulTags")
	public Value initManulTags() {
		Date date = new Date();
		int pageNo = 0;
		int pageSize = 500;

		logger.info("[init Tag] --setp1 start !");

		Page<Question> page = taskQuestionTagManage.findQuestionsForInitTag(date, P.offset(pageNo, pageSize));
		while (page.isNotEmpty()) {
			taskQuestionTagManage.handleInitQuestions(page.getItems());

			// 索引处理
			List<Long> questionIds = new ArrayList<Long>(page.getItemSize());
			for (Question question : page.getItems()) {
				questionIds.add(question.getId());
			}
			indexService.add(IndexType.QUESTION, questionIds);

			pageNo += 1;
			page = taskQuestionTagManage.findQuestionsForInitTag(date, P.offset(pageNo * pageSize, pageSize));
		}
		logger.info("[init Tag] --setp1 end !");

		logger.info("[init Tag] --setp2 start !");

		// 典型题的标签
		pageNo = 0;
		pageSize = 200;
		Page<ExaminationPoint> examinationPointPage = taskQuestionTagManage
				.findExaminationPointForInitTag(P.offset(pageNo, pageSize));
		while (examinationPointPage.isNotEmpty()) {
			Set<Long> epQuestionIds = new HashSet<Long>(examinationPointPage.getItemSize());
			for (ExaminationPoint ep : examinationPointPage.getItems()) {
				if (CollectionUtils.isNotEmpty(ep.getQuestions())) {
					epQuestionIds.addAll(ep.getQuestions());
				}
			}
			taskQuestionTagManage.handleInitExaminationPointQuestions(epQuestionIds);

			// 索引处理
			indexService.add(IndexType.QUESTION, epQuestionIds);

			pageNo += 1;
			examinationPointPage = taskQuestionTagManage
					.findExaminationPointForInitTag(P.offset(pageNo * pageSize, pageSize));
		}

		logger.info("[init Tag] --setp2 end !");

		return new Value();
	}

	/**
	 * 初始化标签数据.
	 * 
	 * @return
	 */
	@RequestMapping(value = "initSystemTags")
	public Value initSystemTags() {
		int pageNo = 0;
		int pageSize = 500;

		taskQuestionTagManage.deleteSystemQuestions();

		List<QuestionTag> systemTags = taskQuestionTagManage.listAll(Status.ENABLED, QuestionTagType.SYSTEM);
		for (QuestionTag tag : systemTags) {
			if (tag.getCfg() == null) {
				continue;
			}
			if (tag.getCfg().get("minPulishCount") != null) {
				logger.info("[init Tag] --setp1 start !");

				// 热门题
				pageNo = 0;
				pageSize = 500;
				int minPulishCount = Integer.parseInt(tag.getCfg().get("minPulishCount").toString());
				Page<Long> page = taskQuestionTagManage.findHotQuestionsForInitTag(minPulishCount,
						P.offset(pageNo, pageSize));
				while (page.isNotEmpty()) {
					taskQuestionTagManage.handleInitTagQuestions(page.getItems(), tag.getCode());

					// 索引处理
					indexService.add(IndexType.QUESTION, page.getItems());

					pageNo += 1;
					page = taskQuestionTagManage.findHotQuestionsForInitTag(minPulishCount,
							P.offset(pageNo * pageSize, pageSize));
				}
				logger.info("[init Tag] --setp1 end !");
			} else if (tag.getCfg().get("maxRightRate") != null) {
				logger.info("[init Tag] --setp2 start !");
				// 易错题
				pageNo = 0;
				pageSize = 500;
				int maxRightRate = Integer.parseInt(tag.getCfg().get("maxRightRate").toString());
				int minDoNum = Integer.parseInt(tag.getCfg().get("minDoNum").toString());
				Page<Long> page = taskQuestionTagManage.findFallQuestionsForInitTag(maxRightRate, minDoNum,
						P.offset(pageNo, pageSize));
				while (page.isNotEmpty()) {
					taskQuestionTagManage.handleInitTagQuestions(page.getItems(), tag.getCode());

					// 索引处理
					indexService.add(IndexType.QUESTION, page.getItems());

					pageNo += 1;
					page = taskQuestionTagManage.findFallQuestionsForInitTag(maxRightRate, minDoNum,
							P.offset(pageNo * pageSize, pageSize));
				}

				logger.info("[init Tag] --setp2 end !");
			} else if (tag.getCfg().get("minCollectCount") != null) {
				logger.info("[init Tag] --setp3 start !");
				// 好题
				pageNo = 0;
				pageSize = 500;
				int minCollectCount = Integer.parseInt(tag.getCfg().get("minCollectCount").toString());
				Page<Long> page = taskQuestionTagManage.findGoodQuestionsForInitTag(minCollectCount,
						P.offset(pageNo, pageSize));
				while (page.isNotEmpty()) {
					taskQuestionTagManage.handleInitTagQuestions(page.getItems(), tag.getCode());

					// 索引处理
					indexService.add(IndexType.QUESTION, page.getItems());

					pageNo += 1;
					page = taskQuestionTagManage.findGoodQuestionsForInitTag(minCollectCount,
							P.offset(pageNo * pageSize, pageSize));
				}

				logger.info("[init Tag] --setp3 end !");
			}
		}

		return new Value();
	}

	/**
	 * 手动调用标签任务处理.
	 * 
	 * @return
	 */
	@RequestMapping(value = "taskSystemTags")
	public Value taskSystemTags() {
		try {
			question2TagTaskService.doTask();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Value(e.getMessage());
		}

		return new Value();
	}
}
