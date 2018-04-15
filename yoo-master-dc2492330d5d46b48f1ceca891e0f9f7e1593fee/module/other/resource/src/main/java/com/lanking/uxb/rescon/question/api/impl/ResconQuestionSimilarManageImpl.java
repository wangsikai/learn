package com.lanking.uxb.rescon.question.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionSimilar;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.question.api.ResconQuestionSimilarManage;
import com.lanking.uxb.rescon.question.form.SimilarQuestionsForm;
import com.lanking.uxb.rescon.question.form.SimilarSameQuestionForm;
import com.lanking.uxb.service.search.api.IndexBuildService;
import com.lanking.uxb.service.search.api.IndexService;

@Service
@Transactional(readOnly = true)
public class ResconQuestionSimilarManageImpl implements ResconQuestionSimilarManage {
	@Autowired
	@Qualifier("QuestionSimilarRepo")
	Repo<QuestionSimilar, Long> questionSimilarRepo;
	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@Autowired
	private IndexService indexService;
	@Autowired
	@Qualifier(value = "questionSimilarIndexHandle")
	private IndexBuildService indexBuildService;

	@Override
	public QuestionSimilar getQuestionSimilar(long id) {
		return questionSimilarRepo.get(id);
	}

	@Override
	@Transactional
	public void saveSimilarQuestions(SimilarQuestionsForm form, long vendorId) {

		// 相似题组处理
		if (CollectionUtils.isNotEmpty(form.getLikeIds()) && form.getLikeIds().size() > 0) {
			// 查找已有数据
			Map<Long, Question> questionMap = questionRepo.mget(form.getLikeIds());
			List<QuestionSimilar> questionSimilars = questionSimilarRepo
					.find("$queryNewDatasByQuestions", Params.param("questionIds", form.getLikeIds())).list();
			Map<Long, QuestionSimilar> questionSimilarMap = new HashMap<Long, QuestionSimilar>(questionSimilars.size());
			for (QuestionSimilar qs : questionSimilars) {
				questionSimilarMap.put(qs.getBaseQuestionId(), qs);
			}
			List<Question> updateQuestions = new ArrayList<Question>(); // 包含相似题的题目
			Set<Long> sqIds = Sets.newHashSet(form.getLikeIds());

			for (Long qid : sqIds) {
				QuestionSimilar questionSimilar = questionSimilarMap.get(qid);
				if (questionSimilar == null) {
					Question question = questionMap.get(qid);
					long showCount = this.geyQuestionTypesCount(Lists.newArrayList(sqIds), null);

					questionSimilar = new QuestionSimilar();
					questionSimilar.setBaseQuestionId(qid);
					questionSimilar.setCreateAt(new Date());
					questionSimilar.setLikeQuestions(Lists.newArrayList(sqIds));
					questionSimilar.setStatus(Status.ENABLED);
					questionSimilar.setChangeFlag(true);
					questionSimilar.setVendorId(question.getVendorId()); // 资源商
					questionSimilar.setShowCount((int) showCount);
					questionSimilarMap.put(qid, questionSimilar);

					question.setHasSimilar(true);
					updateQuestions.add(question);
				} else {
					// 控制相似题的数量
					if (questionSimilar.getLikeQuestions().size() < 50) {
						Set<Long> likes = Sets.newHashSet(questionSimilar.getLikeQuestions());
						likes.addAll(form.getLikeIds());
						long showCount = this.geyQuestionTypesCount(Lists.newArrayList(likes), null);
						questionSimilar.setShowCount((int) showCount);
						questionSimilar.setLikeQuestions(Lists.newArrayList(likes));
					}
				}
			}
			questionRepo.save(updateQuestions);
			questionSimilarRepo.save(questionSimilarMap.values());
		}

		// 原题处理
		List<SimilarSameQuestionForm> similarSameQuestionForms = form.getSimilarSameQuestionForms();
		if (similarSameQuestionForms != null && similarSameQuestionForms.size() > 0) {
			Set<Long> showQuestionIds = new HashSet<Long>();
			Set<Long> sameQuestionIds = new HashSet<Long>();
			for (SimilarSameQuestionForm sf : similarSameQuestionForms) {
				showQuestionIds.add(sf.getShowId());
				sameQuestionIds.addAll(sf.getSameIds());
			}
			Set<Long> queryIds = new HashSet<Long>(showQuestionIds);
			queryIds.addAll(sameQuestionIds);
			Map<Long, Question> questions = questionRepo.mget(queryIds);
			for (SimilarSameQuestionForm sf : similarSameQuestionForms) {
				questions.get(sf.getShowId()).setSameShow(true);
				for (Long sameId : sf.getSameIds()) {
					questions.get(sameId).setSameShow(false);
					questions.get(sameId).setSameShowId(sf.getShowId());
				}
			}
			questionRepo.save(questions.values());

			// 更新索引
			indexService.syncUpdate(IndexType.QUESTION, queryIds);
		}

		// 删除当前操作题组
		indexBuildService.deleteDocument(IndexType.QUESTION_SIMILAR, form.getMd5());
	}

