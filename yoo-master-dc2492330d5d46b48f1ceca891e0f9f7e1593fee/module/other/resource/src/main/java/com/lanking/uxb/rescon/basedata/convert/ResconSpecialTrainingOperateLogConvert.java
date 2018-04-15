package com.lanking.uxb.rescon.basedata.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingOperateLog;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.basedata.value.VSpecialTrainingOperateLog;

/**
 * 针对性训练操作日志转化
 * 
 * @author wangsenhao
 *
 */
@Component
public class ResconSpecialTrainingOperateLogConvert extends
		Converter<VSpecialTrainingOperateLog, SpecialTrainingOperateLog, Long> {
	@Autowired
	private ResconVendorUserManage vendorUserManage;

	@Override
	protected Long getId(SpecialTrainingOperateLog s) {
		return s.getId();
	}

	@Override
	protected VSpecialTrainingOperateLog convert(SpecialTrainingOperateLog s) {
		VSpecialTrainingOperateLog v = new VSpecialTrainingOperateLog();
		v.setCreateAt(s.getCreateAt());
		v.setOperateType(s.getOperateType());
		return v;
	}

	@SuppressWarnings("rawtypes")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {

		assemblers
				.add(new ConverterAssembler<VSpecialTrainingOperateLog, SpecialTrainingOperateLog, Long, VendorUser>() {

					@Override
					public boolean accept(SpecialTrainingOperateLog s) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(SpecialTrainingOperateLog s, VSpecialTrainingOperateLog d) {
						return s.getCreateId();
					}

					@Override
					public void setValue(SpecialTrainingOperateLog s, VSpecialTrainingOperateLog d, VendorUser value) {
						d.setUserName(value.getRealName() == null ? value.getName() : value.getRealName());

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
