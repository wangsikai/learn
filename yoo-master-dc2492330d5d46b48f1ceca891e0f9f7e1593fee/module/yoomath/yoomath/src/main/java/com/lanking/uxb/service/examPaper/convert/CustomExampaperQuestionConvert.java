package com.lanking.uxb.service.examPaper.convert;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.examPaper.value.VCustomExamPaperQuestion;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;

/**
 * 教师题目Convert
 *
 * @author zemin.song
 */
@Component
public class CustomExampaperQuestionConvert extends Converter<VCustomExamPaperQuestion, CustomExampaperQuestion, Long> {

	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;

	@Override
	protected Long getId(CustomExampaperQuestion s) {
		return s.getId();
	}

	@Override
	protected VCustomExamPaperQuestion convert(CustomExampaperQuestion s) {
		VCustomExamPaperQuestion vo = new VCustomExamPaperQuestion();
		vo.setId(s.getId());
		vo.setCreateAt(s.getCreateAt());
		vo.setSequence(s.getSequence());
		vo.setTopicId(s.getCustomExampaperTopicId());
		vo.setScore(s.getScore());
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VCustomExamPaperQuestion, CustomExampaperQuestion, Long, VQuestion>() {

			@Override
			public boolean accept(CustomExampaperQuestion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CustomExampaperQuestion s, VCustomExamPaperQuestion d) {
				return s.getQuestionId();
			}

			@Override
			public void setValue(CustomExampaperQuestion s, VCustomExamPaperQuestion d, VQuestion value) {
				d.setQuestion(value);
			}

			@Override
			public VQuestion getValue(Long key) {
				QuestionConvertOption option = new QuestionConvertOption();
				option.setAnalysis(true);
				option.setInitExamination(true);
				option.setAnswer(true);
				option.setInitQuestionTag(true); // 标签
				return questionConvert.to(questionService.get(key), option);
			}

			@Override
			public Map<Long, VQuestion> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				QuestionConvertOption option = new QuestionConvertOption();
				option.setAnalysis(true);
				option.setInitExamination(true);
				option.setAnswer(true);
				option.setInitQuestionTag(true); // 标签
				return questionConvert.to(questionService.mget(keys), option);
			}

		});
	}

}
