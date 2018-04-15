package com.lanking.uxb.service.code.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.District;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.api.DistrictService;
import com.lanking.uxb.service.code.value.VDistrict;

@Component
public class DistrictConvert extends Converter<VDistrict, District, Long> {

	@Autowired
	private DistrictService districtService;

	@Override
	protected Long getId(District s) {
		return s.getCode();
	}

	@Override
	public VDistrict convert(District s) {
		VDistrict vd = new VDistrict();
		vd.setCode((s.getCode()));
		vd.setName(s.getName());
		vd.setLevel(s.getLevel());
		vd.setPcode(s.getPcode());
		return vd;
	}

	private void internalAssembleDistrictTree(List<VDistrict> districts, VDistrict v) {
		if (v.getLevel() == 1) {
			districts.add(v);
		} else {
			for (VDistrict vd : districts) {
				if (vd.getCode() == v.getPcode()) {
					vd.getChildren().add(v);
					break;
				} else {
					internalAssembleDistrictTree(vd.getChildren(), v);
				}
			}
		}
	}

	public List<VDistrict> assembleDistrictTree(List<VDistrict> districts) {
		List<VDistrict> to = Lists.newArrayList();
		for (VDistrict v : districts) {
			internalAssembleDistrictTree(to, v);
		}
		return to;
	}

	@Override
	protected District internalGet(Long id) {
		return districtService.getDistrict(id);
	}

	@Override
	protected Map<Long, District> internalMGet(Collection<Long> ids) {
		return districtService.mget(ids);
	}
}
