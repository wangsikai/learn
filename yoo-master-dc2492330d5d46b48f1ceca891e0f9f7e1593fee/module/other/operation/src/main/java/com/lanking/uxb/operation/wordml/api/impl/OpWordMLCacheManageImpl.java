package com.lanking.uxb.operation.wordml.api.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionWordMLData;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.uxb.operation.wordml.api.OpWordMLQuestionManage;
import com.lanking.uxb.operation.wordml.api.OpWordMLCacheManage;
import com.lanking.uxb.operation.wordml.controller.OpWordMLCacheController;
import com.lanking.uxb.service.question.api.AnswerService;
import com.lanking.uxb.service.question.api.QuestionWordMLService;
import com.lanking.uxb.service.question.cache.word.QuestionWordMLCacheService;

/**
 * Word缓存控制接口实现.<br>
 * 此类上勿加事务，直接加在方法上
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月5日
 */
@Service
public class OpWordMLCacheManageImpl implements OpWordMLCacheManage {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AnswerService answerService;
	@Autowired
	private QuestionWordMLService questionWordMLService;
	@Autowired
	private QuestionWordMLCacheService questionWordMLCacheService;
	@Autowired
	private OpWordMLQuestionManage questionManage;

	@Override
	@Async
	public void rebuildQuestionWordML(int type, String host, Long minId) {
		int pageSize = 80;
		int page = 1;
		int count = 0;
		try {
			// 习题表搜索
			if (type == 0) {
				Page<Question> p = questionManage.wordMLQueryByPage(type, minId, P.index(page, pageSize));
				if (p.getItemSize() > 0) {
					logger.info("重建索引：" + count + "/" + p.getTotalCount());
					if (OpWordMLCacheController.stop) {
						OpWordMLCacheController.stop = false;
						OpWordMLCacheController.building = false;
						return;
					}
					Set<Long> questionIds = new HashSet<Long>(p.getItemSize());
					for (Question question : p.getItems()) {
						questionIds.add(question.getId());
					}
					Map<Long, List<Answer>> answers = answerService.getQuestionAnswers(questionIds);
					this.rebuild(p.getItems(), answers, host);
					count += p.getItemSize();
					logger.info("重建索引：习题个数=" + count + "/" + p.getTotalCount());
				}
				while (p.isNotEmpty()) {
					if (OpWordMLCacheController.stop) {
						OpWordMLCacheController.stop = false;
						OpWordMLCacheController.building = false;
						return;
					}
					page++;
					p = questionManage.wordMLQueryByPage(type, minId, P.index(page, pageSize));
					if (p.getItemSize() > 0) {
						logger.info("重建索引：" + count + "/" + p.getTotalCount());
						Set<Long> questionIds = new HashSet<Long>(p.getItemSize());
						for (Question question : p.getItems()) {
							questionIds.add(question.getId());
						}
						Map<Long, List<Answer>> answers = answerService.getQuestionAnswers(questionIds);
						this.rebuild(p.getItems(), answers, host);
						count += p.getItemSize();
						logger.info("重建索引：习题个数=" + count + "/" + p.getTotalCount());
					}
				}
			} else if (type == 1) {
				// 缓存表搜索
				pageSize = 200;
				page = 1;
				Page<QuestionWordMLData> mp = questionWordMLService.queryAllFromTable(P.index(page, pageSize));
				if (mp.getItemSize() > 0) {
					questionWordMLCacheService.mutilSet(mp.getItems());
				}
				while (mp.isNotEmpty()) {
					if (OpWordMLCacheController.stop) {
						OpWordMLCacheController.stop = false;
						OpWordMLCacheController.building = false;
						return;
					}
					page++;
					mp = questionWordMLService.queryAllFromTable(P.index(page, pageSize));
					if (mp.getItemSize() > 0) {
						questionWordMLCacheService.mutilSet(mp.getItems());
					}
					logger.info("重建索引（缓存表）：" + page + "/" + mp.getPageCount());
				}
			}
		} catch (Exception e) {
			logger.error("重建习题WordML缓存出错！", e);
		}
		OpWordMLCacheController.stop = false;
		OpWordMLCacheController.building = false;
	}

	private void rebuild(List<Question> questions, Map<Long, List<Answer>> answers, String host) {
		questionWordMLService.batchAdd(questions, answers, host);
	}
}
