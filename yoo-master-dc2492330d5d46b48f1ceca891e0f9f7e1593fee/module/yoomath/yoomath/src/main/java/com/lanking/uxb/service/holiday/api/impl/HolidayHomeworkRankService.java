package com.lanking.uxb.service.holiday.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItem;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItem;
import com.lanking.cloud.sdk.data.Params;

/**
 * 统计假期作业学生排名数据
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
@Service
@Transactional
public class HolidayHomeworkRankService {
	@Autowired
	@Qualifier("HolidayStuHomeworkItemRepo")
	private Repo<HolidayStuHomeworkItem, Long> holidayStuHomeworkItemRepo;
	@Autowired
	@Qualifier("HolidayStuHomeworkRepo")
	private Repo<HolidayStuHomework, Long> holidayStuHomeworkRepo;
	@Autowired
	@Qualifier("HolidayHomeworkItemRepo")
	private Repo<HolidayHomeworkItem, Long> holidayHomeworkItemRepo;

	@Transactional
	public void calculateStuHomeworkItemRank(long hkId) {
		List<HolidayStuHomework> stuHks = holidayStuHomeworkRepo.find("$findStuHkByHk", Params.param("hkId", hkId))
				.list();

		// 保存学生假期作业排名情况
		int rank = 1;
		for (HolidayStuHomework stuHk : stuHks) {
			stuHk.setRank(rank);
			rank++;

			holidayStuHomeworkRepo.save(stuHk);
		}

		// 保存学生专项排名
		List<HolidayHomeworkItem> hkItems = holidayHomeworkItemRepo.find("$findByHk", Params.param("hkId", hkId))
				.list();

		for (HolidayHomeworkItem item : hkItems) {
			List<HolidayStuHomeworkItem> stuHkItems = holidayStuHomeworkItemRepo.find("$findByHkItemRightRate",
					Params.param("hkItemId", item.getId())).list();

			int rankTwo = 1;
			for (HolidayStuHomeworkItem stuItem : stuHkItems) {
				stuItem.setRank(rankTwo);
				holidayStuHomeworkItemRepo.save(stuItem);
				rankTwo++;
			}
		}
	}

}
