package com.lanking.uxb.service.holiday.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.HomeworkConfirmStatus;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItem;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItem;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemAnswer;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemQuestion;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemAnswerService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemQuestionService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemService;
import com.lanking.uxb.service.holiday.form.HolidayStuHomeworkItemPublishForm;
import com.lanking.uxb.service.intelligentCorrection.api.IntelligentCorrectionService;
import com.lanking.uxb.service.intelligentCorrection.value.CorrectResult;
import com.lanking.uxb.service.question.api.AnswerService;
import com.lanking.uxb.service.question.api.QuestionService;

@Service
@Transactional(readOnly = true)
public class HolidayStuHomeworkItemServiceImpl implements HolidayStuHomeworkItemService {

	@Autowired
	@Qualifier("HolidayStuHomeworkItemRepo")
	private Repo<HolidayStuHomeworkItem, Long> holidayStuHomeworkItemRepo;

	@Autowired
	@Qualifier("HolidayStuHomeworkRepo")
	private Repo<HolidayStuHomework, Long> holidayStuHomeworkRepo;

	@Autowired
	@Qualifier("HolidayHomeworkItemRepo")
	private Repo<HolidayHomeworkItem, Long> holidayHomeworkItemRepo;

	@Autowired
	@Qualifier("HolidayStuHomeworkItemAnswerRepo")
	private Repo<HolidayStuHomeworkItemAnswer, Long> holidayStuHomeworkItemAnswerRepo;

	@Autowired
	@Qualifier("HolidayStuHomeworkItemQuestionRepo")
	private Repo<HolidayStuHomeworkItemQuestion, Long> holidayStuHomeworkItemQuestionRepo;

	@Autowired
	private HolidayStuHomeworkItemQuestionService questionService;
	@Autowired
	private HolidayStuHomeworkItemAnswerService answerService;
	@Autowired
	private QuestionService qservice;
	@Autowired
	private AnswerService aservice;
	@Autowired
	private IntelligentCorrectionService intelligentCorrectionService;

	@Override
	public List<HolidayStuHomeworkItem> queryStuHkItems(Long holidayHomeworkId, Long userId, Long holidayStuHomeworkId,
			List<Long> stuIds) {
		Params params = Params.param();
		if (holidayHomeworkId != null) {
			params.put("holidayHomeworkId", holidayHomeworkId);
		}
		if (userId != null) {
			params.put("userId", userId);
		}
		if (holidayStuHomeworkId != null) {
			params.put("holidayStuHomeworkId", holidayStuHomeworkId);
		}
		if (CollectionUtils.isNotEmpty(stuIds)) {
			params.put("stuIds", stuIds);
		}
		return holidayStuHomeworkItemRepo.find("$queryStuHkItems", params).list();
	}

	@Override
	public List<HolidayStuHomeworkItem> listByHomeworkItem(long homeworkItemId) {
		return holidayStuHomeworkItemRepo.find("$findStudentHomework", Params.param("homeworkItemId", homeworkItemId))
				.list();
	}

	@Override
	@Transactional
	public HolidayStuHomeworkItem create(HolidayStuHomeworkItemPublishForm form) {
		HolidayStuHomeworkItem item = new HolidayStuHomeworkItem();
		item.setCreateAt(new Date());
		item.setDelStatus(Status.ENABLED);
		item.setHolidayHomeworkId(form.getHolidayHomeworkId());
		item.setHolidayHomeworkItemId(form.getHolidayHomeworkItemId());
		item.setHolidayStuHomeworkId(form.getHolidayStuHomeworkId());
		item.setStatus(StudentHomeworkStatus.NOT_SUBMIT);
		item.setStudentId(form.getStudentId());
		item.setType(form.getType());

		holidayStuHomeworkItemRepo.save(item);
		return item;
	}

	@Override
	@Transactional
	public void updateHomeworkTime(Long holidayStuHomeworkItemId, int homeworkTime) {
		HolidayStuHomeworkItem stuItem = holidayStuHomeworkItemRepo.get(holidayStuHomeworkItemId);
		stuItem.setHomeworkTime(homeworkTime);
		holidayStuHomeworkItemRepo.save(stuItem);
	}

