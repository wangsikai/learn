package com.lanking.uxb.zycon.holiday.convert;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemAnswer;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.zycon.base.api.ZycQuestionService;
import com.lanking.uxb.zycon.base.convert.ZycQuestionConvert;
import com.lanking.uxb.zycon.base.value.CQuestion;
import com.lanking.uxb.zycon.holiday.api.ZycHolidayStuHomeworkItemAnswerService;
import com.lanking.uxb.zycon.holiday.value.VZycHolidayStuHomeworkItemQuestion;
import com.lanking.uxb.zycon.homework.value.VZycStudentHomeworkAnswer;

/**
 * 假期作业学生专项Convert
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
@Component
public class ZycHolidayStuHomeworkItemQuestionConvert
		extends
			Converter<VZycHolidayStuHomeworkItemQuestion, HolidayStuHomeworkItemQuestion, Long> {
	@Autowired
	private ZycQuestionService questionService;
	@Autowired
	private ZycQuestionConvert questionConvert;
	@Autowired
	private ZycHolidayStuHomeworkItemAnswerService stuHomeworkItemAnswerService;
	@Autowired
	private ZycHolidayStuHomeworkItemAnswerConvert stuHomeworkAnswerConvert;

	@Override
	protected Long getId(HolidayStuHomeworkItemQuestion holidayStuHomeworkItemQuestion) {
		return holidayStuHomeworkItemQuestion.getId();
	}

	@Override
	protected VZycHolidayStuHomeworkItemQuestion convert(
			HolidayStuHomeworkItemQuestion holidayStuHomeworkItemQuestion) {
		VZycHolidayStuHomeworkItemQuestion v = new VZycHolidayStuHomeworkItemQuestion();
		v.setId(holidayStuHomeworkItemQuestion.getId());
		v.setHolidayHomeworkItemId(holidayStuHomeworkItemQuestion.getHolidayHomeworkItemId());
		v.setHolidayStuHomeworkId(holidayStuHomeworkItemQuestion.getHolidayStuHomeworkId());
		v.setHolidayHomeworkItemId(holidayStuHomeworkItemQuestion.getHolidayHomeworkItemId());
		v.setStudentId(holidayStuHomeworkItemQuestion.getStudentId());
		v.setStudentId(holidayStuHomeworkItemQuestion.getStudentId());
		v.setResult(holidayStuHomeworkItemQuestion.getResult());

		return v;
	}

	// 转换题目
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(
				new ConverterAssembler<VZycHolidayStuHomeworkItemQuestion, HolidayStuHomeworkItemQuestion, Long, CQuestion>() {

					@Override
					public boolean accept(HolidayStuHomeworkItemQuestion holidayStuHomeworkItemQuestion) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(HolidayStuHomeworkItemQuestion holidayStuHomeworkItemQuestion,
							VZycHolidayStuHomeworkItemQuestion vZycHolidayStuHomeworkItemQuestion) {
						return holidayStuHomeworkItemQuestion.getQuestionId();
					}

					@Override
					public void setValue(HolidayStuHomeworkItemQuestion holidayStuHomeworkItemQuestion,
							VZycHolidayStuHomeworkItemQuestion vZycHolidayStuHomeworkItemQuestion, CQuestion value) {
						if (value != null) {
							vZycHolidayStuHomeworkItemQuestion.setQuestion(value);
						}
					}

					@Override
					public CQuestion getValue(Long key) {
						if (null == key) {
							return null;
						}
						return questionConvert.to(questionService.get(key), false, true, true, null);
					}

					@Override
					public Map<Long, CQuestion> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Maps.newHashMap();
						}
						List<Question> questions = questionService.mgetList(keys);
						Map<Long, CQuestion> questionMap = new HashMap<Long, CQuestion>(questions.size());
						List<CQuestion> cQuestions = questionConvert.to(questions, false, true, true, null);
						for (CQuestion c : cQuestions) {
							questionMap.put(c.getId(), c);
						}
						return questionMap;
					}
				});

		assemblers.add(
				new ConverterAssembler<VZycHolidayStuHomeworkItemQuestion, HolidayStuHomeworkItemQuestion, Long, List<VZycStudentHomeworkAnswer>>() {

					@Override
					public boolean accept(HolidayStuHomeworkItemQuestion holidayStuHomeworkItemQuestion) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(HolidayStuHomeworkItemQuestion holidayStuHomeworkItemQuestion,
							VZycHolidayStuHomeworkItemQuestion vZycHolidayStuHomeworkItemQuestion) {
						return holidayStuHomeworkItemQuestion.getId();
					}

					@Override
					public void setValue(HolidayStuHomeworkItemQuestion holidayStuHomeworkItemQuestion,
							VZycHolidayStuHomeworkItemQuestion vZycHolidayStuHomeworkItemQuestion,
							List<VZycStudentHomeworkAnswer> value) {
						// vZycHolidayStuHomeworkItemQuestion.getQuestion().setStudentHomeworkAnswers(value);
						vZycHolidayStuHomeworkItemQuestion.setStudentHomeworkAnswers(value);
					}

					@Override
					public List<VZycStudentHomeworkAnswer> getValue(Long key) {
						List<HolidayStuHomeworkItemAnswer> answers = stuHomeworkItemAnswerService.query(key);

						return stuHomeworkAnswerConvert.to(answers);
					}

					@Override
					public Map<Long, List<VZycStudentHomeworkAnswer>> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Maps.newHashMap();
						}

						List<HolidayStuHomeworkItemAnswer> answers = stuHomeworkItemAnswerService
								.mgetListByQuestion(keys);
						List<VZycStudentHomeworkAnswer> vs = stuHomeworkAnswerConvert.to(answers);
						Map<Long, List<VZycStudentHomeworkAnswer>> answerMap = Maps.newHashMap();
						for (VZycStudentHomeworkAnswer v : vs) {
							if (answerMap.get(v.getStudentHomeworkQuestionId()) == null) {
								answerMap.put(v.getStudentHomeworkQuestionId(),
										Lists.<VZycStudentHomeworkAnswer> newArrayList());
							}

							answerMap.get(v.getStudentHomeworkQuestionId()).add(v);
						}
						return answerMap;
					}
				});
	}
}
