package com.lanking.uxb.zycon.user.resource;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.District;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.baseData.SchoolType;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.code.api.DistrictService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.zycon.user.api.ZycUserSchoolService;
import com.lanking.uxb.zycon.user.convert.ZycUserSchoolConvert;
import com.lanking.uxb.zycon.user.form.SchoolForm;
import com.lanking.uxb.zycon.user.value.VZycSchool;

/**
 * 用户管理 --->学校管理
 * 
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("zyc/userSchool")
public class ZycUserSchoolController {

	@Autowired
	private ZycUserSchoolConvert schoolConvert;
	@Autowired
	private ZycUserSchoolService schoolService;
	@Autowired
	private DistrictService districtService;

	/**
	 * 查询学校列表
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "querySchoolList")
	public Value querySchoolList(SchoolForm query) {
		Page<School> page = schoolService.query(query, P.index(query.getPage(), query.getPageSize()));
		VPage<VZycSchool> vp = new VPage<VZycSchool>();
		int tPage = (int) (page.getTotalCount() + query.getPageSize() - 1) / query.getPageSize();
		vp.setPageSize(query.getPageSize());
		vp.setCurrentPage(query.getPage());
		vp.setTotalPage(tPage);
		vp.setTotal(page.getTotalCount());
		vp.setItems(schoolConvert.to(page.getItems()));
		return new Value(vp);
	}

	/**
	 * 添加或编辑学校
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "saveSchool")
	public Value saveSchool(SchoolForm form) {
		schoolService.saveSchool(form);
		return new Value();
	}

	/**
	 * 获取学校信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getSchool")
	public Value getSchool(Long id) {
		Map<String, Object> data = new HashMap<String, Object>();
		School school = schoolService.getSchool(id);
		data.put("schoolName", school.getName());
		data.put("phaseCode", school.getType() == SchoolType.HIGH ? PhaseService.PHASE_HIGH : PhaseService.PHASE_MIDDLE);
		District d = districtService.getDistrict(school.getDistrictCode());
		District d1 = districtService.getDistrict(d.getPcode());
		// 说明是直辖市的学校
		if (d1.getLevel() == 1) {
			data.put("provinceCode", d1.getCode());
			data.put("cityCode", school.getDistrictCode());
		} else if (d1.getLevel() == 2) {
			data.put("areaCode", school.getDistrictCode());
			data.put("cityCode", d1.getCode());
			District d2 = districtService.getDistrict(d1.getPcode());
			data.put("provinceCode", d2.getCode());
		}
		return new Value(data);
	}

	/**
	 * 将修改后的学校应用到系统
	 * 
	 * @return
	 */
	@RequestMapping(value = "syncData")
	public Value syncData() {
		schoolService.syncData();
		return new Value();
	}
}
