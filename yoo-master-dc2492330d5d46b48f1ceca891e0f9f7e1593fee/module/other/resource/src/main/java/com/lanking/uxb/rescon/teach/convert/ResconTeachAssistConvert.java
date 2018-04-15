package com.lanking.uxb.rescon.teach.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssist;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.teach.value.VTeachAssist;

@Component
public class ResconTeachAssistConvert extends Converter<VTeachAssist, TeachAssist, Long> {
	@Autowired
	private ResconVendorUserManage vendorUserManage;

	@Override
	protected Long getId(TeachAssist s) {
		return s.getId();
	}

	@Override
	protected VTeachAssist convert(TeachAssist s) {
		VTeachAssist v = new VTeachAssist();
		v.setId(s.getId());
		v.setCreateAt(s.getCreateAt());
		return v;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VTeachAssist, TeachAssist, Long, VendorUser>() {
			@Override
			public boolean accept(TeachAssist s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(TeachAssist s, VTeachAssist d) {
				return s.getCreateId();
			}

			@Override
			public void setValue(TeachAssist s, VTeachAssist d, VendorUser value) {
				d.setCreator(value);

			}

			@Override
			public VendorUser getValue(Long key) {
				return vendorUserManage.getVendorUser(key);
			}

			@Override
			public Map<Long, VendorUser> mgetValue(Collection<Long> keys) {
				return vendorUserManage.mgetVendorUser(keys);
			}

		});

	}

}
