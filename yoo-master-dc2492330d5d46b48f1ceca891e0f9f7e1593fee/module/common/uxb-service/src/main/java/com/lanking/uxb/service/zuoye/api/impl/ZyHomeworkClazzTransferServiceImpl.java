package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzTransfer;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItem;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStat;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClazzTransferService;

@Transactional(readOnly = true)
@Service
public class ZyHomeworkClazzTransferServiceImpl implements ZyHomeworkClazzTransferService {

	@Autowired
	@Qualifier("HomeworkClazzTransferRepo")
	Repo<HomeworkClazzTransfer, Long> repo;

	@Autowired
	@Qualifier("HomeworkClazzRepo")
	Repo<HomeworkClazz, Long> clazzRepo;

	@Autowired
	@Qualifier("HomeworkRepo")
	Repo<Homework, Long> homeworkRepo;

	@Autowired
	@Qualifier("HolidayHomeworkRepo")
	Repo<HolidayHomework, Long> holidayHomeworkRepo;

	@Autowired
	@Qualifier("HolidayHomeworkItemRepo")
	Repo<HolidayHomeworkItem, Long> holidayHomeworkItemRepo;

	@Autowired
	@Qualifier("AccountRepo")
	Repo<Account, Long> accountRepo;

	@Autowired
	@Qualifier("HomeworkStatRepo")
	Repo<HomeworkStat, Long> homeworkStatRepo;

	@Autowired
	private ZyHomeworkClassService classService;

	@Transactional
	@Override
	public void classTransfer(long classId, long from0, long to) {
		// 处理班级转让记录表HomeworkClazzTransfer
		HomeworkClazzTransfer transfer = new HomeworkClazzTransfer();
		transfer.setHomeworkClassId(classId);
		transfer.setFrom(from0);
		transfer.setTo(to);
		HomeworkClazz clazz = clazzRepo.get(classId);
		if (clazz.getOriginalTeacherId() == clazz.getTeacherId()) {
			transfer.setStartAt(clazz.getCreateAt());
		} else {
			transfer.setStartAt(clazz.getStartAt());
		}
		transfer.setEndAt(new Date());
		transfer.setTransferAt(new Date());
		repo.save(transfer);
		// 判断转入的老师原来是否已经存在同名的班级
		boolean flag = classService.nameExist(clazz.getName(), to);
		if (flag) {
			clazz.setName(clazz.getName() + "-1");
		}
		// 处理班级表HomeworkClazz
		clazz.setStartAt(new Date());
		clazz.setTeacherId(to);
		clazzRepo.save(clazz);

		// 处理 普通作业homework
		// 转让后需要将非下发状态的作业createId更新为当前的老师ID
		homeworkRepo.execute("$homeworkTransfer", Params.param("classId", classId).put("newTeacherId", to));

		// 处理 假期作业 HolidayHomework、HolidayHomeworkItem
		holidayHomeworkRepo.execute("$holidayHkTransfer", Params.param("classId", classId).put("newTeacherId", to));

		holidayHomeworkItemRepo.execute("$holidayItemTransfer", Params.param("classId", classId)
				.put("newTeacherId", to));
		// 处理homeworkStat
		homeworkStatRepo.execute("$homeworkStatTransfer", Params.param("classId", classId).put("newTeacherId", to));
	}

	@Override
	public List<Long> findUserList(String userNameStr, String mobile, long schoolId, Long createId) {
		Params params = Params.param("schoolId", schoolId);
		if (userNameStr != null) {
			params.put("userNameStr", "%" + userNameStr + "%");
		}
		if (mobile != null) {
			params.put("mobile", mobile);
		}
		if (createId != null) {
			params.put("createId", createId);
		}
		return accountRepo.find("$findUserList", params).list(Long.class);
	}

	@Override
	public HomeworkClazzTransfer getLastest(long userId) {
		Long id = repo.find("$getLastest", Params.param("userId", userId)).get(Long.class);
		return id == null ? null : repo.get(id);
	}

	@Transactional
	@Override
	public void read(long id, long userId) {
		repo.execute("$readTransferLog", Params.param("id", id).put("userId", userId).put("nowTime", new Date()));
	}

}
