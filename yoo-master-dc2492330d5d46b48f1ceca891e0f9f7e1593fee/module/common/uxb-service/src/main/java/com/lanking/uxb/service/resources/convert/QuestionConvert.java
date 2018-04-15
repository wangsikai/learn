package com.lanking.uxb.service.resources.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.common.baseData.QuestionTag;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.ChoiceFormat;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question2Tag;
import com.lanking.cloud.domain.common.resource.question.QuestionExaminationPoint;
import com.lanking.cloud.domain.common.resource.question.QuestionSimilar;
import com.lanking.cloud.domain.common.resource.question.QuestionSource;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.yoo.collection.QuestionCollection;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.api.ExaminationPointService;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.code.api.QuestionTagService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.convert.ExaminationPointConvert;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.convert.MetaKnowpointConvert;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.SubjectConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.value.VExaminationPoint;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.service.counter.api.impl.QuestionUserCouterProvider;
import com.lanking.uxb.service.question.api.AnswerService;
import com.lanking.uxb.service.question.api.Question2TagService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.question.util.QuestionUtils;
import com.lanking.uxb.service.resources.api.QuestionExaminationPointService;
import com.lanking.uxb.service.resources.api.QuestionMetaKnowService;
import com.lanking.uxb.service.resources.api.QuestionSimilarService;
import com.lanking.uxb.service.resources.api.QuestionTypeService;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerService;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;
import com.lanking.uxb.service.resources.value.VAnswer;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.resources.value.VQuestionTag;
import com.lanking.uxb.service.resources.value.VQuestionType;
import com.lanking.uxb.service.resources.value.VStudentHomeworkAnswer;
import com.lanking.uxb.service.resources.value.VStudentHomeworkQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCollectionService;

@Component
public class QuestionConvert extends Converter<VQuestion, Question, Long> {

	@Autowired
	private QuestionTypeConvert questionTypeConvert;
	@Autowired
	private QuestionTypeService questionTypeService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionMetaKnowService questionMetaKnowService;
	@Autowired
	private AnswerService answerService;
	@Autowired
	private AnswerConvert answerConvert;
	@Autowired
	private StudentHomeworkQuestionService shqService;
	@Autowired
	private StudentHomeworkQuestionConvert shqConvert;
	@Autowired
	private StudentHomeworkAnswerService shaService;
	@Autowired
	private StudentHomeworkAnswerConvert shaConvert;
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
	private ZyQuestionCollectionService zyQuestionCollectionService;
	@Autowired
	private ExaminationPointService examinationPointService;
	@Autowired
	private ExaminationPointConvert examinationPointConvert;
	@Autowired
	private QuestionExaminationPointService qeService;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private KnowledgePointConvert knowledgePointConvert;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private StudentQuestionAnswerService studentQuestionAnswerService;
	@Autowired
	private QuestionUserCouterProvider questionUserCouterProvider;
	@Autowired
	private QuestionSimilarService questionSimilarService;
	@Autowired
	private Question2TagService question2TagService;
	@Autowired
	private QuestionTagConvert questionTagConvert;
	@Autowired
	private QuestionTagService questionTagService;

	@Override
	protected Long getId(Question s) {
		return s.getId();
	}

	@Override
	public VQuestion to(Question s) {
		return super.to(s);
	}

	@Override
	public List<VQuestion> to(List<Question> ss) {
		return super.to(ss);
	}

	@Override
	public Map<Long, VQuestion> to(Map<Long, Question> sMap) {
		return super.to(sMap);
	}

	@Override
	public Page<VQuestion> to(Page<Question> p) {
		return super.to(p);
	}

	@Override
	public Map<Long, VQuestion> toMap(List<Question> ss) {
		return super.toMap(ss);
	}

