package com.lanking.uxb.rescon.question.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeReview;
import com.lanking.cloud.domain.common.baseData.KnowledgeSync;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.common.baseData.QuestionCategory;
import com.lanking.cloud.domain.common.baseData.QuestionType;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.ChoiceFormat;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question2Tag;
import com.lanking.cloud.domain.common.resource.question.QuestionCategoryType;
import com.lanking.cloud.domain.common.resource.question.QuestionSource;
import com.lanking.cloud.domain.type.AsciiStatus;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionExaminationPointService;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionKnowledgeReviewService;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionKnowledgeService;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionKnowledgeSyncService;
import com.lanking.uxb.rescon.basedata.convert.ResconExaminationPointConvert;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgePointConvert;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgeReviewConvert;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgeSyncConvert;
import com.lanking.uxb.rescon.basedata.convert.ResconMetaKnowpointConvert;
import com.lanking.uxb.rescon.basedata.value.VKnowledgePoint;
import com.lanking.uxb.rescon.basedata.value.VMetaKnowpoint;
import com.lanking.uxb.rescon.basedata.value.VResconExaminationPoint;
import com.lanking.uxb.rescon.basedata.value.VResconKnowledgeReview;
import com.lanking.uxb.rescon.basedata.value.VResconKnowledgeSync;
import com.lanking.uxb.rescon.question.api.ResconAnswerManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionCategoryManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionMetaKnowManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionTagManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionTypeManage;
import com.lanking.uxb.rescon.question.value.VQuestion;
import com.lanking.uxb.rescon.question.value.VQuestionCategory;
import com.lanking.uxb.rescon.question.value.VQuestionTag;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.SubjectConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VSchool;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.service.file.util.FileUtil;

/**
 * 资源管控平台使用的习题转换器.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年8月15日
 */
@Component
public class ResconQuestionConvert extends Converter<VQuestion, Question, Long> {

	@Autowired
	private ResconQuestionTypeManage questionTypeService;
	@Autowired
	private ResconQuestionManage questionService;
	@Autowired
	private ResconQuestionMetaKnowManage questionMetaKnowService;
	@Autowired
	private ResconAnswerManage answerService;
	@Autowired
	private TextbookCategoryService tcService;
	@Autowired
	private TextbookCategoryConvert tcConvert;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private PhaseConvert phaseConvert;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SubjectConvert subjectConvert;
	@Autowired
	private ResconMetaKnowpointConvert mkConvert;
	@Autowired
	private ResconQuestionTypeConvert questionTypeConvert;
	@Autowired
	private ResconAnswerConvert answerConvert;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private ResconKnowledgePointConvert knowledgePointConvert;
	@Autowired
	private ResconExaminationPointConvert examinationPointConvert;
	@Autowired
	private ResconQuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private ResconQuestionExaminationPointService questionExaminationPointService;
	@Autowired
	private ResconQuestionTagManage questionTagManage;
	@Autowired
	private ResconQuestionTagConvert questionTagConvert;
	@Autowired
	private ResconQuestionCategoryManage questionCategoryManage;
	@Autowired
	private ResconQuestionCategoryConvert questionCategoryConvert;
	@Autowired
	private ResconQuestionKnowledgeReviewService questionKnowledgeReviewService;
	@Autowired
	private ResconQuestionKnowledgeSyncService questionKnowledgeSyncService;
	@Autowired
	private ResconKnowledgeReviewConvert knowledgeReviewConvert;
	@Autowired
	private ResconKnowledgeSyncConvert knowledgeSyncConvert;

	@Override
	protected Long getId(Question s) {
		return s.getId();
	}

