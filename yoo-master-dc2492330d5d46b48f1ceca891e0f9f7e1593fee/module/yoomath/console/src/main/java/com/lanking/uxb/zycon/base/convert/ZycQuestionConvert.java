package com.lanking.uxb.zycon.base.convert;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.common.baseData.QuestionType;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.ChoiceFormat;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionSource;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.value.VKnowledgePoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.convert.MetaKnowpointConvert;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.SubjectConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.zycon.base.api.ZycAnswerService;
import com.lanking.uxb.zycon.base.api.ZycQuestionMetaKnowService;
import com.lanking.uxb.zycon.base.api.ZycQuestionService;
import com.lanking.uxb.zycon.base.api.ZycQuestionTypeService;
import com.lanking.uxb.zycon.base.value.CQuestion;

@Component
public class ZycQuestionConvert extends Converter<CQuestion, Question, Long> {

	@Autowired
	private ZycQuestionTypeConvert questionTypeConvert;
	@Autowired
	private ZycQuestionTypeService questionTypeService;
	@Autowired
	private ZycQuestionService questionService;
	@Autowired
	private ZycQuestionMetaKnowService questionMetaKnowService;
	@Autowired
	private ZycAnswerService answerService;
	@Autowired
	private ZycAnswerConvert answerConvert;
	@Autowired
	private TextbookCategoryService tcService;
	@Autowired
	private TextbookCategoryConvert tcConvert;
	@Autowired
	private MetaKnowpointConvert mkConvert;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private PhaseConvert phaseConvert;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SubjectConvert subjectConvert;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private KnowledgePointConvert knowledgePointConvert;
	@Autowired
	private KnowledgePointService knowledgePointService;

	@Override
	protected Long getId(Question s) {
		return s.getId();
	}

	public CQuestion to(Question s, boolean initSub, boolean isAnalysis, boolean isAnswer, Long studentHomeworkId) {
		s.setAnalysis(isAnalysis);
		s.setAnswer(isAnswer);
		s.setInitSub(initSub);
		if (studentHomeworkId != null) {
			s.setStudentHomeworkId(studentHomeworkId);
		}
		return super.to(s);
	}

	public List<CQuestion> to(List<Question> ss, boolean initSub, boolean isAnalysis, boolean isAnswer,
			Long studentHomeworkId) {
		for (Question s : ss) {
			s.setAnalysis(isAnalysis);
			s.setAnswer(isAnswer);
			s.setInitSub(initSub);
			if (studentHomeworkId != null) {
				s.setStudentHomeworkId(studentHomeworkId);
			}
		}
		return super.to(ss);
	}

	public Map<Long, CQuestion> to(Map<Long, Question> sMap, boolean initSub, boolean isAnalysis, boolean isAnswer,
			Long studentHomeworkId) {
		for (Question s : sMap.values()) {
			s.setAnalysis(isAnalysis);
			s.setAnswer(isAnswer);
			s.setInitSub(initSub);
			if (studentHomeworkId != null) {
				s.setStudentHomeworkId(studentHomeworkId);
			}
		}
		return super.to(sMap);
	}

	public Map<Long, CQuestion> toMap(List<Question> ss, boolean initSub, boolean isAnalysis, boolean isAnswer,
			Long studentHomeworkId) {
		for (Question s : ss) {
			s.setAnalysis(isAnalysis);
			s.setAnswer(isAnswer);
			s.setInitSub(initSub);
			if (studentHomeworkId != null) {
				s.setStudentHomeworkId(studentHomeworkId);
			}
		}
		return super.toMap(ss);
	}