	public VQuestion to(Question s, QuestionConvertOption option) {
		s.setAnalysis(option.isAnalysis());
		s.setAnswer(option.isAnswer());
		s.setInitSub(option.isInitSub());
		s.setCollect(option.isCollect());
		s.setInitExamination(option.isInitExamination());
		if (option.getStudentHomeworkId() != null) {
			s.setStudentHomeworkId(option.getStudentHomeworkId());
		}

		// 2017-5-24 新加配置
		s.setInitTextbookCategory(option.isInitTextbookCategory());
		s.setInitKnowledgePoint(option.isInitKnowledgePoint());
		s.setInitMetaKnowpoint(option.isInitMetaKnowpoint());
		s.setInitPhase(option.isInitPhase());
		s.setInitSubject(option.isInitSubject());
		s.setInitQuestionType(option.isInitQuestionType());
		s.setInitStudentQuestionCount(option.isInitStudentQuestionCount());

		// 教师端v1.3.0新加配置 2017-7-3
		s.setInitPublishCount(option.isInitPublishCount());
		s.setInitQuestionSimilarCount(option.isInitQuestionSimilarCount());
		s.setInitQuestionTag(option.isInitQuestionTag());
		return super.to(s);
	}

	public List<VQuestion> to(List<Question> ss, QuestionConvertOption option) {
		for (Question s : ss) {
			s.setAnalysis(option.isAnalysis());
			s.setAnswer(option.isAnswer());
			s.setInitSub(option.isInitSub());
			s.setCollect(option.isCollect());
			s.setInitExamination(option.isInitExamination());
			if (option.getStudentHomeworkId() != null) {
				s.setStudentHomeworkId(option.getStudentHomeworkId());
			}

			// 2017-5-24 新加配置
			s.setInitTextbookCategory(option.isInitTextbookCategory());
			s.setInitKnowledgePoint(option.isInitKnowledgePoint());
			s.setInitMetaKnowpoint(option.isInitMetaKnowpoint());
			s.setInitPhase(option.isInitPhase());
			s.setInitSubject(option.isInitSubject());
			s.setInitQuestionType(option.isInitQuestionType());
			s.setInitStudentQuestionCount(option.isInitStudentQuestionCount());

			// 教师端v1.3.0新加配置 2017-7-3
			s.setInitPublishCount(option.isInitPublishCount());
			s.setInitQuestionSimilarCount(option.isInitQuestionSimilarCount());
			s.setInitQuestionTag(option.isInitQuestionTag());
		}
		return super.to(ss);
	}

	public Map<Long, VQuestion> to(Map<Long, Question> sMap, QuestionConvertOption option) {
		for (Question s : sMap.values()) {
			s.setAnalysis(option.isAnalysis());
			s.setAnswer(option.isAnswer());
			s.setInitSub(option.isInitSub());
			s.setCollect(option.isCollect());
			s.setInitExamination(option.isInitExamination());
			if (option.getStudentHomeworkId() != null) {
				s.setStudentHomeworkId(option.getStudentHomeworkId());
			}

			// 2017-5-24 新加配置
			s.setInitTextbookCategory(option.isInitTextbookCategory());
			s.setInitKnowledgePoint(option.isInitKnowledgePoint());
			s.setInitMetaKnowpoint(option.isInitMetaKnowpoint());
			s.setInitPhase(option.isInitPhase());
			s.setInitSubject(option.isInitSubject());
			s.setInitQuestionType(option.isInitQuestionType());
			s.setInitStudentQuestionCount(option.isInitStudentQuestionCount());

			// 教师端v1.3.0新加配置 2017-7-3
			s.setInitPublishCount(option.isInitPublishCount());
			s.setInitQuestionSimilarCount(option.isInitQuestionSimilarCount());
			s.setInitQuestionTag(option.isInitQuestionTag());
		}
		return super.to(sMap);
	}

	public Map<Long, VQuestion> toMap(List<Question> ss, QuestionConvertOption option) {
		for (Question s : ss) {
			s.setAnalysis(option.isAnalysis());
			s.setAnswer(option.isAnswer());
			s.setInitSub(option.isInitSub());
			s.setCollect(option.isCollect());
			s.setInitExamination(option.isInitExamination());
			if (option.getStudentHomeworkId() != null) {
				s.setStudentHomeworkId(option.getStudentHomeworkId());
			}

			// 2017-5-24 新加配置
			s.setInitTextbookCategory(option.isInitTextbookCategory());
			s.setInitKnowledgePoint(option.isInitKnowledgePoint());
			s.setInitMetaKnowpoint(option.isInitMetaKnowpoint());
			s.setInitPhase(option.isInitPhase());
			s.setInitSubject(option.isInitSubject());
			s.setInitQuestionType(option.isInitQuestionType());
			s.setInitStudentQuestionCount(option.isInitStudentQuestionCount());

			// 教师端v1.3.0新加配置 2017-7-3
			s.setInitPublishCount(option.isInitPublishCount());
			s.setInitQuestionSimilarCount(option.isInitQuestionSimilarCount());
			s.setInitQuestionTag(option.isInitQuestionTag());
		}
		return super.toMap(ss);
	}

