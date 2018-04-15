package com.lanking.cloud.job.nationalDayActivity.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01HomeworkDAO;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01TeaDAO;
import com.lanking.cloud.job.nationalDayActivity.service.NationalDayActivity01TeaService;
import com.lanking.cloud.job.nationalDayActivity.service.TaskNationalDayActivity01TeaScoreService;

import httl.util.CollectionUtils;

@Service
public class TaskNationalDayActivity01TeaScoreServiceImpl implements TaskNationalDayActivity01TeaScoreService {

	@Autowired
	@Qualifier("nda01NationalDayActivity01HomeworkDAO")
	private NationalDayActivity01HomeworkDAO nationalDayActivity01HomeworkDAO;
	@Autowired
	@Qualifier("nda01NationalDayActivity01TeaDAO")
	private NationalDayActivity01TeaDAO nationalDayActivity01TeaDAO;
	@Autowired
	private NationalDayActivity01TeaService nationalDayActivity01TeaService;

	@Transactional(readOnly = false)
	@Override
	public void deleteTeaData() {
		// 直接查询全部需要删除的数据
		List<Long> userIds = nationalDayActivity01TeaDAO.getDeteleUserIds();
		// 删除tea表中数据
		if (CollectionUtils.isNotEmpty(userIds)) {
			nationalDayActivity01TeaDAO.deletes(userIds);
		}
	}

	@Override
	public void statTeaScore() {
		NationalDayActivity01 activity = nationalDayActivity01TeaService.getActivity();

		// 先取教师id
		int start = 0;
		int size = 20;

		List<Long> teacherIds = nationalDayActivity01TeaService.getTeacherIdsSpecify(start, size);
		while (CollectionUtils.isNotEmpty(teacherIds)) {
			nationalDayActivity01TeaService.saveTea(teacherIds, activity);
			// 查询下一部分数据
			start = start + size;
			teacherIds = nationalDayActivity01TeaService.getTeacherIdsSpecify(start, size);
		}
	}
}
