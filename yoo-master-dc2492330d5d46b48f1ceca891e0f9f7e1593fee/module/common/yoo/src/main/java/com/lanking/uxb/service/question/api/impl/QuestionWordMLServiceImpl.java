package com.lanking.uxb.service.question.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionWordMLData;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.question.api.QuestionWordMLService;
import com.lanking.uxb.service.question.cache.word.QuestionWordMLCacheService;
import com.lanking.uxb.service.question.util.QuestionWordMLPretreat;

@Service
public class QuestionWordMLServiceImpl implements QuestionWordMLService {
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("QuestionWordMLDataRepo")
	private Repo<QuestionWordMLData, Long> repo;

	@Autowired
	private WordResourceInitService wordResourceInitService;
	@Autowired
	private QuestionWordMLCacheService questionWordMLCacheService;
	@Autowired
	private QuestionWordMLBatchAddService questionWordMLBatchAddService;
	@Autowired
	private HttpClient httpClient;
	private boolean run = Env.getBoolean("rescon.question.transWordML");

	@Override
	@Async
	@Transactional
	public void asyncAdd(Question question, List<Answer> answers, String host) {
		if (run) {
			try {
				QuestionWordMLPretreat pretreat = new QuestionWordMLPretreat(host,
						wordResourceInitService.getStreamSource(), wordResourceInitService.getImageTemplate(),
						httpClient, wordResourceInitService.getFileService());
				QuestionWordMLData questionWordMLData = pretreat.getQuestionContenMLDatas(question, answers);
				repo.execute("delete from question_wordml where id=" + question.getId());
				repo.save(questionWordMLData);
				questionWordMLCacheService.set(questionWordMLData);
			} catch (Exception e) {
				logger.error("习题WordML预处理出错！", e);
			}
		}
	}

	@Override
	public void batchAdd(List<Question> questions, Map<Long, List<Answer>> answers, String host) {
		if (run) {
			try {
				List<QuestionWordMLData> questionWordMLDatas = new ArrayList<QuestionWordMLData>(questions.size());
				Set<Long> qids = new HashSet<Long>(questions.size());
				for (int i = 0; i < questions.size(); i++) {
					Question q = questions.get(i);
					qids.add(q.getId());
					QuestionWordMLPretreat pretreat = new QuestionWordMLPretreat(host,
							wordResourceInitService.getStreamSource(), wordResourceInitService.getImageTemplate(),
							httpClient, wordResourceInitService.getFileService());
					QuestionWordMLData questionWordMLData = pretreat.getQuestionContenMLDatas(q,
							answers.get(q.getId()));
					if (questionWordMLData != null) {
						questionWordMLDatas.add(questionWordMLData);
					}
				}
				questionWordMLBatchAddService.batchAddTransaction(qids, questionWordMLDatas);
			} catch (Exception e) {
				logger.error("习题WordML预处理出错！", e);
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public QuestionWordMLData get(long id) {
		if (run) {
			QuestionWordMLData data = questionWordMLCacheService.get(id);
			if (data == null) {
				data = repo.get(id);
				questionWordMLCacheService.set(data);
			}
			return data;
		} else {
			return null;
		}
	}

	@Transactional(readOnly = true)
	public Map<Long, QuestionWordMLData> mget(Collection<Long> ids) {
		if (run) {
			Map<Long, QuestionWordMLData> questionWordMLDatas = questionWordMLCacheService.mget(ids);
			Set<Long> nullIds = new HashSet<Long>();
			for (Long id : ids) {
				if (questionWordMLDatas.get(id) == null) {
					nullIds.add(id);
				}
			}
			if (nullIds.size() > 0) {
				Map<Long, QuestionWordMLData> nullQuestionWordMLDatas = repo.mget(nullIds);
				questionWordMLDatas.putAll(nullQuestionWordMLDatas);
				for (QuestionWordMLData data : nullQuestionWordMLDatas.values()) {
					questionWordMLCacheService.set(data);
				}
			}
			return questionWordMLDatas;
		} else {
			return Maps.newHashMap();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<QuestionWordMLData> queryAllFromTable(Pageable pageable) {
		return repo.find("$queryAllQuestionWordMLDatas", Params.param()).fetch(pageable);
	}
}
