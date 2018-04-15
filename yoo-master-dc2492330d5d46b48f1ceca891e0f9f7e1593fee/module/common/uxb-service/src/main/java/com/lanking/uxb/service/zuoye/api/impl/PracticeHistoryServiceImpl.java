package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.common.PracticeHistory;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.PracticeHistoryService;
import com.lanking.uxb.service.zuoye.form.PracticeHistoryForm;

@Transactional(readOnly = true)
@Service
public class PracticeHistoryServiceImpl implements PracticeHistoryService {

	@Autowired
	@Qualifier("PracticeHistoryRepo")
	Repo<PracticeHistory, Long> practiceHistoryRepo;

	@Override
	public void updateHistory(PracticeHistoryForm form) {
		PracticeHistory p = this.getByBizId(form.getBiz(), form.getBizId());
		if (p == null) {
			p = new PracticeHistory();
			p.setBiz(form.getBiz());
			p.setBizId(form.getBizId());
			p.setCreateAt(form.getCreateAt());
			p.setName(form.getName());
		} else {
			if (form.getCreateAt() != null) {
				p.setCreateAt(form.getCreateAt());
			}
			p.setUpdateAt(form.getUpdateAt());
		}
		p.setCompletionRate(form.getCompletionRate());
		p.setRightRate(form.getRightRate());
		p.setUserId(form.getUserId());
		practiceHistoryRepo.save(p);
	}

	@Override
	public PracticeHistory getByBizId(Biz biz, long bizId) {
		return practiceHistoryRepo.find("$getByBizId", Params.param("biz", biz.getValue()).put("bizId", bizId)).get();
	}

	@Override
	public CursorPage<Long, PracticeHistory> queryHistory(Integer type, CursorPageable<Long> cpr, Date cursorDate,
			Long userId) {
		Params params = Params.param();
		params.put("cursorDate", cursorDate);
		params.put("userId", userId);
		if (type != null) {
			if (type == 1) {
				params.put("biz", Biz.SECTION_EXERCISE.getValue());
			}
			if (type == 2) {
				params.put("biz", Biz.DAILY_PRACTICE.getValue());
			}
			if (type == 3) {
				params.put("biz", Biz.SMART_PAPER.getValue());
			}
		}
		return practiceHistoryRepo.find("$queryHistory", params).fetch(cpr);
	}

	@Override
	public long count(long userId) {
		return practiceHistoryRepo.find("$count", Params.param("userId", userId)).count();
	}
}