	@Override
	@Transactional
	public void updateStudentHkStatus(Long holidayStuHomeworkItemId, double completionRate) {
		HolidayStuHomeworkItem stuItem = holidayStuHomeworkItemRepo.get(holidayStuHomeworkItemId);
		stuItem.setStatus(StudentHomeworkStatus.SUBMITED);
		stuItem.setCompletionRate(BigDecimal.valueOf(completionRate));
		holidayStuHomeworkItemRepo.save(stuItem);
		List<HolidayStuHomeworkItem> list = this.queryStuHkItems(null, null, stuItem.getHolidayStuHomeworkId(), null);
		boolean isAllSubmit = true;
		for (HolidayStuHomeworkItem item : list) {
			if (item.getStatus() == StudentHomeworkStatus.NOT_SUBMIT) {
				isAllSubmit = false;
				break;
			}
		}
		// 提交数量+1
		HolidayStuHomework holidayStuHomework = holidayStuHomeworkRepo.get(stuItem.getHolidayStuHomeworkId());
		holidayStuHomework.setCommitItemCount(holidayStuHomework.getCommitItemCount() + 1);
		if (isAllSubmit) {
			holidayStuHomework.setStatus(StudentHomeworkStatus.SUBMITED);
		}
		stuItem.setSubmitAt(new Date());
		holidayStuHomeworkRepo.save(holidayStuHomework);
		HolidayHomeworkItem holidayHomeworkItem = holidayHomeworkItemRepo.get(stuItem.getHolidayHomeworkItemId());
		holidayHomeworkItem.setCommitCount(holidayHomeworkItem.getCommitCount() + 1);
		holidayHomeworkItemRepo.save(holidayHomeworkItem);
	}

	@Override
	public HolidayStuHomeworkItem get(long id) {
		return holidayStuHomeworkItemRepo.get(id);
	}

	@Override
	public HolidayStuHomeworkItem find(long holidayHomeworkItemId, long studentId) {
		return holidayStuHomeworkItemRepo.find("$find",
				Params.param("holidayHomeworkItemId", holidayHomeworkItemId).put("studentId", studentId)).get();
	}

	@Override
	public CursorPage<Long, HolidayStuHomeworkItem> queryNotCalculate(CursorPageable<Long> pageable) {
		return holidayStuHomeworkItemRepo.find("$queryNotCalculate", Params.param()).fetch(pageable);
	}

	@Async
	@Override
	@Transactional
	public void correctHolidayStuHk(long holidayStuHomeworkItemId) {
		List<HolidayStuHomeworkItemQuestion> questionList = questionService.queryQuestionList(holidayStuHomeworkItemId);
		List<Long> itemQuestionIds = new ArrayList<Long>(questionList.size());
		List<Long> questionIds = new ArrayList<Long>(questionList.size());
		for (HolidayStuHomeworkItemQuestion v : questionList) {
			itemQuestionIds.add(v.getId());
			questionIds.add(v.getQuestionId());
		}

		Map<Long, List<HolidayStuHomeworkItemAnswer>> answerMap = answerService.queryItemAnswers(itemQuestionIds);
		Map<Long, Question> questionMap = qservice.mget(questionIds);
		Map<Long, List<Answer>> questionAnswerMap = aservice.getQuestionAnswers(questionIds);

		for (HolidayStuHomeworkItemQuestion v : questionList) {
			Question q = questionMap.get(v.getQuestionId());

			List<HolidayStuHomeworkItemAnswer> answers = answerMap.get(v.getId());
			List<Answer> questionAnswers = questionAnswerMap.get(q.getId());

			if (CollectionUtils.isEmpty(questionAnswers) || CollectionUtils.isEmpty(answers)) {
				correctQuestion(v, true, HomeworkAnswerResult.WRONG);
				continue;
			}

			if (q.getType() == Type.SINGLE_CHOICE || q.getType() == Type.TRUE_OR_FALSE) {
				HomeworkAnswerResult result = HomeworkAnswerResult.WRONG;
				if (StringUtils.isNotBlank(answers.get(0).getContent())
						&& questionAnswers.get(0).getContent().toLowerCase().trim()
								.equals(answers.get(0).getContent().toLowerCase().trim())) {
					result = HomeworkAnswerResult.RIGHT;
				}

				correctQuestion(v, true, result);

				HolidayStuHomeworkItemAnswer a = answers.get(0);
				correctAnswer(a, result, true);
			} else if (q.getType() == Type.MULTIPLE_CHOICE) {
				List<String> ras = Lists.newArrayList();
				for (Answer a : questionAnswers) {
					ras.add(a.getContent().trim().toLowerCase());
				}
				int rightCount = 0;
				int wrongCount = 0;
				for (HolidayStuHomeworkItemAnswer sha : answers) {
					if (StringUtils.isNotBlank(sha.getContent())) {
						if (ras.contains(sha.getContent().trim().toLowerCase())) {
							correctAnswer(sha, HomeworkAnswerResult.RIGHT, true);
							ras.remove(sha.getContent().trim().toLowerCase());
							rightCount++;
						} else {
							correctAnswer(sha, HomeworkAnswerResult.WRONG, true);
							wrongCount++;
						}
					} else {
						correctAnswer(sha, HomeworkAnswerResult.WRONG, true);
					}
				}
				if (ras.size() == 0 && answers.size() == rightCount && wrongCount == 0) {
					correctQuestion(v, true, HomeworkAnswerResult.RIGHT);
				} else {
					correctQuestion(v, true, HomeworkAnswerResult.WRONG);
				}
			} else if (q.getType() == Type.FILL_BLANK) {
				List<Long> queryIds = new ArrayList<Long>(answers.size());
				List<Long> answerIds = new ArrayList<Long>(answers.size());
				List<String> targets = new ArrayList<String>(answers.size());
				List<String> querys = new ArrayList<String>(answers.size());
				int i = 0;
				Map<Long, HolidayStuHomeworkItemAnswer> amap = new HashMap<Long, HolidayStuHomeworkItemAnswer>(
						answers.size());
				boolean hasWrong = false, hasUnCred = false, hasUnknow = false;
				for (HolidayStuHomeworkItemAnswer sha : answers) {
					amap.put(sha.getId(), sha);
					if (StringUtils.isBlank(sha.getContent())) {
						hasWrong = true;
						correctAnswer(sha, HomeworkAnswerResult.WRONG, true);
					} else {
						queryIds.add(sha.getId());
						answerIds.add(questionAnswers.get(i).getId());
						targets.add(questionAnswers.get(i).getContent());
						querys.add(sha.getContent());
					}

					i++;
				}

				if (queryIds.size() > 0) {
					Map<Long, CorrectResult> resultMap = intelligentCorrectionService.correct(queryIds, answerIds,
							targets, querys);

					for (Map.Entry<Long, CorrectResult> e : resultMap.entrySet()) {
						HolidayStuHomeworkItemAnswer sha = amap.get(e.getKey());

						correctAnswer(sha, e.getValue().getResult(), e.getValue().isCredible());
						if (e.getValue().getResult() == HomeworkAnswerResult.WRONG) {
							hasWrong = true;
						} else if (e.getValue().getResult() == HomeworkAnswerResult.UNKNOW) {
							hasUnknow = true;
						}
						if (!e.getValue().isCredible()) {
							hasUnCred = true;
						}
					}

				}

				if (hasUnknow) {
					correctQuestion(v, false, HomeworkAnswerResult.UNKNOW);
				} else {
					correctQuestion(v, !hasWrong || !hasUnCred, hasWrong ? HomeworkAnswerResult.WRONG
							: HomeworkAnswerResult.RIGHT);
				}
			}

		}

	}

