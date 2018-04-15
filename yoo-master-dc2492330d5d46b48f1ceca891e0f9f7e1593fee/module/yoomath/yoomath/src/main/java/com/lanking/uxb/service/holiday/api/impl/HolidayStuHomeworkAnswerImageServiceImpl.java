package com.lanking.uxb.service.holiday.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkAnswerImage;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkAnswerImageService;

/**
 * @see com.lanking.uxb.service.holiday.api.HolidayStuHomeworkAnswerImageService
 * @author xinyu.zhou
 * @since 3.9.0
 */
@Service
@Transactional(readOnly = true)
public class HolidayStuHomeworkAnswerImageServiceImpl implements HolidayStuHomeworkAnswerImageService {
	@Autowired
	@Qualifier("HolidayStuHomeworkAnswerImageRepo")
	private Repo<HolidayStuHomeworkAnswerImage, Long> repo;

	@Override
	public List<HolidayStuHomeworkAnswerImage> findByItemQuestion(Long stuItemQuestionId) {
		return repo.find("$findByItemQuestion", Params.param("id", stuItemQuestionId)).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Long, List<HolidayStuHomeworkAnswerImage>> findByItemQuestions(Collection<Long> stuItemQuestionIds) {
		if (CollectionUtils.isEmpty(stuItemQuestionIds)) {
			return Collections.EMPTY_MAP;
		}
		List<HolidayStuHomeworkAnswerImage> images = repo.find("$findByItemQuestions",
				Params.param("ids", stuItemQuestionIds)).list();
		Map<Long, List<HolidayStuHomeworkAnswerImage>> retMap = new HashMap<Long, List<HolidayStuHomeworkAnswerImage>>(
				stuItemQuestionIds.size());

		for (HolidayStuHomeworkAnswerImage i : images) {
			List<HolidayStuHomeworkAnswerImage> list = retMap.get(i.getHolidayStuItemQuestionId());
			if (CollectionUtils.isNotEmpty(list)) {
				list = new ArrayList<HolidayStuHomeworkAnswerImage>(2);
			}

			list.add(i);

			retMap.put(i.getHolidayStuItemQuestionId(), list);
		}
		return retMap;
	}
}
