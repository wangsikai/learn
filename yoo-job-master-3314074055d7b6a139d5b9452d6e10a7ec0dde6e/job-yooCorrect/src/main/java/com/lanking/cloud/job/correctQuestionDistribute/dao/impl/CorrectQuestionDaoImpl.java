package com.lanking.cloud.job.correctQuestionDistribute.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.job.correctQuestionDistribute.dao.CorrectQuestionDao;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestion;
import com.lanking.microservice.domain.yoocorrect.CorrectUser;

/**
 * <p>
 * Description:
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月7日
 * @since 小优秀快批
 */
@Component
public class CorrectQuestionDaoImpl implements CorrectQuestionDao {

	@Autowired
	@Qualifier("CorrectQuestionRepo")
	Repo<CorrectQuestion, Long> correctQuestionRepo;

	@Override
	public void disTributeCorrectQuestionToUser(CorrectQuestion correctQuestion, CorrectUser correctUser,
			Date firstAllotAt, Date allotAt) {
		Params params = Params.param();
		params.put("correctQuestionId", correctQuestion.getId());
		params.put("correctUserId", correctUser.getId());
		if (null != firstAllotAt) {
			params.put("firstAllotAt", firstAllotAt);
		}
		params.put("allotAt", allotAt);
		List<Long> list = correctQuestion.getCorrectUserIds();
		if (null != list) {
			correctQuestion.getCorrectUserIds().add(correctUser.getId());
		} else {
			list = new ArrayList<Long>();
			list.add(correctUser.getId());
			correctQuestion.setCorrectUserIds(list);
		}
		params.put("correctUserIds", JSON.toJSONString(correctQuestion.getCorrectUserIds()));
		correctQuestionRepo.execute("$distributeCorrectQuestionToUser", params);
	}

	@Override
	public void sendBackCorrectQuestions(List<Long> correctQuestionIds) {
		correctQuestionRepo.execute("$sendBackCorrectQuestions",
				Params.param("correctQuestionIds", correctQuestionIds));
	}

	@Override
	public void batchSave(Collection<CorrectQuestion> entities) {
		correctQuestionRepo.save(entities);

	}

	@Override
	public List<CorrectQuestion> queryCorrectQuestionsToTeacher(int size, List<Long> exqids) {
		Params params = Params.param("size", size);
		if (CollectionUtils.isNotEmpty(exqids)) {
			params.put("exqids", exqids);
		}
		return correctQuestionRepo.find("$queryCorrectQuestionsToTeacher", params).list();
	}

	@Override
	public List<CorrectQuestion> queryCorrectQuestionsToAdmin(int size) {
		return correctQuestionRepo.find("$queryCorrectQuestionsToAdmin", Params.param("size", size)).list();
	}

	@Override
	public Page<CorrectQuestion> queryCorrectQuestions(Pageable page) {
		return correctQuestionRepo.find("$queryCorrectQuestions").fetch(page);
	}

	@Override
	public List<CorrectQuestion> queryAbnormalCorrectQuestions() {
		return correctQuestionRepo.find("$queryAbnormalCorrectQuestions").list();
	}

	@Override
	public void clearAbnormalCorrectQuestions(List<Long> questionIds) {
		correctQuestionRepo.execute("$clearAbnormalCorrectQuestions", Params.param("correctQuestionIds", questionIds));
	}
}
