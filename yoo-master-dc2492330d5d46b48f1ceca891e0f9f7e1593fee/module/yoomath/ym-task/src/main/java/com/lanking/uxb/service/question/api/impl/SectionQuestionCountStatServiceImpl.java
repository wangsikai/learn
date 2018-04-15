package com.lanking.uxb.service.question.api.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.resource.question.QuestionSection;
import com.lanking.cloud.domain.common.resource.stat.SectionQuestionStat;
import com.lanking.cloud.sdk.data.CursorGetter;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.question.api.SectionQuestionCountStatService;
import com.lanking.uxb.service.question.form.ChapterStatForm;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class SectionQuestionCountStatServiceImpl implements SectionQuestionCountStatService {

	@Autowired
	@Qualifier("QuestionSectionRepo")
	private Repo<QuestionSection, Long> questionSectionRepo;

	@Autowired
	@Qualifier("SectionQuestionStatRepo")
	private Repo<SectionQuestionStat, Long> statRepo;

	@Autowired
	@Qualifier("TextbookRepo")
	private Repo<Textbook, Long> textbookRepo;

	@Autowired
	private SectionService sectionService;

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> findQuestionStatBySections(Integer textbookCode, Integer version) {
		Params params = Params.param("textbookCode", textbookCode).put("version", version);
		return questionSectionRepo.find("$findQuestionStatByTextbookCode", params).list(Map.class);
	}

	@SuppressWarnings("rawtypes")
	@Transactional
	@Override
	public void save(List<Map> list, ChapterStatForm form) {
		for (Map map : list) {
			Long code = Long.parseLong(String.valueOf(map.get("code")));
			String name = String.valueOf(map.get("name"));
			Integer total = Integer.parseInt(String.valueOf(map.get("total")));
			Integer passCount = Integer.parseInt(String.valueOf(map.get("pass")));
			Integer nopassCount = Integer.parseInt(String.valueOf(map.get("nopass")));
			Integer nocheckCount = Integer.parseInt(String.valueOf(map.get("editing")));
			Integer onepassCount = Integer.parseInt(String.valueOf(map.get("onepass")));
			SectionQuestionStat s = new SectionQuestionStat();
			s.setSectionCode(code);
			s.setName(name);
			s.setCategoryCode(form.getCategoryCode());
			s.setInputCount(total);
			s.setNocheckCount(nocheckCount);
			s.setNopassCount(nopassCount);
			s.setOnepassCount(onepassCount);
			s.setPassCount(passCount);
			s.setPhaseCode(form.getPhaseCode());
			s.setSubjectCode(form.getSubjectCode());
			s.setTextbookCode(form.getTextbookCode());
			s.setVersion(form.getVersion());
			statRepo.save(s);
		}
	}

	@Transactional
	@Override
	public void deleteStat(int version) {
		statRepo.execute("$deleteStat", Params.param("version", version));
	}

	@Override
	public CursorPage<Long, Textbook> queryTextbookList(CursorPageable<Long> pageable) {

		return textbookRepo.find("$queryTextbookList").fetch(pageable, Textbook.class,
				new CursorGetter<Long, Textbook>() {
					@Override
					public Long getCursor(Textbook bean) {
						return Long.valueOf(bean.getCode());
					}
				});
	}
}
