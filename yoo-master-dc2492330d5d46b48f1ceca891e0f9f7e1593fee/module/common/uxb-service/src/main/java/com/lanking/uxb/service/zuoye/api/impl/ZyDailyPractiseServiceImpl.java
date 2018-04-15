package com.lanking.uxb.service.zuoye.api.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractise;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.PracticeHistoryService;
import com.lanking.uxb.service.zuoye.api.ZyDailyPracticeSettingsService;
import com.lanking.uxb.service.zuoye.api.ZyDailyPractiseQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyDailyPractiseService;
import com.lanking.uxb.service.zuoye.form.DailyPractiseSaveForm;
import com.lanking.uxb.service.zuoye.form.PracticeHistoryForm;

/**
 * @see ZyDailyPractiseService
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.0
 */
@Service
@Transactional(readOnly = true)
public class ZyDailyPractiseServiceImpl implements ZyDailyPractiseService {
	@Autowired
	@Qualifier("DailyPractiseRepo")
	Repo<DailyPractise, Long> repo;
	@Autowired
	private ZyDailyPractiseQuestionService dailyPractiseQuestionService;
	@Autowired
	private ZyDailyPracticeSettingsService dailyPracticeSettingsService;
	@Autowired
	private PracticeHistoryService practiceHistoryService;

	@Override
	public CursorPage<Long, DailyPractise> query(long userId, int textbookCode, CursorPageable<Long> cursorPageable) {
		Params params = Params.param("userId", userId);
		params.put("textbookCode", textbookCode);
		return repo.find("$zyQuery", params).fetch(cursorPageable);
	}

	@Override
	public Page<DailyPractise> query(long userId, int textbookCode, Pageable pageable) {
		Params params = Params.param("userId", userId);
		params.put("textbookCode", textbookCode);
		return repo.find("$zyQuery", params).fetch(pageable);
	}

	@Override
	public DailyPractise getLatest(long userId, int textbookCode) {
		Params params = Params.param("userId", userId);
		params.put("textbookCode", textbookCode);
		DailyPractise practise = repo.find("$zyFindLatest", params).get();

		return practise;
	}

	@Override
	@Transactional
	public DailyPractise save(DailyPractiseSaveForm form) {
		DailyPractise dailyPractise = new DailyPractise();
		dailyPractise.setCreateAt(new Date());
		dailyPractise.setDifficulty(form.getDifficulty());
		dailyPractise.setDoCount(0);
		dailyPractise.setName(form.getName());
		dailyPractise.setPractiseId(0);
		dailyPractise.setQuestionCount(form.getqIds().size());
		dailyPractise.setRightCount(0);
		dailyPractise.setWrongCount(0);
		dailyPractise.setRightRate(new BigDecimal(0));
		dailyPractise.setSectionCode(form.getSectionCode());
		dailyPractise.setUserId(form.getUserId());
		dailyPractise.setTextbookCode(form.getTextbookCode());
		repo.save(dailyPractise);

		// 保存最新的进度
		dailyPracticeSettingsService.set(form.getSettings().getId(), form.getSettings().getCurSectionCode(),
				form.getSettings().getCurPeriod());

		// 保存每日一练题目
		dailyPractiseQuestionService.save(form.getqIds(), dailyPractise.getId());

		return dailyPractise;
	}

	@Override
	public long getTotalPractiseDays(long userId, int textbookCode, boolean isFindTotal) {
		return repo.find("$zyGetPractiseDays",
				Params.param("userId", userId).put("textbookCode", textbookCode).put("isFindTotal", isFindTotal))
				.count();
	}

	@Override
	public DailyPractise get(long id) {
		return repo.get(id);
	}

	@Override
	@Transactional
	public DailyPractise copy(DailyPractise from) {
		DailyPractise dailyPractise = new DailyPractise();
		dailyPractise.setCreateAt(from.getCreateAt());
		dailyPractise.setDifficulty(from.getDifficulty());
		dailyPractise.setDoCount(from.getDoCount());
		dailyPractise.setName(from.getName());
		dailyPractise.setQuestionCount(from.getQuestionCount());
		dailyPractise.setRightCount(from.getRightCount());
		dailyPractise.setRightRate(from.getRightRate());
		dailyPractise.setSectionCode(from.getSectionCode());
		dailyPractise.setTextbookCode(from.getTextbookCode());
		dailyPractise.setUpdateAt(from.getUpdateAt());
		dailyPractise.setUserId(from.getUserId());
		dailyPractise.setWrongCount(from.getWrongCount());
		dailyPractise.setPractiseId(from.getId());
		dailyPractise.setHomeworkTime(from.getHomeworkTime());
		dailyPractise.setCommitAt(from.getCommitAt());
		return repo.save(dailyPractise);
	}

