package com.lanking.uxb.service.mall.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.member.MemberPackageCard;
import com.lanking.cloud.domain.yoo.member.MemberPackageCardStatus;
import com.lanking.uxb.service.mall.api.MemberPackageCardService;

/**
 * 会员卡相关接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年11月15日
 */
@Service
@Transactional(readOnly = true)
public class MemberPackageCardServiceImpl implements MemberPackageCardService {
	@Autowired
	@Qualifier("MemberPackageCardRepo")
	private Repo<MemberPackageCard, String> repo;

	@Override
	public MemberPackageCard get(String code) {
		return repo.get(code);
	}

	@Override
	public void used(String code, long userId, long memberPackageOrderId) {
		MemberPackageCard card = repo.get(code);
		card.setUserId(userId);
		card.setUsedAt(new Date());
		card.setOrderId(memberPackageOrderId);
		card.setStatus(MemberPackageCardStatus.DELETE);
		repo.save(card);
	}
}
