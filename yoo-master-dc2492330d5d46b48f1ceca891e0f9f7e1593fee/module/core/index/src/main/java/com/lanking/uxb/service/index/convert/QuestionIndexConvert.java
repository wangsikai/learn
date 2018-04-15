package com.lanking.uxb.service.index.convert;

import java.math.BigDecimal;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeReview;
import com.lanking.cloud.domain.common.baseData.KnowledgeSync;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question2Category;
import com.lanking.cloud.domain.common.resource.question.Question2Tag;
import com.lanking.cloud.domain.common.resource.question.QuestionExaminationPoint;
import com.lanking.cloud.domain.common.resource.question.QuestionKnowledge;
import com.lanking.cloud.domain.common.resource.question.QuestionKnowledgeReview;
import com.lanking.cloud.domain.common.resource.question.QuestionKnowledgeSync;
import com.lanking.cloud.domain.common.resource.question.QuestionMetaKnow;
import com.lanking.cloud.domain.common.resource.question.QuestionSection;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeReviewService;
import com.lanking.uxb.service.code.api.KnowledgeSyncService;
import com.lanking.uxb.service.index.value.QuestionIndexDoc;

/**
 * 习题索引转换.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年3月30日
 */
@Component
@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
public class QuestionIndexConvert extends Converter<QuestionIndexDoc, Question, Long> {

	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;
	@Autowired
	@Qualifier("QuestionMetaKnowRepo")
	Repo<QuestionMetaKnow, Integer> qmknowpointRepo;
	@Autowired
	@Qualifier("MetaKnowpointRepo")
	Repo<MetaKnowpoint, Integer> mknowpointRepo;
	@Autowired
	@Qualifier("QuestionSectionRepo")
	Repo<QuestionSection, Long> questionSectionRepo;
	@Autowired
	@Qualifier("QuestionKnowledgeRepo")
	Repo<QuestionKnowledge, Long> questionKnowledgeRepo;
	@Autowired
	@Qualifier("QuestionKnowledgeSyncRepo")
	Repo<QuestionKnowledgeSync, Long> questionKnowledgeSyncRepo;
	@Autowired
	@Qualifier("QuestionKnowledgeReviewRepo")
	Repo<QuestionKnowledgeReview, Long> questionKnowledgeReviewRepo;
	@Autowired
	@Qualifier("QuestionExaminationPointRepo")
	Repo<QuestionExaminationPoint, Long> questionExaminationRepo;
	@Autowired
	@Qualifier("ExaminationPointRepo")
	Repo<ExaminationPoint, Long> examinationPointRepo;
	@Autowired
	@Qualifier("Question2CategoryRepo")
	Repo<Question2Category, Long> question2CategoryRepo;
	@Autowired
	@Qualifier("Question2TagRepo")
	Repo<Question2Tag, Long> question2TagRepo;
	@Autowired
	@Qualifier("AnswerRepo")
	Repo<Answer, Long> answerRepo;

	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private KnowledgeReviewService knowledgeReviewService;
	@Autowired
	private KnowledgeSyncService knowledgeSyncService;

	@Override
	protected Long getId(Question s) {
		return s.getCreateId();
	}

