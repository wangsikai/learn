package com.lanking.uxb.service.common.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.cglib.beans.BeanGenerator;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.common.baseData.QuestionType;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.ChoiceFormat;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionExaminationPoint;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.yoo.collection.QuestionCollection;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.bean.GenericConverter;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.api.ExaminationPointService;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.convert.ExaminationPointConvert;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.convert.MetaKnowpointConvert;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.SubjectConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.value.VExaminationPoint;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.service.common.value.VQuestionBase;
import com.lanking.uxb.service.question.api.AnswerService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.question.util.QuestionUtils;
import com.lanking.uxb.service.resources.api.QuestionExaminationPointService;
import com.lanking.uxb.service.resources.api.QuestionMetaKnowService;
import com.lanking.uxb.service.resources.api.QuestionTypeService;
import com.lanking.uxb.service.resources.convert.AnswerConvert;
import com.lanking.uxb.service.resources.convert.QuestionTypeConvert;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCollectionService;

/**
 * 基础题库转换convert
 * 
 * @since yoomath V1.9
 * @author wangsenhao
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class QuestionBaseConvert<D extends VQuestionBase> extends GenericConverter<D, Question, Long> {

	BeanGenerator benGenerator = new BeanGenerator();

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
	private KnowledgePointConvert knowledgePointConvert;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private ExaminationPointService examinationPointService;
	@Autowired
	private ExaminationPointConvert examinationPointConvert;
	@Autowired
	private QuestionExaminationPointService qeService;

	public QuestionBaseConvert(Class genericClass) {
		super(genericClass);
	}

	public D to(Question s, QuestionBaseConvertOption option) {
		s.setAnalysis(option.isAnalysis());
		s.setAnswer(option.isAnswer());
		s.setInitSub(option.isInitSub());
		s.setCollect(option.isCollect());

		// 2017-5-24 新加配置
		s.setInitTextbookCategory(option.isInitTextbookCategory());
		s.setInitKnowledgePoint(option.isInitKnowledgePoint());
		s.setInitMetaKnowpoint(option.isInitMetaKnowpoint());
		s.setInitPhase(option.isInitPhase());
		s.setInitSubject(option.isInitSubject());
		s.setInitQuestionType(option.isInitQuestionType());
		s.setInitStudentQuestionCount(option.isInitStudentQuestionCount());
		return super.to(s);
	}

	public List<D> to(List<Question> ss, QuestionBaseConvertOption option) {
		for (Question s : ss) {
			s.setAnalysis(option.isAnalysis());
			s.setAnswer(option.isAnswer());
			s.setInitSub(option.isInitSub());
			s.setCollect(option.isCollect());
			s.setInitExamination(option.isInitExamination());

			// 2017-5-24 新加配置
			s.setInitTextbookCategory(option.isInitTextbookCategory());
			s.setInitKnowledgePoint(option.isInitKnowledgePoint());
			s.setInitMetaKnowpoint(option.isInitMetaKnowpoint());
			s.setInitPhase(option.isInitPhase());
			s.setInitSubject(option.isInitSubject());
			s.setInitQuestionType(option.isInitQuestionType());
			s.setInitStudentQuestionCount(option.isInitStudentQuestionCount());
		}
		return super.to(ss);
	}

	public Map<Long, D> to(Map<Long, Question> ss, QuestionBaseConvertOption option) {
		for (Question s : ss.values()) {
			s.setAnalysis(option.isAnalysis());
			s.setAnswer(option.isAnswer());
			s.setInitSub(option.isInitSub());
			s.setCollect(option.isCollect());
			s.setInitExamination(option.isInitExamination());

			// 2017-5-24 新加配置
			s.setInitTextbookCategory(option.isInitTextbookCategory());
			s.setInitKnowledgePoint(option.isInitKnowledgePoint());
			s.setInitMetaKnowpoint(option.isInitMetaKnowpoint());
			s.setInitPhase(option.isInitPhase());
			s.setInitSubject(option.isInitSubject());
			s.setInitQuestionType(option.isInitQuestionType());
			s.setInitStudentQuestionCount(option.isInitStudentQuestionCount());
		}
		return super.to(ss);
	}

	@Override
	protected Long getId(Question s) {
		return s.getId();
	}

	@Override
	protected D convert(Question s) {
		benGenerator.setSuperclass(this.getGenericClass());
		D v = (D) benGenerator.create();
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
		v.setCheckStatus(s.getStatus());
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
		// 为客户端处理
		if (Security.isClient()) {
			v.setContent(QuestionUtils.process(v.getContent(), null, true));
			v.setHint(QuestionUtils.process(v.getHint(), null, true));
			v.setAnalysis(QuestionUtils.process(v.getAnalysis(), null, true));
			int index = 0;
			for (String c : v.getChoices()) {
				v.getChoices().set(index, QuestionUtils.process(c, null, true));
				index++;
			}
		}

		v.setCategoryTypes(s.getCategoryTypes());
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 教材分类
		assemblers.add(new ConverterAssembler<D, Question, Integer, VTextbookCategory>() {
			@Override
			public boolean accept(Question s) {
				return s.isInitTextbookCategory();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(Question s, D d) {
				return s.getTextbookCategoryCode();
			}

			@Override
			public void setValue(Question s, D d, VTextbookCategory value) {
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
		// 知识点
		assemblers.add(new ConverterAssembler<D, Question, Long, List<VMetaKnowpoint>>() {
			@Override
			public boolean accept(Question s) {
				return s.isInitMetaKnowpoint();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, D d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, D d, List<VMetaKnowpoint> value) {
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
		assemblers.add(new ConverterAssembler<D, Question, Integer, VPhase>() {
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
			public Integer getKey(Question s, D d) {
				return s.getPhaseCode();
			}

			@Override
			public void setValue(Question s, D d, VPhase value) {
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
		assemblers.add(new ConverterAssembler<D, Question, Integer, VSubject>() {
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
			public Integer getKey(Question s, D d) {
				return s.getSubjectCode();
			}

			@Override
			public void setValue(Question s, D d, VSubject value) {
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
		assemblers.add(new ConverterAssembler<D, Question, Integer, QuestionType>() {
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
			public Integer getKey(Question s, D d) {
				return s.getTypeCode();
			}

			@Override
			public void setValue(Question s, D d, QuestionType value) {
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
		assemblers.add(new ConverterAssembler<D, Question, Long, List<Question>>() {
			@Override
			public boolean accept(Question s) {
				return s.isInitSub();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, D d) {
				if (s.isSubFlag()) {
					return null;
				}
				return s.getId();
			}

			@Override
			public void setValue(Question s, D d, List<Question> value) {
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
		assemblers.add(new ConverterAssembler<D, Question, Long, List<Answer>>() {
			@Override
			public boolean accept(Question s) {
				return s.isAnswer();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, D d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, D d, List<Answer> value) {
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

		// 题目是否收藏
		assemblers.add(new ConverterAssembler<D, Question, Long, QuestionCollection>() {

			@Override
			public boolean accept(Question s) {
				return s.isCollect();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, D d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, D d, QuestionCollection value) {
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

		// @since 3.9.0 转换新知识点
		assemblers.add(new ConverterAssembler<D, Question, Long, List<Long>>() {

			@Override
			public boolean accept(Question question) {
				return question.isInitKnowledgePoint();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question question, D d) {
				return question.getId();
			}

			@Override
			public void setValue(Question question, D d, List<Long> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					List<Long> knowledgePointCodes = new ArrayList<Long>(value.size() * 3);
					for (Long code : value) {
						knowledgePointCodes.add(code);
						knowledgePointCodes.add(code / 100);
						knowledgePointCodes.add(code / 1000);
						knowledgePointCodes.add(code / 100000);
					}

					d.setNewKnowpoints(knowledgePointConvert.to(knowledgePointService.mgetList(knowledgePointCodes)));
				}
			}

			@Override
			public List<Long> getValue(Long key) {
				if (key == null) {
					return null;
				}
				return questionKnowledgeService.queryKnowledgeByQuestionId(key);
			}

			@Override
			public Map<Long, List<Long>> mgetValue(Collection<Long> keys) {
				return questionKnowledgeService.mgetByQuestions(keys);
			}
		});

		// @since 3.9.0 考点转换
		assemblers.add(new ConverterAssembler<D, Question, Long, List<VExaminationPoint>>() {

			@Override
			public boolean accept(Question question) {
				return question.isInitExamination();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question question, D vQuestion) {
				return question.getId();
			}

			@Override
			public void setValue(Question question, D vQuestion, List<VExaminationPoint> value) {
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

	}

	private String delPContent(String str) {
		if (StringUtils.isNotBlank(str) && str.indexOf("<p>") == 0 && str.lastIndexOf("</p>") == str.length() - 4) {
			// 去除首尾的P标签
			str = str.substring(3, str.length() - 4);
		}
		return str;
	}

}
