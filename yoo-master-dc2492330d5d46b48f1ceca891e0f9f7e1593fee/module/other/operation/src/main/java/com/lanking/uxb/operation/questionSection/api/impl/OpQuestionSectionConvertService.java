package com.lanking.uxb.operation.questionSection.api.impl;

import httl.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.uxb.operation.questionSection.api.OpQuestionSectionService;
import com.lanking.uxb.operation.questionSection.api.OpQuestionService;
import com.lanking.uxb.operation.questionSection.cache.OpQuestionSectionConvertCacheService;

import java.util.List;

@Service
public class OpQuestionSectionConvertService {
	private Logger logger = LoggerFactory.getLogger(OpQuestionSectionConvertService.class);
	@Autowired
	private OpQuestionSectionService questionSectionService;
	@Autowired
	private OpQuestionService questionService;
	@Autowired
	private OpQuestionSectionConvertCacheService questionSectionConvertCacheService;

	private static final int FETCH_SIZE = 100;

	/**
	 * 全量进行重新处理
	 *
	 * @param textbookCode
	 *            教材Code
	 * @param convertAll
	 *            是否转换全部，若为true 表示全量重新统计，若为false表示增量统计
	 */
	@Async
	public void convert(int textbookCode, boolean convertAll) {
		Long cursor = 0L;
		if (convertAll) {
			cursor = Long.MAX_VALUE;
		} else {
			String val = questionSectionConvertCacheService.get(textbookCode);
			if (StringUtils.isBlank(val)) {
				cursor = Long.MAX_VALUE;
			} else {
				cursor = Long.valueOf(val);
			}
		}
		CursorPageable<Long> cursorPageable = CP.cursor(cursor, FETCH_SIZE);
		CursorPage<Long, Long> cp = questionService.query(textbookCode, cursorPageable);
		while (cp.isNotEmpty()) {
			try {
				List<Long> questionIds = cp.getItems();
				// 进行转换处理
				questionSectionService.convert(textbookCode, questionIds);

				questionSectionConvertCacheService.set(textbookCode, cp.getLast());
				cp = questionService.query(textbookCode, CP.cursor(cp.getLast(), FETCH_SIZE));
			} catch (Exception e) {
				logger.error("convert question section error: {}", e);
			}
		}
		questionSectionConvertCacheService.set(textbookCode, -1L);
		questionSectionConvertCacheService.incrConvertingCount(-1);
		//questionSectionConvertCacheService.expire(textbookCode);
	}
}
