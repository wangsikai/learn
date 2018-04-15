package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.baseData.QuestionTag;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionCategoryType;
import com.lanking.cloud.domain.common.resource.question.QuestionExaminationPoint;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.uxb.rescon.basedata.api.ResconExaminationPointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeSystemService;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionExaminationPointService;
import com.lanking.uxb.rescon.basedata.form.ResconExaminationPointForm;
import com.lanking.uxb.rescon.question.api.ResconQuestion2TagManage;
import com.lanking.uxb.service.code.api.ExaminationPointKnowledgePointService;
import com.lanking.uxb.service.search.api.IndexService;

/**
 * 
 * @since 教师端 v1.3.0 2017-8-2 添加新的标签处理
 */
@Service
@Transactional(readOnly = true)
public class ResconExaminationPointServiceImpl implements ResconExaminationPointService {
	@Autowired
	@Qualifier("ExaminationPointRepo")
	private Repo<ExaminationPoint, Long> examinationPointRepo;
	@Autowired
	@Qualifier("QuestionExaminationPointRepo")
	private Repo<QuestionExaminationPoint, Long> questionExaminationPointRepo;
	@Autowired
	@Qualifier("QuestionRepo")
	private Repo<Question, Long> questionRepo;

	@Autowired
	private ResconKnowledgeSystemService knowledgeSystem;
	@Autowired
	private IndexService indexService;
	@Autowired
	private ResconQuestionExaminationPointService questionExaminationPointService;
	// @Autowired
	// private ResconQuestionTypeCacheService qtcService;
	@Autowired
	private ExaminationPointKnowledgePointService examinationPointKnowledgePointService;

	@Autowired
	private ResconQuestion2TagManage question2TagManage;

	@Override
	public List<ExaminationPoint> list(Integer phaseCode, Integer subjectCode, Status status) {
		Params params = Params.param("phaseCode", phaseCode).put("subjectCode", subjectCode);
		if (status != null) {
			params.put("status", status.getValue());
		}
		return examinationPointRepo.find("$find", params).list();
	}

	@Override
	public List<ExaminationPoint> findPoint(Integer phaseCode, Integer subjectCode, Long pcode, Status status) {
		Params params = Params.param("phaseCode", phaseCode).put("subjectCode", subjectCode);
		if (status != null) {
			params.put("status", status.getValue());
		}
		if (pcode != null) {
			params.put("pcode", pcode);
		}
		return examinationPointRepo.find("$find", params).list();
	}

	@Override
	public List<ExaminationPoint> listUse(Integer phaseCode, Integer subjectCode) {
		return examinationPointRepo
				.find("$findUse", Params.param("phaseCode", phaseCode).put("subjectCode", subjectCode)).list();
	}

	@Override
	public Map<Long, List<ExaminationPoint>> queryBySpecialKnowledgePoints(Integer phaseCode, Integer subjectCode) {
		Map<Long, List<ExaminationPoint>> map = new HashMap<Long, List<ExaminationPoint>>();
		List<ExaminationPoint> eps = examinationPointRepo
				.find("$find", Params.param("phaseCode", phaseCode).put("subjectCode", subjectCode)).list();
		for (ExaminationPoint ep : eps) {
			List<ExaminationPoint> list = map.get(ep.getPcode());
			if (list == null) {
				list = new ArrayList<ExaminationPoint>();
				map.put(ep.getPcode(), list);
			}
			list.add(ep);
		}

		return map;
	}

	@Override
	public ExaminationPoint get(long id) {
		return examinationPointRepo.get(id);
	}

	@Override
	public String getLevelDesc(long code) {
		ExaminationPoint k1 = this.get(code);
		String name1 = k1.getName();
		KnowledgeSystem k2 = knowledgeSystem.get(k1.getPcode());
		String name2 = k2.getName();
		KnowledgeSystem k3 = knowledgeSystem.get(k2.getPcode());
		String name3 = k3.getName();
		KnowledgeSystem k4 = knowledgeSystem.get(k3.getPcode());
		String name4 = k4.getName();
		String temp = name4 + '>' + name3 + '>' + name2 + '>' + name1;
		return temp;
	}