	@Override
	public Map<String, Object> similarCounts(long vendorId) {
		Map<String, Object> countMap = new HashMap<String, Object>(2);
		long similarCounts = questionSimilarRepo.find("$similarCounts", Params.param("vendorId", vendorId)).count();
		long sameCounts = questionSimilarRepo.find("$sameCounts", Params.param("vendorId", vendorId)).count();
		countMap.put("similarCounts", similarCounts);
		countMap.put("sameCounts", sameCounts);
		return countMap;
	}

	@Override
	public List<Question> listSimilarQuestionsForWeb(long qid, int size) {
		QuestionSimilar questionSimilar = questionSimilarRepo.find("$getByBaseId", Params.param("baseQuestionId", qid))
				.get();
		if (questionSimilar != null) {
			List<Long> questionIds = questionSimilar.getLikeQuestions();
			Map<Long, Question> questions = questionRepo.mget(questionIds);
			List<Question> results = new ArrayList<Question>(questions.size());
			for (long questionId : questionIds) {
				if (questionId == qid) {
					questions.remove(qid);
					continue;
				}
				Question question = questions.get(questionId);
				if (question.getSameShowId() != null) {
					// 展示题替换
					results.add(questionRepo.get(question.getSameShowId()));
				} else {
					results.add(question);
				}
				if (results.size() >= size) {
					break;
				}
			}
			return results;
		} else {
			return Lists.newArrayList();
		}
	}

	@Override
	public Page<QuestionSimilar> queryOldDatas(Pageable pageable) {
		Page<QuestionSimilar> page = questionSimilarRepo.find("$queryOldDatas").fetch(pageable);
		return page;
	}

	@Override
	@Transactional
	public void buildNewDatas(List<QuestionSimilar> oldDatas) {
		// 所有习题ID集合
		Set<Long> questionIds = new HashSet<Long>();
		for (QuestionSimilar qs : oldDatas) {
			questionIds.addAll(qs.getLikeQuestions());
		}

		// 批量找到新数据集合
		Map<Long, Question> questionMap = questionRepo.mget(questionIds);
		List<QuestionSimilar> questionSimilars = questionSimilarRepo
				.find("$queryNewDatasByQuestions", Params.param("questionIds", questionIds)).list();
		Map<Long, QuestionSimilar> questionSimilarMap = new HashMap<Long, QuestionSimilar>(questionSimilars.size());
		for (QuestionSimilar qs : questionSimilars) {
			questionSimilarMap.put(qs.getBaseQuestionId(), qs);
		}

		List<Question> updateQuestions = new ArrayList<Question>(); // 包含相似题的题目
		for (QuestionSimilar qs : oldDatas) {
			// 只有已通过的题目做相似
			for (int i = qs.getLikeQuestions().size() - 1; i >= 0; i--) {
				Question question = questionMap.get(qs.getLikeQuestions().get(i));
				if (question == null || question.getStatus() != CheckStatus.PASS) {
					qs.getLikeQuestions().remove(i);
				}
			}
			if (qs.getLikeQuestions().size() <= 1) {
				continue;
			}
			Set<Long> sqIds = Sets.newHashSet(qs.getLikeQuestions());

			for (Long qid : sqIds) {
				Question question = questionMap.get(qid);
				QuestionSimilar questionSimilar = questionSimilarMap.get(qid);
				if (questionSimilar == null) {
					long showCount = this.geyQuestionTypesCount(Lists.newArrayList(sqIds), null);
					questionSimilar = new QuestionSimilar();
					questionSimilar.setBaseQuestionId(qid);
					questionSimilar.setCreateAt(new Date());
					questionSimilar.setLikeQuestions(Lists.newArrayList(sqIds));
					questionSimilar.setStatus(Status.ENABLED);
					questionSimilar.setChangeFlag(true);
					questionSimilar.setVendorId(question.getVendorId());
					questionSimilar.setShowCount((int) showCount);
					questionSimilarMap.put(qid, questionSimilar);

					question.setHasSimilar(true);
					updateQuestions.add(question);
				} else {
					// 控制相似题的数量
					if (questionSimilar.getLikeQuestions().size() < 50) {
						Set<Long> likes = Sets.newHashSet(questionSimilar.getLikeQuestions());
						likes.addAll(sqIds);
						long showCount = this.geyQuestionTypesCount(Lists.newArrayList(likes), null);
						questionSimilar.setLikeQuestions(Lists.newArrayList(likes));
						questionSimilar.setShowCount((int) showCount);
					}
				}
			}
		}

		questionRepo.save(updateQuestions);
		questionSimilarRepo.save(questionSimilarMap.values());
	}

	@Override
	public long geyQuestionTypesCount(List<Long> questionIds, List<Integer> types) {
		if (CollectionUtils.isEmpty(types)) {
			types = Lists.newArrayList(Question.Type.SINGLE_CHOICE.getValue(), Question.Type.FILL_BLANK.getValue(),
					Question.Type.QUESTION_ANSWERING.getValue());
		}
		return questionSimilarRepo
				.find("$getQuestionTypesCount", Params.param("questionIds", questionIds).put("types", types))
				.get(Long.class);
	}
}
