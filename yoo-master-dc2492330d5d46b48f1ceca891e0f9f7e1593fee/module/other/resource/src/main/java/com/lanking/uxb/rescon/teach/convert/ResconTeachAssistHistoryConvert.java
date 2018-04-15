package com.lanking.uxb.rescon.teach.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistHistory;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.teach.value.VTeachAssistHistory;

@Component
public class ResconTeachAssistHistoryConvert extends Converter<VTeachAssistHistory, TeachAssistHistory, Long> {

	@Autowired
	private ResconVendorUserManage vendorUserManage;

	@Override
	protected Long getId(TeachAssistHistory s) {
		return s.getId();
	}

	@Override
	protected VTeachAssistHistory convert(TeachAssistHistory s) {
		VTeachAssistHistory v = new VTeachAssistHistory();
		v.setTeachAssistId(s.getTeachAssistId());
		v.setType(s.getType());
		v.setVersion(s.getVersion());
		v.setCreateAt(s.getCreateAt());
		return v;
	}

	@SuppressWarnings("rawtypes")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {

		// 获取创建人
		assemblers.add(new ConverterAssembler<VTeachAssistHistory, TeachAssistHistory, Long, VendorUser>() {

			@Override
			public boolean accept(TeachAssistHistory s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(TeachAssistHistory s, VTeachAssistHistory d) {
				return s.getCreateId();
			}

			@Override
			public void setValue(TeachAssistHistory s, VTeachAssistHistory d, VendorUser value) {
				d.setCreator(value.getRealName() == null ? value.getName() : value.getRealName());

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
