package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStat;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatService;

@Transactional(readOnly = true)
@Service
public class ZyHomeworkStatServiceImpl implements ZyHomeworkStatService {

	@Autowired
	@Qualifier("HomeworkStatRepo")
	Repo<HomeworkStat, Long> homeworkStatRepo;

	@Override
	public HomeworkStat findOne(long teacherId, Long homeworkClassId) {
		Params params = Params.param("teacherId", teacherId);
		if (homeworkClassId != null) {
			params.put("homeworkClassId", homeworkClassId);
		}
		return homeworkStatRepo.find("$zyFindOne", params).get();
	}

	@Transactional
	@Override
	public void updateAfterPublishHomework(long teacherId, Long homeworkClassId, HomeworkStatus from,
			HomeworkStatus to) {
		HomeworkStat homeworkStat = findOne(teacherId, homeworkClassId);
		if (homeworkStat == null) {
			homeworkStat = new HomeworkStat();
			homeworkStat.setHomeworkClassId(homeworkClassId);
			homeworkStat.setCreateAt(new Date());
			if (to == HomeworkStatus.PUBLISH) {
				homeworkStat.setDoingNum(1L);
			}
			if (from == null) {
				homeworkStat.setHomeWorkNum(1L);
			}
			homeworkStat.setHomeworkTime(0);
			homeworkStat.setRightRate(null);
			homeworkStat.setUserId(teacherId);
		} else {
			if (to == HomeworkStatus.PUBLISH) {
				homeworkStat.setDoingNum(homeworkStat.getDoingNum() + 1);
			}
			if (from == null) {
				homeworkStat.setHomeWorkNum(homeworkStat.getHomeWorkNum() + 1);
			}
		}
		homeworkStat.setUpdateAt(new Date());
		homeworkStatRepo.save(homeworkStat);
	}

	@Transactional
	@Override
	public void updateAfterIssueHomework(long teacherId, Long homeworkClassId) {
		HomeworkStat homeworkStat = findOne(teacherId, homeworkClassId);
		if (homeworkStat == null) {
			homeworkStat = new HomeworkStat();
			homeworkStat.setHomeworkClassId(homeworkClassId);
			homeworkStat.setCreateAt(new Date());
			homeworkStat.setDoingNum(0);
			homeworkStat.setHomeWorkNum(1L);
			homeworkStat.setHomeworkTime(0);
			homeworkStat.setRightRate(null);
			homeworkStat.setUserId(teacherId);
		} else {
			homeworkStat.setDoingNum(homeworkStat.getDoingNum() - 1);
		}
		homeworkStat.setUpdateAt(new Date());
		homeworkStatRepo.save(homeworkStat);

	}

	@Override
	public HomeworkStat getByHomeworkClassId(long homeworkClassId) {
		return homeworkStatRepo.find("$zyGetByHomeworkClassId", Params.param("homeworkClassId", homeworkClassId)).get();
	}

	@Override
	public List<HomeworkStat> getByHomeworkClassIds(Collection<Long> homeworkClassIds) {
		List<HomeworkStat> list = homeworkStatRepo
				.find("$zyGetByHomeworkClassIds", Params.param("homeworkClassIds", homeworkClassIds)).list();
		return list;
	}
}
