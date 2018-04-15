package com.lanking.uxb.rescon.account.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.resources.vendor.Vendor;
import com.lanking.uxb.rescon.account.api.ResconVendorManage;
import com.lanking.uxb.service.code.api.DistrictService;

@Transactional(readOnly = true)
@Service
public class ResconVendorManageImpl implements ResconVendorManage {

	@Autowired
	@Qualifier("VendorRepo")
	private Repo<Vendor, Long> vendorRepo;

	@Autowired
	private DistrictService districtService;

	@Override
	public Vendor get(long id) {
		return vendorRepo.get(id);
	}
}
