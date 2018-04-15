package com.lanking.uxb.rescon.account.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.resources.vendor.Vendor;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.support.resources.vendor.VendorUserStatus;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.UnSupportedOperationException;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;

@Service
@Transactional(readOnly = true)
public class ResconVendorUserManageImpl implements ResconVendorUserManage {

	@Autowired
	@Qualifier("VendorRepo")
	private Repo<Vendor, Long> vendorRepo;
	@Autowired
	@Qualifier("VendorUserRepo")
	private Repo<VendorUser, Long> vendorUserRepo;

	@Override
	public VendorUser getVendorUser(long id) {
		return vendorUserRepo.get(id);
	}

	@Override
	public Map<Long, VendorUser> mgetVendorUser(Collection<Long> ids) {
		return vendorUserRepo.mget(ids);
	}

	@Transactional
	@Override
	public VendorUser create(VendorUser user) {
		VendorUser p = null;
		if (user.getId() != null) {
			p = vendorUserRepo.get(user.getId());

		} else {
			p = new VendorUser();
			p.setCreateAt(new Date());
			p.setName(nextVendorUserCode(user.getVendorId()));
		}
		p.setPassword1(user.getPassword1());
		p.setPassword2(user.getPassword2());
		p.setStatus(user.getStatus());
		p.setType(user.getType());
		p.setUpdateAt(p.getCreateAt());
		p.setVendorId(user.getVendorId());
		p.setRealName(user.getRealName());
		p.setPermissions(user.getPermissions());
		return vendorUserRepo.save(p);
	}

	private String nextVendorUserCode(long vendorId) {
		Vendor vendor = vendorRepo.get(vendorId);
		long count = vendorUserRepo.find("$getVendorUserCountR", Params.param("vendorId", vendorId)).count() + 1;
		return vendor.getCode() + "U" + count;
	}

	@Override
	public long getVendorUserCount(long vendorId) {
		return vendorUserRepo.find("$getVendorUserCountR", Params.param("vendorId", vendorId)).count();
	}

	@Transactional(readOnly = true)
	@Override
	public VendorUser findOne(GetType getType, String value) {
		if (getType == GetType.PASSWORD || getType == GetType.EMAIL || getType == GetType.MOBILE) {
			throw new UnSupportedOperationException();
		}
		return vendorUserRepo.find("SELECT * FROM vendor_user WHERE " + getType.getName() + " = ?", value).get();
	}

	@Override
	public Page<VendorUser> getVendorUserList(Pageable p, long vendorId, Collection<VendorUserStatus> statuss,
			UserType userType) {
		Params params = Params.param("vendorId", vendorId);
		if (null != statuss) {
			List<Integer> st = new ArrayList<Integer>(statuss.size());
			for (VendorUserStatus status : statuss) {
				st.add(status.getValue());
			}
			params.put("status", st);
		}
		if (null != userType) {
			params.put("userType", userType.getValue());
		}
		return vendorUserRepo.find("$getVendorUserR", params).fetch(p);
	}

	@Override
	public long getVendorUserCountByUserType(long vendorId, UserType userType) {
		Params params = Params.param("vendorId", vendorId);
		if (null != userType) {
			params.put("userType", userType.getValue());
		}
		return vendorUserRepo.find("$getVendorUserCountR", params.put("status", VendorUserStatus.ENABLED.getValue()))
				.count();
	}

	@Override
	public long getVendorUserCountByStatus(long vendorId, VendorUserStatus status) {
		Params params = Params.param("vendorId", vendorId);
		if (null != status) {
			params.put("status", status.getValue());
		}
		return vendorUserRepo.find("$getVendorUserCountR", params).count();
	}

	@Override
	public List<VendorUser> listVendorUsers(long vendorId) {
		return this.listVendorUsers(vendorId, null);
	}

	@Override
	public List<VendorUser> listVendorUsers(long vendorId, Collection<UserType> userTypes) {
		Params params = Params.param("vendorId", vendorId);
		params.put("status", VendorUserStatus.ENABLED.getValue());
		if (null != userTypes) {
			List<Integer> uts = new ArrayList<Integer>(userTypes.size());
			for (UserType userType : userTypes) {
				uts.add(userType.getValue());
			}
			params.put("userTypes", uts);
		}
		return vendorUserRepo.find("$getVendorUser", params).list();
	}

	@Override
	public List<VendorUser> getVendorUserListByUserType(long vendorId, UserType userType) {
		Params params = Params.param("vendorId", vendorId);
		if (null != userType) {
			params.put("userType", userType.getValue());
		}
		return vendorUserRepo.find("$getVendorUserR", params.put("status", VendorUserStatus.ENABLED.getValue())).list();
	}
}
