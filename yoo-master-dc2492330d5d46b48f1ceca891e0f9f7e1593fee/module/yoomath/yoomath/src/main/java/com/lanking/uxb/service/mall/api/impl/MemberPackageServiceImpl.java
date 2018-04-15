package com.lanking.uxb.service.mall.api.impl;

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
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.mall.api.MemberPackageService;

/**
 * 会员套餐接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月27日
 */
@Service
@Transactional(readOnly = true)
public class MemberPackageServiceImpl implements MemberPackageService {
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

	/**
	 * 如果指定套餐组为具体的学校后，则前台用户页面显示的就是这个套餐组内的套餐（取颗粒度最小的套餐组）
	 */
	@Override
	public List<MemberPackage> queryMemberPackageByGroup(UserType userType, MemberType memberType, Long schoolId,
			Integer channelCode) {
		Params params = Params.param();
		if (null != userType) {
			params.put("userType", userType.getValue());
		}
		if (null != memberType) {
			params.put("memberType", memberType.getValue());
		}
		if (null != channelCode) {
			params.put("channelCode", channelCode);
		}
		if (null != schoolId) {
			params.put("schoolId", schoolId);
		}
		/**
		 * 查询指定渠道的数据，从最小粒度的查询开始
		 */
		// 1.对应学校
		List<MemberPackage> appointSchoolData = repo.find("$queryMemberPackageGroupByAppointChannel", params).list();
		if (CollectionUtils.isNotEmpty(appointSchoolData)) {
			return appointSchoolData;
		}
		// 2.对应渠道
		params.put("schoolId", null);
		List<MemberPackage> appointChannelData = repo.find("$queryMemberPackageGroupByAppointChannel", params).list();
		if (CollectionUtils.isNotEmpty(appointChannelData)) {
			return appointChannelData;
		}
		// 3.如果该用户对应的学校、对应的渠道没有对应的套餐，则去查询可以匹配全渠道的套餐
		List<MemberPackage> allChannelData = repo.find("$queryMemberPackageGroupByAllChannel", params).list();
		return allChannelData;

	}

	@Override
	public List<MemberPackage> queryMemberPackageByAutoRegister(UserType userType, MemberType memberType) {
		Params params = Params.param();
		if (null != userType) {
			params.put("userType", userType.getValue());
		}
		if (null != memberType) {
			params.put("memberType", memberType.getValue());
		}
		return repo.find("$queryMemberPackageByAutoRegister", params).list();
	}
}
