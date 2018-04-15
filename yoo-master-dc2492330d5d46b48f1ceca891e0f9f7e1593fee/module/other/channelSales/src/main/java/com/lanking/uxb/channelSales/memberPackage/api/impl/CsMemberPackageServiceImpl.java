package com.lanking.uxb.channelSales.memberPackage.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.member.MemberPackageGroup;
import com.lanking.cloud.domain.yoo.member.MemberPackageGroupChannel;
import com.lanking.cloud.domain.yoo.member.MemberPackageGroupType;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.channelSales.common.ex.ChannelSalesConsoleException;
import com.lanking.uxb.channelSales.memberPackage.api.CsMemberPackageService;
import com.lanking.uxb.channelSales.memberPackage.form.MemberPackageForm;
import com.lanking.uxb.channelSales.memberPackage.form.MemberPackageGroupForm;
import com.lanking.uxb.channelSales.memberPackage.form.MemberPackageQueryForm;
import com.lanking.uxb.channelSales.memberPackage.form.ParameterForm;

import httl.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class CsMemberPackageServiceImpl implements CsMemberPackageService {
	@Autowired
	@Qualifier("MemberPackageRepo")
	private Repo<MemberPackage, Long> memberPackageRepo;
	@Autowired
	@Qualifier("MemberPackageGroupRepo")
	private Repo<MemberPackageGroup, Long> memberPackageGroupRepo;
	@Autowired
	@Qualifier("MemberPackageGroupChannelRepo")
	private Repo<MemberPackageGroupChannel, Long> memberPackageGroupChannelRepo;
	@Autowired
	@Qualifier("ParameterRepo")
	private Repo<Parameter, Long> parameterRepo;

	@Override
	public Page<MemberPackage> query(MemberPackageQueryForm query, Pageable p) {
		Params params = Params.param("userType", query.getUserType());
		params.put("memberType", query.getMemberType());
		params.put("oderByMonth", query.isOrderByMonth());
		return memberPackageRepo.find("$csGetMemberPackages", params).fetch(p);
	}

	@Override
	public List<MemberPackage> findAll(MemberType mtype, UserType utype) {
		Params params = Params.param("", "");
		if (mtype != null) {
			params.put("mtype", mtype.getValue());
		}
		if (utype != null) {
			params.put("utype", utype.getValue());
		}
		return memberPackageRepo.find("$csFindAll", params).list();
	}

	@Override
	public List<MemberPackage> findAll(MemberType memberType, UserType userType, Long schoolId) {
		Params params = Params.param();
		if (memberType != null) {
			params.put("mtype", memberType.getValue());
		}

		if (userType != null) {
			params.put("utype", userType.getValue());
		}

		if (schoolId != null && schoolId > 0) {
			params.put("schoolId", schoolId);
		}

		return memberPackageRepo.find("$csFindPackage", params).list();
	}

	@Override
	public List<MemberPackage> findAll() {
		return findAll(null, null);
	}

	@Override
	public MemberPackage get(long id) {
		return memberPackageRepo.get(id);
	}

	@Transactional
	@Override
	public void save(MemberPackageForm form) {
		MemberPackage mp = null;
		if (form.getId() != null) {
			mp = memberPackageRepo.get(form.getId());
			mp.setDiscount(form.getDiscount());
			mp.setMemberType(form.getMemberType());
			mp.setUserType(form.getUserType());
			mp.setMonth(form.getMonth());
			mp.setOriginalPrice(form.getOriginalPrice());
			mp.setPresentPrice(form.getPresentPrice());
			mp.setSequence(form.getSequence());
			mp.setMemberPackageGroupId(form.getGroupId());
			mp.setTag(form.getTag());
			mp.setExtraMonth(form.getExtraMonth() == null ? 0 : form.getExtraMonth());
			if (form.getTag() == null) {
				mp.setCustomTag(form.getCustomTag());
			}
		} else {
			mp = new MemberPackage();
			mp.setMemberPackageGroupId(form.getGroupId());
			mp.setDiscount(form.getDiscount());
			mp.setMemberType(form.getMemberType());
			mp.setUserType(form.getUserType());
			mp.setMonth(form.getMonth());
			mp.setOriginalPrice(form.getOriginalPrice());
			mp.setPresentPrice(form.getPresentPrice());
			mp.setSequence(form.getSequence());
			mp.setTag(form.getTag());
			mp.setExtraMonth(0);
			mp.setExtraMonth(form.getExtraMonth() == null ? 0 : form.getExtraMonth());
			if (form.getTag() == null) {
				mp.setCustomTag(form.getCustomTag());
			}
		}
		memberPackageRepo.save(mp);
	}

	@Transactional
	@Override
	public int delete(Long id) {
		Params params = Params.param("id", id);
		int ret = memberPackageRepo.execute("$csDaleteMemberPackages", params);
		if (ret == 1) {
			// 重置排序
			MemberPackage memberPackage = memberPackageRepo.get(id);
			List<MemberPackage> mpList = this.findAll(memberPackage.getMemberType(), memberPackage.getUserType());
			List<MemberPackage> newMplist = new ArrayList<MemberPackage>();
			int index = 1;
			for (MemberPackage mp : mpList) {
				mp.setSequence(index++);
				newMplist.add(mp);
			}
			memberPackageRepo.save(newMplist);
		}
		return ret;
	}

	@Transactional
	@Override
	public void deleteGroup(Long id) {
		MemberPackageGroup mg = memberPackageGroupRepo.get(id);
		mg.setStatus(Status.DELETED);
		// 将该组下所有删除
		memberPackageRepo.execute("$csDaleteMemberPackagesByGroupId", Params.param("id", id));
		// 关联删除
		memberPackageGroupChannelRepo.execute("$csDaleteByGroupId", Params.param("id", id));
		memberPackageGroupRepo.save(mg);
	}

	@Transactional
	@Override
	public void sort(List<Long> ids) {
		Map<Long, MemberPackage> map = memberPackageRepo.mget(ids);
		List<MemberPackage> mplist = new ArrayList<MemberPackage>();
		int index = 1;
		for (Long id : ids) {
			MemberPackage mp = map.get(id);
			mp.setSequence(index++);
			mplist.add(mp);
		}
		memberPackageRepo.save(mplist);

	}

	@Override
	public List<MemberPackageGroup> queryGroup(MemberPackageQueryForm query) {
		Params params = Params.param("userType", query.getUserType());
		params.put("memberType", query.getMemberType());
		params.put("status", Status.ENABLED.getValue());
		return memberPackageGroupRepo.find("$csGetMemberPackageGroups", params).list();
	}

	@Transactional
	@Override
	public void createGroup(MemberPackageGroupForm form) {
		MemberPackageGroup group = new MemberPackageGroup();
		group.setMemberType(form.getMemberType());
		group.setName(form.getName());
		group.setProfits1(form.getProfits1());
		group.setProfits2(form.getProfits2());
		group.setUserType(form.getUserType());
		group.setType(form.getType());
		memberPackageGroupRepo.save(group);
		if (form.getType() == MemberPackageGroupType.CUSTOM_CHANNEL_USER) {
			if (CollectionUtils.isEmpty(form.getMap())) {
				throw new ChannelSalesConsoleException(
						ChannelSalesConsoleException.CHANNELSALES_MEMBERPACKAGEGROUPCHANNEL_ERROR);
			}
			for (String key : form.getMap().keySet()) {
				for (Long schoolId : form.getMap().get(key)) {
					MemberPackageGroupChannel mp = new MemberPackageGroupChannel();
					mp.setStatus(Status.ENABLED);
					mp.setUserChannelCode(Integer.parseInt(key));
					mp.setMemberPackageGroupId(group.getId());
					mp.setSchoolId(schoolId);
					memberPackageGroupChannelRepo.save(mp);
				}
			}

		}
	}

	@Override
	public MemberPackageGroup getGroupById(Long id) {
		return memberPackageGroupRepo.get(id);
	}

	@Transactional
	@Override
	public void updateGroup(MemberPackageGroupForm form) {
		// 删除历史数据
		memberPackageGroupChannelRepo.execute("$csDaleteByGroupId", Params.param("id", form.getId()));
		MemberPackageGroup mpg = memberPackageGroupRepo.get(form.getId());
		mpg.setType(form.getType());
		if (mpg.getType() == MemberPackageGroupType.CUSTOM_CHANNEL_USER) {
			for (String key : form.getMap().keySet()) {
				for (Long schoolId : form.getMap().get(key)) {
					MemberPackageGroupChannel mp = new MemberPackageGroupChannel();
					mp.setStatus(Status.ENABLED);
					mp.setUserChannelCode(Integer.parseInt(key));
					mp.setMemberPackageGroupId(form.getId());
					mp.setSchoolId(schoolId);
					memberPackageGroupChannelRepo.save(mp);
				}
			}
		}
	}

	@Transactional
	@Override
	public int updateParam(ParameterForm form) {
		return parameterRepo.execute("$csUpateParameters",
				Params.param("pKey", "memberPackage.settlement").put("pValue", form.getValue()));
	}

	@Override
	public List<MemberPackage> findPackage(UserType userType, MemberType memberType, Long schoolId, Integer channelCode,
			MemberPackageGroupType groupType) {
		Params params = Params.param();
		if (null != userType) {
			params.put("userType", userType.getValue());
		}
		if (memberType != null) {
			params.put("memberType", memberType.getValue());
		}
		if (schoolId != null) {
			params.put("schoolId", schoolId);
		}
		if (groupType != null) {
			params.put("groupType", groupType.getValue());
		}
		if (channelCode != null) {
			params.put("channelCode", channelCode);
			return memberPackageRepo.find("$csFindChannelPackages", params).list();
		} else {
			return memberPackageRepo.find("$csFindNotChannelPackages", params).list();
		}
	}
}