	@Override
	protected Question internalGet(Long id) {
		return questionService.get(id);
	}

	@Override
	protected Map<Long, Question> internalMGet(Collection<Long> ids) {
		return questionService.mget(ids);
	}

	public void convert(Question s, VQuestion v) {
		v.setId(s.getId());
		v.setType(s.getType());
		v.setContent(this.delPContent(s.getContent()));
		v.setDifficulty(s.getDifficulty());
		v.setSource(StringUtils.defaultIfBlank(s.getSource()));
		v.setCode(StringUtils.defaultIfBlank(s.getCode()));
		if (s.isAnalysis()) {
			v.setAnalysis(this.delPContent(validBlank(s.getAnalysis())));
		} else {
			if (Security.isClient() && Security.getUserType() == UserType.STUDENT) {
				v.setAnalysis("请升级到最新版本后查看题目解析");
			} else {
				v.setAnalysis(StringUtils.EMPTY);
			}
		}
		v.setHint(this.delPContent(validBlank(s.getHint())));
		v.setSubFlag(s.isSubFlag());
		v.setSequence(s.getSequence() == null ? 1 : s.getSequence());
		v.setParentId(s.getParentId() == null ? 0 : s.getParentId());

		// 选项
		List<String> choices = Lists.newArrayList();
		if (StringUtils.isNotBlank(s.getChoiceA())) {
			choices.add(this.delPContent(s.getChoiceA()));
		}
		if (StringUtils.isNotBlank(s.getChoiceB())) {
			choices.add(this.delPContent(s.getChoiceB()));
		}
		if (StringUtils.isNotBlank(s.getChoiceC())) {
			choices.add(this.delPContent(s.getChoiceC()));
		}
		if (StringUtils.isNotBlank(s.getChoiceD())) {
			choices.add(this.delPContent(s.getChoiceD()));
		}
		if (StringUtils.isNotBlank(s.getChoiceE())) {
			choices.add(this.delPContent(s.getChoiceE()));
		}
		if (StringUtils.isNotBlank(s.getChoiceF())) {
			choices.add(this.delPContent(s.getChoiceF()));
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
		// 为客户端处理
		if (Security.isClient() && !Security.isWebView()) {
			v.setContent(QuestionUtils.process(v.getContent(), null, true));
			v.setHint(QuestionUtils.process(v.getHint(), null, true));
			v.setAnalysis(QuestionUtils.process(v.getAnalysis(), null, true));
			int index = 0;
			for (String c : v.getChoices()) {
				v.getChoices().set(index, QuestionUtils.process(c, null, true));
				index++;
			}
		}

		// since 2.3.0 题目标签
		v.setCategoryTypes(s.getCategoryTypes());
		v.setSubjectCode(s.getSubjectCode());
	}

	@Override
	protected VQuestion convert(Question s) {
		VQuestion v = new VQuestion();
		convert(s, v);
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 教材分类
		assemblers.add(new ConverterAssembler<VQuestion, Question, Integer, VTextbookCategory>() {
			@Override
			public boolean accept(Question s) {
				return s.isInitTextbookCategory();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(Question s, VQuestion d) {
				return s.getTextbookCategoryCode();
			}

			@Override
			public void setValue(Question s, VQuestion d, VTextbookCategory value) {
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
		// 旧知识点
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, List<VMetaKnowpoint>>() {
			@Override
			public boolean accept(Question s) {
				return s.isInitMetaKnowpoint();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, VQuestion d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, VQuestion d, List<VMetaKnowpoint> value) {
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
		assemblers.add(new ConverterAssembler<VQuestion, Question, Integer, VPhase>() {
			@Override
			public boolean accept(Question s) {
				if (!s.isInitPhase()) {
					return false;
				}
				if (s.getStatus() == CheckStatus.DRAFT && s.getPhaseCode() == null) {
					return false;
				}
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(Question s, VQuestion d) {
				return s.getPhaseCode();
			}

			@Override
			public void setValue(Question s, VQuestion d, VPhase value) {
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
		assemblers.add(new ConverterAssembler<VQuestion, Question, Integer, VSubject>() {
			@Override
			public boolean accept(Question s) {
				if (!s.isInitSubject()) {
					return false;
				}
				if (s.getStatus() == CheckStatus.DRAFT && s.getSubjectCode() == null) {
					return false;
				}
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(Question s, VQuestion d) {
				return s.getSubjectCode();
			}

			@Override
			public void setValue(Question s, VQuestion d, VSubject value) {
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
		assemblers.add(new ConverterAssembler<VQuestion, Question, Integer, VQuestionType>() {
			@Override
			public boolean accept(Question s) {
				if (!s.isInitQuestionType()) {
					return false;
				}
				if (s.getStatus() == CheckStatus.DRAFT && s.getTypeCode() == null) {
					return false;
				}
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(Question s, VQuestion d) {
				return s.getTypeCode();
			}

			@Override
			public void setValue(Question s, VQuestion d, VQuestionType value) {
				d.setQuestionType(value);
			}

			@Override
			public VQuestionType getValue(Integer key) {
				if (key == null) {
					return null;
				}
				return questionTypeConvert.to(questionTypeService.get(key));
			}

			@Override
			public Map<Integer, VQuestionType> mgetValue(Collection<Integer> keys) {
				if (keys.isEmpty()) {
					return Maps.newHashMap();
				}
				return questionTypeConvert.to(questionTypeService.mget(keys));
			}
		});

		// 子题
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, List<Question>>() {
			@Override
			public boolean accept(Question s) {
				return s.isInitSub() && s.getType() == Question.Type.COMPOSITE;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, VQuestion d) {
				if (s.isSubFlag()) {
					return null;
				}
				return s.getId();
			}

			@Override
			public void setValue(Question s, VQuestion d, List<Question> value) {
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

			@SuppressWarnings("unchecked")
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
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, List<VAnswer>>() {
			@Override
			public boolean accept(Question s) {
				return s.isAnswer();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, VQuestion d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, VQuestion d, List<VAnswer> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					d.setAnswers(value);
				}
			}

			@Override
			public List<VAnswer> getValue(Long key) {
				if (key == null) {
					return null;
				}
				return answerConvert.to(answerService.getQuestionAnswers(key));
			}

			@Override
			public Map<Long, List<VAnswer>> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				} else {
					Map<Long, List<Answer>> answers = answerService.getQuestionAnswers(keys);
					Map<Long, List<VAnswer>> vanswers = new HashMap<Long, List<VAnswer>>(answers.size());
					for (Entry<Long, List<Answer>> entry : answers.entrySet()) {
						vanswers.put(entry.getKey(), answerConvert.to(entry.getValue()));
					}
					return vanswers;
				}
			}
		});

		// StudentHomeworkQuestion转换
//		assemblers.add(new ConverterAssembler<VQuestion, Question, String, VStudentHomeworkQuestion>() {
//
//			@Override
//			public boolean accept(Question question) {
//				return question.getStudentHomeworkId() != null && question.getStudentHomeworkId() > 0;
//			}
//
//			@Override
//			public boolean accept(Map<String, Object> hints) {
//				return true;
//			}
//
//			@Override
//			public String getKey(Question s, VQuestion vQuestion) {
//				return s.getId() + "_" + s.getStudentHomeworkId();
//			}
//
//			@Override
//			public void setValue(Question question, VQuestion vQuestion, VStudentHomeworkQuestion value) {
//				if (value != null) {
//					vQuestion.setStudentHomeworkQuestion(value);
//				}
//			}
//
//			@Override
//			public VStudentHomeworkQuestion getValue(String key) {
//				if (StringUtils.isBlank(key)) {
//					return null;
//				}
//				String[] idArr = key.split("_");
//				StudentHomeworkQuestion studentHomeworkQuestion = shqService.find(Long.parseLong(idArr[1]),
//						Long.parseLong(idArr[0]));
//				return shqConvert.to(studentHomeworkQuestion);
//			}
//
//			@Override
//			public Map<String, VStudentHomeworkQuestion> mgetValue(Collection<String> keys) {
//				if (CollectionUtils.isEmpty(keys)) {
//					return Maps.newHashMap();
//				}
//				Set<Long> ids = Sets.newHashSet();
//				Long studentHomeworkId = null;
//				for (String key : keys) {
//					String[] idArr = key.split("_");
//					if (studentHomeworkId == null) {
//						studentHomeworkId = Long.parseLong(idArr[1]);
//					}
//					ids.add(Long.parseLong(idArr[0]));
//				}
//				List<StudentHomeworkQuestion> ps = shqService.find(studentHomeworkId, ids);
//				List<VStudentHomeworkQuestion> vps = shqConvert.to(ps);
//				Map<String, VStudentHomeworkQuestion> map = Maps.newHashMap();
//				for (VStudentHomeworkQuestion p : vps) {
//					map.put(p.getQuestionId() + "_" + p.getStudentHomeworkId(), p);
//				}
//
//				return map;
//			}
//		});
		
		// StudentHomeworkQuestion转换
		assemblers.add(new ConverterAssembler<VQuestion, Question, String, List<VStudentHomeworkQuestion>>() {

			@Override
			public boolean accept(Question question) {
				return question.getStudentHomeworkId() != null && question.getStudentHomeworkId() > 0;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public String getKey(Question s, VQuestion vQuestion) {
				return s.getId() + "_" + s.getStudentHomeworkId();
			}

			@Override
			public void setValue(Question question, VQuestion vQuestion, List<VStudentHomeworkQuestion> value) {
				if (value != null && value.size()>0) {
					for(VStudentHomeworkQuestion v:value){
						if(v.isNewCorrect()){
							vQuestion.setCorrectStudentHomeworkQuestion(v);
						}else{
							vQuestion.setStudentHomeworkQuestion(v);
						}
					}
				}
			}

			@Override
			public List<VStudentHomeworkQuestion> getValue(String key) {
				if (StringUtils.isBlank(key)) {
					return null;
				}
				String[] idArr = key.split("_");
				List<StudentHomeworkQuestion> sthkqList = shqService.queryStuQuestions(Long.parseLong(idArr[1]),
						Long.parseLong(idArr[0]),null,false);
				return shqConvert.to(sthkqList);
			}

			@Override
			public Map<String, List<VStudentHomeworkQuestion>> mgetValue(Collection<String> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				Set<Long> ids = Sets.newHashSet();
				Long studentHomeworkId = null;
				for (String key : keys) {
					String[] idArr = key.split("_");
					if (studentHomeworkId == null) {
						studentHomeworkId = Long.parseLong(idArr[1]);
					}
					ids.add(Long.parseLong(idArr[0]));
				}
				List<StudentHomeworkQuestion> ps = shqService.queryStuQuestions(studentHomeworkId,-1, ids,false);
				List<VStudentHomeworkQuestion> vps = shqConvert.to(ps);
				Map<String, List<VStudentHomeworkQuestion>> map = Maps.newHashMap();
				for (VStudentHomeworkQuestion p : vps) {
					if(null==map.get(p.getQuestionId() + "_" + p.getStudentHomeworkId())){
						map.put(p.getQuestionId() + "_" + p.getStudentHomeworkId(), new ArrayList<VStudentHomeworkQuestion>());
					}
					map.get(p.getQuestionId() + "_" + p.getStudentHomeworkId()).add(p);
				}
				return map;
			}
		});
		// 学生作业答案
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, List<VStudentHomeworkAnswer>>() {
			@Override
			public boolean accept(Question s) {
				return s.getStudentHomeworkId() != null && s.getStudentHomeworkId() > 0;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, VQuestion d) {
				if (d.getStudentHomeworkQuestion() == null) {
					return null;
				}
				return d.getStudentHomeworkQuestion().getId();
			}

			@Override
			public void setValue(Question s, VQuestion d, List<VStudentHomeworkAnswer> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					d.setStudentHomeworkAnswers(value);
				}
			}

			@Override
			public List<VStudentHomeworkAnswer> getValue(Long key) {
				if (key == null) {
					return null;
				}
				return shaConvert.to(shaService.find(key));
			}

			@Override
			public Map<Long, List<VStudentHomeworkAnswer>> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				} else {
					Map<Long, List<StudentHomeworkAnswer>> answers = shaService.find(keys);
					Map<Long, List<VStudentHomeworkAnswer>> vanswers = new HashMap<Long, List<VStudentHomeworkAnswer>>(
							answers.size());
					for (Entry<Long, List<StudentHomeworkAnswer>> entry : answers.entrySet()) {
						vanswers.put(entry.getKey(), shaConvert.to(entry.getValue()));
					}
					return vanswers;
				}
			}
		});

		// 题目是否收藏
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, QuestionCollection>() {

			@Override
			public boolean accept(Question s) {
				return s.isCollect();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, VQuestion d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, VQuestion d, QuestionCollection value) {
				if (value != null) {
					d.setIsCollect(true);
				}
			}

			@Override
			public QuestionCollection getValue(Long key) {
				return zyQuestionCollectionService.get(key, Security.getUserId());
			}

			@Override
			public Map<Long, QuestionCollection> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				Map<Long, QuestionCollection> qcMap = zyQuestionCollectionService.mget(keys, Security.getUserId());
				for (Long key : keys) {
					if (!qcMap.containsKey(key)) {
						qcMap.put(key, null);
					}
				}
				return qcMap;
			}
		});

		// 处理题目对应的考点 since 2.3.0
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, List<VExaminationPoint>>() {

			@Override
			public boolean accept(Question question) {
				return question.isInitExamination();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question question, VQuestion vQuestion) {
				return question.getId();
			}

			@Override
			public void setValue(Question question, VQuestion vQuestion, List<VExaminationPoint> value) {
				vQuestion.setExaminationPoints(value);
			}

			@Override
			public List<VExaminationPoint> getValue(Long key) {
				List<QuestionExaminationPoint> qes = qeService.findByQuestion(key);
				List<Long> ids = new ArrayList<Long>(qes.size());
				for (QuestionExaminationPoint qe : qes) {
					ids.add(qe.getExaminationPointCode());
				}

				return examinationPointConvert.to(examinationPointService.mgetList(ids));
			}

			@Override
			public Map<Long, List<VExaminationPoint>> mgetValue(Collection<Long> keys) {
				List<QuestionExaminationPoint> qes = qeService.findByQuestions(keys);
				Set<Long> ids = new HashSet<Long>(qes.size());
				Map<Long, List<VExaminationPoint>> retMap = new HashMap<Long, List<VExaminationPoint>>(keys.size());
				for (QuestionExaminationPoint qe : qes) {
					ids.add(qe.getExaminationPointCode());
				}

				Map<Long, ExaminationPoint> eMap = examinationPointService.mget(ids);
				Map<Long, VExaminationPoint> vsMap = examinationPointConvert.to(eMap);
				for (QuestionExaminationPoint qe : qes) {
					List<VExaminationPoint> vs = retMap.get(qe.getQuestionId());
					if (vs == null) {
						vs = Lists.newArrayList();
					}

					vs.add(vsMap.get(qe.getExaminationPointCode()));

					retMap.put(qe.getQuestionId(), vs);
				}
				return retMap;
			}
		});

		// 新知识点
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, List<VKnowledgePoint>>() {
			@Override
			public boolean accept(Question s) {
				return s.isInitKnowledgePoint();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, VQuestion d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, VQuestion d, List<VKnowledgePoint> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					d.setNewKnowpoints(value);
				}
			}

			@Override
			public List<VKnowledgePoint> getValue(Long key) {
				if (key == null) {
					return null;
				}
				List<Long> kpCodes = questionKnowledgeService.queryKnowledgeByQuestionId(key);
				if (CollectionUtils.isNotEmpty(kpCodes)) {
					return knowledgePointConvert.to(knowledgePointService.mgetList(kpCodes));
				} else {
					return null;
				}
			}

			@Override
			public Map<Long, List<VKnowledgePoint>> mgetValue(Collection<Long> keys) {
				Map<Long, List<Long>> kpCodeMap = questionKnowledgeService.mgetByQuestions(keys);
				Set<Long> knowledgePointCodes = Sets.newHashSet();
				for (Entry<Long, List<Long>> entry : kpCodeMap.entrySet()) {
					for (Long code : entry.getValue()) {
						knowledgePointCodes.add(code);
					}
				}

				Map<Long, List<VKnowledgePoint>> vkpsMap = new HashMap<Long, List<VKnowledgePoint>>();
				if (CollectionUtils.isEmpty(knowledgePointCodes)) {
					return vkpsMap;
				}

				Map<Long, VKnowledgePoint> vkpMap = knowledgePointConvert
						.to(knowledgePointService.mget(knowledgePointCodes));

				for (Entry<Long, List<Long>> entry : kpCodeMap.entrySet()) {
					List<VKnowledgePoint> vkps = new ArrayList<VKnowledgePoint>();
					for (Long code : entry.getValue()) {
						VKnowledgePoint vkp = vkpMap.get(code);
						if (vkp != null) {
							vkps.add(vkp);
						}
					}
					vkpsMap.put(entry.getKey(), vkps);
				}

				return vkpsMap;
			}
		});

		// 学生做对数量以及题目所有人做题情况
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, Map>() {

			@Override
			public boolean accept(Question question) {
				return question.isInitStudentQuestionCount() && Security.getUserType() == UserType.STUDENT;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question question, VQuestion vQuestion) {
				return question.getId();
			}

			@Override
			public void setValue(Question question, VQuestion vQuestion, Map value) {
				if (value != null && value.size() != 0) {
					BigDecimal doCountVal = (BigDecimal) value.get("do_count");
					BigDecimal wrongCountVal = (BigDecimal) value.get("wrong_count");
					BigDecimal wrongPeopleVal = (BigDecimal) value.get("wrong_people");
					Long doCount = doCountVal == null ? 0 : doCountVal.longValue();
					Long wrongCount = wrongCountVal == null ? 0 : wrongCountVal.longValue();
					if (doCount > 0) {
						Double rightRate = (doCount - wrongCount) * 100d / doCount;
						BigDecimal rightRateDecimal = new BigDecimal(rightRate).setScale(0,BigDecimal.ROUND_HALF_UP);
						vQuestion.setQuestionRightRate(rightRateDecimal.doubleValue());
					} else {
						vQuestion.setQuestionRightRate(0d);
					}
					vQuestion.setDoCount(doCount);
					Long wrongPeople = wrongPeopleVal == null ? 0 : wrongPeopleVal.longValue();
					vQuestion.setWrongPeopleCount(wrongPeople);
				} else {
					vQuestion.setDoCount(0L);
					vQuestion.setQuestionRightRate(0d);
					vQuestion.setWrongPeopleCount(0L);
				}
			}

			@Override
			public Map getValue(Long key) {

				List<Long> questionIds = new ArrayList<Long>(1);
				questionIds.add(key);

				List<Map> results = studentQuestionAnswerService.findStudentCondition(Security.getUserId(),
						questionIds);
				return results == null || results.size() == 0 ? null : results.get(0);
			}

			@Override
			public Map<Long, Map> mgetValue(Collection<Long> keys) {
				List<Map> results = studentQuestionAnswerService.findStudentCondition(Security.getUserId(), keys);
				Map<Long, Map> retMap = new HashMap<Long, Map>(results.size());

				for (Map m : results) {
					Long questionId = ((BigInteger) m.get("question_id")).longValue();
					retMap.put(questionId, m);
				}

				return retMap;
			}
		});

		// 布置过作业次数 @since 教师端v1.3.0
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, Long>() {
			@Override
			public boolean accept(Question s) {
				return s.isInitPublishCount();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, VQuestion d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, VQuestion d, Long value) {
				d.setPublishCount(value);

			}

			@Override
			public Long getValue(Long key) {
				return questionUserCouterProvider.getQuestionPublishCount(key, Security.getUserId());
			}

			@Override
			public Map<Long, Long> mgetValue(Collection<Long> keys) {
				return questionUserCouterProvider.mgetQuestionPublishCount(keys, Security.getUserId());
			}
		});

		// 相似题 @since 教师端v1.3.0
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, Long>() {
			@Override
			public boolean accept(Question s) {
				return s.isInitQuestionSimilarCount();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, VQuestion d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, VQuestion d, Long value) {
				d.setQuestionSimilarCount(value);
			}

			@Override
			public Long getValue(Long key) {
				QuestionSimilar questionSimilar = questionSimilarService.getByQuestion(key);
				if (questionSimilar != null) {
					long count = questionSimilar.getShowCount();
					return count > 0 ? count - 1 : 0L;
				} else {
					return 0L;
				}
			}

			@Override
			public Map<Long, Long> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}

				Map<Long, QuestionSimilar> questionSimilarMap = questionSimilarService.mGetByQuestion(keys);

				Map<Long, Long> retMap = new HashMap<Long, Long>();
				for (Long key : keys) {
					QuestionSimilar questionSimilar = questionSimilarMap.get(key);
					if (questionSimilar != null) {
						long count = questionSimilar.getShowCount();
						retMap.put(key, count > 0 ? count - 1 : 0L);
					} else {
						retMap.put(key, 0L);
					}
				}
				return retMap;
			}
		});

		// 题目标签 @since 教师端v1.3.0
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, List<VQuestionTag>>() {
			@Override
			public boolean accept(Question s) {
				return s.isInitQuestionTag();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, VQuestion d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, VQuestion d, List<VQuestionTag> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					d.setQuestionTags(value);
				}
			}

			@Override
			public List<VQuestionTag> getValue(Long key) {
				// 1.先取题目和标签对应关系
				List<Question2Tag> question2TagList = question2TagService.getByQuestionId(key);
				if (CollectionUtils.isNotEmpty(question2TagList)) {
					List<Long> tagCodeList = Lists.newArrayList();
					for (Question2Tag tag : question2TagList) {
						tagCodeList.add(tag.getTagCode());
					}
					// 2.根据tagCode去QuestionTag表查询
					List<QuestionTag> tagList = questionTagService.getQuestionTag(tagCodeList);
					return questionTagConvert.to(tagList);
				}
				return Lists.newArrayList();
			}

			@Override
			public Map<Long, List<VQuestionTag>> mgetValue(Collection<Long> keys) {
				Map<Long, List<VQuestionTag>> data = new HashMap<Long, List<VQuestionTag>>();
				Map<Long, List<Question2Tag>> question2TagMap = question2TagService.mgetByQuestionIds(keys);
				Set<Long> tagCodes = new HashSet<Long>();
				for (List<Question2Tag> question2Tags : question2TagMap.values()) {
					for (Question2Tag question2Tag : question2Tags) {
						tagCodes.add(question2Tag.getTagCode());
					}
				}
				if (tagCodes.size() == 0) {
					return Maps.newHashMap();
				}

				Map<Long, VQuestionTag> questionTagMap = questionTagConvert.to(questionTagService.mget(tagCodes));

				for (Long key : keys) {
					List<Question2Tag> question2Tags = question2TagMap.get(key);
					List<VQuestionTag> questionTags = new ArrayList<VQuestionTag>(question2Tags.size());
					for (Question2Tag question2Tag : question2Tags) {
						questionTags.add(questionTagMap.get(question2Tag.getTagCode()));
					}

					// 按照sequence排序
					Collections.sort(questionTags, new Comparator<VQuestionTag>() {
						@Override
						public int compare(VQuestionTag q1, VQuestionTag q2) {
							if (q1.getSequence().intValue() < q2.getSequence().intValue()) {
								return -1;
							} else if (q1.getSequence().intValue() > q2.getSequence().intValue()) {
								return 1;
							}
							return 0;
						}
					});

					data.put(key, questionTags);
				}
				return data;
			}
		});
		// 学生作业订正答案
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, List<VStudentHomeworkAnswer>>() {
			@Override
			public boolean accept(Question s) {
				return s.getStudentHomeworkId() != null && s.getStudentHomeworkId() > 0;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, VQuestion d) {
				if (d.getCorrectStudentHomeworkQuestion() == null) {
					return null;
				}
				return d.getCorrectStudentHomeworkQuestion().getId();
			}

			@Override
			public void setValue(Question s, VQuestion d, List<VStudentHomeworkAnswer> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					d.setCorrectStudentHomeworkAnswers(value);
				}
			}

			@Override
			public List<VStudentHomeworkAnswer> getValue(Long key) {
				if (key == null) {
					return null;
				}
				return shaConvert.to(shaService.find(key));
			}

			@Override
			public Map<Long, List<VStudentHomeworkAnswer>> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				} else {
					Map<Long, List<StudentHomeworkAnswer>> answers = shaService.find(keys);
					Map<Long, List<VStudentHomeworkAnswer>> vanswers = new HashMap<Long, List<VStudentHomeworkAnswer>>(
							answers.size());
					for (Entry<Long, List<StudentHomeworkAnswer>> entry : answers.entrySet()) {
						vanswers.put(entry.getKey(), shaConvert.to(entry.getValue()));
					}
					return vanswers;
				}
			}
		});
	}

	private String delPContent(String str) {
		if (StringUtils.isNotBlank(str) && str.indexOf("<p>") == 0 && str.lastIndexOf("</p>") == str.length() - 4) {
			// 去除首尾的P标签
			str = str.substring(3, str.length() - 4);
		}
		return str;
	}
}