	@Override
	@Transactional
	public DailyPractise update(DailyPractise dailyPractise) {
		DailyPractise d = repo.get(dailyPractise.getId());

		copyAndUpdateHistory(d);

		d.setRightRate(dailyPractise.getRightRate());
		d.setDoCount(dailyPractise.getDoCount());
		d.setWrongCount(dailyPractise.getWrongCount());
		d.setRightCount(dailyPractise.getRightCount());
		d.setHomeworkTime(dailyPractise.getHomeworkTime());
		d.setCommitAt(new Date());
		d.setUpdateAt(new Date());

		PracticeHistoryForm historyForm = new PracticeHistoryForm();
		historyForm.setBiz(Biz.DAILY_PRACTICE);
		historyForm.setBizId(dailyPractise.getId());
		historyForm.setName(dailyPractise.getName());
		historyForm.setRightRate(dailyPractise.getRightRate());
		historyForm.setUserId(dailyPractise.getUserId());
		historyForm.setCompletionRate(BigDecimal.valueOf((100d * d.getDoCount()) / d.getQuestionCount()).setScale(2,
				BigDecimal.ROUND_HALF_UP));
		historyForm.setCreateAt(new Date());
		historyForm.setUpdateAt(new Date());

		practiceHistoryService.updateHistory(historyForm);
		return repo.save(d);
	}

	@Override
	@Transactional
	public DailyPractise draft(List<Map<Long, List<String>>> answerList, List<Long> questionIds, long id,
			int homeworkTime) {
		DailyPractise d = repo.get(id);
		if (d.getPractiseId() > 0) {
			d = repo.get(d.getPractiseId());
		}

		copyAndUpdateHistory(d);

		int doCount = dailyPractiseQuestionService.draft(answerList, questionIds, d.getId());
		d.setDoCount(doCount);
		d.setHomeworkTime(homeworkTime);
		d.setUpdateAt(new Date());
		d.setRightRate(null);

		PracticeHistoryForm historyForm = new PracticeHistoryForm();
		historyForm.setBiz(Biz.DAILY_PRACTICE);
		historyForm.setBizId(d.getId());
		historyForm.setName(d.getName());
		historyForm.setRightRate(null);
		historyForm.setUserId(d.getUserId());
		historyForm.setCompletionRate(BigDecimal.valueOf((100d * d.getDoCount()) / d.getQuestionCount()).setScale(2,
				BigDecimal.ROUND_HALF_UP));
		historyForm.setCreateAt(new Date());
		historyForm.setUpdateAt(new Date());

		practiceHistoryService.updateHistory(historyForm);

		return repo.save(d);
	}

	@Transactional
	@Override
	public void copyAndUpdateHistory(DailyPractise d) {
		if (d.getRightRate() != null && d.getDoCount() > 0) {
			DailyPractise copyDailyPractise = copy(d);
			dailyPractiseQuestionService.copy(d.getId(), copyDailyPractise.getId());

			PracticeHistoryForm historyForm = new PracticeHistoryForm();
			historyForm.setBiz(Biz.DAILY_PRACTICE);
			historyForm.setBizId(copyDailyPractise.getId());
			historyForm.setCompletionRate(
					BigDecimal.valueOf((copyDailyPractise.getDoCount() * 100d) / copyDailyPractise.getQuestionCount())
							.setScale(2, BigDecimal.ROUND_HALF_UP));
			historyForm.setCreateAt(copyDailyPractise.getCreateAt());
			historyForm.setName(copyDailyPractise.getName());
			historyForm.setRightRate(copyDailyPractise.getRightRate());
			historyForm.setUserId(copyDailyPractise.getUserId());

			practiceHistoryService.updateHistory(historyForm);
		}
	}

	@Override
	@Transactional
	public DailyPractise doOne(Map<Long, List<String>> answers, long dailyQuestionId, long id, int homeworkTime) {
		DailyPractise d = repo.get(id);

		if (d.getPractiseId() > 0) {
			d = repo.get(d.getPractiseId());
		}

		copyAndUpdateHistory(d);

		dailyPractiseQuestionService.doOne(answers, dailyQuestionId);

		d.setDoCount(dailyPractiseQuestionService.countDone(d.getId()));
		d.setHomeworkTime(homeworkTime);
		d.setUpdateAt(new Date());
		d.setRightRate(null);

		PracticeHistoryForm historyForm = new PracticeHistoryForm();
		historyForm.setBiz(Biz.DAILY_PRACTICE);
		historyForm.setBizId(d.getId());
		historyForm.setName(d.getName());
		historyForm.setRightRate(null);
		historyForm.setUserId(d.getUserId());
		historyForm.setCompletionRate(BigDecimal.valueOf((100d * d.getDoCount()) / d.getQuestionCount()).setScale(2,
				BigDecimal.ROUND_HALF_UP));
		historyForm.setCreateAt(new Date());
		historyForm.setUpdateAt(new Date());

		practiceHistoryService.updateHistory(historyForm);
		return repo.save(d);
	}

}
