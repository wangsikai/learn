package com.lanking.uxb.service.holiday.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItem;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkItemService;
import com.lanking.uxb.service.holiday.form.HolidayHomeworkItemPublishForm;

/**
 * @see HolidayHomeworkItemService
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
@Service
@Transactional(readOnly = true)
public class HolidayHomeworkItemServiceImpl implements HolidayHomeworkItemService {

	@Autowired
	@Qualifier("HolidayHomeworkItemRepo")
	private Repo<HolidayHomeworkItem, Long> hdItemRepo;

	@Override
	public List<HolidayHomeworkItem> listHdItemById(long holidayHomeworkId) {
		return hdItemRepo.find("$listHdItemById", Params.param("holidayHomeworkId", holidayHomeworkId)).list();
	}

	@Override
	public HolidayHomeworkItem get(long id) {
		return hdItemRepo.get(id);
	}

	@Override
	public Map<Long, HolidayHomeworkItem> mget(Collection<Long> ids) {
		return hdItemRepo.mget(ids);
	}

	@Override
	@Transactional
	public HolidayHomeworkItem create(HolidayHomeworkItemPublishForm form) {
		HolidayHomeworkItem item = new HolidayHomeworkItem();
		item.setCreateAt(new Date());
		item.setCreateId(form.getCreateId());
		item.setOriginalCreateId(form.getCreateId());
		item.setDeadline(form.getDeadline());
		item.setDelStatus(Status.ENABLED);
		item.setDifficulty(form.getDifficulty());
		item.setHolidayHomeworkId(form.getHkId());
		item.setHomeworkClassId(form.getClassId());
		item.setHomeworkTime(0);
		item.setCommitCount(0L);
		item.setMetaKnowpoints(form.getMetaKnows());
		item.setKnowledgePoints(form.getKnowedgePoints());
		item.setName(form.getName());
		item.setQuestionCount(form.getQuestionIds().size());
		item.setStartTime(form.getStartTime());
		item.setStatus(form.getStatus());
		item.setType(form.getType());
		item.setDeadline(form.getDeadline());

		hdItemRepo.save(item);

		return item;
	}

	@Override
	@Transactional
	public void updateDistributeCount(long id, int size) {
		hdItemRepo.execute("$updateDistributeCount", Params.param("id", id).put("size", size));
	}

	@Override
	@Transactional
	public void updateHomeworkItemStatus(Collection<Long> ids, HomeworkStatus status) {
		hdItemRepo.execute("$updateStatus", Params.param("ids", ids).put("status", status.getValue()));
	}

}