	public void convert(Question s, CQuestion v) {
		v.setId(s.getId());
		v.setType(s.getType());

		String content = s.getContent();
		if (StringUtils.isNotBlank(content) && content.indexOf("<p>") == 0
				&& content.lastIndexOf("</p>") == content.length() - 4) {
			// 去除首尾的P标签
			content = content.substring(3, content.length() - 4);
		}

		v.setContent(content);
		v.setDifficulty(s.getDifficulty());
		v.setSource(StringUtils.defaultIfBlank(s.getSource()));
		v.setCode(StringUtils.defaultIfBlank(s.getCode()));
		if (s.isAnalysis()) {
			v.setAnalysis(validBlank(s.getAnalysis()));
		} else {
			v.setAnalysis(StringUtils.EMPTY);
		}
		v.setHint(validBlank(s.getHint()));
		v.setSubFlag(s.isSubFlag());
		v.setSequence(s.getSequence() == null ? 1 : s.getSequence());
		v.setParentId(s.getParentId() == null ? 0 : s.getParentId());

		// 选项
		List<String> choices = Lists.newArrayList();
		if (StringUtils.isNotBlank(s.getChoiceA())) {
			choices.add(s.getChoiceA());
		}
		if (StringUtils.isNotBlank(s.getChoiceB())) {
			choices.add(s.getChoiceB());
		}
		if (StringUtils.isNotBlank(s.getChoiceC())) {
			choices.add(s.getChoiceC());
		}
		if (StringUtils.isNotBlank(s.getChoiceD())) {
			choices.add(s.getChoiceD());
		}
		if (StringUtils.isNotBlank(s.getChoiceE())) {
			choices.add(s.getChoiceE());
		}
		if (StringUtils.isNotBlank(s.getChoiceF())) {
			choices.add(s.getChoiceF());
		}
		v.setChoices(choices);
		v.setAnswerNumber(s.getAnswerNumber());
		v.setCreateAt(s.getCreateAt());
		v.setUpdateAt(s.getUpdateAt());
		v.setCheckStatus(s.getStatus());

		v.setCorrectQuestion(s.isCorrectQuestion());
		if (s.getType() == Question.Type.SINGLE_CHOICE || s.getType() == Question.Type.MULTIPLE_CHOICE) {
			if (s.getChoiceFormat() != null) {
				v.setChoiceFormat(s.getChoiceFormat());
			} else {
				// 历史题目默认的排列方式
				if (choices.size() < 4) {
					v.setChoiceFormat(ChoiceFormat.HORIZONTAL);
				} else if (choices.size() == 4) {
					v.setChoiceFormat(ChoiceFormat.ABREAST);
				} else {
					v.setChoiceFormat(ChoiceFormat.VERTICAL);
				}
			}
		}
		v.setQuestionSource(s.getQuestionSource() == null ? QuestionSource.VENDOR : s.getQuestionSource());
	}

	@Override
	protected Map<Long, Question> internalMGet(Collection<Long> ids) {
		return questionService.mget(ids);
	}

	@Override
	protected CQuestion convert(Question s) {
		CQuestion v = new CQuestion();
		convert(s, v);
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 教材分类
		assemblers.add(new ConverterAssembler<CQuestion, Question, Integer, VTextbookCategory>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(Question s, CQuestion d) {
				return s.getTextbookCategoryCode();
			}

			@Override
			public void setValue(Question s, CQuestion d, VTextbookCategory value) {
				d.setTextbookCategory(value);
			}

			@Override
			public VTextbookCategory getValue(Integer key) {
				return key == null ? null : tcConvert.to(tcService.get(key));
			}

			@Override
			public Map<Integer, VTextbookCategory> mgetValue(Collection<Integer> keys) {
				return keys == null ? null : tcConvert.to(tcService.mget(keys));
			}
		});

		// 新知识点
		assemblers.add(new ConverterAssembler<CQuestion, Question, Long, List<VKnowledgePoint>>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, CQuestion d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, CQuestion d, List<VKnowledgePoint> value) {
				d.setNewKnowledgePoints(value);
			}