	@Transactional
	private void correctQuestion(HolidayStuHomeworkItemQuestion q, boolean man, HomeworkAnswerResult result) {
		q.setResult(result);
		q.setAutoResult(result);
		q.setAutoCorrectAt(new Date());
		q.setAutoCorrect(true);
		q.setManualCorrect(man);

		if (man) {
			q.setResult(result);
			q.setConfirmStatus(HomeworkConfirmStatus.NOT_NEED_CONFIRM);
		} else {
			q.setConfirmStatus(HomeworkConfirmStatus.NEED_CONFIRM);
		}

		holidayStuHomeworkItemQuestionRepo.save(q);
	}

	@Transactional
	private void correctAnswer(HolidayStuHomeworkItemAnswer a, HomeworkAnswerResult result, boolean credible) {
		a.setAutoCorrectAt(new Date());
		a.setAutoResult(result);
		a.setResult(result);
		a.setCredible(credible);

		holidayStuHomeworkItemAnswerRepo.save(a);
	}

	@Override
	public List<Map> getClazzStat(long holidayStuHomeworkId) {
		return holidayStuHomeworkItemRepo.find("$getClazzStat",
				Params.param("holidayStuHomeworkId", holidayStuHomeworkId)).list(Map.class);
	}

	@Override
	public Double getSumComplete(long holidayStuHomeworkId) {
		return holidayStuHomeworkItemRepo.find("$getSumComplete",
				Params.param("holidayStuHomeworkId", holidayStuHomeworkId)).get(Double.class);
	}

	@Override
	@Transactional
	public long updateStuItemCompleteRate(long holidayStuHomeworkItemId, double completionRate) {
		Date now = new Date();
		HolidayStuHomeworkItem stuItem = holidayStuHomeworkItemRepo.get(holidayStuHomeworkItemId);
		stuItem.setCompletionRate(BigDecimal.valueOf(completionRate));
		stuItem.setUpdateAt(now);
		holidayStuHomeworkItemRepo.save(stuItem);
		return now.getTime();
	}

	@Override
	public List<HolidayStuHomeworkItem> queryStuItems(Long holidayHomeworkId, StudentHomeworkStatus status) {
		return holidayStuHomeworkItemRepo.find("$queryStuItemIds",
				Params.param("holidayHomeworkId", holidayHomeworkId).put("status", status.getValue())).list();
	}

}