	private String delPContent(String str) {
		if (StringUtils.isNotBlank(str) && str.indexOf("<p>") == 0 && str.lastIndexOf("</p>") == str.length() - 4) {
			// 去除首尾的P标签
			str = str.substring(3, str.length() - 4);
		}
		return str;
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
		v.setAnalysis(this.delPContent(validBlank(s.getAnalysis())));
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
		v.setCreateId(s.getCreateId());
		v.setVerifyId(s.getVerifyId());
		v.setVerify2Id(s.getVerify2Id());
		v.setCreateAt(s.getCreateAt());
		v.setUpdateAt(s.getUpdateAt());
		v.setVerifyAt(s.getVerifyAt());
		v.setVerify2At(s.getVerify2At());
		v.setCheckStatus(s.getStatus());
		v.setAsciiStatus(s.getAsciiStatus() == null ? AsciiStatus.NOCHECK : s.getAsciiStatus());
		v.setNopassContent(s.getNopassContent());
		if (StringUtils.isNotBlank(s.getNopassFiles())) {
			String[] nopassImages = s.getNopassFiles().split(";");
			List<String> urls = new ArrayList<String>(nopassImages.length);
			for (String fileId : nopassImages) {
				urls.add(FileUtil.getUrl(Long.parseLong(fileId)));
			}
			v.setNopassImages(urls);
		}

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
		v.setOpenAnswerFlag(s.getOpenAnswerFlag() == null ? false : s.getOpenAnswerFlag());

		if (CollectionUtils.isNotEmpty(s.getCategoryTypes())) {
			List<String> ctypes = new ArrayList<String>(s.getCategoryTypes().size());
			for (QuestionCategoryType qct : s.getCategoryTypes()) {
				ctypes.add(qct.getName());
			}
			v.setCtypes(ctypes);
		}

		v.setHasSimilar(s.isHasSimilar() == null ? false : s.isHasSimilar());
		v.setSameShow(s.getSameShow());
		v.setSameShowId(s.getSameShowId());
		v.setCheckRefund(s.isCheckRefund());
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
				return true;
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
		// 版本
		assemblers.add(new ConverterAssembler<VQuestion, Question, Integer, VTextbook>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(Question s, VQuestion d) {
				return s.getTextbookCode();
			}

			@Override
			public void setValue(Question s, VQuestion d, VTextbook value) {
				d.setTextbook(value);
			}

			@Override
			public VTextbook getValue(Integer key) {
				return key == null ? null : tbConvert.to(tbService.get(key));
			}

			@Override
			public Map<Integer, VTextbook> mgetValue(Collection<Integer> keys) {
				return keys == null ? null : tbConvert.to(tbService.mget(keys));
			}
		});
		// 章节
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, VSection>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, VQuestion d) {
				return s.getSectionCode();
			}

			@Override
			public void setValue(Question s, VQuestion d, VSection value) {
				d.setSection(value);
			}

			@Override
			public VSection getValue(Long key) {
				return key == null ? null : sectionConvert.to(sectionService.get(key));
			}

			@Override
			public Map<Long, VSection> mgetValue(Collection<Long> keys) {
				return keys == null ? null : sectionConvert.to(sectionService.mget(keys));
			}
		});
		// 知识点
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, List<VMetaKnowpoint>>() {
			@Override
			public boolean accept(Question s) {
				return true;
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
		// 新知识点
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, List<VKnowledgePoint>>() {
			@Override
			public boolean accept(Question s) {
				return true;
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
				d.setKnowledgePoints(value);
			}

			@Override
			public List<VKnowledgePoint> getValue(Long key) {
				return knowledgePointConvert.to(questionKnowledgeService.listByQuestion(key));
			}

			@Override
			public Map<Long, List<VKnowledgePoint>> mgetValue(Collection<Long> keys) {
				Map<Long, List<VKnowledgePoint>> rmap = new HashMap<Long, List<VKnowledgePoint>>();
				Map<Long, List<KnowledgePoint>> map = questionKnowledgeService.mListByQuestions(keys);
				for (Entry<Long, List<KnowledgePoint>> entry : map.entrySet()) {
					rmap.put(entry.getKey(), knowledgePointConvert.to(entry.getValue()));
				}
				return rmap;
			}
		});
		// 考点
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, List<VResconExaminationPoint>>() {
			@Override
			public boolean accept(Question s) {
				return true;
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
			public void setValue(Question s, VQuestion d, List<VResconExaminationPoint> value) {
				d.setExaminationPoints(value);
			}

			@Override
			public List<VResconExaminationPoint> getValue(Long key) {
				return examinationPointConvert.to(questionExaminationPointService.listByQuestion(key));
			}

			@Override
			public Map<Long, List<VResconExaminationPoint>> mgetValue(Collection<Long> keys) {
				Map<Long, List<VResconExaminationPoint>> rmap = new HashMap<Long, List<VResconExaminationPoint>>();
				Map<Long, List<ExaminationPoint>> map = questionExaminationPointService.mListByQuestions(keys);
				for (Entry<Long, List<ExaminationPoint>> entry : map.entrySet()) {
					rmap.put(entry.getKey(), examinationPointConvert.to(entry.getValue()));
				}
				return rmap;
			}

		});

		// 阶段
		assemblers.add(new ConverterAssembler<VQuestion, Question, Integer, VPhase>() {
			@Override
			public boolean accept(Question s) {
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
		assemblers.add(new ConverterAssembler<VQuestion, Question, Integer, QuestionType>() {
			@Override
			public boolean accept(Question s) {
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
			public void setValue(Question s, VQuestion d, QuestionType value) {
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
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, List<Question>>() {
			@Override
			public boolean accept(Question s) {
				return s.getType() == Question.Type.COMPOSITE;
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
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, List<Answer>>() {
			@Override
			public boolean accept(Question s) {
				return true;
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
			public void setValue(Question s, VQuestion d, List<Answer> value) {
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

		// 学校
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, VSchool>() {
			@Override
			public boolean accept(Question s) {
				return s.getSchoolId() != 0;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, VQuestion d) {
				return s.getSchoolId();
			}

			@Override
			public void setValue(Question s, VQuestion d, VSchool value) {
				d.setSchool(value);
			}

			@Override
			public VSchool getValue(Long key) {
				return key == null ? null : schoolConvert.to(schoolService.get(key));
			}

			@Override
			public Map<Long, VSchool> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return schoolConvert.to(schoolService.mget(keys));
			}
		});

		// 题目分类
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, List<VQuestionCategory>>() {
			@Override
			public boolean accept(Question s) {
				return true;
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
			public void setValue(Question s, VQuestion d, List<VQuestionCategory> questionCategorys) {
				d.setQuestionCategorys(questionCategorys);
			}

			@Override
			public List<VQuestionCategory> getValue(Long key) {
				if (key == null) {
					return null;
				}
				return questionCategoryConvert.to(questionCategoryManage.listByQuestion(key));
			}

			@Override
			public Map<Long, List<VQuestionCategory>> mgetValue(Collection<Long> keys) {
				if (keys.isEmpty()) {
					return Maps.newHashMap();
				}

				Map<Long, List<QuestionCategory>> categoryMap = questionCategoryManage.mgetByQuestions(keys);
				Map<Long, List<VQuestionCategory>> vCategoryMap = new HashMap<Long, List<VQuestionCategory>>(
						categoryMap.size());

				for (Entry<Long, List<QuestionCategory>> entry : categoryMap.entrySet()) {
					vCategoryMap.put(entry.getKey(), questionCategoryConvert.to(entry.getValue()));
				}

				return vCategoryMap;
			}
		});

		// 题目标签
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, List<VQuestionTag>>() {
			@Override
			public boolean accept(Question s) {
				return true;
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
			public void setValue(Question s, VQuestion d, List<VQuestionTag> questionTags) {
				d.setQuestionTags(questionTags);
			}

			@Override
			public List<VQuestionTag> getValue(Long key) {
				if (key == null) {
					return null;
				}
				List<Question2Tag> questionTags = questionTagManage.listByQuestion(key);
				if (CollectionUtils.isEmpty(questionTags)) {
					return null;
				}

				Set<Long> tagCodes = new HashSet<Long>(questionTags.size());
				for (Question2Tag question2Tag : questionTags) {
					tagCodes.add(question2Tag.getTagCode());
				}
				Map<Long, VQuestionTag> questionTagMap = questionTagConvert.to(questionTagManage.mget(tagCodes));
				List<VQuestionTag> result = new ArrayList<VQuestionTag>(questionTags.size());

				for (Question2Tag question2Tag : questionTags) {
					VQuestionTag vQuestionTag = questionTagMap.get(question2Tag.getTagCode());
					vQuestionTag.setSystem(question2Tag.getSystem());
					result.add(vQuestionTag);
				}

				return result;
			}

			@Override
			public Map<Long, List<VQuestionTag>> mgetValue(Collection<Long> keys) {
				if (keys.isEmpty()) {
					return Maps.newHashMap();
				}
				Map<Long, List<Question2Tag>> tag2Map = questionTagManage.mgetByQuestions(keys);
				Set<Long> tagCodes = new HashSet<Long>();
				for (Entry<Long, List<Question2Tag>> entry : tag2Map.entrySet()) {
					if (entry.getValue() != null) {
						for (Question2Tag question2Tag : entry.getValue()) {
							tagCodes.add(question2Tag.getTagCode());
						}
					}
				}
				Map<Long, VQuestionTag> questionTagMap = questionTagConvert.to(questionTagManage.mget(tagCodes));

				// 拼装
				Map<Long, List<VQuestionTag>> result = new HashMap<Long, List<VQuestionTag>>(tag2Map.size());
				for (Entry<Long, List<Question2Tag>> entry : tag2Map.entrySet()) {
					if (entry.getValue() != null) {
						List<VQuestionTag> vQuestionTags = new ArrayList<VQuestionTag>(entry.getValue().size());
						for (Question2Tag question2Tag : entry.getValue()) {
							VQuestionTag vQuestionTag = questionTagMap.get(question2Tag.getTagCode());
							vQuestionTag.setSystem(question2Tag.getSystem());
							vQuestionTags.add(vQuestionTag);
						}
						result.put(entry.getKey(), vQuestionTags);
					}
				}

				return result;
			}
		});

		// 知识点V3-同步知识点
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, List<VResconKnowledgeSync>>() {
			@Override
			public boolean accept(Question s) {
				return true;
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
			public void setValue(Question s, VQuestion d, List<VResconKnowledgeSync> value) {
				d.setKnowledgeSyncs(value);
			}

			@Override
			public List<VResconKnowledgeSync> getValue(Long key) {
				return knowledgeSyncConvert.to(questionKnowledgeSyncService.listByQuestion(key));
			}

			@Override
			public Map<Long, List<VResconKnowledgeSync>> mgetValue(Collection<Long> keys) {
				Map<Long, List<VResconKnowledgeSync>> rmap = new HashMap<Long, List<VResconKnowledgeSync>>();
				Map<Long, List<KnowledgeSync>> map = questionKnowledgeSyncService.mListByQuestions(keys);
				for (Entry<Long, List<KnowledgeSync>> entry : map.entrySet()) {
					rmap.put(entry.getKey(), knowledgeSyncConvert.to(entry.getValue()));
				}
				return rmap;
			}
		});

		// 知识点V3-复习知识点
		assemblers.add(new ConverterAssembler<VQuestion, Question, Long, List<VResconKnowledgeReview>>() {
			@Override
			public boolean accept(Question s) {
				return true;
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
			public void setValue(Question s, VQuestion d, List<VResconKnowledgeReview> value) {
				d.setKnowledgeReviews(value);
			}

			@Override
			public List<VResconKnowledgeReview> getValue(Long key) {
				return knowledgeReviewConvert.to(questionKnowledgeReviewService.listByQuestion(key));
			}

			@Override
			public Map<Long, List<VResconKnowledgeReview>> mgetValue(Collection<Long> keys) {
				Map<Long, List<VResconKnowledgeReview>> rmap = new HashMap<Long, List<VResconKnowledgeReview>>();
				Map<Long, List<KnowledgeReview>> map = questionKnowledgeReviewService.mListByQuestions(keys);
				for (Entry<Long, List<KnowledgeReview>> entry : map.entrySet()) {
					rmap.put(entry.getKey(), knowledgeReviewConvert.to(entry.getValue()));
				}
				return rmap;
			}
		});
	}
}
