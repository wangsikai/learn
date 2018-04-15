package com.lanking.uxb.service.resources.api.impl;

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
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswerImage;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerImageService;

/**
 * @author xinyu.zhou
 * @since 3.0.3
 */
@Service
@Transactional(readOnly = true)
public class StudentHomeworkAnswerImageServiceImpl implements StudentHomeworkAnswerImageService {
	@Autowired
	@Qualifier("StudentHomeworkAnswerImageRepo")
	private Repo<StudentHomeworkAnswerImage, Long> repo;

	@Override
	@SuppressWarnings("unchecked")
	public Map<Long, List<StudentHomeworkAnswerImage>> mgetByStuHKQuestion(Collection<Long> stuHkQuestionIds) {
		if (CollectionUtils.isEmpty(stuHkQuestionIds)) {
			return Collections.EMPTY_MAP;
		}

		Params params = Params.param("stuHkQuestionIds", stuHkQuestionIds);

		List<StudentHomeworkAnswerImage> imgs = repo.find("$mgetByStuHkQuestion", params).list();
		if (CollectionUtils.isEmpty(imgs)) {
			return Collections.EMPTY_MAP;
		}
		Map<Long, List<StudentHomeworkAnswerImage>> retMap = new HashMap<Long, List<StudentHomeworkAnswerImage>>(
				imgs.size());
		for (StudentHomeworkAnswerImage img : imgs) {
			List<StudentHomeworkAnswerImage> questionAnswerImgs = retMap.get(img.getStudentHomeworkQuestionId());
			if (questionAnswerImgs == null) {
				questionAnswerImgs = new ArrayList<StudentHomeworkAnswerImage>(2);
			}

			questionAnswerImgs.add(img);

			retMap.put(img.getStudentHomeworkQuestionId(), questionAnswerImgs);
		}

		return retMap;
	}

	@Override
	public List<StudentHomeworkAnswerImage> findByStuHkQuestion(long stuHkQuestionId) {

		return repo.find("$findByStuHkQuestion", Params.param("stuHkQuestionId", stuHkQuestionId)).list();

	}
}