	@Override
	protected QuestionIndexDoc convert(Question s) {
		QuestionIndexDoc doc = new QuestionIndexDoc();
		doc.setVendorId(s.getVendorId());
		doc.setResourceId(s.getId());
		doc.setCode(s.getCode());
		doc.setCheckStatus(s.getStatus().getValue());
		StringBuffer contentbuff = new StringBuffer(s.getContent());
		if (s.getType() == Question.Type.SINGLE_CHOICE || s.getType() == Question.Type.MULTIPLE_CHOICE) {
			contentbuff.append(StringUtils.defaultIfBlank(s.getChoiceA()))
					.append(StringUtils.defaultIfBlank(s.getChoiceB()))
					.append(StringUtils.defaultIfBlank(s.getChoiceC()))
					.append(StringUtils.defaultIfBlank(s.getChoiceD()))
					.append(StringUtils.defaultIfBlank(s.getChoiceE()))
					.append(StringUtils.defaultIfBlank(s.getChoiceF()));
		}
		doc.setContents(contentbuff.toString());
		doc.setCreateAt(s.getCreateAt().getTime());
		doc.setCreateId(s.getCreateId());
		doc.setDifficulty(BigDecimal.valueOf(s.getDifficulty() == null ? 1.0f : s.getDifficulty()));
		doc.setPhaseCode(s.getPhaseCode());
		doc.setSource(s.getSource());
		doc.setSubjectCode(s.getSubjectCode());
		doc.setTextbookCategoryCode(s.getTextbookCategoryCode());
		doc.setTextbookCode(s.getTextbookCode());
		doc.setType(s.getType().getValue());
		doc.setTypeCode(s.getTypeCode());
		doc.setVendorId(s.getVendorId());
		doc.setVerifyId(s.getVerifyId());
		doc.setVerifyAt(s.getVerifyAt() == null ? null : s.getVerifyAt().getTime());
		doc.setVerify2Id(s.getVerify2Id());
		doc.setVerify2At(s.getVerify2At() == null ? null : s.getVerify2At().getTime());
		doc.setSchoolId(s.getSchoolId());
		doc.setKnowledgeCreateAt(s.getKnowledgeCreateAt() == null ? null : s.getKnowledgeCreateAt().getTime());

		// 重复展示题 since 2.5.1
		doc.setSameShow(s.getSameShow());
		doc.setSameShowId(s.getSameShowId());
		return doc;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 知识点
		assemblers.add(new ConverterAssembler<QuestionIndexDoc, Question, Long, List<MetaKnowpoint>>() {

			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, QuestionIndexDoc d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, QuestionIndexDoc d, List<MetaKnowpoint> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					StringBuffer buff = new StringBuffer();
					List<Integer> metaKnowpointCodes = Lists.newArrayList();
					for (MetaKnowpoint metaKnowpoint : value) {
						if (null != metaKnowpoint) {
							buff.append(metaKnowpoint.getName());
							int length = metaKnowpoint.getCode().toString().length();
							if (metaKnowpoint.getCode().toString().length() == 5) {
								metaKnowpointCodes.add(metaKnowpoint.getCode());
							} else {
								for (int i = 5; i <= length; i = i + 2) {
									metaKnowpointCodes
											.add(Integer.parseInt(metaKnowpoint.getCode().toString().substring(0, i)));
								}
							}

						}
					}
					d.setMetaKnowpoints(buff.toString());
					d.setMetaKnowpointCodes(metaKnowpointCodes);
				}
			}

			@Override
			public List<MetaKnowpoint> getValue(Long key) {
				if (key == null) {
					return null;
				}
				return listByQuestion(key);
			}

			@Override
			public Map<Long, List<MetaKnowpoint>> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				return mListByQuestions(keys);
			}
		});

		// 新知识点
		assemblers.add(new ConverterAssembler<QuestionIndexDoc, Question, Long, List<KnowledgePoint>>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, QuestionIndexDoc d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, QuestionIndexDoc d, List<KnowledgePoint> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					StringBuffer knowledgePoints = new StringBuffer();
					Set<Long> knowledgePointCodes = Sets.newHashSet();
					for (KnowledgePoint k : value) {
						Long code = k.getCode();
						knowledgePointCodes.add(code);
						knowledgePointCodes.add(code / 100);
						knowledgePointCodes.add(code / 1000);
						knowledgePointCodes.add(code / 100000);
						knowledgePoints.append(k.getName()).append("、");
					}
					knowledgePoints.deleteCharAt(knowledgePoints.lastIndexOf("、"));
					d.setKnowledgePoints(knowledgePoints.toString());
					d.setKnowledgePointCodes(Lists.newArrayList(knowledgePointCodes));
				}
			}

			@Override
			public List<KnowledgePoint> getValue(Long key) {
				if (key == null) {
					return null;
				}
				List<Long> codes = knowledgePointListByQuestion(key);
				if (CollectionUtils.isEmpty(codes)) {
					return Collections.EMPTY_LIST;
				}
				return knowledgePointService.mgetList(codes);
			}

			@Override
			public Map<Long, List<KnowledgePoint>> mgetValue(Collection<Long> keys) {
				Map<Long, List<KnowledgePoint>> retMap = new HashMap<Long, List<KnowledgePoint>>(keys.size());
				Map<Long, List<Long>> codeMap = knowledgePointMListByQuestions(keys);
				for (Map.Entry<Long, List<Long>> entry : codeMap.entrySet()) {
					if (CollectionUtils.isEmpty(entry.getValue())) {
						retMap.put(entry.getKey(), Collections.EMPTY_LIST);
					} else {
						retMap.put(entry.getKey(), knowledgePointService.mgetList(entry.getValue()));
					}
				}
				return retMap;
			}
		});

		// 同步知识点
		assemblers.add(new ConverterAssembler<QuestionIndexDoc, Question, Long, List<KnowledgeSync>>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, QuestionIndexDoc d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, QuestionIndexDoc d, List<KnowledgeSync> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					StringBuffer knowledgeSyncs = new StringBuffer();
					Set<Long> knowledgeSyncCodes = Sets.newHashSet();
					for (KnowledgeSync k : value) {
						Long code = k.getCode();
						knowledgeSyncCodes.add(code);
						knowledgeSyncCodes.add(code / 100);
						knowledgeSyncCodes.add(code / 1000);
						knowledgeSyncCodes.add(code / 10000);
						knowledgeSyncs.append(k.getName()).append("、");
					}
					knowledgeSyncs.deleteCharAt(knowledgeSyncs.lastIndexOf("、"));
					d.setKnowledgeSyncCodes(Lists.newArrayList(knowledgeSyncCodes));
					d.setKnowledgeSyncs(knowledgeSyncs.toString());
				}
			}

			@Override
			public List<KnowledgeSync> getValue(Long key) {
				if (key == null) {
					return null;
				}
				List<Long> codes = knowledgeSyncListByQuestion(key);
				if (CollectionUtils.isEmpty(codes)) {
					return Collections.EMPTY_LIST;
				}
				return knowledgeSyncService.mgetList(codes);
			}

			@Override
			public Map<Long, List<KnowledgeSync>> mgetValue(Collection<Long> keys) {
				Map<Long, List<KnowledgeSync>> retMap = new HashMap<Long, List<KnowledgeSync>>(keys.size());
				Map<Long, List<Long>> codeMap = knowledgeSyncMListByQuestions(keys);
				for (Map.Entry<Long, List<Long>> entry : codeMap.entrySet()) {
					if (CollectionUtils.isEmpty(entry.getValue())) {
						retMap.put(entry.getKey(), Collections.EMPTY_LIST);
					} else {
						retMap.put(entry.getKey(), knowledgeSyncService.mgetList(entry.getValue()));
					}
				}
				return retMap;
			}
		});

		// 复习知识点
		assemblers.add(new ConverterAssembler<QuestionIndexDoc, Question, Long, List<KnowledgeReview>>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, QuestionIndexDoc d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, QuestionIndexDoc d, List<KnowledgeReview> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					StringBuffer knowledgeReviews = new StringBuffer();
					Set<Long> knowledgeReviewCodes = Sets.newHashSet();
					for (KnowledgeReview k : value) {
						Long code = k.getCode();
						knowledgeReviewCodes.add(code);
						knowledgeReviewCodes.add(code / 1000);
						knowledgeReviewCodes.add(code / 10000);
						knowledgeReviewCodes.add(code / 100000);
						knowledgeReviews.append(k.getName()).append("、");
					}
					knowledgeReviews.deleteCharAt(knowledgeReviews.lastIndexOf("、"));
					d.setKnowledgeReviewCodes(Lists.newArrayList(knowledgeReviewCodes));
					d.setKnowledgeReviews(knowledgeReviews.toString());
				}
			}

			@Override
			public List<KnowledgeReview> getValue(Long key) {
				if (key == null) {
					return null;
				}
				List<Long> codes = knowledgeReviewListByQuestion(key);
				if (CollectionUtils.isEmpty(codes)) {
					return Collections.EMPTY_LIST;
				}
				return knowledgeReviewService.mgetList(codes);
			}

			@Override
			public Map<Long, List<KnowledgeReview>> mgetValue(Collection<Long> keys) {
				Map<Long, List<KnowledgeReview>> retMap = new HashMap<Long, List<KnowledgeReview>>(keys.size());
				Map<Long, List<Long>> codeMap = knowledgeReviewMListByQuestions(keys);
				for (Map.Entry<Long, List<Long>> entry : codeMap.entrySet()) {
					if (CollectionUtils.isEmpty(entry.getValue())) {
						retMap.put(entry.getKey(), Collections.EMPTY_LIST);
					} else {
						retMap.put(entry.getKey(), knowledgeReviewService.mgetList(entry.getValue()));
					}
				}
				return retMap;
			}
		});

		// 考点
		assemblers.add(new ConverterAssembler<QuestionIndexDoc, Question, Long, List<ExaminationPoint>>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, QuestionIndexDoc d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, QuestionIndexDoc d, List<ExaminationPoint> value) {
				// d.setExaminationPointCodes(value);
				if (CollectionUtils.isNotEmpty(value)) {
					List<Long> knowledgePointCodes = Lists.newArrayList();
					for (ExaminationPoint examinationPoint : value) {
						knowledgePointCodes.add(examinationPoint.getId());
						Long pcode = examinationPoint.getPcode();
						knowledgePointCodes.add(pcode);
						knowledgePointCodes.add(Long.parseLong(pcode.toString().substring(0, 5)));
						knowledgePointCodes.add(Long.parseLong(pcode.toString().substring(0, 7)));
					}
					d.setExaminationPointCodes(knowledgePointCodes);
				}
			}

			@Override
			public List<ExaminationPoint> getValue(Long key) {
				return examinationPointListByQuestion(key);
			}

			@Override
			public Map<Long, List<ExaminationPoint>> mgetValue(Collection<Long> keys) {
				return examinationPointMListByQuestions(keys);
			}

		});

		// 教材章节
		assemblers.add(new ConverterAssembler<QuestionIndexDoc, Question, Long, Map<String, List<Long>>>() {

			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, QuestionIndexDoc d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, QuestionIndexDoc d, Map<String, List<Long>> value) {
				if (value.get("sectionCodes") != null) {
					d.setSectionCodes(value.get("sectionCodes"));
				}
				if (value.get("sectionCodes2") != null) {
					d.setSectionCodes2(value.get("sectionCodes2"));
				}
				if (value.get("textbookCodes") != null) {
					d.setTextbookCodes(value.get("textbookCodes"));
				}
				if (value.get("textbookCodes2") != null) {
					d.setTextbookCodes2(value.get("textbookCodes2"));
				}
			}

			@Override
			public Map<String, List<Long>> getValue(Long key) {
				return getQuestionSectionMap(key);
			}

			@Override
			public Map<Long, Map<String, List<Long>>> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				return mgetQuestionSectionMap(keys);
			}

		});

		// 子题
		assemblers.add(new ConverterAssembler<QuestionIndexDoc, Question, Long, String>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, QuestionIndexDoc d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, QuestionIndexDoc d, String value) {
				d.setContents(d.getContents() + value);
			}

			@Override
			public String getValue(Long key) {
				List<Question> questions = getSubQuestions(key);
				StringBuffer contentbuff = new StringBuffer();
				for (Question question : questions) {
					contentbuff.append(question.getContent());
					if (question.getType() == Question.Type.SINGLE_CHOICE
							|| question.getType() == Question.Type.MULTIPLE_CHOICE) {
						contentbuff.append(StringUtils.defaultIfBlank(question.getChoiceA()))
								.append(StringUtils.defaultIfBlank(question.getChoiceB()))
								.append(StringUtils.defaultIfBlank(question.getChoiceC()))
								.append(StringUtils.defaultIfBlank(question.getChoiceD()))
								.append(StringUtils.defaultIfBlank(question.getChoiceE()))
								.append(StringUtils.defaultIfBlank(question.getChoiceF()));
					}
				}
				return contentbuff.toString();
			}

			@Override
			public Map<Long, String> mgetValue(Collection<Long> keys) {
				Map<Long, String> vmap = new HashMap<Long, String>(keys.size());
				Map<Long, List<Question>> map = mgetSubQuestions(keys);
				for (Entry<Long, List<Question>> entry : map.entrySet()) {
					StringBuffer contentbuff = new StringBuffer();
					for (Question question : entry.getValue()) {
						contentbuff.append(question.getContent());
						if (question.getType() == Question.Type.SINGLE_CHOICE
								|| question.getType() == Question.Type.MULTIPLE_CHOICE) {
							contentbuff.append(StringUtils.defaultIfBlank(question.getChoiceA()))
									.append(StringUtils.defaultIfBlank(question.getChoiceB()))
									.append(StringUtils.defaultIfBlank(question.getChoiceC()))
									.append(StringUtils.defaultIfBlank(question.getChoiceD()))
									.append(StringUtils.defaultIfBlank(question.getChoiceE()))
									.append(StringUtils.defaultIfBlank(question.getChoiceF()));
						}
					}
					vmap.put(entry.getKey(), contentbuff.toString());
				}
				return vmap;
			}
		});

		// 2017-7-31 新题目分类
		assemblers.add(new ConverterAssembler<QuestionIndexDoc, Question, Long, List<Long>>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, QuestionIndexDoc d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, QuestionIndexDoc d, List<Long> questionCategoryCodes) {
				d.setQuestionCategorys(questionCategoryCodes);
			}

			@Override
			public List<Long> getValue(Long key) {
				if (key == null) {
					return null;
				}
				List<Long> questionCategoryCodes = question2CategoryRepo
						.find("$indexListByQuestion", Params.param("questionId", key)).list(Long.class);
				return questionCategoryCodes;
			}

			@Override
			public Map<Long, List<Long>> mgetValue(Collection<Long> keys) {
				if (keys.isEmpty()) {
					return Maps.newHashMap();
				}
				List<Question2Category> all = question2CategoryRepo
						.find("$indexListByQuestions", Params.param("questionIds", keys)).list();

				Map<Long, List<Long>> map = new HashMap<Long, List<Long>>(keys.size());
				for (Question2Category q2c : all) {
					List<Long> questionCategoryCodes = map.get(q2c.getQuestionId());
					if (questionCategoryCodes == null) {
						questionCategoryCodes = new ArrayList<Long>();
						map.put(q2c.getQuestionId(), questionCategoryCodes);
					}
					questionCategoryCodes.add(q2c.getCategoryCode());
				}

				return map;
			}
		});

		// 2017-7-31 新题目标签
		assemblers.add(new ConverterAssembler<QuestionIndexDoc, Question, Long, List<Long>>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, QuestionIndexDoc d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, QuestionIndexDoc d, List<Long> questionTagCodes) {
				d.setQuestionTags(questionTagCodes);
			}

			@Override
			public List<Long> getValue(Long key) {
				if (key == null) {
					return null;
				}
				List<Long> questionTagCodes = question2TagRepo
						.find("$indexListByQuestion", Params.param("questionId", key)).list(Long.class);
				return questionTagCodes;
			}

			@Override
			public Map<Long, List<Long>> mgetValue(Collection<Long> keys) {
				if (keys.isEmpty()) {
					return Maps.newHashMap();
				}
				List<Question2Tag> all = question2TagRepo
						.find("$indexListByQuestions", Params.param("questionIds", keys)).list();

				Map<Long, List<Long>> map = new HashMap<Long, List<Long>>(keys.size());
				for (Question2Tag q2t : all) {
					List<Long> questionTagCodes = map.get(q2t.getQuestionId());
					if (questionTagCodes == null) {
						questionTagCodes = new ArrayList<Long>();
						map.put(q2t.getQuestionId(), questionTagCodes);
					}
					questionTagCodes.add(q2t.getTagCode());
				}

				return map;
			}
		});

		// 习题答案，主要为了判断是否复合katex解析，若后期全部处理完成，此处可以考虑去除 2017-10-19 by wanlong.che
		assemblers.add(new ConverterAssembler<QuestionIndexDoc, Question, Long, List<Answer>>() {
			@Override
			public boolean accept(Question s) {
				return s.getStatus() == CheckStatus.PASS
						&& (s.getSubjectCode() == 102 || s.getSubjectCode() == 202 || s.getSubjectCode() == 302)
						&& s.getType() != Question.Type.COMPOSITE;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, QuestionIndexDoc d) {
				return s.getId();
			}

			@Override
			public void setValue(Question question, QuestionIndexDoc d, List<Answer> answers) {
				StringBuffer contentAll = new StringBuffer(question.getContent());
				contentAll.append(StringUtils.defaultIfBlank(question.getChoiceA()));
				contentAll.append(StringUtils.defaultIfBlank(question.getChoiceB()));
				contentAll.append(StringUtils.defaultIfBlank(question.getChoiceC()));
				contentAll.append(StringUtils.defaultIfBlank(question.getChoiceD()));
				contentAll.append(StringUtils.defaultIfBlank(question.getChoiceE()));
				contentAll.append(StringUtils.defaultIfBlank(question.getChoiceF()));
				contentAll.append(StringUtils.defaultIfBlank(question.getAnalysis()));
				contentAll.append(StringUtils.defaultIfBlank(question.getHint()));
				for (Answer answer : answers) {
					if (StringUtils.isNotBlank(answer.getContent())) {
						contentAll.append(answer.getContent());
					} else {
						contentAll.append(StringUtils.defaultIfBlank(answer.getContentLatex()));
					}
				}
				d.setIsKatexSpecs(QuestionKatexUtils.isLatexSpecs(contentAll.toString()));
			}

			@Override
			public List<Answer> getValue(Long key) {
				if (key == null) {
					return null;
				}
				return answerRepo.find("$indexGetQuestionAnswers", Params.param("qid", key)).list();
			}

			@Override
			public Map<Long, List<Answer>> mgetValue(Collection<Long> keys) {
				if (keys.isEmpty()) {
					return Maps.newHashMap();
				}
				List<Answer> answers = answerRepo.find("$indexGetQuestionAnswers", Params.param("qids", keys)).list();
				Map<Long, List<Answer>> map = Maps.newHashMap();
				for (Answer answer : answers) {
					List<Answer> as = map.get(answer.getQuestionId());
					if (as == null) {
						as = new ArrayList<Answer>();
						map.put(answer.getQuestionId(), as);
					}
					as.add(answer);
					map.put(answer.getQuestionId(), as);
				}
				return map;
			}
		});
	}

	private List<MetaKnowpoint> listByQuestion(long questionId) {
		List<QuestionMetaKnow> qmks = qmknowpointRepo
				.find("$listByQuestionForIndex", Params.param("questionId", questionId)).list();
		Set<Integer> metaknows = new HashSet<Integer>(qmks.size());
		for (QuestionMetaKnow questionMetaKnow : qmks) {
			metaknows.add(questionMetaKnow.getMetaCode());
		}
		if (metaknows.size() > 0) {
			return Lists.newArrayList(mknowpointRepo.mget(metaknows).values());
		}

		return Lists.newArrayList();
	}

	private Map<Long, List<MetaKnowpoint>> mListByQuestions(Collection<Long> questionIds) {
		List<QuestionMetaKnow> qmks = qmknowpointRepo
				.find("$listByQuestionsForIndex", Params.param("questionIds", questionIds)).list();
		Set<Integer> metaknows = new HashSet<Integer>(qmks.size());
		for (QuestionMetaKnow questionMetaKnow : qmks) {
			metaknows.add(questionMetaKnow.getMetaCode());
		}
		if (metaknows.size() > 0) {
			Map<Long, List<MetaKnowpoint>> rmap = new HashMap<Long, List<MetaKnowpoint>>(questionIds.size());
			Map<Integer, MetaKnowpoint> metaKnowpointMap = mknowpointRepo.mget(metaknows);
			for (QuestionMetaKnow questionMetaKnow : qmks) {
				long questionId = questionMetaKnow.getQuestionId();
				if (rmap.get(questionId) == null) {
					rmap.put(questionId, new ArrayList<MetaKnowpoint>());
				}
				rmap.get(questionId).add(metaKnowpointMap.get(questionMetaKnow.getMetaCode()));
			}
			return rmap;
		}
		return Maps.newHashMap();
	}

	/**
	 * 获取子题.
	 * 
	 * @param id
	 * @return
	 */
	public List<Question> getSubQuestions(long id) {
		return questionRepo.find("$getSubQuestionsForIndex", Params.param("parentId", id)).list();
	}

	/**
	 * 获取子题.
	 * 
	 * @param id
	 * @return
	 */
	public Map<Long, List<Question>> mgetSubQuestions(Collection<Long> ids) {
		Map<Long, List<Question>> map = new HashMap<Long, List<Question>>(ids.size());
		List<Question> list = questionRepo.find("$mgetSubQuestions", Params.param("parentIds", ids)).list();
		for (Question question : list) {
			if (map.get(question.getParentId()) == null) {
				map.put(question.getParentId(), new ArrayList<Question>());
			}
			map.get(question.getParentId()).add(question);
		}
		return map;
	}

	/**
	 * 获取questionId里对应的教材和章节
	 * 
	 * @param questionId
	 * @return
	 */
	public Map<String, List<Long>> getQuestionSectionMap(Long questionId) {
		Map<String, List<Long>> map = new HashMap<String, List<Long>>();
		List<QuestionSection> qsList = questionSectionRepo
				.find("$getQuestionSectionListAllv", Params.param("questionId", questionId)).list();
		List<Long> textbookcodeList = new ArrayList<Long>();
		List<Long> textbookcodeList2 = new ArrayList<Long>();
		List<Long> sectionCodeList = new ArrayList<Long>();
		List<Long> sectionCode2List = new ArrayList<Long>();
		if (qsList.size() > 0) {
			for (QuestionSection qs : qsList) {
				if (null != qs.getV1() && qs.getV1()) {
					sectionCodeList.add(qs.getSectionCode());
					textbookcodeList.add((long) qs.getTextBookCode());
				}
				if (null != qs.getV2() && qs.getV2()) {
					sectionCode2List.add(qs.getSectionCode());
					textbookcodeList2.add((long) qs.getTextBookCode());
				}
			}
			int textbookcodeLength = qsList.get(0).getTextBookCode().toString().length();
			map.put("sectionCodes2", mgetListByChildIds(sectionCode2List, textbookcodeLength));
			map.put("sectionCodes", mgetListByChildIds(sectionCodeList, textbookcodeLength));
			map.put("textbookCodes", textbookcodeList);
			map.put("textbookCodes2", textbookcodeList2);
		}
		return map;
	}

	public Map<Long, Map<String, List<Long>>> mgetQuestionSectionMap(Collection<Long> questionIds) {
		Map<String, List<Long>> map = null;
		List<QuestionSection> qsList = questionSectionRepo
				.find("$mgetQuestionSectionListAllv", Params.param("questionIds", questionIds)).list();
		Map<Long, Map<String, List<Long>>> qsmap = new HashMap<Long, Map<String, List<Long>>>(qsList.size());
		List<Long> textbookcodeList = null;
		List<Long> textbookcodeList2 = null;
		List<Long> sectionCodeList = null;
		List<Long> sectionCode2List = null;
		if (qsList.size() > 0) {
			int textbookcodeLength = qsList.get(0).getTextBookCode().toString().length();
			for (Long id : questionIds) {
				sectionCodeList = new ArrayList<Long>();
				sectionCode2List = new ArrayList<Long>();
				textbookcodeList = new ArrayList<Long>();
				textbookcodeList2 = new ArrayList<Long>();
				map = new HashMap<String, List<Long>>();
				for (QuestionSection qs : qsList) {
					if (qs.getQuestionId() == id) {
						if (null != qs.getV1() && qs.getV1()) {
							sectionCodeList.add(qs.getSectionCode());
							textbookcodeList.add((long) qs.getTextBookCode());
						}
						if (null != qs.getV2() && qs.getV2()) {
							sectionCode2List.add(qs.getSectionCode());
							textbookcodeList2.add((long) qs.getTextBookCode());
						}
					}
				}
				map.put("textbookCodes2", textbookcodeList2);
				map.put("textbookCodes", textbookcodeList);
				map.put("sectionCodes", mgetListByChildIds(sectionCodeList, textbookcodeLength));
				map.put("sectionCodes2", mgetListByChildIds(sectionCode2List, textbookcodeLength));
				qsmap.put(id, map);
			}
		}

		return qsmap;
	}

	private List<Long> mgetListByChildIds(Collection<Long> sectionCodes, int length) {
		List<Long> list = new ArrayList<Long>();
		for (Long code : sectionCodes) {
			int start = length;
			int end = code.toString().length();
			for (int i = start + 2; i <= end; i = i + 2) {
				list.add(Long.parseLong(code.toString().substring(0, i)));
			}
		}
		return list;
	}

	// 新知识点
	private List<Long> knowledgePointListByQuestion(Long questionId) {
		return questionKnowledgeRepo.find("$indexFindKnowledgePointByQuestion", Params.param("questionId", questionId))
				.list(Long.class);
	}

	private Map<Long, List<Long>> knowledgePointMListByQuestions(Collection<Long> questionIds) {
		List<QuestionKnowledge> qks = questionKnowledgeRepo
				.find("$indexListByQuestions", Params.param("questionIds", questionIds)).list();
		if (qks.size() > 0) {
			Map<Long, List<Long>> rmap = new HashMap<Long, List<Long>>(questionIds.size());
			for (QuestionKnowledge questionKnowledge : qks) {
				long questionId = questionKnowledge.getQuestionId();
				long knowledgePointCode = questionKnowledge.getKnowledgeCode();
				if (rmap.get(questionId) == null) {
					rmap.put(questionId, new ArrayList<Long>());
				}
				rmap.get(questionId).add(knowledgePointCode);
			}
			return rmap;
		}
		return Maps.newHashMap();
	}

	// 同步知识点
	private List<Long> knowledgeSyncListByQuestion(Long questionId) {
		return questionKnowledgeSyncRepo
				.find("$indexFindKnowledgeSyncByQuestion", Params.param("questionId", questionId)).list(Long.class);
	}

	private Map<Long, List<Long>> knowledgeSyncMListByQuestions(Collection<Long> questionIds) {
		List<QuestionKnowledgeSync> qks = questionKnowledgeSyncRepo
				.find("$indexListByQuestions", Params.param("questionIds", questionIds)).list();
		if (qks.size() > 0) {
			Map<Long, List<Long>> rmap = new HashMap<Long, List<Long>>(questionIds.size());
			for (QuestionKnowledgeSync questionKnowledgeSync : qks) {
				long questionId = questionKnowledgeSync.getQuestionId();
				long knowledgeSyncCode = questionKnowledgeSync.getKnowledgeCode();
				if (rmap.get(questionId) == null) {
					rmap.put(questionId, new ArrayList<Long>());
				}
				rmap.get(questionId).add(knowledgeSyncCode);
			}
			return rmap;
		}
		return Maps.newHashMap();
	}

	// 复习知识点
	private List<Long> knowledgeReviewListByQuestion(Long questionId) {
		return questionKnowledgeReviewRepo
				.find("$indexFindKnowledgeReviewByQuestion", Params.param("questionId", questionId)).list(Long.class);
	}

	private Map<Long, List<Long>> knowledgeReviewMListByQuestions(Collection<Long> questionIds) {
		List<QuestionKnowledgeReview> qks = questionKnowledgeReviewRepo
				.find("$indexListByQuestions", Params.param("questionIds", questionIds)).list();
		if (qks.size() > 0) {
			Map<Long, List<Long>> rmap = new HashMap<Long, List<Long>>(questionIds.size());
			for (QuestionKnowledgeReview questionKnowledgeReview : qks) {
				long questionId = questionKnowledgeReview.getQuestionId();
				long knowledgeSyncReview = questionKnowledgeReview.getKnowledgeCode();
				if (rmap.get(questionId) == null) {
					rmap.put(questionId, new ArrayList<Long>());
				}
				rmap.get(questionId).add(knowledgeSyncReview);
			}
			return rmap;
		}
		return Maps.newHashMap();
	}

	// 考点
	public List<ExaminationPoint> examinationPointListByQuestion(Long questionId) {
		return questionExaminationRepo
				.find("$indexFindExaminationPointByQuestion", Params.param("questionId", questionId))
				.list(ExaminationPoint.class);
	}

	public Map<Long, List<ExaminationPoint>> examinationPointMListByQuestions(Collection<Long> questionIds) {
		List<QuestionExaminationPoint> qes = questionExaminationRepo
				.find("$indexListByQuestions", Params.param("questionIds", questionIds)).list();
		Set<Long> examinationPointCodes = new HashSet<Long>(qes.size());
		for (QuestionExaminationPoint questionExaminationPoint : qes) {
			examinationPointCodes.add(questionExaminationPoint.getExaminationPointCode());
		}
		if (examinationPointCodes.size() > 0) {
			Map<Long, ExaminationPoint> examinationPointMap = examinationPointRepo.mget(examinationPointCodes);
			Map<Long, List<ExaminationPoint>> rmap = new HashMap<Long, List<ExaminationPoint>>(questionIds.size());
			for (QuestionExaminationPoint questionExaminationPoint : qes) {
				long questionId = questionExaminationPoint.getQuestionId();
				long examinationPointCode = questionExaminationPoint.getExaminationPointCode();
				if (rmap.get(questionId) == null) {
					rmap.put(questionId, new ArrayList<ExaminationPoint>());
				}
				if (null != examinationPointMap.get(examinationPointCode)) {
					rmap.get(questionId).add(examinationPointMap.get(examinationPointCode));
				}
			}
			return rmap;
		}
		return Maps.newHashMap();
	}
}
