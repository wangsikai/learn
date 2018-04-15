package com.lanking.uxb.service.resources.convert;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoomath.homework.HomeworkQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.value.VHomeworkQuestion;
import com.lanking.uxb.service.resources.value.VQuestion;

@Component
public class HomeworkQuestionConvert extends Converter<VHomeworkQuestion, HomeworkQuestion, Long> {

	@Autowired
	private QuestionConvert qConvert;
	@Autowired
	private QuestionService questionService;

	@Override
	protected Long getId(HomeworkQuestion s) {
		return s.getId();
	}

	@Override
	protected VHomeworkQuestion convert(HomeworkQuestion s) {
		VHomeworkQuestion v = new VHomeworkQuestion();
		v.setHomeworkId(s.getHomeworkId());
		v.setId(s.getId());
		v.setQuestionId(s.getQuestionId());
		v.setSequence(s.getSequence());
		v.setRightCount(s.getRightCount() == null ? 0 : s.getRightCount());
		v.setWrongCount(s.getWrongCount() == null ? 0 : s.getWrongCount());
		v.setHalfWrongCount(s.getHalfWrongCount() == null ? 0 : s.getHalfWrongCount());
		v.setRightRate(s.getRightRate() == null ? BigDecimal.valueOf(0) : s.getRightRate());
		v.setDoTime(s.getDoTime() == null ? 0 : s.getDoTime());
		return v;
	}

	public VHomeworkQuestion to(HomeworkQuestion s, HomeworkQuestionConvertOption option) {
		s.setInitRightRate(option.isRightRate());
		s.setInitSub(option.isInitSub());
		return super.to(s);
	}

	public List<VHomeworkQuestion> to(List<HomeworkQuestion> ss, HomeworkQuestionConvertOption option) {
		for (HomeworkQuestion s : ss) {
			s.setInitRightRate(option.isRightRate());
			s.setInitSub(option.isInitSub());
		}
		return super.to(ss);
	}

	public Map<Long, VHomeworkQuestion> to(Map<Long, HomeworkQuestion> sMap, HomeworkQuestionConvertOption option) {
		for (HomeworkQuestion s : sMap.values()) {
			s.setInitRightRate(option.isRightRate());
			s.setInitSub(option.isInitSub());
		}
		return super.to(sMap);
	}

	public Map<Long, VHomeworkQuestion> toMap(List<HomeworkQuestion> ss, HomeworkQuestionConvertOption option) {
		for (HomeworkQuestion s : ss) {
			s.setInitRightRate(option.isRightRate());
			s.setInitSub(option.isInitSub());
		}
		return super.toMap(ss);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VHomeworkQuestion, HomeworkQuestion, Long, VQuestion>() {
			@Override
			public boolean accept(HomeworkQuestion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(HomeworkQuestion s, VHomeworkQuestion d) {
				return s.getQuestionId();
			}

			@Override
			public void setValue(HomeworkQuestion s, VHomeworkQuestion d, VQuestion value) {
				if (value != null) {
					d.setQuestion(value);
				}
			}

			@Override
			public VQuestion getValue(Long key) {
				QuestionConvertOption questionConvertOption = new QuestionConvertOption(true, true, true, true, true,
						null);
				questionConvertOption.setInitQuestionTag(true);
				questionConvertOption.setInitPublishCount(true);
				questionConvertOption.setCollect(true);
				return qConvert.to(questionService.get(key), questionConvertOption);
			}

			@Override
			public Map<Long, VQuestion> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				Map<Long, Question> questions = questionService.mget(keys);
				QuestionConvertOption questionConvertOption = new QuestionConvertOption(true, true, true, true, true,
						null);
				questionConvertOption.setInitQuestionTag(true);
				questionConvertOption.setInitPublishCount(true);
				questionConvertOption.setCollect(true);
				return qConvert.to(questions, questionConvertOption);
			}
		});
	}

}
