package com.lanking.uxb.service.code.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.District;
import com.lanking.cloud.domain.common.baseData.SchoolType;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.DistrictService;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.convert.DistrictConvert;
import com.lanking.uxb.service.code.value.VDistrict;

/**
 * 地区相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月13日
 */
@RestController
@RequestMapping("common/district")
public class DistrictController {

	@Autowired
	private DistrictService districtService;
	@Autowired
	private DistrictConvert districtConvert;
	@Autowired
	private SchoolService schoolService;

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(@RequestParam(value = "pcode", required = false) Long pcode,
			@RequestParam(value = "level", defaultValue = "1") int level) {
		if (null != pcode) {
			return new Value(districtService.getDistrictByPcode(pcode));
		}
		return new Value(districtService.getDistrictByLevel(level));
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "get_district_tree", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getDistrictTree() {
		List<District> districtList = districtService.findAll();
		List<VDistrict> vDistricts = districtConvert.to(districtList);
		vDistricts = districtConvert.assembleDistrictTree(vDistricts);

		return new Value(vDistricts);
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "get_district_school", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getDistrictSchool(Long districtCode,
			@RequestParam(value = "type", required = false) SchoolType schoolType) {
		return new Value(schoolService.findSchoolByDistrictCode(districtCode, schoolType));
	}
}
