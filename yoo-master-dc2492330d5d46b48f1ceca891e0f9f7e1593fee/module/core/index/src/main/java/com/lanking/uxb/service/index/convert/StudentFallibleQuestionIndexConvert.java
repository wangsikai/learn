package com.lanking.uxb.service.index.convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionKnowledge;
import com.lanking.cloud.domain.common.resource.question.QuestionMetaKnow;
import com.lanking.cloud.domain.common.resource.question.QuestionSection;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.index.value.StudentFallibleQuestionDoc;

/**
 * 学生错题索引
 * 
 * @since yoomath V2.0
 * @author wangsenhao
 *
 */
@Component
public class StudentFallibleQuestionIndexConvert
		extends Converter<StudentFallibleQuestionDoc, StudentFallibleQuestion, Long> {

	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;
	@Autowired
	@Qualifier("QuestionKnowledgeRepo")
	Repo<QuestionKnowledge, Long> questionKnowledgeRepo;
	@Autowired
	@Qualifier("KnowledgePointRepo")
	Repo<KnowledgePoint, Long> knowledgePointRepo;
	@Autowired
	@Qualifier("QuestionSectionRepo")
	Repo<QuestionSection, Long> questionSectionRepo;
	@Autowired
	@Qualifier("QuestionMetaKnowRepo")
	Repo<QuestionMetaKnow, Integer> qmknowpointRepo;
	@Autowired
	@Qualifier("MetaKnowpointRepo")
	Repo<MetaKnowpoint, Integer> mknowpointRepo;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private KnowledgePointService knowledgePointService;

	@Override
	protected Long getId(StudentFallibleQuestion s) {
		return s.getId();
	}

	@Override
	protected StudentFallibleQuestionDoc convert(StudentFallibleQuestion s) {
		StudentFallibleQuestionDoc v = new StudentFallibleQuestionDoc();
		v.setId(s.getId());
		if (s.getQuestionId() != null) {
			v.setQuestionId(s.getQuestionId());
		} else {
			v.setQuestionId(0L);
		}
		v.setStudentId(s.getStudentId());
		v.setMistakeNum(s.getMistakeNum());
		if (s.getLatestSource() != null) {
			v.setLatestSource(s.getLatestSource().getValue());
		} else {
			v.setLatestSource(0);
		}
		v.setDifficulty(BigDecimal.valueOf(s.getDifficulty() == null ? 1.0f : s.getDifficulty()));
		v.setCreateAt(s.getCreateAt().getTime());
		v.setStatus(s.getStatus().getValue());
		v.setUpdateAt(s.getUpdateAt() == null ? s.getCreateAt().getTime() : s.getUpdateAt().getTime());
		if (s.getOcrImageId() != null) {
			v.setOcrImageId(s.getOcrImageId());
		}
		if (s.getOcrKnowpointCodes() != null) {
			v.setOcrKnowpointCodes(s.getOcrKnowpointCodes());
		}
		if (s.getOcrTextbookCode() != null) {
			v.setOcrTextbookCode(s.getOcrTextbookCode());
		}
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {

		assemblers.add(new ConverterAssembler<StudentFallibleQuestionDoc, StudentFallibleQuestion, Long, Question>() {

			@Override
			public boolean accept(StudentFallibleQuestion s) {
				return s.getQuestionId() != null;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(StudentFallibleQuestion s, StudentFallibleQuestionDoc d) {
				return s.getQuestionId();
			}

			@Override
			public void setValue(StudentFallibleQuestion s, StudentFallibleQuestionDoc d, Question value) {
				if (value != null) {
					d.setTextbookCategoryCode(value.getTextbookCategoryCode());
					d.setDifficulty(BigDecimal.valueOf(value.getDifficulty() == null ? 1.0f : value.getDifficulty()));
					StringBuffer contentbuff = new StringBuffer(value.getContent());
					if (value.getType() == Question.Type.SINGLE_CHOICE
							|| value.getType() == Question.Type.MULTIPLE_CHOICE) {
						contentbuff.append(StringUtils.defaultIfBlank(value.getChoiceA()))
								.append(StringUtils.defaultIfBlank(value.getChoiceB()))
								.append(StringUtils.defaultIfBlank(value.getChoiceC()))
								.append(StringUtils.defaultIfBlank(value.getChoiceD()))
								.append(StringUtils.defaultIfBlank(value.getChoiceE()))
								.append(StringUtils.defaultIfBlank(value.getChoiceF()));
					}
					d.setContents(contentbuff.toString());
					d.setTypeCode(value.getTypeCode());
					d.setType(value.getType().getValue());
				}
			}

			@Override
			public Question getValue(Long key) {
				if (key == null) {
					return null;
				}

				return questionRepo.get(key);
			}

			@Override
			public Map<Long, Question> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				return questionRepo.mget(keys);
			}
		});
		// 知识点（旧）
		assemblers.add(
				new ConverterAssembler<StudentFallibleQuestionDoc, StudentFallibleQuestion, Long, List<MetaKnowpoint>>() {

					@Override
					public boolean accept(StudentFallibleQuestion s) {
						return s.getQuestionId() != null;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(StudentFallibleQuestion s, StudentFallibleQuestionDoc d) {
						return s.getQuestionId();
					}

					@Override
					public void setValue(StudentFallibleQuestion s, StudentFallibleQuestionDoc d,
							List<MetaKnowpoint> value) {
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
											metaKnowpointCodes.add(Integer
													.parseInt(metaKnowpoint.getCode().toString().substring(0, i)));
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
		// 知识点（新）
		assemblers.add(
				new ConverterAssembler<StudentFallibleQuestionDoc, StudentFallibleQuestion, Long, List<KnowledgePoint>>() {

					@Override
					public boolean accept(StudentFallibleQuestion s) {
						return s.getQuestionId() != null;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(StudentFallibleQuestion s, StudentFallibleQuestionDoc d) {
						return s.getQuestionId();
					}

					@Override
					public void setValue(StudentFallibleQuestion s, StudentFallibleQuestionDoc d,
							List<KnowledgePoint> value) {
						if (CollectionUtils.isNotEmpty(value)) {
							StringBuffer buff = new StringBuffer();
							List<Long> temp = Lists.newArrayList();
							for (KnowledgePoint point : value) {
								if (null != point) {
									buff.append(point.getName());
									temp.add(point.getCode());
									temp.addAll(findAllParentsCodeByKPoint(point.getCode()));
								}
							}
							d.setKnowpointnames(buff.toString());
							d.setKnowpointCodes(temp);
						}
					}

					@Override
					public List<KnowledgePoint> getValue(Long key) {
						if (key == null) {
							return null;
						}
						return newlistByQuestion(key);
					}

					@Override
					public Map<Long, List<KnowledgePoint>> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Maps.newHashMap();
						}
						return newmListByQuestions(keys);
					}

				});
		// 教材章节
		assemblers.add(
				new ConverterAssembler<StudentFallibleQuestionDoc, StudentFallibleQuestion, Long, Map<String, List<Long>>>() {

					@Override
					public boolean accept(StudentFallibleQuestion s) {
						return s.getQuestionId() != null;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(StudentFallibleQuestion s, StudentFallibleQuestionDoc d) {
						return s.getQuestionId();
					}

					@Override
					public void setValue(StudentFallibleQuestion s, StudentFallibleQuestionDoc d,
							Map<String, List<Long>> value) {
						if (value.get("sectionCodes") != null) {
							d.setSectionCodes(value.get("sectionCodes"));
						}
						if (value.get("textbookCodes") != null) {
							d.setTextbookCodes(value.get("textbookCodes"));
						}
						if (value.get("textbookCategoryCodes") != null) {
							d.setTextbookCategoryCodes(value.get("textbookCategoryCodes"));
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
		assemblers.add(new ConverterAssembler<StudentFallibleQuestionDoc, StudentFallibleQuestion, Long, String>() {
			@Override
			public boolean accept(StudentFallibleQuestion s) {
				return s.getQuestionId() != null;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(StudentFallibleQuestion s, StudentFallibleQuestionDoc d) {
				return s.getQuestionId();
			}

			@Override
			public void setValue(StudentFallibleQuestion s, StudentFallibleQuestionDoc d, String value) {
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

	}

	/**
	 * 旧知识点相关查询
	 * 
	 * @param questionId
	 * @return
	 */
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
	 * 新知识点相关查询
	 * 
	 * @param questionId
	 * @return
	 */
	private List<KnowledgePoint> newlistByQuestion(long questionId) {
		List<QuestionKnowledge> qmks = questionKnowledgeRepo
				.find("$listByQuestionForIndex", Params.param("questionId", questionId)).list();
		List<Long> points = new ArrayList<Long>(qmks.size());
		for (QuestionKnowledge point : qmks) {
			points.add(point.getKnowledgeCode());
		}
		if (points.size() > 0) {
			return Lists.newArrayList(knowledgePointRepo.mget(points).values());
		}

		return Lists.newArrayList();
	}

	private Map<Long, List<KnowledgePoint>> newmListByQuestions(Collection<Long> questionIds) {
		List<QuestionKnowledge> qmks = questionKnowledgeRepo
				.find("$indexListByQuestions", Params.param("questionIds", questionIds)).list();
		List<Long> points = new ArrayList<Long>(qmks.size());
		for (QuestionKnowledge point : qmks) {
			points.add(point.getKnowledgeCode());
		}
		if (points.size() > 0) {
			Map<Long, List<KnowledgePoint>> rmap = new HashMap<Long, List<KnowledgePoint>>(questionIds.size());
			Map<Long, KnowledgePoint> metaKnowpointMap = knowledgePointRepo.mget(points);
			for (QuestionKnowledge point : qmks) {
				long questionId = point.getQuestionId();
				if (rmap.get(questionId) == null) {
					rmap.put(questionId, new ArrayList<KnowledgePoint>());
				}
				rmap.get(questionId).add(metaKnowpointMap.get(point.getKnowledgeCode()));
			}
			return rmap;
		}
		return Maps.newHashMap();
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
				.find("$getQuestionSectionList", Params.param("questionId", questionId)).list();
		List<Long> textbookcodeList = new ArrayList<Long>();
		List<Long> sectionCodeList = new ArrayList<Long>();
		List<Long> textbookCategoryCodeList = new ArrayList<Long>();
		if (qsList.size() > 0) {
			for (QuestionSection qs : qsList) {
				textbookcodeList.add((long) qs.getTextBookCode());
				sectionCodeList.add(qs.getSectionCode());
				textbookCategoryCodeList.add(Long.parseLong(qs.getTextBookCode().toString().substring(0, 2)));
			}
			int textbookcodeLength = qsList.get(0).getTextBookCode().toString().length();
			map.put("sectionCodes", mgetListByChildIds(sectionCodeList, textbookcodeLength));
			map.put("textbookCodes", textbookcodeList);
			map.put("textbookCategoryCodes", textbookCategoryCodeList);
		}
		return map;
	}

	public Map<Long, Map<String, List<Long>>> mgetQuestionSectionMap(Collection<Long> questionIds) {
		Map<String, List<Long>> map = null;
		List<QuestionSection> qsList = questionSectionRepo
				.find("$mgetQuestionSectionList", Params.param("questionIds", questionIds)).list();
		Map<Long, Map<String, List<Long>>> qsmap = new HashMap<Long, Map<String, List<Long>>>(qsList.size());
		List<Long> textbookcodeList = null;
		List<Long> sectionCodeList = null;
		List<Long> textbookCategoryCodeList = null;
		if (qsList.size() > 0) {
			int textbookcodeLength = qsList.get(0).getTextBookCode().toString().length();
			for (Long id : questionIds) {
				sectionCodeList = new ArrayList<Long>();
				textbookcodeList = new ArrayList<Long>();
				textbookCategoryCodeList = new ArrayList<Long>();
				map = new HashMap<String, List<Long>>();
				for (QuestionSection qs : qsList) {
					if (qs.getQuestionId() == id) {
						textbookcodeList.add(qs.getTextBookCode().longValue());
						sectionCodeList.add(qs.getSectionCode());
						textbookCategoryCodeList.add(Long.parseLong(qs.getTextBookCode().toString().substring(0, 2)));
					}
				}
				map.put("textbookCodes", textbookcodeList);
				map.put("sectionCodes", mgetListByChildIds(sectionCodeList, textbookcodeLength));
				map.put("textbookCategoryCodes", textbookCategoryCodeList);
				qsmap.put(id, map);
			}
		}

		return qsmap;
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

	/**
	 * 知识点查询所有父级
	 * 
	 * @param code
	 * @return
	 */
	public List<Long> findAllParentsCodeByKPoint(Long code) {
		// 第三层 知识专项
		String third = code.toString().substring(0, code.toString().length() - 2);
		// 第二层 小专题
		String second = third.substring(0, third.length() - 1);
		// 第一层 大专题
		String first = second.substring(0, second.length() - 2);
		List<Long> codes = new ArrayList<Long>();
		codes.add(Long.valueOf(first));
		codes.add(Long.valueOf(second));
		codes.add(Long.valueOf(third));
		return codes;
	}

}
