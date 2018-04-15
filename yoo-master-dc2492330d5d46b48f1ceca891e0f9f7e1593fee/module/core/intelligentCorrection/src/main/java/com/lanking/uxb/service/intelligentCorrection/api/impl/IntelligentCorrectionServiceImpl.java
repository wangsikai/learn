package com.lanking.uxb.service.intelligentCorrection.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.uxb.service.intelligentCorrection.api.IntelligentCorrectionService;
import com.lanking.uxb.service.intelligentCorrection.ex.IntelligentCorrectionException;
import com.lanking.uxb.service.intelligentCorrection.value.CorrectResult;

@Transactional(readOnly = true)
@Service
public class IntelligentCorrectionServiceImpl implements IntelligentCorrectionService {

	private Logger logger = LoggerFactory.getLogger(IntelligentCorrectionServiceImpl.class);

	@Autowired(required = false)
	private IntelligentCorrectionHandleChain chain;

	@Override
	public CorrectResult correct(Long queryId, Long answerId, String target, String query)
			throws IntelligentCorrectionException, IllegalArgException {
		logger.info("convert single to batch");
		List<Long> queryIds = new ArrayList<Long>(1);
		queryIds.add(queryId);
		List<Long> answerIds = null;
		if (answerId != null) {
			answerIds = new ArrayList<Long>(1);
			answerIds.add(answerId);
		}
		List<String> targets = new ArrayList<String>(1);
		targets.add(target);
		List<String> querys = new ArrayList<String>(1);
		querys.add(query);
		return chain.correct(queryIds, answerIds, targets, querys).get(queryId);
	}

	@Override
	public Map<Long, CorrectResult> correct(List<Long> queryIds, List<Long> answerIds, List<String> targets,
			List<String> querys) throws IntelligentCorrectionException, IllegalArgException, IllegalArgException {
		return chain.correct(queryIds, answerIds, targets, querys);
	}

}
