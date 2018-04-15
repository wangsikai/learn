package com.lanking.uxb.service.examPaper.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaper;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperQuestion;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStudent;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStudentQuestion;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperTopic;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.examPaper.api.CustomExampaperService;
import com.lanking.uxb.service.examPaper.api.CustomExampaperStudentService;
import com.lanking.uxb.service.examPaper.value.VCustomExampaperStudent;
import com.lanking.uxb.service.examPaper.value.VCustomExampaperStudentQuestion;
import com.lanking.uxb.service.examPaper.value.VCustomExampaperStudentTopic;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 学生组卷VO转换.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月12日
 */
@Component
public class CustomExampaperStudentConvert extends Converter<VCustomExampaperStudent, CustomExampaperStudent, Long> {
	@Autowired
	private ZyHomeworkClassService clazzService;
	@Autowired
	private ZyHomeworkClazzConvert clazzConvert;
	@Autowired
	private CustomExampaperService customExampaperService;
	@Autowired
	private CustomExampaperStudentService customExampaperStudentService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;

	@Override
	protected Long getId(CustomExampaperStudent s) {
		return s.getId();
	}

	@Override
	protected VCustomExampaperStudent convert(CustomExampaperStudent s) {
		if (s == null) {
			return null;
		}
		VCustomExampaperStudent vo = new VCustomExampaperStudent();
		vo.setId(s.getId());
		vo.setClassId(s.getClassId());
		vo.setCreateAt(s.getCreateAt());
		vo.setCustomExampaperId(s.getCustomExampaperId());
		vo.setStatisticsAt(s.getStatisticsAt());
		vo.setStatus(s.getStatus());
		vo.setStudentId(s.getStudentId());
		return vo;
	}

	public VCustomExampaperStudent to(CustomExampaperStudent s, CustomExampaperStudentConvertOption option) {
		s.setHasClazz(option.isHasClazz());
		s.setHasCustomExampaper(option.isHasCustomExampaper());
		s.setHasQuestion(option.isHasQuestion());
		return super.to(s);
	}