	@Override
	@Transactional
	public void save(ResconExaminationPointForm form) {
		ExaminationPoint examinationPoint = null;
		if (form.getId() != null) {
			examinationPoint = examinationPointRepo.get(form.getId());
		} else {
			examinationPoint = new ExaminationPoint();
		}
		examinationPoint.setFrequency(form.getFrequency());
		if (null != form.getKnowpoints()) {
			examinationPoint.setKnowpointCount(form.getKnowpoints().size());
		}
		if (null != form.getQuestions()) {
			examinationPoint.setQuestions(form.getQuestions());
			examinationPoint.setQuestionCount(form.getQuestions().size());
			// 添加标签
			question2TagManage.systemAdd(form.getQuestions(), QuestionTag.getTagCode(QuestionCategoryType.TYPICAL));
		}
		examinationPoint.setName(form.getName());
		examinationPoint.setPcode(form.getPcode());
		examinationPoint.setPhaseCode(form.getPhaseCode());
		examinationPoint.setSubjectCode(form.getSubjectCode());
		if (form.getEditFlag() == 0) {
			examinationPoint.setStatus(form.getStatus());
		}
		examinationPointRepo.save(examinationPoint);

		// 考点知识点关联
		examinationPointKnowledgePointService.save(examinationPoint.getId(), form.getKnowpoints());
	}

	@Override
	@Transactional
	public List<Long> saveStatus(Long id, Status status) {
		ExaminationPoint examinationPoint = examinationPointRepo.get(id);
		List<Long> needUpdatedQuestions = new ArrayList<Long>();
		if (examinationPoint != null) {
			examinationPoint.setStatus(status);
			examinationPointRepo.save(examinationPoint);

			if (status == Status.DISABLED) {
				// 若此考点禁用了，则此考点中的典型都移除 "典型题" 标签
				List<Long> questionIds = examinationPoint.getQuestions();
				if (CollectionUtils.isNotEmpty(questionIds)) {
					for (Long questionId : questionIds) {
						// 判断是否需要删除典型题的标签
						long count = examinationPointRepo.find("$countEPByQuestion",
								Params.param("questionId", questionId).put("eliminateId", id)).count();
						if (count == 0) {
							question2TagManage.systemDel(Lists.newArrayList(questionId),
									QuestionTag.getTagCode(QuestionCategoryType.TYPICAL));
						}
					}
					needUpdatedQuestions = questionIds;
				}
			} else if (status == Status.ENABLED) {
				// 若上考点被启用则其下所有典型题都应添加"典型题" 标签
				List<Long> questionIds = examinationPoint.getQuestions();
				// qtcService.add(QuestionCategoryType.TYPICAL, questionIds);
				question2TagManage.systemAdd(questionIds, QuestionTag.getTagCode(QuestionCategoryType.TYPICAL));

				// List<Question> questions =
				// questionRepo.mgetList(questionIds);
				// for (Question q : questions) {
				// List<QuestionCategoryType> questionCategoryTypes =
				// q.getCategoryTypes();
				// if (CollectionUtils.isEmpty(questionCategoryTypes)) {
				// questionCategoryTypes = new
				// ArrayList<QuestionCategoryType>(1);
				// questionCategoryTypes.add(QuestionCategoryType.TYPICAL);
				//
				// q.setCategoryTypes(questionCategoryTypes);
				//
				// needUpdatedQuestions.add(q.getId());
				//
				// questionRepo.save(q);
				// } else {
				// if
				// (!questionCategoryTypes.contains(QuestionCategoryType.TYPICAL))
				// {
				// questionCategoryTypes.add(QuestionCategoryType.TYPICAL);
				// q.setCategoryTypes(questionCategoryTypes);
				//
				// needUpdatedQuestions.add(q.getId());
				//
				// questionRepo.save(q);
				// }
				// }
				// }
				needUpdatedQuestions = questionIds;
			}

			// 同时更新引用该考点的习题关系
			questionExaminationPointRepo.execute(
					"update question_examination_point set status=:status where examination_point_code=:code",
					Params.param("status", status.getValue()).put("code", id));
		}

		return needUpdatedQuestions;
	}

	@Override
	@Transactional
	public List<Long> saveAll() {
		List<ExaminationPoint> examinationPoints = examinationPointRepo.find("$findAllEditing", Params.param()).list();
		List<Long> needUpdatedQuestions = new ArrayList<Long>();
		for (ExaminationPoint e : examinationPoints) {
			if (e.getQuestionCount() == null || e.getQuestionCount() == 0) {
				continue;
			}

			List<Long> questionIds = e.getQuestions();
			question2TagManage.systemAdd(questionIds, QuestionTag.getTagCode(QuestionCategoryType.TYPICAL));
			needUpdatedQuestions = questionIds;
		}
		examinationPointRepo.execute("update examination_point set status=0 where status = 2");

		return needUpdatedQuestions;
	}

	@Override
	@Transactional
	public void delete(Long id) {
		ExaminationPoint examinationPoint = examinationPointRepo.get(id);
		if (examinationPoint != null) {
			examinationPointRepo.delete(examinationPoint);
		}
	}

