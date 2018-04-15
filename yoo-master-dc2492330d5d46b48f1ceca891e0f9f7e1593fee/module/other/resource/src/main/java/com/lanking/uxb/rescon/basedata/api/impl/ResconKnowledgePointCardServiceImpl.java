package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.QuestionTag;
import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.domain.common.resource.card.KnowledgePointCard;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionCategoryType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointCardService;
import com.lanking.uxb.rescon.basedata.form.ResconKnowledgePointCardForm;
import com.lanking.uxb.rescon.question.api.ResconQuestion2TagManage;

/**
 * @author xinyu.zhou
 * @since 2.0.1
 * @since 教师端 v1.3.0 2017-8-2 添加新的标签处理
 */
@Service
@Transactional(readOnly = true)
public class ResconKnowledgePointCardServiceImpl implements ResconKnowledgePointCardService {
	@Autowired
	@Qualifier("KnowledgePointCardRepo")
	private Repo<KnowledgePointCard, Long> repo;
	@Autowired
	@Qualifier("QuestionRepo")
	private Repo<Question, Long> questionRepo;
	// @Autowired
	// private ResconQuestionTypeCacheService qtcService;

	@Autowired
	private ResconQuestion2TagManage question2TagManage;

	@Override
	@Transactional
	public List<Long> save(ResconKnowledgePointCardForm form, long userId) {
		KnowledgePointCard knowledgePointCard = null;
		List<Long> needUpdateQuestions = Lists.newArrayList();
		if (form.getId() == null) {
			knowledgePointCard = new KnowledgePointCard();
			knowledgePointCard.setCreateAt(new Date());
			knowledgePointCard.setCreateId(userId);
			knowledgePointCard.setCheckStatus(form.getCheckStatus());
		} else {
			knowledgePointCard = repo.get(form.getId());
			knowledgePointCard.setUpdateAt(new Date());
			knowledgePointCard.setUpdateId(userId);
			if (knowledgePointCard.getCheckStatus() == CardStatus.DRAFT) {
				knowledgePointCard.setCheckStatus(form.getCheckStatus());
			} else if (knowledgePointCard.getCheckStatus() == CardStatus.NOPASS) {
				if (form.getCheckStatus() == CardStatus.EDITING) {
					knowledgePointCard.setCheckStatus(CardStatus.EDITING);
				}
			} else if (knowledgePointCard.getCheckStatus() == CardStatus.PASS) {

				// since 2.1.0 按照原先逻辑，若卡片已经通过审核了，这时候再进行保存还是审核状态
				// 判断其中的例题是否增加还是减少了，分别针对这些题进行处理
				List<Long> questionIds = knowledgePointCard.getQuestions();
				List<Long> newQuestionIds = form.getQuestions();

				if (CollectionUtils.isEmpty(newQuestionIds)) {
					needUpdateQuestions.addAll(removeQuestionCategory(questionIds));
				} else {
					// 若以前的例题都为空则这时直接将新的题目都打上例题标签
					if (CollectionUtils.isEmpty(questionIds)) {
						needUpdateQuestions.addAll(addQuestionCategory(newQuestionIds));
					} else {
						List<Long> needAddQuestionIds = Lists.newArrayList();
						List<Long> needRemoveQuestionIds = Lists.newArrayList();
						for (Long id : questionIds) {
							if (newQuestionIds.contains(id)) {
								continue;
							}

							needRemoveQuestionIds.add(id);
						}

						for (Long id : newQuestionIds) {
							if (questionIds.contains(id)) {
								continue;
							}

							needAddQuestionIds.add(id);
						}

						if (CollectionUtils.isNotEmpty(needAddQuestionIds)) {
							needUpdateQuestions.addAll(addQuestionCategory(needAddQuestionIds));
						}

						if (CollectionUtils.isNotEmpty(needRemoveQuestionIds)) {
							needUpdateQuestions.addAll(removeQuestionCategory(needRemoveQuestionIds));
						}

					}
				}

			}
		}

		knowledgePointCard.setDelStatus(Status.ENABLED);
		knowledgePointCard.setDescription(form.getDescription());
		knowledgePointCard.setDetailDescription(form.getDetailDescription());
		knowledgePointCard.setHints(form.getHints());
		if (form.getKnowpointCode() > 0) {
			knowledgePointCard.setKnowpointCode(form.getKnowpointCode());
		}
		knowledgePointCard.setQuestions(form.getQuestions());
		knowledgePointCard.setName(form.getName());
		knowledgePointCard.setQuestionCount(form.getQuestions() == null ? 0 : form.getQuestions().size());
		repo.save(knowledgePointCard);

		return needUpdateQuestions;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	private List<Long> removeQuestionCategory(List<Long> questionIds) {
		question2TagManage.systemDel(questionIds, QuestionTag.getTagCode(QuestionCategoryType.EXAMPLE));

		// if (questionIds != null && questionIds.size() > 0) {
		// List<Long> needUpdateQuestions = new
		// ArrayList<Long>(questionIds.size());
		// if (CollectionUtils.isNotEmpty(questionIds)) {
		// List<Long> removedIds =
		// qtcService.remove(QuestionCategoryType.EXAMPLE, questionIds);
		// // 以下题目需要移除EXAMPLE标签
		// if (CollectionUtils.isNotEmpty(removedIds)) {
		// List<Question> questions = questionRepo.mgetList(removedIds);
		//
		// for (Question q : questions) {
		// List<QuestionCategoryType> categoryTypes = q.getCategoryTypes();
		// if (CollectionUtils.isNotEmpty(categoryTypes)) {
		// categoryTypes.remove(QuestionCategoryType.EXAMPLE);
		// q.setCategoryTypes(categoryTypes);
		//
		// needUpdateQuestions.add(q.getId());
		//
		// questionRepo.save(q);
		// }
		// }
		//
		// }
		// }
		//
		// return needUpdateQuestions;
		// }
		//
		return CollectionUtils.isEmpty(questionIds) ? Collections.EMPTY_LIST : questionIds;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	private List<Long> addQuestionCategory(List<Long> questionIds) {
		question2TagManage.systemAdd(questionIds, QuestionTag.getTagCode(QuestionCategoryType.EXAMPLE));
		return CollectionUtils.isEmpty(questionIds) ? Collections.EMPTY_LIST : questionIds;

		// qtcService.add(QuestionCategoryType.EXAMPLE, questionIds);
		// List<Question> questions = questionRepo.mgetList(questionIds);
		// List<Long> needUpdateQuestions = new
		// ArrayList<Long>(questionIds.size());
		// for (Question q : questions) {
		// if (q.getCategoryTypes() == null) {
		// List<QuestionCategoryType> categoryTypes = new
		// ArrayList<QuestionCategoryType>(1);
		// categoryTypes.add(QuestionCategoryType.EXAMPLE);
		//
		// q.setCategoryTypes(categoryTypes);
		//
		// needUpdateQuestions.add(q.getId());
		//
		// questionRepo.save(q);
		// } else {
		// if (!q.getCategoryTypes().contains(QuestionCategoryType.EXAMPLE)) {
		// q.getCategoryTypes().add(QuestionCategoryType.EXAMPLE);
		//
		// needUpdateQuestions.add(q.getId());
		//
		// questionRepo.save(q);
		// }
		// }
		// }
		//
		// return needUpdateQuestions;
	}

	@Override
	public List<KnowledgePointCard> findByKnowpointCode(long code) {
		return repo.find("$findByKnowpointCode", Params.param("code", code)).list();
	}

	@Override
	public List<KnowledgePointCard> findByKnowpointCode(long code, CardStatus cardStatus) {
		return repo.find("$findByKnowpointCode", Params.param("code", code).put("cardStatus", cardStatus.getValue()))
				.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<KnowledgePointCard> findByKnowpointCode(Collection<Long> codes, CardStatus cardStatus) {
		if (CollectionUtils.isEmpty(codes)) {
			return Collections.EMPTY_LIST;
		}
		return repo.find("$findByKnowpointCodes", Params.param("codes", codes).put("cardStatus", cardStatus.getValue()))
				.list();
	}

	@Override
	public KnowledgePointCard get(long id) {
		return repo.get(id);
	}

	@Override
	@Transactional
	public List<Long> use(long id) {
		return updateStatus(id, Status.ENABLED);
	}

	@Override
	@Transactional
	public List<Long> forbid(long id) {
		return updateStatus(id, Status.DISABLED);
	}

	@Override
	@Transactional
	public void delete(long id) {
		repo.execute("$delete", Params.param("id", id));
	}

	@Transactional
	private List<Long> updateStatus(long id, Status status) {
		List<Long> needUpdateQuestions = null;
		// 若对此卡片禁用，则其卡片下的所有例题标签全部去除
		if (status == Status.DISABLED) {
			KnowledgePointCard card = repo.get(id);
			List<Long> questionIds = card.getQuestions();
			if (CollectionUtils.isNotEmpty(questionIds)) {
				needUpdateQuestions = removeQuestionCategory(questionIds);
			}
		} else if (status == Status.ENABLED) {
			// 启用后则对此卡片下的所有例题进行处理，重新添加题目标签
			KnowledgePointCard card = repo.get(id);
			List<Long> questionIds = card.getQuestions();
			if (CollectionUtils.isNotEmpty(questionIds)) {
				needUpdateQuestions = addQuestionCategory(questionIds);
			}
		}
		repo.execute("$updateStatus", Params.param("id", id).put("status", status.getValue()));

		return needUpdateQuestions;

	}

	@Override
	@Transactional
	public List<Long> updateCardStatus(long id, CardStatus status) {
		KnowledgePointCard card = repo.get(id);
		List<Long> questionIds = card.getQuestions();
		List<Long> needUpdateQuestions = null;
		// 通过审核后并且题目不为空，则这些题目标签增加"例题"
		if (CollectionUtils.isNotEmpty(questionIds) && status == CardStatus.PASS) {
			needUpdateQuestions = addQuestionCategory(questionIds);
		}

		card.setCheckStatus(status);
		repo.save(card);

		return needUpdateQuestions;
	}

	@Override
	public long questionCount(int subjectCode) {
		String code = subjectCode + "%";
		Long qCount = repo.find("$questionCount", Params.param("code", code)).get(Long.class);
		return qCount == null ? 0 : qCount;
	}

	@Override
	public Map<CardStatus, Long> statusCount(int subjectCode) {
		String code = subjectCode + "%";
		List<Map> resultMap = repo.find("$statusCount", Params.param("code", code)).list(Map.class);
		Map<CardStatus, Long> retMap = new HashMap<CardStatus, Long>(resultMap.size());
		for (Map m : resultMap) {
			retMap.put(CardStatus.findByValue(Integer.valueOf(m.get("s").toString())),
					Long.valueOf(m.get("c").toString()));
		}
		return retMap;
	}
}
