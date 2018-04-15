package com.lanking.uxb.rescon.basedata.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTraining;
import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingOperateType;
import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingQuestion;
import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingStatus;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.basedata.api.ResconExaminationPointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeSystemService;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionExaminationPointService;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionKnowledgeService;
import com.lanking.uxb.rescon.basedata.api.ResconSpecialTrainingOperateLogService;
import com.lanking.uxb.rescon.basedata.api.ResconSpecialTrainingQuestionService;
import com.lanking.uxb.rescon.basedata.api.ResconSpecialTrainingService;
import com.lanking.uxb.rescon.basedata.form.ResconSpecialTrainingForm;
import com.lanking.uxb.rescon.basedata.value.VSpecialTraining;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;

/**
 * 针对性训练
 * 
 * @since 2.0.1
 * @author wangsenhao
 *
 */
@Service
@Transactional(readOnly = true)
public class ResconSpecialTrainingServiceImpl implements ResconSpecialTrainingService {

	@Autowired
	@Qualifier("SpecialTrainingRepo")
	private Repo<SpecialTraining, Long> specialTrainingRepo;

	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private ResconSpecialTrainingOperateLogService logService;
	@Autowired
	private ResconExaminationPointService resconExaminationPointService;
	@Autowired
	private ResconSpecialTrainingQuestionService resconSpecialTrainingQuestionService;
	@Autowired
	private ResconQuestionManage resconQuestionManage;
	@Autowired
	private ResconQuestionExaminationPointService resconQuestionExaminationPointService;
	@Autowired
	private ResconQuestionKnowledgeService resconQuestionKnowledgeService;
	@Autowired
	private ResconKnowledgeSystemService resconKnowledgeSystemService;

	@Override
	public Page<SpecialTraining> list(Pageable p, Long code) {
		Params params = Params.param();
		params.put("code", code);
		return specialTrainingRepo.find("$list", params).fetch(p);
	}

	@Override
	public List<SpecialTraining> list(long code) {
		return specialTrainingRepo.find("$findByCode", Params.param("code", code)).list();
	}

	@Transactional
	@Override
	public VSpecialTraining createSpecial(ResconSpecialTrainingForm form) {
		SpecialTraining s = null;
		List<Long> questionIds = new ArrayList<Long>();
		if (form.getId() != null) {
			s = this.get(form.getId());
			s.setUpdateId(form.getUserId());
			s.setUpdateAt(new Date());
			if (form.getQuestionIds() != null || form.isEditPullQuestion()) {
				List<SpecialTrainingQuestion> stqList = resconSpecialTrainingQuestionService.questionList(form.getId());
				for (SpecialTrainingQuestion sq : stqList) {
					questionIds.add(sq.getQuestionId());
				}
			}
			// 基础题库增加和录入新题是questionIds不为空
			if (form.getQuestionIds() != null) {
				questionIds.addAll(form.getQuestionIds());
			}
			// 编辑又一次拉题
			if (form.isEditPullQuestion()) {
				// 重新拉取的题目是添加，不覆盖
				// 100题剩下还能拉多少题
				int num = 100 - questionIds.size();
				List<Long> tempList = this.pullQuestion(form.getExamIds(), form.getKnowPoints(), form.getVendorId());
				if (num < 20) {
					questionIds.addAll(tempList.size() > num ? tempList.subList(0, num) : tempList);
				} else {
					questionIds.addAll(tempList);
				}
			}
		} else {
			s = new SpecialTraining();
			s.setCreateId(form.getUserId());
			s.setCreateAt(new Date());
			questionIds = this.pullQuestion(form.getExamIds(), form.getKnowPoints(), form.getVendorId());
		}
		if (form.getKnowpointCode() != null) {
			s.setKnowpointCode(form.getKnowpointCode());
		}
		if (CollectionUtils.isNotEmpty(form.getExamIds())) {
			s.setExaminationPoints(form.getExamIds());
		}
		if (CollectionUtils.isNotEmpty(form.getKnowPoints())) {
			s.setKnowpoints(form.getKnowPoints());
		}
		if (form.getName() != null) {
			s.setName(form.getName());
		}
		s.setStatus(form.getStatus());
		if (form.getStatus() != SpecialTrainingStatus.NOCHECK) {
			Double temp = 0.00;
			if (CollectionUtils.isNotEmpty(questionIds)) {
				List<Question> questionList = resconQuestionManage.mgetList(questionIds);
				for (Question q : questionList) {
					temp += q.getDifficulty() == null ? 0.00 : q.getDifficulty();
				}
				s.setDifficulty(BigDecimal.valueOf((temp) / questionIds.size()).setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue());
			} else {
				s.setDifficulty(temp);
			}
		}
		specialTrainingRepo.save(s);
		if (CollectionUtils.isNotEmpty(questionIds)) {
			// 保存题目
			resconSpecialTrainingQuestionService.saveQuestion(s.getId(), questionIds, form.getUserId());
		}
		// 保存操作记录
		logService.saveLog(s.getId(), form.getOperateType(), form.getUserId(), null);
		VSpecialTraining v = new VSpecialTraining();
		v.setCreateUser(vendorUserManage.getVendorUser(s.getCreateId()).getName());
		v.setCreateAt(s.getCreateAt());
		v.setDifficulty(s.getDifficulty());
		v.setId(s.getId());
		return v;
	}

