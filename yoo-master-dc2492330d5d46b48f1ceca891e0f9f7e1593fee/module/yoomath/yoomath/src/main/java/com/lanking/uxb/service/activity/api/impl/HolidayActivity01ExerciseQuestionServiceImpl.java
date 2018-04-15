package com.lanking.uxb.service.activity.api.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ExerciseQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.HolidayActivity01ExerciseQuestionService;

/**
 * 假期活动01练习题目接口实现
 * 
 * @author zemin.song
 *
 */
@Service
@Transactional(readOnly = true)
public class HolidayActivity01ExerciseQuestionServiceImpl implements HolidayActivity01ExerciseQuestionService {

	@Autowired
	@Qualifier("HolidayActivity01ExerciseQuestionRepo")
	private Repo<HolidayActivity01ExerciseQuestion, Long> repo;

	@Override
	public List<HolidayActivity01ExerciseQuestion> queryQuestionList(long activityCode, long exerciseId) {
		return repo.find("$queryHolidayActivity01ExerciseQuestion",
				Params.param("exerciseIds", exerciseId).put("activityCode", activityCode)).list();
	}

	@Override
	public List<HolidayActivity01ExerciseQuestion> queryQuestionList(long activityCode, Collection<Long> exerciseIds) {
		List<HolidayActivity01ExerciseQuestion> questionlist = repo.find("$queryHolidayActivity01ExerciseQuestion",
				Params.param("exerciseIds", exerciseIds).put("activityCode", activityCode)).list();
		return questionlist;
	}

	@Override
	public Map<Long, List<HolidayActivity01ExerciseQuestion>> queryQuestionMap(long activityCode,
			Collection<Long> exerciseIds) {
		Map<Long, List<HolidayActivity01ExerciseQuestion>> retMap = new HashMap<Long, List<HolidayActivity01ExerciseQuestion>>();
		List<HolidayActivity01ExerciseQuestion> questionlist = this.queryQuestionList(activityCode, exerciseIds);
		for (Long exerciseId : exerciseIds) {
			List<HolidayActivity01ExerciseQuestion> questionList = Lists.newArrayList();
			for (HolidayActivity01ExerciseQuestion question : questionlist) {
				if (question.getHolidayActivity01ExerciseId() == exerciseId.longValue()) {
					questionList.add(question);
				}
			}
			retMap.put(exerciseId, questionList);
		}
		return retMap;
	}

}
