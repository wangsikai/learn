package com.lanking.uxb.service.code.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.api.DistrictService;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.value.VSchool;

@Component
public class SchoolConvert extends Converter<VSchool, School, Long> {

	@Autowired
	private DistrictService districtService;
	@Autowired
	private SchoolService schoolService;

	@Override
	protected Long getId(School s) {
		return s.getId();
	}

	@Override
	protected VSchool convert(School s) {
		VSchool v = new VSchool();
		v.setId(s.getId());
		v.setType(s.getType());
		v.setName(s.getName());
		v.setDistrictCode(s.getDistrictCode());
		return v;
	}

	@Override
	protected School internalGet(Long id) {
		return schoolService.get(id);
	}

	@Override
	protected Map<Long, School> internalMGet(Collection<Long> ids) {
		return schoolService.mget(ids);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VSchool, School, Long, String>() {
			@Override
			public boolean accept(School s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(School s, VSchool d) {
				return s.getDistrictCode();
			}

			@Override
			public void setValue(School s, VSchool d, String value) {
				if (StringUtils.isBlank(value)) {
					d.setDistrictName(StringUtils.EMPTY);
				} else {
					d.setDistrictName(value);
				}
			}

			@Override
			public String getValue(Long key) {
				return districtService.getDistrictName(key);
			}

			@Override
			public Map<Long, String> mgetValue(Collection<Long> keys) {
				return districtService.mgetDistrictName(keys);
			}

		});
	}

}
