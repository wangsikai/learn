package com.lanking.uxb.service.syncOrder.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.syncOrder.api.TaskMemberPackageService;

/**
 * 会员套餐接口实现.
 * 
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 *
 * @version 2016年9月27日
 */
@Service
@Transactional(readOnly = true)
public class TaskMemberPackageServiceImpl implements TaskMemberPackageService {

	@Autowired
	@Qualifier("MemberPackageRepo")
	private Repo<MemberPackage, Long> repo;

	@Override
	public List<MemberPackage> queryMemberPackage(UserType userType, MemberType memberType) {
		Params params = Params.param();
		if (null != userType) {
			params.put("userType", userType.getValue());
		}
		if (null != memberType) {
			params.put("memberType", memberType.getValue());
		}
		return repo.find("queryMemberPackage", params).list();
	}

	@Override
	public MemberPackage get(long id) {
		return repo.get(id);
	}
}
