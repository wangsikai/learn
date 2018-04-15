package com.lanking.uxb.rescon.basedata.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTraining;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.basedata.value.VSpecialTraining;

/**
 * 针对性训练操作日志转化
 * 
 * @author wangsenhao
 *
 */
@Component
public class ResconSpecialTrainingConvert extends Converter<VSpecialTraining, SpecialTraining, Long> {
	@Autowired
	private ResconVendorUserManage vendorUserManage;

	@Override
	protected Long getId(SpecialTraining s) {
		return s.getId();
	}

	@Override
	protected VSpecialTraining convert(SpecialTraining s) {
		VSpecialTraining v = new VSpecialTraining();
		v.setName(s.getName());
		v.setDifficulty(s.getDifficulty());
		v.setId(s.getId());
		v.setCreateAt(s.getCreateAt());
		v.setStatus(s.getStatus());
		v.setDifficulty(s.getDifficulty());
		v.setKnowpointCode(s.getKnowpointCode());
		return v;
	}

	@SuppressWarnings("rawtypes")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {

		assemblers.add(new ConverterAssembler<VSpecialTraining, SpecialTraining, Long, VendorUser>() {

			@Override
			public boolean accept(SpecialTraining s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(SpecialTraining s, VSpecialTraining d) {
				return s.getCreateId();
			}

			@Override
			public void setValue(SpecialTraining s, VSpecialTraining d, VendorUser value) {
				d.setCreateUser(value.getRealName() == null ? value.getName() : value.getRealName());

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
