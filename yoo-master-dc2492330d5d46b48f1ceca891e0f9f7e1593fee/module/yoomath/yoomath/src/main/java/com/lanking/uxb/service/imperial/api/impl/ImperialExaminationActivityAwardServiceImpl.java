package com.lanking.uxb.service.imperial.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAward;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityAwardService;

/**
 * 科举活动--颁奖相关接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年4月5日
 */
@Service
@Transactional(readOnly = true)
public class ImperialExaminationActivityAwardServiceImpl implements ImperialExaminationActivityAwardService {
	@Autowired
	@Qualifier("ImperialExaminationActivityAwardRepo")
	private Repo<ImperialExaminationActivityAward, Long> repo;

	@Override
	public List<ImperialExaminationActivityAward> awardList(long code, int num) {
		Page<ImperialExaminationActivityAward> page = repo.find("$findEffectiveList", Params.param("code", code))
				.fetch(P.first(num));
		return page.getItems();
	}

	@Override
	public ImperialExaminationActivityAward get(long id) {
		return repo.get(id);
	}

	@Override
	@Transactional
	public void improve(long awardId, String contact, String mobile, String address) {
		ImperialExaminationActivityAward award = repo.get(awardId);
		if (award != null) {
			award.setAwardContact(contact);
			award.setAwardContactNumber(mobile);
			award.setAwardDeliveryAddress(address);
			repo.save(award);
		}
	}

	@Override
	public ImperialExaminationActivityAward getByUser(long code, long userId) {
		List<ImperialExaminationActivityAward> list = repo.find("$getByUser",
				Params.param("code", code).put("userId", userId)).list();
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	@Override
	public List<ImperialExaminationActivityAward> awardListByRoom(long code, int num, int room) {
		Params params = Params.param();
		params.put("code", code);
		params.put("room", room);
		Page<ImperialExaminationActivityAward> page = repo.find("$findEffectiveListByRoom", params)
				.fetch(P.first(num));
		return page.getItems();
	}
}
