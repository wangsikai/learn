package com.lanking.uxb.zycon.holiday.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.HomeworkConfirmStatus;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItem;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemAnswer;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemQuestion;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.zycon.common.ex.YoomathConsoleException;
import com.lanking.uxb.zycon.holiday.api.ZycHolidayStuHomeworkItemQuestionService;
import com.lanking.uxb.zycon.holiday.cache.ZycHolidayStuHomeworkQuestionCacheService;

/**
 * @see ZycHolidayStuHomeworkItemQuestionService
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
@Service
@Transactional(readOnly = true)
public class ZycHolidayStuHomeworkItemQuestionServiceImpl implements ZycHolidayStuHomeworkItemQuestionService {
	@Autowired
	@Qualifier("HolidayStuHomeworkItemQuestionRepo")
	private Repo<HolidayStuHomeworkItemQuestion, Long> repo;

	@Autowired
	@Qualifier("HolidayStuHomeworkItemAnswerRepo")
	private Repo<HolidayStuHomeworkItemAnswer, Long> answerRepo;

	@Autowired
	@Qualifier("HolidayStuHomeworkItemRepo")
	private Repo<HolidayStuHomeworkItem, Long> itemRepo;
	@Autowired
	private ZycHolidayStuHomeworkQuestionCacheService stuHomeworkQuestionCacheService;

	@Override
	public List<HolidayStuHomeworkItemQuestion> findCorrectQuestions(int size) {
		List<Long> notInIds = stuHomeworkQuestionCacheService.alreadyPushedIds();
		Params params = Params.param();
		if (CollectionUtils.isNotEmpty(notInIds)) {
			params.put("notInIds", notInIds);
		}
		params.put("size", size);

		return repo.find("$zycFindHolidayStuQuestion", params).list();
	}

	@Override
	public long countNotCorrectQuestions() {
		return repo.find("$zycCountNotCorrectQuestions", Params.param()).count();
	}

	@Override
	@Transactional
	public void correct(long stuItemQuestionId, HomeworkAnswerResult result, long correctUserId) {
		HolidayStuHomeworkItemQuestion itemQuestion = repo.get(stuItemQuestionId);
		HolidayStuHomeworkItem item = itemRepo.get(itemQuestion.getHolidayStuHomeworkItemId());
		if (item.getRightRate() != null && item.getRightRate().doubleValue() >= 0) {
			throw new YoomathConsoleException(YoomathConsoleException.HOMEWORK_ISSUED);
		}
		itemQuestion.setCorrectAt(new Date());
		itemQuestion.setManualCorrect(true);
		itemQuestion.setResult(result);

		List<HolidayStuHomeworkItemAnswer> answers = answerRepo.find("$zycQueryByItemQuestion",
				Params.param("itemQuestionId", itemQuestion.getId())).list();

		// 保存学生题目答案批改结果
		for (HolidayStuHomeworkItemAnswer a : answers) {
			a.setResult(result);

			answerRepo.save(a);
		}

		repo.save(itemQuestion);

	}

	@Override
	public HolidayStuHomeworkItemQuestion get(long id) {
		return repo.get(id);
	}

	@Override
	@Transactional
	public void confirm(List<Long> ids) {
		repo.execute("$zycUpdateStatus",
				Params.param("ids", ids).put("status", HomeworkConfirmStatus.HAD_CONFIRM.getValue()));
	}

	@Override
	public Page<HolidayStuHomeworkItemQuestion> findConfirmQuestions(Pageable pageable) {
		return repo.find("$zycFindConfirm", Params.param()).fetch(pageable);
	}

	@Override
	@Transactional
	public void correct(long stuItemQuestionId, Map<Long, HomeworkAnswerResult> results, long correctUserId) {
		boolean hasWrong = false;
		for (Map.Entry<Long, HomeworkAnswerResult> e : results.entrySet()) {
			Long answerId = e.getKey();
			HomeworkAnswerResult result = e.getValue();

			if (result == HomeworkAnswerResult.WRONG) {
				hasWrong = true;
			}
			answerRepo.execute("$zycUpdateResult", Params.param("id", answerId).put("result", result.getValue()));
		}

		repo.execute(
				"$zycUpdateResult",
				Params.param("id", stuItemQuestionId)
						.put("result",
								hasWrong ? HomeworkAnswerResult.WRONG.getValue() : HomeworkAnswerResult.RIGHT
										.getValue()).put("correctAt", new Date()));
	}

	@Override
	public Page<HolidayStuHomeworkItemQuestion> findConfirmQuestionsByCode(Pageable pageable, String questionCode) {
		Params params = Params.param();
		params.put("questionCode", questionCode);
		return repo.find("$zycFindConfirmById", params).fetch(pageable);
	}
}
