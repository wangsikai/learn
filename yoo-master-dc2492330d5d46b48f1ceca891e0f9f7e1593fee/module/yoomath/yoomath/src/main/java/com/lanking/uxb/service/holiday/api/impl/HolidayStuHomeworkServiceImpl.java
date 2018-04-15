package com.lanking.uxb.service.holiday.api.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItem;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkService;
import com.lanking.uxb.service.holiday.form.HolidayStuHomeworkPublishForm;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuery;

@Service
@Transactional(readOnly = true)
public class HolidayStuHomeworkServiceImpl implements HolidayStuHomeworkService {
	@Autowired
	@Qualifier("HolidayStuHomeworkRepo")
	private Repo<HolidayStuHomework, Long> holidayStuHomeworkRepo;

	@Autowired
	private HolidayStuHomeworkItemService hdStuHomeworkItemService;

	@Override
	public HolidayStuHomework get(long id) {
		return holidayStuHomeworkRepo.get(id);
	}

	@Override
	@Transactional
	public HolidayStuHomework create(HolidayStuHomeworkPublishForm form) {
		HolidayStuHomework stuHomework = new HolidayStuHomework();
		stuHomework.setCreateAt(new Date());
		stuHomework.setDelStatus(Status.ENABLED);
		stuHomework.setHolidayHomeworkId(form.getHolidayHomeworkId());
		stuHomework.setStatus(StudentHomeworkStatus.NOT_SUBMIT);
		stuHomework.setStudentId(form.getStudentId());
		stuHomework.setType(form.getType());
		stuHomework.setAllItemCount(form.getItemCount());
		stuHomework.setCommitItemCount(0);

		holidayStuHomeworkRepo.save(stuHomework);
		return stuHomework;
	}

	@Override
	public List<HolidayStuHomework> queryStuHomework(long holidayHomeworkId, List<Long> stuIds) {
		return holidayStuHomeworkRepo.find("$queryStuHomework",
				Params.param("holidayHomeworkId", holidayHomeworkId).put("stuIds", stuIds)).list();
	}

	@Override
	public List<HolidayStuHomework> mget(Collection<Long> ids) {
		return holidayStuHomeworkRepo.mgetList(ids);
	}

	@Transactional
	@Override
	public void uptStuHomeworkCompleteRate(long holidayStuHomeworkId) {
		List<HolidayStuHomeworkItem> stuHkItems = hdStuHomeworkItemService.queryStuHkItems(null, null,
				holidayStuHomeworkId, null);
		Double completeRate = hdStuHomeworkItemService.getSumComplete(holidayStuHomeworkId);
		holidayStuHomeworkRepo.execute(
				"$uptStuHomeworkCompleteRate",
				Params.param("completeRate",
						new BigDecimal(completeRate / stuHkItems.size()).setScale(0, BigDecimal.ROUND_HALF_UP)).put(
						"id", holidayStuHomeworkId));

	}

	@Override
	public Map<Long, HolidayStuHomework> mgetMap(Collection<Long> ids) {
		return holidayStuHomeworkRepo.mget(ids);
	}

	@Override
	public long countNotSubmit(long studentId) {
		return holidayStuHomeworkRepo.find("$countNotSubmit", Params.param("studentId", studentId)).count();
	}

	@Override
	@Transactional
	public void updateViewStatus(long id) {
		holidayStuHomeworkRepo.execute("$updateViewStatus", Params.param("id", id));
	}

	/**
	 * 新作业查询.
	 */
	@Override
	public Page<HolidayStuHomework> queryHolidayHomeworkWeb(ZyStudentHomeworkQuery query, Pageable pageable) {
		Params params = Params.param("studentId", query.getStudentId());
		if (query.getClassId() != null) {
			params.put("homeworkClassId", query.getClassId());
		}
		if (StringUtils.isNotBlank(query.getKey())) {
			params.put("keys", "%" + query.getKey().replace(" ", "") + "%");
		}
		if (query.getBeginTime() != null && query.getEndTime() != null) {
			params.put("bt", query.getBeginTime());
			params.put("et", query.getEndTime());
		}
		if (query.getStatusIndex() != null) {
			params.put("statusIndex", query.getStatusIndex());
		}
		return holidayStuHomeworkRepo.find("$queryHolidayHomeworkWeb", params).fetch(pageable);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Date getFirstStartAt(Long studentId) {
		Params params = Params.param("studentId", studentId);
		// List<HomeworkClazz> clazzs =
		// zyHkClassService.listCurrentClazzs(teacherId);
		// Set<Long> clazzIds = Sets.newHashSet();
		// for (HomeworkClazz clazz : clazzs) {
		// clazzIds.add(clazz.getId());
		// }
		// if (clazzIds.size() > 0) {
		// params.put("homeworkClassIds", clazzIds);
		// }
		List<Map> list = holidayStuHomeworkRepo.find("$getFirstStartAt", params).list(Map.class);
		return list.size() == 0 ? null : (Date) list.get(0).get("ct");
	}
}
