package com.lanking.uxb.service.imperial.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationHomework;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperial.api.ImperialExaminationHomeworkService;

@Service
@Transactional(readOnly = true)
public class ImperialExaminationHomeworkServiceImpl implements ImperialExaminationHomeworkService {

	@Autowired
	@Qualifier("ImperialExaminationHomeworkRepo")
	private Repo<ImperialExaminationHomework, Long> repo;

	@Override
	public List<ImperialExaminationHomework> list(Long code, ImperialExaminationType type, Long userId, Integer tag,
			Integer room) {
		Params params = Params.param("code", code);
		params.put("type", type.getValue());
		params.put("userId", userId);
		if (tag != null) {
			params.put("tag", tag);
		}
		if (room != null) {
			params.put("room", room);
		}

		return repo.find("$list", params).list();
	}

	@Override
	public ImperialExaminationHomework get(long id) {
		return repo.get(id);
	}

	@Override
	public boolean isAllIssue(Long code, ImperialExaminationType type, Long userId, Integer tag, Integer room) {
		Integer count = this.countHkByTag(code, type, userId, 1, tag, room);
		return count > 0 ? false : true;
	}

	@Override
	public boolean isExistIssue(Long code, ImperialExaminationType type, Long userId) {
		Integer count = this.countHk(code, type, userId, 2);
		return count > 0 ? true : false;
	}

	@Override
	public Integer countHk(Long code, ImperialExaminationType type, Long userId, Integer flag) {
		Params params = Params.param("code", code);
		params.put("type", type.getValue());
		params.put("userId", userId);
		params.put("flag", flag);
		Integer count = repo.find("$countHk", params).get(Integer.class);
		return count;
	}

	@Override
	public long getHkId(Long code, ImperialExaminationType type, Long userId) {
		Params params = Params.param("code", code);
		params.put("type", type.getValue());
		params.put("userId", userId);
		List<ImperialExaminationHomework> list = this.list(code, type, userId, null, null);
		return list.size() > 0 ? list.get(0).getHomeworkId() : 0;
	}

	@Override
	public boolean isExistIssueByTag(Long code, ImperialExaminationType type, Long userId, Integer tag, Date endTime) {
		Params params = Params.param("code", code);
		params.put("type", type.getValue());
		params.put("userId", userId);
		if (tag != null) {
			params.put("tag", tag);
		}
		params.put("endTime", endTime);

		Integer count = repo.find("$existIssueByTag", params).get(Integer.class);
		
		return count > 0 ? true : false;
	}

	@Override
	public Integer countHkByTag(Long code, ImperialExaminationType type, Long userId, Integer flag, Integer tag,
			Integer room) {
		Params params = Params.param("code", code);
		params.put("type", type.getValue());
		params.put("userId", userId);
		params.put("flag", flag);
		if (tag != null) {
			params.put("tag", tag);
		}
		if (room != null) {
			params.put("room", room);
		}

		return repo.find("$countHkByTag", params).get(Integer.class);
	}

	@Override
	@Transactional
	public void save(ImperialExaminationHomework homework) {
		repo.save(homework);
	}
}