	@Transactional
	@Override
	public void addQuestion(Long id, Long questionId) {
		ExaminationPoint examinationPoint = examinationPointRepo.get(id);
		if (examinationPoint != null) {
			List<Long> questions = examinationPoint.getQuestions();
			if (questions == null) {
				questions = new ArrayList<Long>();
			}
			questions.add(questionId);
			examinationPoint.setQuestions(questions);
			examinationPoint.setQuestionCount(examinationPoint.getQuestionCount() + 1);
			examinationPointRepo.save(examinationPoint);

			if (examinationPoint.getStatus() == Status.ENABLED) {
				// 如果是已经生效的考点
				question2TagManage.systemAdd(Lists.newArrayList(questionId),
						QuestionTag.getTagCode(QuestionCategoryType.TYPICAL));
			}
		}
	}

	@Transactional
	@Override
	public void deleteQuestion(Long id, Long questionId) {
		ExaminationPoint examinationPoint = examinationPointRepo.get(id);
		if (examinationPoint != null) {
			List<Long> questions = examinationPoint.getQuestions();
			if (questions != null) {
				for (Long qid : questions) {
					if (qid.longValue() == questionId.longValue()) {
						questions.remove(qid);
						break;
					}
				}
			}
			examinationPoint.setQuestions(questions);
			examinationPoint.setQuestionCount(examinationPoint.getQuestionCount() - 1);
			examinationPointRepo.save(examinationPoint);

			// 判断是否需要删除典型题的标签
			long count = examinationPointRepo
					.find("$countEPByQuestion", Params.param("questionId", questionId).put("eliminateId", id)).count();
			if (count == 0) {
				question2TagManage.systemDel(Lists.newArrayList(questionId),
						QuestionTag.getTagCode(QuestionCategoryType.TYPICAL));
			}
		}
	}

	@Override
	public List<Long> queryQuestionsByExamIds(List<Long> examIds) {
		List<ExaminationPoint> list = examinationPointRepo
				.find("$queryQuestionsByExamIds", Params.param("examIds", examIds)).list();
		// 去掉重复的question_id
		Set<Long> questionIds = new HashSet<Long>();
		for (ExaminationPoint p : list) {
			questionIds.addAll(p.getQuestions());
		}
		return new ArrayList<Long>(questionIds);
	}

	@Transactional
	@Override
	public void saveQuestions(Long id, List<Long> questionIds) {
		ExaminationPoint examinationPoint = examinationPointRepo.get(id);
		if (examinationPoint != null) {
			examinationPoint.setQuestions(questionIds);
			examinationPointRepo.save(examinationPoint);
		}
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Integer> queryCounts(Integer phaseCode, Integer subjectCode) {
		List<Map> list = examinationPointRepo
				.find("$queryCounts", Params.param("phaseCode", phaseCode).put("subjectCode", subjectCode))
				.list(Map.class);
		Map<String, Integer> map = new HashMap<String, Integer>(2);
		if (list.size() > 0) {
			map = list.get(0);
		}
		return map;
	}

	@Override
	public List<ExaminationPoint> listBySmallSpecailCode(Integer phaseCode, Integer subjectCode, Long knowpointCode) {
		return examinationPointRepo.find("$listBySmallSpecailCode", Params.param("phaseCode", phaseCode)
				.put("subjectCode", subjectCode).put("knowpointCode", knowpointCode)).list();
	}

	@Override
	@Async
	public void asyncUpdateQuestionIndexByExaminationPoint(long id) {
		int page = 1;
		int pagesize = 200;
		VPage<Long> vpage = this.queryQuestionIndexByExaminationPoint(id, page, pagesize);
		if (vpage.getItems().size() > 0) {
			indexService.update(IndexType.QUESTION, vpage.getItems());
		}
		if (vpage.getTotalPage() > 1) {
			for (int i = 2; i <= vpage.getTotalPage(); i++) {
				vpage = this.queryQuestionIndexByExaminationPoint(id, i, pagesize);
				if (vpage.getItems().size() > 0) {
					indexService.update(IndexType.QUESTION, vpage.getItems());
				}
			}
		}
	}

	private VPage<Long> queryQuestionIndexByExaminationPoint(long id, int page, int size) {
		com.lanking.cloud.sdk.data.Page<Long> p = questionExaminationPointService
				.queryQuestionByExaminationPointCode(id, P.offset((page - 1) * size, size));

		VPage<Long> vPage = new VPage<Long>();
		vPage.setCurrentPage(page);
		vPage.setPageSize(size);
		vPage.setTotal(p.getTotalCount());
		vPage.setTotalPage(p.getPageCount());
		vPage.setItems(p.getItems());
		return vPage;
	}
}
