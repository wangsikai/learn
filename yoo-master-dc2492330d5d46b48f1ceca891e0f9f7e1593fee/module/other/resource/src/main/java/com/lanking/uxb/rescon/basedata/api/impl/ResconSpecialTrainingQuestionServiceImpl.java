package com.lanking.uxb.rescon.basedata.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTraining;
import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingQuestion;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.basedata.api.ResconSpecialTrainingQuestionService;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;

@Service
@Transactional(readOnly = true)
public class ResconSpecialTrainingQuestionServiceImpl implements ResconSpecialTrainingQuestionService {

	@Autowired
	private ResconQuestionManage resconQuestionManage;
	@Autowired
	@Qualifier("SpecialTrainingRepo")
	private Repo<SpecialTraining, Long> specialTrainingRepo;
	@Autowired
	@Qualifier("SpecialTrainingQuestionRepo")
	private Repo<SpecialTrainingQuestion, Long> repo;

	@Override
	public Page<SpecialTrainingQuestion> queryQuestionList(Pageable p, long specialTrainingId) {
		return repo.find("$queryQuestionList", Params.param("specialTrainingId", specialTrainingId)).fetch(p);
	}

	@Transactional
	@Override
	public void saveQuestion(long specialTrainingId, List<Long> questionIds, Long createId) {
		this.deleteQuestions(specialTrainingId);
		if (CollectionUtils.isNotEmpty(questionIds)) {
			List<Long> newList = new ArrayList<Long>();
			for (Long questionId : questionIds) {
				if (!newList.contains(questionId)) {
					newList.add(questionId);
				}
			}
			int i = 1;
			for (Long id : newList) {
				SpecialTrainingQuestion s = new SpecialTrainingQuestion();
				s.setSpecialTrainingId(specialTrainingId);
				s.setCreateId(createId);
				s.setQuestionId(id);
				s.setSequence(i);
				s.setCreateAt(new Date());
				repo.save(s);
				i++;
			}
		}
	}

	@Transactional
	@Override
	public void deleteQuestions(long specialTrainingId) {
		repo.execute("$deleteQuestions", Params.param("specialTrainingId", specialTrainingId));

	}

	@Override
	public List<SpecialTrainingQuestion> questionList(long specialTrainingId) {
		return repo.find("$queryQuestionList", Params.param("specialTrainingId", specialTrainingId)).list();
	}

	@Transactional
	@Override
	public void deleteQuestion(long specialTrainingId, Long questionId) {
		repo.execute("$deleteQuestion",
				Params.param("specialTrainingId", specialTrainingId).put("questionId", questionId));
		List<SpecialTrainingQuestion> stQuestionList = this.questionList(specialTrainingId);
		List<Long> questionIds = new ArrayList<Long>();
		for (SpecialTrainingQuestion s : stQuestionList) {
			questionIds.add(s.getQuestionId());
		}
		Double temp = 0.00;
		List<Question> questionList = resconQuestionManage.mgetList(questionIds);
		for (Question q : questionList) {
			temp += q.getDifficulty() == null ? 0.00 : q.getDifficulty();
		}
		SpecialTraining st = specialTrainingRepo.get(specialTrainingId);
		if (questionList.size() == 0) {
			st.setDifficulty(0.0);
		} else {
			st.setDifficulty(BigDecimal.valueOf((temp) / questionList.size()).setScale(2, BigDecimal.ROUND_HALF_UP)
					.doubleValue());
		}
		specialTrainingRepo.save(st);

	}

	@Override
	public Map<Integer, Long> getQuestionStat(Long specialTrainingId) {
		List<Map> list = repo.find("$getQuestionStat", Params.param("specialTrainingId", specialTrainingId)).list(
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
	@Transactional
	public void changeQuestion(long trainId, long oldQuestionId, long newQuestionId) {
		repo.execute("$changeQuestion",
				Params.param("trainId", trainId).put("oldQuestionId", oldQuestionId)
						.put("newQuestionId", newQuestionId));
	}
}
