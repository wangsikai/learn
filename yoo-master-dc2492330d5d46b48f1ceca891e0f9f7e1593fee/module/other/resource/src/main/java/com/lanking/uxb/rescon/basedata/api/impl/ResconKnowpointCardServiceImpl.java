package com.lanking.uxb.rescon.basedata.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.card.KnowpointCard;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.basedata.api.ResconKnowpointCardService;
import com.lanking.uxb.rescon.basedata.form.ResconPointCardForm;

/**
 * 知识卡片sevice
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2016年1月12日 上午10:22:25
 */
@Service
@Transactional(readOnly = true)
public class ResconKnowpointCardServiceImpl implements ResconKnowpointCardService {
	@Autowired
	@Qualifier("KnowpointCardRepo")
	private Repo<KnowpointCard, Long> repo;

	@Transactional
	@Override
	public KnowpointCard save(ResconPointCardForm pointCardForm) {
		KnowpointCard card = new KnowpointCard();
		if (pointCardForm.getId() == null) {
			card.setCreateAt(pointCardForm.getDate());
			card.setCreateId(pointCardForm.getUserId());
			card.setDescription(pointCardForm.getDescription());
			card.setExamples(pointCardForm.getExamples());
			card.setHints(pointCardForm.getHints());
			card.setKnowpointCode(pointCardForm.getKnowpointCode());
			card.setUpdateAt(pointCardForm.getDate());
			card.setUpdateId(pointCardForm.getUserId());
		} else {
			card = repo.get(pointCardForm.getId());
			if (card.getKnowpointCode() != pointCardForm.getKnowpointCode()) {
				throw new IllegalArgException();
			}
			card.setDescription(pointCardForm.getDescription());
			card.setExamples(pointCardForm.getExamples());
			card.setHints(pointCardForm.getHints());
			card.setUpdateAt(pointCardForm.getDate());
			card.setUpdateId(pointCardForm.getUserId());

		}
		return repo.save(card);
	}

	@Override
	public KnowpointCard getByCode(long knowpointCode) {
		return repo.find("$getByCode", Params.param("knowpointCode", knowpointCode)).get();
	}

}