	public List<VCustomExampaperStudent> to(List<CustomExampaperStudent> ss, CustomExampaperStudentConvertOption option) {
		for (CustomExampaperStudent s : ss) {
			s.setHasClazz(option.isHasClazz());
			s.setHasCustomExampaper(option.isHasCustomExampaper());
			s.setHasQuestion(option.isHasQuestion());
		}
		return super.to(ss);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {

		// 组卷信息
		assemblers
				.add(new ConverterAssembler<VCustomExampaperStudent, CustomExampaperStudent, Long, CustomExampaper>() {

					@Override
					public boolean accept(CustomExampaperStudent s) {
						return s.isHasCustomExampaper();
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(CustomExampaperStudent s, VCustomExampaperStudent d) {
						return s.getCustomExampaperId();
					}

					@Override
					public void setValue(CustomExampaperStudent s, VCustomExampaperStudent d, CustomExampaper value) {
						if (value != null) {
							d.setName(value.getName());
							d.setType(value.getType());
							d.setTime(value.getTime());
							d.setScore(value.getScore());
							d.setQuestionCount(value.getQuestionCount());
							d.setDifficulty(value.getDifficulty());
							d.setCustomExampaperstatus(value.getStatus());
							d.setCustomExampaperCreateAt(value.getCreateAt());
							d.setOpenAt(value.getOpenAt());
						}
					}

					@Override
					public CustomExampaper getValue(Long key) {
						return customExampaperService.get(key);
					}

					@Override
					public Map<Long, CustomExampaper> mgetValue(Collection<Long> keys) {
						return customExampaperService.mget(keys);
					}
				});

		// 班级信息
		assemblers.add(new ConverterAssembler<VCustomExampaperStudent, CustomExampaperStudent, Long, VHomeworkClazz>() {

			@Override
			public boolean accept(CustomExampaperStudent s) {
				return s.isHasClazz();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CustomExampaperStudent s, VCustomExampaperStudent d) {
				return s.getClassId();
			}

			@Override
			public void setValue(CustomExampaperStudent s, VCustomExampaperStudent d, VHomeworkClazz value) {
				if (value != null) {
					d.setClazz(value);
				}
			}

			@Override
			public VHomeworkClazz getValue(Long key) {
				if (key == null) {
					return null;
				}
				HomeworkClazz clazz = clazzService.get(key);
				return clazzConvert.to(clazz);
			}

			@Override
			public Map<Long, VHomeworkClazz> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				Map<Long, HomeworkClazz> clazzs = clazzService.mget(keys);
				Map<Long, VHomeworkClazz> vclazzs = clazzConvert.to(clazzs);
				Map<Long, VHomeworkClazz> vs = new HashMap<Long, VHomeworkClazz>(keys.size());
				for (Long key : keys) {
					vs.put(key, vclazzs.get(key));
				}
				return vs;
			}
		});

		// 习题信息
		assemblers
				.add(new ConverterAssembler<VCustomExampaperStudent, CustomExampaperStudent, Long, List<VCustomExampaperStudentTopic>>() {

					@Override
					public boolean accept(CustomExampaperStudent s) {
						return s.isHasQuestion();
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(CustomExampaperStudent s, VCustomExampaperStudent d) {
						return s.getId();
					}

					@Override
					public void setValue(CustomExampaperStudent s, VCustomExampaperStudent d,
							List<VCustomExampaperStudentTopic> value) {
						d.setTopics(value);
					}

					@Override
					public List<VCustomExampaperStudentTopic> getValue(Long key) {
						// 分类列表.
						List<CustomExampaperTopic> topics = customExampaperStudentService
								.findCustomExampaperTopicByStudent(key);

						// 学生试卷习题列表
						List<CustomExampaperStudentQuestion> studentPaperQuestions = customExampaperStudentService
								.listStudentPaperQuestionByCustomExampaperStudentID(key);
						Map<Long, CustomExampaperStudentQuestion> studentPaperQuestionMap = new HashMap<Long, CustomExampaperStudentQuestion>(
								studentPaperQuestions.size());
						for (CustomExampaperStudentQuestion sq : studentPaperQuestions) {
							studentPaperQuestionMap.put(sq.getCustomExampaperQuestionId(), sq);
						}

						// 试卷习题列表
						List<CustomExampaperQuestion> paperQuestions = customExampaperStudentService
								.listPaperQuestionByCustomExampaperStudentID(key);
						Map<Long, List<CustomExampaperQuestion>> topicPaperQuestionMap = new HashMap<Long, List<CustomExampaperQuestion>>(
								topics.size());
						Set<Long> questionIds = new HashSet<Long>(paperQuestions.size());
						for (CustomExampaperQuestion pq : paperQuestions) {
							List<CustomExampaperQuestion> list = topicPaperQuestionMap.get(pq
									.getCustomExampaperTopicId());
							if (list == null) {
								list = new ArrayList<CustomExampaperQuestion>();
								topicPaperQuestionMap.put(pq.getCustomExampaperTopicId(), list);
							}
							questionIds.add(pq.getQuestionId());
							list.add(pq);
						}

						// 习题列表
						QuestionConvertOption option = new QuestionConvertOption();
						option.setInitExamination(true);
						option.setAnalysis(true);
						option.setAnswer(true);
						Map<Long, VQuestion> questionMap = questionConvert.to(questionService.mget(questionIds), option);

						// TOPIC组装
						List<VCustomExampaperStudentTopic> stuTopics = new ArrayList<VCustomExampaperStudentTopic>(
								topics.size());
						for (CustomExampaperTopic topic : topics) {
							List<CustomExampaperQuestion> pqs = topicPaperQuestionMap.get(topic.getId());
							if (pqs == null || pqs.size() == 0) {
								continue;
							}
							VCustomExampaperStudentTopic vTopic = new VCustomExampaperStudentTopic();
							vTopic.setId(topic.getId());
							vTopic.setName(topic.getName());
							vTopic.setSequence(topic.getSequence());
							vTopic.setType(topic.getType());
							vTopic.setQuestions(new ArrayList<VCustomExampaperStudentQuestion>());

							// 习题组装
							int totalScore = 0; // 总分
							int singleScore = 0; // 单题分
							for (CustomExampaperQuestion pq : pqs) {
								VCustomExampaperStudentQuestion sq = new VCustomExampaperStudentQuestion();
								CustomExampaperStudentQuestion cesq = studentPaperQuestionMap.get(pq.getId());

								sq.setId(cesq.getId());
								sq.setQuestionId(pq.getQuestionId());
								sq.setResult(cesq.getResult());
								sq.setDifficulty(pq.getDifficulty());
								sq.setAnswerCount(pq.getAnswerCount());
								sq.setQuestionType(pq.getQuestionType());
								sq.setScore(pq.getScore());
								sq.setSequence(pq.getSequence());
								sq.setQuestion(questionMap.get(pq.getQuestionId()));
								vTopic.getQuestions().add(sq);

								totalScore += pq.getScore();
								if (singleScore == 0) {
									singleScore = pq.getScore() / pq.getAnswerCount();
								}
							}

							vTopic.setTotalScore(totalScore);
							vTopic.setSingleScore(singleScore);
							stuTopics.add(vTopic);
						}
						return stuTopics;
					}

					@Override
					public Map<Long, List<VCustomExampaperStudentTopic>> mgetValue(Collection<Long> keys) {
						// 暂无场景需要列表显示习题信息
						return null;
					}
				});
	}
}