	@Override
	public SpecialTraining get(long id) {
		return specialTrainingRepo.get(id);
	}

	@Override
	public List<Long> pullQuestion(List<Long> examIds, List<Long> knowPoints, Long vendorId) {
		// 查询考点对应的所有典型题
		List<Long> typicalQuestions = resconExaminationPointService.queryQuestionsByExamIds(examIds);
		// 如果正好20题直接返回
		if (typicalQuestions.size() == 20) {
			return typicalQuestions;
		} else if (typicalQuestions.size() > 20) {
			List<Long> ids = new ArrayList<Long>();
			// 如果大于20题随机取其中20题,
			for (int i = 0; i < 20; i++) {
				Random rand = new Random();
				int randNum = rand.nextInt(typicalQuestions.size());
				ids.add(typicalQuestions.get(randNum));
				typicalQuestions.remove(randNum);
			}
			return ids;
		} else {
			// 如果小于20题,则继续补全
			/**
			 * 考点相关+子知识点相关,考点相关,子知识点相关
			 */
			// 考点相关题目集合
			List<Long> examQuestionList = resconQuestionExaminationPointService.findQuestionIds(examIds, vendorId);
			// 子知识点相关题目集合
			List<Long> knowQuestionList = resconQuestionKnowledgeService.findQuestionIds(knowPoints, vendorId);
			// 考点和子知识点都相关的题目id集合
			List<Long> newList = new ArrayList<Long>();
			for (Long questionId : examQuestionList) {
				if (knowQuestionList.contains(questionId)) {
					newList.add(questionId);
				}
			}
			// 还差多少题目
			int leftLength = 20 - typicalQuestions.size();
			if (newList.size() < leftLength) {
				typicalQuestions.addAll(newList);
				int tempLength = leftLength - newList.size();
				// 去掉之前两个交集的部分,已经算过的部分
				examQuestionList.removeAll(newList);
				knowQuestionList.removeAll(newList);
				if (CollectionUtils.isNotEmpty(examQuestionList)) {
					if (examQuestionList.size() >= tempLength) {
						typicalQuestions.addAll(examQuestionList.subList(0, tempLength));
					} else {
						int tempLength2 = tempLength - examQuestionList.size();
						if (CollectionUtils.isNotEmpty(knowQuestionList)) {
							typicalQuestions.addAll(knowQuestionList.subList(0,
									knowQuestionList.size() >= tempLength2 ? tempLength2 : knowQuestionList.size()));
						}
					}
				}
			} else if (newList.size() == leftLength) {
				typicalQuestions.addAll(newList);
			} else {
				typicalQuestions.addAll(newList.subList(0, leftLength));
			}

		}
		return typicalQuestions;
	}

	@Transactional
	@Override
	public void updateStatus(SpecialTrainingStatus status, long id, Long userId) {
		SpecialTraining s = this.get(id);
		s.setStatus(status);
		specialTrainingRepo.save(s);
		// 同时要记录操作记录
		logService.saveLog(s.getId(), status == SpecialTrainingStatus.PASS ? SpecialTrainingOperateType.PUBLISH
				: SpecialTrainingOperateType.EDIT, userId, null);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Integer, Long> getStat(Long code) {
		KnowledgeSystem ks = resconKnowledgeSystemService.get(code);
		Params params = Params.param();
		if (ks.getLevel() == 1) {
			params.put("flag", 1);
		} else {
			params.put("flag", 2);
		}
		params.put("code", code + "%");
		List<Map> list = specialTrainingRepo.find("$getStat", params).list(Map.class);
		Map<Integer, Long> map = new HashMap<Integer, Long>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map pa : list) {
				map.put(Integer.parseInt(pa.get("status").toString()), Long.parseLong(pa.get("count").toString()));
			}
		}
		return map;
	}

	@Override
	public Long total(Long code) {
		return specialTrainingRepo.find("$total", Params.param("code", code + "%")).get(Long.class);
	}

	@Override
	public Map<Integer, Long> getStatBySubject(Long subjectCode) {
		List<Map> list = specialTrainingRepo.find("$getStatBySubject", Params.param("subjectCode", subjectCode)).list(
				Map.class);
		Map<Integer, Long> map = new HashMap<Integer, Long>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map pa : list) {
				map.put(Integer.parseInt(pa.get("status").toString()), Long.parseLong(pa.get("count").toString()));
			}
		}
		return map;
	}

	@Override
	public Long getTotalBySubject(Long subjectCode) {
		return specialTrainingRepo.find("$getTotalBySubject", Params.param("subjectCode", subjectCode)).get(Long.class);
	}

}
