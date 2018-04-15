package com.lanking.uxb.service.holiday.api.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkAnswerImage;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItem;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemAnswer;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemAnswerService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemQuestionService;
import com.lanking.uxb.service.holiday.form.StuItemAnswerForm;

@Service
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class HolidayStuHomeworkItemAnswerServiceImpl implements HolidayStuHomeworkItemAnswerService {

	@Autowired
	@Qualifier("HolidayStuHomeworkItemAnswerRepo")
	private Repo<HolidayStuHomeworkItemAnswer, Long> itemAnswerRepo;
	@Autowired
	@Qualifier("HolidayStuHomeworkItemRepo")
	private Repo<HolidayStuHomeworkItem, Long> holidayStuHomeworkItemRepo;
	@Autowired
	@Qualifier("HolidayStuHomeworkItemQuestionRepo")
	private Repo<HolidayStuHomeworkItemQuestion, Long> holidayStuHomeworkItemQuestionRepo;
	@Autowired
	@Qualifier("HolidayHomeworkRepo")
	private Repo<HolidayHomework, Long> holidayHomeworkRepo;
	@Autowired
	@Qualifier("HolidayStuHomeworkAnswerImageRepo")
	private Repo<HolidayStuHomeworkAnswerImage, Long> holidayStuHomeworkAnswerImageRepo;

	@Autowired
	private HolidayStuHomeworkItemQuestionService itemQuestionService;

	@Transactional
	@Override
	public void saveAnswer(StuItemAnswerForm stuItemAnswerForm) {
		HolidayStuHomeworkItem stuItem = holidayStuHomeworkItemRepo
				.get(stuItemAnswerForm.getHolidayStuHomeworkItemId());
		stuItem.setCompletionRate(BigDecimal.valueOf(stuItemAnswerForm.getCompletionRate()));
		stuItem.setHomeworkTime(stuItemAnswerForm.getHomeworkTime());
		holidayStuHomeworkItemRepo.save(stuItem);
		for (Long holidystuHkItemQuestionId : stuItemAnswerForm.getAnswerData().keySet()) {
			saveAnswer(holidystuHkItemQuestionId, stuItemAnswerForm.getAnswerData().get(holidystuHkItemQuestionId),
					stuItemAnswerForm.getAnswerAsciiData().get(holidystuHkItemQuestionId),
					stuItemAnswerForm.getSolvingImgs(), stuItemAnswerForm.getStudentId(), stuItemAnswerForm.getType());
		}
	}

	@Transactional
	@Override
	public void saveAnswer(long holidystuHkItemQuestionId, List<String> answers, List<String> answerAsciis,
			List<Long> solvingImgs, long studentId, Type type) {

		// 清除数据
		holidayStuHomeworkAnswerImageRepo.execute("$deleteByHolidayStuHkQuestion",
				Params.param("id", holidystuHkItemQuestionId));
		// 保存解题截图过程
		if (CollectionUtils.isNotEmpty(solvingImgs)) {
			// 历史保存方式(保存答题图片)
			itemQuestionService.saveAnswerImg(holidystuHkItemQuestionId, solvingImgs.get(0));
			for (long imgId : solvingImgs) {
				HolidayStuHomeworkAnswerImage img = new HolidayStuHomeworkAnswerImage();
				img.setAnswerImg(imgId);
				img.setHolidayStuItemQuestionId(holidystuHkItemQuestionId);
				holidayStuHomeworkAnswerImageRepo.save(img);
			}
		}
		// itemQuestionService.saveSolvingImg(holidystuHkItemQuestionId,
		// solvingImg);
		List<HolidayStuHomeworkItemAnswer> list = queryItemAnswers(holidystuHkItemQuestionId);
		HolidayStuHomeworkItemQuestion itemQuestion = holidayStuHomeworkItemQuestionRepo.get(holidystuHkItemQuestionId);
		// 表示第一次没有存过
		if (list.isEmpty()) {
			// 如果是多选题存一条记录
			if (type == Type.MULTIPLE_CHOICE) {
				HolidayStuHomeworkItemAnswer itemAnswer = new HolidayStuHomeworkItemAnswer();
				itemAnswer.setAnswerAt(new Date());
				itemAnswer.setContent(listToString(answers));
				itemAnswer.setContentAscii(listToString(answerAsciis));
				itemAnswer.setHolidayStuHomeworkItemQuestionId(holidystuHkItemQuestionId);
				itemAnswer.setHolidayHomeworkItemId(itemQuestion.getHolidayHomeworkItemId());
				itemAnswer.setHolidayStuHomeworkItemId(itemQuestion.getHolidayStuHomeworkItemId());
				itemAnswer.setHolidayStuHomeworkId(itemQuestion.getHolidayHomeworkId());
				itemAnswer.setHolidayHomeworkId(itemQuestion.getHolidayHomeworkId());
				itemAnswer.setAnswerId(studentId);
				itemAnswerRepo.save(itemAnswer);
			} else {
				for (int i = 0; i < answers.size(); i++) {
					HolidayStuHomeworkItemAnswer itemAnswer = new HolidayStuHomeworkItemAnswer();
					itemAnswer.setAnswerAt(new Date());
					itemAnswer.setContent(answers.get(i));
					itemAnswer.setContentAscii(answerAsciis.get(i));
					itemAnswer.setHolidayStuHomeworkItemQuestionId(holidystuHkItemQuestionId);
					itemAnswer.setHolidayHomeworkItemId(itemQuestion.getHolidayHomeworkItemId());
					itemAnswer.setHolidayStuHomeworkItemId(itemQuestion.getHolidayStuHomeworkItemId());
					itemAnswer.setHolidayStuHomeworkId(itemQuestion.getHolidayHomeworkId());
					itemAnswer.setHolidayHomeworkId(itemQuestion.getHolidayHomeworkId());
					itemAnswer.setAnswerId(studentId);
					itemAnswer.setSequence(i + 1);
					itemAnswerRepo.save(itemAnswer);
				}
			}
		} else {
			if (type == Type.MULTIPLE_CHOICE) {
				HolidayStuHomeworkItemAnswer itemAnswer = list.get(0);
				itemAnswer.setAnswerAt(new Date());
				itemAnswer.setContent(answers.size() == 0 ? null : listToString(answers));
				itemAnswer.setContentAscii(answerAsciis.size() == 0 ? null : listToString(answerAsciis));
				itemAnswer.setAnswerId(studentId);
				itemAnswerRepo.save(itemAnswer);
			} else {
				for (int i = 0; i < list.size(); i++) {
					HolidayStuHomeworkItemAnswer itemAnswer = list.get(i);
					itemAnswer.setAnswerAt(new Date());
					itemAnswer.setContent(answers.size() == 0 ? null : answers.get(i));
					itemAnswer.setContentAscii(answerAsciis.size() == 0 ? null : answerAsciis.get(i));
					itemAnswer.setAnswerId(studentId);
					itemAnswerRepo.save(itemAnswer);
				}
			}
		}
	}

	@Override
	@Transactional
	public void saveAnswer(List<Long> holidayStuHkItemQuestionIds, Map<Long, List<String>> answers,
			Map<Long, List<String>> asciiAnswers, Map<Long, List<Long>> solvingImgs, long studentId,
			Map<Long, Type> types, long holidayHkId, long holidayHkItemId, long holidayStuHkId, long holidayStuHkItemId) {
		Date answerAt = new Date();
		HolidayHomework holidayHomework = holidayHomeworkRepo.get(holidayHkId);
		for (Long id : holidayStuHkItemQuestionIds) {
			List<String> answerList = answers.get(id);
			List<String> asciiAnswerList = asciiAnswers.get(id);
			Question.Type type = types.get(id);
			int sequence = 1;
			List<Long> imgs = solvingImgs.get(id);
			long answerImg = CollectionUtils.isEmpty(imgs) ? 0 : imgs.get(0);
			itemQuestionService.saveAnswerImg(id, answerImg);

			holidayStuHomeworkAnswerImageRepo.execute("$deleteByHolidayStuHkQuestion", Params.param("id", id));

			if (CollectionUtils.isNotEmpty(imgs)) {
				for (long imgId : imgs) {
					HolidayStuHomeworkAnswerImage img = new HolidayStuHomeworkAnswerImage();
					img.setAnswerImg(imgId);
					img.setHolidayStuItemQuestionId(id);

					holidayStuHomeworkAnswerImageRepo.save(img);
				}
			}
			// itemQuestionService.saveSolvingImg(id, solvingImg);
			if (type == Type.MULTIPLE_CHOICE) {
				itemAnswerRepo.execute("$deleteItemAnswers", Params.param("itemQuestionId", id));
				for (String answer : answerList) {
					HolidayStuHomeworkItemAnswer a = new HolidayStuHomeworkItemAnswer();
					// a.setSolvingImg(solvingImgs.get(id) == null ? 0 :
					// solvingImgs.get(id));
					a.setContent(answer);
					a.setContentAscii(asciiAnswerList.get(sequence - 1));
					a.setAnswerAt(answerAt);
					a.setHolidayHomeworkId(holidayHkId);
					a.setHolidayHomeworkItemId(holidayHkItemId);
					a.setHolidayStuHomeworkId(holidayStuHkId);
					a.setHolidayStuHomeworkItemId(holidayStuHkItemId);
					a.setHolidayStuHomeworkItemQuestionId(id);
					a.setAnswerId(studentId);
					a.setSequence(sequence);
					a.setType(holidayHomework.getType());

					itemAnswerRepo.save(a);
					sequence++;
				}
			} else {
				Params params = Params.param();
				params.put("itemQuestionId", id);
				for (String answer : answerList) {
					params.put("sequence", sequence);
					if (sequence > answerList.size()) {
						params.put("answerId", null);
						params.put("answerAt", null);
						params.put("content", null);
						params.put("asciiContent", null);
						// params.put("sovlingImg", null);
						sequence++;
					} else {
						params.put("answerId", studentId);
						params.put("answerAt", answerAt);
						// params.put("sovlingImg", solvingImg);
						params.put("content", answer);
						params.put("asciiContent", asciiAnswerList.get(sequence - 1));
						sequence++;
					}
					itemAnswerRepo.execute("$updateItemAnswer", params);
				}
			}
		}

	}

	public String listToString(List<String> list) {
		String temp = "";
		for (String val : list) {
			temp += val;
		}
		return temp;
	}

	@Override
	public List<HolidayStuHomeworkItemAnswer> queryItemAnswers(long holidystuHkItemQuestionId) {
		return itemAnswerRepo.find("$queryItemAnswers", Params.param("itemQuestionId", holidystuHkItemQuestionId))
				.list();
	}

	@Override
	public Map<Long, List<HolidayStuHomeworkItemAnswer>> queryItemAnswers(Collection<Long> holidystuHkItemQuestionIds) {
		Map<Long, List<HolidayStuHomeworkItemAnswer>> map = Maps.newHashMap();
		for (Long id : holidystuHkItemQuestionIds) {
			map.put(id, Lists.<HolidayStuHomeworkItemAnswer> newArrayList());
		}
		List<HolidayStuHomeworkItemAnswer> list = itemAnswerRepo.find("$queryItemAnswers",
				Params.param("itemQuestionIds", holidystuHkItemQuestionIds)).list();
		for (HolidayStuHomeworkItemAnswer holidayStuHomeworkItemAnswer : list) {
			List<HolidayStuHomeworkItemAnswer> sas = map.get(holidayStuHomeworkItemAnswer
					.getHolidayStuHomeworkItemQuestionId());
			sas.add(holidayStuHomeworkItemAnswer);
			map.put(holidayStuHomeworkItemAnswer.getHolidayStuHomeworkItemQuestionId(), sas);
		}
		return map;
	}

	@Override
	public List<HolidayStuHomeworkItemAnswer> queryItemAnswerList(Collection<Long> holidystuHkItemQuestionIds) {
		return itemAnswerRepo.find("$queryItemAnswers", Params.param("itemQuestionIds", holidystuHkItemQuestionIds))
				.list();
	}

	@Override
	public HolidayStuHomeworkItemAnswer get(long id) {
		return itemAnswerRepo.get(id);
	}
}
