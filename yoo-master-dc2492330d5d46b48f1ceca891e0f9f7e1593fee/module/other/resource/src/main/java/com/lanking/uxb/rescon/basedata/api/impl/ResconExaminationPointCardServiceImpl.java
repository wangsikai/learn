package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.domain.common.resource.card.ExaminationPointCard;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.basedata.api.ResconExaminationPointCardService;
import com.lanking.uxb.rescon.basedata.form.ResconExaminationPointCardForm;

/**
 * 考点卡片服务
 *
 * @see ResconExaminationPointCardService
 * @author xinyu.zhou
 * @since 2.0.1
 */
@Service
@Transactional(readOnly = true)
public class ResconExaminationPointCardServiceImpl implements ResconExaminationPointCardService {
	@Autowired
	@Qualifier("ExaminationPointCardRepo")
	private Repo<ExaminationPointCard, Long> repo;

	@Override
	@Transactional
	public ExaminationPointCard save(ResconExaminationPointCardForm form, long userId) {
		ExaminationPointCard card = null;
		if (form.getId() == null) {
			card = new ExaminationPointCard();
			card.setCreateAt(new Date());
			card.setCreateId(userId);
			card.setCheckStatus(form.getCheckStatus());
		} else {
			card = repo.get(form.getId());
			card.setUpdateAt(new Date());
			card.setUpdateId(form.getId());

			if (card.getCheckStatus() == CardStatus.DRAFT) {
				card.setCheckStatus(form.getCheckStatus());
			} else if (card.getCheckStatus() == CardStatus.NOPASS) {
				if (form.getCheckStatus() == CardStatus.EDITING) {
					card.setCheckStatus(CardStatus.EDITING);
				}
			}
		}

		card.setDelStatus(Status.ENABLED);
		card.setDescription(form.getDescription());
		card.setExaminationPointId(form.getExaminationPointId());

		return repo.save(card);
	}

	@Override
	public ExaminationPointCard findByExaminationPoint(long epId) {
		return repo.find("$findByEP", Params.param("epId", epId)).get();
	}

	@Override
	public ExaminationPointCard get(long id) {
		return repo.get(id);
	}

	@Override
	@Transactional
	public void updateCardStatus(long id, CardStatus status) {
		repo.execute("$updateCardStatus", Params.param("id", id).put("status", status.getValue()));
	}

	@Override
	public long questionCount(int subjectCode) {
		Long qCount = repo.find("$questionCount", Params.param("subjectCode", subjectCode)).get(Long.class);
		return qCount == null ? 0 : qCount;
	}

	@Override
	public Map<CardStatus, Long> statusCount(int subjectCode) {
		List<Map> resultMap = repo.find("$statusCount", Params.param("code", subjectCode)).list(Map.class);
		Map<CardStatus, Long> retMap = new HashMap<CardStatus, Long>(resultMap.size());
		for (Map m : resultMap) {
			retMap.put(CardStatus.findByValue(Integer.valueOf(m.get("s").toString())),
					Long.valueOf(m.get("c").toString()));
		}
		return retMap;
	}
}