			@Override
			@SuppressWarnings("unchecked")
			public List<VKnowledgePoint> getValue(Long key) {
				List<Long> codes = questionKnowledgeService.queryKnowledgeByQuestionId(key);
				if (CollectionUtils.isEmpty(codes)) {
					return Collections.EMPTY_LIST;
				}
				List<KnowledgePoint> points = knowledgePointService.mgetList(codes);
				return knowledgePointConvert.to(points);
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, List<VKnowledgePoint>> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				Map<Long, List<VKnowledgePoint>> rmap = new HashMap<Long, List<VKnowledgePoint>>();
				Map<Long, List<Long>> map = questionKnowledgeService.mgetByQuestions(keys);
				for (Entry<Long, List<Long>> entry : map.entrySet()) {
					if (CollectionUtils.isEmpty(entry.getValue())) {
						rmap.put(entry.getKey(), Collections.EMPTY_LIST);
					} else {
						rmap.put(entry.getKey(),
								knowledgePointConvert.to(knowledgePointService.mgetList(entry.getValue())));
					}
				}
				return rmap;
			}
		});

		// 知识点
		assemblers.add(new ConverterAssembler<CQuestion, Question, Long, List<VMetaKnowpoint>>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, CQuestion d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, CQuestion d, List<VMetaKnowpoint> value) {
				d.setMetaKnowpoints(value);
			}

			@Override
			public List<VMetaKnowpoint> getValue(Long key) {
				return mkConvert.to(questionMetaKnowService.listByQuestion(key));
			}

			@Override
			public Map<Long, List<VMetaKnowpoint>> mgetValue(Collection<Long> keys) {
				Map<Long, List<VMetaKnowpoint>> rmap = new HashMap<Long, List<VMetaKnowpoint>>();
				Map<Long, List<MetaKnowpoint>> map = questionMetaKnowService.mListByQuestions(keys);
				for (Entry<Long, List<MetaKnowpoint>> entry : map.entrySet()) {
					rmap.put(entry.getKey(), mkConvert.to(entry.getValue()));
				}
				return rmap;
			}

		});
		// 阶段
		assemblers.add(new ConverterAssembler<CQuestion, Question, Integer, VPhase>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(Question s, CQuestion d) {
				return s.getPhaseCode();
			}

			@Override
			public void setValue(Question s, CQuestion d, VPhase value) {
				d.setPhase(value);
			}

			@Override
			public VPhase getValue(Integer key) {
				return phaseConvert.to(phaseService.get(key));
			}

			@Override
			public Map<Integer, VPhase> mgetValue(Collection<Integer> keys) {
				return phaseConvert.to(phaseService.mget(keys));
			}
		});
		// 学科
		assemblers.add(new ConverterAssembler<CQuestion, Question, Integer, VSubject>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(Question s, CQuestion d) {
				return s.getSubjectCode();
			}

			@Override
			public void setValue(Question s, CQuestion d, VSubject value) {
				d.setSubject(value);
			}

			@Override
			public VSubject getValue(Integer key) {
				return subjectConvert.to(subjectService.get(key));
			}

			@Override
			public Map<Integer, VSubject> mgetValue(Collection<Integer> keys) {
				return subjectConvert.to(subjectService.mget(keys));
			}
		});
		// 题目类型
		assemblers.add(new ConverterAssembler<CQuestion, Question, Integer, QuestionType>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(Question s, CQuestion d) {
				return s.getTypeCode();
			}

			@Override
			public void setValue(Question s, CQuestion d, QuestionType value) {
				d.setQuestionType(questionTypeConvert.to(value));
			}

			@Override
			public QuestionType getValue(Integer key) {
				if (key == null) {
					return null;
				}
				return questionTypeService.get(key);
			}

			@Override
			public Map<Integer, QuestionType> mgetValue(Collection<Integer> keys) {
				if (keys.isEmpty()) {
					return Maps.newHashMap();
				}
				return questionTypeService.mget(keys);
			}
		});
		// 子题
		assemblers.add(new ConverterAssembler<CQuestion, Question, Long, List<Question>>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, CQuestion d) {
				if (s.isSubFlag()) {
					return null;
				}
				return s.getId();
			}

			@Override
			public void setValue(Question s, CQuestion d, List<Question> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					for (Question q : value) {
						q.setInitSub(s.isInitSub());
						q.setAnalysis(s.isAnalysis());
						q.setAnswer(s.isAnswer());
						q.setStudentHomeworkId(s.getStudentHomeworkId());
					}
					d.setChildren(to(value));
				}
			}

			@Override
			public List<Question> getValue(Long key) {
				if (key == null) {
					return null;
				}
				return questionService.getSubQuestions(key);
			}

			@Override
			public Map<Long, List<Question>> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				Map<Long, List<Question>> map = questionService.mgetSubQuestions(keys);
				return map;
			}

		});
		// 答案
		assemblers.add(new ConverterAssembler<CQuestion, Question, Long, List<Answer>>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, CQuestion d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, CQuestion d, List<Answer> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					d.setAnswers(answerConvert.to(value));
				}
			}

			@Override
			public List<Answer> getValue(Long key) {
				if (key == null) {
					return null;
				}
				return answerService.getQuestionAnswers(key);
			}

			@Override
			public Map<Long, List<Answer>> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				return answerService.getQuestionAnswers(keys);
			}
		});
	}
}
