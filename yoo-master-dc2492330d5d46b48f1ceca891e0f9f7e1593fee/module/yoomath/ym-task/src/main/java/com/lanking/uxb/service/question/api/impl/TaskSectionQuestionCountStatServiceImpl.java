package com.lanking.uxb.service.question.api.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.question.api.SectionQuestionCountStatService;
import com.lanking.uxb.service.question.api.TaskSectionQuestionCountStatService;
import com.lanking.uxb.service.question.form.ChapterStatForm;

@Service
public class TaskSectionQuestionCountStatServiceImpl implements TaskSectionQuestionCountStatService {

	@Autowired
	private SectionQuestionCountStatService statService;
	@Autowired
	private TextbookService textbookService;

	private static final int TEXTBOOK_FETCH_SIZE = 20;

	@SuppressWarnings("rawtypes")
	@Override
	public void chapterStat(int version) {
		CursorPageable<Long> cursorPageable = CP.cursor(Long.MAX_VALUE, TEXTBOOK_FETCH_SIZE);
		CursorPage<Long, Textbook> cursorPage = statService.queryTextbookList(cursorPageable);
		statService.deleteStat(version);
		while (CollectionUtils.isNotEmpty(cursorPage.getItems())) {
			List<Textbook> list = cursorPage.getItems();
			for (Textbook t : list) {
				List<Map> statlist = statService.findQuestionStatBySections(t.getCode(), version);
				ChapterStatForm form = new ChapterStatForm();
				form.setCategoryCode(t.getCategoryCode());
				form.setPhaseCode(t.getPhaseCode());
				form.setSubjectCode(t.getSubjectCode());
				form.setTextbookCode(t.getCode());
				form.setVersion(version);
				statService.save(statlist, form);
			}
			Long nextCursor = cursorPage.getNextCursor();
			cursorPageable = CP.cursor(nextCursor, TEXTBOOK_FETCH_SIZE);
			cursorPage = statService.queryTextbookList(cursorPageable);
		}
	}
}
